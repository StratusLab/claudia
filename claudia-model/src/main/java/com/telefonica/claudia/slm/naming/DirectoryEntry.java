/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.naming;

import java.util.Set;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DirectoryEntry {
	
	private String fqn;
	
	public abstract FQN getFQN();
	
	public void setFqnString(String fqn) {
		this.fqn = fqn;
	}
	
	public String getFqnString() {
		return fqn;
	}
	
	/**
	 * Retrieve the set of all the descendants of this node in the naming
	 * hierarchy, including itself.
	 * 
	 * @return
	 */
	public abstract Set<?> getDescendants();
}
