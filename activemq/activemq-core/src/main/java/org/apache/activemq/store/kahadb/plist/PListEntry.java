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
package org.apache.activemq.store.kahadb.plist;

import org.apache.kahadb.util.ByteSequence;

public class PListEntry {

    private final ByteSequence byteSequence;
    private final EntryLocation entry;

    PListEntry(EntryLocation entry, ByteSequence bs) {
        this.entry = entry;
        this.byteSequence = bs;
    }

    /**
     * @return the byteSequence
     */
    public ByteSequence getByteSequence() {
        return this.byteSequence;
    }

    public String getId() {
        return this.entry.getId();
    }

    /**
     * @return the entry
     */
    EntryLocation getEntry() {
        return this.entry;
    }

    public PListEntry copy() {
        return new PListEntry(this.entry, this.byteSequence);
    }

    @Override
    public String toString() {
        return this.entry.getId() + "[pageId=" + this.entry.getPage().getPageId() + ",next=" + this.entry.getNext()
                + "]";
    }

}
