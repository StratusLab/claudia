package com.telefonica.claudia.smi.provisioning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.telefonica.claudia.smi.provisioning.FlexiscaleDriver.ControlActionType;
import com.telefonica.claudia.smi.provisioning.FlexiscaleDriver.DeployNetworkTask;

public class ProbarDriver {
	
	public static void main(String args[]) {

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("./conf/tcloud.properties"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FlexiscaleDriver fd=new FlexiscaleDriver(prop);
		try {
			//
	//		System.out.println(fd.deployVirtualMachine("es.tid.customers.cc1.services.ss3.vapp.vapp1.replicas.1","./conf/ovf.xml"));
			//fd.deleteVirtualMachine("es.tid.customers.cc1.services.ss1.vapp.lau.VEEExecutor.replicas.1");

		//	
	//	System.out.println(fd.deleteVirtualMachine("es.tid.customers.cc1.services.b2.vapp.vapp1.replicas.1"));
			//fd.createMetaData("es.tid.customers.cc1.services.ss1.vapp.lau.VEEExecutor.replicas.1");
			//fd.deployNetwork("es.tid.customers.cc1.networks.admin_net", "iop");
	    //
	
		System.out.println(fd.deployNetwork("public", "./conf/net.xml"));
		
		//
		//  fd.deleteNetwork("es.tid.customers.cc1.services.s2.networks.admin_net");
		  //fd.powerActionVirtualMachine("es.tid.customers.cc1.services.b3.vapp.vapp1.replicas.1","./conf/ovf.xml");
			System.out.println(fd.powerActionVirtualMachine("es.tid.customers.cc1.services.ss3.vapp.vapp1.replicas.1","stop"));
			System.out.println(fd.powerActionVirtualMachine("es.tid.customers.cc1.services.ss3.vapp.vapp1.replicas.1","reboot"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
	}
}
