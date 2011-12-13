package com.telefonica.claudia.smi.provisioning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import com.flexiant.extility.FlexiScaleServiceLocator;
import com.flexiant.extility.FlexiScaleSoapBindingStub;
import com.flexiant.extility.Server;
import com.flexiant.extility.VDC;
import org.junit.Test;

public class TestFlexiscale {

	private final static String USER_PROPERTY = "flexiscaleUser";
	private final static String PASSWORD_PROPERTY = "flexiscalePassword";	
	private final static String URL_PROPERTY = "flexiscaleAddresss";
	/**
	 * @param args
	 */
	@Test
	public void testFlexiscale () {
		
		Properties prop = new Properties ();
		FileInputStream is;
		try { 
			is = new FileInputStream("."+File.separator+ "src"+File.separator+"main"+File.separator+"config"+File.separator+"tcloud.properties");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			prop.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		FlexiscaleDriver dd = new FlexiscaleDriver(prop);
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
			
			VDC[] vdcs = null; 
			try {
				vdcs = service.listVDCs();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (VDC vdc: vdcs)
			{
			  Server[] dde = null; 
			  try {
				  dde = service.listServers(vdc.getVdc_id());
			  } catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
			  
			  for (Server ddd: dde)
			  {
					System.out.println (ddd.getServer_id() + " " + ddd.getServer_name());
					try {
						Server s = service.getServer(ddd.getServer_id());
						System.out.println (s.getInitial_password() + " " + s.getInitial_user());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  }
			}
			
			
			
			

	}

}
