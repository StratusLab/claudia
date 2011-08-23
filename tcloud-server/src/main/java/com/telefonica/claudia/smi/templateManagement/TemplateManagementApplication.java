/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.templateManagement;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.restlet.Router;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.templateManagement.resources.CatalogResource;
import com.telefonica.claudia.smi.templateManagement.resources.CatalogItemResource;
import com.telefonica.claudia.smi.templateManagement.resources.TemplateResource;
import com.telefonica.claudia.smi.templateManagement.resources.TemplatizeResource;

public class TemplateManagementApplication {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.monitoring.TemplateManagementApplication");

	public static final String ATTR_PLUGIN_TEMPLATEMANAGEMENT="templateManagement.plugin";
	
	static TemplateManagementDriver actualDriver;

	public static void setDriver(Class<?> driver, Properties prop) throws IllegalArgumentException {
		
		System.out.println("TemplateManagementApplication.setDriver()");
		
		try {
			actualDriver = (TemplateManagementDriver) driver.getConstructor(Properties.class).newInstance(prop);
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
		
        router.getContext().getAttributes().put(ATTR_PLUGIN_TEMPLATEMANAGEMENT, actualDriver);
       
		//URI:/api/catalog
		router.attach(URICreation.URI_CATALOG_ROOT, CatalogResource.class);
		//URI:/api/catalog/<catalogId>
		router.attach(URICreation.URI_CATALOGID, CatalogItemResource.class);		
		//URI:/api/template/<templateId>
		router.attach(URICreation.URI_TEMPLATEID, TemplateResource.class);		

        // /api/org/{org-id}/vdc/{vdc-id}/vapp/{vapp-id}/{vee-id}/{vm-id}/action/templatize
        router.attach(URICreation.URI_TEMPLATE_ADD, TemplatizeResource.class);
				
        log.info("Routes attached to TemplateManagement router");
	}
}
