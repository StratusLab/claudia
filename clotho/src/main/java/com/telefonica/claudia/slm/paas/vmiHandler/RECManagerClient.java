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
import org.dmtf.schemas.ovf.envelope._1.AnnotationSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
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

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.SectionAlreadyPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.paas.OVFContextualization;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.paas.URICreationPaaS;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.VEEReplicaAllocationResult;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class RECManagerClient implements VMIHandler {
	
	private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;
    MediaType APPLICATION_OVF_XML = MediaType.register("application/ovf+xml",
    "XML OVF document");

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
	

	public void installProductsInVm (VEE vee, String ip, String login, String password) throws CommunicationErrorException, AccessDeniedException, Exception
	{
		Reference urlReplica = new Reference(serverURL +  URICreationPaaS.getURIService((vee.getFQN().toString())) + "/vms");
			
		System.out.println ("URL " +urlReplica);
		
		DomRepresentation data = null;
        try {

        	    Document doc = getInstallProductInVirtualMachine(vee, ip, login, password);
        	    System.out.println ("VEE Product " + PaasUtils.tooString(doc));
        		data = new DomRepresentation(APPLICATION_OVF_XML, doc);

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
		} catch (SectionAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidSectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
                try {
    				String job = response.getEntity().getText();


    				while (true)
    				{
    					
                    String status = getTaskStatus(serverURL +"/jobs/"+job);
                    System.out.println ("Status " + serverURL +"/jobs/"+job + " " + status);

                    if (status.equals("FINISHED")) 
                       return;
                     else  if (status.equals("PENDING")) 
                     {
                    	 
                     }
                     else if (status.equals("FAILED"))   {
                    	// String error = getTaskMessageError ();
                    	logger.error("Error to create the VM " );
                    	
                    	 throw new Exception("Error to install the product in the VM: Aborting operation", null);
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {}
    			}
    				
    			
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				logger.info("Error to obtain the job.");
    			}
                    
                break;
                    
                
        }
		
	}

	public void installProductsInService (VEE vee) throws CommunicationErrorException, AccessDeniedException
	{
		Reference urlReplica = new Reference(serverURL +  "/applications");
			//	URICreationPaaS.getURIService(vee.getFQN().toString()));
			
		System.out.println ("URL " +urlReplica);
		
		DomRepresentation data = null;
        try {
        	
        	    Document doc = getEnvelope(vee.getServiceApplication().getSerAppName());
        	    System.out.println ("VEE Product " + PaasUtils.tooString(doc));
        		data = new DomRepresentation(APPLICATION_OVF_XML, doc);
        		
        		System.out.println(data.getMediaType());
        	

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
		} catch (SectionAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidSectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

            case 412:
            case 415:
            	logger.error("Internal error in communication " + response.getStatus().getDescription());
            	throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
            case 202:
            case 201:
            case 200:
                logger.info("Operation suceesfully done.");
          
			try {
				String job = response.getEntity().getText();


				while (true)
				{
					
                String status = getTaskStatus(serverURL +"/jobs/"+job);
                System.out.println ("Status " + serverURL +"/jobs/"+job + " " + status);

                if (status.equals("FINISHED")) {
                   return;
                } else if (status.toLowerCase().equals("error")) {
                	
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}
			}
				
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.info("Error to obtain the job.");
			}
 
                break;
                    
                
        }
		
	}
	public void installProduct(Product product, String ip) throws AccessDeniedException,
			CommunicationErrorException {
		
		
	   Reference urlReplica = new Reference(serverURL + 
			   
			   
		URICreationPaaS.getURIVEE((product.getFQN().toString()))+"/pics");
		
		System.out.println ("URL " +urlReplica);

        // Call the server with the URI and the data
		DomRepresentation data;
        try {

        	    Document doc = getInstallProductParams(product,ip);
        	    System.out.println ("Product " + PaasUtils.tooString(doc));
        		data = new DomRepresentation(APPLICATION_OVF_XML, doc);

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
                try {
    				String job = response.getEntity().getText();


    				while (true)
    				{
    					
                    String status = getTaskStatus(serverURL +"/jobs/"+job);

                    if (status.equals("FINISHED")) {
                       return;
                    } else  if (status.equals("PENDING")) 
                    {
                   	 
                    }
                    else if (status.equals("FAILED"))   {
                   	// String error = getTaskMessageError ();
                   	logger.error("Error to create the VM " );
                   	
                   	throw new AccessDeniedException("Error to install the product in the VM: Aborting operation", null, null);
                   }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {}
    			}
    				
    			
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				logger.info("Error to obtain the job.");
    			}
            
		/*	try {
				responseXml = response.getEntity().getText()
				System.out.println (PaasUtils.tooString(responseXml));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

                    
                break;*/
                
                
                    
                
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
	
	public Document getInstallProductInVirtualMachine(VEE vee, String ip, String login, String password) throws ParserConfigurationException, XMLException, SAXException, IOException, SectionAlreadyPresentException, InvalidSectionException
	{
        VirtualSystemType vs = new VirtualSystemType();
        vs.setId(vee.getVEEName()+"-"+ip.replace(".", "-"));
        vee.setVEEName (vee.getVEEName()+"-"+ip.replace(".", "-"));
      //  vs.setId(vee.getVEEName()+"-"+ip.replace(".", "-"));
		ProductSectionType productsection = new ProductSectionType();

		MsgType category  = new MsgType ();
		category.setMsgid("org.fourcaast.instancecomponent");
		category.setValue("Instance Component Metadata");
		productsection.getCategoryOrProperty().add(category);
		
		ProductSectionType.Property property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.instancecomponent.type");
		property.setValue("REC");
		productsection.getCategoryOrProperty().add(property);
		
		if (vee.getRecipes()!= null && vee.getRecipes().size() >0)
		{
		     for (String recipe: vee.getRecipes())
		     {
			   property = new ProductSectionType.Property ();
		       property.setKey("org.fourcaast.instancecomponent.recipe");
		       property.setValue(recipe);
		       productsection.getCategoryOrProperty().add(property);
		     }
		}
		
				  
		category  = new MsgType ();
		category.setMsgid("org.fourcaast.rec");
		category.setValue("REC Attributes");
		productsection.getCategoryOrProperty().add(category);
		
		property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.rec.address");
	
		property.setValue(ip);
		productsection.getCategoryOrProperty().add(property);
		
		property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.rec.username");
		if (vee.getUserName().indexOf("@")!=-1)
			property.setValue(login);

		else
		   property.setValue(vee.getUserName());

		productsection.getCategoryOrProperty().add(property);
		
		property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.rec.password");
		if (vee.getPassword().indexOf("@")!=-1)
			property.setValue(password);

		else
		   property.setValue(vee.getPassword());
	
		productsection.getCategoryOrProperty().add(property);

		OVFEnvelopeUtils.addSection(vs, productsection);
		
		
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
	
	
		String vsstring = ovfSerializer.writeXML(vs);
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(vsstring.getBytes("UTF-8"));
       
       	 
        Document doc = docBuilder.parse (is);

     
		return doc;
		

	}
	
	public Document getEnvelope(String servicename) throws ParserConfigurationException, XMLException, SAXException, IOException, SectionAlreadyPresentException, InvalidSectionException
	{
        EnvelopeType env = new EnvelopeType();
		
        
        AnnotationSectionType annot = new AnnotationSectionType();
        MsgType info = new MsgType ();
        info.setMsgid("org.fourcaast.application.id");
        info.setValue(servicename);
        annot.setInfo(info);
        
        annot.setAnnotation(null);
        
        ReferencesType ref = new ReferencesType();
		
		/*
		<ovfenvelope:Envelope xmlns:cimresallocdata="http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData" xmlns:ovfenvelope="http://schemas.dmtf.org/ovf/envelope/1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		  <ovfenvelope:References/>
		  <ovfenvelope:AnnotationSection ovfenvelope:required="true">
		    <ovfenvelope:Info ovfenvelope:msgid="org.fourcaast.application.id">app-00001</ovfenvelope:Info>
		    <ovfenvelope:Annotation></ovfenvelope:Annotation>
		  </ovfenvelope:AnnotationSection>
		</ovfenvelope:Envelope>*/
		    
		    
			
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		OVFEnvelopeUtils.addSection(env,annot);
	
	
		String vsstring = ovfSerializer.writeXML(env);
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(vsstring.getBytes("UTF-8"));
       
       	 
        Document doc = docBuilder.parse (is);

     
		return doc;
		

	}
	
	private Document getTask(String url) throws AccessDeniedException, CommunicationErrorException {
        // Check the state of the task
        Reference urlTask = new Reference(url);

        System.out.println ("URL " + url);
        Response response = client.get(urlTask);

        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

            case 400:    // Bad Request
            case 404:    // Not found
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 501:
            case 500:
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


            case 202:
                // The VEE has been accepted to be deployed, but the response will be asynchronous.
                // Wait for the response actively.

            case 201:

            case 200:
                // The VirtualMachine has been started without errors. Parse the response and
                // get the task id.
                try {
                    Document responseXml = response.getEntityAsDom().getDocument();
                    return responseXml;
                   

                } catch (IOException e) {
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                } catch (Throwable e) {
                    throw new CommunicationErrorException("Internal error while decoding the answer", e);
                }
        }

        return null;
    }
	
	  private String   getTaskStatus(String url) throws AccessDeniedException, CommunicationErrorException {
	    	
	    	Document responseXml = getTask (url);
	    	
	    	
	    	if (responseXml == null)
	    		return "unknown";
	    	 NodeList tasks = responseXml.getElementsByTagName("status");

	         if (tasks.getLength() != 0 ) {

	             // If the task is Success or Error, store the result.
	             return ((Element)tasks.item(0)).getFirstChild().getNodeValue();

	         } 
	         return "unknown";
	    }
	  
	  private String getTaskMessageError(Document message) throws AccessDeniedException, CommunicationErrorException {


		    NodeList tasks = message.getElementsByTagName("message");

	         if (tasks.getLength() != 0 ) {

	             // If the task is Success or Error, store the result.
	             return ((Element)tasks.item(0)).getFirstChild().getNodeValue();

	         } 
	         return null;
	    }

}
