package com.telefonica.claudia.slm.monitoring;

import java.io.FileInputStream;
import java.io.IOException;
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

import org.apache.log4j.Logger;

//import com.telefonica.claudia.slm.common.DbManager;
//import com.telefonica.claudia.slm.deployment.NIC;
//import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringSampleData;
import com.telefonica.claudia.smi.URICreation;

public class PersistenceClient {
	
	private static final Logger logger = Logger.getLogger(PersistenceClient.class);
	private final String TCloudServerURL;
/*	private final String DB_URL;
	private final String DB_USER;
	private final String DB_PASSWORD;*/
	
	public static final String PATH_TO_PROPERTIES_FILE = "./conf/reportClient.properties";

	private static final Properties properties = new Properties();	
	
	public PersistenceClient() {		
		try {
			properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
			TCloudServerURL = properties.getProperty("TServer.url");
		/*	DB_URL = properties.getProperty("bd.url");
			DB_USER = properties.getProperty("bd.user");
			DB_PASSWORD = properties.getProperty("bd.password");
			DbManager dbManager = DbManager.createDbManager(DB_URL, false,DB_USER, DB_PASSWORD);*/
		} catch (IOException e) {
			logger.error("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
			throw new RuntimeException("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
		}
	}	
	
	public List<String> getVMs(){
		ArrayList<String> result = new ArrayList<String>();
	/*	DbManager dbManager = DbManager.getDbManager();
		List<NodeDirectory> list = dbManager.getList(NodeDirectory.class);
		for (int i = 0; i < list.size(); i++) {
			NodeDirectory node = list.get(i);
			if (node.getTipo()== NodeDirectory.TYPE_REPLICA){
				String url = TCloudServerURL+URICreation.getURIVEEReplica(node.getFqnString());
				result.add(url);
			}
		}*/
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
