package com.telefonica.euro_iaas.placement.model.provider;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


/**
 * Virtual Execution Environment
 * 
 * @author jpuente
 *
 */
@Entity
public class VEE {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	private String name;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private CloudProvider cloudProvider;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private DiskConf diskConf;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private CPUConf cPUConf;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private MemoryConf memoryConf;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private NICConf nICConf;
	
	private int cost;
	
	/**
	 */
	public VEE() {
		super();
	}


	/**
	 * @param name
	 * @param memoryConf
	 * @param cost
	 */
	public VEE(String name, MemoryConf memoryConf, int cost) {
		super();
		this.name = name;
		this.memoryConf = memoryConf;
		this.cost = cost;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return the memoryConf
	 */
	public MemoryConf getMemoryConf() {
		return memoryConf;
	}

	/**
	 * @param memoryConf the memoryConf to set
	 */
	public void setMemoryConf(MemoryConf memoryConf) {
		this.memoryConf = memoryConf;
	}
	


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VEE [name=" + name + ", cost="+ cost +", memory=" + memoryConf +"]";
	}


	/**
	 * @return the diskConf
	 */
	public DiskConf getDiskConf() {
		return diskConf;
	}


	/**
	 * @param diskConf the diskConf to set
	 */
	public void setDiskConf(DiskConf diskConf) {
		this.diskConf = diskConf;
	}


	/**
	 * @return the cPUConf
	 */
	public CPUConf getcPUConf() {
		return cPUConf;
	}


	/**
	 * @param cPUConf the cPUConf to set
	 */
	public void setcPUConf(CPUConf cPUConf) {
		this.cPUConf = cPUConf;
	}


	/**
	 * @return the nICConf
	 */
	public NICConf getnICConf() {
		return nICConf;
	}


	/**
	 * @param nICConf the nICConf to set
	 */
	public void setnICConf(NICConf nICConf) {
		this.nICConf = nICConf;
	}


	/**
	 * @return the cloudProvider
	 */
	public CloudProvider getCloudProvider() {
		return cloudProvider;
	}


	/**
	 * @param cloudProvider the cloudProvider to set
	 */
	public void setCloudProvider(CloudProvider cloudProvider) {
		this.cloudProvider = cloudProvider;
	}	
	
}
