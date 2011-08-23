/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.monitoring.resources;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.telefonica.claudia.smi.monitoring.bean.Link;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.util.Util;

public abstract class MonitorResource extends BasicResource {

	public MonitorResource(Context context, Request request, Response response) {
        super(context, request, response);
	}
	
	protected void setMeasureDescriptorListHrefsLinks(MeasureDescriptorList mdl, String type){
		mdl.setHref(getIdentifier());
		mdl.setMeasureDescriptorsHrefs(getIdentifier());
		Link link = new Link();
		link.setRel("up");
		link.setType(type);
		link.setHref(Util.getUpHref(getIdentifier()));
		mdl.setLink(link);
	}
}
