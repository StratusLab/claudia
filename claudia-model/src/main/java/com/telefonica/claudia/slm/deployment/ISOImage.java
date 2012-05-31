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

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class ISOImage implements PersistentObject {
    
	@Id
	@GeneratedValue
	public long internalId;
	
    private int hashValue = 0;
    
    @Transient
    private URL url = null;
    private String mountUnit = null;
    
    public ISOImage() {}
    
    public ISOImage(int hashValue, URL url, String mountUnit) {
    	if(mountUnit == null)
    		throw new IllegalArgumentException("Mount unit cannot be null");
    	if(url == null)
    		throw new IllegalArgumentException("URL cannot be null");
        this.hashValue = hashValue;
        this.url = url;
        this.mountUnit = mountUnit;                
    }
    
    public int getHashValue() {
        return hashValue;
    }
    
    public URL getUrl() {
        return url;
    }
    
    public String getMountUnit(){
        return mountUnit;
    }
    
	public long getObjectId() {
		return internalId;
	}
}
