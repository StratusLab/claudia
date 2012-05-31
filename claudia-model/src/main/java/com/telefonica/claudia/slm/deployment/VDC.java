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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.deployment.HWComponent.HWType;
import com.telefonica.claudia.slm.deployment.Network.NetworkModeType;
import com.telefonica.claudia.slm.deployment.Zone.ZoneType;
import com.telefonica.claudia.slm.monitoring.MeasurableElement;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.smi.DataTypesUtils;

@Entity
public class VDC  extends DirectoryEntry implements PersistentObject, MeasurableElement {
    
	private static final double DEFAULT_MEMORY_ALLOCATION = -1;

	private static final String DEFAULT_CPU_UNITS = "# CPUs";

	private static final double DEFAULT_CPU_ALLOCATION = -1;

	private static final double DEFAULT_DISK_ALLOCATION = -1;

	private static Logger logger = Logger.getLogger("VDC");
	
	@Id
	@GeneratedValue
	public long internalId;
	
	private long tcloudId;
	
	@Basic
    private String vdcName = null;
	
	private String description = null;
    
	//@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Transient
	private FQN vdcFQN = null;
    
    @OneToMany(mappedBy="vdc", cascade=CascadeType.ALL)
    private Set<ServiceApplication> services = new HashSet<ServiceApplication>();
    
    @OneToMany(mappedBy="vdc", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Zone> zones = new HashSet<Zone>();
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Capacity cpuCapacity;
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Capacity diskCapacity;
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Capacity memoryCapacity;
    
    public VDC() {
    }
    
    public VDC(String vdcName) {
    	if(vdcName == null)
    		throw new IllegalArgumentException("VDC name cannot be null");
    	
        this.vdcName = vdcName;
    }
    
    @SuppressWarnings("unchecked")
	public static VDC createVDC(String instantiateOVFParams, String org) throws IOException {
    	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		VDC resultVDC = new VDC();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(instantiateOVFParams.getBytes()));
			
			Element root= (Element) doc.getFirstChild();
			
			resultVDC.tcloudId = Integer.parseInt(root.getAttribute("Id"));
			resultVDC.vdcName = root.getAttribute("name");
			
			NodeList descriptionList = root.getElementsByTagName("Description");
			
			if (descriptionList.getLength() > 0) {
				resultVDC.description = ((Element)descriptionList.item(0)).getTextContent();
			}
			
			NodeList topologyList = root.getElementsByTagName("Topology");
			
			if (topologyList.getLength()>0) {
				Element topology = (Element) topologyList.item(0);
				
				NodeList listZones= topology.getElementsByTagName("Zone");
				
				for (int i=0; i < listZones.getLength(); i++) {
					Element zone = (Element) listZones.item(i);
					
					Zone tmpZone = new Zone();
					
					tmpZone.setVdc(resultVDC);
					resultVDC.registerZone(tmpZone);
					
					tmpZone.setZoneType(ZoneType.valueOf(zone.getAttribute("type").replace('-', '_').toUpperCase()));
					
					NodeList netList = zone.getElementsByTagName("VirtualNetwork");
					
					for (int j=0; j < netList.getLength(); j++) {
						
						Element network = (Element) netList.item(j);
						String name = network.getAttribute("name");
						
						Network tmpNet = new Network(name, tmpZone);
						
						NodeList modeList = network.getElementsByTagName("Mode");
						if (modeList.getLength() > 0) {
							Element mode = (Element) modeList.item(0);
							tmpNet.setNetworkMode(NetworkModeType.valueOf(mode.getTextContent().trim().toUpperCase()));
						}
						
						switch(tmpNet.getNetworkMode()) {
							case ISOLATED:
								tmpNet.setNetworkMode(NetworkModeType.ISOLATED);
								break;
							
							default:
								tmpNet.setNetworkMode(NetworkModeType.ROUTED);
						}
						
						tmpNet.setTcloudId(Integer.parseInt(network.getAttribute("id")));
						
						NodeList sizeList = network.getElementsByTagName("Size");
						if (sizeList.getLength() > 0) {
							Element size = (Element) sizeList.item(0);
							tmpNet.setSize(Integer.parseInt(size.getTextContent().trim()));
						}
						
						descriptionList = network.getElementsByTagName("Description");
						if (descriptionList.getLength() > 0) {
							Element description = (Element) descriptionList.item(0);
							tmpNet.setDescription(description.getTextContent().trim());
						}
						
						NodeList bandwidthList = network.getElementsByTagName("Bandwidth");
						if (bandwidthList.getLength() > 0) {
							Element bandwidth = (Element) bandwidthList.item(0);
							tmpNet.setBandwidth(Long.parseLong(bandwidth.getTextContent().trim()));
							
							if (bandwidth.hasAttribute("units"))
								tmpNet.setBandwidthUnits(bandwidth.getAttribute("units"));
						}
						tmpZone.registerNetwork(tmpNet);
					}	
				}
				
			} else {
				throw new IllegalArgumentException("Topology element is mandatory");
			}
			
			NodeList computeCapacityList = root.getElementsByTagName("ComputeCapacity");
			
			if (computeCapacityList.getLength()>0) {
				Element computeCapacity = (Element) computeCapacityList.item(0);
				
				NodeList listCpus= computeCapacity.getElementsByTagName("Cpu");
				
				String cpuUnits;
				double cpuValue;
				
				if (listCpus.getLength()>0) {
					Element cpu = (Element) listCpus.item(0);
					Element allocated = (Element) cpu.getElementsByTagName("Limit").item(0);
					Element units = (Element) cpu.getElementsByTagName("Units").item(0);
					
					cpuUnits = units.getTextContent();
					
			    	try {
			    		cpuValue = Double.parseDouble(allocated.getTextContent());
			    	} catch (Throwable t) {
			    		throw new IllegalArgumentException("Error parsing cpu capacity: " + allocated.getTextContent());
			    	}
			    	
				} else {
					cpuUnits = DEFAULT_CPU_UNITS;
					cpuValue = DEFAULT_CPU_ALLOCATION;
				}
				
		    	Capacity ct = new Capacity(); 
		    	
		    	ct.setAllocated(cpuValue);
		    	ct.setUsed(0.0);
		    	ct.setUnits(cpuUnits);
		    	ct.setType(HWType.CPU);
		    	
		    	resultVDC.cpuCapacity = ct;
		    	
				NodeList listMemory= computeCapacity.getElementsByTagName("Memory");
				
				double memValue;
				
				if (listMemory.getLength()>0) {
					Element memory = (Element) listMemory.item(0);
					Element allocated = (Element) memory.getElementsByTagName("Limit").item(0);
					Element units = (Element) memory.getElementsByTagName("Units").item(0);
					
			    	try {
			    		memValue= Double.parseDouble(allocated.getTextContent())*DataTypesUtils.getStorageUnitConversion(units.getTextContent());
			    	} catch (Throwable t) {
			    		throw new IllegalArgumentException("Error parsing memory capacity: " + allocated.getTextContent());
			    	}
				} else {
					memValue= DEFAULT_MEMORY_ALLOCATION;
				}
				
		    	ct = new Capacity(); 
		    	ct.setUnits(DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT);
		    	ct.setAllocated(memValue);
		    	ct.setUsed(0.0);
		    	ct.setType(HWType.MEMORY);
		    	
		    	resultVDC.memoryCapacity = ct;
		    	
			} else {
				Capacity ct = new Capacity(); 
		    	ct.setAllocated(DEFAULT_CPU_ALLOCATION);
		    	ct.setUsed(0.0);
		    	ct.setUnits(DEFAULT_CPU_UNITS);
		    	ct.setType(HWType.CPU);
		    	
		    	resultVDC.cpuCapacity = ct;
		    	
		    	ct = new Capacity(); 
		    	ct.setAllocated(DEFAULT_MEMORY_ALLOCATION);
		    	ct.setUsed(0.0);
		    	ct.setUnits(DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT);
		    	ct.setType(HWType.MEMORY);
		    	
		    	resultVDC.memoryCapacity = ct;
			}

			NodeList storageCapacityList = root.getElementsByTagName("StorageCapacity");
			
			if (storageCapacityList.getLength()>0) {
				Element storageCapacity = (Element) storageCapacityList.item(0);
				
				NodeList listDisks= storageCapacity.getElementsByTagName("Disk");
				
				Element disk = (Element) listDisks.item(0);
				Element allocated = (Element) disk.getElementsByTagName("Limit").item(0);
				Element units = (Element) disk.getElementsByTagName("Units").item(0);
				
				Capacity ct = new Capacity(); 
				ct.setUnits(DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT);
				try {
					ct.setAllocated(Double.parseDouble(allocated.getTextContent())*DataTypesUtils.getStorageUnitConversion(units.getTextContent()));
				} catch (Throwable t) {
					throw new IllegalArgumentException("Error parsing storage capacity: " + allocated.getTextContent());
				}
				
		    	ct.setUsed(0.0);
		    	ct.setType(HWType.DISK);
		    	
				resultVDC.diskCapacity=ct;
			} else {
				Capacity ct = new Capacity(); 
		    	ct.setAllocated(DEFAULT_DISK_ALLOCATION);
		    	ct.setUsed(0.0);
		    	ct.setUnits(DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT);
		    	ct.setType(HWType.DISK);
		    	
				resultVDC.diskCapacity=ct;				
			}

			if (ReservoirDirectory.getInstance().isNameRegistered(resultVDC.getFQN())) {
				 throw new IllegalArgumentException("The VDC already exists");
			} else {
				ReservoirDirectory.getInstance().registerObject(resultVDC.getFQN(), resultVDC);
				logger.info("New VDC: " + resultVDC.toString() + ", registering in directory");	
			}
			
			return resultVDC;
			
		} catch (ParserConfigurationException e) {
			logger.error("Parser Configuration Error: " + e.getMessage());
			throw new IOException ("Parser Configuration Error", e);
		} catch (SAXException e) {
			logger.error("Parse error reading the answer: " + e.getMessage());
			throw new IOException ("XML Parse error", e);
		}
    }
    
    public String getVdcName(){
        return vdcName;
    }
    
    public Set<ServiceApplication> getServices() {
        return services;
    }
    
    public void registerService(ServiceApplication service) {
        
        if(service == null)
            throw new IllegalArgumentException("Cannot register null service");
        
        if(!service.getVdc().equals(this))       
            throw new IllegalArgumentException("Trying to register Service " + service + " on a different customer " + this);
        
        services.add(service);
    }
    
    public boolean isServiceRegistered(ServiceApplication service) {
        return services.contains(service);
    }
    
    public void unregisterService(ServiceApplication service) {
        services.remove(service);
    }
    
    public FQN getFQN(){
        if(vdcFQN == null)
            vdcFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return vdcFQN;
    }
    
    @Override
    public String toString() {
        return getFQN().toString();
    }
    
    @Override
    public int hashCode() {
        return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof VDC))
            return false;
        
        return ((VDC)object).internalId == internalId;
        
    }   
    
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		
		return result;
	}

    public void registerZone(Zone zone) {
        
        if(zone == null)
            throw new IllegalArgumentException("Cannot register null network");
        
        if(!zone.getVdc().equals(this))
            throw new IllegalArgumentException("Trying to register network " + zone + " on a different Service " + this);
    	
        zones.add(zone);
    }
    
    public boolean isZoneRegistered(Zone zone) {
    	return zones.contains(zone);
    }
    
    public void unregisterZone(Zone zone) {
    	zones.remove(zone);
    }
	
    /**
     * @param netName
     * @return the Network class correspondig to that name, null if no Netwokr can be found
     */
    public Network getNetworkByName(String netName) {

    	for (Iterator<Zone> itz= zones.iterator(); itz.hasNext(); ) {
    		Zone z= itz.next();
    		
	    	for (Iterator<Network> it = z.getNetworks().iterator(); it.hasNext(); ) {
	    		Network net = it.next();
	    		if (net.getName().equals(netName)) {
	    			return net;
	    		}
	    	}
    	}
    	return null;
    }
    
    public Set<Zone> getZones() {
    	return zones;
    }
    
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTcloudId(long tcloudId) {
		this.tcloudId = tcloudId;
	}

	public long getTcloudId() {
		return tcloudId;
	}
	
	public Capacity getCpuCapacity() {
		return cpuCapacity;
	}
	
	public void setCpuCapacity(double capacity) {	
		cpuCapacity.setAllocated(capacity);
	}

	public Capacity getDiskCapacity() {
		return diskCapacity;
	}
	
	public void setDiskCapacity(double capacity) {	
		diskCapacity.setAllocated(capacity);
	}

	public Capacity getMemoryCapacity() {
		return memoryCapacity;
	}
	
	public void setMemoryCapacity(double capacity) {	
		memoryCapacity.setAllocated(capacity);
	}
	
	public long getObjectId() {
		return internalId;
	}



}
