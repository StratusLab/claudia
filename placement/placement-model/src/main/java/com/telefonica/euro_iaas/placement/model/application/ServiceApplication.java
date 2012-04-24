package com.telefonica.euro_iaas.placement.model.application;

import java.util.Set;

/**
 * @author Javier de la Puente Alonso
 *
 */
public class ServiceApplication {
	
	private VDC vDC;
	
	private Set<VEERequired> veesRequired;



	/**
	 * @return the vDC
	 */
	public VDC getvDC() {
		return vDC;
	}

	/**
	 * @param vDC the vDC to set
	 */
	public void setvDC(VDC vDC) {
		this.vDC = vDC;
	}

	/**
	 * @return the veesRequired
	 */
	public Set<VEERequired> getVeesRequired() {
		return veesRequired;
	}

	/**
	 * @param veesRequired the veesRequired to set
	 */
	public void setVeesRequired(Set<VEERequired> veesRequired) {
		this.veesRequired = veesRequired;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceApplication [vDC=" + (vDC==null?vDC:vDC.getName()) + ", veesRequired="
				+ veesRequired + "]";
	}




}
