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

import javax.persistence.Entity;

import com.telefonica.claudia.slm.monitoring.MeasurableElement.MeasureDescriptor;
import com.telefonica.claudia.slm.naming.FQN;

@Entity
public class CPUConf extends HWComponent {    
	
    private double capacity = 0;    
    
    public CPUConf() {
    	this.setType(HWType.CPU);
    }
    
    public CPUConf(int capacity) {
        this.capacity = capacity;
        this.setType(HWType.CPU);
    }
    
    public double getCapacity() {
        return capacity;
    }
    
	public MeasureDescriptor calculateMeasureDescriptorValues(FQN element, String measureId) {
		
		MeasureDescriptor result = new MeasureDescriptor();
		
		result.typeId= "cpuUsage";
		result.description= "Percentage of CPU used";
		result.valueType= "xs:decimal";
		result.min= "0";
		result.max= "100";
		
		return result;
	}
}
