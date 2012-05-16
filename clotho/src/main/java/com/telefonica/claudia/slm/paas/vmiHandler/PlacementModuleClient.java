package com.telefonica.claudia.slm.paas.vmiHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import javax.xml.parsers.ParserConfigurationException;


import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;

import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;

import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;


import com.telefonica.schemas.nuba_model.exp.CIMUserEntityType;




public class PlacementModuleClient  {
	
	private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;

    private static Logger logger = Logger.getLogger(PlacementModuleClient.class);
    
    public PlacementModuleClient (String url)
    {
    	logger.info("PlacementModuleClient Url " + url);
    	if (url.charAt(url.length()-1) == '/')
            serverURL = url.substring(0, url.length()-1);
        else
            serverURL = url;

        client = new Client(Protocol.HTTP);
    }
    
  
	public String bestOVFProvider(String filename) throws JAXBException, AccessDeniedException, CommunicationErrorException, XMLException, IOException{
		
		
		
		String url = serverURL+ "/api/org/1/vdc/1/action/providermapping";
		logger.info("Url operation " + url);
		url = "http://109.231.81.30:8892/placement/api/org/1/vdc/1/action/ovfprovidermapping";
		
	/*	Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		filename)));
	    JAXBContext context = JAXBContext.newInstance(EnvelopeType.class);
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) unmarshaller.unmarshal( reader );
	    
		com.sun.jersey.api.client.Client c = com.sun.jersey.api.client.Client.create();
		WebResource r = c.resource(filename);
		
		CIMUserEntityType response = r.accept(javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE).
	        type(javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE).
	        post(CIMUserEntityType.class, obj);
		
	    System.out.println("response testOVFProviderMappingJersey ->" + response);
	    System.out.println("Response id:"+response.getId());
	    System.out.println("Response URL:"+response.getURLDRP());*/
		
		
		return sendingOperation (filename, url);

	}
	
	public String sendingOperation(String filename, String url) throws JAXBException, AccessDeniedException, CommunicationErrorException, XMLException, IOException{
		
		File xmlFile = new File(filename);
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		ovfSerializer.setValidateXML(false);
		EnvelopeType envelope = ovfSerializer.readXMLEnvelope(new FileInputStream(
				xmlFile));
		DomRepresentation data;
		
		   try {

			Document doc = OVFSerializer.getInstance().bindToDocument(envelope, false);	
        	Element root = doc.getDocumentElement();
        	String s = root.toString();
        	System.out.println (s);
        	
        	data = new DomRepresentation(MediaType.APPLICATION_XML,doc);

        } catch (ParserConfigurationException e) {
            logger.error("Error creating parser: " + e.getMessage());
            return null;
        }
		Response response = client.post(url, data);

		// Depending on the response code, return with an error, or wait for a response
		System.out.println ("Response " + response.getStatus().getCode());
		switch (response.getStatus().getCode()) {

		case 401:    // Unauthorized
		case 403:    // Forbidden
        // Throw an Access Denied Exception
			logger.error("Not enough privileges to access the VM information.");
			throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

		case 400:    // Bad Request
		case 404:    // Not found
			logger.error("The resource was not found on the server; the tcloud server may be misconfigured, or the URL may be wrong.");
			throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

		case 501:
		case 500:
			logger.error("Internal error in the VEEM tcloud server: " + response.getStatus().getDescription());
			throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

		case 202:
		case 201:
		case 200:
		case 204:
			logger.info("Operation suceesfully done.");
			
			DomRepresentation dataresponse = response.getEntityAsDom();
			JAXBContext contextEnvelope = JAXBContext.newInstance(CIMUserEntityType.class);
			Binder<Node> binder = contextEnvelope.createBinder();        
			
			
			
			  
	        
	        JAXBElement<CIMUserEntityType> jaxb = binder.unmarshal(dataresponse.getDocument(), CIMUserEntityType.class); 
	        CIMUserEntityType provider = jaxb.getValue();
	        
	       

	       System.out.println("response testOVFProviderMappingJersey ->" + response);
	       System.out.println("Response id:"+provider.getId());
	       System.out.println("Response URL:"+provider.getURLDRP());
	       
	       return provider.getURLDRP();

		}
		return null;
	
	}

}
