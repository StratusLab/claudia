/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context;

import com.telefonica.claudia.smi.exception.EnvironmentDeploymentException;

/**
 * Interface to deploy the environment anywhere
 * 
 * @author luismarcos.ayllon
 *
 */
public interface EnvironmentDeployer {

	/**
	 * Deploys the environment 
	 * 
	 * @param environment 
	 * 			the environment to be deployed
	 * @throws EnvironmentDeploymentException
	 */
	public void deploy (Environment environment) throws EnvironmentDeploymentException;
	
}
