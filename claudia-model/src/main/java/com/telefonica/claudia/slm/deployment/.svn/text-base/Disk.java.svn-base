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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Disk extends HWStatus {
    
	@OneToOne
    private DiskConf diskConf = null;
    
    private double freeCapacity = 0;   
    
    @ManyToOne
    protected VEEReplica veeReplica = null;
    
    private String urlImage = null;
    
	private boolean readOnly = false;
	
	/**
	 * Determines whether the image of the disk should be copied to each worker host or
	 * used with a shared storage.
	 */
	private boolean cloneDisk = false;
    
	@Transient
	private FQN diskFQN = null;

	@SuppressWarnings("unused")
	private int id;
    
    public Disk() {}
    
    public Disk(int id, DiskConf diskConf, VEEReplica veeReplica) {
    	if(diskConf == null)
    		throw new IllegalArgumentException("Disk conf cannot be null");
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");
        this.veeReplica = veeReplica;
        this.freeCapacity = diskConf.getCapacity();
        this.diskConf = diskConf;
        this.id = id;
    }
    
    public double getFreeCapacity() {
        return freeCapacity;
    }
    
    public void setFreeCapacity(int freeCapacity) {
        this.freeCapacity = freeCapacity;
    }          
    
    public VEEReplica getVEEReplica() {
        return veeReplica;
    }
    
    public DiskConf getDiskConf() {
    	return diskConf;
    }
    
    public FQN getFQN() {
        if(diskFQN == null)
            diskFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return diskFQN;
    }
    
	public boolean readOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public boolean cloneDisk() {
		return cloneDisk;
	}

	public void setCloneDisk(boolean cloneDisk) {
		this.cloneDisk = cloneDisk;
	}
	
	public String getUrlImage() {
		return this.urlImage;
	}
	
	public void setUrlImage(String url) {
		this.urlImage = url;
	}
    
    @Override
    public String toString() {
        return getFQN().toString();
    }
    
    @Override
    public int hashCode() {
        return (int) (internalId%5);
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof Disk))
            return false;
                    
        return ((Disk)object).internalId == this.internalId;
    }

	@Override
	public double getUsed() {
		return this.getValue();
		
	}

	@Override
	public HWComponent getComponent() {
		return getDiskConf();
	}
	
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		return result;
	}

	@Override
	public VEEReplica getRunningReplica() {
		return veeReplica;
	}	
	

}