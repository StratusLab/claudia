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

public class VappMonitorResource extends MonitorResource {

	private static Logger log = Logger.getLogger(VappMonitorResource.class);

	public VappMonitorResource(Context context, Request request,
			Response response) {
		super(context, request, response);
		log.info("VappMonitorResource created");// log

		//this.setUpType(TYPE_VAPP);

		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			log.info("VappMonitorResource.represent");
			MonitoringDriver actualDriver = (MonitoringDriver) getContext()
					.getAttributes().get(
							MonitoringApplication.ATTR_PLUGIN_MONITORING);

			MeasureDescriptorList mdl = null;

			try {
				mdl = actualDriver.getVappMeasureDescriptorList(orgId, vdcId, vappIds);
			} catch (MonitorException e) {
				log.debug(e.getMessage());
				return getUnknownElementErrorRepresentation(e.getMessage());
			}

			log.debug("Data returned for monitor "
					+ URICreation.getFQN(orgId, vdcId, vappIds.get(0))
					+ ": \n\n" + Bean2Xml.toString(mdl));
			
			setMeasureDescriptorListHrefsLinks(mdl, TYPE_VAPP);

			log.info("Data returned for monitor "
					+ URICreation.getFQN(orgId, vdcId, vappIds.get(0))
					+ ": \n\n" + Bean2Xml.toString(mdl));

			return new StringRepresentation(Bean2Xml.toString(mdl),
					MediaType.TEXT_XML);

		}
		return null;
	}
}
