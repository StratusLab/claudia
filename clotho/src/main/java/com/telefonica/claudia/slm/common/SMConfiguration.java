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
package com.telefonica.claudia.slm.common;

import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.naming.ReservoirDirectory;

public class SMConfiguration extends Properties {

	private static final long serialVersionUID = 1L;
	
	private static SMConfiguration instance = null;
	
	private static Logger logger = Logger.getLogger(SMConfiguration.class);
	
	// Properties file
	private static final String SM_PROPERTIES_FILE_NAME = "./conf/sm.properties";
	
	// Properties names
	//----------------------------------------------------------------------------------------
	
	// IP/Host and port of REST server attending for monitoring info from the
	// VEEs.
	private static final String REST_HOST_PROPERTY_NAME = "RestListenerHost";
	private static final String REST_PORT_PROPERTY_NAME = "RestListenerPort";
	// IP/Host, port and path of HTTP server of disk images.	
	private static final String IMAGES_SERVER_HOST_PROPERTY_NAME = "ImagesServerHost";
	private static final String IMAGES_SERVER_PORT_PROPERTY_NAME = "ImagesServerPort";
	private static final String IMAGES_SERVER_PATH_PROPERTY_NAME = "ImagesServerPath";
	// VEEM (ONE) IP/Host, port and path.
	private static final String VEEM_HOST_PROPERTY_NAME = "VEEMHost";
	private static final String VEEM_PORT_PROPERTY_NAME = "VEEMPort";
	private static final String VEEM_PATH_PROPERTY_NAME = "VEEMPath";
	
	// SMI IP/Host and port
	private static final String SMI_HOST_PROPERTY_NAME = "SMIHost";
	private static final String SMI_PORT_PROPERTY_NAME = "SMIPort";
	
	// Network ranges available for virtual network creation
	private static final String NETWORK_RANGES_AVAILABLE = "NetworkRanges";
	
	// Network ranges available for virtual network creation
	private static final String STATIC_IP_LIST = "StaticIpList";
	
	// Network private MACS available for virtual network creation
	private static final String NETWORK_MAC_LIST = "NetworkMacList";
	private static final String NETWORK_MAC_ENABLE = "MacEnabled";
	
	// Server direction and port of the WASUP
	private static final String WASUP_ACTIVE = "WASUPActive";
	
	private static final String WASUP_HOST_PROPERTY_NAME = "WASUPHost";
	private static final String WASUP_PORT_PROPERTY_NAME = "WASUPPort";
	private static final String WASUP_PATH_PROPERTY_NAME = "WASUPPath";
	private static final String WASUP_LOGIN_PROPERTY_NAME = "WASUPLogin";
	private static final String WASUP_PASSWORD_PROPERTY_NAME = "WASUPPassword";
	
	private static final String SITE_ROOT = "SiteRoot";
	
	private static final String UNDEPLOY_ON_SERVER_STOP= "UndeployOnServerStop";
	
	private static final String JNDI_SERVER_PROVIDER_URL = "java.naming.provider.url";
	private static final String JNDI_NAMING_FACTORY = "java.naming.factory.initial";
	
	private static final String MONITORING_MULTICAST_ADD= "MonitoringAddress";
	
	private static final String DOMAIN_NAME = "DomainName";
	private static final String ACTIVATE_ACD = "ActivateAcd";
	private static final String EXTENDED_OCCI = "ExtendedOCCI";
	
    private static final String CUSTOMER_TYPE= "wasup.customer";
    private static final String SERVICE_TYPE= "wasup.service";
    private static final String VEE_TYPE= "wasup.vee";
    private static final String VEEREPLICA_TYPE= "wasup.veereplica";
    private static final String NETWORK_TYPE= "wasup.network";
    private static final String HW_TYPE= "wasup.hw";
    
    private static final String OVF_ENV_ENTITY_GEN = "OVFEnvEntityGen";
	
	private String serverProviderUrl = null;
	private String namingFactory = null;
	
	private File propsFile = null;
	private String restServerHost = null;
	private int restServerPort = -1;
	private String imagesServerHost = null;
	private int imagesServerPort = -1;
	private String imagesServerPath = null;
	private String veemHost = null;
	private int veemPort = -1;
	private String veemPath = null;
	
	private boolean wasupActive= false;
	private String wasupHost = null;
	private int wasupPort = -1;
	private String wasupPath = null;
	private String wasupLogin = null;
	private String wasupPassword = null;
	
	private String siteRoot = null;
	private boolean activateACD = false;
	private boolean extendedOCCI = false;
	private String domainName = null;
	private String monitoringAddress = null;
	private boolean ovfEnvEntityGen = true;
	
    private String customerType= null;
    private String serviceType= null;
    private String veeType= null;
    private String veeReplicaType= null;
    private String networkType= null;
    private String hwType= null;
	
	private String[] networkRanges = null;
	
	private String[] networkStaticList = null;
	
	private String[] networkMacList = null;
	
	private String macEnabled = null;
	
	private boolean undeployOnServerStop = false;

	private String smiHost;

	private int smiPort;
	
	private boolean management = true;
	
	private String sdcurl = "";
	
	private boolean paasaware = false;
	
	private boolean monitoring = false;
	
	private String monitoringurl = "";
	
	private SMConfiguration() throws Exception {		
		readSMProperties();
	}
	
	public static void loadProperties() throws Exception {
		instance = new SMConfiguration();
	}
	
	public static SMConfiguration getInstance() {		
		return instance;
	}
	
	// Load properties from default file.
	private void readSMProperties() throws Exception {
		
		// Checking that the properties file is there.
		propsFile = new File(SM_PROPERTIES_FILE_NAME);
		
		logger.info("Reading properties file " + propsFile.getAbsolutePath());
		
		if(!propsFile.exists()) {
			String errorMessage = "Service Manager configuration file " + propsFile.getAbsolutePath() + " does not exist!";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		if(!propsFile.canRead()) {
			String errorMessage = "Cannot read Service Manager configuration file " + propsFile.getAbsolutePath() + "!";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		
		// Loading file
		super.load(new FileReader(propsFile));
		
		// Reading properties and checking validity
		restServerHost = readProperty(REST_HOST_PROPERTY_NAME);
		restServerPort = readPortInProperty(REST_PORT_PROPERTY_NAME);
		imagesServerHost = readProperty(IMAGES_SERVER_HOST_PROPERTY_NAME);
		imagesServerPort = readPortInProperty(IMAGES_SERVER_PORT_PROPERTY_NAME);
		imagesServerPath = readProperty(IMAGES_SERVER_PATH_PROPERTY_NAME);
		veemHost = readProperty(VEEM_HOST_PROPERTY_NAME);
		veemPort = readPortInProperty(VEEM_PORT_PROPERTY_NAME);
		veemPath = readProperty(VEEM_PATH_PROPERTY_NAME);
		
		smiHost = readProperty(SMI_HOST_PROPERTY_NAME);
		smiPort = readPortInProperty(SMI_PORT_PROPERTY_NAME);
		
		networkRanges = readCollectionInProperty(NETWORK_RANGES_AVAILABLE);
		
		try
		{
			networkMacList = readCollectionInProperty(NETWORK_MAC_LIST);
		}
		catch (Throwable t) {
			logger.warn("Parsing error on " +  "networkMacList" + "property: not found");
		}
		
		
		
		try
		{
			networkStaticList = readCollectionInProperty(STATIC_IP_LIST);
		}
		catch (Throwable t) {
			logger.warn("Parsing error on " +  "networkStaticList" + "property: not found");
		}
		
		
		try
		{
			macEnabled = readProperty(NETWORK_MAC_ENABLE);
		}
		catch (Throwable t) {
			logger.warn("Parsing error on " +  "macEnabled" + "property: not found");
		}
		
		
		try {
			undeployOnServerStop = Boolean.parseBoolean(readProperty(UNDEPLOY_ON_SERVER_STOP));
		} catch (Throwable t) {
			logger.error("Parsing error on " + UNDEPLOY_ON_SERVER_STOP + " property: " + readProperty(UNDEPLOY_ON_SERVER_STOP));
		}
		
		siteRoot = readProperty(SITE_ROOT);
		
		ReservoirDirectory.ROOT_NAME_SPACE = siteRoot;
		
		namingFactory = readProperty(JNDI_NAMING_FACTORY);
		serverProviderUrl = readProperty(JNDI_SERVER_PROVIDER_URL);
		
		try {
			wasupActive = Boolean.parseBoolean(readProperty(WASUP_ACTIVE));
		} catch (Throwable t) {
			logger.error("Parsing error on " + WASUP_ACTIVE + "property: " + readProperty(WASUP_ACTIVE));
		}
		
		wasupHost = readProperty(WASUP_HOST_PROPERTY_NAME);
		wasupPort = readPortInProperty(WASUP_PORT_PROPERTY_NAME);
		wasupPath = readProperty(WASUP_PATH_PROPERTY_NAME);
		wasupLogin = readProperty(WASUP_LOGIN_PROPERTY_NAME);
		wasupPassword = readProperty(WASUP_PASSWORD_PROPERTY_NAME);
		
		domainName = readProperty(DOMAIN_NAME);
		
	    customerType= readProperty(CUSTOMER_TYPE);
		logger.info("Property [" + CUSTOMER_TYPE + "] with value [" + customerType + "]");
		serviceType= readProperty(SERVICE_TYPE);
		logger.info("Property [" + SERVICE_TYPE + "] with value [" + serviceType + "]");
		veeType= readProperty(VEE_TYPE);
		logger.info("Property [" + VEE_TYPE + "] with value [" + veeType + "]");
		veeReplicaType= readProperty(VEEREPLICA_TYPE);
		logger.info("Property [" + VEEREPLICA_TYPE + "] with value [" + veeReplicaType + "]");
		networkType= readProperty(NETWORK_TYPE);
		logger.info("Property [" + NETWORK_TYPE + "] with value [" + networkType + "]");
		hwType= readProperty(HW_TYPE);
		logger.info("Property [" + HW_TYPE + "] with value [" + hwType + "]");		
		monitoringAddress = readProperty(MONITORING_MULTICAST_ADD);
		logger.info("Property [" + MONITORING_MULTICAST_ADD + "] with value [" + monitoringAddress + "]");
		
		try {
			activateACD = Boolean.parseBoolean(readProperty(ACTIVATE_ACD));
		} catch (Throwable t) {
			logger.error("Parsing error on " + ACTIVATE_ACD + "property: " + readProperty(ACTIVATE_ACD));
		}
		
		logger.info("Property [" + ACTIVATE_ACD + "] with value [" + activateACD + "]");
		
		try {
			extendedOCCI = Boolean.parseBoolean(readProperty(EXTENDED_OCCI));
		} catch (Throwable t) {
			logger.error("Parsing error on " + EXTENDED_OCCI + "property: " + readProperty(EXTENDED_OCCI));
		}
		logger.info("Property [" + EXTENDED_OCCI + "] with value [" + extendedOCCI + "]");
		
		try {
			ovfEnvEntityGen = Boolean.parseBoolean(readProperty(OVF_ENV_ENTITY_GEN));
		} catch (Throwable t) {
			logger.error("Parsing error on " + OVF_ENV_ENTITY_GEN + "property: " + readProperty(OVF_ENV_ENTITY_GEN));
		}
		logger.info("Property [" + OVF_ENV_ENTITY_GEN + "] with value [" + ovfEnvEntityGen + "]");
		
		try
		{
		management = Boolean.parseBoolean(readProperty("ipmanagement"));
		}
		catch (Throwable t) {
			logger.error("Parsing error on " +  "ipmanagement" + "property: not found");
		}
		logger.info("Property [" + "ipmanagement" + "] with value [" + management + "]");
		
		try
		{
		sdcurl = readProperty("sdcurl");
		}
		catch (Throwable t) {
			logger.error("Parsing error on " +  "sdcurl" + "property: not found");
		}
		logger.info("Property [" + "sdcurl" + "] with value [" + sdcurl + "]");
		
		try
		{
		  paasaware = Boolean.parseBoolean(readProperty("paasaware"));
		}
		catch (Throwable t) {
			logger.error(" Parsing error on PaaS Aware " + paasaware );
		}
		logger.info("Property [" + "paasaware" + "] with value [" + paasaware + "]");
		
		try
		{
		  monitoring = Boolean.parseBoolean(readProperty("monitoring"));
		}
		catch (Throwable t) {
			logger.error(" Parsing error on PaaS Aware " + monitoring );
		}
		logger.info("Property [" + "monitoring" + "] with value [" + monitoring + "]");
		
		try
		{
			monitoringurl = readProperty("monitoringurl");
		}
		catch (Throwable t) {
			logger.error("Parsing error on " +  "monitoringurl" + "property: not found");
		}
		logger.info("Property [" + "monitoringurl" + "] with value [" + monitoringurl + "]");
		
		
	}
	
	 public boolean isMonitoringEnabled() {
	    	return monitoring;
	 }
	    
	public void setIsMonitoringEnabled(boolean monitoring) {
	    	 this.monitoring = monitoring;
	}
	
	 public boolean isPaaSAware() {
	    	return paasaware;
	 }
	    
	public void setIpPaaSAware(boolean paasaware) {
	    	 this.paasaware = paasaware;
	}
	 public boolean getIpManagement() {
	    	return management;
	 }
	    
	public void setIpManagement(boolean management) {
	    	 this.management = management;
	}
	
	public String getMonitoringUrl() {
	    	return monitoringurl;
	 }
	    
	public void setMonitoringUrl(String sdcurl) {
	    	 this.monitoringurl = monitoringurl;
	}
	
	public String getSdcUrl() {
    	return sdcurl;
 }
    
public void setSdcUrl(String sdcurl) {
    	 this.sdcurl = sdcurl;
}
	    
    public String getCustomerType() {
    	return customerType;
    }
    
    public String getServiceType() {
    	return serviceType;
    }
    
    public String getVeeType() {
    	return veeType;
    }
    
    public String getVeeReplicaType() {
    	return veeReplicaType;
    }
    
    public String getNetworkType() {
    	return networkType;
    }
    
    public String getHwType() {
    	return hwType;
    }
	
	public String getSiteRoot() {
		return siteRoot;
	}

	public void setSiteRoot(String siteRoot) {
		this.siteRoot = siteRoot;
	}
	
	public boolean isExtendedOCCI() {
		return extendedOCCI;
	}
	
	public boolean isACDActive() {
		return activateACD;
	}
	
	public boolean isWasupActive() {
		return wasupActive;
	}
	
	public boolean isOvfEnvEntityGen() {
		return ovfEnvEntityGen;
	}	
	
	public boolean isUndeployOnServertopFlagSet() {
		return undeployOnServerStop;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
	public String getMonitoringAddress() {
		return monitoringAddress;
	}
	
	public String getRestServerHost() {
		return restServerHost;
	}
	
	public int getRestServerPort() {
		return restServerPort;
	}
	
	public String getImagesServerHost() {
		return imagesServerHost;
	}
	
	public int getImagesServerPort() {
		return imagesServerPort;
	}
	
	public String getImagesServerPath() {
		return imagesServerPath;
	}
	
	public String getVEEMHost() {
		return veemHost;
	}
	
	public int getVEEMPort() {
		return veemPort;
	}
	
	public String getSMIHost() {
		return smiHost;
	}
	
	public int getSMIPort() {
		return smiPort;
	}
	
	public String getVEEMPath() {
		return veemPath;
	}
	
	public String getWASUPHost() {
		return wasupHost;
	}
	
	public int getWASUPPort() {
		return wasupPort;
	}
	
	public String getWASUPPath() {
		return wasupPath;
	}
	
	public String getWASUPPassword() {
		return wasupPassword;
	}
	
	public String getWASUPLogin() {
		return wasupLogin;
	}
	
	
	public String[] getNetworkRanges() {
		return networkRanges;
	}
	
	public String[] getNetworkStaticList() {
		return networkStaticList;
	}
	
	
	public String[] getNetworkMacList() {
		return networkMacList;
	}
	
	public String getNetworkMacEnable() {
		return macEnabled;
	}
	
	private String[] readCollectionInProperty(String propertyName) throws Exception {
		
		String propValue = readProperty(propertyName);
		
		return propValue.split(",");
	}
	
	private int readPortInProperty(String propertyName) throws Exception {
		
		String propValue = readProperty(propertyName);
		
		int port = -1;
		
		try {
			port = Integer.parseInt(propValue);
		} catch (NumberFormatException ex) {
			String errorMessage = "Could not format to a valid integer '" + propValue + "', from property " + propertyName + " in configuration file " + propsFile.getAbsolutePath();
			logger.error(errorMessage, ex);
			throw new Exception(errorMessage, ex);
		}
		
		if((port < 0) || (port > 65535)){
			String errorMessage = port + " is not a vaid port number, check property " + propertyName + " in configuration file " + propsFile.getAbsolutePath();
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
		
		return port;
		
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
	
	@SuppressWarnings("unchecked")
	public Hashtable<String, String> getJNDIEnv() {
		Hashtable env = new Hashtable();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, namingFactory);
		env.put(Context.PROVIDER_URL, serverProviderUrl);
		
		env.put("topic.SERVICE_SLA_VIOLATION_EVENT", "SERVICE_SLA_VIOLATION_EVENT");
		env.put("topic.INFRASTRUCTURE_SLA_VIOLATION_EVENT", "INFRASTRUCTURE_SLA_VIOLATION_EVENT");
		env.put("topic.VEE_HW_MEASUREMENT", "VEE_HW_MEASUREMENT");
		env.put("topic.AGENT_MEASUREMENT", "AGENT_MEASUREMENT");
		env.put("topic.PROBE_MEASUREMENT", "PROBE_MEASUREMENT");
		env.put("topic.FSM_BUS_EVENT", "FSM_BUS_EVENT");
		env.put("topic.SM_CONTROL_EVENT", "SM_CONTROL_EVENT");
		env.put("queue.SMI_CHANNEL_EVENT_REQUEST", "SMI_CHANNEL_REQUEST");
		env.put("queue.SMI_CHANNEL_EVENT_REPLY", "SMI_CHANNEL_REPLY");
		env.put("queue.ADMINISTRATIVE_EVENT_REQUEST", "ADMINISTRATIVE_EVENT_REQUEST");
		env.put("queue.ADMINISTRATIVE_EVENT_REPLY", "ADMINISTRATIVE_EVENT_REPLY");
		return env;
	}

}
