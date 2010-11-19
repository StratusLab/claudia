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


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;

public class BusMediator {
    
    private static final String DEFAULT_CONN_FACTORY_NAME = "ConnectionFactory";    
    private String connFactoryName = null;    
    
    private static final long MESSAGE_TIMEOUT=1000*2;
    
    private static Logger logger = Logger.getLogger("Monitoring");
    
    private boolean open = false;
    private boolean closed = false;
    
    /** JNDI initial context */
    Context initialContext = null;
    
    /** Connection with bus */
    private Connection connection = null;
    /** Session with bus */
    private Session session = null;
    
    private Topic veeHwMonTopic = null;
    private Topic infrSLAViolTopic = null;
    private Topic servSLAViolTopic = null;
    private Topic agentMeasurementTopic = null;
    private Topic probesMeasurementTopic = null;
    private Topic fsmBusTopic = null;
    private Topic smControlTopic = null;
    
    private Queue smiChannelRequest = null;
    private Queue smiChannelReply = null;
    
    private Queue administrativeRequest = null;
    private Queue administrativeReply = null;
    
    private MessageProducer veeHwMonProducer = null;
    private MessageProducer infrSLAViolProducer = null;
    private MessageProducer servSLAViolProducer = null;
    private MessageProducer agentsMeasProducer = null;
    private MessageProducer probesMeasProducer = null;
    private MessageProducer smiChannelProducer = null; 
    private MessageProducer smControlProducer = null; 
    private MessageProducer administrativeProducer = null; 
    private MessageProducer fsmBusProducer = null; 
    

    private Map<BusListener, List<MessageConsumer>> busListeners = new Hashtable<BusListener, List<MessageConsumer>>();
    
    static {    	
        Logger.getLogger("com.telefonica.claudia.slm.eventsBus").setLevel(Level.INFO); 
        Logger.getLogger("com.telefonica.claudia.slm.eventsBus").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   	
    }
    
    public BusMediator(String connFactoryName) {
        this.connFactoryName = connFactoryName;
    }
    
    public BusMediator(){
        this.connFactoryName = DEFAULT_CONN_FACTORY_NAME;
    }   
    
    public void openJMSSession() throws NamingException, JMSException {
    	
        logger.info("Getting initial JNDI context");
        
        initialContext = new InitialContext(SMConfiguration.getInstance().getJNDIEnv());
        
        
        logger.info("Looking up for JMS connection factory " + connFactoryName);        
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connFactoryName);
        
        logger.info("Creating connection to bus");
        connection = connectionFactory.createConnection();
        connection.start();
        
        logger.info("Creating session");              
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        logger.info("Getting topic for event type " + EventType.VEE_HW_MEASUREMENT);
        veeHwMonTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.VEE_HW_MEASUREMENT));
        veeHwMonProducer = session.createProducer(veeHwMonTopic);
        
        logger.info("Getting topic for event type " + EventType.INFRASTRUCTURE_SLA_VIOLATION_EVENT);
        infrSLAViolTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.INFRASTRUCTURE_SLA_VIOLATION_EVENT));
        infrSLAViolProducer = session.createProducer(infrSLAViolTopic);
        
        logger.info("Getting topic for event type " + EventType.SERVICE_SLA_VIOLATION_EVENT);
        servSLAViolTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.SERVICE_SLA_VIOLATION_EVENT));
        servSLAViolProducer = session.createProducer(servSLAViolTopic);
        
        logger.info("Getting topic for event type " + EventType.AGENT_MEASUREMENT);
        agentMeasurementTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.AGENT_MEASUREMENT));
        agentsMeasProducer = session.createProducer(agentMeasurementTopic);
        
        logger.info("Getting topic for event type " + EventType.PROBE_MEASUREMENT);
        probesMeasurementTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.PROBE_MEASUREMENT));
        probesMeasProducer = session.createProducer(probesMeasurementTopic);
        
        logger.info("Getting topic for event type " + EventType.SM_CONTROL_EVENT);
        smControlTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.SM_CONTROL_EVENT));
        smControlProducer = session.createProducer(smControlTopic);
        
        logger.info("Getting queue for event type " + EventType.SMI_CHANNEL_EVENT);
        smiChannelRequest = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, false));
        smiChannelReply = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, true));        
        smiChannelProducer = session.createProducer(smiChannelReply);
        
        logger.info("Getting topic for event type " + EventType.FSM_BUS_EVENT);
        fsmBusTopic = (Topic)initialContext.lookup(Event.busTopicForEventType(EventType.FSM_BUS_EVENT));
        fsmBusProducer = session.createProducer(fsmBusTopic);

        logger.info("Getting queue for event type " + EventType.ADMINISTRATIVE_EVENT);
        administrativeRequest = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.ADMINISTRATIVE_EVENT, false));
        administrativeReply = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.ADMINISTRATIVE_EVENT, true));        
        administrativeProducer = session.createProducer(administrativeReply);

        
        open = true; closed = false;
    }
    
    /**
     * Clean all the queues where the BusMediator is suscribed, consuming all the messages in the queue and
     * discarding them.
     */
    @SuppressWarnings("unchecked")
	public void cleanQueues() {
    	if (session== null) return;
    	
    	Queue[] queues = new Queue[] {smiChannelRequest, administrativeRequest};
    	
    	for (Queue actualQueue: queues)
	    	try {
				QueueBrowser qb = session.createBrowser(actualQueue);
				
				Enumeration e = qb.getEnumeration();
				
				MessageConsumer messageConsumer = session.createConsumer(actualQueue);
	
				int n=0;
				while (e.hasMoreElements()) {
					e.nextElement();
					n++;
				}
				
				for (int i =0; i < n; i++) {
					logger.info("Deleting pending messages...");
					messageConsumer.receive(MESSAGE_TIMEOUT);
				}
				
				messageConsumer.close();
				
				try { Thread.sleep(5000); } catch (InterruptedException ex) { }
				
			} catch (JMSException e) {
				logger.warn("Exception cleaning the queue. Execution might continue without errors: " + e.getMessage());
			}
    }
    
    /**
     * Proxy for the sendEvent method, without the need of passing the properties parameter.
     * 
     * @param event
     * 		Event to be sent.
     * 
     * @throws JMSException
     */
    public void sendEvent(Event event) throws JMSException {
    	sendEvent(event, null);
    }
    
    /**
     * Sends the event passed as a parameter to the event bus. The topic will be chosen 
     * depending on the event type. 
     * 
     *  Aditionally, selector properties may be specified. These are properties that will
     *  be added to the message header, and who let the event bus consumer distingish 
     *  which messages it is interested in. 
     * 
     * 
     * @param event
     * 		Event to be sent
     * 
     * @param selectors
     * 		Map with the properties to be added to the message header for message selection.
     * 
     * @throws JMSException
     */
    public void sendEvent(Event event, HashMap<String, String> selectors) throws JMSException {
        
        ObjectMessage message = session.createObjectMessage(event);
        
        if (selectors != null) {
        	for (String key: selectors.keySet())
        		message.setStringProperty(key, selectors.get(key));
        }
        
        switch(event.getEventType()) {
            case VEE_HW_MEASUREMENT:
                logger.debug("Sending VEE Hw measurement event");
                veeHwMonProducer.send(message);
                return;
            case INFRASTRUCTURE_SLA_VIOLATION_EVENT:
                logger.debug("Sending infrastructure SLA violation event");
                infrSLAViolProducer.send(message);
                return;
            case SERVICE_SLA_VIOLATION_EVENT:
            	logger.debug("Sending service SLA violation event");
            	servSLAViolProducer.send(message);
            	return;
            case AGENT_MEASUREMENT:
                logger.debug("Sending agent measurement event");
                agentsMeasProducer.send(message);
                return;
            case PROBE_MEASUREMENT:
                logger.debug("Sending probe measurement event");
                probesMeasProducer.send(message);
                return;
            case SMI_CHANNEL_EVENT:
                logger.debug("Sending SMI channel event");
                smiChannelProducer.send(message);
                return;
            case SM_CONTROL_EVENT:
                logger.debug("Sending SM Control event");
                smControlProducer.send(message);
                return;
            case ADMINISTRATIVE_EVENT:
                logger.debug("Sending Administrative event");
                administrativeProducer.send(message);
                return;
            case FSM_BUS_EVENT:
                logger.debug("Sending FSM Communication event");
                fsmBusProducer.send(message);
                return;
            default:
                logger.error("Event type " + event.getEventType() + " not supported by this bus mediator.");
                throw new Error("Event type " + event.getEventType() + " not supported by mediator.");
        }
        
    }
    
    public void registerEventsReceiver(BusListener busListener, EventType eventType) throws JMSException {
        
        MessageConsumer messageConsumer = null;
        
        switch(eventType) {
            case VEE_HW_MEASUREMENT:        
                logger.debug("Creating events listener for VEE Hw events");
                messageConsumer = session.createConsumer(veeHwMonTopic);
                break;
            case INFRASTRUCTURE_SLA_VIOLATION_EVENT:
                logger.debug("Creating events listener for infrastructure SLA violation events");
                messageConsumer = session.createConsumer(infrSLAViolTopic);
                break;
            case SERVICE_SLA_VIOLATION_EVENT:
                logger.debug("Creating events listener for service SLA violation events");
                messageConsumer = session.createConsumer(servSLAViolTopic);
            	break;
            case AGENT_MEASUREMENT:
                logger.debug("Creating events listener for agent measure events");
                messageConsumer = session.createConsumer(agentMeasurementTopic);
                break;
            case PROBE_MEASUREMENT:
                logger.debug("Creating events listener for probes measure events");
                messageConsumer = session.createConsumer(probesMeasurementTopic);
                break;
            case SMI_CHANNEL_EVENT:
                logger.debug("Creating events listener for SMI Comunication");
                messageConsumer = session.createConsumer(smiChannelRequest);
                break;
            case SM_CONTROL_EVENT:
                logger.debug("Creating events listener for SM Control Comunication");
                messageConsumer = session.createConsumer(smControlTopic);
                break;    
            case ADMINISTRATIVE_EVENT:
                logger.debug("Creating events listener for Administration Communication");
                messageConsumer = session.createConsumer(administrativeRequest);
                break; 
                
            case FSM_BUS_EVENT:
                logger.debug("Creating events listener for communication between FSM and LCC");
                messageConsumer = session.createConsumer(fsmBusTopic);
                break;
                
            default :
                logger.error("Event type " + eventType + " not supported by this bus mediator.");
                throw new Error("Event type " + eventType + " not supported by mediator.");                
        }
        
        
        messageConsumer.setMessageListener(new EventsReceiver(busListener, eventType));
        
        if(!busListeners.containsKey(busListener))
        	busListeners.put(busListener, new ArrayList<MessageConsumer>());
        busListeners.get(busListener).add(messageConsumer);
        return;
    }
    
    public void unregisterEventsReceiver(BusListener busListener) {
    	List<MessageConsumer> messageConsumers = busListeners.remove(busListener);
    	if(messageConsumers == null)
    		return;
    	
    	for(MessageConsumer messageConsumer : messageConsumers)    	
	    	try {
				messageConsumer.close();
			} catch (JMSException ex) {
	            logger.error("JMS Exception caught when closing message consumer", ex);
			}
    }
    
    public void closeJMSSession() {
    	
    	if(!open || closed)
    		return;
        
    	for(BusListener busListener : busListeners.keySet())
    		unregisterEventsReceiver(busListener);
    	
    	busListeners.clear();
        
        if(session != null) {
            logger.info("Clossing session");
            try{
                session.close();
            } catch(JMSException ex) {
                logger.error("JMS Exception caught when clossing session", ex);
            }
        }
        
        if(connection != null) {
            logger.info("Clossing connection with bus");
            try {
                connection.close();
            } catch(JMSException ex) {
                logger.error("JMS Exception caught when clossing session", ex);                
            }
        }
        
        open = false; closed = true;
    }
    
    @Override
    protected void finalize() {    	
        closeJMSSession();        
    }
    
}

class EventsReceiver implements MessageListener {
    
    private static Logger logger = Logger.getLogger(EventsReceiver.class);
    
    private BusListener busListener = null;
    private EventType eventType = null;
    
    public EventsReceiver(BusListener busListener, EventType eventType) {
        this.busListener = busListener;
        this.eventType = eventType;
    }

    public void onMessage(Message message) {        
        
        if(message instanceof ObjectMessage) {

            ObjectMessage objectMessage = (ObjectMessage)message;
            try {
            	
                if(objectMessage.getObject() instanceof Event) {         
                    Event event = (Event)objectMessage.getObject();
                    if(event.getEventType() == eventType)                        
                        busListener.onEvent(event);
                    else
                        logger.error("Received event is of type " + event.getEventType() +
                                " not of expected type " + eventType);
                } else
                    logger.error("Message does not contain an Event");                           
            } catch (JMSException ex) {
                logger.error("JMSException when extracting ObjectMessage instance from message", ex);
            }
        } else
            logger.error("Message is not instance of ObjectMessage");
    }

}