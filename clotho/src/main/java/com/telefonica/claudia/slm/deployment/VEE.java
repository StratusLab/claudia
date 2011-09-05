/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.deployment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.hwItems.CPUConf;
import com.telefonica.claudia.slm.deployment.hwItems.DiskConf;
import com.telefonica.claudia.slm.deployment.hwItems.MemoryConf;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.maniParser.GetOperationsUtils;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
@SuppressWarnings("unchecked")
public class VEE implements Comparable, DirectoryEntry {
    
    public static enum VirtualizationTechnologyType {XEN, VMWARE, KVM};
    
	@Id
	@GeneratedValue
	public long internalId;
	
    private String veeName = null;
    

    
    @ManyToOne
    private ServiceApplication serviceApplication = null;
    
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private FQN veeFQN = null;
    
    @Enumerated(EnumType.STRING)
    private VirtualizationTechnologyType virtType = null;
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<DiskConf> disksConf = new ArrayList<DiskConf>();
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<Product> products = new ArrayList<Product>();
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<CPUConf> cpusConf = new ArrayList<CPUConf>();
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<NICConf> nicsConf = new ArrayList<NICConf>();
    
    @OneToOne(cascade=CascadeType.ALL)
    private MemoryConf memoryConf = null;
    
    @OneToOne(cascade = CascadeType.ALL)
    private VEE balancedBy = null;
    
   

    
    private int lastReplicaId = 0;
    private int maxReplicas = 1;    
    private int minReplicas = 1;
    private int initReplicas = minReplicas;
    private int currentReplicas= 0;
    private boolean hotMigrationAllowed = true;
    private boolean coldMigrationAllowed = true;
    private int deploymentOrder = -1;
    private int lbManagementPort = 0;
    private boolean isBalancer = false;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="ALLOWED_DOMAINS")
    private Set<GeographicDomain> allowedDomains = new HashSet<GeographicDomain>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="FORBIDDEN_DOMAINS")
    private Set<GeographicDomain> forbiddenDomains = new HashSet<GeographicDomain>();
    
    @OneToMany(cascade=CascadeType.ALL)
    private Set<ISOImage> isoImages = new HashSet<ISOImage>();
    
    @OneToMany(mappedBy="vee", cascade=CascadeType.ALL)
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
    @Column(columnDefinition = "VARCHAR(32672)")
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
    
    public VEE(String veeName, ServiceApplication serviceApplication) {
    	if(veeName == null)
    		throw new IllegalArgumentException("VEE name cannot be null");
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application name cannot be null");
        this.veeName = veeName;
        this.serviceApplication = serviceApplication;
    }
    
    public void addDiskConf(DiskConf diskConf) {
		System.out.println(" +++++ DiskConf " + diskConf.getImageURL() + " added to VEE " + getFQN().toString() + " configuration");
        disksConf.add(diskConf);
    }
    
    public List<DiskConf> getDisksConf() {
        return disksConf;
    }
    
    public void addProduct(Product product) {
		System.out.println(" +++++ Product " + product.getName());
		products.add(product);
    }
    
    public List<Product> getProducts() {
        return products;
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
    
    public VEE getBalancedBy() {
        return balancedBy;
}

public void setBalancedBy(VEE balancedBy) {
        this.balancedBy = balancedBy;
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
        return ++lastReplicaId;             
    }
    
    public int VEELastReplicaId() {        
        return lastReplicaId;             
    }
    
    public Set<VEEReplica> getVEEReplicas() {
        return activeReplicas;
    }
    
    public void registerVEEReplica(VEEReplica veeReplica) {
        
        if(veeReplica == null)
            throw new IllegalArgumentException("Cannot register null VEE replica");
        
        if(!veeReplica.getVEE().equals(this))
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

    public VirtualizationTechnologyType getVirtType() {
        return virtType;
    }

    public void setVirtType(VirtualizationTechnologyType virtType) {
        this.virtType = virtType;
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
        return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof VEE))
            return false;
        
        return ((VEE)object).getFQN().equals(getFQN());
        
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
	
	   public Document toXML() {
	    	
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder;
	        Document doc;
	    	
	        String organizationId = SMConfiguration.getInstance().getSiteRoot().replace(".", "_");
	        
	        try {
				docBuilder = dbfac.newDocumentBuilder();
				
				doc = docBuilder.newDocument();
				
			
		    		
		    		Element veeElement = doc.createElement("VApp");
		    		doc.appendChild(veeElement);
		    				    		
		    		veeElement.setAttribute("name", getFQN().toString());
		    		
		    		veeElement.setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:min", ""+this.getMinReplicas());
		    		veeElement.setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:max", ""+this.getMaxReplicas());
		    		veeElement.setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:initial", ""+this.getInitReplicas());	                	
	    			
		    		veeElement.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" + this.getServiceApplication().getCustomer().getCustomerName() + "/vapp/" + this.getServiceApplication().getSerAppName() + "/" + getVEEName());
		    		
		    		Element monitorLink = doc.createElement("Link");
		    		veeElement.appendChild(monitorLink);
		    		
		    		monitorLink.setAttribute("rel", "monitor:measures");
		    		monitorLink.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
		    		monitorLink.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" + 
		    				this.getServiceApplication().getCustomer().getCustomerName() + "/vapp/" + this.getServiceApplication().getSerAppName() + "/" + this.getVEEName() + "/monitor");
		    		
		    		Element veeChildren = doc.createElement("Children");
		    		veeElement.appendChild(veeChildren);
		    		
		    		SortedSet<VEEReplica> orderedVEEReplicas = new TreeSet<VEEReplica>(new VEEReplicasComparator());
		    		orderedVEEReplicas.addAll(getVEEReplicas());
		    		
		    		for(VEEReplica veeReplica : orderedVEEReplicas) { 
		    			
		    			Element veeReplicaElement = doc.createElement("VApp");
		    			veeChildren.appendChild(veeReplicaElement);
		    			
		    			veeReplicaElement.setAttribute("name", veeReplica.getFQN().toString());
		    			
		    			
		    			veeReplicaElement.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" +   this.getServiceApplication().getCustomer().getCustomerName() + "/vapp/" + this.getServiceApplication().getSerAppName() +
														"/" + getVEEName() + "/" + veeReplica.getId());
		    			
		    			Element linkVeeReplica = doc.createElement("Link");
		    			veeReplicaElement.appendChild(linkVeeReplica);
		    			
		    			linkVeeReplica.setAttribute("rel", "monitor:measures");
		    			linkVeeReplica.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
		    			linkVeeReplica.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" +   
		    					this.getServiceApplication().getCustomer().getCustomerName() + "/vapp/" + this.getServiceApplication().getSerAppName() + "/" + getVEEName() + "/" + veeReplica.getId() + "/monitor");
		    			
		    			Element virtuahardware = GetOperationsUtils.getVirtualHardwareSystem(doc, "@HOSTNAME/api/org/" + organizationId + "/vdc/" +   this.getServiceApplication().getCustomer().getCustomerName() + "/vapp/" + this.getServiceApplication().getSerAppName() +
														"/" + getVEEName() + "/" + veeReplica.getId(), veeReplica);
		    			veeReplicaElement.appendChild(virtuahardware);

		    		}
		    	
		    	
		    	return doc;

			} catch (ParserConfigurationException e) {
				
			}

	        return null;
	    }
	   
		public void setLbManagementPort(int lbManagementPort)
		{
			this.lbManagementPort = lbManagementPort;
		}
		
		public int getLbManagementPort()
		{
			return this.lbManagementPort;
		}
		
		public void setBalancer(boolean isBalancer)
		{
			this.isBalancer= isBalancer;
		}
		
		public boolean getBalancer()
		{
			return this.isBalancer;
		}
}