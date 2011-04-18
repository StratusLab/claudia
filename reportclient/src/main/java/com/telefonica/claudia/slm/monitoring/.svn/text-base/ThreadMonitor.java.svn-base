package com.telefonica.claudia.slm.monitoring;

import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;

public class ThreadMonitor extends Thread{
	private static final MonitoringRestClient client = new MonitoringRestClient();
	private String URL;
	
	public ThreadMonitor(String url){
		this.URL = url;
	}	
	
	public void run() {
		PersistenceClient pc = new PersistenceClient();
		BaseMonitoringData data = client.getMonitoringData(URL);

		pc.export(data);
	}
}