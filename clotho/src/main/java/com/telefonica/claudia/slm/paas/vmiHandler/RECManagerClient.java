package com.telefonica.claudia.slm.paas.vmiHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.abiquo.ovf.exceptions.XMLException;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class RECManagerClient implements VMIHandler {
	
	private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;

    private static Logger logger = Logger.getLogger(RECManagerClient.class);
    
    public RECManagerClient (String url)
    {
    	logger.info("SDC Url " + url);
    	if (url.charAt(url.length()-1) == '/')
            serverURL = url.substring(0, url.length()-1);
        else
            serverURL = url;

        client = new Client(Protocol.HTTP);
    }

	public void installApplication(Product product)
			throws AccessDeniedException, CommunicationErrorException {
		

	}

	public void installProduct(Product product, String ip) throws AccessDeniedException,
			CommunicationErrorException {
		
		
	   Reference urlReplica = new Reference(serverURL +  URICreation.getURIProduct(product.getFQN().toString()));
		
		System.out.println ("URL " +urlReplica);

        // Call the server with the URI and the data
		DomRepresentation data;
        try {

        		data = new DomRepresentation(MediaType.APPLICATION_XML, getInstallProductParams(product,ip));

        } catch (ParserConfigurationException e) {
            logger.error("Error creating parser: " + e.getMessage());
            return;
        } catch (XMLException e) {
			// TODO Auto-generated catch block
        	
			e.printStackTrace();
			logger.error("Error creating parser: " + e.getMessage());
            return;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			logger.error("Error creating parser: " + e.getMessage());
            return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error creating parser: " + e.getMessage());
            return;
		}

        // Call the server with the URI and the data
        logger.debug("Posting product information: " );
        Response response = client.post(urlReplica,data);

        // Depending on the response code, return with an error, or wait for a response
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
                logger.info("Operation suceesfully done.");
                Document responseXml;

            
			try {
				responseXml = response.getEntityAsDom().getDocument();
				System.out.println (PaasUtils.tooString(responseXml));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

                    
                break;
                
                
                    
                
        }
     

	}
	
	public Document getInstallProductParams(Product product,String ip) throws ParserConfigurationException, XMLException, SAXException, IOException
	{
		 String productxml = product.getProductXML(ip);
		 
		 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
         InputStream is = new ByteArrayInputStream(productxml.getBytes("UTF-8"));
        
        	 
         Document doc = docBuilder.parse (is);

      
		return doc;
	}

}
