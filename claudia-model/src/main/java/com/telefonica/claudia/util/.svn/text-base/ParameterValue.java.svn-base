package com.telefonica.claudia.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class ParameterValue implements PersistentObject {
	
	@Id
	@GeneratedValue
	long id;

	public String parameterKey;
	
	@Column(columnDefinition = "TEXT")
	public String parameterValue;
	
	public ParameterValue() {}
	
	public ParameterValue(String key, String value) {
		this.parameterValue=value;
		this.parameterKey=key;
	}

	public long getObjectId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return parameterKey.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParameterValue) 
			return parameterKey.equals(((ParameterValue)obj).parameterKey);
		else
			return false;
	}
}
