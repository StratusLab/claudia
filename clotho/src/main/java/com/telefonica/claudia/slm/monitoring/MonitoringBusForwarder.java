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

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.eventsBus.BusMediator;
import com.telefonica.claudia.slm.eventsBus.events.AgentMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.ProbeMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.VeeHwMeasureEvent;
import com.telefonica.claudia.slm.monitoring.MonitoringDataToken.MonitorDataType;


public class MonitoringBusForwarder {
    
    private static Logger logger = Logger.getLogger(MonitoringBusForwarder.class);
    
    static {
    	logger.setLevel(Level.INFO);
    }
    
    private BusMediator busMediator = null;
	
	public MonitoringBusForwarder() throws JMSException, NamingException {
        
        busMediator = new BusMediator();
        
        busMediator.openJMSSession();
		
	}
	
	public void forwardMonitoringData(MonitoringDataToken monitoringData){
		
		try {
			if(monitoringData.getDataType() == MonitorDataType.VEEHW) {
				logger.debug("Forwarding VEE Hw monitoring data to bus");
				VeeHwMeasureEvent veeHwEvent = new VeeHwMeasureEvent(monitoringData.getT0(), monitoringData.getDeltaT(), monitoringData.getFQN().toString(), monitoringData.getValue());
				busMediator.sendEvent(veeHwEvent);
			} else if(monitoringData.getDataType() == MonitorDataType.AGENT) {
				logger.debug("Forwarding agent monitoring data to bus");
				AgentMeasureEvent agentEvent = new AgentMeasureEvent(monitoringData.getT0(), monitoringData.getDeltaT(), monitoringData.getFQN().toString(), monitoringData.getValue());
				busMediator.sendEvent(agentEvent);
			} else {
				logger.debug("Forwarding probe monitoring data to bus");
				ProbeMeasureEvent probeEvent = new ProbeMeasureEvent(monitoringData.getT0(), monitoringData.getDeltaT(), monitoringData.getFQN().toString(), monitoringData.getValue());
				busMediator.sendEvent(probeEvent);
			}
		} catch (JMSException ex) {
			logger.error("JMSException caught when trying to forward monitoring data to bus", ex);
			//throw ex;
		}
		
	}
	
	public void close() {
		busMediator.closeJMSSession();
	}
    
    @Override
    protected void finalize() {
    	busMediator.closeJMSSession();        
    }

}
