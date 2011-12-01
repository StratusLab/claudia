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
 * Thrown when the environment can not be deployed
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentDeploymentException extends Exception {

	private static final long serialVersionUID = 1L;

	public EnvironmentDeploymentException() {
	}

	public EnvironmentDeploymentException(String message) {
		super(message);
	}

	public EnvironmentDeploymentException(Throwable cause) {
		super(cause);
	}

	public EnvironmentDeploymentException(String message, Throwable cause) {
		super(message, cause);
	}

}
