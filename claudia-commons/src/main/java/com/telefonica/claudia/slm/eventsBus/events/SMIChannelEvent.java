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
package com.telefonica.claudia.slm.eventsBus.events;

import java.io.Serializable;
import java.util.HashMap;


public class SMIChannelEvent extends Event implements Serializable {

	private static final long serialVersionUID = 8133331375710757334L;

    private HashMap<String, String> parameters = new HashMap<String, String>();
    
    /**
     * Kinds of actions the SMI can request the SM to fullfill:
     * 
     * - Deploy (OVFDocument, CustomerName): deploy the service described in the OVF Document passed
     *   as a parameter, as a Service of the CustomerName.
     * 
     * - Undeploy (FQN_ID): undeploy the service indicated with the FQN.
     *   
     * @author daniel
     *
     */
    public enum SMIAction {
    	DEPLOY, 
    	UNDEPLOY,
    	GET_VAPP,
    	GET_VDC,
    	GET_VEE,
    	GET_ORG
    }
    
    public static final String OVF_DOCUMENT= "OvfDocument";
    public static final String CUSTOMER_NAME= "CustomerName";
    public static final String SERVICE_NAME = "ServiceName";
    public static final String SERVICE_DESCRIPTION = "ServiceDescription";
    public static final String VEE_DESCRIPTION = "VeeDescription";
    public static final String CUSTOMER_DESCRIPTION = "CustomerDescription";
    public static final String ORG_DESCRIPTION = "OrgDescription";
    public static final String FQN_ID = "fqnId";
    
    private SMIAction action;
    
    private boolean success=false;
    
    private String message;
	
	public SMIChannelEvent(long t_0, long deltaT, SMIAction action) {
		super(t_0, deltaT, EventType.SMI_CHANNEL_EVENT);
		
		this.action = action;
	}
    
	/**
	 * Each SMIAction will have a variable number of parameters. Those parameters will be
	 * encoded in an internal map, that the LCC will access in order to fullfill the request.
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void put(String parameterName, String value) {
		parameters.put(parameterName, value);
	}
	
	public String get(String parameterName) {
		return parameters.get(parameterName);
	}
	
    public SMIAction getAction() {
    	return action;
    }
	
    @Override
    public String toString() {
        return super.toString();
    }

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
