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

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.MonitoringApplication;
import com.telefonica.claudia.smi.monitoring.MonitoringDriver;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.error.ErrorSet;
import com.telefonica.claudia.smi.monitoring.bean.error.UnknownElementsError;

import com.telefonica.claudia.smi.util.Bean2Xml;
import com.telefonica.claudia.smi.util.Util;

public class MeasureDescriptorResource extends BasicResource {

	private static Logger log = Logger
			.getLogger(MeasureDescriptorResource.class);

	public MeasureDescriptorResource(Context context, Request request,
			Response response) {
		super(context, request, response);
		log.info("MeasureDescriptorResource created");// log

		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			log.info("MeasureDescriptorResource.represent");
			ErrorSet es = new ErrorSet();
			MonitoringDriver actualDriver = (MonitoringDriver) getContext()
					.getAttributes().get(
							MonitoringApplication.ATTR_PLUGIN_MONITORING);

			MeasureDescriptor md = null;

			try {
				String et = getElemetType();
				if (et.equals(VDC_ELEMENT_TYPE)) {
					md = actualDriver.getVdcMeasureDescriptor(orgId, vdcId,
							measureId);
					log.info("Data returned for vdc monitor " + orgId + "/"
							+ vdcId + "/" + measureId + ": \n\n" + md);
				} else if (et.equals(SERVICE_ELEMENT_TYPE)
						|| et.equals(VEE_ELEMENT_TYPE)
						|| et.equals(VEEREPLICA_ELEMENT_TYPE)) {
					md = actualDriver.getVappMeasureDescriptor(orgId, vdcId,
							vappIds, measureId);
					log.info("size: " + vappIds.size());
					log.info("Data returned for vapp monitor "
							+ orgId
							+ "/"
							+ vdcId
							+ "/"
							+ (vappIds.size() < 1 ? ""
									: (vappIds.get(0) + "/"))
							+ ((vappIds.size() < 2) ? ""
									: (vappIds.get(1) + "/"))
							+ (vappIds.size() < 3 ? ""
									: (vappIds.get(2) + "/")) + measureId
							+ ": \n\n" + md);
				} else if (et.equals(NET_ELEMENT_TYPE)) {
					md = actualDriver.getNetMeasureDescriptor(orgId, vdcId,
							netId, measureId);
					log.info("Data returned for net monitor "
							+ orgId
							+ "/"
							+ vdcId
							+ "/"
							+ (vappIds.size() < 1 ? ""
									: (vappIds.get(0) + "/"))
							+ ((vappIds.size() < 2) ? ""
									: (vappIds.get(1) + "/"))
							+ (vappIds.size() < 3 ? ""
									: (vappIds.get(2) + "/")) + netId + "/"
							+ measureId + ": \n\n" + md);
				} else if (et.equals(HWITEM_ELEMENT_TYPE)) {
					md = actualDriver.getHwItemMeasureDescriptor(orgId, vdcId,
							vappIds, hwItemId, measureId);
					log.info("Data returned for hw monitor "
							+ orgId
							+ "/"
							+ vdcId
							+ "/"
							+ (vappIds.size() < 1 ? ""
									: (vappIds.get(0) + "/"))
							+ ((vappIds.size() < 2) ? ""
									: (vappIds.get(1) + "/"))
							+ (vappIds.size() < 3 ? ""
									: (vappIds.get(2) + "/")) + hwItemId + "/"
							+ measureId + ": \n\n" + md);
				}

			} catch (MonitorException e) {
				log.error(e.getMessage());
				es.add(new UnknownElementsError(e.getMessage(), Util
						.getUpHref(Util.getUpHref(getIdentifier()))));
				return new StringRepresentation(Bean2Xml.toString(es),
						MediaType.TEXT_XML);
			}

			md.setHrefs(Util.getUpHref(getIdentifier()));
            String xml = null;
			try {
				xml = md.getString();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new StringRepresentation(xml,
					MediaType.TEXT_XML);
		}
		return null;

	}

}
