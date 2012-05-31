package com.telefonica.claudia.smi.monitoring;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.claudia.smi.Main;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValue;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
//import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;



 

public class OneMonitoringDriver implements MonitoringDriver {

	
	private String oneURL ;
	private String oneSession;
	private final static String URL_PROPERTY = "oneUrl";
	private final static String USER_PROPERTY = "oneUser";
	private final static String PASSWORD_PROPERTY = "onePassword";	
	private XmlRpcClient xmlRpcClient;
	private final static String VM_GETALL_COMMAND = "one.vmpool.info";
	private final static String VM_GETINFO_COMMAND = "one.vm.info";
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("com.telefonica.claudia.smi.monitoring.ONEMonitoringDriver");
	
	HashMap<String,String> table = new HashMap ();
	
	/**
	 * Collection containing the mapping from fqns to ids. This mapped is used as a cache
	 * of the getVmId method (vm ids never change once assigned). 
	 */
	private Map<String, Integer> idVmMap = new HashMap<String, Integer>();
	
	//------------------
	public OneMonitoringDriver() {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		
		log.info("Creating OpenNebula conector");

	    	oneURL = "http://62.217.120.136:2633/RPC2";


		

	    	oneSession = "OCCIServer:42d9d2622f862cd803d4395be2c1edd362213525";
	    	
	    	try {
				config.setServerURL(new URL(oneURL));
			} catch (MalformedURLException e) { 
				log.error("Malformed URL: " + oneURL);
				throw new RuntimeException(e);
			}
		
			xmlRpcClient = new XmlRpcClient();
			log.info("XMLRPC client created"); 
			xmlRpcClient.setConfig(config);
			log.info("XMLRPC client configured");

	}
	
	//------------------
	
	
	public OneMonitoringDriver(Properties prop) {
		
		table.put("cpus", "CPU");
		table.put("memory", "MEMORY");
		table.put("disks", "DISK");
		table.put("network", "NIC");
		
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		
		log.info("Creating OpenNebula conector");
		if (prop.containsKey(URL_PROPERTY)) {
	    	oneURL = (String) prop.get(URL_PROPERTY);
	    	log.info("URL created: " + oneURL);
	    }
		
		if (prop.containsKey(USER_PROPERTY)&&prop.containsKey(PASSWORD_PROPERTY)) {
	    	oneSession = ((String) prop.get(USER_PROPERTY)) + ":" + ((String) prop.get(PASSWORD_PROPERTY));
	    	log.info("Session created: " + oneSession);
	    }
		
		/*if (prop.containsKey(Main.CUSTOMIZATION_PORT_PROPERTY)) {
	    	customizationPort = ((String) prop.get(Main.CUSTOMIZATION_PORT_PROPERTY));
	    }*/
		
		
		/*oneSession = "oneadmin:5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
		oneURL = "http://localhost:2633/RPC2";*/
		
		try {
			config.setServerURL(new URL(oneURL));
		} catch (MalformedURLException e) { 
			log.error("Malformed URL: " + oneURL);
			throw new RuntimeException(e);
		}
	
		xmlRpcClient = new XmlRpcClient();
		log.info("XMLRPC client created"); 
		xmlRpcClient.setConfig(config);
		log.info("XMLRPC client configured");
	}
	
	/**
	 * Retrieve a map of the currently deployed VMs, and its ids.
	 * 
	 * @return
	 * 		A map where the key is the VM's FQN and the value the VM's id.
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> getVmIds() throws Exception {
		
		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(-2);
		
		HashMap<String, Integer> mapResult = new HashMap<String, Integer>();
		
		Object[] result = null;
		try {				
			result = (Object[])xmlRpcClient.execute(VM_GETALL_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			throw new IOException ("Error obtaining the VM list: " + ex.getMessage(), ex);
		}
		
		boolean success = (Boolean)result[0];

		if(success) {
			String resultList = (String) result[1];
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new ByteArrayInputStream(resultList.getBytes()));
				
				NodeList vmList = doc.getElementsByTagName("VM");
				
				for (int i=0; i < vmList.getLength(); i++) {
					
					Element vm = (Element) vmList.item(i);
					
					String fqn = ((Element)vm.getElementsByTagName("NAME").item(0)).getTextContent();
					try {
						Integer value = Integer.parseInt(((Element)vm.getElementsByTagName("ID").item(0)).getTextContent());
						mapResult.put(fqn, value);
					} catch(NumberFormatException nfe) {
						log.warn("Numerical id expected, got [" + ((Element)vm.getElementsByTagName("ID").item(0)).getTextContent() + "]");
						continue;
					}
				}
				
				return mapResult;
				
			} catch (ParserConfigurationException e) {
				log.error("Parser Configuration Error: " + e.getMessage());
				throw new IOException ("Parser Configuration Error", e);
			} catch (SAXException e) {
				log.error("Parse error reading the answer: " + e.getMessage());
				throw new IOException ("XML Parse error", e);
			}
			
		} else {
			log.error("Error recieved from ONE: " + result[1]);
			throw new Exception("Error recieved from ONE: " + result[1]);
		}
	}
	
	/**
	 * Retrieve the vm's id given its fqn.
	 * 
	 * @param fqn
	 * 		FQN of the Virtual Machine (mapped to its name property in ONE).
	 * 
	 * @return
	 * 		The internal id of the Virtual Machine if it exists or -1 otherwise.
	 * 
	 * @throws Exception 
	 * 		
	 */
	protected Integer getVmId(String fqn) throws Exception {
		
		if (!idVmMap.containsKey(fqn)) 
			idVmMap = getVmIds();
		
		if (idVmMap.containsKey(fqn))
			return idVmMap.get(fqn);
		else
			return -1;
	}
	
    
	
	@Override
	public MeasureDescriptorList getVdcMeasureDescriptorList(String orgName,
			String vdcName) throws MonitorException {
		
		
		MeasureDescriptorList mdl=new MeasureDescriptorList();
		return mdl;
		/*String vdcFQN = URICreation.getFQN(orgName, vdcName);
		log.info("vdcFQN: " + vdcFQN);		
		int elementId;
		try {
			//elementId = wClient.getElementId(vdcFQN, VDC_ET_ID);
			elementId = getVmId(vdcFQN);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MonitorException(e.getMessage());
		} 
		
		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(new Integer(elementId));
		
		log.debug("Virtual machine info requested for id: " + elementId);
		
		Object[] result = null;
		try {				
			result = (Object[])xmlRpcClient.execute(VM_GETINFO_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			log.error("Connection error trying to get VM information: " + ex.getMessage());
			throw new MonitorException ("Error on reading VM state , XMLRPC call failed"+ ex);
		}
		
		boolean completed = (Boolean) result[0];
		
		if (completed) {
			
			String resultList = (String) result[1];
			XPathFactory  factory=XPathFactory.newInstance();
			XPath xPath=factory.newXPath();
			XPathExpression xPathExpression = null;
			try {
				xPathExpression = xPath.compile("/VM/TEMPLATE/NIC/IP");
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputSource inputSource = null;
			try {
				inputSource = new InputSource(new FileInputStream(resultList));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				String ip = xPathExpression.evaluate(inputSource);
				mdl = new MeasureDescriptorList();
				mdl.setHref("http://"+ip+vdcFQN);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			log.error("VM Info request failed: " + result[1]);
			
		}
		//obtener de open nebula con el xmlrpcclient el xml de la maquina
		//leer de ese xml los datos q necesito
		//crear un objeto MeasureDescriptor y llenarlo con esos datos (utilizando los metodos
		//implementados en esa clase)
		//devolver el 
		*/
		
	
	}

	@Override
	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames) throws MonitorException {
		// TODO Auto-generated method stub
		
			String vappFQN = URICreation.getFQN(orgName, vdcName, vappNames);
			log.info("vappFQN: " + vappFQN);
			int elementId;
			MeasureDescriptorList mdl;
			try {
				switch(vappNames.size()) {
				 	case 1: mdl=new MeasureDescriptorList(); return mdl; 
				 	case 2: mdl=new MeasureDescriptorList(); return mdl; 
				 	default: elementId=this.getVmId(vappFQN); break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new MonitorException(e.getMessage());
			} 
			if (elementId == -1)
				throw new MonitorException("Virtual Application Not Found");
			return getMeasureDescriptorList(elementId);
		}


	@Override
	public MeasureDescriptorList getNetMeasureDescriptorList(String orgName,
			String vdcName, String netName) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName)
			throws MonitorException {
		
		// TODO Auto-generated method stub
		return null;
	}

	//private
	@SuppressWarnings("unchecked")
	private MeasureDescriptorList getMeasureDescriptorList(int elementId) {
		// TODO Auto-generated method stub
		MeasureDescriptorList mdl = new MeasureDescriptorList();
		
		
		log.debug("Virtual machine info requested for id: " + elementId);
		
		Object result [] = getOneInfoParams (elementId);
		
		boolean completed = (Boolean) result[0];
		
		if (completed) {
			
			String resultList = (String) result[1];
			System.out.println (resultList);
		
			
			//File f=new File("op.xml");
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				//Document doc = builder.parse(new File("1.xml"));
				Document doc = builder.parse(new ByteArrayInputStream(resultList.getBytes()));
				String expression = "/VM/TEMPLATE/DISK";      
				XPath xpath=XPathFactory.newInstance().newXPath();
				NodeList nodes = (NodeList) xpath.evaluate(expression, doc.getDocumentElement(), XPathConstants.NODESET);
				
				
				for (int i=0; i < nodes.getLength(); i++) {
					
					System.out.println (nodes.item(i).getNodeName());
					int j=1;
					
					int longitud=nodes.item(i).getChildNodes().getLength();
					String nombreNodo=nodes.item(i).getChildNodes().item(j).getNodeName();
					
					while (j < longitud && !nombreNodo.equals("SIZE")) {
						j=j+1;
						nombreNodo=nodes.item(i).getChildNodes().item(j).getNodeName();
							
					}
					
				    String size=nodes.item(i).getChildNodes().item(j).getTextContent();
				    String size2 ="";
				    for (int x=0; x < size.length(); x++) 
						  if (size.charAt(x) != ' ' && size.charAt(x) != '\n')
						    size2 = size2+size.charAt(x);
				    
				    MeasureDescriptor md=new MeasureDescriptor();
			    	md.setDescription(""+elementId);
			    	md.setValueType("xs:decimal");
			    	//System.out.println(size2);
			    	md.setMinValue("0");
			    	md.setMaxValue(size2);
			    	md.setName("disks.sda");
			     	mdl.add(md);
			    	
			    	log.debug("*******Acabo de anyadir un measureDescriptor del disco");
			   
				    
			    	//NIC
				    expression = "/VM/TEMPLATE/NIC";      
					
					nodes = (NodeList) xpath.evaluate(expression, doc.getDocumentElement(), XPathConstants.NODESET);
					for (i=0; i < nodes.getLength(); i++) {
						MeasureDescriptor md2=new MeasureDescriptor();
						md2.setDescription(""+elementId);
						md2.setValueType("xs:decimal");
						md2.setMaxValue(size2);
						md2.setMinValue("0");
						int sum=i+1;
						md2.setName("networks."+sum);
						mdl.add(md2);
					}
					
					log.debug("*********He anyadido el measure descriptor de la red");
					expression = "/VM/MEMORY"; 
					nodes=(NodeList)xpath.evaluate(expression, doc.getDocumentElement(), XPathConstants.NODESET);
					for (i=0; i < nodes.getLength(); i++) {
						MeasureDescriptor md3=new MeasureDescriptor();
						md3.setDescription(""+elementId);
						md3.setValueType("xs:decimal");
						md3.setMaxValue(size2);
						md3.setMinValue("0");
						md3.setName("memory");
						mdl.add(md3);
					}
					log.debug("*********He anyadido el measure descriptor de la memoria");
					expression = "/VM/CPU"; 
					nodes=(NodeList)xpath.evaluate(expression, doc.getDocumentElement(), XPathConstants.NODESET);
					for (i=0; i < nodes.getLength(); i++) {
						MeasureDescriptor md4=new MeasureDescriptor();
						md4.setDescription(""+elementId);
						md4.setValueType("xs:decimal");
						md4.setMaxValue(size2);
						md4.setMinValue("0");
						int sum=i+1;
						md4.setName("cpus."+sum);
						mdl.add(md4);
					}
					log.debug("*********He anyadido el measure descriptor de la cpu");
				}
				
				log.debug("Monitor request succeded");
				
				
			} catch (Exception e) {
				log.error("Error configuring parser: " + e.getMessage());
				
			}
			
			
		} else {
			log.error("VM Info request failed: " + result[1]);
			return null;
		}
		
		//log.debug(mdl.)
		return mdl;
		
	}
	

	@Override
	public MeasureDescriptor getVdcMeasureDescriptor(String orgName,
			String vdcName, String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String measureId)
			throws MonitorException {
		MeasureDescriptorList mdl = getVappMeasureDescriptorList(orgName, vdcName, vappNames);
		MeasureDescriptor md = mdl.getMeasureDescriptorWithName(measureId);
		if (md == null) {
			throw new MonitorException("MeasureDescriptor Not Found - " + measureId);
		}
		return md;
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeasureDescriptor getNetMeasureDescriptor(String orgName,
			String vdcName, String netName, String measureId)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName,
			String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}



	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,
			int samples) throws MonitorException {
		
		MeasuredValueList mvl = new MeasuredValueList();
		
		int elementId =  Integer.parseInt(md.getDescription());
	    String metric = md.getName();
	    
	    HashMap <String, String >metricsTemplateONe = new HashMap();
	    metricsTemplateONe.put("cpus", "/VM/CPU");
	    metricsTemplateONe.put("memory", "/VM/MEMORY");
	    metricsTemplateONe.put("disks", "/VM/TEMPLATE/DISK");
	    metricsTemplateONe.put("networks", "/VM/NET_TX");
	    
	    String metricaONE  = null;
	   
	    if (metric.indexOf(".")!=-1)
	       metricaONE = metricsTemplateONe.get(metric.substring(0,metric.indexOf(".")));
	    else if (metric.indexOf(".")==-1)
	    	metricaONE = metricsTemplateONe.get(metric);
	   
	    Object result [] = this.getOneInfoParams(elementId);
	
	    String value = "";
	    
		boolean completed = (Boolean) result[0];
	    
	    if (completed) {
			
			String resultList = (String) result[1];
			//System.out.println (resultList);
			
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				//Document doc = builder.parse(new File("1.xml"));
				Document doc = builder.parse(new ByteArrayInputStream(resultList.getBytes()));
				String expression = metricaONE;      
				XPath xpath=XPathFactory.newInstance().newXPath();
				NodeList nodes = (NodeList) xpath.evaluate(expression, doc.getDocumentElement(), XPathConstants.NODESET);
				
				
				for (int i=0; i < nodes.getLength(); i++) {
					//int j=1;
					
					if (nodes.item(i).getChildNodes().getLength()==1)
						value = nodes.item(i).getChildNodes().item(0).getNodeValue();
					else
					{
						for (int k=0; k < nodes.item(i).getChildNodes().getLength(); k++) 
						{
							if (nodes.item(i).getChildNodes().item(k).getNodeName().equals("SIZE"))
							{
							
							  value = nodes.item(i).getChildNodes().item(k).getChildNodes().item(0).getNodeValue();

							}
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println (e.getMessage());
				value="";
			}
			
			MeasuredValue mv = new MeasuredValue();
			mv.setValue(value);
	        Date date = new Date();
	        
			mv.setRegisterDate(date);
			mv.setUnit(md.getValueType());
			mvl.add(mv);	
	    }

		return mvl;
	}
	
	public Object[]  getOneInfoParams (int elementId)
	{
		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(new Integer(elementId));
		
		Object[] result = null;
		try {				
			result = (Object[])xmlRpcClient.execute(VM_GETINFO_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			log.error("Connection error trying to get VM information: " + ex.getMessage());
			
		}
		return result;
	}
	
	public Object[]  getOneVmPoolInfoParams (int elementId)
	{
		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(elementId);
		
		Object[] result = null;
		try {				
			result = (Object[])xmlRpcClient.execute("one.vmpool.info", rpcParams);
		} catch (XmlRpcException ex) {
			System.out.println("Connection error trying to get VM information: " + ex.getMessage());
			
		}
		return result;
	}

	@Override
	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String hwItemName,
			String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames, String hwItemName)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeasuredValueList> getMeasuredValueList(
			List<MeasureDescriptor> md, MeasuredValueFilter filter)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String measureId)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
	/*public List<MeasuredValueList> getMeasuredValueList(
			List<MeasureDescriptor> md, MeasuredValueFilter filter)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}*/
	


}
