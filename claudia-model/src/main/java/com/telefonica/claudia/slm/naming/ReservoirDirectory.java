/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.naming;

import com.telefonica.claudia.slm.deployment.CPU;
import com.telefonica.claudia.slm.deployment.Disk;
import com.telefonica.claudia.slm.deployment.MeasuredValue;
import com.telefonica.claudia.slm.deployment.Memory;
import com.telefonica.claudia.slm.deployment.NIC;
import com.telefonica.claudia.slm.deployment.Network;
import com.telefonica.claudia.slm.deployment.Notification;
import com.telefonica.claudia.slm.deployment.RuleSet;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VDC;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;

@SuppressWarnings("unchecked")
public class ReservoirDirectory extends Directory {
    
    private static ReservoirDirectory resDirInstance = null;
    
    // FIXME: make the following line work and avoid the hardwiring (quick fix during London F2F :)
    //public static final String ROOT_NAME_SPACE = SMConfiguration.getInstance().getSiteRoot();
    public static String ROOT_NAME_SPACE = "es.tid";
    public static final String COSTUMERS_NAME_SPACE = "customers";
    public static final String SERVICES_NAME_SPACE = "services";
    public static final String NETWORKS_NAME_SPACE= "networks";
    public static final String VEES_NAME_SPACE = "vees";
    public static final String VEE_REPLICAS_NAME_SPACE = "replicas";
    public static final String CPUS_NAME_SPACE = "cpus";
    public static final String DISKS_NAME_SPACE = "disks";
    public static final String MEMORY_NAME_SPACE = "memory";
    public static final String NICS_NAME_SPACE = "networks";
    public static final String SERVICE_KPIS_NAME_SPACE = "kpis";
    public static final String RULES_KPIS_NAME_SPACE = "rules";
    
    private ReservoirDirectory() {
        super(new FQN(ROOT_NAME_SPACE));
    }
    
    public static ReservoirDirectory getInstance() {
        if(resDirInstance == null)
            resDirInstance = new ReservoirDirectory();
        return resDirInstance;
    }
    
    public void setRoot(String root) {
    	ROOT_NAME_SPACE = root;
    }
    
    public FQN buildFQN(VDC customer) {
    	FQN newFQN=new FQN(ROOT_NAME_SPACE + FQN.CONTEXT_SEPARATOR + COSTUMERS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + customer.getVdcName());  
    	
    	customer.setFqnString(newFQN.toString());
        return newFQN;     
    }
    
    /**
     * Generate the ServiceApplication FQN. 
     * 
     * @param service
     * 		The service whose FQN is to be build.		
     * 
     * @return
     */
    public FQN buildFQN(ServiceApplication service) {
    	FQN newFQN = new FQN(buildFQN(service.getVdc()) + FQN.CONTEXT_SEPARATOR + SERVICES_NAME_SPACE  + FQN.CONTEXT_SEPARATOR + service.getServiceName());
    	
    	service.setFqnString(newFQN.toString());
        return newFQN;
    }
    
    public FQN buildFQN(VEE vee) {
    	FQN newFQN = new FQN(buildFQN(vee.getServiceApplication()) + FQN.CONTEXT_SEPARATOR + VEES_NAME_SPACE + FQN.CONTEXT_SEPARATOR + vee.getVEEName());
    	
    	vee.setFqnString(newFQN.toString());
    	
        return newFQN;
    }
    
    public FQN buildFQN(VEEReplica veeReplica) {
    	FQN newFQN = new FQN(buildFQN(veeReplica.getParentVEE()) + FQN.CONTEXT_SEPARATOR + VEE_REPLICAS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + veeReplica.getId());
    	
    	veeReplica.setFqnString(newFQN.toString());
        return newFQN;
    }
    
    public FQN buildFQN(CPU cpu) {
    	FQN newFQN = new FQN(buildFQN(cpu.getRunningReplica()) + FQN.CONTEXT_SEPARATOR + CPUS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + cpu.getId());
    	
    	cpu.setFqnString(newFQN.toString());
        return newFQN;
    }
    
    public FQN buildFQN(Disk disk) {
    	FQN newFQN = new FQN(buildFQN(disk.getVEEReplica()) + FQN.CONTEXT_SEPARATOR + DISKS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + disk.getDiskConf().getFileSystem().getName());
    	
    	disk.setFqnString(newFQN.toString());
    	
        return newFQN;
    }
    
    public FQN buildFQN(Memory memory) {
    	FQN newFQN = new FQN(buildFQN(memory.getVEEReplica()) + FQN.CONTEXT_SEPARATOR + MEMORY_NAME_SPACE);
    	
    	memory.setFqnString(newFQN.toString());
    	
        return newFQN; 
    }
    
    public FQN buildFQN(NIC nic) {
    	FQN newFQN = new FQN(buildFQN(nic.getVeeReplica()) + FQN.CONTEXT_SEPARATOR + NICS_NAME_SPACE + FQN.CONTEXT_SEPARATOR +  nic.getId());
    	
    	nic.setFqnString(newFQN.toString());
    	
        return newFQN;
    }
    
    public FQN buildFQN(Network network) {
    	FQN newFQN = new FQN(buildFQN(network.getZone().getVdc()) + FQN.CONTEXT_SEPARATOR + NETWORKS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + network.getName());
    	
    	network.setFqnString(newFQN.toString());
    	
        return newFQN; 
    }
    
    public FQN buildFQN(MeasuredValue serviceKPI) {
    	FQN newFQN =new FQN(buildFQN(serviceKPI.getServiceApplication()) + FQN.CONTEXT_SEPARATOR + SERVICE_KPIS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + serviceKPI.getName());
    	
    	serviceKPI.setFqnString(newFQN.toString());
    	return newFQN;
    }
    
    public FQN buildFQN(Notification notification) {
    	FQN newFQN = new FQN(buildFQN(notification.getServiceApplication()) + FQN.CONTEXT_SEPARATOR + SERVICE_KPIS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + notification.getNotificationName());
    	
    	notification.setFqnString(newFQN.toString());
    	return newFQN;
    }
    
    public FQN buildFQN(RuleSet rule) {
    	FQN newFQN = new FQN(buildFQN(rule.getServiceApplication()) + FQN.CONTEXT_SEPARATOR + RULES_KPIS_NAME_SPACE + FQN.CONTEXT_SEPARATOR + rule.getName());
    	
    	rule.setFqnString(newFQN.toString());
    	return newFQN;
    }
}
