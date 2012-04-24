package com.telefonica.euro_iaas.placement.model.provider;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * @author jpuente
 *
 */
@XmlRootElement
@Entity
public class CloudProvider{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String name;
	
	private String uri;
	
	private int totalCost;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Location location;
	
	@XmlTransient
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Set<VEE> vees;
	
	/**
	 * 
	 */
	public CloudProvider() {
		super();
	}

	/**
	 * @param name
	 */
	public CloudProvider(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the vees
	 */
	@XmlTransient
	public Set<VEE> getVees() {
		return vees;
	}

	/**
	 * @param vees the vees to set
	 */
	public void setVees(Set<VEE> vees) {
		this.vees = vees;
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

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the totalCost
	 */
	public int getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CloudProvider [id=" + id + ", name=" + name + ", uri=" + uri
				+ ", totalCost=" + totalCost + ", location=" + location + "]";
	}
	
	
}
