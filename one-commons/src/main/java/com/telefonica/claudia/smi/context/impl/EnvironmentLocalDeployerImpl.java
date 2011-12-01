/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.context.Environment;
import com.telefonica.claudia.smi.context.EnvironmentDeployer;
import com.telefonica.claudia.smi.exception.EnvironmentDeploymentException;
import com.telefonica.claudia.smi.utils.Constants;
import com.telefonica.claudia.smi.utils.OneProperties;

/**
 * An environment deployer implementation to deploy the environment into the 
 * local file system.
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentLocalDeployerImpl implements EnvironmentDeployer {

	private static Logger log = Logger.getLogger(EnvironmentLocalDeployerImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deploy(Environment environment) throws EnvironmentDeploymentException {

		String customizationDirName = "";
		try {
			customizationDirName = PropertyManager.getInstance().getProperty(OneProperties.PATH_TO_CONTEXT_REPOSITORY);	
		} catch (Exception e) {
			/* Loads the configuration file */
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(Constants.PATH_TO_PROPERTIES_FILE));
				customizationDirName = prop.getProperty(OneProperties.PATH_TO_CONTEXT_REPOSITORY);
			} catch (Exception e1) {
				log.error("Property file not found: " + Constants.PATH_TO_PROPERTIES_FILE);
				throw new EnvironmentDeploymentException("Property file not found: " + Constants.PATH_TO_PROPERTIES_FILE);
			}
		}
		customizationDirName += "/" + environment.getVmFqn();
		File customizationDir = new File(customizationDirName);
		customizationDir.mkdirs();

		String customizationFileName = customizationDirName + "/" + "ovf-env.xml";
		File customizationFile = new File(customizationFileName);
		log.info("Creating customization file in " + customizationFile.getPath());

		String customizationDirURLPath = "/" + environment.getVmFqn();

		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(customizationFile));
		} catch (IOException ex) {
			log.error("IO Exception trying to create environment file");
			throw new EnvironmentDeploymentException("IO Exception trying to create environment file", ex);
		}
		
		out.write(environment.getContent() + "\n");
		out.close();

		String urlToCustomFile = "http://" + customizationDirURLPath + "/" + Constants.ENVIRONMENT_FILE_NAME;
		log.info("URL to customization file: " + urlToCustomFile);

	}

}
