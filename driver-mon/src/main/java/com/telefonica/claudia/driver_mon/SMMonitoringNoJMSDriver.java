package com.telefonica.claudia.driver_mon;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.deployment.NIC;
import com.telefonica.claudia.slm.deployment.Network;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VDC;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.Zone;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent;
import com.telefonica.claudia.slm.eventsBus.events.SMIChannelEvent.SMIAction;
import com.telefonica.claudia.slm.monitoring.nodedirectory;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.driver_mon.DriverClothoDbManager;
import com.telefonica.claudia.driver_mon.ClothoUtils;
import com.telefonica.claudia.driver_mon.DBUtils;
import com.telefonica.claudia.driver_mon.ClothoUtils.JMSData;
import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.MonitoringDriver;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValue;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;

public class SMMonitoringNoJMSDriver implements MonitoringDriver {

	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.SMMonitoringDriver");
	
	DriverClothoDbManager db;
	TCloudMonitoringClient restClient;
	
	JMSData jmsConnectionData;
	private String connFactoryName = null;
	private String serverProviderUrl = null;
	private String namingFactory = null;
	
	//yo
	//la url está bien escrita?
	private final static String USER_PROPERTY = "monitoring.login";
	private final static String PASSWORD_PROPERTY = "monitoring.password";	
	private final static String URL_PROPERTY = "monitoring.uri";
	private  static String url = "jdbc:mysql://109.231.67.227:3306/monitoring?noDatetimeStringSync=true";
	private  static String username = "claudia";
	private  static String password = "ClaudiaPass";
	
    public SMMonitoringNoJMSDriver(Properties prop) throws NamingException, ClassNotFoundException, JMSException { 
        // Creates the bus manager and attach it to the context as an attribte.
      /*  try {
        	this.connFactoryName = ClothoUtils.DEFAULT_CONN_FACTORY_NAME;
        	
    		namingFactory = prop.getProperty(ClothoUtils.JNDI_NAMING_FACTORY);
    		serverProviderUrl = prop.getProperty(ClothoUtils.JNDI_SERVER_PROVIDER_URL);
        	
        	// Open the JMS Session to create the queue conections
    		this.jmsConnectionData = ClothoUtils.openJMSSession(serverProviderUrl, namingFactory, connFactoryName);
        	ClothoUtils.cleanQueues(jmsConnectionData);
        	
        	jmsConnectionData.smiChannelProducer = jmsConnectionData.session.createProducer(jmsConnectionData.smiChannelRequest);	

		} catch (JMSException e) {
			log.error("The connection to the bus could not be created. " + e.getMessage());
			throw e;
		} catch (ClassNotFoundException e) {
			log.error("The Factory class could not be found");
			throw e;
		}*/
    	
    	if (prop!= null && prop.getProperty(USER_PROPERTY)!=null)
    	{
    		username = prop.getProperty(USER_PROPERTY);

    	}
    	if (prop!= null &&prop.getProperty(PASSWORD_PROPERTY)!=null)
    	{
    		password = prop.getProperty(PASSWORD_PROPERTY);
    		
    	}
    	if (prop!= null &&prop.getProperty(URL_PROPERTY)!=null)
    	{
    		url = prop.getProperty(URL_PROPERTY);
    		
    	}
    	System.out.println("Connection data: URL " + url + " user " + username + " password " + password);
		restClient = new TCloudMonitoringClient();
		
		//meter url, usuario y contraseña a mano
		
		db = new DriverClothoDbManager(url, false, username, password);
  
		
    	//db = new DriverClothoDbManager(DBUtils.getURL(prop), false, DBUtils.getUser(prop), DBUtils.getPassword(prop));
    }
    
    public DriverClothoDbManager getDB ()
    {
    	return db;
    }
    
    public String[] getElementAndHref(String orgName,
			String vdcName, List<String> vappNames,
			String hwItem, String networkName) {
    	
    	String element;
    	String href="";
    	
		if (vappNames!= null) {
			if (networkName!=null)
				element = URICreation.getNetworkFQN(orgName, vdcName, networkName);
			else {
				if (hwItem!= null) {
					if (hwItem.contains("networks_")) {
						element= URICreation.getFQN(orgName, vdcName, vappNames) + "." + URICreation.FQN_SEPARATOR_NET + "." + hwItem.substring(hwItem.indexOf('_')+1);
					} else {
						element= URICreation.getFQN(orgName, vdcName, vappNames, hwItem);
					}
					
					href = URICreation.getURIVEEReplica(element) + "/hw/" + hwItem;
				} else {
					element= URICreation.getFQN(orgName, vdcName, vappNames);
					
					if (vappNames.size() == 1) {
			        	href = URICreation.getURIService(element);
			        } else if (vappNames.size() == 2) {
			        	href = URICreation.getURIVapp(element);
			        } else if (vappNames.size() == 3) {
						href = URICreation.getURIVEEReplica(element);
			        }
				}
			}
		} else {
			element= URICreation.getFQN(orgName, vdcName);
			href = URICreation.getURIVDC(element);
		}
		
		return new String[] {element, href};
    }
    
	public MeasureDescriptor getGenericMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames,
			String measureId, String hwItem, String networkName) throws MonitorException {
		
        String[] elementHref = getElementAndHref(orgName, vdcName, vappNames, hwItem, networkName);
		
		nodedirectory nd = db.get(nodedirectory.class, elementHref[0]);
		
		if ((measureId.contains("netInput_")||measureId.contains("netOutput_"))&&
				nd.getTipo()!=nodedirectory.TYPE_NIC) {
			return new MeasureDescriptor(measureId, nd.getFqnString() + "." + URICreation.FQN_SEPARATOR_MEASURE + "." + measureId, 
					  "", DataTypesUtils.STANDARD_BANDWIDTH_UNIT_DEFAULT, "0", "0", "");
		} else if (measureId.equals("diskUsage")) {
			return db.getDiskMeasureDescriptor(nd);
			
		} else {
			MeasureDescriptor result = db.getMeasureDescriptors(nd, measureId);
			
			if (result==null)
				throw new MonitorException("Measure " + measureId + " not available.");
				
			result.setHref(elementHref[1]);
			
			return result;
		}
	}

	public MeasureDescriptorList getGenericMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames, String item, String networkName)
			throws MonitorException {

        String[] elementHref = getElementAndHref(orgName, vdcName, vappNames, item, networkName);
		
		MeasureDescriptorList result = new MeasureDescriptorList(elementHref[1], "");
		System.out.println (result.getHref());
		nodedirectory nd = db.get(nodedirectory.class, elementHref[0]);
		
		if (nd == null) 
			return result;
		
		List<MeasureDescriptor> descriptorList = db.getMeasureDescriptorsList(nd);
		
		for (MeasureDescriptor md: descriptorList) {
			md.setHref(elementHref[1] + md.getName());
			result.add(md);
		}
		
		if (nd.getTipo()== nodedirectory.TYPE_VDC) {
			VDC vdc = db.get(VDC.class, nd.getFqnString());
			
			for (Zone z: vdc.getZones())
				for (Network network: z.getNetworks()) {
					result.add(new MeasureDescriptor("netInput_"+ network.getName(), nd.getFqnString() + "." + URICreation.FQN_SEPARATOR_MEASURE + "." + "netInput_" + network.getName(), 
													  "", DataTypesUtils.STANDARD_BANDWIDTH_UNIT_DEFAULT, "0", "0", ""));
					result.add(new MeasureDescriptor("netOutput_"+ network.getName(), nd.getFqnString() + "." + URICreation.FQN_SEPARATOR_MEASURE + "." + "netOutput_" + network.getName(), 
							  "", DataTypesUtils.STANDARD_BANDWIDTH_UNIT_DEFAULT, "0", "0", ""));
				}
			
		} else if (nd.getTipo()== nodedirectory.TYPE_SERVICE) {
			
			ServiceApplication sap = db.getServiceApplication(nd.getFqnString());
			
			for (Network network: sap.getNetworks()) {
				result.add(new MeasureDescriptor("netInput_" + network.getName(), nd.getFqnString() + "." + URICreation.FQN_SEPARATOR_MEASURE + "." + "netInput_" + network.getName(), 
												  "", DataTypesUtils.STANDARD_BANDWIDTH_UNIT_DEFAULT, "0", "0", ""));
				result.add(new MeasureDescriptor("netOutput_"+ network.getName(), nd.getFqnString() + "." + URICreation.FQN_SEPARATOR_MEASURE + "." + "netOutput_" + network.getName(), 
						  "", DataTypesUtils.STANDARD_BANDWIDTH_UNIT_DEFAULT, "0", "0", ""));
			}
		}
		
		return result;
	}

	public MeasuredValueList getMeasuredValueListBD(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		String id = md.getMeasurementTypeId();
		String element = id.substring(0, id.indexOf(URICreation.FQN_SEPARATOR_MEASURE) - 1);
		
		nodedirectory nd = db.get(nodedirectory.class, element);
		List<MeasuredValue> valueList;
		
		if ((md.getName().contains("netInput_")||md.getName().contains("netOutput_"))&&nd.getTipo()!=nodedirectory.TYPE_NIC) {
			valueList = db.getNetworkValues(nd, filter.getFrom(), filter.getTo(), 
					filter.getSamples(), filter.getInterval(), md.getName(), md.getValueType());
		} else {
			valueList = db.getMeasureValues(nd, filter.getFrom(), filter.getTo(), 
					filter.getSamples(), filter.getInterval(), md.getName(), md.getValueType());
		}
		
		MeasuredValueList mvl = new MeasuredValueList(md);
		
		for (MeasuredValue mv: valueList) {
			mvl.add(mv);
		}
		
    	return mvl;
	}
	
	public MeasuredValueList getMeasuredValueListRest(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		
		// Find the data to create the URL
		String url;
		
		nodedirectory nd = db.get(nodedirectory.class, md.getMeasurementTypeId().substring(0, md.getMeasurementTypeId().indexOf(".measure")));
		
		VEEReplica rep;
		
		if (nd==null||(nd.getTipo()!=nodedirectory.TYPE_REPLICA&&nd.getTipo()!=nodedirectory.TYPE_NIC))
			return new MeasuredValueList(md);
		
		String path;
		
		if (nd.getTipo()==nodedirectory.TYPE_REPLICA) {
			path=URICreation.getURIVEEReplica(md.getMeasurementTypeId());
			rep = db.get(VEEReplica.class, nd.getInternalNodeId());
		} else if (nd.getTipo()==nodedirectory.TYPE_NIC) {
			path=URICreation.getURIHwItem(md.getMeasurementTypeId());
			path=translateNetInfrastructureId(path, nd.getFqnString());
			rep = db.get(VEEReplica.class, nd.getFqnString().substring(0, md.getMeasurementTypeId().indexOf(".network")));
		} else {
			return new MeasuredValueList(md);
		}
		
		if (rep.getDeployedOn()==null)
			return new MeasuredValueList(md);
		else {
			url = "http://" + rep.getDeployedOn().getHost() + ":" + rep.getDeployedOn().getPort() + path + URICreation.MONITOR + "/" + md.getName() + URICreation.VALUES; 
			
			// Get the measures
			return restClient.getReplicaMeasure(url, filter, md);
		}
	}
	
	private String translateNetInfrastructureId(String path, String fqn) {
		NIC nic = db.get(NIC.class, fqn);
		return path.replaceAll("networks_.*", nic.getInstanceId());
	}

	public MeasuredValueList getMeasuredValueListJMS(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		
    	try {
    		SMIChannelEvent smi = new SMIChannelEvent(0, 0, SMIAction.GET_MEASURE_VALUE);
    		
			Long seqNumber= ClothoUtils.getSecuenceNumber();
			smi.put(SMIChannelEvent.SEQUENCE_NUMBER, seqNumber.toString());
			
			String id = md.getMeasurementTypeId();
			
			smi.put(SMIChannelEvent.FQN_ID, id.substring(0, id.indexOf(URICreation.FQN_SEPARATOR_MEASURE) - 1));
			
			smi.put(SMIChannelEvent.MEASURE_NAME, md.getName());
			smi.put(SMIChannelEvent.MEASURE_TYPE_ID, md.getMeasurementTypeId());
			smi.put(SMIChannelEvent.MEASURE_FILTER_FROM, (filter.getFrom()==null)?null:DataTypesUtils.date2String(filter.getFrom().getTime()));
			smi.put(SMIChannelEvent.MEASURE_FILTER_TO, (filter.getTo()==null)?null:DataTypesUtils.date2String(filter.getTo().getTime()));
			smi.put(SMIChannelEvent.MEASURE_FILTER_NUMBER, String.valueOf(filter.getSamples()));
			smi.put(SMIChannelEvent.MEASURE_FILTER_INTERVAL, String.valueOf(filter.getInterval()));
			
			jmsConnectionData.smiChannelProducer.send(jmsConnectionData.session.createObjectMessage(smi));			
			
			MessageConsumer messageConsumer = jmsConnectionData.session.createConsumer(jmsConnectionData.smiChannelReply, "element = '" + md.getMeasurementTypeId() + "' AND " + SMIChannelEvent.SEQUENCE_NUMBER + "='" + seqNumber + "'");
			ObjectMessage m = (ObjectMessage) messageConsumer.receive(ClothoUtils.EVENT_BUS_TIMEOUT);
			
			if (m==null) {
				log.error("Timeout waiting for a server response. Try later.");
				
				throw new MonitorException("Timeout waiting for a server response. Try later.");
			}
			
			smi = (SMIChannelEvent) m.getObject();
			Map<String, String> values = (HashMap<String, String>) smi.getSerializable(SMIChannelEvent.MEASURE_VALUES);
			String units = smi.get(SMIChannelEvent.MEASURE_UNITS);
			
			MeasuredValueList mvl = new MeasuredValueList();
			mvl.setHref(md.getHref() + URICreation.VALUES);

			for (String measureDate: values.keySet()) {
				MeasuredValue mv = new MeasuredValue();

				mv.setUnit(units);
				mv.setValue(values.get(measureDate));
				
				try {
					mv.setRegisterDate(DataTypesUtils.string2Date(measureDate));
				} catch (ParseException e) {
					log.error("SMDeploymentDriver: Wrong date format received: " + measureDate);
					continue;
				}
				
				mvl.add(mv);
			}
			
			return mvl;
			
		} catch (JMSException e) {
			log.error("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve service data.",e);
			throw new MonitorException("SMDeploymentDriver: Communication error accessing the SLM. Impossible to retrieve service data." + e.getMessage());
		}
	}

	public MeasureDescriptor getNetMeasureDescriptor(String orgName,
			String vdcName, String netName, String measureId)
			throws MonitorException {
		
		return getGenericMeasureDescriptor(orgName,vdcName, null, null, null, netName);
	}

	public MeasureDescriptorList getNetMeasureDescriptorList(String orgName,
			String vdcName, String netName) throws MonitorException {
		
		return getGenericMeasureDescriptorList(orgName, vdcName, null, null, netName);
	}

	
	
	
	

	public MeasureDescriptor getVdcMeasureDescriptor(String orgName,
			String vdcName, String measureId) throws MonitorException {
		
		return getGenericMeasureDescriptor(orgName,vdcName, null, measureId, null, null);
	}

	public MeasureDescriptorList getVdcMeasureDescriptorList(String orgName,
			String vdcName) throws MonitorException {
		
		return getGenericMeasureDescriptorList(orgName, vdcName, null, null, null);
	}

	
	

	public List<MeasuredValueList> getMeasuredValueList(
			List<MeasureDescriptor> md, MeasuredValueFilter filter)
			throws MonitorException {
		
		List<MeasuredValueList> result = new ArrayList<MeasuredValueList>();
		
		for (MeasureDescriptor descriptor: md)
			result.add(getMeasuredValueList(descriptor, filter));
		
		return result;
	}

	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		
	
		if (filter.getInterval() != 300 && filter.getInterval() != 3600 && filter.getInterval() != 86400)
			return getMeasuredValueListRest(md, filter);
		else
			return getMeasuredValueListBD(md, filter);
	}


	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName,
			String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return getGenericMeasureDescriptor(orgName,vdcName, vappNames, measureId, hwItemName, null);
	}
	
	
	
	
	

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName)
			throws MonitorException {
		// TODO Auto-generated method stub
		return getGenericMeasureDescriptorList(orgName, vdcName, vappNames, hwItemName, null);
	}
	
	
	

	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String measureId)
			throws MonitorException {
		return getGenericMeasureDescriptor(orgName,vdcName, vappNames, measureId, null, null);
	}
	

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames)
			throws MonitorException {
		// TODO Auto-generated method stub
		return getGenericMeasureDescriptorList(orgName, vdcName, vappNames, null, null);
	}

	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String hwItemName,
			String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames, String hwItemName)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String measureId)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,
			int samples) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
/*	
	

	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String measureId)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}*/
}
