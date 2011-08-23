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

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.MonitoringApplication;
import com.telefonica.claudia.smi.monitoring.MonitoringDriver;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.util.Bean2Xml;

public class HWMonitorResource extends MonitorResource {

	private static Logger log = Logger.getLogger(HWMonitorResource.class);

	public HWMonitorResource(Context context, Request request, Response response) {
		super(context, request, response);
		log.info("HWMonitorResource created");// log

		// this.setUpType(TYPE_RASD);

		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			log.info("HWMonitorResource.represent");// log

			MonitoringDriver actualDriver = (MonitoringDriver) getContext()
					.getAttributes().get(
							MonitoringApplication.ATTR_PLUGIN_MONITORING);
			MeasureDescriptorList mdl = null;

			try {
				mdl = actualDriver.getHwItemMeasureDescriptorList(orgId, vdcId,
						vappIds, hwItemId);
			} catch (MonitorException e) {
				log.debug(e.getMessage());
				return getUnknownElementErrorRepresentation(e.getMessage());
			}

			setMeasureDescriptorListHrefsLinks(mdl, TYPE_RASD);
			log.info("Data returned for monitor "
					+ orgId + "/" + vdcId + "/" + vappIds.get(0) + "/" + hwItemId
					+ ": \n\n" + mdl);
			return new StringRepresentation(Bean2Xml.toString(mdl),
					MediaType.TEXT_XML);

		}
		return null;
	}
}
