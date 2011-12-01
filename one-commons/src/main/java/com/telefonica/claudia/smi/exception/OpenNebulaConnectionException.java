/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.exception;

/**
 * Thrown when the connection with OpenNebula is not available
 * 
 * @author luismarcos.ayllon
 *
 */
public class OpenNebulaConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	public OpenNebulaConnectionException() {
		super();
	}

	public OpenNebulaConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenNebulaConnectionException(String message) {
		super(message);
	}

	public OpenNebulaConnectionException(Throwable cause) {
		super(cause);
	}
	
}
