package com.telefonica.euro_iaas.placement.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;

@XmlRootElement
public class CloudProviderList {
	
	private List<CloudProvider> relations;
	
	public CloudProviderList() {}

	/**
	 * @return the relations
	 */
	@XmlElement(name="CloudProvider")
	public List<CloudProvider> getRelations() {
		return relations;
	}

	/**
	 * @param relations the relations to set
	 */
	public void setRelations(List<CloudProvider> relations) {
		this.relations = relations;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CloudProviderList [relations=" + relations + "]";
	}
	
	
}
