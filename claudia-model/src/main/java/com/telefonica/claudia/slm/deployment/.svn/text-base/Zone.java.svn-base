package com.telefonica.claudia.slm.deployment;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.telefonica.claudia.slm.common.PersistentObject;


@Entity
public class Zone implements PersistentObject {

	@Id
	@GeneratedValue
	public long internalId;
	
    @OneToMany(mappedBy="zone", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Network> networks = new TreeSet<Network>();
    
    @ManyToOne
    private VDC vdc;
    
    public enum ZoneType {VPN_ACCESS, PRIVATE_PROTECTED, PRIVATE_BACKEND, PUBLIC_UNPROTECTED, PUBLIC_PROTECTED, PUBLIC_CUSTOM_PROTECTED};
    
    private ZoneType zoneType; 
    
	public Zone() {}
    
    @Override
    public String toString() {
        return String.valueOf(internalId);
    }
    
    @Override
    public int hashCode() {
        return (int) (internalId%100);
    }
    
    public void registerNetwork(Network network) {
        
        if(network == null)
            throw new IllegalArgumentException("Cannot register null network");
        
        if(!network.getZone().equals(this))
            throw new IllegalArgumentException("Trying to register network " + network + " on a different Service " + this);
    	
        networks.add(network);
    }
    
    public boolean isNetworkRegistered(Network network) {
    	return networks.contains(network);
    }
    
    public void unregisterNetwork(Network network) {
    	networks.remove(network);
    }
    
    public Set<Network> getNetworks() {
    	return networks;
    }

	public void setVdc(VDC vdc) {
		this.vdc = vdc;
	}

	public VDC getVdc() {
		return vdc;
	}

	public void setZoneType(ZoneType zoneType) {
		this.zoneType = zoneType;
	}

	public ZoneType getZoneType() {
		return zoneType;
	}
	
	public long getObjectId() {
		return internalId;
	}
}
