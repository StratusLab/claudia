package com.telefonica.euro_iaas.placement.model.application;

import com.telefonica.euro_iaas.placement.model.provider.CPUConf;
import com.telefonica.euro_iaas.placement.model.provider.DiskConf;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf;
import com.telefonica.euro_iaas.placement.model.provider.NICConf;

/**
 * Virtual Execution Environment
 * 
 * @author jpuente
 *
 */
public class VEERequired {
	
	private ServiceApplication serviceApplication;	
	
	private String name;
	
	private DiskConf diskConf;
	
	private CPUConf cPUConf;
	
	private MemoryConf memoryConf;
	
	private NICConf nICConf;
	
	
	/**
	 */
	public VEERequired() {
		super();
	}


	/**
	 * @param name
	 * @param memoryConf
	 * @param cost
	 */
	public VEERequired(String name, MemoryConf memoryConf, int cost) {
		super();
		this.name = name;
		this.memoryConf = memoryConf;
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
	 * @return the serviceApplication
	 */
	public ServiceApplication getServiceApplication() {
		return serviceApplication;
	}


	/**
	 * @param serviceApplication the serviceApplication to set
	 */
	public void setServiceApplication(ServiceApplication serviceApplication) {
		this.serviceApplication = serviceApplication;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VEERequired ["
				+ ", name=" + name + ", diskConf=" + diskConf + ", cPUConf="
				+ cPUConf + ", memoryConf=" + memoryConf + ", nICConf="
				+ nICConf + "]";
	}	
	

	
}
