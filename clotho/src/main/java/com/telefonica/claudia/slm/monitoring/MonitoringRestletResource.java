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
package com.telefonica.claudia.slm.monitoring;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.slm.monitoring.MonitoringDataToken.MonitorDataType;
import com.telefonica.claudia.slm.naming.FQN;


public class MonitoringRestletResource extends Resource {
    
    private static Logger logger = Logger.getLogger(MonitoringRestletResource.class);
    
    static {
    	logger.setLevel(Level.INFO);
    }
    
    public static final String ROOT_MONITORING_TAG_NAME = "MonitoringInformation";
    public static final String EVENT_TYPE_TAG_NAME = "EventType";
    public static final String T_0_TAG_NAME = "EpochTimestamp";
    public static final String T_DELTA_TAG_NAME = "TimeDelta";
    public static final String FQN_TAG_NAME = "FQN";
    public static final String VALUE_TAG_NAME = "Value";
    
    public MonitoringRestletResource(Context parentContext, Request request, Response response) {
        super(parentContext, request, response);
    }
    
    @Override
    public boolean allowPost() {
        return true;
    }
    
    @Override
    public void post(Representation entity){
    	
    	logger.debug("Attending POST petition on monitoring Rest server");
    	
    	DomRepresentation dom = new DomRepresentation(entity);
    	
		Document xmlDoc = null;
    	try {
			xmlDoc = dom.getDocument();
		} catch (IOException ex) {
			logger.error("IOException caught when trying to parse monitoring message", ex);
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Could not parse XML doc containing monitoring data", MediaType.TEXT_PLAIN));
			return;
		}
		    	
		Element rootElement = xmlDoc.getDocumentElement();
		
		if(rootElement.getNodeName() != ROOT_MONITORING_TAG_NAME) {
			logger.error("Root element of document with monitoring data is not " + ROOT_MONITORING_TAG_NAME + ", is " + rootElement.getLocalName());
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Root element of document with monitoring data is not " + ROOT_MONITORING_TAG_NAME + ", is " + rootElement.getLocalName(), MediaType.TEXT_PLAIN));
			return;			
		}
		
		// Reading event type.
		NodeList eventTypeList = rootElement.getElementsByTagName(EVENT_TYPE_TAG_NAME);
		if(eventTypeList.getLength() != 1) {
			logger.error("There must be one (and only one) " + EVENT_TYPE_TAG_NAME + " tag");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("There must be one (and only one) " + EVENT_TYPE_TAG_NAME + " tag", MediaType.TEXT_PLAIN));
			return;					
		}
		
		MonitorDataType monitorDataType = null;
		String monitorDataTypeString = eventTypeList.item(0).getTextContent();
		if(monitorDataTypeString.equalsIgnoreCase(MonitorDataType.AGENT.toString())){
			monitorDataType = MonitorDataType.AGENT;
		} else if(monitorDataTypeString.equalsIgnoreCase(MonitorDataType.PROBE.toString())){
			monitorDataType = MonitorDataType.PROBE;
		} else if(monitorDataTypeString.equalsIgnoreCase(MonitorDataType.VEEHW.toString())){
			monitorDataType = MonitorDataType.VEEHW;
		} else {
			logger.error("Unknown event type: " + monitorDataTypeString);
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Unknown event type: " + monitorDataTypeString, MediaType.TEXT_PLAIN));
            return;
		}
		
		// Reading t_0
		long t_0 = -1;
		eventTypeList = rootElement.getElementsByTagName(T_0_TAG_NAME);
		if(eventTypeList.getLength() != 1) {
			logger.error("There must be one (and only one) " + T_0_TAG_NAME + " tag");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("There must be one (and only one) " + T_0_TAG_NAME + " tag", MediaType.TEXT_PLAIN));
			return;					
		}
		
		try{
			t_0 = Long.parseLong(eventTypeList.item(0).getTextContent());
		} catch(NumberFormatException ex) {
			logger.error("Could not parse t_0 " + eventTypeList.item(0).getTextContent() + " to a valid long value", ex);
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Could not parse t_0 " + eventTypeList.item(0).getTextContent() + " to a valid long value", MediaType.TEXT_PLAIN));
            return;
		}
		if (t_0 < 0) {
			logger.error("t_0 " + t_0 + " cannot have a negative value");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("t_0 " + t_0 + " cannot have a negative value", MediaType.TEXT_PLAIN));
            return;			
		}
		
		// Reading delta_t
		long delta_t = -1;
		eventTypeList = rootElement.getElementsByTagName(T_DELTA_TAG_NAME);
		if(eventTypeList.getLength() != 1) {
			logger.error("There must be one (and only one) " + T_DELTA_TAG_NAME + " tag");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("There must be one (and only one) " + T_DELTA_TAG_NAME + " tag", MediaType.TEXT_PLAIN));
			return;					
		}
		
		try {
			delta_t = Long.parseLong(eventTypeList.item(0).getTextContent());
		} catch (NumberFormatException ex) {
			logger.error("Could not parse delta_t " + eventTypeList.item(0).getTextContent() + " to a valid long value", ex);
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Could not parse delta_t " + eventTypeList.item(0).getTextContent() + " to a valid long value", MediaType.TEXT_PLAIN));
            return;			
		}
		if(delta_t < 0) {
			logger.error("delta_t " + delta_t + " cannot have a negative value");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("t_0 " + t_0 + " cannot have a negative value", MediaType.TEXT_PLAIN));
            return;
		}
		
		// Reading FQN
		FQN fqn = null;
		eventTypeList = rootElement.getElementsByTagName(FQN_TAG_NAME);
		if(eventTypeList.getLength() != 1) {
			logger.error("There must be one (and only one) " + FQN_TAG_NAME + " tag");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("There must be one (and only one) " + FQN_TAG_NAME + " tag", MediaType.TEXT_PLAIN));
			return;					
		}
		fqn = new FQN(eventTypeList.item(0).getTextContent());
		
		// Reading value
		double value = 0.0;
		eventTypeList = rootElement.getElementsByTagName(VALUE_TAG_NAME);
		if(eventTypeList.getLength() != 1) {
			logger.error("There must be one (and only one) " + VALUE_TAG_NAME + " tag");
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("There must be one (and only one) " + VALUE_TAG_NAME + " tag", MediaType.TEXT_PLAIN));
			return;								
		}
		
		try {
			value = Double.parseDouble(eventTypeList.item(0).getTextContent());
		} catch (NumberFormatException ex) {
			logger.error("Could not parse " + VALUE_TAG_NAME + " tag content " + eventTypeList.item(0).getTextContent() + " to a valid double value", ex);
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(new StringRepresentation("Could not parse " + VALUE_TAG_NAME + " tag content " + eventTypeList.item(0).getTextContent() + " to a valid double value", MediaType.TEXT_PLAIN));
            return;					
		}
		
		// Building monitoring token
		logger.debug("Received event " + monitorDataType + ", t_0:" + t_0 + ", delta_t:" + delta_t + " FQN:" + fqn + ", value:" + value);
		MonitoringDataToken monitoringData = new MonitoringDataToken(monitorDataType, t_0, delta_t, fqn, value);
				
		((MonitoringBusForwarder)getContext().getAttributes().get(MonitoringRestBusConnector.SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS)).forwardMonitoringData(monitoringData);
    }

}
