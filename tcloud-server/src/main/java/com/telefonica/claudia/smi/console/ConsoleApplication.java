/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.console;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.restlet.Router;

import com.telefonica.claudia.smi.URICreation;

public class ConsoleApplication {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.monitoring.ConsoleApplication");

	public static final String ATTR_PLUGIN_CONSOLE = "console.plugin";
	
	static ConsoleDriver actualDriver;

	public static void setDriver(Class<?> driver, Properties prop) throws IllegalArgumentException {
		try {
			actualDriver = (ConsoleDriver) driver.getConstructor(Properties.class).newInstance(prop);
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
			e.printStackTrace();
			throw new IllegalArgumentException("Couldn't instantiate the driver: " + e.getMessage());			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error invoking the constructor on the selected class: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Selected driver doesn't implement the TemplateManagementDriver(Properties) constructor.");
		}
	}
	
	public synchronized void modifyRoot(Router router) {
        router.getContext().getAttributes().put(ATTR_PLUGIN_CONSOLE, actualDriver);
       
		//URI:/api/ticket
		router.attach(URICreation.URI_TICKET, TicketResource.class);
		
        log.info("Routes attached to Console router");
	}
}
