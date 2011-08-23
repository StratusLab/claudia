/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */

package com.telefonica.claudia.smi.templateManagement.resources;

import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_VM_COMMUNICATION;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.smi.ExceptionMessages;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.task.TaskManager;
import com.telefonica.claudia.smi.templateManagement.TemplateManagementApplication;
import com.telefonica.claudia.smi.templateManagement.TemplateManagementDriver;

public class TemplatizeResource  extends Resource  {

	String vdcId;
	String vappId;
	String veeId;
	String vmId;
	String orgId;
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.vmi");
	
    public TemplatizeResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);  
        
        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.veeId = (String) getRequest().getAttributes().get("vee-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		this.vmId = (String) getRequest().getAttributes().get("vm-id");
				
        if (vdcId != null && orgId != null && vappId!=null && vmId != null && veeId != null) {
        	
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
            
        } else {
            // This resource is not available.
            setAvailable(false);
        } 
        
        log.info("Power resource created");
    }
    
   
    /**
     * Handle POST requests: handle Templatize action (Create a template from a VM)
     * 
     */
    public void acceptRepresentation(Representation entity) throws ResourceException {
    	String fqn = URICreation.getFQN(orgId, vdcId, vappId, veeId, vmId);
    	log.info("Create Template request recieved for vm: " + fqn);
    	    	
    	// If the content is XML, process the representation
    	if (entity.getMediaType().equals(MediaType.APPLICATION_XML)) {
    		
    		// Get new TemplateName from the received data.
    		DomRepresentation domR = new DomRepresentation(entity);
    		
			long taskId;
			Document doc;
			
    		try {
    			log.debug("Data recieved [" + domR.getText() + "]"); 
    			
    			String templateName=null;
    			doc = domR.getDocument();
    			
    			NodeList nameNodes = doc.getElementsByTagName("Name");
    			if (nameNodes.getLength()==1){
    				templateName= ((Element)nameNodes.item(0)).getTextContent();
    			}

    			TemplateManagementDriver actualDriver= (TemplateManagementDriver) getContext().getAttributes().get(TemplateManagementApplication.ATTR_PLUGIN_TEMPLATEMANAGEMENT);
                taskId = actualDriver.takeTemplate(fqn, templateName);
            
    		} catch (IOException e) {
    			log.error(ERROR_VM_COMMUNICATION + e.getMessage());
    			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
    			return;
    		}

			// Send the response 
			try {
				DomRepresentation responseXml = new DomRepresentation(MediaType.APPLICATION_XML, TaskManager.getInstance().getTask(taskId).getXmlDescription());
				getResponse().setEntity(responseXml);
				getResponse().setStatus(Status.SUCCESS_OK);
				log.info("Request succesful");
			} catch (IOException e) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				log.info("Error creating the Task document to return");
			}    		
    		
    	} else {
    		getResponse().setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, ExceptionMessages.ERROR_BAD_MEDIA_TYPE);
    	}
    }
}
