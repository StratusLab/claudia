package com.telefonica.claudia.slm.paas;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;

import com.telefonica.claudia.slm.paas.vmiHandler.MonitoringClient;
import com.telefonica.claudia.slm.paas.vmiHandler.MonitoringVMIHandler;

public class Monitoring {
	
	private static Logger logger = Logger.getLogger("Monitoring");
	


	public boolean setupMonitoring (ServiceApplication sap)
	{
		logger.info("Setuping monitoring ");
		if (SMConfiguration.getInstance().isMonitoringEnabled()) {
			try {
				java.net.URLClassLoader  classLoader = (java.net.URLClassLoader)ClassLoader.getSystemClassLoader();

				Class mon  =  classLoader.loadClass(SMConfiguration.getInstance().getMonitoringClass());
				MonitoringVMIHandler client  = (MonitoringVMIHandler) mon.getConstructor(String.class).newInstance(SMConfiguration.getInstance().getMonitoringUrl());
				//MonitoringVMIHandler client = (MonitoringVMIHandler)classDriver;
				//MonitoringClient client = new MonitoringClient (SMConfiguration.getInstance().getMonitoringUrl());
				client.setUpMonitoring(sap.getFQN().toString(),getListIpsService(sap));
				return true;
			
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error("Unknow error connecting to monitoring systems: "
						+ e.getMessage(), e);
				return false;
			}
		}
		return false;

	/*	if (SMConfiguration.getInstance().isWasupActive()) {
			try {
				wasupClient.createWasupHierarchy(getServiceApplication());
			} catch (IOException e) {
				logger
				.error("Error creating the WASUP monitorization hierarchy: "
						+ e.getMessage());
			} catch (JSONException e) {
				logger.error("Error parsing the response of a WASUP request: "
						+ e.getMessage());
			} catch (Throwable e) {
				logger.error("Unknow error connecting to WASUP: "
						+ e.getMessage(), e);
			}
		}*/
	}
	
	public boolean deletingMonitoring (ServiceApplication sap)
	{
		logger.info("Deleting monitoring ");
		if (SMConfiguration.getInstance().isMonitoringEnabled()) {
			try {
				java.net.URLClassLoader  classLoader = (java.net.URLClassLoader)ClassLoader.getSystemClassLoader();

				Class mon  =  classLoader.loadClass(SMConfiguration.getInstance().getMonitoringClass());
				MonitoringVMIHandler client  = (MonitoringVMIHandler) mon.getConstructor(String.class).newInstance(SMConfiguration.getInstance().getMonitoringUrl());
				//MonitoringVMIHandler client = (MonitoringVMIHandler)classDriver;
				//MonitoringClient client = new MonitoringClient (SMConfiguration.getInstance().getMonitoringUrl());
				client.deleteSetupMonitoring(sap.getFQN().toString());
				return true;
			
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error("Unknow error connecting to monitoring systems: "
						+ e.getMessage(), e);
				return false;
			}
		}
		return false;

	/*	if (SMConfiguration.getInstance().isWasupActive()) {
			try {
				wasupClient.createWasupHierarchy(getServiceApplication());
			} catch (IOException e) {
				logger
				.error("Error creating the WASUP monitorization hierarchy: "
						+ e.getMessage());
			} catch (JSONException e) {
				logger.error("Error parsing the response of a WASUP request: "
						+ e.getMessage());
			} catch (Throwable e) {
				logger.error("Unknow error connecting to WASUP: "
						+ e.getMessage(), e);
			}
		}*/
	}
	
	private String getListIpsService (ServiceApplication sap)
	{
		String listips = "";
		for (VEE vee: sap.getVEEs())
		{
			for (VEEReplica replica: vee.getVEEReplicas() )
			{
				for (NIC nic: replica.getNICs())
				{
					if (nic.getNICConf().getNetwork().getPrivateNet())
						continue;
					
				     listips = listips + nic.getIPAddresses().get(0)+" ";
				}
			}
		}
		return listips;
		
		
	}

}
