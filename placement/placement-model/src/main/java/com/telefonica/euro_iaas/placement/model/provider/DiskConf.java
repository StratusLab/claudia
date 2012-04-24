package com.telefonica.euro_iaas.placement.model.provider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DiskConf implements Comparable<DiskConf>{
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	private int quantity;

	
	public DiskConf() {
		super();
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

	@Override
	public int compareTo(DiskConf arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "DiskConf [id=" + id + ", quantity=" + quantity + "]";
	}



}
