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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class RuleSet  extends DirectoryEntry implements PersistentObject {

	@Id
	@GeneratedValue
	public long internalId;
	
	@Transient
	protected FQN measureFQN = null;
    
	@ManyToOne
    private ServiceApplication serviceApplication = null;
    
	@Column(columnDefinition = "TEXT")
	private String RIFCode;
	
	@Column(columnDefinition = "TEXT")
	private String OWLCode;
	private String runningState;

	private String name;
	
	public RuleSet() {
		
	}
	
    public RuleSet(ServiceApplication serviceApplication, String name) {
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application cannot be null");
    	if(name == null)
    		throw new IllegalArgumentException("Measured value name cannot be null");
    	this.setServiceApplication(serviceApplication);
    	this.setName(name);
    }
	
	public void setRIFCode(String rIFCode) {
		RIFCode = rIFCode;
	}
	
	public String getRIFCode() {
		return RIFCode;
	}
	
	public void setOWLCode(String oWLCode) {
		OWLCode = oWLCode;
	}
	
	public String getOWLCode() {
		return OWLCode;
	}
	
	public void setRunningState(String runningState) {
		this.runningState = runningState;
	}
	
	public String getRunningState() {
		return runningState;
	}

	public String getName() {
		return name;
	}

	public ServiceApplication getServiceApplication() {
		return serviceApplication;
	}

	public FQN getFQN() {
    	if(measureFQN == null)
    		measureFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return measureFQN;
	}
	
    @Override
    public String toString() {
    	return getFQN().toString();
    }
	
    @Override
    public int hashCode() {
    	return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
    	
    	if(object == null)
    		return false;
    	
    	if(!(object instanceof RuleSet))
    		return false;
    	
    	return ((RuleSet)object).getFQN().equals(getFQN());
    	
    }
    
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		return result;
	}
	
	public long getObjectId() {
		return internalId;
	}

	public void setServiceApplication(ServiceApplication serviceApplication) {
		this.serviceApplication = serviceApplication;
	}

	public void setName(String name) {
		this.name = name;
	}
}
