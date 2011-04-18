package com.telefonica.claudia.slm.monitoring;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;
import com.telefonica.claudia.slm.monitoring.report.MonitoringSampleData;
import com.telefonica.claudia.slm.monitoring.report.OtherTypeEntityEnum;
import com.telefonica.claudia.slm.monitoring.report.Utils;
import com.telefonica.claudia.smi.URICreation;

public class DiskUsageMonitor extends Thread{

	private static final MonitoringRestClient client = new MonitoringRestClient();
	private static String DISK_USAGE = "diskUsage";
	private String URL;	
	
	public DiskUsageMonitor(String url){
		this.URL = url;
	}	

	public void run() {
		generateDiskUsageReport(URL);
	}

	private void generateDiskUsageReport(String baseUrl) {
		Calendar day = new GregorianCalendar();
	
		String url = client.getMonitoringDataUrl(OtherTypeEntityEnum.diskUsage, FrequencyEnum.REALTIME, day, baseUrl);
		PersistenceClient pc = new PersistenceClient();
		
		System.out.println("url "+url);
		BaseMonitoringData data = client.getMonitoringData(url);
		//Normalizamos los datos
		if (data.get(DISK_USAGE)!=null){
			MonitoringSampleData[] samples = data.get(DISK_USAGE);
			Date date = samples[0].getTimestamp();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar = Utils.roundToInterval(calendar, 5);
			samples[0].setTimestamp(calendar.getTime());
			data.put(DISK_USAGE, samples);			
			pc.export(data);	
		}		
	}
	
	public static void generateAllDiskUsageReport() {
		long i1 = System.currentTimeMillis();			
		PersistenceClient pc = new PersistenceClient();
		List<String> list = pc.getVMs();
		List<DiskUsageMonitor> monitorThreads = new ArrayList<DiskUsageMonitor>();		
		for (int i = 0; i < list.size(); i++) {
			String vmUrl = list.get(i);
			DiskUsageMonitor monitor = new DiskUsageMonitor(vmUrl);
			monitorThreads.add(monitor);
			monitor.start();			
			//generateDiskUsageReport(vmUrl);
		}		
		for (Iterator iterator = monitorThreads.iterator(); iterator.hasNext();) {
			DiskUsageMonitor diskUsageMonitor = (DiskUsageMonitor) iterator.next();
			try {
				diskUsageMonitor.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long t1 = System.currentTimeMillis() - i1;
		System.out.println("generateAllDiskUsageReport Total Time:" + t1 + " miliseg");			
	}
}