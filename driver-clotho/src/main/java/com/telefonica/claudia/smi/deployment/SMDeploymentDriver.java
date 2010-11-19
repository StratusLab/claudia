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
package com.telefonica.claudia.smi.deployment;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

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
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent.SMIAction;
import com.telefonica.claudia.smi.Main;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.task.Task;
import com.telefonica.claudia.smi.task.TaskManager;


public class SMDeploymentDriver implements DeploymentDriver {

	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.SMPlugin");

	private static final int EVENT_BUS_TIMEOUT=15*60*1000;
	
    private Queue smiChannelRequest = null;
    private Queue smiChannelReply = null;

    private MessageProducer smiChannelProducer = null; 
    
    private Session session = null;

    Context initialContext = null;

    private Connection connection = null;
    
    private static final String DEFAULT_CONN_FACTORY_NAME = "ConnectionFactory";    
    private String connFactoryName = null;
    
    private static final String JNDI_SERVER_PROVIDER_URL = "java.naming.provider.url";
	private static final String JNDI_NAMING_FACTORY = "java.naming.factory.initial";
	
	private String serverProviderUrl = null;
	private String namingFactory = null;
	
	public class UndeployServiceTask extends Task {

		String fqn;
		
		public UndeployServiceTask(String fqn) {
			super();
			
			this.fqn = fqn;
		}
		
		@Override
		public void execute() {
			
			status = TaskStatus.QUEUED;
			
			// Send the undeploy message.
			try {
				undeploy(fqn);
				status = TaskStatus.SUCCESS;
			} catch (JMSException e) {
				log.error("Error connecting with the SLM. Task could not be completed: " + e.getMessage());
				status = TaskStatus.ERROR;
			} catch (IOException e) {
				log.error("Unknown IO Error. Task could not be completed: " + e.getMessage());
				status = TaskStatus.ERROR;
			}
			
			this.endTime = System.currentTimeMillis();
		}
		
	    /**
	     * Undeploy the selected service.
	     * 
	     * @param receives 
	     * 		A String representation of the service's FQN
	     */
	    public void undeploy(String serviceFQNRep) throws JMSException, IOException {    	
	    	
	    	SMIChannelEvent smi = new SMIChannelEvent(0, 0, SMIAction.UNDEPLOY);
	    	try {
				smi.put(SMIChannelEvent.FQN_ID, serviceFQNRep);
				
				smiChannelProducer.send(session.createObjectMessage(smi));
				
			} catch (JMSException e) {
				log.error("SMPlugin: Communicatin error accessing the Service Manager. Service was not deployed",e);
			}
	        
			// Wait until the Service is deployed.
			MessageConsumer messageConsumer = session.createConsumer(smiChannelReply, "service = '" + serviceFQNRep + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				throw new IOException("Timeout waiting for a server response. Try later.");
			}
			
			messageConsumer.close();
			
	        log.info("SMPlugin: Undeployment of service launched");
	        return;
	    }
	}
	
	public class DeployServiceTask extends Task {

		String fqnCustomer;
		String serviceName;
		String ovf;
		
		public DeployServiceTask(String fqn, String serviceName, String ovf) {
			super();
		
			this.fqnCustomer= fqn;
			this.serviceName= serviceName;
			this.ovf= ovf;
		}
		
		@Override
		public void execute() {
			status = TaskStatus.RUNNING;
			
			try {
				if (deploy())
					status = TaskStatus.SUCCESS;
				else
					status = TaskStatus.ERROR;
				
				this.endTime = System.currentTimeMillis();
				
			} catch (JMSException e) {
				status = TaskStatus.ERROR;
				error = new TaskError();
				error.message = "JMS exception connecting with the SLM: " + e.getMessage();
			} catch (IOException e) {
				status = TaskStatus.ERROR;
				error = new TaskError();
				error.message = "Unknown IO Error connecting with the SLM: " + e.getMessage();
			}
		}
		
	    /**
	     * Deploys a service application for the customer passed as a parameter.
	     * 
	     * @param domR
	     * 		String with a XML representation of the service in OVF format.
	     * 
	     * @param customerName 
	     * 		Receives a String representation of the service's name
	     * 
	     * @return String 
	     * 		Service name assigned by the LCC (reservoir's FQN)
	     * 
	     * @throws IOException 
	     * 		If the XML representation of the service is not well formed.
	     * 
	     * @throws JMSException 
	     * 		If its not possible to connect to the event bus.
	     */    
	    public boolean deploy() throws JMSException, IOException {    	   	
	    	
	    	log.info("Sending deploying message for service ["+serviceName+"] of user [" + fqnCustomer +"]");
	    	SMIChannelEvent smi = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.DEPLOY);

			smi.put(SMIChannelEvent.OVF_DOCUMENT, ovf);
			smi.put(SMIChannelEvent.CUSTOMER_NAME, fqnCustomer);
			smi.put(SMIChannelEvent.SERVICE_NAME, serviceName);
			
			smiChannelProducer.send(session.createObjectMessage(smi));

			// Wait until the Service is deployed.
			log.info("Waiting for the answer of the SLM.");
			
			MessageConsumer messageConsumer = session.createConsumer(smiChannelReply, "customer = '" + fqnCustomer + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				error = new TaskError();
				error.message = "Timeout waiting for a server response. The service may not have been deployed.";

				return false;
			}
			
			smi = (SMIChannelEvent) m.getObject();
			String serviceFQN= smi.get(SMIChannelEvent.FQN_ID);
			
			log.info("FQN name for the service received: " + serviceFQN);
			
			messageConsumer.close();
			
			this.returnMsg = serviceFQN;
			log.info("SMPlugin: Deployment of service " + serviceFQN + " launched");    	
		    return true;
	    }
	}
	
    public SMDeploymentDriver(Properties prop) throws NamingException, JMSException, ClassNotFoundException { 

        // Creates the bus manager and attach it to the context as an attribte.
        try {
        	this.connFactoryName = DEFAULT_CONN_FACTORY_NAME;
        	
    		namingFactory = prop.getProperty(JNDI_NAMING_FACTORY);
    		serverProviderUrl = prop.getProperty(JNDI_SERVER_PROVIDER_URL);
        	
        	// Open the JMS Session to create the queue conections
        	openJMSSession();
        	cleanQueues();
        	
	        smiChannelProducer = session.createProducer(smiChannelRequest);	

		} catch (JMSException e) {
			log.error("The connection to the bus could not be created. " + e.getMessage());
			throw e;
		} catch (ClassNotFoundException e) {
			log.error("The Factory class could not be found");
			throw e;
		}
    }
    
    @SuppressWarnings("unchecked")
	private void cleanQueues() {
    	log.error("Clean the queues from lost replys. ");

    	if (session== null) return;
    	
    	try {
			QueueBrowser qb = session.createBrowser(smiChannelRequest);
			
			Enumeration e = qb.getEnumeration();
			
			MessageConsumer messageConsumer = session.createConsumer(smiChannelRequest);

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
    
    private void openJMSSession() throws NamingException, JMSException, ClassNotFoundException {
        
        log.info("Getting initial JNDI context");
        
        Hashtable<String, String> jndiContext = getJNDIEnv();
        SMDeploymentDriver.class.getClassLoader().loadClass(namingFactory);
        Class classFactory = Class.forName(namingFactory, true, SMDeploymentDriver.class.getClassLoader());
        
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
        connection = connectionFactory.createConnection();
        connection.start();
        
        log.info("Creating session");              
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        log.info("Getting queue for event type " + EventType.SMI_CHANNEL_EVENT);
        smiChannelRequest = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, false));
        smiChannelReply = (Queue)initialContext.lookup(Event.busQueueForEventType(EventType.SMI_CHANNEL_EVENT, true));
    }
    
	@SuppressWarnings("unchecked")
	public Hashtable<String, String> getJNDIEnv() {
		Hashtable env = new Hashtable();
		
		//env.put(Context.INITIAL_CONTEXT_FACTORY, namingFactory);
		env.put(Context.PROVIDER_URL, serverProviderUrl);

		env.put("queue.SMI_CHANNEL_EVENT_REQUEST", "SMI_CHANNEL_REQUEST");
		env.put("queue.SMI_CHANNEL_EVENT_REPLY", "SMI_CHANNEL_REPLY");
		
		return env;
	}
    
	public long deploy(String fqnCustomer, String serviceName, String ovf)
			throws IOException {
		
		Task deployTask = new DeployServiceTask(fqnCustomer, serviceName, ovf);
		deployTask.setResource("http://" + Main.serverHost + ":" + Main.serverPort + URICreation.getURIVDC(fqnCustomer) + "/vapp/" + serviceName, URICreation.VDC_MIME_TYPE);
		
		return TaskManager.getInstance().addTask(deployTask, fqnCustomer).getTaskId();
	}
	
//	public long createVdc(String fqnCustomer, String vdc) throws IOException {
//		//TODO: Must be implemented when clotho supports VDC creation
//		return -1;
//	}
//public long createVdc(String fqnCustomer, String vdc) throws IOException
//{
		//TODO: Must be implemented when clotho supports VDC creation
//		return -1;
//	}

public String  createVdc(String fqnCustomer, String vdc) throws IOException
{
  return null;
}
	
	public String getOrg(String fqn) throws IOException {
		//TODO: Must be implemented when clotho supports Orgs
		return null;
	}
	
	public String getVdc(String fqn) throws IOException {
		
		SMIChannelEvent smi = new SMIChannelEvent(0, 0, SMIAction.GET_VDC);
		
    	try {
			smi.put(SMIChannelEvent.FQN_ID, fqn);

			smiChannelProducer.send(session.createObjectMessage(smi));
			
			// Wait until the Service is deployed.
			MessageConsumer messageConsumer = session.createConsumer(smiChannelReply, "customer = '" + fqn + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				throw new IOException("Timeout waiting for a server response. Try later.");
			}
			
			smi = (SMIChannelEvent) m.getObject();
			String serviceDescription= smi.get(SMIChannelEvent.CUSTOMER_DESCRIPTION);
			
			messageConsumer.close();
			
	        log.info("SMPlugin: Customer data retrieved");
			return serviceDescription;
			
		} catch (JMSException e) {
			log.error("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve customer data.",e);
			throw new IOException("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve customer data." + e.getMessage());
		}
	}

	public String getService(String fqn) throws IOException {
		
		SMIChannelEvent smi = new SMIChannelEvent(0, 0, SMIAction.GET_VAPP);
		
    	try {
			smi.put(SMIChannelEvent.FQN_ID, fqn);

			smiChannelProducer.send(session.createObjectMessage(smi));
			
			// Wait until the Service is deployed.
			MessageConsumer messageConsumer = session.createConsumer(smiChannelReply, "service = '" + fqn + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				throw new IOException("Timeout waiting for a server response. Try later.");
			}
			
			smi = (SMIChannelEvent) m.getObject();
			String serviceDescription= smi.get(SMIChannelEvent.SERVICE_DESCRIPTION);
			
			messageConsumer.close();
			
	        log.info("SMPlugin: Service data retrieved");
			return serviceDescription;
			
		} catch (JMSException e) {
			log.error("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve service data.",e);
			throw new IOException("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve service data." + e.getMessage());
		}
	}
	
	public String getVee(String fqn) throws IOException {
		
		SMIChannelEvent smi = new SMIChannelEvent(0, 0, SMIAction.GET_VEE);
		
		try {
			smi.put(SMIChannelEvent.FQN_ID, fqn);

			smiChannelProducer.send(session.createObjectMessage(smi));
			
			// Wait until the Service is deployed.
			MessageConsumer messageConsumer = session.createConsumer(smiChannelReply, "vee = '" + fqn + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				throw new IOException("Timeout waiting for a server response. Try later.");
			}
			
			smi = (SMIChannelEvent) m.getObject();
			String serviceDescription= smi.get(SMIChannelEvent.VEE_DESCRIPTION);
			
			messageConsumer.close();
			
	        log.info("SMPlugin: VEE data retrieved");
			return serviceDescription;
			
		} catch (JMSException e) {
			log.error("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve VEE data.",e);
			throw new IOException("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve VEE data." + e.getMessage());
		}
	}

	public void undeploy(String fqn) throws IOException {
		TaskManager.getInstance().addTask(new UndeployServiceTask(fqn), URICreation.getVDC(fqn)).getTaskId();
	}


	
	/**
	 * Retrieves information on the selected org. 
	 * 
	 * @param fqn
	 * 		FQN of the service to retrieve.
	 * 
	 * @return
	 * 		XML Representation of the org
	 * 
	 * @throws IOException
	 */



}
