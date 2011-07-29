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

public class ProbeMeasureEvent extends Event {
    
	private static final long serialVersionUID = 1L;
    
	private String fqnString = null;
    private double value = 0.0;
    
    public ProbeMeasureEvent (long t_0, long delta_t, String fqnString, double value) {
        super(t_0, delta_t, EventType.PROBE_MEASUREMENT);
        this.fqnString = fqnString;
        this.value = value;
    }
    
    public String getMeasure() {
        return fqnString;
    }
    
    public double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return super.toString() + " measure: " + fqnString + " value: " + value;
    }        
}
