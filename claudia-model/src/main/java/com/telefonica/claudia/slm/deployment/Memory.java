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
public class Memory extends HWStatus {
	
	@OneToOne
    private MemoryConf memoryConf = null;
    
    @ManyToOne
    protected VEEReplica veeReplica = null;
	
    private double freeMem = 0;
    private long cacheMem = 0;
    
    @Transient
    private FQN memFQN = null;
    
    public Memory() {}
    
    public Memory(MemoryConf memoryconf, VEEReplica veeReplica) {
    	if(memoryconf == null)
    		throw new IllegalArgumentException("Memory conf cannot be null");
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");
        this.veeReplica = veeReplica;
        this.memoryConf=memoryconf;
        this.freeMem = memoryconf.getCapacity();
    }

    public long getCacheMem() {
        return cacheMem;
    }

    public void setCacheMem(int cacheMem) {
        this.cacheMem = cacheMem;
    }

    public double getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(int freeMem) {
        this.freeMem = freeMem;
    }
    
    public VEEReplica getVEEReplica() {
        return veeReplica;
    }
    
    public MemoryConf getMemoryConf() {
    	return memoryConf;
    }
    
    public FQN getFQN() {
        if(memFQN == null)
        	memFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return memFQN;
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
        
        if(!(object instanceof Memory))
            return false;
                    
        return ((Memory)object).internalId == this.internalId;
    }
    
	@Override
	public double getUsed() {
		return this.getValue();
		
	}

	@Override
	public HWComponent getComponent() {
		return getMemoryConf();
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
