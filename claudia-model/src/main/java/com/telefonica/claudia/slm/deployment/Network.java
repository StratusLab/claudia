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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Network  extends DirectoryEntry implements PersistentObject {
    
	@Id
	@GeneratedValue
	public long internalId;

    public enum NetworkModeType {ROUTED, ISOLATED};
	
	/**
	 * Network name.
	 */
    private String name = null;
    
    /**
     * The maximum number of ips that will be leased in this network. Default value
     * is a C class network (the real value should be calculated upon the expected 
     * needs of the Service Application).
     */
    private int size = 255;
    
	/**
	 * Network addresses asociated to the network. The network addresess
	 * are given by the Lifecycle controller from one of its IP ranges. The value is an array
	 * containing, in this order: 
	 * 		- The IP address of the network.
	 * 		- The Network mask
	 * 		- The DNS Server if there is one, or null, otherwise
	 * 		- The Gateway if there is one, or null otherwise;
	 * 
	 */
	private String[] networkAddresses;
    
	/**
	 * Entrypoints inside each network. Each entrypoint is represented as
	 * a pair <VEEName, IPAddress>. This array contains the last IP Address leased to 
	 * each VEE: it will only make sense in the entrypoint VEES, that will have only
	 * one replica per VEE.
	 */
	private HashMap<String, String> entryPoints= new  HashMap<String, String>();
	
	/**
	 * Identifier of the network in the underlying infrastructure.
	 */
	private int infrastructureId;
    
    private String bridge = null;
	
    @ManyToOne(fetch= FetchType.EAGER)
    private Zone zone = null;
    
    @Transient
    private FQN networkFQN = null;
    
    private long tcloudId;
    private String description;
    private String bandwidthUnits;
    private long bandwidth;
    /**
     * Indicates whether the hosts in this network will have a public IP or
     * belong to a private network.
     */
    private NetworkModeType networkMode;
    
    public Network() {}
    
    public Network(String name, Zone zone) {
    	if(zone == null)
    		throw new IllegalArgumentException("Zone cannot be null");
    	
        this.name = name;
        this.zone = zone;
    }
    
    public String getName() {
        return name;
    }
    
    public void setNetworkAddresses(String[] addresses) {
    	this.networkAddresses = addresses;
    }
    
    public String[] getNetworkAddresses() {
    	return networkAddresses;
    }
    
    public HashMap<String, String> getEntryPoints() {
    	return entryPoints;
    }
    
    public Zone getZone() {
        return zone;
    }
    
    public void setSize(int s) {
    	this.size = s;
    }
    
    public int getSize() {
    	return size;
    }
    
	public String getBrigde() {
		return bridge;
	}
	
	public void setBrigde(String bridge) {
		this.bridge = bridge;
	}
    
    public void setInfrastructureId(int id) {
    	this.infrastructureId = id;
    }
    
    public int getInfrastructureId() {
    	return this.infrastructureId;
    }
    
    public FQN getFQN() {
    	if(networkFQN == null)
    		networkFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return networkFQN;
    }
    
    @Override
    public String toString() {
    	return getFQN().toString();
    }
    
    @Override
    public int hashCode() {
    	return (int) (internalId%100);
    }
    
    @Override
    public boolean equals(Object object) {
    	
    	if(object == null)
    		return false;
    	
    	if(!(object instanceof Network))
    		return false;
    		
    	return ((Network)object).getFQN().equals(getFQN());
    	
    }
    
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		return result;
	}

	public void setTcloudId(long tcloudId) {
		this.tcloudId = tcloudId;
	}

	public long getTcloudId() {
		return tcloudId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setNetworkMode(NetworkModeType networkMode) {
		this.networkMode = networkMode;
		
    	if (networkMode == NetworkModeType.ISOLATED)
			this.bridge= "private";
		else
			this.bridge= "public";
	}

	public NetworkModeType getNetworkMode() {
		return networkMode;
	}

	public void setBandwidthUnits(String bandwidthUnits) {
		this.bandwidthUnits = bandwidthUnits;
	}

	public String getBandwidthUnits() {
		return bandwidthUnits;
	}

	public void setBandwidth(long bandwidth) {
		this.bandwidth = bandwidth;
	}

	public long getBandwidth() {
		return bandwidth;
	}

	public Map<String, String> calculateMeasureDescriptorValues(FQN element) {
		return entryPoints;
	}
	
	public long getObjectId() {
		return internalId;
	}
}
