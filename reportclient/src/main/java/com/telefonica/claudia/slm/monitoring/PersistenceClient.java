package com.telefonica.claudia.slm.monitoring;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.Protocol;


//import com.telefonica.claudia.slm.deployment.ServiceApplication;
//import com.telefonica.claudia.slm.deployment.NIC;
//import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringSampleData;
import com.telefonica.claudia.smi.URICreation;
import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PersistenceClient {


	private static final Logger logger = Logger.getLogger(PersistenceClient.class);
	private static String TCloudServerURL;
	/*	private final String DB_URL;
	private final String DB_USER;
	private final String DB_PASSWORD;*/
	private final String SITE_ROOT;

	public static final String ROOT_MONITORING_TAG_NAME = "MonitoringInformation";
	public static final String EVENT_TYPE_TAG_NAME = "EventType";
	public static final String T_0_TAG_NAME = "EpochTimestamp";
	public static final String T_DELTA_TAG_NAME = "TimeDelta";
	public static final String FQN_TAG_NAME = "FQN";
	public static final String VALUE_TAG_NAME = "Value";

	private static String restPath;
	private static String restServerPort;
	private static String restServerHost;
	private static String measurementTopicIdentifier;
	private static String namingFactory;
	private static String serverProviderUrl;
	private static String connFactoryName;

	private static HttpClient httpClient = new HttpClient();

	public static final String PATH_TO_PROPERTIES_FILE = "./conf/reportClient.properties";

	private static final Properties properties = new Properties();	

	public static Client client;

	public PersistenceClient() {		
		try {
			properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
			TCloudServerURL = properties.getProperty("TServer.url");
			/*	DB_URL = properties.getProperty("bd.url");
			DB_USER = properties.getProperty("bd.user");
			DB_PASSWORD = properties.getProperty("bd.password");
			DbManager dbManager = DbManager.createDbManager(DB_URL, false,DB_USER, DB_PASSWORD);*/
			SITE_ROOT = properties.getProperty("SiteRoot");
			restPath=properties.getProperty("restPath");
			restServerPort=properties.getProperty("restServerPort");
			restServerHost=properties.getProperty("restServerHost");
		} catch (IOException e) {
			logger.error("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
			throw new RuntimeException("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
		}
	}	

	public static void sendRESTMessage(String eventType, long t_0, long delta_t, String fqn, double value) {

		String message =	"<" + ROOT_MONITORING_TAG_NAME + ">" +
		"<" + EVENT_TYPE_TAG_NAME + ">" + eventType +
		"</" + EVENT_TYPE_TAG_NAME + ">" +
		"<" + T_0_TAG_NAME + ">" + t_0 +
		"</" + T_0_TAG_NAME + ">" +
		"<" + T_DELTA_TAG_NAME + ">" + delta_t +
		"</" + T_DELTA_TAG_NAME + ">" +
		"<" + FQN_TAG_NAME + ">" + fqn +
		"</" + FQN_TAG_NAME + ">" +
		"<" + VALUE_TAG_NAME + ">" + value +
		"</" + VALUE_TAG_NAME + ">" +
		"</" + ROOT_MONITORING_TAG_NAME + ">" ;

		PostMethod post = new PostMethod("http://" + restServerHost + ":" + restServerPort + restPath);

		RequestEntity request = null;		
		try {
			request = new StringRequestEntity(message, "text/xml", null);
		} catch (UnsupportedEncodingException ex) {
			System.out.println("This should never happen? Cannot create a String request entity with null char encoding");
			return;
		}

		post.setRequestEntity(request);

		try {			
			httpClient.executeMethod(post);
			System.out.println("\n\tResult status: " + post.getStatusText() + "\n");
		} catch (HttpException ex) {
			System.out.println("HTTPException caught when trying to send POST message: " + ex.getMessage());
			return;
		} catch (IOException ex) {
			System.out.println("IOException caught when trying to send POST message: " + ex.getMessage());
			return;
		} finally {
			post.releaseConnection();
		}
	}


	public static String get(Client client, Reference reference)
	throws IOException {
		client = new Client(Protocol.HTTP);
		Response response = client.get(reference);
		System.out.println (" reference " + reference.getIdentifier());
		if (response.getStatus().isSuccess()) {
			if (response.isEntityAvailable()) {
				return response.getEntity().getText();
			} else {
				return "No response from the server";
			}
		} else {
			System.out.println("GET request didn't succeed");
			return "ERROR";
		}
	}

	public static ArrayList<String> findvdc(String getresponse){


		ArrayList<String>vdcs = new ArrayList<String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc= builder.parse(new ByteArrayInputStream(getresponse.getBytes()));

			NodeList vdcList = doc.getElementsByTagName("Link");

			for (int i=0; i < vdcList.getLength(); i++) {

				Node node = vdcList.item(i);
				NamedNodeMap atributes = node.getAttributes(  );
				Node typeAtribute = atributes.getNamedItem( "rel" );
				if (typeAtribute.getNodeValue().equals("down")){
					Node hrefAtribute = atributes.getNamedItem( "href" );
					String fqn=hrefAtribute.getNodeValue();
					vdcs.add(fqn);
					logger.info("VDC found " + fqn); 
				}
			}
		}
		catch (Exception spe)
		{
			// Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
		}
		return vdcs;
	}

	public static ArrayList<String> findvapps(String getresponse){


		ArrayList<String>vapps = new ArrayList<String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc= builder.parse(new ByteArrayInputStream(getresponse.getBytes()));

			NodeList vappList = doc.getElementsByTagName("Link");

			for (int i=0; i < vappList.getLength(); i++) {

				Node node = vappList.item(i);
				NamedNodeMap atributes = node.getAttributes(  );
				Node typeAtribute = atributes.getNamedItem( "type" );
				if (typeAtribute.getNodeValue().equals("application/vnd.telefonica.tcloud.vapp+xml")){
					Node hrefAtribute = atributes.getNamedItem( "href" );
					String fqn=hrefAtribute.getNodeValue();
					vapps.add(fqn);
					logger.info("Vapp found " + fqn); 
				}
			}
		}
		catch (Exception spe)
		{
			// Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
		}
		return vapps;
	}

	public static ArrayList<String> findvms(String getresponse){


		ArrayList<String>vms = new ArrayList<String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc= builder.parse(new ByteArrayInputStream(getresponse.getBytes()));

			NodeList vmList = doc.getElementsByTagName("VApp");

			for (int i=0; i < vmList.getLength(); i++) {

				Node node = vmList.item(i);
				NamedNodeMap atributes = node.getAttributes(  );
				Node hrefAtribute = atributes.getNamedItem( "href" );
				String fqn=hrefAtribute.getNodeValue();
				if (fqn.substring(fqn.length()-2,fqn.length()-1).equals("/")){
					vms.add(fqn);
					//logger.info(" VM found " + fqn); 
				}
			}
		}
		catch (Exception spe)
		{
			// Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
		}
		return vms;
	}
	
	public static ArrayList<String> findmeasures(String monitor) throws IOException{

		logger.info("Measures for monitor" + monitor); 
		ArrayList<String>measures = new ArrayList<String>();
		
			int i = monitor.indexOf("/api");
			Reference  monitorurl  = new Reference(monitor);
			String monxml=get(client,monitorurl);
			
		//	logger.info(" monitor XML  " + monxml); 
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc= builder.parse(new ByteArrayInputStream(monxml.getBytes()));

			NodeList measureList = doc.getElementsByTagName("MeasureDescriptor");

			for (int j=0; j < measureList.getLength(); j++) {

				Node node = measureList.item(j);
				NamedNodeMap atributes = node.getAttributes(  );
				Node nameAtribute = atributes.getNamedItem( "name" );
				String name=nameAtribute.getNodeValue();
				measures.add(name);
				logger.info(" measure found " + name); 
			}
		}
		catch (Exception spe)
		{
			// Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
		}


	return measures;
	}


	public  ArrayList<String> findmonitors(List<String> list) throws IOException{


		ArrayList<String>monitors = new ArrayList<String>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String vm = (String) iterator.next();	
			int i = vm.indexOf("/api");
			String monitorfqn =TCloudServerURL+(vm.substring(i,vm.length())+"/monitor");
			
			logger.info("Monitor found: " + monitorfqn); 
			monitors.add(monitorfqn);

		}

		return monitors;
	}
	
	public  String getmeasure(String monitor, String measure) throws IOException {

		String value=null;
		Reference  ValueURL  = new Reference(monitor+"/"+measure+"/"+"values");
		String valuexml=get(client,ValueURL);


return valuexml;
	}
	
	public void  sendvalue(String valuexml)  {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc= builder.parse(new ByteArrayInputStream(valuexml.getBytes()));

			NodeList valueList = doc.getElementsByTagName("Sample");

			for (int i=0; i < valueList.getLength(); i++) {

				Node node = valueList.item(i);
				NamedNodeMap atributes = node.getAttributes(  );
				Node unitAtribute = atributes.getNamedItem( "unit" );
				String unit=unitAtribute.getNodeValue();
				Node timestampAtribute = atributes.getNamedItem( "timestamp" );
				String timestamp=timestampAtribute.getNodeValue();
				Node valueAtribute = atributes.getNamedItem( "value" );
				String value=valueAtribute.getNodeValue();
				
				logger.info(" values: " + unit+" "+timestamp+" "+ value); 
			}
		}
		catch (Exception spe)
		{
			// Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
		}
	}

	

	public List<String> getVMs() throws IOException{

		ArrayList<String> result= new ArrayList<String>();
		ArrayList<String>vdcs = new ArrayList<String>();



		client = new Client(Protocol.HTTP);
		Reference  TcloudURL  = new Reference(TCloudServerURL+"/api/org/"+SITE_ROOT);
		String url=get(client,TcloudURL);

		logger.info("Tcloud URL: " + TcloudURL); 
		vdcs=findvdc(url);

		for (Iterator iterator = vdcs.iterator(); iterator.hasNext();) {
			String vdc = (String) iterator.next();		
			ArrayList<String> vapps = new ArrayList<String>();
			int i = vdc.indexOf("/api");
			String vdcfqn = TCloudServerURL+vdc.substring(i,vdc.length());
			Reference  vdcURL  = new Reference(vdcfqn);
			//	    logger.info(" VDC: " + vdcURL);
			String vappxml=get(client,vdcURL);
			//		logger.info(" GET VM: " + vmurl); 
			vapps=findvapps(vappxml);

			for (Iterator iterator2 = vapps.iterator(); iterator2.hasNext();) {
				String vapp = (String) iterator2.next();	
				ArrayList<String> vms = new ArrayList<String>();
				int j = vapp.indexOf("/api");
				String vappfqn =TCloudServerURL+vapp.substring(j,vapp.length());
				Reference  vmurl  = new Reference(vappfqn);
				String vmxml=get(client,vmurl);
				vms=findvms(vmxml);

				for (Iterator iterator3 = vms.iterator(); iterator3.hasNext();) {
					String vm = (String) iterator3.next();	
					int k = vm.indexOf("/api");
					String vmfqn =TCloudServerURL+vm.substring(k,vm.length());
					logger.info("VM found " + vmfqn); 
					result.add(vmfqn);

				}

			}

		}


		return result;
	}

	public void export(BaseMonitoringData data){
		/*	DbManager dbManager = DbManager.getDbManager();

		NodeDirectory node = dbManager.get(NodeDirectory.class, data.getFQN());
		Set<String> keys = data.keys();

		for (String measure : keys) {
			MonitoringSampleData[] samples2 = data.get(measure);
			SortedSet<MonitoringSampleData> samplesOrdered = new TreeSet<MonitoringSampleData>();			
			samplesOrdered.addAll(Arrays.asList(samples2));
			Iterator<MonitoringSampleData> iterator = samplesOrdered.iterator();
			Set<MonitoringSample> samplesToPersist = new HashSet<MonitoringSample>();
			while (iterator.hasNext()) {
				MonitoringSampleData sample = (MonitoringSampleData) iterator.next();

				Timestamp timestamp = (Timestamp) dbManager.executeQuery("select max(datetime) from "+MonitoringSample.class.getName()+
						" where associatedObject_internalId =" + node.getObjectId() +
						" AND  measure_type ='"+measure+"'");
				if (timestamp== null || timestamp.before(sample.getTimestamp())){
					MonitoringSample ms = new MonitoringSample(node, 
							sample.getTimestamp(), 
							measure,
							sample.getValue(),
							sample.getUnit());

					samplesToPersist.add(ms);					
				}
			}
			dbManager.save(samplesToPersist);
		}*/
	}

	public List<String> getNics(){
		ArrayList<String> result = new ArrayList<String>();
		/*	DbManager dbManager = DbManager.getDbManager();
		List<NodeDirectory> list = dbManager.getList(NodeDirectory.class);
		for (int i = 0; i < list.size(); i++) {
			NodeDirectory node = list.get(i);
			try{
				if (node.getTipo()== NodeDirectory.TYPE_NIC){
					NIC nic = dbManager.get(NIC.class, node.getFqnString());
					String instanceId = nic.getInstanceId();
					if (instanceId != null){
						StringBuilder url = new StringBuilder();
						url.append(TCloudServerURL);
						url.append(URICreation.getURIVEEReplica(node.getFqnString()));
						url.append("/").append(URICreation.FQN_SEPARATOR_HW);
						url.append("/").append(instanceId);
						result.add(url.toString());
					}
				}
			}catch (Exception e) {
			// TODO: handle exception
			}
		}*/
		return result;
	}	

	public String getFqnNIC(String url) {
		String HARDWARE_SEPARATOR = "/hw";
		String fqnNIC = null;
		String fqnVeeReplica = null; URICreation.getFQNFromURL(url);
		int i = url.indexOf(HARDWARE_SEPARATOR);
		/*	DbManager dbManager = null;
		if (i > 0){
			String nicInstanceId = url.substring(i+HARDWARE_SEPARATOR.length()+1);
			nicInstanceId = nicInstanceId.substring(0,nicInstanceId.indexOf("/"));

			 dbManager = DbManager.getDbManager();

			VEEReplica veeReplica = null; //dbManager.getVEEReplica(fqnVeeReplica);

			List<NIC> nics = veeReplica.getNICs();
			for (Iterator iterator = nics.iterator(); iterator.hasNext();) {
				NIC nic = (NIC) iterator.next();

				if (nic.getInstanceId().equals(nicInstanceId)){
					fqnNIC = nic.getFqnString();
				}
			}
		}*/
		return fqnNIC;
	}	

	public static void main(String[] args) {
		//	PersistenceClient pc = new PersistenceClient();
		//String fqn = pc.getFqnNIC("http://10.95.129.34:8183/api/org/tid34/vdc/joseldCPD/vapp/joseldServ/AnaVM1/1/hw/networks-4001/monitor/values?measures=netPacketstxSummation,netInput,netOutput,netPacketsrxSummation&from=2011-02-03T11:48:00Z&to=2011-02-03T12:18:00Z&interval=5m");
		//	List<String> nics = pc.getNics();
		//	System.out.println("Nic"+nics.size());

	}
}
