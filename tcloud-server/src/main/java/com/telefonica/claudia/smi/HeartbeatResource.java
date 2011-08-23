package com.telefonica.claudia.smi;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class HeartbeatResource extends Resource  {

    public HeartbeatResource(Context context, Request request, Response response) {
        super(context, request, response);        
      
        getVariants().add(new Variant(MediaType.TEXT_XML));
        setModifiable(true);    
    }
    
    @Override
    public Representation represent(Variant variant) throws ResourceException {
    	
    	StringRepresentation representation = new StringRepresentation("ACK");
    	
    	return representation;
    }
}
