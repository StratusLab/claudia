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

package com.telefonica.claudia.smi.deployment;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telefonica.claudia.smi.Main;
import com.telefonica.claudia.smi.URICreation;


public class ServiceItemResource extends Resource  {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.ServiceItemResource");

    String vdcId;
    String vappId;
    String orgId;
    
    
    public ServiceItemResource(Context context, Request request, Response response) {
        super(context, request, response);        
      
        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
        this.orgId = (String) getRequest().getAttributes().get("org-id");

        // Get the item directly from the "persistence layer".
        if (this.orgId != null && this.vdcId!=null && this.vappId!=null) {
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
        } else {
            // This resource is not available.
            setAvailable(false);
        }            
    }

    
    /**
     * Handle GETS
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        // Generate the right representation according to its media type.
        if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
            try {
            	DeploymentDriver actualDriver= (DeploymentDriver) getContext().getAttributes().get(DeploymentApplication.ATTR_PLUGIN_DEPLOYMENT);
            	
            	String serviceInfo = actualDriver.getService(URICreation.getFQN(orgId, vdcId, vappId));
            	
            	// Substitute the macros in the description
            	serviceInfo = serviceInfo.replace("@HOSTNAME", "http://" + Main.serverHost + ":" + Main.serverPort);
            	
            	if (serviceInfo==null) {
            		log.error("Null response from the SM.");
            		getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            		return null;
            	}
            	
                DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(serviceInfo));

                Document doc = db.parse(is);

                DomRepresentation representation = new DomRepresentation(
                        MediaType.TEXT_XML, doc);

                log.info("Data returned for service "+ URICreation.getFQN(orgId, vdcId, vappId) + ": \n\n" + serviceInfo);
                
                // Returns the XML representation of this document.
                return representation;
                
			} catch (IOException e) {
				log.error("Time out waiting for the Lifecycle Controller.");
				
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return null;
			} catch (SAXException e) {
				log.error("Retrieved data was not in XML format: " + e.getMessage());
				
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return null;
			} catch (ParserConfigurationException e) {
				log.error("Error trying to configure parser.");
				
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return null;
			}
        }

        return null;
    }

    /** 
      * Handle DELETE requests. 
      */  
    @Override  
    public void removeRepresentations() throws ResourceException {  
        	
    	DeploymentDriver actualDriver= (DeploymentDriver) getContext().getAttributes().get(DeploymentApplication.ATTR_PLUGIN_DEPLOYMENT);
    	
    	try {
    		actualDriver.undeploy(URICreation.getFQN(orgId, vdcId, vappId));
    		
		} catch (IOException e) {
			log.error("Time out waiting for the Lifecycle Controller.");
			
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return;
			
		}
		
        // Tells the client that the request has been successfully fulfilled.  
        getResponse().setStatus(Status.SUCCESS_NO_CONTENT);  
    }  
}
