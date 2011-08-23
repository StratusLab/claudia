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

import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_UNEXPECTED;
import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_VIRTUALIZATION_MANAGER;
import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_VM_COMMUNICATION;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.smi.ExceptionMessages;
import com.telefonica.claudia.smi.task.TaskManager;

public class NetworkCollectionResource extends Resource {

	String vdcId;
	String vappId;
	String orgId;
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.provisioning.NetworkCollectionResource");
	
    public NetworkCollectionResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);
 
        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		
        if (vdcId != null && orgId != null) {
        	
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
            
        } else {
            // This resource is not available.
            setAvailable(false);
        } 
        
        log.info("Network collection resource created");
    }
    
    /**
     * POST request: creates a new network with the received template.
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {
    	
    	log.info("POST request recieved");
    	
    	// If the content is XML, process the representation
    	if (entity.getMediaType().equals(MediaType.APPLICATION_XML)) {
    		
    		// Create the VEEReplica from the received data.
    		DomRepresentation domR = new DomRepresentation(entity);
    		
			long taskId;
			Document doc;
			
    		try {
    			log.debug("Data recieved [" + domR.getText() + "]"); 
    			
    			String networkName=null;
    			doc = domR.getDocument();
    			
    			NodeList networkNodes = doc.getElementsByTagName("Network");
    			for (int i=0; i < networkNodes.getLength(); i++) {
    				networkName= ((Element)networkNodes.item(i)).getAttribute("name");
    			}
    			
    			StringWriter sw = new StringWriter();
				DOMSource domSource = new DOMSource(doc);
				StreamResult streamResult = new StreamResult(sw);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer serializer;
				try {
					serializer = tf.newTransformer();
					serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
					serializer.setOutputProperty(OutputKeys.INDENT,"yes");
					
					serializer.transform(domSource, streamResult);
					
				} catch (TransformerConfigurationException e) {
					log.error("Error transforming XML content. The OVF document may be malformed: " + e.getMessage());
					getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
					return;
				} catch (TransformerException e) {
					log.error("Error transforming XML content. The OVF document may be malformed." + e.getMessage());
					getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
					return;
				}
				
				String ovf =sw.toString();
    			
    			ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
    			
				taskId = actualDriver.deployNetwork(networkName, ovf);
				
			} catch (IOException e) {
				log.error(ERROR_VM_COMMUNICATION + e.getMessage());
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e);
				return;
			} catch (Exception e) {
				log.error(ERROR_UNEXPECTED + e.getMessage());
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
    
    /**
     * GET request: provides a list of all the available networks, in OVF format.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
    	
    	log.info("GET request recieved");
    	
    	if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
    		
    		try {
                // Get the VM ids from the Virtualization provider
                ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
                
                String netList = actualDriver.getNetworkList();
                
                StringRepresentation representation = new StringRepresentation(netList, MediaType.TEXT_XML);
                
                getResponse().setStatus(Status.SUCCESS_OK);
                
                log.debug("Data returned [" + representation.getText() + "]");
                log.info("Request successfull");
                return representation;
				
			} catch (IOException ioe) {
            	log.error(ERROR_VIRTUALIZATION_MANAGER + ioe.getMessage());
            	getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ioe);
			}
    	}
    	
    	return null;
    }    
}
