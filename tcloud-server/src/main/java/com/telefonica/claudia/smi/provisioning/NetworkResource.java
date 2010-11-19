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

public class NetworkResource extends Resource {

	String vdcId;
	String vappId;
	String orgId;
	String netId;
	
	private static Logger log = Logger.getLogger("es.tid.reservoir.serviceManager.OCCI");
	
    public NetworkResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);  

        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		this.netId = (String) getRequest().getAttributes().get("net-id");
		
        if (vdcId != null && orgId != null && vappId!=null && netId != null) {
        	
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
    
    /**
     * GET request: returns a XML representation of the actual state of the 
     * selected network resource. The XML has the following format (example):
     * 
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
    	
    	log.info("GET request recieved");
    	
    	if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
    		
			try {
				ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
				actualDriver.getNetwork(URICreation.getNetworkFQN(orgId, vdcId, vappId, netId));
				
				String networkDefinition = actualDriver.getNetwork(URICreation.getNetworkFQN(orgId, vdcId, vappId, netId));
				StringRepresentation representation = new StringRepresentation(networkDefinition, MediaType.TEXT_XML);

	            log.debug("Data representation returned [" +  networkDefinition + "]");
	            getResponse().setStatus(Status.SUCCESS_OK);
	            
	            return representation;
			} catch (IOException e) {
				log.error(ERROR_VM_COMMUNICATION + e.getMessage());
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
			}
    	}
    	
		return null;
    } 
    
    /**
     * DELETE request: remove the selected network.
     */
    @Override
    public void removeRepresentations() throws ResourceException {
    	
    	log.info("DELETE request recieved on network " + netId);
    	
		try {
			ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
            actualDriver.deleteNetwork(URICreation.getNetworkFQN(orgId, vdcId, vappId, netId));
            getResponse().setStatus(Status.SUCCESS_OK);
            
		} catch (IOException e) {
			log.error(ERROR_VM_COMMUNICATION + e.getMessage());
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
		}
    }
}
