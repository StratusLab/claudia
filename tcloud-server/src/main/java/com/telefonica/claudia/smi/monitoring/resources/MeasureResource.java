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
package com.telefonica.claudia.smi.monitoring.resources;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
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
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;
import com.telefonica.claudia.smi.monitoring.bean.error.ErrorSet;
import com.telefonica.claudia.smi.monitoring.bean.error.UnknownElementsError;
import com.telefonica.claudia.smi.util.Bean2Xml;
import com.telefonica.claudia.smi.util.Util;

public class MeasureResource extends BasicResource {

	private static Logger log = Logger.getLogger(MeasureResource.class);

	private int samples = 1;

	public MeasureResource(Context context, Request request, Response response) {
		super(context, request, response);
		log.info("MeasureResource created");// log

		Reference resourceRef = request.getResourceRef();
		Form form = resourceRef.getQueryAsForm();
		if (form.getNames().size() > 0) {
			String s = form.getFirstValue("samples");
			if (Util.isNumber(s))
				samples = Integer.parseInt(s);
		}

		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			log.info("MeasureResource.represent");// log
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

			MeasuredValueList mvl = null;

			try {
				mvl = actualDriver.getMeasuredValueList(md, samples);
			} catch (MonitorException e) {
				log.debug(e.getMessage());
				es
						.add(new UnknownElementsError(e.getMessage(),
								getIdentifier()));
				return new StringRepresentation(Bean2Xml.toString(es),
						MediaType.TEXT_XML);
			}

			mvl.setHrefsLinks(getIdentifier(), TYPE_MONI);

			return new StringRepresentation(Bean2Xml.toString(mvl),
					MediaType.TEXT_XML);
		}
		return null;
	}
}
