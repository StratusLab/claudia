/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context.impl;

import com.telefonica.claudia.smi.context.EnvironmentDeployer;
import com.telefonica.claudia.smi.context.EnvironmentDeployerFactory;

/**
 * Creates the Environment Deployer Implementation
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentDeployerFactoryImpl implements EnvironmentDeployerFactory {

	private EnvironmentDeployer deployer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentDeployer getInstance(String type) {
		if (type.equals("deployment-in-server")){
			if (deployer == null)
				deployer = new EnvironmentServerDeployerImpl();
			return deployer;
		} else {  
			if (deployer == null)
				deployer = new EnvironmentLocalDeployerImpl();
			return deployer;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentDeployer getInstance() {
		return getInstance("");
	}

}
