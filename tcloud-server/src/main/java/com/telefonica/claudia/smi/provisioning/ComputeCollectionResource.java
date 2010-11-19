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
import static com.telefonica.claudia.smi.ExceptionMessages.ERROR_VM_COMMUNICATION;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
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
import org.xml.sax.SAXException;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.OVFEnvironmentUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.ExceptionMessages;
import com.telefonica.claudia.smi.Main;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.task.TaskManager;

/**
 * Represents a collection of computing resources (deployed VMs) as a Restlet Resource.
 * It provides to actions: POST to insert a new VM and GET to get a list of all the
 * deployed computing resources.
 */
public class ComputeCollectionResource  extends Resource {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.vmi");
	
	String vdcId;
	String vappId;
	String veeId;
	String orgId;
	
    public ComputeCollectionResource(Context context, Request request,  
            Response response) {  
    	
        super(context, request, response);
 
        this.vappId = (String) getRequest().getAttributes().get("vapp-id");
        this.veeId = (String) getRequest().getAttributes().get("vee-id");
        this.vdcId = (String) getRequest().getAttributes().get("vdc-id");
		this.orgId = (String) getRequest().getAttributes().get("org-id");
		
        if (vdcId != null && orgId != null && vappId!=null && veeId != null) {
        	
            // Define the supported variant.
            getVariants().add(new Variant(MediaType.TEXT_XML));
            // By default a resource cannot be updated.
            setModifiable(true);
            
        } else {
            // This resource is not available.
            setAvailable(false);
        }  
        
        log.info("Compute collection resource created");
    }
    
    /**
     * POST request: creates a new VM with the received template.
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {
    	
    	log.info("POST request recieved");
    	
    	// If the content is XML, process the representation
    	if (entity.getMediaType().equals(MediaType.APPLICATION_XML)) {
    		
    		// Create the OCCI Representation of a VirtualMachine from the received data.
    		DomRepresentation domR = new DomRepresentation(entity);
    		
			long taskId;
			
    		try {
    			log.debug("Data recieved [" + domR.getText() + "]"); 
    			
    			createOVFEnvironment(domR.getText());
    			
    			// Create the virtual machine itself
    			ProvisioningDriver actualDriver= (ProvisioningDriver) getContext().getAttributes().get(ProvisioningApplication.ATTR_PLUGIN_PROVISIONING);
				taskId = actualDriver.deployVirtualMachine(URICreation.getFQN(orgId, vdcId, vappId, veeId), domR.getText());
				
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
				StringRepresentation representation = new StringRepresentation(TaskManager.getInstance().getTask(taskId).getStringDescription().replace("{org-id}", orgId).replace("{vdc-id}", vdcId), MediaType.TEXT_XML);
				getResponse().setEntity(representation);
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

	private void createOVFEnvironment(String text) {
		
		// Create the OVF Environment file for the Virtual Machine
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
	    DocumentBuilder builder;
	    
	    Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(text.getBytes()));
			
		} catch (ParserConfigurationException e2) {
			e2.printStackTrace();
			return;
		} catch (SAXException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (!doc.getFirstChild().getNodeName().equals(TCloudConstants.TAG_INSTANTIATE_OVF)) {
			log.error("Element <"+TCloudConstants.TAG_INSTANTIATE_OVF+"> not found.");
			return;
		}
		
		// Get the replica's FQN
		Element root = (Element) doc.getFirstChild();
		String name = root.getAttribute("name");
		
		NodeList envelopeItems = doc.getElementsByTagNameNS("*", "Envelope");
		
		if (envelopeItems.getLength() != 1) {
			log.error("Envelope items not found.");
			return;
		}
		
		ContentType entityInstance = null;
		
		EnvelopeType envelope = null;
		Document ovfDoc = builder.newDocument();
		ovfDoc.appendChild(ovfDoc.importNode(envelopeItems.item(0), true));
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		
		try {
			ovfSerializer.setValidateXML(false);
			envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(DataTypesUtils.serializeXML(ovfDoc).getBytes()));
		} catch (JAXBException e1) {
			e1.printStackTrace();
			return;
		} catch (XMLException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
		} catch (EmptyEnvelopeException e) {
			e.printStackTrace();
			return;
		}
		
		if (entityInstance instanceof VirtualSystemCollectionType){
			
		} else if (entityInstance instanceof VirtualSystemType)  {
			
			VirtualSystemType vs = (VirtualSystemType) entityInstance;

			try {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				OVFEnvironmentUtils.createOVFEnvironment(vs,
						0,
						"",
						"",
						"", 
						null,
						null,
						null,
						null,
						null,
						output,
						true);
				
				log.debug("OVF Environment file for VM [" +  output.toString()+ "] ");
				output.toString();
				
				String customizationDirName = Main.PATH_TO_REPOSITORY + "/" + name;
				File customizationDir = new File(customizationDirName);
				customizationDir.mkdirs();

				String customizationFileName = customizationDirName + "/ovf-env.xml";
				File customizationFile = new File(customizationFileName);
				log.info("Creating customization file in " + customizationFile.getPath());

				String customizationDirURLPath = "/" + name;

				PrintWriter out = null;
				try {
					out = new PrintWriter(new FileWriter(customizationFile));
				} catch (IOException ex) {
					log.error("IO Exception trying to create environment file");
					return;
				}
				
				out.write(output.toString() + "\n");
				out.close();

				String urlToCustomFile = "http://" 
							+ customizationDirURLPath + "/ovf-env.xml";

				log.info("URL to customization file: " + urlToCustomFile);
				
			} catch (IPNotFoundException e) {
				e.printStackTrace();
			} catch (DNSServerNotFoundException e) {		
				e.printStackTrace();
			} catch (NetmaskNotFoundException e) {			
				e.printStackTrace();
			} catch (GatewayNotFoundException e) {
				e.printStackTrace();
			} catch (PrecedentTierEntryPointNotFoundException e) {
				e.printStackTrace();
			} catch (NotEnoughIPsInPoolException e) {
				e.printStackTrace();
			} catch (PoolNameNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}    
}
