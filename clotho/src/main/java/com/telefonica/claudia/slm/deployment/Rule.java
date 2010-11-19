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

package com.telefonica.claudia.slm.deployment;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;


/**
 * Class representing old style rules
 * @author tid
 *
 */
@Entity
public class Rule implements DirectoryEntry {
	
	@Id
	@GeneratedValue
	public long internalId;
	
	private String KPIName="";
	private String name="";
	private String eventType="";
	
    @ManyToOne
	private ServiceApplication serviceApplication = null;
	
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
	private FQN ruleFQN = null;  
	
	// Y1 style elements
    
    @Basic
    private String checkPeriod="";
    private String condition="";
    private String action="";  
    
    // Y2 elements
    private String associatedVee=""; 
    
    /**
     * Buffer of measure values. The buffer will have a fixed capacity, and all
     * the reads and writes will be made on the head position.
     */
    private double[] buffer;
    
    /**
     *	Reading head for the buffer. 
     */
    private int bufferHead = 0;
    private double window = 0.0;
	private double frequency = 0.0;
	private double quota = 0.0;
	private double tolerance = 0.0;
    
    @ManyToOne
	private ServiceKPI kpi;	
   
    public Rule() {}
    
    public Rule(ServiceApplication serviceApplication, String checkPeriod, String condition, String action){    	
        this.action=action;
        this.checkPeriod=checkPeriod;
        this.condition=condition; 
        this.serviceApplication=serviceApplication;
    }
    
    public Rule(ServiceApplication serviceApplication){
		this.serviceApplication=serviceApplication;
	}
    
    public void setAction(String action){this.action=action;}
    public String getAction(){return action;}
    public void setCheckPeriod(String checkPeriod){this.checkPeriod=checkPeriod;}
    public String getCheckPeriod(){return checkPeriod;}
    public void setCondition(String condition){this.condition=condition;}
    public String getCondition(){return condition;}   
 
    /**
	 * This method has to be invoked by the parser before actually considering
	 * the KPI object is usable
	 * 
	 * @param window
	 * @param frequency
	 * @param quota 
	 * @param associatedVee
	 */
	public void configure(double window, double frequency, double quota, String associatedVee, ServiceKPI kpi, double tolerance) {
		this.window = window;
		this.frequency = frequency;
		this.quota=quota;
		this.associatedVee=associatedVee;
		this.kpi=kpi;
		this.tolerance=tolerance;
		buffer = new double[(int) window];
	}     
	
	/**
	 * 
	 * Configure the rule for the the given KPI. This method 
	 * should be called once all the other parameters have been fullfilled with
	 * its appropiate setters.
	 * 
	 * @param kpi
	 */
	public void configure(ServiceKPI kpi) {
		this.kpi=kpi;
		buffer = new double[(int) window];
	}
	
	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public String buildRule(boolean up){
		String bodyUp, bodyDown, rule;
		String actionUp= "actions.createReplica(\""+associatedVee+"\")";
		String actionDown="actions.removeReplica(\""+associatedVee+"\")";
		String header="package com.telefonica.claudia.slm.rulesEngine;"+"\n"+"// Imports for all types of events should be here..."+"\n"+"import com.telefonica.claudia.slm.eventsBus.events.VeeHwMeasureEvent;"+"\n"+"import com.telefonica.claudia.slm.eventsBus.events.AgentMeasureEvent;"+"\n"+"import com.telefonica.claudia.slm.eventsBus.events.ProbeMeasureEvent;"+"\n"+"global com.telefonica.claudia.slm.rulesEngine.Actions actions; \n";
		
        if (up){
        	// add rule name	        
            bodyUp="rule"+" \""+ruleFQN.toString();
            // add other headers
            bodyUp=bodyUp+"\""+"\n"+"lock-on-active true"+"\n"+"salience 100"+"\n"+"when"+"\n";
            // add then part of the rule	       
            bodyUp=bodyUp+"   event : "+eventType.toString()+" (measure == \""+kpi.getFQN().toString()+"\" , "+"eval(actions.getAverage(\""+kpi.getFQN().toString()+"\",value)/(actions.getAmount(\""+ associatedVee+"\"))> "+Double.toString(quota+(quota*tolerance)/100) +" )); \n"+"then \n";
        	bodyUp=bodyUp+actionUp+";\n"+"end";
        	rule=header+bodyUp;
        	System.out.println(header+bodyUp);
        }else{
        	// add rule name	        
            bodyDown="rule"+" \""+ruleFQN.toString() + "b";
            // add other headers
            bodyDown=bodyDown+"\""+"\n"+"lock-on-active true"+"\n"+"salience 100"+"\n"+"when"+"\n";
            // add then part of the rule	       
            bodyDown=bodyDown+"   event : "+eventType.toString()+" (measure == \""+kpi.getFQN().toString()+"\" , "+"eval(actions.getAverage(\""+kpi.getFQN().toString()+"\",value)/(actions.getAmount(\""+ associatedVee+"\"))< "+Double.toString(quota-(quota*tolerance)/100)+" )); \n"+"then \n";
            bodyDown=bodyDown+actionDown+";\n"+"end";
            rule=header+bodyDown;
            System.out.println(header+bodyDown);
        }
        return rule;	    
	}
    
    public String getAssociatedVee() {
		return associatedVee;
	}

    
	public void setAssociatedVee(String associatedVee) {
		this.associatedVee = associatedVee;
	}	

	/**
	 * Put the received measure inside the buffer, in the head position.
	 * 
	 * @param receivedKpi
	 * 		Measure value to store
	 */
	public void put(double receivedKpi) {
		
		bufferHead = (++bufferHead)%buffer.length;
		buffer[bufferHead] =receivedKpi;
	}

	public double getAverage() {
		double average = 0.0;
		for (int i = 0; i < buffer.length; i++) {
			average += buffer[i];
		}
		return (average / buffer.length);
	}

	public double[] getBuffer() {
		return buffer;
	}

	public void setBuffer(double[] buffer) {
		this.buffer = buffer;
	}

	public double getWindow() {
		return window;
	}

	public void setWindow(double window) {
		this.window = window;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
    
	public double getQuota() {
		return quota;
	}

	public void setQuota(double quota) {
		this.quota = quota;
	}
	
	public void setKpi(ServiceKPI kpi) {
		this.kpi = kpi;
	}

	public ServiceKPI getKpi() {
		return kpi;
	}
    
    public String getKPIName() {
		return KPIName;
	}

	public void setKPIName(String kPIName) {
		KPIName = kPIName;
	}
    
    public void setName(String name){this.name=name;}
    public String getName(){return name;}
    
    public void setEventType(String eventType){this.eventType=eventType;}
    public String getEventType(){return eventType;}
    
    public ServiceApplication getServiceApplication() {
    	return serviceApplication;
    }
    
    public FQN getFQN() {
    	if(ruleFQN == null)
    		ruleFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return ruleFQN;
    }
    
    @Override
    public int hashCode() {
    	return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
    	
    	if(object == null)
    		return false;
    	
    	if(!(object instanceof Rule))
    		return false;
    		
    	return ((Rule)object).getFQN().equals(getFQN());
    }
}