package com.telefonica.claudia.slm.paas.vmiHandler;

import java.io.IOException;
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

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class SDCClient implements VMIHandler {
	
	private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;

    private static Logger logger = Logger.getLogger(SDCClient.class);
    
    public SDCClient (String url)
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
        Reference urlReplica = new Reference(serverURL + "/rest/product");
		
		System.out.println ("URL " +serverURL + "/rest/product");

        // Call the server with the URI and the data
		DomRepresentation data;
        try {

        		data = new DomRepresentation(MediaType.APPLICATION_XML, getInstallProductParams(product,ip));

        } catch (ParserConfigurationException e) {
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
	
	public Document getInstallProductParams(Product product,String ip) throws ParserConfigurationException
	{
		logger.info("Obtain XML for installing Product");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
       
        Element root = doc.createElement("productInstanceDto");
        doc.appendChild(root);

      
        
       // Set<Property> properties = product.getProperties();
        
        for (Property prop:  product.getProperties()) {

            Element attribute = doc.createElement("attributes");
            Element key = doc.createElement("key");
            key.appendChild(doc.createTextNode(prop.getKey()));
            Element value = doc.createElement("value");
            value.appendChild(doc.createTextNode(prop.getValue()));
            attribute.appendChild(key);
            attribute.appendChild(value);

            root.appendChild(attribute);
    	}
        
 
        
        Element vm = doc.createElement("vm");
        Element ipnode = doc.createElement("ip");
        ipnode.appendChild(doc.createTextNode(ip));
        vm.appendChild(ipnode);
        root.appendChild(vm);
        
        Element productnode = doc.createElement("product");
        Element productversion = doc.createElement("version");
        productversion.appendChild(doc.createTextNode(product.getVersion()));
        productnode.appendChild(productversion);
        Element productname = doc.createElement("name");
        productname.appendChild(doc.createTextNode(product.getName()));
        productnode.appendChild(productname);
        root.appendChild(productnode);
        
        System.out.println (PaasUtils.tooString(doc));

      
		return doc;
	}

}
