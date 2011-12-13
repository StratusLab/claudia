/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */

package com.telefonica.claudia.smi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;


import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Directory;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;


import com.telefonica.claudia.smi.console.ConsoleApplication;
import com.telefonica.claudia.smi.deployment.DeploymentApplication;
import com.telefonica.claudia.smi.monitoring.MonitoringApplication;
import com.telefonica.claudia.smi.provisioning.ProvisioningApplication;
import com.telefonica.claudia.smi.task.TaskApplication;
import com.telefonica.claudia.smi.task.TaskManager;
import com.telefonica.claudia.smi.templateManagement.TemplateManagementApplication;

public class Main {

	protected static final Set<PersistenceProvider> providers = new HashSet<PersistenceProvider>();
	private static final int ERROR_CODE_INITIALIZATION = 1;
	private static final int ERROR_CODE_DRIVERS = 2;

	public static final String PATH_TO_PROPERTIES_FILE = "./conf/tcloud.properties";
	public static final String PATH_TO_REPOSITORY = "./repository";

	private static final String KEY_PORT = "com.telefonica.claudia.server.port";
	private static final String KEY_HOST = "com.telefonica.claudia.server.host";

	private static final String KEY_DRIVER_PROVISIONING = "com.telefonica.claudia.smi.drivers.provisioning";
	private static final String KEY_DRIVER_DEPLOYMENT = "com.telefonica.claudia.smi.drivers.deployment";
	private static final String KEY_DRIVER_MONITORING = "com.telefonica.claudia.smi.drivers.monitoring";
	private static final String KEY_DRIVER_TASKMANAGER = "com.telefonica.claudia.smi.drivers.taskManager";
	private static final String KEY_DRIVER_TEMPLATEMANAGEMENT = "com.telefonica.claudia.smi.drivers.templateManagement";	
	private static final String KEY_DRIVER_CONSOLE = "com.telefonica.claudia.smi.drivers.console";
	
	public static final Object CUSTOMIZATION_PORT_PROPERTY = "com.telefonica.claudia.customization.port";
	
	public static final String PROTOCOL = "http://";
	private static final String HEARTBEAT_URI = "/heartbeat";

	private static Logger log = Logger
			.getLogger("com.telefonica.claudia.smi.Main");

	public static int serverPort;
	public static String serverHost;

	/**
	 * Load all the drivers deployed in the folder /driver under the root of the
	 * application. This folder is created during the deployment.
	 * 
	 * The driver folder should contain a collection of folders, each one
	 * belonging to a different driver. This folder should contain the all the
	 * jar files needed to execute the driver, including its dependencies.
	 * 
	 * Even if more than one driver is found in the driver folder, only the one
	 * specified in the config file will be loaded.
	 * 
	 * @return A class loader with all the jars belonging to the drivers loaded.
	 * 
	 * @throws MalformedURLException
	 */
	private static URLClassLoader loadDrivers() throws MalformedURLException {

		// First of all, check if there is any zip file, and uncompress it
		unzipFiles();

		// Load all the drivers from the path
		File driverFolder = new File("driver");

		File[] driverFolders = driverFolder.listFiles();

		ArrayList<URL> urlDrivers = new ArrayList<URL>();
		for (File particularDriverFolder : driverFolders) {

			if (particularDriverFolder.getName().charAt(0) == '.'
					|| !particularDriverFolder.isDirectory()) {
				log.info("Ignoring [" + particularDriverFolder.getName() + "]");
				continue;
			}

			File[] drivers = particularDriverFolder.listFiles();

			log.info("Examining [" + particularDriverFolder.getName()
					+ "] driver folder");
			for (int i = 0; i < drivers.length; i++) {

				if (!drivers[i].getName().matches(".*jar")) {
					log.info("Ignoring [" + drivers[i].getName() + "]");
					continue;
				}

				// urlDrivers.add(new URL("jar:file://" +
				// drivers[i].getAbsolutePath() + "!/"));
				urlDrivers.add(drivers[i].toURL());
				log.info("Adding [" + drivers[i].getName()
						+ "] to the classpath");
			}
		}

		return new URLClassLoader(urlDrivers
				.toArray(new URL[urlDrivers.size()]));
	}

	public static void unzipFiles() {
		File driverFolder = new File("driver");

		File[] driverFolders = driverFolder.listFiles();

		for (File particularDriverFile : driverFolders) {
			// Check if its a zip file. If it is, unzip it and delete it.
			if (particularDriverFile.getName().matches(".*\\.zip")) {
				String fileName = particularDriverFile.getName();

				log.info("Trying to uncompress: " + fileName);

				File targetDir = new File(driverFolder, fileName.substring(0,
						fileName.indexOf(".zip")));

				if (!targetDir.mkdir()) {
					log.warn("The directory for driver " + fileName
							+ " could not be created. Ignoring driver.");
					continue;
				}

				try {
					ZipFile zipFile = new ZipFile(particularDriverFile);

					Enumeration<? extends ZipEntry> entries = zipFile.entries();

					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();

						// Create the file inside the target directory
						if (entry.getName().endsWith(".jar")){
							File newEntry = new File(targetDir, entry.getName());

							FileOutputStream out = new FileOutputStream(newEntry);
							InputStream in = zipFile.getInputStream(entry);

							byte[] buffer = new byte[1024];
							int len;

							while ((len = in.read(buffer)) >= 0)
								out.write(buffer, 0, len);

							in.close();
							out.close();
						}
					}

					if (!particularDriverFile.delete())
						log.warn("The file " + particularDriverFile.getName()
								+ " could not be deleted.");

				} catch (ZipException e) {
					log.warn("The file could not be uncompressed. Zip error: "
							+ e.getMessage());
				} catch (IOException e) {
					log
							.warn("The file could not be uncompressed. Unknown IO Error: "
									+ e.getMessage());
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {

		// Load the configuration file
		Map <String, Object> configuration = new HashMap<String, Object>();
		configuration.put("hibernate.connection.url", "jdbc:mysql://62.217.120.136:3306/monitoring?noDatetimeStringSync=true");
		configuration.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.put("hibernate.connection.username", "claudia");
		configuration.put("hibernate.connection.password", "ClaudiaPass");
		configuration.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		
		configuration.put("hibernate.c3p0.min_size", "5");
		configuration.put("hibernate.c3p0.max_size", "50");
		configuration.put("hibernate.c3p0.timeout", "5000");
		configuration.put("hibernate.c3p0.max_statements", "100");
		createEntityManagerFactory("ClaudiaPU",configuration);
		Properties prop = new Properties();
		prop.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));

		try {
			serverPort = Integer.parseInt(prop.getProperty(KEY_PORT));
		} catch (NumberFormatException nfe) {
			log.error("Error parsing the " + KEY_PORT
					+ " property. A number was expected, but '"
					+ prop.getProperty(KEY_PORT) + "' was found.");
			System.exit(ERROR_CODE_INITIALIZATION);
		}

		serverHost = prop.getProperty(KEY_HOST);

		// Load the appropiate drivers
		URLClassLoader cl = loadDrivers();

		// Create a new Component.
		Component component = new Component();

		// Add a new HTTP server listening on port.
		component.getServers().add(Protocol.HTTP, serverPort);

		TaskApplication taskApp = new TaskApplication();
		component.getDefaultHost().attach(taskApp);
	
		((Router)taskApp.getRoot()).attach(HEARTBEAT_URI , HeartbeatResource.class);
		
		// Load the driver for TaskManager
		if (prop.get(KEY_DRIVER_TASKMANAGER) != null
				&& !prop.get(KEY_DRIVER_TASKMANAGER).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_TASKMANAGER));
				// Initialize the Task Manager
				TaskManager taskManager = TaskApplication.setDriver(classDriver, prop);
				taskManager.createManager(taskManager);			

				log.info("TaskManager active.");
			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
				System.exit(ERROR_CODE_DRIVERS);
			} catch (ClassNotFoundException cnfe) {
				log.error("The TaksManager class was not loaded. Check the driver folder contents.");
				System.out.println("The TaksManager class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		} else {
			log.error("No TaskManager driver was specified. Task driver is mandatory.");
			System.out.println("No TaskManager driver was specified. Task driver is mandatory.");
			System.exit(ERROR_CODE_DRIVERS);
		}
		
		// Load the driver for each API
		if (prop.get(KEY_DRIVER_MONITORING) != null
				&& !prop.get(KEY_DRIVER_MONITORING).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_MONITORING));

				/*
				 * Properties prop = new Properties(); try { prop.load(new
				 * FileInputStream("./conf/tcloud.properties")); } catch
				 * (FileNotFoundException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } catch (IOException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); }
				 */
				
				
				
			  /*    for (String s : names) {
			            try{
			                providers.add((PersistenceProvider)loader.loadClass(s).newInstance());
			            } catch (ClassNotFoundException exc){
			            } catch (InstantiationException exc){
			            } catch (IllegalAccessException exc){
			            }*/
				
				MonitoringApplication.setDriver(classDriver, prop);

				MonitoringApplication app = new MonitoringApplication();
				app.modifyRoot((Router) taskApp.getRoot());

				log.info("Monitoring application active.");
			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
			} catch (ClassNotFoundException cnfe) {
				log
						.error("The Monitoring class was not loaded. Check the driver folder contents.");
				System.out
						.println("The Monitoring class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		}
		
		if (prop.get(KEY_DRIVER_DEPLOYMENT) != null
				&& !prop.get(KEY_DRIVER_DEPLOYMENT).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_DEPLOYMENT));

				DeploymentApplication.setDriver(classDriver, prop);

				DeploymentApplication app = new DeploymentApplication();
				app.modifyRoot((Router) taskApp.getRoot());

				log.info("Deployment application active.");

			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
			} catch (ClassNotFoundException cnfe) {
				log
						.error("The Deployment class was not loaded. Check the driver folder contents.");
				System.out
						.println("The Deployment class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		}
		
		if (prop.get(KEY_DRIVER_PROVISIONING) != null
				&& !prop.get(KEY_DRIVER_PROVISIONING).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop.getProperty(KEY_DRIVER_PROVISIONING));

				ProvisioningApplication.setDriver(classDriver, prop);

				ProvisioningApplication app = new ProvisioningApplication();
				app.modifyRoot((Router) taskApp.getRoot());

				log.info("Provisioning application active.");

			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
			} catch (ClassNotFoundException cnfe) {
				log
						.error("The Provisioning driver class was not loaded. Check the driver folder contents.");
				System.out
						.println("The Provisioning driver class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		}

		if (prop.get(KEY_DRIVER_TEMPLATEMANAGEMENT) != null
				&& !prop.get(KEY_DRIVER_TEMPLATEMANAGEMENT).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop.getProperty(KEY_DRIVER_TEMPLATEMANAGEMENT));

				TemplateManagementApplication.setDriver(classDriver, prop);

				TemplateManagementApplication app = new TemplateManagementApplication();
				app.modifyRoot((Router) taskApp.getRoot());

				log.info("TemplateManagement application active.");

			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
			} catch (ClassNotFoundException cnfe) {
				log.error("The Template Manager driver class was not loaded. Check the driver folder contents.");
				System.out.println("The Template Manager class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		}
		
		if (prop.get(KEY_DRIVER_CONSOLE) != null
				&& !prop.get(KEY_DRIVER_CONSOLE).equals("")) {
			try {
				Class classDriver = cl.loadClass(prop.getProperty(KEY_DRIVER_CONSOLE));

				ConsoleApplication.setDriver(classDriver, prop);

				ConsoleApplication app = new ConsoleApplication();
				app.modifyRoot((Router) taskApp.getRoot());

				log.info("Console application active.");

			} catch (IllegalArgumentException iae) {
				log.error("Driver didn't extend the expected class.");
			} catch (ClassNotFoundException cnfe) {
				log.error("The Console driver class was not loaded. Check the driver folder contents.");
				System.out.println("The Console driver class was not loaded. Check the driver folder contents.");
				System.exit(ERROR_CODE_DRIVERS);
			}
		}
		
		final String staticDocRoot = prop.getProperty("com.telefonica.claudia.server.docroot");
    	if (staticDocRoot != null && staticDocRoot.trim().equals("") == false) {
			// start serving static files
			// @see http://www.restlet.org/documentation/1.1/tutorial#part06
			Application application = new Application(component.getContext().createChildContext()) { 
			    @Override  
			    public Restlet createRoot() {		    	
			    	return new Directory(getContext(), staticDocRoot);
			    }  
			};  
			
			((Router)taskApp.getRoot()).attach("", application);
    	}
		
		// Start the component.
		component.getDefaultHost().attach(taskApp);
		component.start();

		log.info("Service started");
	}
	
	 private static void findAllProviders() throws IOException {
	        ClassLoader loader = Thread.currentThread().getContextClassLoader();
	        Enumeration<URL> resources = 
	            loader.getResources("META-INF/services/" + PersistenceProvider.class.getName());
	        Set<String> names = new HashSet<String>();
	        while (resources.hasMoreElements()) {
	            URL url = resources.nextElement();
	            InputStream is = url.openStream();
	            try {
	                names.addAll(providerNamesFromReader(new BufferedReader(new InputStreamReader(is))));
	            } finally {
	                is.close();
	            }
	        }
	        for (String s : names) {
	            try{
	                providers.add((PersistenceProvider)loader.loadClass(s).newInstance());
	                
	                System.out.println ("Provider " + s);
	            } catch (ClassNotFoundException exc){
	            } catch (InstantiationException exc){
	            } catch (IllegalAccessException exc){
	            }
	        }
	    }
	 private static final Pattern nonCommentPattern = Pattern.compile("^([^#]+)");
	 private static Set<String> providerNamesFromReader(BufferedReader reader) throws IOException {
	        Set<String> names = new HashSet<String>();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            line = line.trim();
	            Matcher m = nonCommentPattern.matcher(line);
	            if (m.find()) {
	                names.add(m.group().trim());
	            }
	        }
	        return names;
	    }
	 
	 public static EntityManagerFactory createEntityManagerFactory(
	            String persistenceUnitName, Map properties) {
	        EntityManagerFactory emf = null;
	        if (providers.size() == 0) {
	            try{
	                findAllProviders();
	            } catch (IOException exc){};
	        }
	        for (PersistenceProvider provider : providers) {
	            emf = provider.createEntityManagerFactory(persistenceUnitName, properties);
	            if (emf != null){
	                break;
	            }
	        }
	        if (emf == null) {
	            throw new PersistenceException("No Persistence provider for EntityManager named " + persistenceUnitName);
	        }
	        return emf;
	    }
	

}
