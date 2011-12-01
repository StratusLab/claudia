/*
 *  NUBA Project
 *  http://nuba.morfeo-project.org/
 *
 *  NubaClient translates Tcloud API monitoring calls into SQL querys to the database.
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

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.smi.monitoring.bean.*;
import com.telefonica.claudia.smi.URICreation;

public class NubaClient {
	
	private static Logger log = Logger.getLogger(NubaClient.class);
	
	private static NubaClient _singleton = new NubaClient();
	
	private Connection connection;
	
	private String urlConnection;
	
	private String password;
	
	private String user;
	
	private NubaClient() {}

	public static NubaClient getInstance() {
    	return _singleton;
	}
	
	private void connect() throws SQLException{
		connection = DriverManager.getConnection(urlConnection, this.user, this.password);
		if(!connection.isClosed())
			log.info("Connected to database");
	}
	
	public void login(String host, int port, String database,String user, String passwd, Properties prop) throws ClassNotFoundException, SQLException {
		log.info("NubaClient.login: host=" + host + "; port=" + port + "; database=" + database + "; user=" + user);
		urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + database;
		this.password = passwd;
		this.user = user;
		//Connect to Database
		Class.forName("com.mysql.jdbc.Driver");
		connect();
	}

	protected MeasureDescriptorList getVdcMeasureDescriptorList(String orgName, String vdcName)
	{
		MeasureDescriptorList mdl = new MeasureDescriptorList();
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;
		String nubaId = URICreation.getFQN(orgName, vdcName);
		log.debug("getVdcMeasureDescriptorList FQN: " + nubaId);
		return	mdl;
	}
	
	protected MeasureDescriptorList getVappMeasureDescriptorList(String orgName, String vdcName, ArrayList<String> vappNames) throws SQLException
	{
		MeasureDescriptorList mdl = new MeasureDescriptorList();
		//String nubaId = "";
		/*if (vappNames.size() == 0){
			nubaId = URICreation.getFQN(orgName, vdcName);
		}
		else if (vappNames.size() == 1){
			nubaId = URICreation.getFQN(orgName, vdcName, vappNames.get(0));
		}
		else if (vappNames.size() == 2){
			nubaId = URICreation.getFQN(orgName, vdcName, vappNames.get(0), vappNames.get(1));
		}
		else if (vappNames.size() == 3){
			nubaId = URICrenubaIdArrayation.getFQN(orgName, vdcName, vappNames.get(0), vappNames.get(1), vappNames.get(2));
		}*/
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;		
		String nubaId = URICreation.getFQN(orgName, vdcName, vappNames);
		//Iterator<String> iTvapp = vappNames.iterator();
		//while (iTvapp.hasNext())
			//nubaId = nubaId + "/vapp/" + iTvapp.next();
			//nubaId = nubaId + iTvapp.next();

		log.debug("getVappMeasureDescriptorList FQN: " + nubaId);
		String query = "" +  
						"SELECT metrics.name, types.name AS type, metrics.description FROM " +
						"types,metrics INNER JOIN (" +
						"machine_metrics INNER JOIN machines ON machine_metrics.machine=machines.id) " +
						"ON metrics.id=machine_metrics.metric " +
						"WHERE metrics.type=types.id AND " +
						"machines.name=\'" + nubaId + "\'";
		
		log.debug("getVappMeasureDescriptorList Query: " + query);
		
		if(connection.isClosed())
			connect();
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery(query);			
		while (rs.next())
		{
			mdl.add(BuildMeasureDescriptor(rs.getString("name"), rs.getString("type"), rs.getString("description")));
		}
		return	mdl;
	}
	
	protected MeasureDescriptorList getNetMeasureDescriptorList(String orgName, String vdcName, String netName)
	{
		MeasureDescriptorList mdl = new MeasureDescriptorList();		
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName + "/net/" + netName;
		String nubaId = URICreation.getFQN(orgName, vdcName);
		log.debug("getNetMeasureDescriptorList FQN: " + nubaId);
		return	mdl;
	}
	
	protected MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName, String vdcName, ArrayList<String> vappNames, String hwItemName)
	{
		MeasureDescriptorList mdl = new MeasureDescriptorList();		
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;
		//String nubaId = URICreation.getHwItemFQN(orgName, vdcName, vappNames, hwItemName);		
		//Iterator<String> iTvapp = vappNames.iterator();
		String nubaId="";
		//while (iTvapp.hasNext())
			//nubaId = nubaId + "/vapp/" + iTvapp.next();
			//nubaId = nubaId + iTvapp.next();
			//nubaId = nubaId + "/hw/" + hwItemName;			

		log.debug("getHwItemMeasureDescriptorList FQN: " + nubaId);
		return	mdl;
	}
	
	protected MeasureDescriptor getVdcMeasureDescriptor(String orgName, String vdcName, String measureName)
	{
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;
		String nubaId = URICreation.getFQN(orgName, vdcName);
		log.debug("getVdcMeasureDescriptor FQN: " + nubaId);
		return new MeasureDescriptor();
	}
	
	protected MeasureDescriptor getVappMeasureDescriptor(String orgName, String vdcName, ArrayList<String> vappNames, String measureName) throws SQLException
	{
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;
		//String nubaId = URICreation.getFQN(orgName, vdcName);
		String nubaId = "";
		nubaId = URICreation.getFQN(orgName, vdcName, vappNames);
		/*if (vappNames.size() == 0){
			nubaId = URICreation.getFQN(orgName, vdcName);
		}
		else if (vappNames.size() == 1){
			nubaId = URICreation.getFQN(orgName, vdcName, vappNames.get(0));
		}
		else if (vappNames.size() == 2){
			nubaId = URICreation.getFQN(orgName, vdcName, vappNames.get(0), vappNames.get(1));
		}
		else if (vappNames.size() == 3){
			nubaId = URICreation.getFQN(orgName, vdcName, vappNames.get(0), vappNames.get(1), vappNames.get(2));
		}
		*/
		//Iterator<String> iTvapp = vappNames.iterator();
		//while (iTvapp.hasNext())
			//nubaId = nubaId + "/vapp/" + iTvapp.next();
			//nubaId = nubaId + iTvapp.next();

		log.debug("getVappMeasureDescriptor FQN: " + nubaId);
		String query = "" +
				"SELECT metrics.name, types.name AS type, metrics.description FROM " +
				"types, metrics INNER JOIN (" +
				"machine_metrics INNER JOIN machines ON machine_metrics.machine=machines.id) " +
				"ON metrics.id=machine_metrics.metric " +
				"WHERE metrics.type=types.id AND " +
				"metrics.name=\'" + measureName+ "\' AND machines.name=\'" + nubaId + "\'";
		
		log.debug("getVappMeasureDescriptor Query: " + query);
		
		if(connection.isClosed())
			connect();
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery(query);
		if (rs.first())		
			return BuildMeasureDescriptor(rs.getString("name"), rs.getString("type"), rs.getString("description"));
		return new MeasureDescriptor();
	}
	
	protected MeasureDescriptor getNetMeasureDescriptor(String orgName, String vdcName, String netName, String measureName)
	{
		MeasureDescriptor md = new MeasureDescriptor();	
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName + "/net/" + netName;	
		String nubaId = URICreation.getFQN(orgName, vdcName);
		log.debug("getNetMeasureDescriptor FQN: " + nubaId);
		return md;
	}
	
	protected MeasureDescriptor getHwItemMeasureDescriptor(String orgName, String vdcName, ArrayList<String> vappNames, String hwItemName,	String measureName)
	{
		MeasureDescriptor md = new MeasureDescriptor();	
		//String nubaId = "org/" + orgName + "/vdc/" + vdcName;
		String nubaId = URICreation.getFQN(orgName, vdcName, vappNames);
		//Iterator<String> iTvapp = vappNames.iterator();
		//while (iTvapp.hasNext())
			//nubaId = nubaId + "/vapp/" + iTvapp.next();
			//nubaId = nubaId + iTvapp.next();
			//nubaId = nubaId + "/hw/" + hwItemName;
		log.debug("getHwItemMeasureDescriptor FQN: " + nubaId);
		return md;
	}
	
	protected MeasuredValueList getMeasuredValueList(MeasureDescriptor md,	int samples) throws SQLException
	{
		String nubaIdString = md.getHref().split("/org/")[1];
		nubaIdString = nubaIdString.substring(0, nubaIdString.lastIndexOf("/monitor/"));
		String[] nubaIdList = nubaIdString.split("/");
		List<String> nubaIdArray = Arrays.asList(nubaIdList);
		String orgName = nubaIdArray.get(0);
		String vdcName = nubaIdArray.get(2);
						
		for (int i=0; i<nubaIdArray.size();i++){
			log.debug(i + " " + nubaIdArray.get(i));
		}
		
		List<String> nubaIdVapps = new ArrayList();
						
		for (int i=4;i<nubaIdArray.size();i++) {			
			nubaIdVapps.add(nubaIdArray.get(i));
		}
		
		String nubaId = URICreation.getFQN(orgName, vdcName, nubaIdVapps);
		
		log.debug("getMeasuredValueList FQN: " + nubaIdVapps);
		String metric = md.getName();
		
		String query = "" +
						"SELECT measures.value,timestamps.value AS timestamp,units.name AS unit FROM " +
						"timestamps,units,metrics,measures INNER JOIN machines ON measures.machine=machines.id " +
						"WHERE measures.metric=metrics.id AND measures.timestamp=timestamps.id AND metrics.unit=units.id AND " +
						"metrics.name=\'" + metric + "\' AND machines.name=\'" + nubaId + "\' " +
						"ORDER BY timestamps.value DESC LIMIT "+ samples;
				
		log.debug("getVappMeasureDescriptorList Query: " + query);

		if(connection.isClosed())
			connect();
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		MeasuredValueList mvl = new MeasuredValueList();
		while (rs.next())
		{
			mvl.add(BuildMeasuredValue(rs.getString("value"), rs.getLong("timestamp"), rs.getString("unit")));
		}
		return mvl;
	}
	
	private MeasureDescriptor BuildMeasureDescriptor(String name, String type, String description)
	{
		MeasureDescriptor md = new MeasureDescriptor();
		md.setDescription(description);
		md.setName(name);
		md.setValueType(TypeConversor(type));		
		return md;
	}
	
	private MeasuredValue BuildMeasuredValue(String value, long timestamp, String unit)
	{
		MeasuredValue mv = new MeasuredValue(value,new Date(timestamp*1000),unit);
		return mv;
	}
	
	private String TypeConversor(String type)
	{
		if (type.compareTo("string") == 0)
			return "xs:string";
		else if (type.compareTo("int8") == 0)
			return "xs:byte";
		else if (type.compareTo("uint8") == 0)
			return "xs:unsignedByte";
		else if (type.compareTo("int16") == 0)
			return "xs:short";
		else if (type.compareTo("uint16") == 0)
			return "xs:unsignedShort";
		else if (type.compareTo("int32") == 0)
			return "xs:int";
		else if (type.compareTo("uint32") == 0)
			return "xs:unsignedInt";
		else if (type.compareTo("float") == 0)
			return "xs:float";
		else if (type.compareTo("double") == 0)
			return "xs:double";
		else
			return "xs:string";
	}
}
