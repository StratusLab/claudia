package com.telefonica.euro_iaas.placement.model.provider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Javier de la Puente Alonso
 *
 */

@Entity
public class Location {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	private String name;
	
	
	/**
	 * 
	 */
	public Location() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Location){
			Location loc = (Location) arg0;
			return this.getName().equals(loc.getName());
		} else if (arg0 instanceof String){
			return this.getName().equals((String)arg0);
		}
		return super.equals(arg0);
	}



}
