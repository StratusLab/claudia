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
 * Thrown when the environment can not be created
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentCreationException extends Exception {

	private static final long serialVersionUID = 1L;

	public EnvironmentCreationException() {
		super();
	}

	public EnvironmentCreationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EnvironmentCreationException(String arg0) {
		super(arg0);
	}

	public EnvironmentCreationException(Throwable arg0) {
		super(arg0);
	}
	
}
