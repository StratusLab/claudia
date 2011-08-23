/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.monitoring.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.telefonica.claudia.smi.util.Util;

public class MeasuredValueList implements Serializable {

	private static final long serialVersionUID = -3573610103879093074L;
	
	private String href;
	private List<MeasuredValue> measuredValues;
	private MeasureDescriptor descriptor;
	
	public MeasuredValueList(MeasureDescriptor descriptor) {
		measuredValues = new ArrayList<MeasuredValue>();
		this.descriptor = descriptor;
	}

	public MeasuredValueList() {
		measuredValues = new ArrayList<MeasuredValue>();
	}

	public boolean add(MeasuredValue mv) {
		return measuredValues.add(mv);
	}
	
	public List<MeasuredValue> getMeasuredValues() {
		return measuredValues;
	}
	
	public void setHref(String identifier) {
		String rawIdentifier = Util.removeParameters(identifier);		
		this.href = rawIdentifier;
	}

	public String getHref() {
		return href;
	}

	public String getDescriptorHref() {
		return href.replace("/values", "");
	}

	public MeasureDescriptor getDescriptor() {
		return descriptor;
	}
}
