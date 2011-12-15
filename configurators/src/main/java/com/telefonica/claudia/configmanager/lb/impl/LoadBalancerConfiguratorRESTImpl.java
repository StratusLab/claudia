package com.telefonica.claudia.configmanager.lb.impl;

import static com.telefonica.claudia.configmanager.common.ReturnCode.ERROR_INTERNAL_SERVER_ERROR;
import static com.telefonica.claudia.configmanager.common.ReturnCode.ERROR_NOT_AVAILABLE_NODE;
import static com.telefonica.claudia.configmanager.common.ReturnCode.SUCCESS_NODE_DELETE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.ObjectRepresentation;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.configmanager.lb.LoadBalancerConfigurator;


public class LoadBalancerConfiguratorRESTImpl implements LoadBalancerConfigurator {


	
	private Properties properties;

	public LoadBalancerConfiguratorRESTImpl() {

	}


	public int addNode(String ipLb, int portLb, String fqnNode, String ipNode) {
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);
		
		// The URI of the load balancer
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		
		// Fill the form for adding the new node
		Form newNodeForm = new Form();
		newNodeForm.add("fqn", fqnNode);
		newNodeForm.add("ip", ipNode);
		Representation representation = newNodeForm.getWebRepresentation();
	//	System.out.println (representation.getText());
		
		// Make the post operation sending the form an get the response
		Response response = client.post(lbUri, representation);
		response.getEntity().release();
		response.release();
		return response.getStatus().getCode();
	
	}
	

	public int addNodes(String ipLb, int portLb, Map<String, String> nodes) {
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.getAcceptedMediaTypes().add(new Preference<MediaType>(MediaType.APPLICATION_JAVA_OBJECT));
		
		// The URI of the load balancer
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		
		// Fill the representation with the list of nodes
		HashMap<String, String> aux = new HashMap<String, String>();
		aux.putAll(nodes);
		Representation representation = new ObjectRepresentation<HashMap<String, String>>(aux);
		
		// Make the post operation sending the list of nodes an get the response
		Response response = client.post(lbUri, representation);
		response.getEntity().release();
		response.release();
		return response.getStatus().getCode();
	}


	public int updateNode(String ipLb, int portLb, String fqnNode, String ipNode) {
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);
		
		// The URI of the node
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		lbUri.addSegment(fqnNode);
		
		// Fill the form for updating de IP
		Form updateIpfrom = new Form();
		updateIpfrom.add("ip", ipNode);
		
		Response response = client.put(lbUri, updateIpfrom.getWebRepresentation());
		response.getEntity().release();
		response.release();
		return response.getStatus().getCode();
	}


	public int removeNode(String ipLb, int portLb, String fqnNode) {
		// Define our Restlet HTTP client.
		
		
	/*	Client client = new Client(Protocol.HTTP);
		
	
		// The URI of the node
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		lbUri.addSegment("removeWN/");
		
		Response response = client.delete(lbUri);
		response.getEntity().release();
		
		
		
		response.release();
		return response.getStatus().getCode();*/
		return 200;

		
	}
	
	public String [] getNodeRemove(String ipLb, int portLb,  int numbernodes) throws Exception {
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);
		String [] ips = new String [numbernodes];
		
		 System.out.println ("Iclient");
		// The URI of the node
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		lbUri.addSegment("lb_server/removeWN/"+numbernodes);
		
		
		Response response = client.get(lbUri);
		String ipstext = null;
		 Representation output = null;
		 System.out.println ("IP text" + response + response.getStatus().isSuccess());
	      if (response.getStatus().isSuccess()) { 
	    	  
	    	  
	         if (response.isEntityAvailable()) { 

	        		ipstext = response.getEntity().getText();
					System.out.println ("IP text" + ipstext);
	         }  
	      }  
	      else
	      {
	    	 return null;
	      }

	      
		response.getEntity().release();
		System.out.println (response.getStatus());
	
	
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
		
		for (int i=0; i<numbernodes; i++)
		{
		
			if (nodes.item(i).getNodeName().equals("node"))
			{
				ips[i] = nodes.item(i).getFirstChild().getNodeValue();
				System.out.println (ips[i]);
			}
		}
		
	//	ips[0] ="62.217.120.164";
		return ips;
	 
	}

	

	public ArrayList<String> removeNodes(String ipLb, int portLb, List<String> nodeList) {
		int status;
		ArrayList<String> statusList = new ArrayList<String>();
		
		for(String node : nodeList) {
			status = removeNode(ipLb, portLb, node);
			if(status == SUCCESS_NODE_DELETE) {
				statusList.add(node + " DELETED");
			}
			else if(status == ERROR_NOT_AVAILABLE_NODE) {
				statusList.add(node + " ERROR_NOT_AVAILABLE_NODE");
			}
			else if(status == ERROR_INTERNAL_SERVER_ERROR) {
				return null;
			}
		}
		
		return statusList;
	}


	public ArrayList<String> getNodes(String ipLb, int portLb) {
		// The URI of the load balancer
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.getAcceptedMediaTypes().add(new Preference<MediaType>(MediaType.APPLICATION_JAVA_OBJECT));
		Request request = new Request(Method.GET, lbUri);
		request.setClientInfo(clientInfo);
		
		// Make the get operation
		Response response = client.handle(request);
		try {
			ArrayList<String> listaNodos = new ObjectRepresentation<ArrayList<String>>(response.getEntity()).getObject();
			response.getEntity().exhaust();
			return listaNodos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setProperties(Properties prop) {
		this.properties = prop;

	}

}
