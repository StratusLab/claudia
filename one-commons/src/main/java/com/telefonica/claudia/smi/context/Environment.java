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
 * Environment related to the virtual machine context
 * 
 * @author luismarcos.ayllon
 *
 */
public abstract class Environment {

	private String vmFqn;

	
    /**
     * Obtains the virtual machine FQN
     * 
     * @return the FQN as string
     */
	public String getVmFqn() {
		return vmFqn;
	}

    /**
     * Establishes the the virtual machine FQN
     * 
     * @param vmFqn
     */
	public void setVmFqn(String vmFqn) {
		this.vmFqn = vmFqn;
	}

	/**
	 * Abstract method to get the environment content
	 * 
	 * @return the environment content as string
	 */
	public abstract String getContent ();
	
}
