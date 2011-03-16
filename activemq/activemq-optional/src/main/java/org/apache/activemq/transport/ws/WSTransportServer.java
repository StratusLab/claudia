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

package org.apache.activemq.transport.ws;

import java.net.InetSocketAddress;
import java.net.URI;

import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.transport.TransportServerSupport;
import org.apache.activemq.util.ServiceStopper;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;

/**
 * Creates a web server and registers web socket server
 *
 */
public class WSTransportServer extends TransportServerSupport {
    
    private URI bindAddress;
    private Server server;
    private Connector connector;
    
    public WSTransportServer(URI location) {
        super(location);
        this.bindAddress = location;
    }

    protected void doStart() throws Exception {
        server = new Server();
        if (connector == null) {
            connector = new SocketConnector();
        }
        connector.setHost(bindAddress.getHost());
        connector.setPort(bindAddress.getPort());
        server.setConnectors(new Connector[] {
                connector
        });
        
        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setServer(server);
        server.setHandler(contextHandler);

        SessionHandler sessionHandler = new SessionHandler();
        contextHandler.setHandler(sessionHandler);

        ServletHandler servletHandler = new ServletHandler();
        sessionHandler.setHandler(servletHandler);
        
        ServletHolder holder = new ServletHolder();
        holder.setName("WSStomp");
        holder.setClassName(StompServlet.class.getName());
        servletHandler.setServlets(new ServletHolder[] {
            holder
        });

        ServletMapping mapping = new ServletMapping();
        mapping.setServletName("WSStomp");
        mapping.setPathSpec("/*");
        servletHandler.setServletMappings(new ServletMapping[] {
            mapping
        });

        contextHandler.setAttribute("acceptListener", getAcceptListener());
        
        server.start();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        Server temp = server;
        server = null;
        if (temp != null) {
            temp.stop();
        }
    }

    public InetSocketAddress getSocketAddress() {
        return null;
    }

    public void setBrokerInfo(BrokerInfo brokerInfo) {
    }

}
