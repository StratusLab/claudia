package com.telefonica.claudia.slm.paas.vmiHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javassist.convert.Transformer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.StringRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.w3c.dom.Document;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;


import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class NUBAMonitoringClient extends MonitoringVMIHandler {
	
	private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;

    private static Logger logger = Logger.getLogger(NUBAMonitoringClient.class);
    
    public NUBAMonitoringClient (String url)
    {
    	logger.info("MonitoringClient Url " + url);
    	if (url.charAt(url.length()-1) == '/')
            serverURL = url.substring(0, url.length()-1);
        else
            serverURL = url;

        client = new Client(Protocol.HTTP);
    }
    
   

	

	public void setUpMonitoring(String fqn, String ip) throws AccessDeniedException,
			CommunicationErrorException {
        
        
        Reference urlMonitoring = new Reference(serverURL );
		
		System.out.println ("Setting up monitoring in URL " +serverURL + " for " + fqn + " " + ip);

        // Call the server with the URI and the data
		DomRepresentation data;
		
        try {

        	Document doc = getMonitoringParams(fqn,ip);	
        	Element root = doc.getDocumentElement();
        	String s = root.toString();
        	System.out.println ("Data sent " + getString(doc));
        	data = new DomRepresentation(MediaType.APPLICATION_XML,doc);

        } catch (ParserConfigurationException e) {
            logger.error("Error creating parser: " + e.getMessage());
            return;
        }
        
    /*    StringRepresentation rep = null;
        try {
        	String test = getMonitoringParams1 (fqn,ip);
        	System.out.println (test);
			 rep = new StringRepresentation(getMonitoringParams1 (fqn,ip));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
               
        // Call the server with the URI and the data
        logger.debug("Posting Monitoring information: " );
        Response response = client.post(urlMonitoring,data);

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
        /*        Document responseXml;

            
			try {
				if (response.getEntityAsDom()!= null)
				{
				responseXml = response.getEntityAsDom().getDocument();
				System.out.println (PaasUtils.tooString(responseXml));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
                   
             break;

                
        }
     

	}
	
	public void deleteSetupMonitoring(String fqn) throws AccessDeniedException,
	CommunicationErrorException {


		Reference urlMonitoring = new Reference(serverURL +"/"+fqn);

		System.out.println ("URL " +urlMonitoring );

		
		// Call the server with the URI and the data
		logger.debug("Deleing Monitoring information for "+ fqn );
		Response response = client.delete(urlMonitoring);

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

			break;
  
		}

}
	
	public Document getMonitoringParams(String fqn) throws ParserConfigurationException
	{
		logger.info("Obtain XML for monitoring data for " +fqn);
		String md5 = this.getMd5FromFQN(fqn);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
       
        Element root = doc.createElement("");
        doc.appendChild(root);

      
        
      
      
		return doc;
	}
	
	public String getMd5FromFQN (String fqn) 
	{
		
		 
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
		}
        md.update(fqn.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
      
		return sb.toString();
	}
	
	public Document getMonitoringParams (String fqn, String ip) throws ParserConfigurationException
	{
		logger.info("Obtain XML for installing Product");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
       
        Element root = doc.createElement("DatasourceList");
        doc.appendChild(root);
        
        Element DatasourceData  = doc.createElement("DatasourceData");
        DatasourceData.setAttribute("name", fqn);
        DatasourceData.setAttribute("source", ip);
        root.appendChild(DatasourceData);
        return doc;
        
	}
	
	public String getMonitoringParams1 (String fqn, String ip) throws ParserConfigurationException
	{
		String text="<DatasourceList>\n"+
		"<DatasourceData name='"+fqn+"' source='"+ip+"'/>\n"+
		"</DatasourceList>";
		return text;
        
	}
	
	
	
	public String getString (Document doc)
	{
		TransformerFactory tFactory = TransformerFactory.newInstance();
	    javax.xml.transform.Transformer transformer = null;
		try {
			transformer = tFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    DOMSource source = new DOMSource(doc);
	    StringWriter sw = new StringWriter();
	    StreamResult result = new StreamResult(sw);
	   
	    transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
	    try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    return sw.toString();

	}



	public void removeMonitoring(String fqn) throws AccessDeniedException,
			CommunicationErrorException {
		// TODO Auto-generated method stub
		
	}



	

}
