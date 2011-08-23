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

public class TaskResource extends Resource {

	String orgId;
	String taskId;
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.TaskResource");
	
    public TaskResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);  

		this.orgId = (String) getRequest().getAttributes().get("org-id");
		this.taskId = (String) getRequest().getAttributes().get("task-id");
		
        if (orgId!=null && taskId!=null) {
        	
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
            
        } else {
            // This resource is not available.
            setAvailable(false);
        }  
        
        log.info("Task resource created");
    }
    
    @Override
    public Representation represent(Variant variant) throws ResourceException {
    	
    	log.info("GET request recieved");
    	
    	if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
    		
			try {
				
				if (TaskManager.getInstance().getTask(Long.parseLong(taskId))== null) {
					getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
					return null;
				}
				
				String taskDefinition = TaskManager.getInstance().getTask(Long.parseLong(taskId)).getStringDescription().replace("{org-id}", orgId);
				StringRepresentation representation = new StringRepresentation(taskDefinition, MediaType.TEXT_XML);

	            log.debug("Data representation returned [" +  taskDefinition + "]");
	            getResponse().setStatus(Status.SUCCESS_OK);
	            
	            return representation;
			} catch (IOException e) {
				log.error("Error connecting to the TaskManager: " + e.getMessage());
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			}
    	}
    	
		return null;
    } 
    
    @Override
    public void removeRepresentations() throws ResourceException {
    	
    	log.info("DELETE request recieved on task " + taskId);
    	
		TaskManager.getInstance().removeTask(Long.parseLong(taskId));
		
		getResponse().setStatus(Status.SUCCESS_OK);
    }
}
