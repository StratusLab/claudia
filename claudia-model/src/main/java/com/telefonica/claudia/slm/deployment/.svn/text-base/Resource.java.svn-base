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
import javax.persistence.ManyToOne;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.deployment.HWComponent.HWType;


@Entity
public class Resource implements PersistentObject {

	@Id
	@GeneratedValue
	public long internalId;
	
	private double overallCapacity;
	private double capacityUsed=0.0;
	private double free;
	private HWType type;
	
	@ManyToOne
	private CloudProvider hostedOn;
	
	public Resource() {
		
	}
	
	public Resource(HWType type) {
		this.type = type;
	}
	
	public HWType getType() {
		return this.type;
	}
	
	public String getResourceType() {
		return this.type.toString();
	}
	
	public void setOverallCapacity(double overallCapacity) {
		this.overallCapacity = overallCapacity;
	}
	public double getOverallCapacity() {
		return overallCapacity;
	}
	public void setFree(double free) {
		this.free = free;
	}
	public double getFree() {
		return free;
	}

	public void setHostedOn(CloudProvider hostedOn) {
		this.hostedOn = hostedOn;
	}

	public CloudProvider getHostedOn() {
		return hostedOn;
	}
	
	public void add(double amount) {
		capacityUsed -= amount; 
		
		if (capacityUsed<0) capacityUsed=0;
		
		free = (1- capacityUsed/overallCapacity)*100;
	}
	
	public void remove(double amount) {
		capacityUsed += amount;
		free = (1- capacityUsed/overallCapacity)*100;
	}
	
	public long getObjectId() {
		return internalId;
	}
}
