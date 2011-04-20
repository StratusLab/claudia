package com.telefonica.claudia.configmanager.lb.impl;

import static com.telefonica.claudia.configmanager.common.ReturnCode.ERROR_INTERNAL_SERVER_ERROR;
import static com.telefonica.claudia.configmanager.common.ReturnCode.ERROR_NOT_AVAILABLE_NODE;
import static com.telefonica.claudia.configmanager.common.ReturnCode.SUCCESS_NODE_DELETE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
		Client client = new Client(Protocol.HTTP);
		
		// The URI of the node
		Reference lbUri = new Reference(Protocol.HTTP, ipLb, portLb);
		lbUri.addSegment(fqnNode);
		
		Response response = client.delete(lbUri);
		response.getEntity().release();
		response.release();
		return response.getStatus().getCode();
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
