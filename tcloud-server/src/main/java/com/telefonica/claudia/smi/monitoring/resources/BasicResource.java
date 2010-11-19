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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;

import com.telefonica.claudia.smi.monitoring.bean.error.ErrorSet;
import com.telefonica.claudia.smi.monitoring.bean.error.UnknownElementsError;
import com.telefonica.claudia.smi.util.Bean2Xml;
import com.telefonica.claudia.smi.util.Util;


public abstract class BasicResource extends Resource{
	
	private static Logger log = Logger.getLogger(BasicResource.class);
	
	public static final String TYPE_RASD = "application/vnd.dmtf.cim.rasdType+xml";
	public static final String TYPE_VAPP = "application/vnd.telefonica.tcloud.vapp+xml";
	public static final String TYPE_NET = "application/vnd.telefonica.tcloud.network+xml";
	public static final String TYPE_VDC = "application/vnd.telefonica.tcloud.vdc+xml";
	public static final String TYPE_MONI = "application/vnd.telefonica.tcloud.monitoringCallback+plain";
	
	//FIXME
	protected static final String VDC_ELEMENT_TYPE 			= "1";
	protected static final String SERVICE_ELEMENT_TYPE 		= "2";
	protected static final String VEE_ELEMENT_TYPE			= "3";
	protected static final String VEEREPLICA_ELEMENT_TYPE 	= "4";
	protected static final String HWITEM_ELEMENT_TYPE		= "5";
	protected static final String NET_ELEMENT_TYPE			= "6";

	protected String orgId = null;
	protected String vdcId = null;
	protected ArrayList<String> vappIds = null;
	protected String hwItemId  = null;
	protected String netId = null;
	protected String measureId = null;
	
	private String identifier = null;
	private String upType = null;

	
	public BasicResource(Context context, Request request, Response response) {
        super(context, request, response);
        
        orgId = (String) request.getAttributes().get("org-id");
        vdcId = (String) request.getAttributes().get("vdc-id");
        String vappId = (String) request.getAttributes().get("vapp-id");
        String vapp2Id = (String) request.getAttributes().get("vee-id");
        String vapp3Id = (String) request.getAttributes().get("vm-id");
        
		vappIds = new ArrayList<String>();
		if (vappId != null) vappIds.add(vappId);
		if (vapp2Id != null) vappIds.add(vapp2Id);
		if (vapp3Id != null) vappIds.add(vapp3Id);
	    hwItemId = (String) request.getAttributes().get("hwitem-id");
	    netId = (String) request.getAttributes().get("net-id");
	    measureId = (String) request.getAttributes().get("measure-id");
	    
	    setIdentifier(request.getResourceRef().getIdentifier());
		
	    log.info("===BasicResource");
	    log.info(" org-id:" + request.getAttributes().get("org-id"));
	    log.info(" vdc-id:" + vdcId);
	    log.info(" vapp-id:" + vappId);
	    log.info(" vapp2-id:" + vapp2Id);
	    log.info(" vapp3-id:" + vapp3Id);
	    log.info(" hwitem-id:" + hwItemId);
	    log.info(" net-id:" + netId);
	    log.info(" measure-id:" + measureId);
	    log.info("===");	    
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setUpType(String upType) {
		this.upType = upType;
	}

	public String getUpType() {
		return upType;
	}
	
	protected String getElemetType() {
		String elementType = "";
		if (netId != null)				elementType = NET_ELEMENT_TYPE;
		else if (hwItemId != null)		elementType = HWITEM_ELEMENT_TYPE;
		else if (vappIds.size() == 3)	elementType = VEEREPLICA_ELEMENT_TYPE;
		else if (vappIds.size() == 2)	elementType = VEE_ELEMENT_TYPE;
		else if (vappIds.size() == 1)	elementType = SERVICE_ELEMENT_TYPE;
		else if (vdcId != null)			elementType = VDC_ELEMENT_TYPE;
		return elementType;
	}

	public Representation getUnknownElementErrorRepresentation(String elementType) {
		getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		ErrorSet es = new ErrorSet();
		es.add(new UnknownElementsError(elementType, Util.getUpHref(getIdentifier())));
		return new StringRepresentation(Bean2Xml.toString(es), MediaType.TEXT_XML); 
	}
	
}
