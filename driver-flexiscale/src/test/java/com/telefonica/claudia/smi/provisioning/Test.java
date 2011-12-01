package com.telefonica.claudia.smi.provisioning;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.flexiant.extility.FlexiScaleServiceLocator;
import com.flexiant.extility.FlexiScaleSoapBindingStub;
import com.flexiant.extility.Server;

public class Test {

	private final static String USER_PROPERTY = "flexiscaleUser";
	private final static String PASSWORD_PROPERTY = "flexiscalePassword";	
	private final static String URL_PROPERTY = "flexiscaleAddresss";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FlexiscaleDriver dd = new FlexiscaleDriver(null);
		 String user;
		 String pass;
		 String endpointAddress;
		 FlexiScaleSoapBindingStub service = null;
		dd.generateXMLVEE ("es.tid.customers.cc1.dd.services.ser.vees.dd.replicas.1", "10.98.56.54", "user", "password");
		

		    	user="4caast@flexiant.com";
		    	pass="tn5AW7Bx";
		    	
		    	endpointAddress="https://api2.flexiscale.com/?wsdl";
			FlexiScaleServiceLocator locator = new FlexiScaleServiceLocator();
			locator.setFlexiScaleEndpointAddress(endpointAddress);
			try {
				service =(FlexiScaleSoapBindingStub) locator.getFlexiScale();
			} catch (ServiceException e) {
				e.printStackTrace();
			
				return;
				
			}
			System.out.println ("user" + user);
			System.out.println ("paas" + pass);
			
			System.out.println ("url" + endpointAddress);
			
			
			service.setUsername(user);
			service.setPassword(pass);
			
			Server[] dde = null; 
			try {
				dde = service.listServers(1842);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Server ddd: dde)
			{
				System.out.println (ddd.getServer_id() + " " + ddd.getServer_name());
			}
			
			try {
				Server s = service.getServer(9735);
				System.out.println (s.getInitial_password() + " " + s.getInitial_user());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
