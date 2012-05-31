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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class CPU extends HWStatus {
	
	@OneToOne
    private CPUConf cpuConf = null;
    private int id = 0;
    
    @Column(name="cpuUsage")
    private float usage = 0;
    
    @ManyToOne
    protected VEEReplica veeReplica = null;
    
    @Transient
    private FQN cpuFQN = null;
    
    public CPU() {}
    
    public CPU(int id, CPUConf cpuConf, VEEReplica veeReplica) {
    	if(cpuConf == null)
    		throw new IllegalArgumentException("CPU conf cannot be null");
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");    		
        this.id = id;
        this.cpuConf = cpuConf;
        this.veeReplica = veeReplica;
    }
    
    public int getId() {
        return id;
    }
    
    public void setUsage(float usage){
        this.usage = usage;
    }    
    
    public float getUsage(){
        return usage;
    }
    
    public CPUConf getCPUConf() {
        return cpuConf;
    }
    
    public FQN getFQN() {
        if(cpuFQN == null)
        	cpuFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return cpuFQN;
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
        
        if(!(object instanceof CPU))
            return false;
                    
        return ((CPU)object).internalId == this.internalId;
    }

	@Override
	public double getUsed() {
		return this.getValue();
		
	}

	@Override
	public HWComponent getComponent() {
		return getCPUConf();
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
