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
package com.telefonica.claudia.slm.eventsBus;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent;
import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent.SubcommandType;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;


/**
 * Intermediate class used to issue commands to the running version of the SLM. When it is started
 * the SLM keeps listening to a JMS QUEUE for events, issued from any other component
 * of Claudia. This class creates and send Administration JMS events to change the SLM configuration
 * on line.
 * 
 * 
 * @author daniel
 */
public class AdministrationConnector {

	private static Logger log = Logger.getLogger("com.telefonica.claudia.slm.AdministrationConnector");

	private static final int EVENT_BUS_TIMEOUT=15*60*1000;
	
    private Queue administrationRequest = null;
    private Queue administrationReply = null;

    private MessageProducer administrationProducer = null; 
    
    private static final String DEFAULT_CONN_FACTORY_NAME = "ConnectionFactory"; 
    
    private Session session = null;

    Context initialContext = null;

    private Connection connection = null;
	
    public AdministrationConnector() throws NamingException, JMSException { 

        try {
        	cleanQueues();
        	openJMSSession();
        	
	        administrationProducer = session.createProducer(administrationRequest);	

		} catch (JMSException e) {
			log.error("The connection to the bus could not be created. ");
			throw e;
		}
    }
    
    @SuppressWarnings("unchecked")
	private void cleanQueues() {
    	log.info("Clean the queues from lost replys. ");

    	if (session== null) return;
    	
    	try {
			QueueBrowser qb = session.createBrowser(administrationRequest);
			
			Enumeration e = qb.getEnumeration();
			
			MessageConsumer messageConsumer = session.createConsumer(administrationRequest);

			int n=0;
			while (e.hasMoreElements()) {
				e.nextElement();
				n++;
			}
			
			for (int i =0; i < n; i++) {
				log.info("Deleting pending messages...");
				messageConsumer.receive(EVENT_BUS_TIMEOUT);
			}
			
			messageConsumer.close();
			
			try { Thread.sleep(5000); } catch (InterruptedException ex) { }
			
		} catch (JMSException e) {
			log.warn("Exception cleaning the queue. Execution might continue without errors: " + e.getMessage());
		}
	}

   
    public void finish() throws JMSException {    	   	
    	
    	AdministrativeEvent admEv = new AdministrativeEvent(System.currentTimeMillis(), 0, AdministrativeEvent.CommandType.STOP);
		
		administrationProducer.send(session.createObjectMessage(admEv));
    }
    
	public String getStatus() throws JMSException {
		
		AdministrativeEvent admEv = new AdministrativeEvent(System.currentTimeMillis(), 0, AdministrativeEvent.CommandType.STATUS);
		
    	try {
			administrationProducer.send(session.createObjectMessage(admEv));
			
		} catch (JMSException e) {
			log.error("SMPlugin: Communication error accessing the Service Manager. Impossible to retrieve service data.",e);
		}
		
		// Wait until the Service is deployed.
		MessageConsumer messageConsumer = session.createConsumer(administrationReply, null);
		ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
		
		if (m==null) {
			log.error("Timeout waiting for a server response. Try later.");
			
			return "Timeout waiting for a server response. Try later.";
		}
		
		admEv = (AdministrativeEvent) m.getObject();
		String status= admEv.getPayload();
		
		messageConsumer.close();
		
		return status;
	}
    
	public void createNetworkRange(String description) throws JMSException {
    	AdministrativeEvent admEv = new AdministrativeEvent(System.currentTimeMillis(), 0, AdministrativeEvent.CommandType.NETWORKRANGE);
    	admEv.setSubcommand(SubcommandType.CREATE);
    	admEv.setPayload(description);
		
		administrationProducer.send(session.createObjectMessage(admEv));
	}
	
	public void removeNetworkRange(String networkName) throws JMSException {
    	AdministrativeEvent admEv = new AdministrativeEvent(System.currentTimeMillis(), 0, AdministrativeEvent.CommandType.NETWORKRANGE);
    	admEv.setSubcommand(SubcommandType.REMOVE);
    	admEv.setPayload(networkName);
    	
		administrationProducer.send(session.createObjectMessage(admEv));
	}
	
    private void openJMSSession() throws NamingException, JMSException {
        
        log.info("Getting initial JNDI context");
        initialContext = new InitialContext(SMConfiguration.getInstance().getJNDIEnv());          
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(DEFAULT_CONN_FACTORY_NAME);
        
        log.info("Creating connection to bus");
        connection = connectionFactory.createConnection();
        connection.start();
        
        log.info("Creating session");              
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        log.info("Getting queue for event type " + EventType.ADMINISTRATIVE_EVENT);
        administrationRequest = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.ADMINISTRATIVE_EVENT, false));
        administrationReply = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.ADMINISTRATIVE_EVENT, true));
    }
}
