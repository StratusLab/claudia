/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.monitoring;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.restlet.Component;
import org.restlet.data.Protocol;

import com.telefonica.claudia.slm.common.SMConfiguration;


public class MonitoringRestBusConnector {
    
    private static Logger logger = Logger.getLogger(MonitoringRestBusConnector.class);
    
    //private static final int DEFAULT_REST_PORT = SMProperties.getInstance().getRestServerPort();
    private static final String DEFAULT_REST_PATH = "/vmi";
    
    private static MonitoringRestBusConnector _instance = null;
    
    private MonitoringBusForwarder monitoringDataForwarder = null;
    private boolean running = false;
    
    public static String SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS = "reservoir.sm.monitor";
    
    private Component component = null;
    private String host = null;
    private int port = -1;
    private String monitoringPath = null;
    
    static {    	
    	Logger.getLogger("org.safehaus.asyncweb.http.internal.Request").setLevel(Level.ERROR);
    	Logger.getLogger("org.safehaus.asyncweb.http.internal.Request").setAdditivity(false);
    	Logger.getLogger("org.safehaus.asyncweb.transport.nio.HttpIOHandler").setLevel(Level.ERROR);
    	Logger.getLogger("org.safehaus.asyncweb.transport.nio.HttpIOHandler").setAdditivity(false);
    	Logger.getLogger("org.safehaus.asyncweb.transport.nio.NIOTransport").setLevel(Level.ERROR);
    	Logger.getLogger("org.safehaus.asyncweb.transport.nio.NIOTransport").setAdditivity(false);
    	Logger.getLogger("org.safehaus.asyncweb.http.codec.HttpRequestParser").setLevel(Level.ERROR);
    	Logger.getLogger("org.safehaus.asyncweb.http.codec.HttpRequestParser").setAdditivity(false);
    	Logger.getLogger("com.noelios.restlet.LogFilter").setLevel(Level.ERROR);
    	Logger.getLogger("com.noelios.restlet.LogFilter").setAdditivity(false);    	
    	Logger.getLogger("org.apache.activemq").setLevel(Level.INFO);
        Logger.getLogger("es.tid.reservoir.serviceManager.vmiHandler").setLevel(Level.DEBUG); 
        Logger.getLogger("es.tid.reservoir.serviceManager.vmiHandler").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   	
    }
    
    public static void main(String[] args) {
        
        Logger.getLogger("es.tid.reservoir").addAppender(
                new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                                    "System.out"));
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("es.tid.reservoir").setLevel(Level.DEBUG);
    	
    	MonitoringRestBusConnector restBusConnector = new MonitoringRestBusConnector(SMConfiguration.getInstance().getRestServerHost(), SMConfiguration.getInstance().getRestServerPort(), DEFAULT_REST_PATH);

    	try {
        	restBusConnector.start();
    	} catch (JMSException ex) {
    		logger.error("JMSException caught when trying to connect to JMS bus", ex);
    		return;
    	} catch (NamingException ex) {
    		logger.error("NamingException caught when trying to connect to JMS bus", ex);
    		return;
    	}		
    	
    	System.out.println("Press RETURN key to stop the server");
    	try {
			System.in.read();
		} catch (IOException e) {
		}
    	
    	restBusConnector.stop();

    	System.out.println("Done");
    }
    
    public static MonitoringRestBusConnector getInstance() {
    	if(_instance == null)
    		_instance = new MonitoringRestBusConnector();
    	return _instance;
    }
    
    private MonitoringRestBusConnector() {
    	this(SMConfiguration.getInstance().getRestServerHost(), SMConfiguration.getInstance().getRestServerPort(), DEFAULT_REST_PATH);
    }
    
    private MonitoringRestBusConnector(String host, int port, String monitoringPath){
    	
    	if(host == null)
    		throw new IllegalArgumentException("Inet address cannot be null");
		
		if(monitoringPath == null)
			throw new IllegalArgumentException("Path of rest server cannot be null");
		
		if(port <= 0)
			throw new IllegalArgumentException("Port must be a positive number");
		
		this.host = host;
		this.port = port;
		this.monitoringPath = monitoringPath;
	}
	
	public void start()  throws JMSException, NamingException {
		
		if(running){
			logger.warn("Rest bus connector already started, ignoring start petition...");
			return;
		}
		
		logger.info("Opening bus connection");
    	
		try {
			monitoringDataForwarder = new MonitoringBusForwarder();
		} catch (JMSException ex) {
			logger.error("JMS exception caught when trying to create bus connector", ex);
			throw ex;
		} catch (NamingException ex) {
			logger.error("Naming exception caught when trying to create bus connector", ex);
			throw ex;
		}
        
        logger.info("Starting REST server");        
        component = new Component();
		
        component.getServers().add(Protocol.HTTP, host, port);
        component.getContext().getAttributes().put(SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS, monitoringDataForwarder);
        component.getDefaultHost().attach(new MonitoringRestletApplication(component.getContext(), monitoringPath));        

        logger.info("Monitoring Restlet Application attached");
        
        // Start the server 
        try {
            component.start();
        } catch (Exception ex) {
            logger.error("Exception caught", ex);
        }

		running = true;

		logger.info("REST server listening at " + host + ":" + port);
        
	}
	
	public void stop() {
		
		if(!running){
			logger.warn("Rest bus connector is not started, ignoring stop petition...");
			return;
		}
		
        logger.info("Stopping REST server");		
		try {	        
			component.stop();
		} catch (Exception ex) {
			logger.error("Exception caught when trying to close Rest server", ex);
		}		
        logger.info("REST server stopped");
		
        logger.info("Closing bus connection");
		monitoringDataForwarder.close();
        logger.info("Bus connection closed");
        
        running = false;
	}
	
	public List<String> getRestEndPoints() {
		
		if(!running) {
			logger.warn("Rest bus connector not running, can not return list of endpoints");
			return null;
		}
       
		List<String> endPoints = new ArrayList<String>();
		
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException ex) {
			logger.error("SocketException caught when trying to get network interfaces of system", ex);
			return null;
		}
		while(netInterfaces.hasMoreElements()){
			NetworkInterface netInterface = netInterfaces.nextElement();
			if(netInterface.getName().equals("lo"))
				continue;
			Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
			while(inetAddresses.hasMoreElements()) {				
				InetAddress inetAddress = inetAddresses.nextElement();
				if(inetAddress instanceof Inet4Address)
					endPoints.add(inetAddress.getHostAddress() + ":" + port+monitoringPath);
			}
		}	
		
		return endPoints;
	}
	
	public boolean isRunning() {
		return running;
	}

}
