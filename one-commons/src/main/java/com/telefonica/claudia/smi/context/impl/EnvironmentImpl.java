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

import com.telefonica.claudia.smi.context.Environment;

/**
 * The environment implementation using a string as content
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentImpl extends Environment {

	private String content;

	/**
	 * Establishes the environment content as a string
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContent() {
		return content;
	}

}
