/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.connection;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.utils.OneProperties;
import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Connector to access to OpenNebula
 * 
 * @author luismarcos.ayllon
 *
 */
public class OpenNebulaConnector extends XmlRpcConnectionManager {

	private static OpenNebulaConnector instance = null;
	
	private String session;
	 
	/**
	 * Constructor of the class.
     *
     * @param url server url
	 * @throws ConnectionException if connection can not be established
     */
	private OpenNebulaConnector(String url) throws ConnectionException {
    	super(url);
    	
   		String oneUser = PropertyManager.getInstance().getProperty(OneProperties.USER_PROPERTY);
        String onePass = PropertyManager.getInstance().getProperty(OneProperties.PASSWORD_PROPERTY);
        session = oneUser + ":" + onePass;
    }

	/**
	 * Create an unique instance of the OpenNebulaConnector class
	 * 
	 * @return an instance of OpenNebulaConnector
	 * @throws ConnectionException if connection can not be established
	 */
    public synchronized static OpenNebulaConnector getInstance() throws ConnectionException {
        if (instance == null) { 
        	String oneUrl = PropertyManager.getInstance().getProperty(OneProperties.URL_PROPERTY);
        	instance = new OpenNebulaConnector(oneUrl);
        }
        return instance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String sendRequest (String action, List<Object> params) throws ConnectionException {
    	List<Object> newParams = new ArrayList<Object>();
    	
    	newParams.add(session);
    	newParams.addAll(params);
    	
    	return super.sendRequest(action, newParams);
	}

}
