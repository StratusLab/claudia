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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Manages the server connection using XML RPC technology
 * 
 * @author luismarcos.ayllon
 *
 */
public class XmlRpcConnectionManager implements ConnectionManager<Object> {

	private static Logger log = Logger.getLogger(XmlRpcConnectionManager.class);	
	
	protected XmlRpcClient client;
	private String url;
	
	/**
	 * Constructor of the class.
     *
     * @param url server url
	 * @throws ConnectionException if connection can not be established
     */
	public XmlRpcConnectionManager (String url) throws ConnectionException{
		this.client = null;
		this.url = null;
		
		connect(url);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect (String url) throws ConnectionException{
		log.info("Creating XMLRPC connector");
		
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		this.url = url;
		
		try {
			config.setServerURL(new URL(this.url));
		} catch (MalformedURLException e) { 
			log.error("Malformed URL: " + url);
			throw new ConnectionException("Malformed URL: " + url, e);
		}
		
	    client = new XmlRpcClient();
		client.setConfig(config);
	    log.debug("XMLRPC client configured"); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sendRequest (String action, List<Object> params) throws ConnectionException {
		log.info ("Sending request ["+ action +"]");
		
		if (client == null){
			log.error("Connection was not established");
			throw new ConnectionException ("Connection was not established");		
		}
		
		Object[] result = null;
		
		try {				
			result = (Object[])client.execute(action, params);
		} catch (XmlRpcException ex) {
			log.error("Connection error trying to access OpenNebula: " + ex.getMessage());
			throw new ConnectionException ("Connection error trying to access OpenNebula: " + ex.getMessage());
		}
		
		boolean completed = (Boolean) result[0];
		String res = (result.length >= 2) ? String.valueOf(result[1]) : "";
		if (!completed) {
			log.error ("Request could not be sent: " + res);
			return null;
		}
		return res;
	}
	
}
