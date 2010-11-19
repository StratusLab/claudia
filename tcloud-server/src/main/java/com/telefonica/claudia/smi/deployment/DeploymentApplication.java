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
package com.telefonica.claudia.smi.deployment;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.restlet.Router;

import com.telefonica.claudia.smi.URICreation;


public class DeploymentApplication {

	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.deployment.DeploymentApplication");
	
	public static final String ATTR_PLUGIN_DEPLOYMENT="deployment.plugin";
	
	static DeploymentDriver actualDriver;
	
	public static void setDriver(Class<?> driver, Properties prop) throws IllegalArgumentException {
		
		try {
			actualDriver = (DeploymentDriver) driver.getConstructor(Properties.class).newInstance(prop);
		} catch(ClassCastException cce) {
			throw new IllegalArgumentException("Driver class doesn't match the selected Application requirements.");
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Security exception while instantiating the driver: " + e.getMessage());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Couldn't instantiate the driver: " + e.getMessage());
		} catch (IllegalAccessException e) {
			
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error invoking the constructor on the selected class: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Selected driver doesn't implement the DeploymentDriver(Properties) constructor.");
		}
	}
	
	public synchronized void modifyRoot(Router router) {
		
        router.getContext().getAttributes().put(ATTR_PLUGIN_DEPLOYMENT, actualDriver);
        
        router.attach(URICreation.URI_ORG , OrgItemResource.class);
        router.attach(URICreation.URI_VDC_ROOT , VDCItemCollectionResource.class);
        router.attach(URICreation.URI_VDC , VDCItemResource.class);
        router.attach(URICreation.URI_VDC + "/vapp", ServiceItemCollectionResource.class);
        router.attach(URICreation.URI_VAPP, ServiceItemResource.class);
        router.attach(URICreation.URI_VDC + "/action/instantiateOvf", ServiceItemCollectionResource.class);  
        router.attach(URICreation.URI_VAPP2, VeeItemResource.class);

        log.info("Routes attached to router");
    }

}
