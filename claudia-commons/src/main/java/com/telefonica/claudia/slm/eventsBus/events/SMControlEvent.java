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

package com.telefonica.claudia.slm.eventsBus.events;

import com.telefonica.claudia.slm.eventsBus.events.*;


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