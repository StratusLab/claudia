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

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class Notification extends DirectoryEntry implements PersistentObject {

	@Id
	@GeneratedValue
	public long internalId;
	
    @Transient
    protected FQN notificationFQN = null;
    
	@ManyToOne
    private ServiceApplication serviceApplication = null;

	private String notificationName;
	
	private Date expirationDate;
	private Date creationDate;
	private String message;
    
    public Notification(ServiceApplication serviceApplication, String name) {
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application cannot be null");
    	if(name == null)
    		throw new IllegalArgumentException("Notification name cannot be null");
    	this.serviceApplication = serviceApplication;
    	this.notificationName = name;
    }
	
	public FQN getFQN() {
		if(notificationFQN == null)
			notificationFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return notificationFQN;
	}

	public ServiceApplication getServiceApplication() {
		return serviceApplication;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		return result;
	}

	public Map<String, String> calculateMeasureDescriptorValues(FQN element) {
		return null;
	}
	
	public long getObjectId() {
		return internalId;
	}
}
