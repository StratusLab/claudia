package com.telefonica.claudia.smi.provisioning;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

public class TestDeployPublicNubaNet {
	

	@Test
    public void testDeployNetwork3() {
	

		
		
		OneOperations tester = prepareOneOperations (3);
		
		tester.configOperations("3.0", "br0",  "/opt/claudia/repository","/opt/claudia/extrafiles",null, "18888",
				"/boot/vmlinuz-2.6.26-2-xen-amd64", "/boot/initrd.img-2.6.26-2-xen-amd64", "s" , "", "10.65.44.44", null, null);
		
		
	
		    try
		    {
		    	tester.deployNetwork(readFileAsString ("./src/test/resources/publicnuba"));
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	/*@Test
	public void testGetVirtualMachine ()
	{
		OneOperations tester = prepareOneOperations (3);
		
		tester.configOperations("3.0", "br0",  "/opt/claudia/repository", null, "18888",
				"/boot/vmlinuz-2.6.26-2-xen-amd64", "/boot/initrd.img-2.6.26-2-xen-amd64", "s" , "", "10.65.44.44");
		try {
			System.out.println(tester.getVirtualMachine("399"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	@Test
	public void doActionStop ()
	{
		OneOperations tester = prepareOneOperations (3);
		
		tester.configOperations("3.0", "br0",  "/opt/claudia/repository","/opt/claudia/extrafiles",null, "18888",
				"/boot/vmlinuz-2.6.26-2-xen-amd64", "/boot/initrd.img-2.6.26-2-xen-amd64", "s" , "", "10.65.44.44", null, null);
		try {
			System.out.println(tester.doAction("378", "power-off"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void doActionStart ()
	{
		OneOperations tester = prepareOneOperations (3);
		
		tester.configOperations("3.0", "br0",  "/opt/claudia/repository","/opt/claudia/extrafiles",null, "18888",
				"/boot/vmlinuz-2.6.26-2-xen-amd64", "/boot/initrd.img-2.6.26-2-xen-amd64", "s" , "", "10.65.44.44", null, null);
		try {
			System.out.println(tester.doAction("378", "power-on"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private OneOperations prepareOneOperations (int version)
	{
		String oneSession = "oneadmin:8944aada99d6d0121494dd0ac8129db653bc1f4a";
		
		// Testbed NUBA
		/* oneUrl= http://84.21.173.28:2633/RPC2
			oneUser=oneadmin
			onePassword=8944aada99d6d0121494dd0ac8129db653bc1f4a
			*/
		
		XmlRpcClient xmlRpcClient = new XmlRpcClient();
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			if (version == 3)
			   config.setServerURL(new URL("http://84.21.173.28:2633/RPC2"));
			else
				config.setServerURL(new URL("http://84.21.173.28:2634/RPC2"));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		xmlRpcClient.setConfig(config);

		
		
		OneOperations tester = new OneOperations(oneSession, xmlRpcClient);
		return tester;
	}
	
	

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

}
