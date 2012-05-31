/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */

package com.telefonica.claudia.slm.eventsBus.events;



/**
 * This event does not go through the Event Bus, but is directly launched by different SM components to the controller
 * Also this information is stored into the EventLog
 * 
 */

public class SMControlEvent extends Event {
	
    public static final int ELASTICITY_RULE_VIOLATION=0;
    public static final int NEW_RULE_ADDED=1;
    public static final int RULE_CHANGED=2;
    public static final int START_SERVICE=3;
    public static final int UNDEPLOY_SERVICE=4;
    public static final int GET_MONITORING_DATA=5;

    private static final long serialVersionUID = 1L;
    
    private String fqnString = null; // FQN of the Sm Event origin
    private String action =null;       // depending on the event type, the SM component may suggest an action to be taken by the controller
    private int controlEventType;
    private String serviceFQN = null;
    
    
    
    public SMControlEvent (long t_0, long delta_t, String fqnString, String action, String serviceFQN) {
        super(t_0, delta_t, EventType.SM_CONTROL_EVENT);
        this.fqnString = fqnString;
        this.action = action;
        this.serviceFQN= serviceFQN;
    }
    
    public String getOrigin() {
        return fqnString;
    }
    
    public String getServiceFQN(){
        return serviceFQN;
    }
    
    public int getControlEventType(){
        return controlEventType;
    }
    
    public void setControlEventType(int type){
        controlEventType=type;
    }
    
    public String getSuggestedAction() {
        return action;
    }
    
    @Override
    public String toString() {
        return super.toString() + " origin: " + fqnString + " suggested action: " + action;
    }        
}
