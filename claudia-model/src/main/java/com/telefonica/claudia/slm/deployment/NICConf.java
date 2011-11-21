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
import javax.persistence.OneToOne;

@Entity
public class NICConf extends HWComponent {
	
	@OneToOne
    private Network network = null;
    
	public NICConf() {
		this.setType(HWType.NIC);
	}
	
    public NICConf(Network network) {
    	if(network == null)
    		throw new IllegalArgumentException("Network cannot be null");
    	
        this.network = network;
        this.setType(HWType.NIC);
    }
    
    public Network getNetwork() {
        return network;
    }

	@Override
	public double getCapacity() {
		return 0;
	}            
    
}