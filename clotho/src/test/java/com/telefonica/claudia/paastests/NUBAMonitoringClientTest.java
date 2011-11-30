package com.telefonica.claudia.paastests;

import org.junit.Test;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.paas.vmiHandler.NUBAMonitoringClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;


public class NUBAMonitoringClientTest {
	
	@Test
	
	public void setupmonitoring() {
	
	try {
		SMConfiguration.loadProperties();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	NUBAMonitoringClient client = new NUBAMonitoringClient(SMConfiguration.getInstance().getMonitoringUrl());
	
	String name = "holas";
	String ip = " ips";
	System.out.println ("Posting name " + name + " " + ip);
	try {
		client.setUpMonitoring(name, ip);
	} catch (AccessDeniedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (CommunicationErrorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		client.deleteSetupMonitoring(name);
	} catch (AccessDeniedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (CommunicationErrorException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	

}
