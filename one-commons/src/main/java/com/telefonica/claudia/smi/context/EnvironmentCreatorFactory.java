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

/**
 * Defines a factory object to get the concrete instance of 
 * environment creator class depending on some variables 
 *  
 * @author luismarcos.ayllon
 *
 */
public interface EnvironmentCreatorFactory {

	/**
     * Gets the environment creator according to application type.
     * 
     * @param type the type
     * @return the manager.
     */
	public EnvironmentCreator getInstance(String type);

    /**
     * Gets the default environment creator.
     * 
     * @return the manager.
     */
    public EnvironmentCreator getInstance();
 
	
}
