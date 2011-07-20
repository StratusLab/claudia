package com.telefonica.claudia.paastests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

public class TestLoadBalancerDeleteNode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		
		// TODO Auto-generated method stub
		Client client = new Client(Protocol.HTTP);
		String [] ips = new String [1];
		
	
		// The URI of the node
		Reference lbUri = new Reference(Protocol.HTTP, "62.217.120.164", 8080);
		lbUri.addSegment("removeWN/1");
		
		
		Response response = client.get(lbUri);
		String ipstext = null;
		 Representation output = null;
		
	      if (response.getStatus().isSuccess()) {  
	         if (response.isEntityAvailable()) { 
	        	try {
	        		ipstext = response.getEntity().getText();
					System.out.println ("tt" + ipstext);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	        	  
	        	 
	           
	         }  
	      }  
	      
	      
		response.getEntity().release();
		System.out.println (response.getStatus());
		
		if (response.getStatus().getCode() == 500)
			return;
		
		
		
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println ("ipt tests" + ipstext);
		Document doc = null;
		
			try {
				doc = builder.parse(new ByteArrayInputStream(ipstext.getBytes()));
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		NodeList nodes = doc.getElementsByTagName("node");
		
		for (int i=0; i<nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeName().equals("node"))
			{
				ips[i] = nodes.item(i).getFirstChild().getNodeValue();
				System.out.println (ips[i]);
			}
		}
	 
	}

}
