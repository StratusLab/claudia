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

import java.util.List;

import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Interface to send requests
 * 
 * @author luismarcos.ayllon
 *
 */
public interface ConnectionManager<E> {
	
	/**
	 * Connects to the server
	 * 
	 * @param url server url
	 * @throws ConnectionException if connection can not be established
	 */
	public void connect (String url) throws ConnectionException;
	
	/**
	 * Sends a request to be executed
	 * 
	 * @param action action that have to be executed 
	 * @param params list of parameters to execute the action
	 * @return the result of the executed action
	 * @throws ConnectionException if the request can not be sent or executed
	 */
	public String sendRequest (String action, List<E> params) throws ConnectionException;

}
