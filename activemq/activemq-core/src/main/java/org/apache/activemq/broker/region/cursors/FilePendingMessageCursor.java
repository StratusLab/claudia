/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.broker.region.cursors;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.broker.region.QueueMessageReference;
import org.apache.activemq.command.Message;
import org.apache.activemq.filter.NonCachedMessageEvaluationContext;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.store.kahadb.plist.PList;
import org.apache.activemq.store.kahadb.plist.PListEntry;
import org.apache.activemq.store.kahadb.plist.PListStore;
import org.apache.activemq.usage.SystemUsage;
import org.apache.activemq.usage.Usage;
import org.apache.activemq.usage.UsageListener;
import org.apache.activemq.wireformat.WireFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kahadb.util.ByteSequence;

/**
 * persist pending messages pending message (messages awaiting dispatch to a
 * consumer) cursor
 * 
 * @version $Revision: 963118 $
 */
public class FilePendingMessageCursor extends AbstractPendingMessageCursor implements UsageListener {
    static final Log LOG = LogFactory.getLog(FilePendingMessageCursor.class);
    private static final AtomicLong NAME_COUNT = new AtomicLong();
    protected Broker broker;
    private final PListStore store;
    private final String name;
    private LinkedList<MessageReference> memoryList = new LinkedList<MessageReference>();
    private PList diskList;
    private Iterator<MessageReference> iter;
    private Destination regionDestination;
    private boolean iterating;
    private boolean flushRequired;
    private final AtomicBoolean started = new AtomicBoolean();
    private final WireFormat wireFormat = new OpenWireFormat();
    /**
     * @param broker
     * @param name
     * @param prioritizedMessages 
     * @param store
     */
    public FilePendingMessageCursor(Broker broker, String name, boolean prioritizedMessages) {
        super(prioritizedMessages);
        this.useCache = false;
        this.broker = broker;
        // the store can be null if the BrokerService has persistence
        // turned off
        this.store = broker.getTempDataStore();
        this.name = NAME_COUNT.incrementAndGet() + "_" + name;
    }

    @Override
    public void start() throws Exception {
        if (started.compareAndSet(false, true)) {
            super.start();
            if (systemUsage != null) {
                systemUsage.getMemoryUsage().addUsageListener(this);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        if (started.compareAndSet(true, false)) {
            super.stop();
            if (systemUsage != null) {
                systemUsage.getMemoryUsage().removeUsageListener(this);
            }
        }
    }

    /**
     * @return true if there are no pending messages
     */
    @Override
    public synchronized boolean isEmpty() {
        if (memoryList.isEmpty() && isDiskListEmpty()) {
            return true;
        }
        for (Iterator<MessageReference> iterator = memoryList.iterator(); iterator.hasNext();) {
            MessageReference node = iterator.next();
            if (node == QueueMessageReference.NULL_MESSAGE) {
                continue;
            }
            if (!node.isDropped()) {
                return false;
            }
            // We can remove dropped references.
            iterator.remove();
        }
        return isDiskListEmpty();
    }

    /**
     * reset the cursor
     */
    @Override
    public synchronized void reset() {
        iterating = true;
        last = null;
        if (isDiskListEmpty()) {
            this.iter = this.memoryList.iterator();
        } else {
            this.iter = new DiskIterator();
        }
    }

    @Override
    public synchronized void release() {
        iterating = false;
        if (flushRequired) {
            flushRequired = false;
            flushToDisk();
        }
    }

    @Override
    public synchronized void destroy() throws Exception {
        stop();
        for (Iterator<MessageReference> i = memoryList.iterator(); i.hasNext();) {
            Message node = (Message) i.next();
            node.decrementReferenceCount();
        }
        memoryList.clear();
        destroyDiskList();
    }

    private void destroyDiskList() throws Exception {
        if (!isDiskListEmpty()) {
            store.removePList(name);
        }
    }

    @Override
    public synchronized LinkedList<MessageReference> pageInList(int maxItems) {
        LinkedList<MessageReference> result = new LinkedList<MessageReference>();
        int count = 0;
        for (Iterator<MessageReference> i = memoryList.iterator(); i.hasNext() && count < maxItems;) {
            MessageReference ref = i.next();
            ref.incrementReferenceCount();
            result.add(ref);
            count++;
        }
        if (count < maxItems && !isDiskListEmpty()) {
            for (Iterator<MessageReference> i = new DiskIterator(); i.hasNext() && count < maxItems;) {
                Message message = (Message) i.next();
                message.setRegionDestination(regionDestination);
                message.setMemoryUsage(this.getSystemUsage().getMemoryUsage());
                message.incrementReferenceCount();
                result.add(message);
                count++;
            }
        }
        return result;
    }

    /**
     * add message to await dispatch
     * 
     * @param node
     * @throws Exception 
     */
    @Override
    public synchronized void addMessageLast(MessageReference node) throws Exception {
        tryAddMessageLast(node, 0);
    }
    
    @Override
    public synchronized boolean tryAddMessageLast(MessageReference node, long maxWaitTime) throws Exception {
        if (!node.isExpired()) {
            try {
                regionDestination = node.getMessage().getRegionDestination();
                if (isDiskListEmpty()) {
                    if (hasSpace() || this.store == null) {
                        memoryList.add(node);
                        node.incrementReferenceCount();
                        return true;
                    }
                }
                if (!hasSpace()) {
                    if (isDiskListEmpty()) {
                        expireOldMessages();
                        if (hasSpace()) {
                            memoryList.add(node);
                            node.incrementReferenceCount();
                            return true;
                        } else {
                            flushToDisk();
                        }
                    }
                }
                if (systemUsage.getTempUsage().waitForSpace(maxWaitTime)) {
                    ByteSequence bs = getByteSequence(node.getMessage());
                    getDiskList().addLast(node.getMessageId().toString(), bs);
                    return true;
                }
                return false;

            } catch (Exception e) {
                LOG.error("Caught an Exception adding a message: " + node + " first to FilePendingMessageCursor ", e);
                throw new RuntimeException(e);
            }
        } else {
            discard(node);
        }
        //message expired
        return true;
    }

    /**
     * add message to await dispatch
     * 
     * @param node
     */
    @Override
    public synchronized void addMessageFirst(MessageReference node) {
        if (!node.isExpired()) {
            try {
                regionDestination = node.getMessage().getRegionDestination();
                if (isDiskListEmpty()) {
                    if (hasSpace()) {
                        memoryList.addFirst(node);
                        node.incrementReferenceCount();
                        return;
                    }
                }
                if (!hasSpace()) {
                    if (isDiskListEmpty()) {
                        expireOldMessages();
                        if (hasSpace()) {
                            memoryList.addFirst(node);
                            node.incrementReferenceCount();
                            return;
                        } else {
                            flushToDisk();
                        }
                    }
                }
                systemUsage.getTempUsage().waitForSpace();
                node.decrementReferenceCount();
                ByteSequence bs = getByteSequence(node.getMessage());
                getDiskList().addFirst(node.getMessageId().toString(), bs);

            } catch (Exception e) {
                LOG.error("Caught an Exception adding a message: " + node + " first to FilePendingMessageCursor ", e);
                throw new RuntimeException(e);
            }
        } else {
            discard(node);
        }
    }

    /**
     * @return true if there pending messages to dispatch
     */
    @Override
    public synchronized boolean hasNext() {
        return iter.hasNext();
    }

    /**
     * @return the next pending message
     */
    @Override
    public synchronized MessageReference next() {
        Message message = (Message) iter.next();
        last = message;
        if (!isDiskListEmpty()) {
            // got from disk
            message.setRegionDestination(regionDestination);
            message.setMemoryUsage(this.getSystemUsage().getMemoryUsage());
        }
        message.incrementReferenceCount();
        return message;
    }

    /**
     * remove the message at the cursor position
     */
    @Override
    public synchronized void remove() {
        iter.remove();
        if (last != null) {
            last.decrementReferenceCount();
        }
    }

    /**
     * @param node
     * @see org.apache.activemq.broker.region.cursors.AbstractPendingMessageCursor#remove(org.apache.activemq.broker.region.MessageReference)
     */
    @Override
    public synchronized void remove(MessageReference node) {
        if (memoryList.remove(node)) {
            node.decrementReferenceCount();
        }
        if (!isDiskListEmpty()) {
            try {
                getDiskList().remove(node.getMessageId().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @return the number of pending messages
     */
    @Override
    public synchronized int size() {
        return memoryList.size() + (isDiskListEmpty() ? 0 : getDiskList().size());
    }

    /**
     * clear all pending messages
     */
    @Override
    public synchronized void clear() {
        memoryList.clear();
        if (!isDiskListEmpty()) {
            try {
                getDiskList().destroy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        last = null;
    }

    @Override
    public synchronized boolean isFull() {

        return super.isFull() || (systemUsage != null && systemUsage.getTempUsage().isFull());

    }

    @Override
    public boolean hasMessagesBufferedToDeliver() {
        return !isEmpty();
    }

    @Override
    public void setSystemUsage(SystemUsage usageManager) {
        super.setSystemUsage(usageManager);
    }

    public void onUsageChanged(Usage usage, int oldPercentUsage, int newPercentUsage) {
        if (newPercentUsage >= getMemoryUsageHighWaterMark()) {
            synchronized (this) {
                flushRequired = true;
                if (!iterating) {
                    expireOldMessages();
                    if (!hasSpace()) {
                        flushToDisk();
                        flushRequired = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean isTransient() {
        return true;
    }

    protected boolean isSpaceInMemoryList() {
        return hasSpace() && isDiskListEmpty();
    }

    protected synchronized void expireOldMessages() {
        if (!memoryList.isEmpty()) {
            LinkedList<MessageReference> tmpList = new LinkedList<MessageReference>(this.memoryList);
            this.memoryList = new LinkedList<MessageReference>();
            while (!tmpList.isEmpty()) {
                MessageReference node = tmpList.removeFirst();
                if (node.isExpired()) {
                    discard(node);
                } else {
                    memoryList.add(node);
                }
            }
        }

    }

    protected synchronized void flushToDisk() {

        if (!memoryList.isEmpty()) {
            while (!memoryList.isEmpty()) {
                MessageReference node = memoryList.removeFirst();
                node.decrementReferenceCount();
                ByteSequence bs;
                try {
                    bs = getByteSequence(node.getMessage());
                    getDiskList().addLast(node.getMessageId().toString(), bs);
                } catch (IOException e) {
                    LOG.error("Failed to write to disk list", e);
                    throw new RuntimeException(e);
                }

            }
            memoryList.clear();
        }
    }

    protected boolean isDiskListEmpty() {
        return diskList == null || diskList.isEmpty();
    }

    protected PList getDiskList() {
        if (diskList == null) {
            try {
                diskList = store.getPList(name);
            } catch (Exception e) {
                LOG.error("Caught an IO Exception getting the DiskList " + name, e);
                throw new RuntimeException(e);
            }
        }
        return diskList;
    }

    protected void discard(MessageReference message) {
        message.decrementReferenceCount();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Discarding message " + message);
        }
        broker.getRoot().sendToDeadLetterQueue(new ConnectionContext(new NonCachedMessageEvaluationContext()), message);
    }

    protected ByteSequence getByteSequence(Message message) throws IOException {
        org.apache.activemq.util.ByteSequence packet = wireFormat.marshal(message);
        return new ByteSequence(packet.data, packet.offset, packet.length);
    }

    protected Message getMessage(ByteSequence bs) throws IOException {
        org.apache.activemq.util.ByteSequence packet = new org.apache.activemq.util.ByteSequence(bs.getData(), bs
                .getOffset(), bs.getLength());
        return (Message) this.wireFormat.unmarshal(packet);

    }

    final class DiskIterator implements Iterator<MessageReference> {
        private PListEntry next = null;
        private PListEntry current = null;
        PList list;

        DiskIterator() {
            try {
                this.list = getDiskList();
                synchronized (this.list) {
                    this.current = this.list.getFirst();
                    this.next = this.current;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public MessageReference next() {
            this.current = next;
            try {
                ByteSequence bs = this.current.getByteSequence();
                synchronized (this.list) {
                    this.current = this.list.refresh(this.current);
                    this.next = this.list.getNext(this.current);
                }
                return getMessage(bs);
            } catch (IOException e) {
                LOG.error("I/O error", e);
                throw new RuntimeException(e);
            }
        }

        public void remove() {
            try {
                synchronized (this.list) {
                    this.current = this.list.refresh(this.current);
                    this.list.remove(this.current);
                }

            } catch (IOException e) {
                LOG.error("I/O error", e);
                throw new RuntimeException(e);
            }

        }

    }
}
