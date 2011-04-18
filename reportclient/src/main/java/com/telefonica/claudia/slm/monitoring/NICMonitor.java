package com.telefonica.claudia.slm.monitoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;

public class NICMonitor extends Thread {

	private static final MonitoringRestClient client = new MonitoringRestClient();
	private static final Logger logger = Logger.getLogger(NICMonitor.class);
	
	
	public NICMonitor(){
	}
	
	public void run() {
		System.out.println("Starting All NIC Monitoring");
		generateAllNICReport();
	}
	

	public static void generateAllNICReport() {
		long i1 = System.currentTimeMillis();
		PersistenceClient pc = new PersistenceClient();

		// Monitoriza las redes		
		List<String> listNICs = pc.getNics();
		List<NICMonitorThread> monitorNICsThreads = new ArrayList<NICMonitorThread>();
		for (int i = 0; i < listNICs.size(); i++) {
			String nicUrl = listNICs.get(i);			
			logger.debug("NIC Url:"+nicUrl);
			
			NICMonitorThread nicmonitorThread = new NICMonitorThread(nicUrl);
			monitorNICsThreads.add(nicmonitorThread);
			nicmonitorThread.start();
		}
		for (Iterator iterator = monitorNICsThreads.iterator(); iterator.hasNext();) {
			NICMonitorThread nicMonitorThread = (NICMonitorThread) iterator.next();
			try {
				nicMonitorThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long t1 = System.currentTimeMillis() - i1;
		logger.debug("generateAllNICReport Total Time:" + t1 + " miliseg");					
	}

}