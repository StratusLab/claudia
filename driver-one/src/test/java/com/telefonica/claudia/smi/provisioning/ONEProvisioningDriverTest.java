package com.telefonica.claudia.smi.provisioning;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import com.telefonica.claudia.smi.task.TaskApplication;
import com.telefonica.claudia.smi.task.TaskManager;
import com.telefonica.claudia.smi.task.Task.TaskStatus;

public class ONEProvisioningDriverTest {
	
@Test
	
	public void getVM() {
		
		ONEProvisioningDriver one = getPreConditions();
		
		try {
		//	long vnet = one.deployNetwork("org.tid.customers.otro.services.tel.networks.net",readFileAsString ("./src/test/resources/network1.xml"));
			
		//	System.out.println ("Network deployed " + vnet);
			
		//	System.out.println ("Deleting network " + vnet);
			
			one.getVirtualMachine("grnet.customers.dd.services.ssss.vees.VEEMaster.replicas.1");
			System.out.println ("powering off  " );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	
	public void testStartStop() {
		
		ONEProvisioningDriver one = getPreConditions();
		
		try {
		//	long vnet = one.deployNetwork("org.tid.customers.otro.services.tel.networks.net",readFileAsString ("./src/test/resources/network1.xml"));
			
		//	System.out.println ("Network deployed " + vnet);
			
		//	System.out.println ("Deleting network " + vnet);
			
			one.powerActionVirtualMachine("CESGA.customers.dddd.services.dd.vees.VEEMaster.replicas.1","power-on");
			System.out.println ("powering off  " );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
/*	@Test

	public void testDeployNetwork() {
		
		ONEProvisioningDriver one = getPreConditions();
		
		try {
		//	long vnet = one.deployNetwork("org.tid.customers.otro.services.tel.networks.net",readFileAsString ("./src/test/resources/network1.xml"));
			
		//	System.out.println ("Network deployed " + vnet);
			
		//	System.out.println ("Deleting network " + vnet);
			
			one.deleteNetwork("org.tid.customers.otro.services.tel.networks.net");
			System.out.println ("network deleting " );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/*	@Test
	public void testDeployVirtualMachine() {
       ONEProvisioningDriver one = getPreConditions();
		
		try {
			long vm = one.deployVirtualMachine("org.tid.customers.otro.services.tel.vees.vee",readFileAsString ("./src/test/resources/ovf.xml"));
			System.out.println ("VM deployed " + vm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void testGetVirtualMachine() {
     ONEProvisioningDriver one = getPreConditions();
		
		try {
			String vm = one.getVirtualMachine("CESGA.customers.26-TELEFONICA.services.48-test2.vees.LastApp.replicas.1");
			System.out.println ("VM deployed " + vm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	@Test
	public void testDoActionVirtualMachine() {
     ONEProvisioningDriver one = getPreConditions();
		
		try {
			long vm = one.powerActionVirtualMachine("org.tid.customers.otro.services.tel.vees.vee","power-off");
			System.out.println ("VM deployed " + vm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteVirtualMachine() {
		System.out.println ("Deleting VM org.tid.customers.otro.services.tel.vees.vee");
     ONEProvisioningDriver one = getPreConditions();
		
		try {
			long vm = one.deleteVirtualMachine("org.tid.customers.otro.services.tel.vees.vee");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteNetwork() {
       ONEProvisioningDriver one = getPreConditions();
       System.out.println ("Deleting network org.tid.customers.otro.services.tel.networks.net");
		try {
			long vm = one.deleteNetwork("org.tid.customers.otro.services.tel.networks.net");

			System.out.println ("Network deployed " + vm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	

	

	@Test
	public void testGetNetwork() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNetworkList() {
		//fail("Not yet implemented");
	}
*/
	
	
	private String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
	
	private ONEProvisioningDriver getPreConditions ()
	{
		Properties props = new Properties();
	 
	    FileInputStream fis;
		try {
			fis = new FileInputStream("./src/main/resources/tcloud.properties");
			props.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   
		
		TaskManager taskManager = TaskApplication.setDriver(com.telefonica.claudia.smi.task.ClaudiaTaskManager.class, props);
		taskManager.createManager(taskManager);	
	  
		ONEProvisioningDriver one = new ONEProvisioningDriver (props);
		return one;
	}

}
