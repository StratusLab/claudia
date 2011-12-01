/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.smi.exception.ConnectionException;
import com.telefonica.claudia.smi.exception.OpenNebulaConnectionException;
import com.telefonica.claudia.smi.exception.ParserException;
import com.telefonica.claudia.smi.connection.ConnectionManager;
import com.telefonica.claudia.smi.connection.OpenNebulaConnector;

/**
 * Utilities to get information from OpenNebula 
 * 
 * @author luismarcos.ayllon
 *
 */
public class OneProvisioningUtils {

	private static Logger log = Logger.getLogger(OneProvisioningUtils.class);
	
	public static enum ControlActionType {shutdown, hold, release, stop, suspend, resume, finalize};
	
	public static final int INIT_STATE = 0;
	public static final int PENDING_STATE = 1;
	public static final int HOLD_STATE = 2;
	public static final int ACTIVE_STATE = 3;
	public static final int STOPPED_STATE = 4;
	public static final int SUSPENDED_STATE = 5;
	public static final int DONE_STATE = 6;
	public static final int FAILED_STATE = 7;	

	public static final int INIT_SUBSTATE = 0;
	public static final int PROLOG_SUBSTATE = 1;
	public static final int BOOT_SUBSTATE = 2;
	public static final int RUNNING_SUBSTATE = 3;
	public static final int MIGRATE_SUBSTATE = 4;
	public static final int SAVE_STOP_SUBSTATE = 5;
	public static final int SAVE_SUSPEND_SUBSTATE = 6;
	public static final int SAVE_MIGRATE_SUBSTATE = 7;
	public static final int PROLOG_MIGRATE_SUBSTATE = 8;
	public static final int PROLOG_RESUME_SUBSTATE = 9;
	public static final int EPILOG_STOP_SUBSTATE = 10;
	public static final int EPILOG_SUBSTATE = 11;
	public static final int SHUDTOWN_SUBSTATE = 12;
	public static final int CANCEL_SUBSTATE = 13;

	// Tag names of the returning info doc for Virtual machines
	public static final String VM_STATE = "STATE";
	public static final String VM_SUBSTATE = "LCM_STATE";
	
	/**
	 * Obtains information about a specific virtual machine from OpenNebula 
	 * 
	 * @param id the virtual machine identifier
	 * @return a XML document with the information about the virtual machine
	 * @throws OpenNebulaConnectionException if connection can not be established or the 
	 *                                       obtained information is wrong 
	 */
	public static Document getVirtualMachineState(String id) throws Exception {
		log.debug("Sending request to OpenNebula: VM info for id " + id);
			
		try {
			ConnectionManager<Object> conn = OpenNebulaConnector.getInstance();
			List<Object> params = new ArrayList<Object>();
			
			params.add(new Integer(id));
			String res = conn.sendRequest(OneActions.VM_GETINFO_COMMAND, params);
			
			Document doc = XMLUtils.parse(res);
			return doc;
		} catch (ConnectionException e) {
			log.error("Could not connect to OpenNebula: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not connect to OpenNebula", e);
		} catch (ParserException e) {
			log.error("Could not parse the result: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not parse the result", e);
		}
		
	}
	
	/**
	 * Returns a map of the currently deployed VMs, and its ids.
	 * 
	 * @return
	 * 		A map where the key is the VM's FQN and the value the VM's id.
	 * @throws Exception 
	 */
	public static Map<String, Integer> getVmIds() throws Exception {
		log.debug("Sending request to OpenNebula: VMs deployed");
		
		HashMap<String, Integer> mapResult = new HashMap<String, Integer>();
		
		try {
			ConnectionManager<Object> conn = OpenNebulaConnector.getInstance();
			List<Object> params = new ArrayList<Object>();
			
			params.add(new Integer(-2));
			String res = conn.sendRequest(OneActions.VM_GETALL_COMMAND, params);
			
			Document doc = XMLUtils.parse(res);
			NodeList vmList = doc.getElementsByTagName("VM");
			
			for (int i=0; i < vmList.getLength(); i++) {
				
				Element vm = (Element) vmList.item(i);
				
				String fqn = ((Element)vm.getElementsByTagName("NAME").item(0)).getTextContent();
				try {
					Integer value = Integer.parseInt(((Element)vm.getElementsByTagName("ID").item(0)).getTextContent());
					mapResult.put(fqn, value);
				} catch(NumberFormatException nfe) {
					log.warn("Numerical id expected, got [" + ((Element)vm.getElementsByTagName("ID").item(0)).getTextContent() + "]");
					continue;
				}
			}
			
			return mapResult;
		} catch (ConnectionException e) {
			log.error("Could not connect to OpenNebula: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not connect to OpenNebula", e);
		} catch (ParserException e) {
			log.error("Could not parse the result: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not parse the result", e);
		}
		
	}
	
	/**
	 * Returns a map of the currently deployed networks, and its ids.
	 * 
	 * @return
	 * 		A map where the key is the network's FQN and the value the network's id.
	 * @throws IOException
	 */
	public static HashMap<String, Integer> getNetworkIds() throws Exception {
		log.debug("Sending request to OpenNebula: VMs deployed");
		
		HashMap<String, Integer> mapResult = new HashMap<String, Integer>();
		
		try {
			ConnectionManager<Object> conn = OpenNebulaConnector.getInstance();
			List<Object> params = new ArrayList<Object>();
			
			params.add(new Integer(-2));
			String res = conn.sendRequest(OneActions.NET_GETALL_COMMAND, params);
			
			Document doc = XMLUtils.parse(res);
			NodeList vmList = doc.getElementsByTagName("VNET");
			
			for (int i=0; i < vmList.getLength(); i++) {
				
				Element vm = (Element) vmList.item(i);
				String fqn = ((Element)vm.getElementsByTagName("NAME").item(0)).getTextContent();
				try {
					Integer value = Integer.parseInt(((Element)vm.getElementsByTagName("ID").item(0)).getTextContent());
					mapResult.put(fqn, value);
				} catch(NumberFormatException nfe) {
					log.warn("Numerical id expected, got [" + ((Element)vm.getElementsByTagName("ID").item(0)).getTextContent() + "]");
					continue;
				}
			}
			
			return mapResult;
		} catch (ConnectionException e) {
			log.error("Could not connect to OpenNebula: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not connect to OpenNebula", e);
		} catch (ParserException e) {
			log.error("Could not parse the result: " + e.getMessage());
			throw new OpenNebulaConnectionException("Could not parse the result", e);
		}
	}
	
}
