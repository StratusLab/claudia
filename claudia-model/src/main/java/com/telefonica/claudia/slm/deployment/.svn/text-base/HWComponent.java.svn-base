/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.deployment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class HWComponent implements PersistentObject {
	
	@Id
	@GeneratedValue
	public long internalId;
	
	public enum HWType {CPU, MEMORY, DISK, NIC};
	
	private HWType componentType;

	public void setType(HWType type) {
		this.componentType = type;
	}

	public HWType getType() {
		return componentType;
	}
	
	public void setComponentType(String type) {
		componentType = HWType.valueOf(type);
	} 
	
	public String getComponentType() {
		return componentType.toString();
	}
	
	public abstract double getCapacity();
	
	public long getObjectId() {
		return internalId;
	}
}
