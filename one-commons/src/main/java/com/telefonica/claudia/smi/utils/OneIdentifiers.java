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

import java.util.HashMap;
import java.util.Map;

public class OneIdentifiers {

	/**
	 * Collection containing the mapping from fqns to ids. This mapped is used as a cache
	 * of the getVmId method (vm ids never change once assigned). 
	 */
	private Map<String, Integer> idVmMap;
	
	/**
	 * Collection containing the mapping from fqns to ids. This mapped is used as a cache
	 * of the getNetId method (net ids never change once assigned). 
	 */
	private Map<String, Integer> idNetMap;

	
	private static OneIdentifiers instance = null;
	 
	/**
	 * Constructor of the class.
     *
     * @param url server url
	 * @param session identifier of user session
	 * @throws ConnectionException if connection can not be established
     */
	private OneIdentifiers() {
    	idVmMap = new HashMap<String, Integer>();
    	idNetMap = new HashMap<String, Integer>();
    }
	
	/**
	 * Create an unique instance of the OpenNebulaConnector class
	 * 
	 * @return an instance of OpenNebulaConnector
	 * @throws ConnectionException if connection can not be established
	 */
    public synchronized static OneIdentifiers getInstance() {
        if (instance == null) { 
        	instance = new OneIdentifiers();
        }
        return instance;
    }

	/**
	 * Retrieve the vm's id given its fqn.
	 * 
	 * @param fqn
	 * 		FQN of the Virtual Machine (mapped to its name property in ONE).
	 * 
	 * @return
	 * 		The internal id of the Virtual Machine if it exists or -1 otherwise.
	 * 
	 * @throws Exception 
	 * 		
	 */
    public Integer getVmId(String fqn) throws Exception {
		
		if (!idVmMap.containsKey(fqn)){ 
			idVmMap = OneProvisioningUtils.getVmIds();
		}
		
		if (idVmMap.containsKey(fqn)){
			return idVmMap.get(fqn);
		} else {
			return -1;
		}
	}
    
	/**
	 * Retrieve the virtual network id given its fqn.
	 * 
	 * @param fqn
	 * 		FQN of the Virtual Network (mapped to its name property in ONE).
	 * 
	 * @return
	 * 		The internal id of the Virtual Network if it exists or -1 otherwise.
	 * 
	 * @throws Exception 
	 * 		
	 */
	public Integer getNetId(String fqn) throws Exception {
		if (!idNetMap.containsKey(fqn)){ 
			idNetMap = OneProvisioningUtils.getNetworkIds();
		}
		
		if (idNetMap.containsKey(fqn)){
			return idNetMap.get(fqn);
		} else {
			return -1;
		}
	}

	/**
	 * Removes the virtual network id given its fqn
	 * 
	 * @param fqn
	 * 			FQN of the Virtual Network (mapped to its name property in ONE).
	 * 
	 * @throws Exception 			
	 */
	public void removeNetId(String fqn) throws Exception {
		if (!idNetMap.containsKey(fqn)){ 
			idNetMap.remove(fqn);
		}
	}
	
	/**
	 * Removes the virtual machine id given its fqn
	 * 
	 * @param fqn
	 * 			FQN of the Virtual Machine (mapped to its name property in ONE).
	 * 
	 * @throws Exception 			
	 */
	public void removeVmId(String fqn) throws Exception {
		if (!idVmMap.containsKey(fqn)){ 
			idVmMap.remove(fqn);
		}
	}
	
}
