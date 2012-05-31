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
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.monitoring.MeasurableElement;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
@SuppressWarnings("unchecked")
public class VEE  extends DirectoryEntry implements Comparable, PersistentObject, MeasurableElement {

	
    public static enum VirtualizationTechnologyType {XEN, VMWARE, KVM, AMAZON};
    
	@Id
	@GeneratedValue
	public long internalId;
	
    private String veeName = null;
    
    @ManyToOne
    private ServiceApplication serviceApplication = null;
    
    //@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @Transient
    private FQN veeFQN = null;
    
    @Enumerated(EnumType.STRING)
    private VirtualizationTechnologyType virtType = null;
    
    @JoinTable(name="VEE_DISKSCONF")
    @OneToMany(cascade=CascadeType.ALL)
    private List<DiskConf> disksConf = new ArrayList<DiskConf>();
    
    @JoinTable(name="VEE_CPUSCONF")
    @OneToMany(cascade=CascadeType.ALL)
    private List<CPUConf> cpusConf = new ArrayList<CPUConf>();
    
    @JoinTable(name="VEE_NICSCONF")
    @OneToMany(cascade=CascadeType.ALL)
    private List<NICConf> nicsConf = new ArrayList<NICConf>();
    
    @OneToOne(cascade=CascadeType.ALL)
    private MemoryConf memoryConf = null;
    
    private int maxReplicas = 1;    
    private int minReplicas = 1;
    private int initReplicas = minReplicas;
    private int currentReplicas= 0;
    private boolean hotMigrationAllowed = true;
    private boolean coldMigrationAllowed = true;
    private int deploymentOrder = -1;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="ALLOWED_DOMAINS")
    private Set<GeographicDomain> allowedDomains = new HashSet<GeographicDomain>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="FORBIDDEN_DOMAINS")
    private Set<GeographicDomain> forbiddenDomains = new HashSet<GeographicDomain>();
    
    @OneToMany(cascade=CascadeType.ALL)
    private Set<ISOImage> isoImages = new HashSet<ISOImage>();
    
    @OneToMany(mappedBy="vee", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<VEEReplica> activeReplicas = new HashSet<VEEReplica>();
    
    @Transient
    private AvailabilityWindow availabilityWindow;
    
    // RESERVOIR attributes
    private String UUID=null;
    
    private boolean migratable;
    
    private double availabilityValue;
    
	/**
	 * OVF document that describe the VEE template that will be used to generate virtual machine templates.
	 * The OVF document contains a collection of identifiers that will be replaced with current values when
	 * the VEE is being instantiated.
	 */
    @Column(columnDefinition = "TEXT")
	private String ovfRepresentation=null;
    
    public VEE() {}
    
    public String getUUID() {
    	return UUID;
    }
    
    public void setUUID(String uuid) {
    	this.UUID = uuid;
    }
    
    public boolean getMigratable() {
    	return migratable;
    }
    
    public void setMigratable(boolean migrat) {
    	this.migratable = migrat;
    }
    
    public double getAvailabilityValue() {
		return availabilityValue;
	}

	public void setAvailabilityValue(double availabilityValue) {
		this.availabilityValue = availabilityValue;
	}

	public static class AvailabilityWindow {
    	public String unit;
    	public long value;
    }
    
    public VEE(String veeName) {
    	if(veeName == null)
    		throw new IllegalArgumentException("VEE name cannot be null");
    	
        this.veeName = veeName;
    }
    
    public void addDiskConf(DiskConf diskConf) {
		System.out.println(" +++++ DiskConf " + diskConf.getImageURL() + " added to VEE " + getVEEName() + " configuration");
        disksConf.add(diskConf);
    }
    
    public List<DiskConf> getDisksConf() {
        return disksConf;
    }
    
    public void removeDiskConf(DiskConf diskConf){
    	if (disksConf.remove(diskConf))
    		System.out.println(" +++++ DiskConf " + diskConf.getImageURL() + " removed from VEE " + getFQN().toString() + " configuration");
    	System.out.println(" +++++ There are " + disksConf.size() + " disk configurations left in VEE");
    	for(DiskConf diskConfT : disksConf)
    		System.out.println(" +++++ DiskConf " + diskConf.getImageURL() + " has disk configuration " + diskConfT.getImageURL());
    }    
    
    public void addCPUConf(CPUConf cpuConf) {
        cpusConf.add(cpuConf);
    }
    
    public List<CPUConf> getCPUsConf() {
        return cpusConf;
    }
    
    public void addNICConf(NICConf nicConf) {
        nicsConf.add(nicConf);
    }
    
    public List<NICConf> getNICsConf() {
        return nicsConf;
    }
    
    public void setMemoryConf(MemoryConf memoryConf) {
        this.memoryConf = memoryConf;
    }
    
    public MemoryConf getMemoryConf() {
        return memoryConf;
    }
    
    public String getVEEName() {
        return veeName;
    }
    
    public int nextVEEReplicaId() {      
    	int id = 0;
    	
        for (VEEReplica replica: activeReplicas) {
        	if (replica.getId() > id)
        		id = replica.getId();
        }
        
        return id+1;
    }
    
    public Set<VEEReplica> getVEEReplicas() {
        return activeReplicas;
    }
    
    public void registerVEEReplica(VEEReplica veeReplica) {
        
        if(veeReplica == null)
            throw new IllegalArgumentException("Cannot register null VEE replica");
        
        if(!veeReplica.getParentVEE().equals(this))
            throw new IllegalArgumentException("Trying to register VEE Replica " + veeReplica + " in a different VEE " + this);
        
        activeReplicas.add(veeReplica);
    }
    
    public boolean isVEEReplicaRegistered(VEEReplica veeReplica) {
        return activeReplicas.contains(veeReplica);
    }
    
    public void unregisterVEEReplica(VEEReplica veeReplica) {
        activeReplicas.remove(veeReplica);
    }
    
    public ServiceApplication getServiceApplication() {
        return serviceApplication;
    }
    
    public void addIsoImage(ISOImage isoImage){
        isoImages.add(isoImage);
    }
    
    public Set<ISOImage> getIsoImages() {
        return isoImages;
    }
    
    public void addAllowedDomain(GeographicDomain domain) {
        allowedDomains.add(domain);
    }
    
    public Set<GeographicDomain> getAllowedDomains() {
        return allowedDomains;
    }
    
    public void addForbiddenDomain(GeographicDomain domain) {
        forbiddenDomains.add(domain);
    }
    
    public Set<GeographicDomain> getForbiddenDomains() {
        return forbiddenDomains;
    }

    public boolean isColdMigrationAllowed() {
        return coldMigrationAllowed;
    }

    public void setColdMigrationAllowed(boolean coldMigrationAllowed) {
        this.coldMigrationAllowed = coldMigrationAllowed;
    }

    public boolean isHotMigrationAllowed() {
        return hotMigrationAllowed;
    }

    public void setHotMigrationAllowed(boolean hotMigrationAllowed) {
        this.hotMigrationAllowed = hotMigrationAllowed;
    }

    public int getCurrentReplicas(){
        return currentReplicas;
    }
    
    public void addCurrentReplicas(){
        currentReplicas++;
    }
    
    public void removeCurrentReplicas(){
        currentReplicas--;
    }
    
    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }
    
    public void setInitReplicas(int initReplicas) {
        this.initReplicas = initReplicas;
    }
    
    public int getInitReplicas() {
        return initReplicas;
    }

    public VirtualizationTechnologyType getVirtualizationType() {
        return virtType;
    }

    public void setVirtualizationType(VirtualizationTechnologyType virtType) {
        this.virtType = virtType;
    }
    
    public String getVirtType() {
    	return this.virtType.toString();
    }
    
    public FQN getFQN(){
        if(veeFQN == null)
            veeFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return veeFQN;
    }
    
    @Override
    public String toString() {
        return getFQN().toString();
    }
    
    @Override
    public int hashCode(){
        return (int) (internalId%5);
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof VEE))
            return false;
        
        return ((VEE)object).internalId == this.internalId;
        
    }

	public int getDeploymentOrder() {
		return deploymentOrder;
	}

	public void setDeploymentOrder(int deploymentOrder) {
		this.deploymentOrder = deploymentOrder;
	}
       
    public int compareTo(Object arg0) {
        if(arg0 instanceof VEE){ //if the object is-a VEE 
            VEE temp = (VEE)arg0; //Edit
            if(temp.deploymentOrder == deploymentOrder)
                return 0;
            else if(temp.deploymentOrder < deploymentOrder)
                return 1;
            else return -1;
        }
        return 99;
    }

    public void setAvailabilityWindow(String units, long value) {
    	this.availabilityWindow = new AvailabilityWindow();
    	
    	this.availabilityWindow.unit = units;
    	this.availabilityWindow.value = value;
    }

	public AvailabilityWindow getAvailabilityWindow() {
		return availabilityWindow;
	}

	public void setOvfRepresentation(String ovfRepresentation) {
		this.ovfRepresentation = ovfRepresentation;
	}

	public String getOvfRepresentation() {
		return ovfRepresentation;
	}
	   
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		result.addAll(disksConf);
		result.addAll(cpusConf);
		result.addAll(nicsConf);
		result.add(memoryConf);

		for (VEEReplica vr: getVEEReplicas()) {
			result.addAll(vr.getDescendants());
		}
		
		return result;
	}
	
	
	public long getObjectId() {
		return internalId;
	}

	public void setServiceApplication(ServiceApplication serviceApplication) {
		this.serviceApplication = serviceApplication;
	}

}
