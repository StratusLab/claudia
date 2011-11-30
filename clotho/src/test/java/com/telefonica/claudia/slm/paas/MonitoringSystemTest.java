package com.telefonica.claudia.slm.paas;

import com.telefonica.claudia.slm.paas.vmiHandler.MonitoringClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;

public class MonitoringSystemTest {
	
	public static void main (String [] args)
	{
		MonitoringClient client = new MonitoringClient("http://109.231.67.227:8080/registerfqn4monitoring_ws/webresources/maps2fqn/");
		try {
			client.setUpMonitoring("es.tid.customers.cc1.services.demo.vees.vee1.replicas.3","");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
