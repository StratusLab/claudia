package com.telefonica.claudia.smi.provisioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.flexiant.extility.FlexiScaleServiceLocator;
import com.flexiant.extility.FlexiScaleSoapBindingStub;
import com.flexiant.extility.ServerMetadata;
import com.flexiant.extility.VDC;
import com.flexiant.extility.Vlan;

public class DeleteVM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String user ="axel.spriestersbach@sap.com";
		String pass ="36jS8WJ4";
		String endpointAddress="https://api2.flexiscale.com/index.php";
			
		FlexiScaleServiceLocator locator = new FlexiScaleServiceLocator();
		locator.setFlexiScaleEndpointAddress(endpointAddress);
		FlexiScaleSoapBindingStub service = null;
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
		
		VDC[] dd = null;
		try {
			dd = service.listVDCs();
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		VDC vdc = null;
		try {
			vdc = service.getVDC(1924);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for (VDC vlan: dd)
		{
			System.out.println(vlan.getVdc_id());
			System.out.println(vlan.getVdc_name());
		}
		
		Vlan[] dd2 = null;
		try {
			dd2 = service.listVLANs(1924);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for (Vlan vlan: dd2)
		{
			System.out.println(vlan.getVlan_name());
			System.out.println(vlan.getVlan_id());
		}
		
		
		long id = 9474;
		ServerMetadata meta = null;
		try {
			meta = service.getServerMetadata(id);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println (meta.getPublic_metadata());
		
		
		try {
			if (service.getServer(id).getStatus() == 2) {
				service.stopServer(id, 1);
				while (service.getServer(id).getStatus() != 5)
					try {
						Thread.sleep(1);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			if (service.getServer(id).getStatus() == 5)
				service.destroyServer(id);
			else System.out.println ("NO possible to be deployed");
			
		} catch(RemoteException re) {
			re.printStackTrace();
		}
			
			
			
		
	
	
	 //File inFile = new File(file);
    File inFile = new File("./conf/servers.txt");
    System.out.println("inFile"+inFile);
    if (!inFile.isFile()) {
       System.out.println("Parameter is not an existing file");
       return;
     }
      
     //Construct the new file that will later be renamed to the original filename.
     //File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
     //yo
     File tempFile = new File("./conf/servers.tmp");
	        
    
     BufferedReader br;
	try {
		br = new BufferedReader(new FileReader("./conf/servers.txt"));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		  
	     String line = null;

	     //Read from the original file and write to the new
	     //unless content matches data to be removed.
	     while ((line = br.readLine()) != null) {
	      
			
	   	  String str = new Long(id).toString();
	   	  
	       
	   	  //yo
	   	  if (!line.contains(str)) {
	         pw.println(line);
	         pw.flush();
	       }
	     }
	     pw.close();
	     br.close();
	     
	     //Delete the original file
	     if (!inFile.delete()) {
	       System.out.println("Could not delete file");
	       return;
	     }
	     
	     //Rename the new file to the filename the original file had.
	     if (!tempFile.renameTo(inFile))
	       System.out.println("Could not rename file");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
     
     
   }
   
	

}
