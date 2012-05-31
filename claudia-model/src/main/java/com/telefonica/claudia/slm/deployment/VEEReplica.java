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
import java.util.Iterator;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.monitoring.MeasurableElement;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.TCloudConstants.StateType;

@Entity
public class VEEReplica extends DirectoryEntry implements PersistentObject, MeasurableElement {
    
	private static Logger logger = Logger.getLogger(VEEReplica.class); 
	
	public static enum ActiveSubStateType {INIT, PROLOG, BOOT, RUNNING, MIGRATE, SAVE_STOP, SAVE_SUSPEND, SAVE_MIGRATE, PROLOG_MIGRATE, PROLOG_RESUME, EPILOG_STOP, EPILOG, SHUTDOWN, CANCEL};

	@Id
	@GeneratedValue
	public long internalId;
	
	@Column(columnDefinition = "TEXT")
    private String customizationInformation = null;
    
    @ManyToOne
    private VEE vee = null;
    
    /**
     * Replica number, unique among the replicas of the related VEE.
     */
    private int id = 0;
    
    @Enumerated(EnumType.STRING)
	private StateType state = StateType.initial;
    
    @Enumerated(EnumType.STRING)
	private ActiveSubStateType subState = null;    
    
    @Transient
    private FQN veeReplicaFQN = null;
    
    @OneToMany(mappedBy="veeReplica", cascade=CascadeType.ALL)
    private List<Disk> disks = new ArrayList<Disk>();
    
    @OneToMany(mappedBy="veeReplica", cascade=CascadeType.ALL)
    private List<CPU> cpus = new ArrayList<CPU>();
    
    @OneToMany(mappedBy="veeReplica", cascade=CascadeType.ALL)
    private List<NIC> nics = new ArrayList<NIC>();
    
    @OneToOne(mappedBy="veeReplica", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Memory memory = null;
    
    private long initialTime;
    
	private String osBoot = null;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private CloudProvider deployedOn = null;
	
	/**
	 * OVF document that describe the VM as it was deployed in the underlying infrastructure.
	 */
	@Column(columnDefinition = "TEXT")
	private String ovfRepresentation=null;
    
    /**
     * Identifier of the Virtual Machine represented by the replica in the underlying
     * infraestructure.
     */
    private String infraestructureId;

	private long monitoringTime;
	
	@Transient
	private Set<String> availablePowerOperations = new HashSet<String>();
	
	private boolean deployed=false;
	private boolean running=false;
    
    public VEEReplica() {}
    
    public VEEReplica(VEE vee) {
    	if(vee == null)
    		throw new IllegalArgumentException("VEE cannot be null");
        this.vee = vee;        
        id = vee.nextVEEReplicaId();
        
        if(vee.getMemoryConf()!=null)
        	memory = new Memory(vee.getMemoryConf(),this);
        
        List<CPUConf> cpusConfs = vee.getCPUsConf();        
        if(cpusConfs != null) {
        	int cpuId = 1;
        	for(CPUConf cpuConf : cpusConfs)
        		cpus.add(new CPU(cpuId++, cpuConf, this));
        }
        
        List<DiskConf> disksConfs = vee.getDisksConf();
        if(disksConfs != null) {
        	int diskId = 1;
        	for(DiskConf diskConf : disksConfs) {
        		System.out.println(" >>> CREATING DISK FOR REPLICA " + this.getFQN() + ", CONF IS :");
        		System.out.println(" >>> Capacity: " + diskConf.getCapacity());
        		System.out.println(" >>> Type: " + diskConf.getType());
        		System.out.println(" >>> File System: " + diskConf.getFileSystem());
        		System.out.println(" >>> Image URL: " + diskConf.getImageURL());
        		disks.add(new Disk(diskId++, diskConf, this));
        	}
        }
        
        List<NICConf> nicsConfs = vee.getNICsConf();
        if(nicsConfs != null) {
        	int nicId = 1;
        	for(NICConf nicConf : nicsConfs) {
        		NIC newNic = new NIC(nicId++,nicConf, this);
        		newNic.getFQN();
        		nics.add(newNic);
        	}
        }
        
		List<Disk> disks;
		disks = getDisks();
		Iterator<Disk> diskIt = disks.iterator();

		while (diskIt.hasNext()) {
			// create disk
			Disk disk = diskIt.next();
			
			if (disk.getDiskConf().getImageURL() != null) {
				
				String urlDisk;
				
				if (disk.getDiskConf().getImageURL().toString().contains("file:/"))
					urlDisk = disk.getDiskConf().getImageURL().toString().replace("file:/", "file:///");
				else
					urlDisk = disk.getDiskConf().getImageURL().toString();
				
				disk.setUrlImage(urlDisk);
			}
			
			disk.setReadOnly(false);
			disk.setCloneDisk(true);
		}
        
        this.getFQN();
    }
    
    @SuppressWarnings("unchecked")
	public void registerHwElementsInResDir() {
    	if(memory != null)
    		ReservoirDirectory.getInstance().registerObject(memory.getFQN(), memory);
    	for(Disk disk : disks)
    		ReservoirDirectory.getInstance().registerObject(disk.getFQN(), disk);
    	for(CPU cpu : cpus)
    		ReservoirDirectory.getInstance().registerObject(cpu.getFQN(), cpu);
    	for(NIC nic : nics)
    		ReservoirDirectory.getInstance().registerObject(nic.getFQN(), nic);    		
    }

	public synchronized StateType getVEEReplicaVmState() {
		return state;
	}
	
	public synchronized void setVEEReplicaVmState(StateType replicaState) {
		this.state = replicaState;
	}
	
	public ActiveSubStateType getVEEReplicaVmSubState() {
		return subState;
	}

	public void setVEEReplicaVmSubState(ActiveSubStateType replicaSubState) {
		this.subState = replicaSubState;
	}
    
    public String getOVFEnvironment() {
        return customizationInformation;
    }
    
    public void setOVFEnvironment(String env) {
        customizationInformation=env;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }

    public String getInfraestructureId() {
        return infraestructureId;
    }
    
    public void setInfraestructureId(String id) {
    	this.infraestructureId = id;
    }
    
    public VEE getParentVEE() {
        return vee;
    }
    
    public void addDisk(Disk disk) {
        disks.add(disk);
    }
    
    public List<Disk> getDisks() {
        return disks;
    }
    
    public void addCPU(CPU cpu) {
        cpus.add(cpu);
    }
    
    public List<CPU> getCPUs() {
        return cpus;
    }
    
    public void addNIC(NIC nic) {
        nics.add(nic);
    }
    
    public List<NIC> getNICs() {
        return nics;
    }
    
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    
    public Memory getMemory() {
        return memory;
    }    
    
	public String getOsBoot() {
		return osBoot;
	}
	
	public void setOsBoot(String osBoot) {
		this.osBoot = osBoot;
	}
    
    public FQN getFQN() {
        if(veeReplicaFQN == null)
            veeReplicaFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return veeReplicaFQN;
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
        
        if(!(object instanceof VEEReplica))
            return false;
        
        return ((VEEReplica)object).internalId == internalId;
        
    }
	
	public void setMonitoringTime(long parseLong) {
		this.monitoringTime = parseLong;
	}
	
	public long getMonitoringTime() {
		return this.monitoringTime;
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
		
		for (DirectoryEntry de: getDisks())
			result.addAll(de.getDescendants());

		for (DirectoryEntry de: getCPUs())
			result.addAll(de.getDescendants());

		for (DirectoryEntry de: getNICs())
			result.addAll(de.getDescendants());
		
		result.add(memory);
		
		return result;
	}

	public void setDeployedOn(CloudProvider deployedOn) {
		this.deployedOn = deployedOn;
	}

	public CloudProvider getDeployedOn() {
		return deployedOn;
	}

	public void setDeployed(boolean deployed) {
		this.deployed = deployed;
	}

	public boolean isDeployed() {
		return deployed;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return running;
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
	
	public Set<String> getAvailablePowerOperations() {
		return availablePowerOperations;
	}
	
	public void updateReplicaInfo() {
		CloudProvider cp = getDeployedOn();
 
		if (cp!= null&&cp.isProviderAvailable()) {
			Document result=null;
			
			try {
				result = cp.getRemoteReplicaDescription(this);
			} catch (Throwable t) {
				logger.error("Replica could not be updated: " + t.getMessage());
				t.printStackTrace();
				return;
			}
			
			NodeList nl = result.getElementsByTagName("VApp");
			
			if (nl.getLength()>0) {
				Element vappElement = (Element) nl.item(0);
				String status = vappElement.getAttribute("status");

				if (StateType.valueOf(status) != null) {
					setVEEReplicaVmState(StateType.valueOf(status));
				}
				
				// Update available power operation availablePowerOperations
				NodeList linksList = result.getElementsByTagName("Link");
				
				availablePowerOperations.clear();
				for (int i=0; i < linksList.getLength(); i++) {
					Element linkElement = (Element) linksList.item(i);
					
					String relType = linkElement.getAttribute("rel");
					String linkValue = linkElement.getAttribute("href");
					
					if (relType.matches("power.*")&&linkValue!=null&!linkValue.isEmpty()) {
						availablePowerOperations.add(relType);
					}
				}
				
				NodeList identifierList  = vappElement.getElementsByTagNameNS(TCloudConstants.NAMESPACE_VSSD, "VirtualSystemIdentifier");
				
				if (identifierList.getLength() > 0) {
					Element identifierElement = (Element) identifierList.item(0);
					infraestructureId = identifierElement.getTextContent().trim();
				}
				
				// Update hardware info
				NodeList items = vappElement.getElementsByTagName("Item");
				
				for (int i =0; i < items.getLength(); i++) {
					Element item = (Element) items.item(i);
					
					NodeList resourceTypeList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "ResourceType");
					if (resourceTypeList.getLength()==0) {
						logger.error("Network item didn't had a ResourceType element.");
						continue;
					}
						
					Element resourceType = (Element) resourceTypeList.item(0);
					
					if (resourceType.getTextContent().trim().equals("10")) {
					
						NodeList connectionList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "Connection");
						if (connectionList.getLength()==0) {
							logger.error("Network item didn't had a Connection element.");
							continue;
						}
							
						Element connection = (Element) connectionList.item(0);
						
						NodeList addresList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "Address");
						Element address = null;
						if (addresList.getLength()==0) {
							logger.error("Network item didn't had a Address element.");
						} else {
							address = (Element) addresList.item(0);
						}
							
						NodeList ipList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_IEP, "IPv4Address");
						
						NodeList instanceIdList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "InstanceID");
						Element instanceId = null;
						if (instanceIdList.getLength()==0) {
							logger.error("Network item didn't had a InstanceId element.");
						} else {
							instanceId = (Element) instanceIdList.item(0);
						}
						
						for (NIC nic: nics) {
							if (nic.getNicConf().getNetwork().getName().trim().equals(connection.getTextContent().trim())) {
								
								if (address!=null)
									nic.setMacAddress(address.getTextContent().trim());
								
								if (instanceId !=null) 
									nic.setInstanceId(instanceId.getTextContent().trim());
								
								nic.getIPAddresses().clear();
								
								for (int j=0; j < ipList.getLength(); j++) {
									Element ip = (Element) ipList.item(j);
									nic.addIPAddress(ip.getTextContent());
								}
							}
						}
					}
				}
			} 
			
		} else {
			logger.error("The replica does not have an associated Cloud Provider.");
		}
	}

}
