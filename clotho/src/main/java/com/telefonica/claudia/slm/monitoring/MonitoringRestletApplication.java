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

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;

public class MonitoringRestletApplication extends Application {
    
    private static Logger logger = Logger.getLogger(MonitoringRestletApplication.class);
    
    private String monitoringPath = null;
    
    public MonitoringRestletApplication(Context parentContext, String monitoringPath) {
    	super(parentContext);    	
    	getContext().getAttributes().put(MonitoringRestBusConnector.SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS,
    									parentContext.getAttributes().get(MonitoringRestBusConnector.SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS));
    	this.monitoringPath = monitoringPath;
    }

    @Override 
    public Restlet createRoot() {    	
        
        // Create a router Restlet that defines routes.
        Router router = new Router(getContext());
		
        router.attach(monitoringPath, MonitoringRestletResource.class);
        
        logger.info("Path " + monitoringPath + " registered for monitoring messages");
        
        return router;    
    	
    }

}
