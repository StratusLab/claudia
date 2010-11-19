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
package com.telefonica.claudia.slm.serviceconfiganalyzer;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ScaConfiguration extends Properties {

	private static final long serialVersionUID = 1L;
	
	private static ScaConfiguration instance = null;
	
	private static Logger logger = Logger.getLogger(ScaConfiguration.class);
	
	// Properties file
	private static final String SCA_PROPERTIES_FILE_NAME = "./conf/sca.properties";
	
	// Properties names //
	private static final String SMALL_CPU_NAME = "SmallCpu";
	private static final String SMALL_MEM_NAME = "SmallMem";
	private static final String SMALLH_CPU_NAME = "SmallHCpu";
	private static final String SMALLH_MEM_NAME = "SmallHMem";
	private static final String MEDIUM_CPU_NAME = "MediumCpu";
	private static final String MEDIUM_MEM_NAME = "MediumMem";
	private static final String MEDIUMH_CPU_NAME = "MediumHCpu";
	private static final String MEDIUMH_MEM_NAME = "MediumHMem";
	private static final String LARGE_CPU_NAME = "LargeCpu";
	private static final String LARGE_MEM_NAME = "LargeMem";
	private static final String LARGEH_CPU_NAME = "LargeHCpu";
	private static final String LARGEH_MEM_NAME = "LargeHMem";
	
	private static final String SILVER = "Silver";
	private static final String GOLD = "Gold";
	private static final String PLATINUM = "Platinum";
	
	private static final String UMEA_SITE = "UmeaSite";
	private static final String THALES_SITE = "ThalesSite";
	private static final String MESSINA_SITE = "MessinaSite";
	private static final String IBM_SITE = "IbmSite";

	private File propsFile = null;
	
	private int smallCpu;
	private int smallMem;
	private int smallHCpu;
	private int smallHMem;	
	
	private int mediumCpu;
	private int mediumMem;
	private int mediumHCpu;
	private int mediumHMem;	
	
	private int largeCpu;
	private int largeMem;
	private int largeHCpu;
	private int largeHMem;	
	
	private double silver;
	private double gold;
	private double platinum;
	
	private String umeaSite;
	private String messinaSite;
	private String thalesSite;
	private String ibmSite;
	
	private ScaConfiguration() throws Exception {		
		readSCAProperties();
	}
	
	public static void loadProperties() throws Exception {
		instance = new ScaConfiguration();
	}
	
	public static ScaConfiguration getInstance() {		
		return instance;
	}
	
	// Load properties from default file.
	private void readSCAProperties() throws Exception {
		
		// Checking that the properties file is there.
		propsFile = new File(SCA_PROPERTIES_FILE_NAME);
		
		logger.info("Reading properties file " + propsFile.getAbsolutePath());
		
		if(!propsFile.exists()) {
			String errorMessage = "SCA configuration file " + propsFile.getAbsolutePath() + " does not exist!";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		if(!propsFile.canRead()) {
			String errorMessage = "Cannot read SCA file " + propsFile.getAbsolutePath() + "!";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		
		// Loading file
		super.load(new FileReader(propsFile));
		
		// Reading properties and checking validity
		smallCpu = Integer.parseInt(readProperty(SMALL_CPU_NAME));
		smallMem = Integer.parseInt(readProperty(SMALL_MEM_NAME));
		smallHCpu = Integer.parseInt(readProperty(SMALLH_CPU_NAME));
		smallHMem = Integer.parseInt(readProperty(SMALLH_MEM_NAME));	
		
		mediumCpu = Integer.parseInt(readProperty(MEDIUM_CPU_NAME));
		mediumMem = Integer.parseInt(readProperty(MEDIUM_MEM_NAME));
		mediumHCpu = Integer.parseInt(readProperty(MEDIUMH_CPU_NAME));
		mediumHMem = Integer.parseInt(readProperty(MEDIUMH_MEM_NAME));	
		
		largeCpu = Integer.parseInt(readProperty(LARGE_CPU_NAME));
		largeMem = Integer.parseInt(readProperty(LARGE_MEM_NAME));
		largeHCpu = Integer.parseInt(readProperty(LARGEH_CPU_NAME));
		largeHMem = Integer.parseInt(readProperty(LARGEH_MEM_NAME));
		
		umeaSite = readProperty(UMEA_SITE);
		thalesSite = readProperty(THALES_SITE);
		messinaSite = readProperty(MESSINA_SITE);
		ibmSite = readProperty(IBM_SITE);
		
		silver = Double.parseDouble(readProperty(SILVER));
		gold = Double.parseDouble(readProperty(GOLD));
		platinum = Double.parseDouble(readProperty(PLATINUM));
	}
	
	
	private String readProperty(String propertyName) throws Exception {
		
		String propValue = super.getProperty(propertyName);
		
		if(propValue == null) {
			String errorMessage = "Property " + propertyName + " not found in configuration file " + propsFile.getAbsolutePath();
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		
		propValue = propValue.trim();
		
		if(propValue.isEmpty()) {
			String errorMessage = "Property " + propertyName + " in configuration file " + propsFile.getAbsolutePath() + " has no value";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
	
		return propValue.trim();
	}

	public int getSmallCpu() {
		return smallCpu;
	}

	public int getSmallMem() {
		return smallMem;
	}

	public int getSmallHCpu() {
		return smallHCpu;
	}

	public int getSmallHMem() {
		return smallHMem;
	}

	public int getMediumCpu() {
		return mediumCpu;
	}

	public int getMediumMem() {
		return mediumMem;
	}

	public int getMediumHCpu() {
		return mediumHCpu;
	}

	public int getMediumHMem() {
		return mediumHMem;
	}

	public int getLargeCpu() {
		return largeCpu;
	}

	public int getLargeMem() {
		return largeMem;
	}

	public int getLargeHCpu() {
		return largeHCpu;
	}

	public int getLargeHMem() {
		return largeHMem;
	}

	public double getSilver() {
		return silver;
	}

	public double getGold() {
		return gold;
	}

	public double getPlatinum() {
		return platinum;
	}

	public String getUmeaSite() {
		return umeaSite;
	}

	public String getMessinaSite() {
		return messinaSite;
	}

	public String getThalesSite() {
		return thalesSite;
	}

	public String getIbmSite() {
		return ibmSite;
	}

	
}
