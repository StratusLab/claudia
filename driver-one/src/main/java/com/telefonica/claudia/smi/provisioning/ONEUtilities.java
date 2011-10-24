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
      
     
          if (isFqnIncluded (vnet, fqn))
          {
    	    return obtainIdFromVnet (fqn, vnet);
          }
		}
		while ((tests.indexOf("<VNET>")!=-1));
     
    
     return null;
     
	}
	
	public boolean isFqnIncluded (String vnet, String fqn)
	{
		if (vnet.indexOf(fqn)!= -1)
			return true;
		else
			return false;
	}
	
	public String obtainIdFromVnet (String fqn, String vnet)
	{
		
       String id = vnet.substring(vnet.indexOf("<ID>")+"<ID>".length(),vnet.indexOf("</ID>"));
       return id;
	}
	
	
	public HashMap getCpuRamDiskIp (String xml) throws ParserConfigurationException, SAXException, IOException
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
				
				String ip = ((Element)vm.getElementsByTagName("IP").item(0)).getTextContent();
				map.put("IP", ip);
				
				String disk = ((Element)vm.getElementsByTagName("DISK").item(0)).getElementsByTagName("SIZE").item(0).getTextContent();
				map.put("DISK", disk);
				
				
				
				
				System.out.println ("RAM " +ram);
				System.out.println ("CPU " +cpu);
				System.out.println ("IP " +ip);
				
				System.out.println ("DISK " +disk);
		}	
				
		return map;
				
				
		

	
	}
	
	public String generateXMLVEE (String fqn, String ip, String cpu, String ram, String disk)
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
		    					vappid +"/" + veeid + "/" + 1, cpu,ram,disk,ip);
		    	
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
