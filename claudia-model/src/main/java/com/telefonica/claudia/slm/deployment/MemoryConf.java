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
import com.telefonica.claudia.smi.DataTypesUtils;

@Entity
public class MemoryConf extends HWComponent {
	
    private double capacity = 0; /* capacity units in MegaBytes (MB) */
    
    public MemoryConf() {
        this.setType(HWType.MEMORY);
    }
    
    public MemoryConf(double capacity) {
        this.capacity = capacity;
        this.setType(HWType.MEMORY);
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
	public MeasureDescriptor calculateMeasureDescriptorValues(FQN element, String measureId) {
		
		MeasureDescriptor result = new MeasureDescriptor();

		result.typeId= "memoryUsage";
		result.description= "MB of memory used";
		result.valueType= DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT;
		result.min=  "0";
		result.max=  String.valueOf(getCapacity());
		
		return result;
	}
}
