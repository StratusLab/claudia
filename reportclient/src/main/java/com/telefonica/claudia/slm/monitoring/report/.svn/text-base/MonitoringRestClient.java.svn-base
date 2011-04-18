package com.telefonica.claudia.slm.monitoring.report;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.slm.monitoring.FrequencyEnum;
import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.smi.URICreation;

public class MonitoringRestClient {
	private final Client client;
	private final String serverURL;
	private final String orgId;
	
	private static final Logger logger = Logger.getLogger(MonitoringRestClient.class);
	
	public static final String PATH_TO_PROPERTIES_FILE = "./conf/reportClient.properties";
	
	public static final String API = "/api";
	public static final String ORG = "/org";
	public static final String VDC = "/vdc";
	public static final String VAPP = "/vapp";
	public static final String VEE = "/vee";
	
	private static final Properties properties = new Properties();
	
	public MonitoringRestClient() {
		client = new Client(Protocol.HTTP);
		
		try {
			properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
			serverURL = properties.getProperty("TServer.url");
			orgId = properties.getProperty("org");
		} catch (IOException e) {
			throw new RuntimeException("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
		}
	}

	public BaseMonitoringData getMonitoringData(String url) {
		String fqn = URICreation.getFQNFromURL(url);
		BaseMonitoringData baseMonitoringData = new BaseMonitoringData(fqn);
		String data = getRESTData(url);
		logger.debug("\nUrl:"+url+
					"\nData:"+data);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(data.getBytes()));
			Element root= (Element) doc.getFirstChild();
			
			NodeList measure = root.getElementsByTagName("Measure");
			for (int i = 0; i < measure.getLength(); i++) {
				NodeList measureChilds = measure.item(i).getChildNodes();
				ArrayList<MonitoringSampleData> samples = new ArrayList<MonitoringSampleData>();
				String measureType = "";
				for (int j = 0; j < measureChilds.getLength(); j++) {
					if (measureChilds.item(j)!=null && measureChilds.item(j).getNodeName().equals("Sample")){
						
						try{
							String value = measureChilds.item(j).getAttributes().getNamedItem("value").getNodeValue();
							String timestamp = measureChilds.item(j).getAttributes().getNamedItem("timestamp").getNodeValue();
							String unit = "";
							if (measureChilds.item(j).getAttributes().getNamedItem("unit")!=null){
								unit = measureChilds.item(j).getAttributes().getNamedItem("unit").getNodeValue();
							}
							
							MonitoringSampleData sample = new MonitoringSampleData();
							sample.setValue(value);
							sample.setTimestamp(MonitoringSampleData.timestampToDate(timestamp));
							sample.setUnit(unit);
							samples.add(sample);							
						}	
						catch (Exception e) {
						}

					}
					else if (measureChilds.item(j)!=null && measureChilds.item(j).getNodeName().equals("Descriptor")){
						measureType = measureChilds.item(j).getAttributes().getNamedItem("href").getNodeValue();
						int ind = measureType.lastIndexOf("/");
						measureType = measureType.substring(ind+1);
					}
				}
				baseMonitoringData.put(measureType, samples.toArray(new MonitoringSampleData[0]));
			}

		} catch (IOException e) {
			logger.fatal("Error: " + e.getMessage());
		} catch (Throwable e) {
			logger.fatal("Error: " + e.getMessage());
		}		
		return baseMonitoringData;
	}
	
	private String getRESTData(String url) {
		Reference urlMonitoring = new Reference(url);
	
		logger.debug("Asking for measures at url: " + urlMonitoring);
		try{
			Response response = client.get(urlMonitoring);
			
			switch (response.getStatus().getCode()) {		
				case 401:	// Unauthorized
				case 403:	// Forbidden
					logger.fatal("Error: Access Forbidden."+response.getStatus().getDescription());
					break;
				case 400:	// Bad Request
				case 404:	// Not found
					logger.fatal("Error: Not found."+response.getStatus().getDescription());
					break;
				case 501:
				case 500:
					logger.fatal("Error:"+response.getStatus().getDescription());
					break;
				case 202:
				case 201:	
				case 200:	
					try {
						Document responseXml = response.getEntityAsDom().getDocument();
						return Utils.getStringFromDocument(responseXml);			
					} catch (IOException e) {
						logger.fatal("Error: " + response.getStatus().getDescription());
					} catch (Throwable e) {
						logger.fatal("Error: " + response.getStatus().getDescription());
					}
			}
		}catch (Exception e) {
			logger.fatal("Error: " + e.getMessage());
		}
		return null;
	}
	
	public BaseMonitoringData getMonitoringData(TypeEntityEnum type, FrequencyEnum frequency, Calendar day, String baseUrl) {
		String frequencyParameters = Utils.getMonitoringParameters(frequency, day);

		String measures = properties.getProperty(type.name() + ".measures"); // ie. "cpu.measures"
		String url = baseUrl + URICreation.MONITOR + URICreation.VALUES + "?measures="+measures +frequencyParameters;
		BaseMonitoringData samples = getMonitoringData(url);
		
		return samples;
	}
	
	public String getMonitoringDataUrl(TypeEntityEnum type, FrequencyEnum frequency, Calendar day, String baseUrl) {
		String frequencyParameters = Utils.getMonitoringParameters(frequency, day);

		String measures = properties.getProperty(type.name() + ".measures"); // ie. "cpu.measures"
		String url = baseUrl + URICreation.MONITOR + URICreation.VALUES + "?measures="+measures +frequencyParameters;
	
		return url;
	}
	public String getMonitoringDataUrl(OtherTypeEntityEnum type,FrequencyEnum frequency, Calendar day, String baseUrl) {
		String frequencyParameters = Utils.getMonitoringParameters(frequency, day);

		String measures = properties.getProperty(type.name() + ".measures"); // ie. "cpu.measures"
		String url = baseUrl + URICreation.MONITOR + URICreation.VALUES + "?measures="+measures +frequencyParameters;
	
		return url;
	}	
	
	public String[] getVDCs(){
		String url = serverURL+API+ORG+"/"+orgId;
		return getLinks(url);
	}
	public String[] getServices(String url){
		return getLinks(url);
	}
	
	private String[] getLinks(String url){
		ArrayList<String> result = new ArrayList<String>();
	
		try {
			String restData = getRESTData(url);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(restData.getBytes()));
			Element root= (Element) doc.getFirstChild();
			
			NodeList links = root.getElementsByTagName("Link");
			for (int i = 0; i < links.getLength(); i++) {
				String linkType = links.item(i).getAttributes().getNamedItem("rel").getNodeValue();
				if (linkType.equals("down")){
					String href = links.item(i).getAttributes().getNamedItem("href").getNodeValue();
					result.add(href);
				}
			}			
		}catch (Exception e) {
			logger.fatal("Error: " + e.getMessage());
		}
		return result.toArray(new String[0]);
	}	
	
	public String[] getVMs(String url){
		ArrayList<String> result = new ArrayList<String>();

		String restData = getRESTData(url);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(restData.getBytes()));
			Element root= (Element) doc.getFirstChild();
			
			NodeList childrenN1 = root.getElementsByTagName("Children");
			for (int i = 0; i < childrenN1.getLength(); i++) {
				NodeList vapp1 = childrenN1.item(i).getChildNodes();
				if (vapp1 != null){
					for (int j = 0; j < vapp1.getLength(); j++) {
						NodeList childrenN2 = vapp1.item(j).getChildNodes();
						if (childrenN2 != null){
							for (int k = 0; k < childrenN2.getLength(); k++) {
								NodeList nodes = childrenN2.item(k).getChildNodes();
								if (nodes != null){
									for (int h = 0; h < nodes.getLength(); h++) {
										if (nodes.item(h).getNodeName().equals("VApp")){
											String href = nodes.item(h).getAttributes().getNamedItem("href").getNodeValue();
											result.add(href);
										}
									}
								}
							}
						}
					}
				}
			}			
		}catch (Exception e) {
			logger.fatal("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return result.toArray(new String[0]);
	}		
}