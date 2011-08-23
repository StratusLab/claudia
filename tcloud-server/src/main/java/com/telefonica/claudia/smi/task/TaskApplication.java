/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.task;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.provisioning.ProvisioningDriver;

public class TaskApplication extends Application {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.TaskApplication");
	
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createRoot() {
    	
        // Create a router Restlet that defines routes.
        Router router = new Router(getContext());
        
        router.attach(URICreation.URI_TASK, TaskResource.class);
        router.attach(URICreation.URI_ORG + "/task", TaskCollectionResource.class);

        log.info("Routes attached to router");
        
        return router;
    }
    
	public static TaskManager setDriver(Class<?> driver, Properties prop) throws IllegalArgumentException {
		TaskManager actualDriver = null;
		try {
			actualDriver = (TaskManager) driver.getConstructor(Properties.class).newInstance(prop);
		} catch(ClassCastException cce) {
			throw new IllegalArgumentException("Driver class doesn't match the selected Application requirements.");
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Security exception while instantiating the driver: " + e.getMessage());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Couldn't instantiate the driver: " + e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Couldn't access the driver: " + e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Couldn't invocate the driver: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Selected driver doesn't implement the DeploymentDriver(Properties) constructor.");
		}
		return actualDriver;
	}	    
}
