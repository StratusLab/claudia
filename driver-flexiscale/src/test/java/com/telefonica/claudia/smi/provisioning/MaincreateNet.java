package com.telefonica.claudia.smi.provisioning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


import org.restlet.Client;
import org.restlet.data.ClientInfo;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;

import org.restlet.resource.DomRepresentation;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;

import sun.util.logging.resources.logging;



public class MaincreateNet  {

	public static final String PATH_TO_PROPERTIES_FILE = "./src/main/config/cliente.properties";
	public static final String KEY_PORT="hostPort";
	public static final String KEY_HOST="hostName";
	//NO lo ha utilizado
	public static final String KEY_URI="serverURInet";
	public static final String KEY_XML="xmlFile";
		public static void main(String[] args) {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
			System.out.println("lee");
			
		} catch (FileNotFoundException e1) {
			
		
	
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
		}
		int port=Integer.parseInt(prop.getProperty(KEY_PORT));
		
		String host=prop.getProperty(KEY_HOST);
	
		//estaba comentado
		//String propURIs=prop.getProperty(KEY_URI);
		
		//el original
		String propURI="api/org/es_tid/vdc/cc1/vapp/vapp1/net/action/add";
		
		//es.tid.customers.c2.services.c.networks.admin_net
		///api/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/net/action/add
		
		System.out.println("propURI" + propURI);
		
	
		
		
		String xmlpath= "./conf/net.xml";
		// Define our Restlet HTTP client.
		Client client = new Client(Protocol.HTTP);

		System.out.println("client"+client);

	
		ClientInfo clientInfo = new ClientInfo();

		System.out.println("clientInfo"+clientInfo);

		
		clientInfo.getAcceptedMediaTypes().add(new Preference<MediaType>(MediaType.APPLICATION_XML));
		
		// The URI of the REST server
		Reference ServerUri = new Reference(Protocol.HTTP, host, port);

		System.out.println("ServerUri"+ServerUri);


		ServerUri.addSegment(propURI);

		System.out.println("ServerUri.addSegment(propURI)"+ServerUri.addSegment(propURI));

		
		
		File f=new File(xmlpath);

		System.out.println("f"+f);

		

		Representation representation = new FileRepresentation(f,MediaType.APPLICATION_XML);

		System.out.println("representation"+representation);


		/*try {
			System.out.println("l"+representation.getText().toString());
		} catch (IOException e) {
			
			e.printStackTrace();
		}*/
		
		
		//Response response = client.delete(ServerUri);
	

	    Response response = client.post(ServerUri, representation);
		System.out.println("response"+response);

		response.getEntity().release();
			//Liberar
		response.release();
		

		System.out.println("response.getEntity()"+response.getEntity());

	

	//yo	
	response.getStatus();
	System.out.println("response.getStatus()"+response.getStatus());

	/*	try {
			Assert.assertTrue(response.getStatus().isSuccess());
			System.out.println("Test pasado con éxito");
		} catch (AssertionFailedError e) {
			System.err.println("El test ha fallado");
		}
		*/
		System.out.println(response.getStatus().getCode());
		System.out.println(response.getStatus().getName());
		System.out.println(response.getStatus().getDescription());
		System.out.println(response.getStatus().getClass());
		System.out.println(response.getStatus().getUri());
	
	}
}
	
