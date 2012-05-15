package com.telefonica.claudia.smi.provisioning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.smi.URICreation;

public class ONEUtilities {
	
	
	
	public String obtainIdOneNetworkFromFQN (String fqn, String tests)
	{
		
		do
		{
          String vnet = tests.substring(tests.indexOf("<VNET>")+"<VNET>".length(),tests.indexOf("</VNET>"));
          tests = tests.substring (tests.indexOf("</VNET>")+ "</VNET>".length() );
      
         
          if (fqn.contains("."))
        	  fqn = fqn.substring(fqn.lastIndexOf("."),fqn.length());
          
          boolean included = isFqnIncluded (vnet, fqn);
          
          System.out.println ("Included  " + fqn + included);
         
          if (included)
          {
    	    return obtainIdFromVnet (vnet);
          }
		}
		while ((tests.indexOf("<VNET>")!=-1));
     
    
     return null;
     
	}
	
	public boolean isFqnIncluded (String vnet, String fqn)
	{
		System.out.println ("vnet " + vnet +  " fqn + " +fqn);
		if (fqn.contains("."))
		{
        	  fqn = fqn.substring(fqn.lastIndexOf(".")+1,fqn.length());
		}
		if (fqn.indexOf("public")!=-1)
		{
			if (vnet.indexOf("public")!= -1)
				return true;
		}
		else if (vnet.indexOf(fqn)!=-1)
			return true;
		else
			return false;
		
	     return false;
	}
	
	public String obtainIdFromVnet ( String vnet)
	{
		
       String id = vnet.substring(vnet.indexOf("<ID>")+"<ID>".length(),vnet.indexOf("</ID>"));
       return id;
	}
	
	
	public HashMap getCpuRamDisk (String xml) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap map = new HashMap ();
	
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

		NodeList vmList = doc.getElementsByTagName("VM");
			
			
			

		for (int i=0; i < vmList.getLength(); i++) {

				Element vm = (Element) vmList.item(i);

				String ram = ((Element)vm.getElementsByTagName("MEMORY").item(0)).getTextContent();
				
				map.put("MEMORY", ram);
				
				String cpu = ((Element)vm.getElementsByTagName("CPU").item(0)).getTextContent();
				map.put("CPU", cpu);
				
				String disk = ((Element)vm.getElementsByTagName("DISK").item(0)).getElementsByTagName("SIZE").item(0).getTextContent();
				map.put("DISK", disk);

				System.out.println ("RAM " +ram);
				System.out.println ("CPU " +cpu);
		
				
				System.out.println ("DISK " +disk);
		}	
				
		return map;

	}

	public HashMap getNetworksIp (String xml) throws ParserConfigurationException, SAXException, IOException
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap map = new HashMap ();
	
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

		NodeList remoteNIC = doc.getElementsByTagName("REMOTE_IP"); 
		
		if (remoteNIC == null || remoteNIC.getLength()==0)
		{
			System.out.println("Not remote IP");
		}
		else
		{
			String ip = remoteNIC.item(0).getTextContent();
			map.put("public", ip);
			return map;
		}
		NodeList nicList = doc.getElementsByTagName("NIC");
		
		for (int i = 0; i < nicList.getLength(); i++) 
		{
			Node nic = nicList.item(i);
			
			NodeList child = nic.getChildNodes();
			String ip = null;
			String network = null;
			for (int j = 0; j < child.getLength(); j++) 
			{
				
				System.out.println (child.item(j).getNodeName());
				if (child.item(j).getNodeName().equals("IP"))
				{
					System.out.println ("Obtained Ip " + child.item(j).getTextContent());
					ip = child.item(j).getTextContent();
				
				}
				else if (child.item(j).getNodeName().equals("NETWORK"))
				{
					System.out.println ("Obtained network " + child.item(j).getTextContent());
					network = child.item(j).getTextContent();
				}
					
			}
			map.put(getNetworkName (network), ip);
			
		}
		return map;
	}
	
	private String getNetworkName (String fqnname)
	{
		while (fqnname.indexOf(".")!=-1)
		{
			fqnname = fqnname.substring(fqnname.indexOf(".")+1, fqnname.length());
			System.out.println (fqnname);
		}
		return fqnname;
	}
	public String generateXMLVEE (String fqn, HashMap ips, String cpu, String ram, String disk)
    {
 	   DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	   DocumentBuilder docBuilder;
	   Document doc = null;
	    	
	   String organizationId =  URICreation.getOrg(fqn).replace(".", "_");
	   String vdcid = URICreation.getVDC(fqn).substring(fqn.indexOf("customers")+"customers".length()+1);
	        
	   String vappid =  URICreation.getService(fqn).substring(fqn.indexOf("services")+"services".length()+1);
	   String veeid =  URICreation.getVEE(fqn).substring(fqn.indexOf("vees")+"vees".length()+1);
	        
	        try {
				docBuilder = dbfac.newDocumentBuilder();
				
				doc = docBuilder.newDocument();
				
	
		    			
		    	Element veeReplicaElement = doc.createElement("VApp");
		    	doc.appendChild(veeReplicaElement);
		    			
		    	veeReplicaElement.setAttribute("name", fqn);

		    	veeReplicaElement.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" +   vdcid + "/vapp/" +
		    			vappid + "/" + veeid + "/" + 1);
		    			
		    	Element linkVeeReplica = doc.createElement("Link");
		    	veeReplicaElement.appendChild(linkVeeReplica);
		    			
		    	linkVeeReplica.setAttribute("rel", "monitor:measures");
		    	linkVeeReplica.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
		    	linkVeeReplica.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" +   
		    			vdcid + "/vapp/" + vappid + "/" + veeid + "/" + "1" + "/monitor");
		    	
		    //	Element productvm = GetOperationsUtils.getInstallProductInVirtualMachine(doc, user, password);
		    //	veeReplicaElement.appendChild(productvm);

		    			
		    	Element virtuahardware = GetOperationsUtils.getVirtualHardwareSystem(doc, "@HOSTNAME/api/org/" + organizationId + "/vdc/" +  
		    					vdcid + "/vapp/" + 
		    					vappid +"/" + veeid + "/" + 1, cpu,ram,disk,ips);
		    	
		    	veeReplicaElement.appendChild(virtuahardware);

		    	

			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}

			OutputFormat format    = new OutputFormat (doc); 
         // as a String
         StringWriter stringOut = new StringWriter ();    
         XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                     format);
         try {
 			serial.serialize(doc);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         // Display the XML
         System.out.println("XML " + stringOut.toString());
         return  stringOut.toString();
    }



}
