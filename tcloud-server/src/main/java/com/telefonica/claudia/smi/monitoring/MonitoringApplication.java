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
package com.telefonica.claudia.smi.monitoring;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.restlet.Router;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.monitoring.resources.HWMonitorResource;
import com.telefonica.claudia.smi.monitoring.resources.MeasureDescriptorResource;
import com.telefonica.claudia.smi.monitoring.resources.MeasureResource;
import com.telefonica.claudia.smi.monitoring.resources.NetMonitorResource;
import com.telefonica.claudia.smi.monitoring.resources.TestResource;
import com.telefonica.claudia.smi.monitoring.resources.VappMonitorResource;
import com.telefonica.claudia.smi.monitoring.resources.VdcMonitorResource;


public class MonitoringApplication {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.monitoring.MonitoringApplication");

	public static final String ATTR_PLUGIN_MONITORING="monitoring.plugin";
	
	static MonitoringDriver actualDriver;

	public static void setDriver(Class<?> driver, Properties prop) throws IllegalArgumentException {
		
		System.out.println("MonitoringApplication.setDriver()");
		
		try {
			actualDriver = (MonitoringDriver) driver.getConstructor(Properties.class).newInstance(prop);
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Driver class doesn't match the selected Application requirements.");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Security exception while instantiating the driver: " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Couldn't instantiate the driver: " + e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error invoking the constructor on the selected class: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Selected driver doesn't implement the DeploymentDriver(Properties) constructor.");
		}
		
	}
	
	public synchronized void modifyRoot(Router router) {
		
        router.getContext().getAttributes().put(ATTR_PLUGIN_MONITORING, actualDriver);
        //router.attach(URICreation.URI_VAPP_MON, TestResource.class);
        
		//URI:/org/{org-id}/vdc/{vdc-id}/monitor
		router.attach(URICreation.URI_VDC_MON, VdcMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/monitor
		router.attach(URICreation.URI_VAPP_MON, VappMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/hw/{hwitem-id}/monitor
		router.attach(URICreation.URI_HWIT_MON, HWMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/monitor
		router.attach(URICreation.URI_VAPP2_MON, VappMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/hw/{hwitem-id}/monitor
		router.attach(URICreation.URI_HWIT2_MON, HWMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/monitor
		router.attach(URICreation.URI_VAPP3_MON, VappMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/hw/{hwitem-id}/monitor
		router.attach(URICreation.URI_HWIT3_MON, HWMonitorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/net/{net-id}/monitor
		router.attach(URICreation.URI_NET_MON, NetMonitorResource.class);
		
		//URI:/org/{org-id}/vdc/{vdc-id}/monitor/{measure-id}
		router.attach(URICreation.URI_VDC_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/monitor/{measure-id}
		router.attach(URICreation.URI_VAPP_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/hw/{hwitem-id}/monitor/{measure-id}
		router.attach(URICreation.URI_HWIT_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/monitor/{measure-id}
		router.attach(URICreation.URI_VAPP2_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/hw/{hwitem-id}/monitor/{measure-id}
		router.attach(URICreation.URI_HWIT2_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/monitor/{measure-id}
		router.attach(URICreation.URI_VAPP3_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/hw/{hwitem-id}/monitor/{measure-id}
		router.attach(URICreation.URI_HWIT3_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/net/{net-id}/monitor/{measure-id}
		router.attach(URICreation.URI_NET_MON + URICreation.MEASURE_ID, MeasureDescriptorResource.class);		
		
		//URI:/org/{org-id}/vdc/{vdc-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_VDC_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_VAPP_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/hw/{hwitem-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_HWIT_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_VAPP2_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/hw/{hwitem-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_HWIT2_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_VAPP3_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vapp2-id}/{vapp3-id}/hw/{hwitem-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_HWIT3_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		//URI:/org/{org-id}/vdc/{vdc-id}/net/{net-id}/monitor/{measure-id}/values
		router.attach(URICreation.URI_NET_MON + URICreation.MEASURE_ID + URICreation.VALUES, MeasureResource.class);
		router.attach(URICreation.URI_VAPP3_MON + URICreation.MEASURE_ID+ URICreation.VALUES, MeasureResource.class);

        log.info("Routes attached to Monitoring router");
	}
}
