/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */

package com.telefonica.claudia.smi.console;

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

public class TicketResource extends Resource  {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.TicketResource");
    
    public TicketResource(Context context, Request request, Response response) {
        super(context, request, response);
        
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        getVariants().add(new Variant(MediaType.TEXT_XML));
        
        setModifiable(true);
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {
        try {
        	ConsoleDriver actualDriver = (ConsoleDriver) getContext().getAttributes().get(ConsoleApplication.ATTR_PLUGIN_CONSOLE);
        	String ticket = actualDriver.getTicket();

            log.info("Ticket returned " + ticket);
            
            // Returns the XML representation of this document.
            return new StringRepresentation(ticket, MediaType.TEXT_XML);
            
		} catch (IOException e) {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return null;
		}
    }
}
