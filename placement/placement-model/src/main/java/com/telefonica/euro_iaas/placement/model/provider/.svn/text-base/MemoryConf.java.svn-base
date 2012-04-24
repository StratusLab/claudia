package com.telefonica.euro_iaas.placement.model.provider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MemoryConf implements Comparable<MemoryConf>{
	
	public enum MemoryUnit {MB, GB};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	private MemoryUnit memoryUnit;
	private int quantity;

	public MemoryConf() {
		super();
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
	public int compareTo(MemoryConf o) {
		if (quantity > o.quantity) return 1;
		else if (quantity < o.quantity) return -1;
		else return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MemoryConf [quantity=" + quantity + "]";
	}

	/**
	 * @return the memoryUnit
	 */
	public MemoryUnit getMemoryUnit() {
		return memoryUnit;
	}

	/**
	 * @param memoryUnit the memoryUnit to set
	 */
	public void setMemoryUnit(MemoryUnit memoryUnit) {
		this.memoryUnit = memoryUnit;
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

}
