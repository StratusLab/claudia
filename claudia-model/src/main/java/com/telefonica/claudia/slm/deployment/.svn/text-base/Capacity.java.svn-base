package com.telefonica.claudia.slm.deployment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.deployment.HWComponent.HWType;

@Entity
public class Capacity implements PersistentObject {

	@Id
	@GeneratedValue
	public long internalId;
	
	private String units;
	private double allocated;
	private double used;
	
	private HWType type;
	
	private boolean unlimited;
	
	public void setUnits(String units) {
		this.units = units;
	}
	public String getUnits() {
		return units;
	}
	public void setAllocated(double allocated) {
		if (allocated <= 0)
			unlimited =true;
		else
			this.allocated = allocated;
	}
	public double getAllocated() {
		return allocated;
	}
	public void addUsed(double amount) {
		this.used += amount;
	}
	public void setUsed(double used) {
		this.used = used;
	}
	public double getUsed() {
		return used;
	}
	public void setType(HWType type) {
		this.type = type;
	}
	public HWType getType() {
		return type;
	}
	public long getObjectId() {
		return internalId;
	}
	public void setUnlimited(boolean unlimited) {
		this.unlimited = unlimited;
	}
	public boolean isUnlimited() {
		return unlimited;
	}
}
