/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.task;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.telefonica.claudia.smi.URICreation;

public class TaskCollectionResource extends Resource {
	
	String orgId;
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.TaskResource");
	
    public TaskCollectionResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);  
        
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		
        if (orgId!=null) {
        	
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
            
        } else {
            // This resource is not available.
            setAvailable(false);
        }  
        
        log.info("Network resource created");
    }
    
    @Override
    public Representation represent(Variant variant) throws ResourceException {
    	
    	log.info("GET request recieved");
    	
    	if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
    		
			try {
				String taskList = TaskManager.getInstance().taskList(URICreation.getFQN(orgId)).replace("{org-id}", orgId);
				StringRepresentation representation = new StringRepresentation(taskList, MediaType.TEXT_XML);

	            log.debug("Data representation returned [" +  taskList + "]");
	            getResponse().setStatus(Status.SUCCESS_OK);
	            
	            return representation;
			} catch (IOException e) {
				log.error("Error connecting to the TaskManager: " + e.getMessage());
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			}
    	}
    	
		return null;
    }
}
