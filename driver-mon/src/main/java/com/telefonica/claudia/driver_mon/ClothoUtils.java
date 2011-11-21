package com.telefonica.claudia.driver_mon;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;
import com.telefonica.claudia.smi.deployment.DeploymentDriver;

public class ClothoUtils {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.ClothoUtils");
	
	// This time must be greater than TASK_TIMEOUT in TCloudClient, in order to prevent
	// a JMS message timeout before the task gives a correct result
	public static final int EVENT_BUS_TIMEOUT = 60 * 60 * 1000;
	
    public static final String DEFAULT_CONN_FACTORY_NAME = "ConnectionFactory";    
    public static final String JNDI_SERVER_PROVIDER_URL = "java.naming.provider.url";
    public static final String JNDI_NAMING_FACTORY = "java.naming.factory.initial";
	
	public static class JMSData {
	    public Queue smiChannelRequest = null;
	    public Queue smiChannelReply = null;
	    public MessageProducer smiChannelProducer = null; 
	    public Session session = null;
	    public Connection connection = null;
	}
	
	private static long sequenceNumber = 0;
	public static long getSecuenceNumber() {
		return sequenceNumber++;
	}

	public static void cleanQueues(JMSData jmsConnectionDataLocal) {
    	log.info("Clean the queues from lost replys. ");

    	if (jmsConnectionDataLocal.session== null) return;
    	MessageConsumer messageConsumer  = null;
    	try {
			QueueBrowser qb = jmsConnectionDataLocal.session.createBrowser(jmsConnectionDataLocal.smiChannelRequest);
			
			Enumeration e = qb.getEnumeration();
			
			messageConsumer = jmsConnectionDataLocal.session.createConsumer(jmsConnectionDataLocal.smiChannelRequest);

			int n=0;
			while (e.hasMoreElements()) {
				e.nextElement();
				n++;
			}
			
			for (int i =0; i < n; i++) {
				log.info("Deleting pending messages...");
				messageConsumer.receive(EVENT_BUS_TIMEOUT);
			}
			
			try { Thread.sleep(5000); } catch (InterruptedException ex) { }
			
		} catch (JMSException e) {
			log.warn("Exception cleaning the queue. Execution might continue without errors: " + e.getMessage());
		} finally {
			try{
				if (messageConsumer!=null){
					messageConsumer.close();
				}
			}catch (Exception e) {}
		}
		
	}
    
    public static JMSData openJMSSession(String serverProviderUrl, String namingFactory, String connFactoryName) throws NamingException, JMSException, ClassNotFoundException {
        
    	JMSData jmsConnectionData = new JMSData();
    	
        log.info("Getting initial JNDI context");
        
        Hashtable<String, String> jndiContext = getJNDIEnv(serverProviderUrl);
       DeploymentDriver.class.getClassLoader().loadClass(namingFactory);
        Class classFactory = Class.forName(namingFactory, true, DeploymentDriver.class.getClassLoader());
        Context initialContext = null;
        
        try {
			InitialContextFactory icFactory = (InitialContextFactory) classFactory.getConstructor().newInstance();
			initialContext = icFactory.getInitialContext(jndiContext);
		} catch (Exception e) {
			log.error("It wasn't possible to create the initialContext: " + e.getMessage());
			throw new NamingException("It wasn't possible to create the initialContext: " + e.getMessage());
		}
        
        log.info("Looking up for JMS connection factory " + connFactoryName);        
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connFactoryName);
        
        log.info("Creating connection to bus");
        jmsConnectionData.connection = connectionFactory.createConnection();
        jmsConnectionData.connection.start();
        
        log.info("Creating session");
        jmsConnectionData.session = jmsConnectionData.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        log.info("Getting queue for event type " + EventType.SMI_CHANNEL_EVENT);
        jmsConnectionData.smiChannelRequest = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, false));
        jmsConnectionData.smiChannelReply = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, true));
        
        return jmsConnectionData;
    }
        
	public static Hashtable<String, String> getJNDIEnv(String serverProviderUrl) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		
		//env.put(Context.INITIAL_CONTEXT_FACTORY, namingFactory);
		env.put(Context.PROVIDER_URL, serverProviderUrl);

		env.put("queue.SMI_CHANNEL_EVENT_REQUEST", "SMI_CHANNEL_REQUEST");
		env.put("queue.SMI_CHANNEL_EVENT_REPLY", "SMI_CHANNEL_REPLY");
		
		return env;
	}
}
