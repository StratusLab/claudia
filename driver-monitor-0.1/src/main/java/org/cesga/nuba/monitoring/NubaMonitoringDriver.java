/*
 *  NUBA Project
 *  http://nuba.morfeo-project.org/
 *
 *  NubaMonitoringDriver is a driver for Tcloud server that implements the monitoring extension in Nuba Project.
 *
 *  Copyright (C) 2010 Centro de Supercomputación de Galicia (CESGA)  lmcarril@cesga.es
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.cesga.nuba.monitoring;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.MonitoringDriver;
import com.telefonica.claudia.smi.monitoring.bean.*;

public class NubaMonitoringDriver implements MonitoringDriver{
	
	private static Logger log = Logger.getLogger(NubaMonitoringDriver.class);
	
	//Database connection configuration properties 
	private static final String DB_HOST 		= "monitoring.nuba.db.host";
	private static final String DB_PORT 		= "monitoring.nuba.db.port";
	private static final String DB_NAME 		= "monitoring.nuba.db.name";
	private static final String DB_USER	 	    = "monitoring.nuba.db.user";
	private static final String DB_PASSWORD 	= "monitoring.nuba.db.password";
	
	private static NubaClient nClient;
	
	//When a driver is instanced, a properties file with connection information must be used.
	public NubaMonitoringDriver(Properties prop) throws Exception {
		String dbHost = prop.getProperty(DB_HOST);
		int dbPort = Integer.parseInt(prop.getProperty(DB_PORT));
		String dbName = prop.getProperty(DB_NAME);
		String dbUser = prop.getProperty(DB_USER);
		String dbPassword = prop.getProperty(DB_PASSWORD);
		
		log.info("NubaMonitoringDriver(prop) :: "+ dbUser + "@" + dbHost + ":" + dbPort + "/" + dbName );
		log.debug("Debug test" );
		
		nClient = NubaClient.getInstance();
		nClient.login(dbHost, dbPort, dbName, dbUser, dbPassword, prop);
	}
	
	public MeasureDescriptorList getVdcMeasureDescriptorList(String orgName, String vdcName) 
			throws MonitorException 
	{
		log.info("getVdcMeasureDescriptorList");
		return	nClient.getVdcMeasureDescriptorList(orgName, vdcName);
	}
	
	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName, String vdcName, ArrayList<String> vappNames) 
			throws MonitorException
	{
		log.info("getVappMeasureDescriptorList");
		try{
			return	nClient.getVappMeasureDescriptorList(orgName, vdcName, vappNames);
		}
		catch(SQLException excp)
		{
			throw new MonitorException(excp.getMessage());
		}
	}

	public MeasureDescriptorList getNetMeasureDescriptorList(String orgName, String vdcName, String netName) 
			throws MonitorException 
	{
		log.info("getNetMeasureDescriptorList");
		return	nClient.getNetMeasureDescriptorList(orgName, vdcName, netName);
	}

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName, String vdcName, ArrayList<String> vappNames, String hwItemName)
			throws MonitorException 
	{
		log.info("getHwItemMeasureDescriptorList");
		return	nClient.getHwItemMeasureDescriptorList(orgName, vdcName, vappNames, hwItemName);
	}
	
	public MeasureDescriptor getVdcMeasureDescriptor(String orgName, String vdcName, String measureName) 
			throws MonitorException 
	{
		log.info("getVdcMeasureDescriptor");
		return nClient.getVdcMeasureDescriptor(orgName, vdcName, measureName);
	}
	
	public MeasureDescriptor getVappMeasureDescriptor(String orgName, String vdcName, ArrayList<String> vappNames, String measureName)
			throws MonitorException 
	{
		log.info("getVappMeasureDescriptor");
		try{
			return nClient.getVappMeasureDescriptor(orgName, vdcName, vappNames, measureName);
		} 
		catch(SQLException excp)
		{
			throw new MonitorException(excp.getMessage());
		}
	}
	
	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName, String vdcName, ArrayList<String> vappNames, String hwItemName, String measureName) 
			throws MonitorException 
	{
		log.info("getHwItemMeasureDescriptor");
			return nClient.getHwItemMeasureDescriptor(orgName, vdcName, vappNames, hwItemName, measureName);		
	}

	public MeasureDescriptor getNetMeasureDescriptor(String orgName, String vdcName, String netName, String measureName)
			throws MonitorException 
	{
		log.info("getNetMeasureDescriptor");
		return nClient.getNetMeasureDescriptor(orgName, vdcName, netName, measureName);
	}
	
	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md, int samples) 
			throws MonitorException
	{
		log.info("getMeasuredValueList");	
		try{
			return nClient.getMeasuredValueList(md, samples);	
		} 
		catch(SQLException excp)
		{
			throw new MonitorException(excp.getMessage());
		}
	}

	@Override
	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String hwItemName,
			String measureId) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames, String hwItemName)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,
			MeasuredValueFilter filter) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeasuredValueList> getMeasuredValueList(
			List<MeasureDescriptor> md, MeasuredValueFilter filter)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptor getVappMeasureDescriptor(String orgName,
			String vdcName, List<String> vappNames, String measureId)
			throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, List<String> vappNames) throws MonitorException {
		// TODO Auto-generated method stub
		return null;
	}
}
