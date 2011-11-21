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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.CollectionOfElements;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.monitoring.MeasurableElement;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class ServiceApplication  extends DirectoryEntry implements PersistentObject, MeasurableElement {
	
	@Id
	@GeneratedValue
	public long internalId;
	
    private String serviceName = null;
	
	@ManyToOne
    private VDC vdc = null;
	
	@Transient
	private FQN serAppFQN = null;
    
	@OneToMany(mappedBy="serviceApplication", cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	private Set<VEE> vees = new HashSet<VEE>();
    
    @OneToMany(mappedBy="measuredApplication", cascade=CascadeType.ALL)
    private Set<MeasuredValue> serviceKPIs = new HashSet<MeasuredValue>();
    
    @OneToMany(mappedBy="serviceApplication", cascade=CascadeType.ALL)
    private Set<RuleSet> rules = new HashSet<RuleSet>();
    
    @CollectionOfElements
    private Set<String> hostAffinities = new HashSet<String>();
    
    @CollectionOfElements
    private Set<String> siteAffinities = new HashSet<String>();
    
    @CollectionOfElements
    private Set<String> domainAffinities = new HashSet<String>();
    
    @CollectionOfElements
    private Set<String> hostAntiAffinity = new HashSet<String>();
    
    @CollectionOfElements
    private Set<String> siteAntiAffinities = new HashSet<String>();
    
    @CollectionOfElements
    private Set<String> domainAntiAffinities = new HashSet<String>();
    
    @ManyToMany
    private List<Network> networks = new ArrayList<Network>();
	
	private long initialTime;
	
	private double savings;
	
	private long vmCount;
	
	private long maxVm;
	
	private long vmPerHourLimit;
	
	private long savingsCalculationFrequency;
	
	@Column(columnDefinition = "MEDIUMTEXT")
	private String ovfDescriptor;
	
    public ServiceApplication() {}
    
    public ServiceApplication(String appName, VDC vdc) {
    	if(appName == null)
    		throw new IllegalArgumentException("Application name cannot be null");
    	if(vdc == null)
    		throw new IllegalArgumentException("VDC cannot be null");
        this.serviceName = appName;
        this.vdc = vdc;
    }
    
    public void addHostAffinity(String vee){
        hostAffinities.add(vee);
    }
    
    public Set<String> getHostAffinities() {
        return hostAffinities;
    }
    
    public void addSiteAffinity(String vee){
        siteAffinities.add(vee);
    }
    
    public Set<String> getSiteAffinities() {
        return siteAffinities;
    }
    
    public void addDomainAffinity(String vee){
        domainAffinities.add(vee);
    }
    
    public Set<String> getDomainAffinities() {
        return domainAffinities;
    }
    
    public void addHostAntiAffinity(String vee) {
        hostAntiAffinity.add(vee);
    }
    
    public Set<String> getHostAntiAffinities() {
        return hostAntiAffinity;
    }
    
    public void addSiteAntiAffinity(String vee) {
        siteAntiAffinities.add(vee);
    }
    
    public Set<String> getSiteAntiAffinities() {
        return siteAntiAffinities;
    }
    
    public void addDomainAntiAffinity(String vee) {
        domainAntiAffinities.add(vee);
    }
    
    public Set<String> getDomainAntiAffinities() {
        return domainAntiAffinities;
    }
    
    public String getServiceName(){
        return serviceName;
    }
    
    public void setServiceName(String name) {
    	this.serviceName=name;
    }
    
    public VDC getVdc(){
        return vdc;
    }
    
    public Set<VEE> getVEEs(){
        return vees;
    }
    
    public void registerVEE(VEE vee){
        
        if(vee == null)
            throw new IllegalArgumentException("Cannot register null VEE");
                
        vee.setServiceApplication(this);
        vees.add(vee);
    }
    
    public boolean isVEERegistered(VEE vee) {
        return vees.contains(vee);
    }
    
    public void unregisterVEE(VEE vee) {
        vees.remove(vee);
    }
    
    public Set<MeasuredValue> getServiceKPIs() {
    	return serviceKPIs;
    }
    
    public void registerServiceKPI(MeasuredValue serviceKPI) {
    	
    	if(serviceKPI == null)
            throw new IllegalArgumentException("Cannot register null service KPI");
        
        if(!serviceKPI.getServiceApplication().equals(this))
            throw new IllegalArgumentException("Trying to register service KPI " + serviceKPI + " on a different Service " + this);
    	
        serviceKPIs.add(serviceKPI);
    }
    
    public boolean isServiceKPIRegistered(MeasuredValue serviceKPI) {
    	return serviceKPIs.contains(serviceKPI);
    }
    
    public void unregisterServiceKPI(MeasuredValue serviceKPI) {
    	serviceKPIs.remove(serviceKPI);
    }
    
    public Set<Network> getNetworksSet() {
    	Set<Network> networks = new HashSet<Network>();
    	for(VEE vee : vees) {
    		List<NICConf> nicConfs = vee.getNICsConf();
    		for(NICConf nicConf: nicConfs)
    			networks.add(nicConf.getNetwork());
    	}
    	return networks;
    }
    
    public List<Network> getNetworks() {
    	return networks;
    }
    
    public void registerNetwork(Network network) {
        
        if(network == null)
            throw new IllegalArgumentException("Cannot register null network");
    	
        networks.add(network);
    }
    
    public boolean isNetworkRegistered(Network network) {
    	return networks.contains(network);
    }
    
    public void unregisterNetwork(Network network) {
    	networks.remove(network);
    }
    
    public Set<RuleSet> getServiceRules() {
    	return rules;
    }
    
    public void registerServiceRule(RuleSet rule) {
    	rules.add(rule);
    }
    
    public boolean isServiceRuleRegistered(RuleSet rule) {
    	return rules.contains(rule);
    }
    
    public void unregisterRule(RuleSet rule) {
    	rules.remove(rule);
    }
    
    public FQN getFQN(){
        if(serAppFQN == null)
            serAppFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return serAppFQN;
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
        
        if(!(object instanceof ServiceApplication))
            return false;
        
        return ((ServiceApplication)object).internalId == internalId;
    }

	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		result.add(vdc);
		
		for (VEE vee: getVEEs())
			result.addAll(vee.getDescendants());

		for (MeasuredValue kpi: getServiceKPIs()) 
			result.addAll(kpi.getDescendants());
		
		for (Network net: getNetworks())
			result.addAll(net.getDescendants());
		
		return result;
	}

	public void releaseNetworks() {
		networks.clear();
	}

	public void setOvfDescriptor(String ovfDescriptor) {
		this.ovfDescriptor = ovfDescriptor;
	}

	public String getOvfDescriptor() {
		return ovfDescriptor;
	}
	
	public long getObjectId() {
		return internalId;
	}

	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

	public long getInitialTime() {
		return initialTime;
	}

	public void setSavings(double savings) {
		this.savings = savings;
	}

	public double getSavings() {
		return savings;
	}

	public long getVmCount() {
		return this.vmCount;
	}
	
	public void setVmCount(long vmCount) {
		this.vmCount = vmCount;
	}

	public void setMaxVm(long maxVm) {
		this.maxVm = maxVm;
	}

	public long getMaxVm() {
		return maxVm;
	}

	public void setVmPerHourLimit(long vmPerHourLimit) {
		this.vmPerHourLimit = vmPerHourLimit;
	}

	public long getVmPerHourLimit() {
		return vmPerHourLimit;
	}

	public void setSavingsCalculationFrequency(long savingsCalculationFrequency) {
		this.savingsCalculationFrequency = savingsCalculationFrequency;
	}

	public long getSavingsCalculationFrequency() {
		return savingsCalculationFrequency;
	}
}

class VEEReplicasComparator implements Comparator<VEEReplica> {

	public int compare(VEEReplica replica1, VEEReplica replica2) {
		return replica2.getId() - replica1.getId();
	}
	
}