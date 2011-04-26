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
package com.telefonica.claudia.slm.lifecyclemanager;
 
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.slm.common.DbManager;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ProbeKPI;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.ServiceKPI;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.CPU;
import com.telefonica.claudia.slm.deployment.hwItems.Disk;
import com.telefonica.claudia.slm.deployment.hwItems.Memory;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.eventsBus.BusListener;
import com.telefonica.claudia.slm.eventsBus.BusMediator;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent;
import com.telefonica.claudia.slm.eventsBus.events.AgentMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.ProbeMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.SMControlEvent;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent;
import com.telefonica.claudia.slm.eventsBus.events.VeeHwMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.Event.EventType;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent.SMIAction;
import com.telefonica.claudia.slm.monitoring.MonitoringRestBusConnector;
import com.telefonica.claudia.slm.monitoring.WasupClient;
import com.telefonica.claudia.slm.monitoring.WasupHierarchy;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.slm.rulesEngine.RulesEngine;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.URICreation;


/**
 * This class is the core of the SM, it manages the lifecycle of the finite state machine representing a single/or a set of services
 *  This element is connected to:
 *      - the SP through some parts of the SMI
 *      - SM components invoking controller methods
 *      - the FSMs themselves
 */

public class LifecycleController  extends UnicastRemoteObject implements SMI, Serializable, BusListener{
	
	private static Logger logger = Logger.getLogger(LifecycleController.class); 

	private static Logger monitoringLog = Logger.getLogger("Monitoring");
	
	static {
        Logger.getLogger("com.telefonica.claudia.slm.lifecyclemanager").setLevel(Level.INFO); 
        Logger.getLogger("com.telefonica.claudia.slm.lifecyclemanager").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   	
	}
 
	private static final long serialVersionUID = 1L;
	private static final String manifestRepository="./repository/manifestFiles";    
    private static final String manifestBuffer="./repository/manifestBuffer";

    private BusMediator busMediator = new BusMediator();
    
    /**
     * Reference to the monitorization hierarchy. 
     */
	private WasupHierarchy wasupClient;
    
    /**
     * Collection of network ranges available for making subnets.
     */
    private List<NetworkRange> networkRanges = new ArrayList<NetworkRange>();
    
    //  event Buffer
    private ArrayBlockingQueue<SMControlEvent> eventBuffer; 
    
    /**
     * Map containing the buffer vectors for each FSM. The key is the service application
     * FQN.
     */
    private HashMap<String, ArrayBlockingQueue<SMControlEvent>> bufferVector;
    
    
    private HashMap<FQN, RulesEngine> ruleEngineVector;
    private HashMap<String, FSM> fsmVector;
    
    // map service FQN to the corresponding LCC
    private static HashMap<FQN, String> service2Controller=new HashMap<FQN, String>();
    private static int count=0;
    
    
    private static int deploymentNum=0;
    
    //Monitoring endPoint
    private String endPoint;
    
    private enum LccStates {INIT, RUNNING, FINISHING};
    
    List<Customer> customers= null;
    
    /**
     * Time the SLM will wait for the FSM to undeploy and finish, until killing it explicitly.
     */
	private static final long FSM_FINISH_TIMEOUT = 8*60*1000;
    
    private LccStates state= LccStates.INIT;
    
    public LifecycleController()  throws RemoteException, JMSException, NamingException{
    	
    	this.customers = new ArrayList<Customer>();

       
        bufferVector=new HashMap<String, ArrayBlockingQueue<SMControlEvent>>();
        ruleEngineVector=new HashMap<FQN, RulesEngine>();
        fsmVector=new HashMap<String, FSM>();        
        MonitoringRestBusConnector restBusConnector = MonitoringRestBusConnector.getInstance();
        restBusConnector.start();
        
        // Connecting to JMS bus
        try {
			busMediator.openJMSSession();
		} catch (NamingException ex) {
			logger.error("Could not connect LifecycleController to JMS bus, NamingException caught: " + ex.getMessage(), ex);
			return;
		} catch (JMSException ex) {
			logger.error("Could not connect LifecycleController to JMS bus, JMSException caught: " + ex.getMessage(), ex);
			return;
		}
		
		// In order to use the WASUP, is it neccesary to create its element types.
		// First off all, we should log in.
		if (SMConfiguration.getInstance().isWasupActive()) {
			try {
				WasupClient.getWasupClient().login("http://" + SMConfiguration.getInstance().getWASUPHost() + ":" +
												   SMConfiguration.getInstance().getWASUPPort() + 
												   SMConfiguration.getInstance().getWASUPPath(), 
												   SMConfiguration.getInstance().getWASUPLogin(), 
												   SMConfiguration.getInstance().getWASUPPassword());
				
				WasupHierarchy.setParameters(SMConfiguration.getInstance().getCustomerType(), 
											 SMConfiguration.getInstance().getServiceType(), 
											 SMConfiguration.getInstance().getVeeType(), 
											 SMConfiguration.getInstance().getVeeReplicaType(), 
											 SMConfiguration.getInstance().getNetworkType(), 
											 SMConfiguration.getInstance().getHwType());
				
				WasupHierarchy.loadWasupTypes();
				
				wasupClient = WasupHierarchy.getWasupHierarchy();
			} catch (IOException e) {
				monitoringLog.error("Could not create WASUP element types: " + e.getMessage(), e);
			} catch (JSONException e) {
				monitoringLog.error("Error parsing the WASUP response: " + e.getMessage(), e);
			} catch (Throwable e) {
				monitoringLog.error("Unknow error connecting to WASUP: " + e.getMessage(), e);
			}
		}
		
		// Registering for JMS events
		busMediator.cleanQueues();
		
		try {
			busMediator.registerEventsReceiver(this, EventType.AGENT_MEASUREMENT);
			busMediator.registerEventsReceiver(this, EventType.PROBE_MEASUREMENT);
			busMediator.registerEventsReceiver(this, EventType.VEE_HW_MEASUREMENT);
			busMediator.registerEventsReceiver(this, EventType.SMI_CHANNEL_EVENT);
			busMediator.registerEventsReceiver(this, EventType.ADMINISTRATIVE_EVENT);
			busMediator.registerEventsReceiver(this, EventType.FSM_BUS_EVENT);
			busMediator.registerEventsReceiver(this, EventType.SM_CONTROL_EVENT);
		} catch (JMSException ex) {
			monitoringLog.error("Could not register for events to JMS bus, JMSException caught: " + ex.getMessage(), ex);
			return;
		}
		
		loadSavedData();
        loadNetworkRanges();
		
		state= LccStates.RUNNING;
    }

    private void loadSavedData() {
    	
    	List<ServiceApplication> services = DbManager.getDbManager().getList(ServiceApplication.class);
    	
    	for (ServiceApplication sap: services) {
    		
    		logger.info("Restoring service " + sap.getSerAppName());
        	
            FSM fsm=new FSM(this, endPoint);
            
            RulesEngine rle=new RulesEngine(this);    
            
            // "load" the rule engine into the FSM
            fsm.loadRuleEngine(rle);
            fsm.loadService(sap);
            fsm.start();         
            
            ruleEngineVector.put(fsm.getServiceFQN(), rle);

            
            service2Controller.put(fsm.getServiceFQN(), String.valueOf(count++));
    	}
    	
    	networkRanges = DbManager.getDbManager().getList(NetworkRange.class);
    }
    
    /**
    * Read the network ranges definition from the Service Manager configuration and load them into
    * the networkRanges array.
    * 
    * The NetworkRanges property is composed of a list of network properties defined this way:
    * 
    * 			[  IP:10.95.240.0,
    *              Netmask:255.255.240.0,
    *              Gateway:10.95.240.1,
    *              DNS:10.95.240.1 ]
    * 
    */
   private void loadNetworkRanges() {
       String[] networkAddresses = SMConfiguration.getInstance().getNetworkRanges();
       
       for (String range: networkAddresses) {
    	   
    	   loadNetworkAddress(range);
       }
   }
   
   private void loadNetworkAddress(String range) {
	   if (!range.replaceAll("\n| ", "").trim().matches("\\[((Network:\\d+\\.\\d+\\.\\d+.\\d+);?|" + 
				   "(IP:(\\d+\\.\\d+\\.\\d+.\\d+/?)+);?|" + 
                  "(Netmask:\\d+\\.\\d+\\.\\d+.\\d+);?|" + 
                  "(Gateway:\\d+\\.\\d+\\.\\d+.\\d+);?|" + 
                  "(DNS:\\d+\\.\\d+\\.\\d+.\\d+);?|(Public:(yes|no));?)+\\]")) {
			
			logger.warn("Wrong network address format. Expected: [IP:ip_value,Netmask:nmask_value,Gateway:gw_value,DNS:dns_value]");
				return;
			}
		
		try {
		
			int IPPos= range.indexOf("IP:");
			String ip;
			if (IPPos>0) {
				ip = range.substring(IPPos+3, range.indexOf(";", IPPos)).trim();
			} else {
				logger.error("There must be one IP Address per network description.");
				return;
			}
		
			int MaskPos= range.indexOf("Netmask:");
			String mask;
			if (MaskPos>0)
				mask = range.substring(MaskPos+8, range.indexOf(";", MaskPos)).trim();
			else
				throw new IllegalArgumentException("Ranges must contain a Mask.");
		
			NetworkRange nr;
			
			if (ip.contains("/")) {
			
				logger.info("Creating a network leaser for IP addresses: " + ip);
			
				int networkPos= range.indexOf("Network:");
				String network;
			
				if (networkPos>0) {
					network = range.substring(networkPos+8, range.indexOf(";", networkPos)).trim();
				} else {
					logger.error("There must be one IP Address per network description.");
					return;
				}
			
				NetworkLeaser nl = new NetworkLeaser();
			
				nl.setIP(network);
				nl.setMask(mask);
				
				String[] ips = ip.split("/");
				
				for (String ipLease: ips)
					nl.addIPToPool(ipLease);
				
				nr=nl;
			
			} else {
			
				logger.info("Creating network range for network address: " + ip + " with mask " + mask);
				
				nr = new NetworkRange();
				
				nr.setIP(ip);
				nr.setMask(mask);
			}
			
			int GWPos= range.indexOf("Gateway:");
			if (GWPos>0)
				nr.setGateway(range.substring(GWPos+8, range.indexOf(";", GWPos)).trim());
			
			int DNSPos= range.indexOf("DNS:");
			if (DNSPos>0)
				nr.setDNS(range.substring(DNSPos+4, range.indexOf(";", DNSPos)).trim());
			
			int PublicPos= range.indexOf("Public:");
			if (PublicPos>0) {
				if (range.substring(PublicPos+7, range.indexOf(";", PublicPos)).trim().equals("yes"))
					nr.setPublic(true);
				else
					nr.setPublic(false);
			}
			
			if (!networkRangeExists(nr)) {
				networkRanges.add(nr);
				DbManager.getDbManager().save(nr);
			} else
				logger.info("Network range already loaded from DB: " + ip);
			
		} catch (IllegalArgumentException iae) {
			logger.warn("Wrong network address format: " + iae.getMessage());
			return;
		} catch (Throwable t) {
			logger.warn("Exception parsing network properties, check your syntax: " + t.getMessage());
			return;
		}
   }

   public boolean networkRangeExists(NetworkRange candidate) {

	   for (NetworkRange nr: networkRanges) 
		   if (nr.getIP().equals(candidate.getIP()))
			   return true;
	   
	   return false;
   }
   
    public int getController (FQN remoteFqn){   
        Integer enter=Integer.parseInt((String) service2Controller.get(remoteFqn));
        return enter.intValue();
    }
   
    /**
     * notify interested parties that the machine is up and running     
     * @param machine
     * @returnint representing the current machine state 0 = is not running; 1 = is running
     */
    protected int notifyRunning(FQN machine){
        // let the user know about the actual state of the machine
        // BY UPDATING ANY FIELD IN THE DEPLOYMENT MODEL???
        FSM checkFsm=(FSM)fsmVector.get(machine.toString());
        if (checkFsm.getCurrentState()==FSM.RUNNING)
            return 1;
        else
            return 0;
    }
    
    /**
     *  the controller keeps a list of the FQNs of the FSM services it controls
     *  the method is invoked by the FSM itself whenever it has been successfully created
     */
    public void registerFSM(FQN serviceID, FSM finiteStateMachine){        
        // add a new buffer for this service        
        eventBuffer=new ArrayBlockingQueue<SMControlEvent>(1000);
        // add the buffer to the buffer pool
        bufferVector.put(serviceID.toString(), eventBuffer);       
        fsmVector.put(serviceID.toString(), finiteStateMachine);
    }
    
    /**
     * the controller deregisters a FSM whenever the user prompts to do it so or the FSM
     * finishes its lifetime
     */
    public void deregisterFSM(FQN serviceID){
        if(!bufferVector.isEmpty()){
            bufferVector.remove(serviceID.toString());
            fsmVector.remove(serviceID.toString());
        }
    }
    
    /**
     * method to receive the incoming Events from other parts of the SM     
     * @param SM Control Event
     */
    public void putEvent(SMControlEvent controlEvent) {       
       
    	logger.info("\n\n* Sending Control Event to FSM: " + controlEvent.getServiceFQN().toString());
    	
        // check whether this controller is monitoring that service
        if(bufferVector.containsKey(controlEvent.getServiceFQN().toString())){
            try {
                // get the buffer for this Element
                ArrayBlockingQueue<SMControlEvent> buffer = bufferVector.get(controlEvent.getServiceFQN().toString());
                
                // add event at the end of the queue and wait for space to be made
                buffer.put(controlEvent);
                //buffer.notify();                                
            } catch (InterruptedException ex) {
                logger.error("The event buffer has been interrupted putting an event on the queue: " + ex.getMessage());
            }
        }else{
        	logger.error(controlEvent.getServiceFQN().toString()+" not in the current list of keys");
        }
        
    }
    
    /**
     * Method to be invoked by the FSM
     * to check whether there has been any new event
     */
    public SMControlEvent deliver(FQN serviceID) {     
        ArrayBlockingQueue<SMControlEvent> buffer = bufferVector.get(serviceID.toString());
        return buffer.poll();        
    }

    /**
     * Checks whether the provided serviceName is unique or it has been already deployed for the same
     * customer. If there is a service with the same name and customer, it creates a sufix for the 
     * service name, so to make it unique.
     * 
     * @param serviceName
     * 		Candidate service name.
     * 
     * @return
     * 		A unique service name based upon the candidate.
     * 
     */
    private String checkServiceName(String customerName, String serviceName) {
    	
    	int i=0;
    	String sufix = "";
    	String actualName = serviceName;
    	
    	for (FQN fsmFQN: service2Controller.keySet()) {
    		ServiceApplication fsm = (ServiceApplication) ReservoirDirectory.getInstance().getObject(fsmFQN);
    		
    		if (fsm !=null && fsm.getCustomer().getCustomerName().equals(customerName) && fsm.getSerAppName().equals(actualName + sufix)) {
    			i++;
    			sufix = String.valueOf(i);
    		}
    	}
    	
    	return actualName + sufix;
    }
    
    /**
     * Method invoked by the SP to command the deployment of a new service
     * @param manifest
     */
    @SuppressWarnings("deprecation")
	public FQN deploy(URL manifest){// throws RemoteException {
    	
    	logger.info("Getting manifest file from URL " + manifest + " and copying it to local file " + manifestRepository+manifest.getFile());
    	
        String s;
        InputStream is;
        DataInputStream dis;
        PrintWriter out;
        try {
            //download the URL and store the file in a local repository
            is = manifest.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));            
            out = new PrintWriter(new FileWriter(manifestRepository+manifest.getFile()));
           
            while ((s = dis.readLine()) != null) {                 
                out.write(s+"\n");                                
            }
            is.close();
            out.close();
        } catch (IOException ex) {
        	logger.error("Could not copy remote file " + manifest + " to " + manifestRepository+manifest.getFile(), ex);
        	return null;
        }
        
        return deploy(new File(manifestRepository+manifest.getFile()), "UNKNOWNCOSTUMER", "ANONYMOUSSERVICE");
    }

    /**
     * Deploys the Service application defined in the manifest passed as a parameter.
     * 
     * 
     * @param manifest
     * 		File object representing the xml manifes file containing the description
     * 		of the service.
     * 
     * @param customer
     * 		String identifier of the custumer who deploys the server.
     * 
     * @return
     */
    public FQN deploy(File manifest, String customerFqn, String serviceName){    
    	
    	logger.info("Deploying manifest file " + manifest.getAbsolutePath());
    	
        // start the FSM
        FSM fsm=new FSM(this, endPoint);    
        fsm.loadManifest(manifest.getAbsolutePath());
        
        // assume there is one rule engine instance per running  service
        // start a new rule engine and store it in a HashTable
        RulesEngine rle=new RulesEngine(this);    
        
        // "load" the rule engine into the FSM
        fsm.loadRuleEngine(rle);   
        
        // Before creating the service, check the service name is unique.
        String customer = customerFqn.substring(customerFqn.indexOf("customers"));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        serviceName = checkServiceName(customer, serviceName);
        
        fsm.init(serviceName, customer);
        fsm.start();          
        ruleEngineVector.put(fsm.getServiceFQN(), rle);
        
        
        // Store the mapping from FSM FQN to this LCC
        service2Controller.put(fsm.getServiceFQN(), String.valueOf(count++));
        
        // GENERATE CONTROL EVENT TO ACTUALLY START THE SERVICE
        SMControlEvent controlEvent = new SMControlEvent(0, 0, null, "start", fsm.getServiceFQN().toString());
        controlEvent.setControlEventType(SMControlEvent.START_SERVICE);
        this.putEvent(controlEvent);
        
        return fsm.getServiceFQN();
    }
    
    /**
     * Deploys the Service application defined in the manifest passed as a parameter.
     * 
     * @param manifest
     * 		XML code containing the description of the Service Application.
     * 
     * @param customer
     * 		String identifier of the custumer deploying the service
     * 	
     * @return
     */
     public FQN deploy(String manifest, String customer, String serviceName){      
    	
        String fileName = new String(manifestBuffer + Integer.toString(deploymentNum) + ".xml");
        deploymentNum++;
       
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            out.write(manifest);			
	    out.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LifecycleController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
         
    	logger.info("Deploying manifest file ");
    	
    	File bufferedFile = new File(fileName);
    	
    	return deploy(bufferedFile, customer, serviceName);
    }	
    

    public void reconfig(FQN serviceID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void undeploy(FQN serviceID) {
        //create a SMCOntrolEvent of the UNDEPLOY_SERVICE subtype and put it in the appropriate queue
        SMControlEvent undeploy=new SMControlEvent(0,0,null, "undeploy",serviceID.toString());
        undeploy.setControlEventType(SMControlEvent.UNDEPLOY_SERVICE);
        //put event into the apporpiate queue
        this.putEvent(undeploy);
    }
   
    /**
     *  Method invoked by the SP to set the desired frequency for the monitoring events of a given service
     * @param serviceID
     * @param period
     */
    public void setFrequency(FQN serviceID, long period) {
        ((FSM)fsmVector.get(serviceID)).setMonFreq(period);
    }
    
    /**
     * Method invoked by the AllEventListener whenever a new Event is received in the EventBus
     * @param event
     */
    @SuppressWarnings("unchecked")
	public void onEvent(Event event) {
    	
    	if (state!= LccStates.RUNNING) return;
    	
    	monitoringLog.info("Event reached Lifecycle controller: " + event.toString());
    	
        FQN deployObject;        
        // possible Hw items in the deployment model
        CPU cpu;
        Disk disk;
        Memory mem;
        NIC nic;
        // service KPI value
        ServiceKPI sKpi;
        // probe KPI
        ProbeKPI pKpi;
        
        // whenever an Event has been received cast the event to determine the event type
        EventType eventType=event.getEventType();
         switch(eventType) {     
         	case SM_CONTROL_EVENT:
         		SMControlEvent smce = (SMControlEvent)event;
         		logger.info("External event received: " + smce.getSuggestedAction());
         		this.putEvent(smce);
         		break;
         		
            case VEE_HW_MEASUREMENT:
                // create a FQN to get the appropriate object in the Directory
            	VeeHwMeasureEvent hwEvent;
                hwEvent=(VeeHwMeasureEvent)event;  
                Object object = ReservoirDirectory.getInstance().getObject(new FQN(hwEvent.getMeasure()));
                
                if(object == null) {
                	monitoringLog.error("Not object registered in directory with name " + hwEvent.getMeasure());
                	Set<FQN> fqns = ReservoirDirectory.getInstance().matchingNamesAndChildrenRegistered(ReservoirDirectory.getInstance().getRootNameSpace());
                	if(fqns.isEmpty())
                		monitoringLog.error("NOT FQNs registered!");
                	else {
                	}
                	break;
                }
                else if(object instanceof CPU) {
                	cpu = (CPU) object;
                	cpu.setUsage((float)hwEvent.getValue());
                	monitoringLog.info("CPU usage updated: " + (float)hwEvent.getValue());
                } else if(object instanceof Disk) {
                    disk = (Disk) object;
                    disk.setFreeCapacity((int)hwEvent.getValue());
                    logger.info("Disk usage updated: " + (float)hwEvent.getValue());
                } else if(object instanceof Memory) {
                    mem = (Memory) object;
                    mem.setFreeMem((int)hwEvent.getValue());
                    monitoringLog.info("Memory usage updated: " + (float)hwEvent.getValue());
                } else if(object instanceof NIC) {
                	nic = (NIC) object;
                    nic.setThroughput((int)hwEvent.getValue());
                    monitoringLog.info("NIC usage updated: " + (float)hwEvent.getValue());
                }
                
                if (object!= null && SMConfiguration.getInstance().isWasupActive()) {
    				try {
    					wasupClient.postMeasure((DirectoryEntry)object, String.valueOf(hwEvent.getValue()));
    				} catch (IOException e) {
    					monitoringLog.error("Measure could not be post due to an IO error: "+ e.getMessage());
    				} catch (JSONException e) {
    					monitoringLog.error("Measure may have not been posted, due to an error in the response: " + e.getMessage());
    				}  catch (Throwable e) {
    					monitoringLog.error("Unknow error connecting to WASUP: " + e.getMessage(), e);
    				}
                }
                
            break;

            case AGENT_MEASUREMENT:
            	AgentMeasureEvent agentEvent;
                agentEvent=(AgentMeasureEvent)event;
                // create a FQN to get the appropriate object in the Directory
                deployObject=new FQN(agentEvent.getMeasure());
                sKpi= (ServiceKPI) ReservoirDirectory.getInstance().getObject(deployObject);
                
                if(sKpi == null) {
                	monitoringLog.error("Not KPI registered in directory with name " + deployObject);
                } else{
                    sKpi.setKPIValue(agentEvent.getValue());
                    
                    if (SMConfiguration.getInstance().isWasupActive())
	    				try {
	    					wasupClient.postMeasure(sKpi, String.valueOf(agentEvent.getValue()));
	    				} catch (IOException e) {
	    					monitoringLog.error("Measure could not be post due to an IO error: "+ e.getMessage());
	    				} catch (JSONException e) {
	    					monitoringLog.error("Measure may have not been posted, due to an error in the response: " + e.getMessage());
	    				} catch (Throwable e) {
	    					monitoringLog.error("Unknow error connecting to WASUP: " + e.getMessage(), e);
	    				}
                    
    				monitoringLog.info("Updated KPI " + deployObject.toString() + " with value " + sKpi.getKPIValue());
                }
             break;
            case PROBE_MEASUREMENT:
            	ProbeMeasureEvent probeEvent;
                probeEvent=(ProbeMeasureEvent)event;
                 // create a FQN to get the appropriate object in the Directory
                deployObject=new FQN(probeEvent.getMeasure());
                pKpi= (ProbeKPI) ReservoirDirectory.getInstance().getObject(deployObject);
                if(pKpi == null) {
                	monitoringLog.error("Not KPI registered in directory with name " + deployObject);
                } else{
                	pKpi.setKPIValue(probeEvent.getValue());
                	monitoringLog.info("Updated KPI " + deployObject.toString() + " with value " + pKpi.getKPIValue());
                }
                break;
                
            case SMI_CHANNEL_EVENT:
            	SMIChannelEvent smiEvent = (SMIChannelEvent) event;
            	
            	processSmiEvent(smiEvent);
            	break;
            	
            case ADMINISTRATIVE_EVENT:
            	
            	AdministrativeEvent adminEvent = (AdministrativeEvent) event;
            	processAdminEvent(adminEvent);
            	break;
            	
            default:
                throw new Error("Unkonwn event type???");
        }               
    }

	private void processAdminEvent(AdministrativeEvent event) {
    	logger.info("Processing Administration Event");
    	
    	AdministrativeEvent response ;
    	
    	switch (event.getCommand()) {
    		case STOP:
    			finish();
    			break;
    			
    		case NETWORKRANGE:
    			
    			switch (event.getSubcommand()) {
	    			case CREATE:
	    				logger.info("Creating new Network Range: " + event.getPayload());
	    				
	    				loadNetworkAddress(event.getPayload());
	    				
	    				break;
	    				
	    			case REMOVE:
	    				logger.info("Removing Network Range: " + event.getPayload());
	    				NetworkRange selectedNr = null;
	    				
	    				for (NetworkRange nr: networkRanges)
	    					if (nr.getIP().equals(event.getPayload()))
	    						selectedNr = nr;
	    				
	    				if  (selectedNr!=null && selectedNr.getSubnets().size() == 0)
	    					networkRanges.remove(selectedNr);
	    				break;	    				
    			}
    			
    			break;
    			
    		case STATUS:
    			logger.info("Sending internal status");
    			
    			response = new AdministrativeEvent(System.currentTimeMillis(), 0, AdministrativeEvent.CommandType.STATUS);
    			
    			StringBuffer payload = new StringBuffer();
    			
    			payload.append("<SLM state=\"" + state.toString() + "\" >\n");
    			
    			payload.append("<DeployedServices>\n");
    			for (FSM fsmItr: fsmVector.values()) {
    				payload.append("<Service fqn=\"" + fsmItr.getServiceFQN() + "\" state=\"" + fsmItr.getStateText() + "\" />\n");
    			}
    				
    			payload.append("</DeployedServices>\n");
    			
    			payload.append("<NetworkRanges>\n");
    			
    			for (NetworkRange nr: networkRanges) {
    				payload.append(nr);
    			}
    			
    			payload.append("</NetworkRanges>\n");    			
    			
    			payload.append("</SLM>\n");
    			
    			response.setPayload(payload.toString());
    			
		    	try {
					busMediator.sendEvent(response, null);
					
				} catch (JMSException e1) {
					logger.error("Bus communication exception: " + e1.getMessage());
				}
				
    			break;
    	}
    }
    
    @SuppressWarnings("serial")
	private void processSmiEvent(SMIChannelEvent event) {
    	
    	logger.info("Processing a SMI Event");
    	
    	SMIChannelEvent e;
    	ServiceApplication sa;
    	VEE vee;
    	Document doc=null;
    	
    	switch(event.getAction()) {
    	
    	case GET_ORG:
    		
    		System.out.println ("get org");
    		
    		final String fqnOrgGet = event.get(SMIChannelEvent.FQN_ID);
    		String org = URICreation.getOrg(fqnOrgGet);
    		
    		logger.info("Org obtained " + org);
    		
    		
    		
    		//final String seqNumberGetOrg = event.get(SMIChannelEvent.SEQUENCE_NUMBER);
    		
			e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.GET_ORG);
			
			if (!SMConfiguration.getInstance().getSiteRoot().equals(org))
    		{
    			logger.error("The Org id " + org + " does not belong to the site deployed ");
        		e.setSuccess(false);
    			
    		}
			else 
			{	
			
	//		List<Customer> customers =  DbManager.getDbManager().getList(Customer.class);
			
			doc = getOrganizationXML(this.customers);

			
			final String xmlOrgRepresentation = DataTypesUtils.serializeXML(doc);
			System.out.println ("xml file " + xmlOrgRepresentation);
			
    		
    		e.put(SMIChannelEvent.ORG_DESCRIPTION, xmlOrgRepresentation);
    		e.setSuccess(true);
			}
    		
	    	try {
				busMediator.sendEvent(e,
									  new HashMap<String, String>() {{
										  put("org", fqnOrgGet);
										  put("action", SMIAction.GET_VDC.toString());
									//	  put(SMIChannelEvent.SEQUENCE_NUMBER, seqNumberGetOrg);
									  }});
				
				
			} catch (JMSException e1) {
				logger.error("Bus communication exception: " + e1.getMessage());
			}	
			
    		break;

    	     case DEPLOY:
	    		try {
	    			FQN name = deploy(event.get(SMIChannelEvent.OVF_DOCUMENT), event.get(SMIChannelEvent.CUSTOMER_NAME), event.get(SMIChannelEvent.SERVICE_NAME));
	    		
		    		e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.DEPLOY);
		    		e.put(SMIChannelEvent.FQN_ID, name.toString());
		    		
		    		e.setSuccess(true);
		    		
		    		final String customerName = event.get(SMIChannelEvent.CUSTOMER_NAME);

			    	try {
						busMediator.sendEvent(e,
											  new HashMap<String, String>() {{
												  put("customer", customerName);
												  put("action", SMIAction.DEPLOY.toString());
											  }});
						
					} catch (JMSException e1) {
						logger.error("Bus communication exception: " + e1.getMessage());
					}
					
	    		} catch (Exception ex) {
		    		e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.DEPLOY);
		    		e.put(SMIChannelEvent.FQN_ID, "");
		    		
		    		e.setSuccess(false);
		    		e.setMessage("Exception deploying service: " + e.getMessage());
		    		
		    		final String customerName = event.get(SMIChannelEvent.CUSTOMER_NAME);

			    	try {
						busMediator.sendEvent(e,
											  new HashMap<String, String>() {{
												  put("customer", customerName);
												  put("action", SMIAction.DEPLOY.toString());
											  }});
						
					} catch (JMSException e1) {
						logger.error("Bus communication exception: " + e1.getMessage());
					}
	    			
	    		}
	    		
	    		break;
	    		
	    	case UNDEPLOY:
	    		undeploy(new FQN(event.get(SMIChannelEvent.FQN_ID)));
	    		
	    		e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.UNDEPLOY);
	    		
	    		final String serviceName = event.get(SMIChannelEvent.FQN_ID);
		    	try {
					busMediator.sendEvent(e,
										  new HashMap<String, String>() {{
											  put("service", serviceName);
											  put("action", SMIAction.UNDEPLOY.toString());
										  }});
					
				} catch (JMSException e1) {
					logger.error("Bus communication exception: " + e1.getMessage());
				}
				
				FQN servFQN = new FQN(event.get(SMIChannelEvent.FQN_ID)); 
				ServiceApplication serv = (ServiceApplication) ReservoirDirectory.getInstance().getObject(servFQN);
				Customer cust = serv.getCustomer();
				if (cust.isServiceRegistered(serv)){
					cust.unregisterService(serv);
				}
				ReservoirDirectory.getInstance().removeMatchingNames(servFQN);
				// Removes the customer if it has no more services
				if (cust.getServices().isEmpty()){
					ReservoirDirectory.getInstance().removeMatchingNames(cust.getFQN());
					this.customers.remove(cust);
				}
				

				
				

				
	    		break;
	    		
	    	case GET_VAPP:
	    		
	    		final String serviceFQN = event.get(SMIChannelEvent.FQN_ID);
	    		
	    		e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.GET_VAPP);
	    		
	    		boolean success=false;
				try {
					sa = getApplication(new FQN(event.get(SMIChannelEvent.FQN_ID)));
					
					if (sa!=null) {
						doc = sa.toXML();
						
						success=true;
					}
					
				} catch (RemoteException e2) {
					logger.error("Error obtaining the Service application from its FQN: " + e2.getMessage());
				}
				
				if (!success) {
	                Element r = doc.createElement("ErrorSet");
	                doc.appendChild(r);
	                
	                Element unknown= doc.createElement("UnknownElements");
	                r.appendChild(unknown);
	                
	                Element element= doc.createElement("element");
	                unknown.appendChild(element);
	                
	                element.setAttribute("type", "vapp");
	                
	                try {
	                	element.setAttribute("ref", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort()  + "/" + URICreation.getURIService(serviceFQN));
	                } catch (IllegalArgumentException iae) {
	                	element.appendChild(doc.createElement(event.get(SMIChannelEvent.FQN_ID)));
	                }
				}
				
	    		
	    		final String xmlRepresentation = DataTypesUtils.serializeXML(doc);
	    		
	    		e.put(SMIChannelEvent.SERVICE_DESCRIPTION, xmlRepresentation);
		    	try {
					busMediator.sendEvent(e,
										  new HashMap<String, String>() {{
											  put("service", serviceFQN);
											  put("action", SMIAction.GET_VAPP.toString());
										  }});
					
				} catch (JMSException e1) {
					logger.error("Bus communication exception: " + e1.getMessage());
				}	    		
	    		break;
	    		
	    	case GET_VEE:
	    		
	    		final String veeFQN = event.get(SMIChannelEvent.FQN_ID);
	    		
	    		e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.GET_VEE);
	    		
	    		success=false;
				try {
					vee = getVee(new FQN(event.get(SMIChannelEvent.FQN_ID)));
					
					if (vee!=null) {
						doc = vee.toXML();
						
						success=true;
					}
					
				} catch (RemoteException e2) {
					logger.error("Error obtaining the VEE from its FQN: " + e2.getMessage());
				}
				
				if (!success) {
	                Element r = doc.createElement("ErrorSet");
	                doc.appendChild(r);
	                
	                Element unknown= doc.createElement("UnknownElements");
	                r.appendChild(unknown);
	                
	                Element element= doc.createElement("element");
	                unknown.appendChild(element);
	                
	                element.setAttribute("type", "vapp");
	                
	                try {
	                	element.setAttribute("ref", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort()  + "/" + URICreation.getURIService(veeFQN));
	                } catch (IllegalArgumentException iae) {
	                	element.appendChild(doc.createElement(event.get(SMIChannelEvent.FQN_ID)));
	                }
				}
				
	    		
	    		String xml = DataTypesUtils.serializeXML(doc);
	    		
	    		e.put(SMIChannelEvent.VEE_DESCRIPTION, xml);
		    	try {
					busMediator.sendEvent(e,
										  new HashMap<String, String>() {{
											  put("vee", veeFQN);
											  put("action", SMIAction.GET_VEE.toString());
										  }});
					
				} catch (JMSException e1) {
					logger.error("Bus communication exception: " + e1.getMessage());
				}	    		
	    		break;
	    		
	    	case GET_VDC:
	    		
				try {
					 cust = ((Customer)ReservoirDirectory.getInstance().getObject(new FQN(event.get(SMIChannelEvent.FQN_ID))));
					e = new SMIChannelEvent(System.currentTimeMillis(), 0, SMIAction.GET_VDC);
					
		            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		            DocumentBuilder docBuilder;
					
		            docBuilder = dbfac.newDocumentBuilder();
					doc = docBuilder.newDocument();
					
					
					if (cust != null) {
		                Element r = doc.createElement("VDC");
		                doc.appendChild(r);

		                r.setAttribute("name", cust.getCustomerName());
		                
		                for (ServiceApplication srvApp: cust.getServices()) {
		                    Element link = (Element) doc.createElement("Link");
		                    link.setAttribute("rel", "down");
		                    link.setAttribute("type", "application/vnd.telefonica.tcloud.vapp+xml");
		                //    link.setAttribute("href", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort()  + "/" + URICreation.getURIService(srvApp.getFQN().toString()));
		                    
		                    link.setAttribute("href", "@HOSTNAME" + URICreation.getURIService(srvApp.getFQN().toString()));
		                    r.appendChild(link);
		                    
		                }
					} else {
		                Element r = doc.createElement("ErrorSet");
		                doc.appendChild(r);
		                
		                Element unknown= doc.createElement("UnknownElements");
		                r.appendChild(unknown);
		                
		                Element element= doc.createElement("element");
		                unknown.appendChild(element);
		                
		                element.setAttribute("type", "vdc");
		                
		                try {
		                	element.setAttribute("ref", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort()  + "/" + URICreation.getVDC(event.get(SMIChannelEvent.FQN_ID)));
		                } catch (IllegalArgumentException iae) {
		                	element.appendChild(doc.createElement(event.get(SMIChannelEvent.FQN_ID)));
		                }
					}
					
					doc.normalizeDocument();
					final String xmlCustomerRepresentation = DataTypesUtils.serializeXML(doc);
		    		
		    		final String customerFQN =event.get(SMIChannelEvent.FQN_ID);
		    		e.put(SMIChannelEvent.CUSTOMER_DESCRIPTION, xmlCustomerRepresentation);
			    	try {
						busMediator.sendEvent(e,
											  new HashMap<String, String>() {{
												  put("customer", customerFQN);
												  put("action", SMIAction.GET_VAPP.toString());
											  }});
						
					} catch (JMSException e1) {
						logger.error("Bus communication exception: " + e1.getMessage());
					}	
					
				} catch (ParserConfigurationException ex) {

				}
				
	    		break;
	    		
    		default:
    			
    	}
    	
    }
    
    /**
     * SMI implemented method.
     * allows the SM servlet to get the sap of a given service to later communicate with the gadgets and show the SP all the information
     * @param serviceID
     * @return
     * @throws java.rmi.RemoteException
     */
/*    public ServiceApplication getApplication(FQN serviceID) throws RemoteException {
        // it looks for the SAP into the ReservoirDirectory
        // this could obviously have been done by the servlet itself
        // but in the near future they both may dwell on different machines and this method actually invoked through RMI calls
        return ((ServiceApplication)ReservoirDirectory.getInstance().getObject(serviceID));
    }*/
    
    /**
     * SMI implemented method.
     * allows the SM servlet to get the VEE of a given service to later communicate with the gadgets and show the SP all the information
     * @param veeID
     * @return
     * @throws java.rmi.RemoteException
     */
    public VEE getVee(FQN veeID) throws RemoteException {
        // it looks for the SAP into the ReservoirDirectory
        // this could obviously have been done by the servlet itself
        // but in the near future they both may dwell on different machines and this method actually invoked through RMI calls
        return ((VEE)ReservoirDirectory.getInstance().getObject(veeID));
    }
    
    
    public ServiceApplication getApplication(FQN serviceID) throws RemoteException {
        // it looks for the SAP into the ReservoirDirectory
        // this could obviously have been done by the servlet itself
        // but in the near future they both may dwell on different machines and this method actually invoked through RMI calls
        return ((ServiceApplication)ReservoirDirectory.getInstance().getObject(serviceID));
    }
    
    /**
     * Check all the available network ranges for a network address meeting the size requirements. If 
     * a suitable network is not found, returns null. 
     * 
     * @param size
     * 		Minimum size of the network (number of hosts)
     * 
     * @param publicNetwork
     * 		Whether the returned address should be public or not.
     */
    public String[] getNetworkAddress(long size, boolean publicNetwork) {

    	logger.info("Asking LCC for a " + ((publicNetwork)?"public":"private") + " network of size: " + size);
    	
    	for (NetworkRange nr: networkRanges) {
    		
    		if (nr.isPublic() != publicNetwork) continue;
    		
    		logger.info("Triying to get Network from range: " + nr.getIP());
    		
    		String[] address = nr.getNetwork(size);
    		
    		if (address!= null) {
        		String[] result = new String[] {address[0],
						address[1],
						nr.getDNS(),
						nr.getGateway()
						};
        		
    			logger.info("Network leased: [address=" + result[0] + ", mask=" + result[1] + ", dns=" + result[2] + ", gateway=" + result[3]);
    			
       			// Save the network ranges
    	    	logger.info("Saving network information.");
    	    	DbManager.getDbManager().save(nr);
    	    	
    			return result;
    		}
    		
    		logger.info("No subnetwork could be leased");
    	}
    	
    	logger.error("No suitable network could be created.");
    	return null;
    }
    
    /**
     * Returns an IP address for a host, belonging to the specified network.
     *  
     * @param network
     * 		Target network.
     * 
     * @return
     * 		An IP address belonging to the target network if there was one available.
     * 		null otherwise. Note that the method doesn't make a difference between the
     * 		case where there are no IPs left on the desired network and the one in which
     * 		the network itself doesn't belong to any of the actual ranges of the LCC.
     */
    public String getHostAddress(String network) {
    	
    	logger.info("Asking LCC for a host address on network: " + network);
    	
    	for (NetworkRange nr: networkRanges) {
    		if (nr.isNetworkInRange(network)) {
    			String ipLeased = nr.getNetworkAddress(network);
    			logger.info("IP address [" + ipLeased + "] leased");

    			// Save the network ranges
    	    	logger.info("Saving network information.");
    	    	DbManager.getDbManager().save(nr);

    			return ipLeased;
    		}
    	}
    	
    	logger.error("No host address could be leased");
    	return null;
    }
    
    public void releaseHostAddress(String ip, String network) {
    	
    	logger.info("Releasing host address [" + ip + "] on network [" + network + "]");
    	
    	for (NetworkRange nr: networkRanges) {
    		if (nr.isNetworkInRange(network)) {
    			nr.releaseAddress(ip, network);
    			
    	    	// Save the network ranges
    	    	logger.info("Saving network information.");
    	    	DbManager.getDbManager().save(nr);
    	    	
    	    	break;
    		}
    	}
    }
    
    /**
     * Prepares the SLM to be stopped. Save the state when appropiate, stop all the services and
     * undeploy them.
     */
    public void finish() {
    	
    	logger.info("LCC changing to FINISHING state.");
    	state = LccStates.FINISHING;
    	
    	if (SMConfiguration.getInstance().isUndeployOnServertopFlagSet()) {
	    	// Tell all the FSMs to undeploy its services.
	    	for (String fqn: fsmVector.keySet()) {
	    		undeploy(new FQN(fqn));
	    	}
	    	
	    	// Wait for the threads to orderly join its parent.
	    	for (String fqn: fsmVector.keySet()) {	
	    		FSM machine = fsmVector.get(fqn);
	    		
	    		try {
					machine.join(FSM_FINISH_TIMEOUT);
				} catch (InterruptedException e) {
					logger.error("SLM interrupted while waiting for FSM finishing.");
				}
				
				if (machine.isAlive()) {
					logger.warn("Service [" + fqn + "] did not stop on time. It will be killed on exit.");
				}
	    	}
    	}
    	
    	logger.info("LCC finished"); 
    	System.exit(0);
    }
    
    public static Document getOrganizationXML(List<Customer> vdcList) {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        dbfac.setNamespaceAware(true);
        
        try {
			docBuilder = dbfac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
	        Element r = doc.createElement("Org");
	        doc.appendChild(r);
	        
	        r.setAttribute("name", getSiteRoot());
	        //r.setAttribute("href", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort() + URICreation.getURIOrg(siteRoot));
	        r.setAttribute("href", "@HOSTNAME" + URICreation.getURIOrg(getSiteRoot()));
	        
	        for (Customer vDatacenter: vdcList) {
	            Element link = (Element) doc.createElement("Link");
	            link.setAttribute("rel", "down");
	            link.setAttribute("type", "application/vnd.telefonica.tcloud.vdc+xml");
	            link.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vDatacenter.getFQN().toString()));
	            r.appendChild(link);
	        }

            Element vdcAddlink = (Element) doc.createElement("Link");
            vdcAddlink.setAttribute("rel", "add");
            vdcAddlink.setAttribute("type", "application/vnd.telefonica.tcloud.vdc+xml");
            vdcAddlink.setAttribute("href", "@HOSTNAME" + URICreation.URI_VDC_ADD.replace("{org-id}", getSiteRoot().replace(".", "_")));
            r.appendChild(vdcAddlink);
	        
            Element link = (Element) doc.createElement("Link");
            link.setAttribute("rel", "tasks");
            link.setAttribute("type", "application/vnd.telefonica.tcloud.tasklist+xml");
            link.setAttribute("href", "@HOSTNAME" + URICreation.getURIOrg(getSiteRoot()) + "/task");
            r.appendChild(link);
            
            return doc;
	        
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			
			return null;
		}
	}
    
    private static String getSiteRoot() {
    	String siteRoot = null;
		if (siteRoot == null) {
			siteRoot = ReservoirDirectory.ROOT_NAME_SPACE;
		}
		return siteRoot;
	}
    
	public void addCustomer(Customer client) {
		this.customers.add(client);

	}



}