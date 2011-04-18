package com.telefonica.claudia.slm.monitoring;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;
import com.telefonica.claudia.slm.monitoring.report.TypeEntityEnum;

public class NICMonitorThread extends Thread {

	private static final MonitoringRestClient client = new MonitoringRestClient();
	private static final Logger logger = Logger.getLogger(NICMonitorThread.class);
	
	private String baseURL;
	
	public NICMonitorThread(String baseURL){
		this.baseURL = baseURL;
	}
	
	public void run() {
		System.out.println("Starting for baseURL:"+baseURL);
		generateNICReport(baseURL);
	}
	
	private void generateNICReport(String baseUrl) {
		Calendar day = new GregorianCalendar();

		String url = client.getMonitoringDataUrl(TypeEntityEnum.nic, FrequencyEnum.REALTIME, day, baseUrl);
		PersistenceClient pc = new PersistenceClient();
		String fqnNIC = pc.getFqnNIC(url);
		System.out.println("url:"+url+"\nfqnNIC:"+fqnNIC);
		
		BaseMonitoringData data = client.getMonitoringData(url);
		data.setFQN(fqnNIC);	

		pc.export(data);
	}
}