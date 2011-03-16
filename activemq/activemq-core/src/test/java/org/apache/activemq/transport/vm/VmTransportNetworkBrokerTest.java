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
package org.apache.activemq.transport.vm;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.network.NetworkConnector;

public class VmTransportNetworkBrokerTest extends TestCase {

    private static final String VM_BROKER_URI = 
        "vm://localhost?create=false";
    
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch gotConnection = new CountDownLatch(1);

    public void testNoThreadLeakWithActiveVMConnection() throws Exception {
        
        BrokerService broker = new BrokerService();
        broker.setDedicatedTaskRunner(true);
        broker.setPersistent(false);
        broker.addConnector("tcp://localhost:61616");
        NetworkConnector networkConnector = broker.addNetworkConnector("static:(tcp://wrongHostname1:61617,tcp://wrongHostname2:61618)?useExponentialBackOff=false");
        networkConnector.setDuplex(true);
        broker.start();
        
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(new URI(VM_BROKER_URI));
        cf.createConnection("system", "manager").start();
        
        // let it settle
        TimeUnit.SECONDS.sleep(5);
        
        int threadCount = Thread.activeCount();
        TimeUnit.SECONDS.sleep(30);
        int threadCountAfterSleep = Thread.activeCount();
        
        assertTrue("Threads are leaking, threadCount=" + threadCount + " threadCountAfterSleep=" + threadCountAfterSleep, 
                threadCountAfterSleep < threadCount + 8);
                
        broker.stop(); 
    }
}
