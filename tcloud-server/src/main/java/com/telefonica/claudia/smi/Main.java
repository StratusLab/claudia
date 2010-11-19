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

package com.telefonica.claudia.smi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.restlet.Component;
import org.restlet.Router;
import org.restlet.data.Protocol;

import com.telefonica.claudia.smi.deployment.DeploymentApplication;
import com.telefonica.claudia.smi.monitoring.MonitoringApplication;
import com.telefonica.claudia.smi.provisioning.ProvisioningApplication;
import com.telefonica.claudia.smi.task.TaskApplication;
import com.telefonica.claudia.smi.task.TaskManager;

public class Main {

	private static final int ERROR_CODE_INITIALIZATION = 1;
	private static final int ERROR_CODE_DRIVERS = 2;

	public static final String PATH_TO_PROPERTIES_FILE = "./conf/tcloud.properties";
	public static final String PATH_TO_REPOSITORY = "./repository";

	private static final String KEY_PORT = "com.telefonica.claudia.server.port";
	private static final String KEY_HOST = "com.telefonica.claudia.server.host";

	private static final String KEY_DRIVER_PROVISIONING = "com.telefonica.claudia.smi.drivers.provisioning";
	private static final String KEY_DRIVER_DEPLOYMENT = "com.telefonica.claudia.smi.drivers.deployment";
	private static final String KEY_DRIVER_MONITORING = "com.telefonica.claudia.smi.drivers.monitoring";

	public static final Object CUSTOMIZATION_PORT_PROPERTY = "com.telefonica.claudia.customization.port";

	public static final String PROTOCOL = "http://";

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

		// Initialize the Task server
		TaskManager.createManager(PROTOCOL + serverHost + ":" + serverPort
				+ URICreation.URI_VDC + "/task");
		TaskApplication taskApp = new TaskApplication();
		component.getDefaultHost().attach(taskApp);

		// Load the driver for each API
		if (prop.get(KEY_DRIVER_MONITORING) != null
				&& !prop.get(KEY_DRIVER_MONITORING).equals("")) {
			try {
				Class claseDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_MONITORING));

				MonitoringApplication.setDriver(claseDriver, prop);

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
		if (prop.get(KEY_DRIVER_PROVISIONING) != null
				&& !prop.get(KEY_DRIVER_PROVISIONING).equals("")) {
			try {
				Class claseDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_PROVISIONING));

				ProvisioningApplication.setDriver(claseDriver, prop);

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

		if (prop.get(KEY_DRIVER_DEPLOYMENT) != null
				&& !prop.get(KEY_DRIVER_DEPLOYMENT).equals("")) {
			try {
				Class claseDriver = cl.loadClass(prop
						.getProperty(KEY_DRIVER_DEPLOYMENT));

				DeploymentApplication.setDriver(claseDriver, prop);

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

		// Start the component.
		component.getDefaultHost().attach(taskApp);
		component.start();

		log.info("Service started");
	}

}
