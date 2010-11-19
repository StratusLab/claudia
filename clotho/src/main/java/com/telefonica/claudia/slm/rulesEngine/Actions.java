/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.rulesEngine;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.deployment.Rule;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.ServiceKPI;
import com.telefonica.claudia.slm.eventsBus.events.SMControlEvent;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.slm.lifecyclemanager.LifecycleController;
import com.telefonica.claudia.slm.naming.FQN;


public class Actions {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.slm.rulesEngine.Actions");
	
    private Date stamp;
    private SMControlEvent controlEvent;
    private LifecycleController lcc;
    private ServiceApplication sap;
    private FSM fsm;
    
    Actions(LifecycleController lcc, ServiceApplication sap, FSM fsm){
        this.lcc=lcc;
        this.sap=sap;
        this.fsm=fsm;
    }

     public void createReplica(String value) {
            //prepare an appropriate event to be put into the buffer
            stamp=new Date();
            FQN name =sap.getFQN();
            controlEvent = new SMControlEvent(stamp.getTime(), 0, null, "createReplica("+value+",1)", name.toString());
            controlEvent.setControlEventType(SMControlEvent.ELASTICITY_RULE_VIOLATION);
             //putEvent in the bus for the FSM to process it
            lcc.putEvent(controlEvent);  
            
            log.info("ACTION TRIGGERED: Create Replica event queued for value "+value);
     }

    
     public void removeReplica(String value) {
                 
            //prepare an appropriate event to be put into the buffer
            stamp=new Date();
            FQN name =sap.getFQN();
            controlEvent = new SMControlEvent(stamp.getTime(), 0, null, "removeReplica("+value+")", name.toString());
            controlEvent.setControlEventType(SMControlEvent.ELASTICITY_RULE_VIOLATION);
             //putEvent in the bus for the FSM to process it
            lcc.putEvent(controlEvent);  
            
          
            log.info("ACTION TRIGGERED: Remove Replica event queued for value "+value);
    }
     
    /**
     * Return the number of replicas of the selected VEEs that are actually running. In case this number
     * is zero, return one insted (to avoid division by zero errors in Drools).
     * 
     * @param veeFqn
     * @return
     */
    public int getAmount(String veeFqn){
    	log.debug("Asking for amount of VEE [" + veeFqn + "]: ");
    	int amount = (fsm.getAmount(veeFqn));
    	
    	log.debug("Amount of VEE [" + veeFqn + "]: " + amount + " ");
        return (amount>0)?amount:1;
    }
    
    public double getAverage(String kpiFQN, double value){
    	log.debug("Asking for average of VEE [" + kpiFQN + "] with new value " + value);
    	Set<Rule> rules= sap.getServiceRules();
    	ServiceKPI kpi;
    	Rule rule;
    	double newData;
    	double average=0.0;
    	 for (Iterator<Rule> ruleIter = rules.iterator(); ruleIter.hasNext(); ) {
    		 rule=ruleIter.next();
    		 //univocal relationship between rule and kpi in Y2 rules
    		 kpi=rule.getKpi();
    		 if (kpi.getFQN().toString().equalsIgnoreCase(kpiFQN)){
    			 newData=value;
    			 // update average before analyzing
    			 rule.put(newData);
    			 average=rule.getAverage();
    		 }
    	 }
    	 
    	 log.debug("Average for KPI [" + kpiFQN + "]: " + value);
    	 return average;
    }
}