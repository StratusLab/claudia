/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/

package com.telefonica.claudia.smi.provisioning;

import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_VM_COMMUNICATION;

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
import com.telefonica.claudia.smi.task.TaskManager;

public class PowerResource  extends Resource  {

	String vdcId;
	String vappId;
	String veeId;
	String vmId;
	String orgId;
	String powerAction;
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.vmi");
	
    public PowerResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);  
        
        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.veeId = (String) getRequest().getAttributes().get("vee-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		this.vmId = (String) getRequest().getAttributes().get("vm-id");
		this.powerAction = (String) getRequest().getAttributes().get("power-action");
				
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
     * Determines whether the requested power action is valid or not
     * @return
     */
    private boolean validatePowerAction() {
    	return 
    		powerAction != null &&
    		(
    				powerAction.equals("powerOn") ||
    				powerAction.equals("powerOff") ||
    				powerAction.equals("reset") ||
    				powerAction.equals("suspend") ||
    				powerAction.equals("shutdown") ||
    				powerAction.equals("reboot")
    		);	
    }
    
    /**
     * Handle POST requests: handle power action
     */
    public void acceptRepresentation(Representation entity) throws ResourceException {
    	String fqn = URICreation.getFQN(orgId, vdcId, vappId, veeId, vmId);
    	log.info("POST power " + powerAction + " request recieved for vm: " + fqn);
    	
    	if (!validatePowerAction()) {
    		getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
    		return;
    	}
    	
    	try {
            ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
            long taskId = actualDriver.powerActionVirtualMachine(fqn, powerAction);
            
            StringRepresentation representation = new StringRepresentation(
            		TaskManager.getInstance().getTask(taskId).getStringDescription()
            			.replace("{org-id}", orgId)
            			.replace("{vdc-id}", vdcId)
            			.replace("{vee-id}", veeId)
            			.replace("{vm-id}", vmId), MediaType.APPLICATION_XML);
			getResponse().setEntity(representation);
            getResponse().setStatus(Status.SUCCESS_OK);
            log.info("Request succesful");
		} catch (IOException e) {
			log.error(ERROR_VM_COMMUNICATION + e.getMessage());
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
		}
    }
}
