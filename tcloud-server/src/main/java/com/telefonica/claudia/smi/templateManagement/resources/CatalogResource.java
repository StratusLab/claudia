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
import com.telefonica.claudia.smi.templateManagement.TemplateManagementApplication;
import com.telefonica.claudia.smi.templateManagement.TemplateManagementDriver;

public class CatalogResource extends Resource  {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.CatalogResource");

    public CatalogResource(Context context, Request request, Response response) {
        super(context, request, response);        
      
        // Define the supported variant.
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        getVariants().add(new Variant(MediaType.TEXT_XML));
        // By default a resource cannot be updated.
        setModifiable(true);
    }
    
    /**
     * Handle GETS
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        // Generate the right representation according to its media type.
        try {
        	TemplateManagementDriver actualDriver= (TemplateManagementDriver) getContext().getAttributes().get(TemplateManagementApplication.ATTR_PLUGIN_TEMPLATEMANAGEMENT);
        	
        	String catalogInfo = actualDriver.getCatalog();
        	
        	if (catalogInfo==null) {
    			log.error("Error getCatalog().");			
    			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);        		
        		return null;
        	}
        	
        	// Substitute the macros in the description
        	catalogInfo = catalogInfo.replace("@HOSTNAME", "http://" + Main.serverHost + ":" + Main.serverPort);
        	
            DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(catalogInfo));

            Document doc = db.parse(is);

            DomRepresentation representation = new DomRepresentation(
                    MediaType.TEXT_XML, doc);

            log.info("Data returned for catalog : \n\n" + catalogInfo);
            
            // Returns the XML representation of this document.
            return representation;
            
		} catch (IOException e) {
			log.error("Error getCatalog().");			
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
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
}
