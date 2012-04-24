package com.telefonica.euro_iaas.placement.model;

import com.telefonica.euro_iaas.placement.model.application.VEERequired;
import com.telefonica.euro_iaas.placement.model.provider.VEE;

/**
 * Mapping between the VEE of a service with a provider
 * 
 * @author Javier de la Puente Alonso
 *
 */
public class VEEMapping {
	private VEE veeProvider;
	private VEERequired veeService;
	private boolean bestInCP;
	private boolean bestGlobal;

	/**
	 * @return the bestInCP
	 */
	public boolean isBestInCP() {
		return bestInCP;
	}


	/**
	 * @param bestInCP the bestInCP to set
	 */
	public void setBestInCP(boolean bestInCP) {
		this.bestInCP = bestInCP;
	}


	/**
	 * 
	 */
	public VEEMapping() {
		super();
	}


	/**
	 * @param veeProvider
	 * @param veeService
	 * @param valid
	 */
	public VEEMapping(VEE veeProvider, VEERequired veeService) {
		super();
		this.veeProvider = veeProvider;
		this.veeService = veeService;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VEEMapping [veeProvider=" + veeProvider + ", veeService="
				+ veeService + ", bestInCP=" + bestInCP + ", bestGlobal=" + bestGlobal + "]";
	}


	/**
	 * @return the veeProvider
	 */
	public VEE getVeeProvider() {
		return veeProvider;
	}


	/**
	 * @param veeProvider the veeProvider to set
	 */
	public void setVeeProvider(VEE veeProvider) {
		this.veeProvider = veeProvider;
	}


	/**
	 * @return the veeService
	 */
	public VEERequired getVeeService() {
		return veeService;
	}


	/**
	 * @param veeService the veeService to set
	 */
	public void setVeeService(VEERequired veeService) {
		this.veeService = veeService;
	}


	/**
	 * @return the bestGlobal
	 */
	public boolean isBestGlobal() {
		return bestGlobal;
	}


	/**
	 * @param bestGlobal the bestGlobal to set
	 */
	public void setBestGlobal(boolean bestGlobal) {
		this.bestGlobal = bestGlobal;
	}





	
}
