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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

import com.telefonica.claudia.slm.monitoring.MeasurableElement;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class NIC extends HWStatus implements MeasurableElement {    
	
    private int id = 0;
    private int throughput = 0;

    @ManyToOne
    protected VEEReplica veeReplica = null;
    
    @CollectionOfElements(fetch=FetchType.EAGER)
    private List<String> ipAddresses = new ArrayList<String>();
    
    private String macAddress = null;
    
    private String instanceId = null;
    
    @OneToOne
    private NICConf nicConf = null;
    
    @Transient
    private FQN nicFQN = null;
    
    public NIC() {}
    
    public NIC(int id, NICConf nicConf, VEEReplica veeReplica) {
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");
    	if(nicConf == null)
    		throw new IllegalArgumentException("NIC conf cannot be null");
        this.id = id;
        this.veeReplica = veeReplica;
        this.nicConf = nicConf;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<String> getIPAddresses() {
        return ipAddresses;
    }
    
    public void addIPAddress(String ipAddress) {
        this.ipAddresses.add(ipAddress);
    }
    
    public String getMacAddress() {
    	return macAddress;
    }
    
    public void setMacAddress(String macAddress) {
    	this.macAddress = macAddress;
    }
    
    public int getThroughput() {
        return throughput;
    }
    
    public void setThroughput(int throughput) {
        this.throughput = throughput;                 
    }
    
    public VEEReplica getVeeReplica() {
        return veeReplica;
    }
    
    public NICConf getNicConf() {
    	return nicConf;
    }
    
    public FQN getFQN() {
        if(nicFQN == null)
            nicFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return nicFQN;
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
        
        if(!(object instanceof NIC))
            return false;
                    
        return ((NIC)object).internalId == this.internalId;
    }
    
	@Override
	public double getUsed() {
		return this.getValue();
		
	}

	@Override
	public HWComponent getComponent() {
		return getNicConf();
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

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceId() {
		return instanceId;
	}
}