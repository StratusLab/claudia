package com.telefonica.claudia.slm.monitoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;
import com.telefonica.claudia.slm.monitoring.report.TypeEntityEnum;

public class Monitor extends Thread {

	private static final MonitoringRestClient client = new MonitoringRestClient();
	private static final Logger logger = Logger.getLogger(Monitor.class);
	
	private String URL;
	
	public Monitor(String url){
		this.URL = url;
	}
	
	public void run() {
		System.out.println("Starting for "+URL);
		generateReport(URL);
	}
	
	private void generateReport(String baseUrl) {
		Calendar day = new GregorianCalendar();
		TypeEntityEnum[] typeEntities = TypeEntityEnum.values();
		List<ThreadMonitor> monitorThreads = new ArrayList<ThreadMonitor>();
		for (TypeEntityEnum typeEntity : typeEntities) {
			String url = client.getMonitoringDataUrl(typeEntity, FrequencyEnum.REALTIME, day, baseUrl);
			ThreadMonitor tm = new ThreadMonitor(url);
			monitorThreads.add(tm);
			tm.start();
		}
		for (Iterator iterator = monitorThreads.iterator(); iterator.hasNext();) {
			ThreadMonitor threadMonitor = (ThreadMonitor) iterator.next();
			try {
				threadMonitor.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void generateAllReport() throws IOException {
		long i1 = System.currentTimeMillis();
		PersistenceClient pc = new PersistenceClient();
		List<String> list = pc.getVMs();
		ArrayList<String> monitorlist = pc.findmonitors(list);
		

		for (Iterator iterator = monitorlist.iterator(); iterator.hasNext();) {
			String monitor = (String) iterator.next();	
			ArrayList<String> measurelist = pc.findmeasures(monitor);
			for (Iterator iterator2 = measurelist.iterator(); iterator2.hasNext();) {
				String measure = (String) iterator2.next();	

				String valuexml=pc.getmeasure(monitor, measure);
				logger.info(" monitor values: " + monitor+ " "+measure); 
				pc.sendvalue(valuexml);

			}

		}
		/*	List<Monitor> monitorThreads = new ArrayList<Monitor>();
		for (int i = 0; i < list.size(); i++) {
			String vmUrl = list.get(i);			
			logger.debug("VM Url:"+vmUrl);
			Monitor monitor = new Monitor(vmUrl);
			monitorThreads.add(monitor);
			monitor.start();
		}
		for (Iterator iterator = monitorThreads.iterator(); iterator.hasNext();) {
			Monitor monitor = (Monitor) iterator.next();
			try {
				monitor.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/

		long t1 = System.currentTimeMillis() - i1;
		logger.debug("generateAllReport Total Time:" + t1 + " miliseg");					
	}

	private static void showUsage() {
		System.out.println("Usage:\n\nmonitor <monitorType> \n\n");
		System.out.println("\t allmonitoring");
		System.out.println("\t\tStart monitoring allparameters (Except DiskUsage).\n");
		
		System.out.println("\t diskusage");
		System.out.println("\t\tStart monitoring DiskUsage.\n");
	}
	
	public static void main(String[] args) throws IOException {
	 	if (args.length < 1) {
    		System.out.println("Command not recognized. \n");
    		showUsage();
    	}
	 	else if (args.length==1){
			if (args[0].toLowerCase().equals("allmonitoring") ){
		/*		NICMonitor nic = new NICMonitor();
				nic.start();*/
				generateAllReport();
			}
			else if (args[0].toLowerCase().equals("diskusage") ){
				DiskUsageMonitor.generateAllDiskUsageReport();
			}
			else{
				System.out.println("It is needed the type of monitoring: (allmonitoring | diskusage)");
			}			
		}
		else{
			System.out.println("It is needed the type of monitoring: (allmonitoring | diskusage)");
		}
	}	
}