/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.deployment;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.ForceDiscriminator;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
@ForceDiscriminator
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class MeasuredValue extends DirectoryEntry implements PersistentObject {
	
    private static final int WINDOW_SIZE = 8;
    
    @Transient
    protected FQN measureFQN = null;
	protected double measureValue;
	protected String measureName;
	
	@Id
	@GeneratedValue
	public long internalId;
	
	@ManyToOne
    private ServiceApplication measuredApplication = null;
	
    /**
     * Buffer of measure values. The buffer will have a fixed capacity, and all
     * the reads and writes will be made on the head position.
     */
    private double[] buffer;
    
    /**
     *  Reading head for the buffer.
     */
    private int bufferHead = 0;
    
	public MeasuredValue() {};
	
    public MeasuredValue(ServiceApplication serviceApplication, String name) {
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application cannot be null");
    	if(name == null)
    		throw new IllegalArgumentException("Measured value name cannot be null");
    	this.measuredApplication = serviceApplication;
    	this.measureName = name;
    	buffer = new double[(int) WINDOW_SIZE];
    }
	
    public ServiceApplication getServiceApplication() {
    	return measuredApplication;
    }
    
    public FQN getFQN() {
    	if(measureFQN == null)
    		measureFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return measureFQN;
    }
	
    public void setValue(double value) {
        bufferHead = (++bufferHead)%buffer.length;
        buffer[bufferHead] =value;
    	
        this.measureValue= getAverage();
    }
    
    public double getAverage() {
        double average = 0.0;
        for (int i = 0; i < buffer.length; i++) {
                average += buffer[i];
        }
        return (average / buffer.length);
}

    
    public double getValue() {
    	return measureValue;
    }
    
    public String getName() {
    	return measureName;
    }
    
    @Override
    public String toString() {
    	return getFQN().toString();
    }

    @Override
    public int hashCode() {
    	return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
    	
    	if(object == null)
    		return false;
    	
    	if(!(object instanceof MeasuredValue))
    		return false;
    	
    	return ((MeasuredValue)object).getFQN().equals(getFQN());
    	
    }
    
	public Set<Object> getDescendants() {
		Set<Object> result = new HashSet<Object>();
		result.add(this);
		return result;
	}
	
	public long getObjectId() {
		return internalId;
	}
}
