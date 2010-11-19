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
package com.telefonica.claudia.slm.deployment;

import java.io.File;
import java.util.Iterator;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.deployment.hwItems.CPU;
import com.telefonica.claudia.slm.deployment.hwItems.Disk;
import com.telefonica.claudia.slm.eventsBus.BusListener;
import com.telefonica.claudia.slm.eventsBus.BusMediator;
import com.telefonica.claudia.slm.eventsBus.events.AgentMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.Event;
import com.telefonica.claudia.slm.eventsBus.events.ProbeMeasureEvent;
import com.telefonica.claudia.slm.eventsBus.events.VeeHwMeasureEvent;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;


public class ModelUpdater implements BusListener {

	private static Logger logger = Logger.getLogger(ModelUpdater.class);
	
	private static final String CPU_USAGE_NAME = "cpuUsage";
	private static final String MEM_INFO_NAME = "memInfo";
	private static final String DISK_INFO_NAME = "diskInfo";
    private static final String SERVICE_KPIS_NAME_SPACE = "kpis";
	
	private BusMediator busMediator = null;
	private boolean listening = false;
	
	public void startListeningBus() throws NamingException, JMSException {
		if(listening) {
			logger.error("Already listening on bus for events, ignoring call");
			return;
		}			
		logger.info("Starting to listen to bus events to update deployment models");
    	busMediator = new BusMediator();
    	busMediator.openJMSSession();
		busMediator.registerEventsReceiver(this, Event.EventType.AGENT_MEASUREMENT);
		busMediator.registerEventsReceiver(this, Event.EventType.PROBE_MEASUREMENT);
		busMediator.registerEventsReceiver(this, Event.EventType.VEE_HW_MEASUREMENT);
		listening = true;
	}
	
	public void stopListeningBus() {		
		busMediator.unregisterEventsReceiver(this);
		busMediator.closeJMSSession();
		listening = false;
	}

	public void onEvent(Event event) {		
		switch(event.getEventType()) {
			case VEE_HW_MEASUREMENT:
				VeeHwMeasureEvent veeHwEVent = (VeeHwMeasureEvent)event;
                logger.info("VeeHwMeasureEvent arrived: " + veeHwEVent.toString());
                updateReplicaHwState(veeHwEVent.getMeasure(), veeHwEVent.getValue());
                break;
			case PROBE_MEASUREMENT:
                ProbeMeasureEvent probeEvent = (ProbeMeasureEvent)event;
                logger.info("ProbeMeasureEvent arrived: " + probeEvent.toString());
                updateServiceKPI(probeEvent.getMeasure(), probeEvent.getValue());
                break;
			case AGENT_MEASUREMENT:
				AgentMeasureEvent agentEvent = (AgentMeasureEvent)event;
                logger.info("AgentMeasureEvent arrived: " + event.toString());
                updateServiceKPI(agentEvent.getMeasure(), agentEvent.getValue());
                break;
		}
	}
	
	private void updateReplicaHwState(String dataFQN, double value) {
		String[] fqnTokens = dataFQN.split(".");
		if(fqnTokens.length <=2) {
			logger.error("Bad FQN of Vee Hw data: " + dataFQN);
			return;
		}
		
		FQN veeReplicaFQN = null;
		if(fqnTokens[fqnTokens.length-1].equals(CPU_USAGE_NAME) || fqnTokens[fqnTokens.length-1].equals(MEM_INFO_NAME)) {
			veeReplicaFQN = new FQN(dataFQN.substring(0, dataFQN.lastIndexOf(".")));			
		} else if(fqnTokens[fqnTokens.length-2].equals(DISK_INFO_NAME)) { 
			String tmp = dataFQN.substring(0, dataFQN.lastIndexOf("."));
			veeReplicaFQN = new FQN(tmp.substring(0, tmp.lastIndexOf(".")));
		} else { 
			logger.error("Bad FQN of Vee Hw data: " + dataFQN);
			return;
		}
		
		Object registeredObject = ReservoirDirectory.getInstance().getObject(veeReplicaFQN);		
		if(registeredObject == null) {
			logger.error("Nothing registered on directory with FQN " + veeReplicaFQN);
			return;
		}
		if(!(registeredObject instanceof VEEReplica)) {
			logger.error("Object registered with FQN " + veeReplicaFQN + " is not a VEE Replica");
			return;
		}
		VEEReplica replica = (VEEReplica) registeredObject;

		if(fqnTokens[fqnTokens.length-1].equals(CPU_USAGE_NAME)) {
			Iterator<CPU> cpusIter = replica.getCPUs().iterator();
			if(!cpusIter.hasNext()) {
				logger.error("No CPUs registered for VEE replica " + replica);
				return;
			}
			logger.info("Setting CPU usage of replica " + replica + " to " + (float)value);
			replica.getCPUs().iterator().next().setUsage((float)value);			
		} else if(fqnTokens[fqnTokens.length-1].equals(MEM_INFO_NAME)) {
			logger.info("Setting free memory of replica " + replica + " to " + (int)value);
			replica.getMemory().setFreeMem((int)value);
		} else if(fqnTokens[fqnTokens.length-2].equals(DISK_INFO_NAME)) {
			Iterator<Disk> disks = replica.getDisks().iterator();
			Disk disk = null;
			File device = new File(fqnTokens[fqnTokens.length-1]);
			while(disks.hasNext()) {
				Disk tmpDisk = disks.next();
				if(tmpDisk.getDiskConf().getFileSystem().equals(device)) {
					disk = tmpDisk;
					break;
				}					
			}
			if(disk == null) {
				logger.error("Could not find device attached to " + device + " in VEE replica " + replica);
				return;
			}
			logger.info("Setting free capacity of disk " + device + " of replica " + replica + " to " + (int)value);
			disk.setFreeCapacity((int)value);
		}
	}
	
	private void updateServiceKPI(String dataFQN, double value) {
		String[] fqnTokens = dataFQN.split(".");
		if(fqnTokens[2].length() < 2) {
			logger.error("Bad FQN of service KPI: " + dataFQN);
			return;
		}
		
		if(!fqnTokens[fqnTokens.length-2].equals(SERVICE_KPIS_NAME_SPACE)){
			logger.error("Bad FQN of service KPI: " + dataFQN);
			return;
		}
		
		FQN serviceKPIFQN = new FQN(dataFQN);
		Object registeredObject = ReservoirDirectory.getInstance().getObject(serviceKPIFQN);
		if(registeredObject == null) {
			logger.error("Nothing registered on directory with FQN " + serviceKPIFQN);
			return;			
		}
		if(!(registeredObject instanceof ServiceKPI)) {
			logger.error("Object registered with FQN " + serviceKPIFQN + " is not a service KPI");
			return;
		}
		logger.info("Updating service KPI " + serviceKPIFQN + " to "  + value);
		((ServiceKPI)registeredObject).setKPIValue(value);		
	}

}
