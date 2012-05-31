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
package com.telefonica.claudia.slm.rulesEngine;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.drools.FactHandle;
import org.drools.WorkingMemory;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.eventsBus.BusListener;
import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.slm.lifecyclemanager.LifecycleController;



public class EventsReceptor implements BusListener {
    
    @SuppressWarnings("unused")
	private FactHandle factHandle=null;
    
    private static Logger logger = Logger.getLogger("Monitoring");
    
    private RulesEngine rulesEngine = null;
    
     private ServiceApplication sap;
    
    private LifecycleController lcc;
    
    private FSM fsm;
    
    static {
    	Logger.getLogger("es.tid.reservoir.serviceManager.rulesEngine").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   
    	Logger.getLogger("es.tid.reservoir.serviceManager.rulesEngine").setLevel(Level.INFO);    	
    }
    
     public EventsReceptor(RulesEngine rulesEngine){
          this.rulesEngine = rulesEngine;
     }
    
    public EventsReceptor(RulesEngine rulesEngine, FSM fsm, ServiceApplication sap, LifecycleController lcc) {
        this.rulesEngine = rulesEngine;
          this.lcc=lcc;
        this.sap=sap;
        this.fsm=fsm;
        
        Thread veehwMeasThread = 
                new Thread(new EventListener(EventType.VEE_HW_MEASUREMENT, this),
                           "Listener thread for " +
                           EventType.VEE_HW_MEASUREMENT + " events");
        Thread agentMeasThread = 
                new Thread(new EventListener(EventType.AGENT_MEASUREMENT, this),
                           "Listener thread for " +
                           EventType.AGENT_MEASUREMENT + " events");
        Thread probeMeasThread = 
                new Thread(new EventListener(EventType.PROBE_MEASUREMENT, this),
                           "Listener thread for " +
                           EventType.PROBE_MEASUREMENT + " events");
        
        veehwMeasThread.start();
        agentMeasThread.start();
        probeMeasThread.start();
        
    }

    public void onEvent(final Event event) {    
        
    	logger.debug("Event reached Rules Engine: " + event.getEventType() + " " + fsm.getStateText());
        
    	if (!fsm.getStateText().equals("RUNNING")) {
    		logger.warn("Measure got when the service is not already deployed");
    		return;
    	}
    	
    	/**
    	 * Rococo engineering: the inline implementation of this interface seems to have one
    	 * and only purpose: code obfuscation. The interface itself it's not the most useful
    	 * software component...
    	 */
       rulesEngine.executeRules(
                new WorkingEnvironmentCallback() {
			public void initEnvironment(WorkingMemory workingMemory) {				
				// Load incoming event into the working memory				
                Actions act=new Actions(lcc,sap,fsm);
                workingMemory.setGlobal("actions", act);   
                
                factHandle=workingMemory.insert(event);
				};
		    }
       );
    }
    
}

    
class EventListener implements Runnable {
    
    private static Logger logger = Logger.getLogger(EventListener.class);
    
    private Connection connection = null;
    private Session session = null;
    private EventType eventType = null;
    private EventsReceptor ere = null;
    private MessageConsumer messageConsumer = null;
    
    public EventListener(EventType eventType, EventsReceptor ere){
        this.eventType = eventType;
        this.ere = ere;
    }
    
    public void finalize() {
    	if(messageConsumer!=null)
			try {
				messageConsumer.close();
			} catch (JMSException ex) {
				logger.warn("Exception caught when closing Message Consumer", ex);
			}
    }
    
    public void run() {
        try {
            listen();
        } catch (NamingException ex) {
            logger.error("NamingException caught", ex);
        } catch (JMSException ex) {
            logger.error("JMSException caught", ex);
        } finally {
        
            try{
                if(session != null){
                    logger.info("Clossing session");
                    session.close();                    
                }
        
                if(connection != null){
                    logger.info("Clossing connection");
                    connection.close();
                }
            } catch (JMSException ex) {
                logger.error("JMSException caucht when closing session and" +
                        "connection", ex);
            }
        }
    }
    
    public void listen() throws NamingException, JMSException {

        logger.info("Getting initial JNDI context");
        Context initialContext = new InitialContext(SMConfiguration.getInstance().getJNDIEnv());

        logger.info("Creating Connection Factory");
        ConnectionFactory connectionFactory = 
                (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        
        logger.info("Getting Topic");    
        Topic topic = (Topic)initialContext.lookup(Event.busTopicForEventType(eventType));

        logger.info("Creating Connection");
        connection = connectionFactory.createConnection();
        connection.start();
        
        logger.info("Creating Session");              
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        logger.info("Registering message listener");
        messageConsumer = session.createConsumer(topic);
        
        Message message = null;
        while(true){
            message = messageConsumer.receive();            
            logger.debug("New message received, topic: " + 
                        ((Topic)message.getJMSDestination()).toString());
            if(message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage)message;
                if(objectMessage.getObject() instanceof Event) {                
                    Event event = (Event)objectMessage.getObject();  
                    ere.onEvent(event);
                    logger.debug("Event info - " + event);
                } else  {
                    logger.error("Message does not contain an Event");
                }                           
            } else {
                logger.error("Message is not instance of ObjectMessage");
            }
        }
        
    }

}
