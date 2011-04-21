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

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class Monitor extends Thread {

	private static final MonitoringRestClient client = new MonitoringRestClient();
	private static final Logger logger = Logger.getLogger(Monitor.class);
	public static List<String> totalvmlist;
	private String URL;

	public Monitor(String url){
		this.URL = url;
	}

	static Runnable periodicGetVm = new Runnable(){
		public void run() {
			PersistenceClient pc = new PersistenceClient();
			try {
				totalvmlist = pc.getVMs();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	};
	
	static Runnable periodicGetMonValues= new Runnable(){
		public void run() {
			PersistenceClient pc = new PersistenceClient();
			try {
				periodicMonitoring();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	};

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

	public static void periodicMonitoring() throws IOException {

		PersistenceClient pc = new PersistenceClient();
		ArrayList<String> monitorlist = pc.findmonitors(totalvmlist);

		for (Iterator iterator = monitorlist.iterator(); iterator.hasNext();) {
			String monitor = (String) iterator.next();	
			ArrayList<String> measurelist = pc.findmeasures(monitor);
			for (Iterator iterator2 = measurelist.iterator(); iterator2.hasNext();) {
				String measure = (String) iterator2.next();	

				String valuexml=pc.getmeasure(monitor, measure);
				logger.info(" monitor values: " + monitor+ " "+measure); 
				pc.sendvalue(valuexml,monitor);

			}

		}

	}
	public static void generateAllReport() throws IOException {
		//long i1 = System.currentTimeMillis();
		PersistenceClient pc = new PersistenceClient();
		totalvmlist = pc.getVMs();
		periodicMonitoring();
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(periodicGetVm,0,180, TimeUnit.SECONDS);
		
		ScheduledExecutorService executor2 = Executors.newSingleThreadScheduledExecutor();
		executor2.scheduleAtFixedRate(periodicGetMonValues,0,20, TimeUnit.SECONDS);
		
		//long t1 = System.currentTimeMillis() - i1;
		//logger.debug("generateAllReport Total Time:" + t1 + " miliseg");					
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