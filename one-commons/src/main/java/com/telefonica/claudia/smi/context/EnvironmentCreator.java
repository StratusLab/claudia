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

import com.telefonica.claudia.smi.exception.EnvironmentCreationException;

/**
 * Interface to create the environment 
 * 
 * @author luismarcos.ayllon
 *
 */
public interface EnvironmentCreator {

	/**
	 * Creates the environment
	 * 
	 * @return the environment
	 * @throws EnvironmentCreationException 
	 */
	public Environment create (String ovf) throws EnvironmentCreationException;
	
	/**
	 * Creates the environment class using the VM FQN and the environment content
	 * 
	 * @return the environment
	 * @throws EnvironmentCreationException 
	 */
	public Environment create (String fqn, String text) throws EnvironmentCreationException;

	
}
