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
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MeasuredValue implements Serializable{

	private static final long serialVersionUID = -3490921200395717269L;
	
	private String value;
	private Date registerDate;
	private String unit;

	public MeasuredValue() {}
	
	public MeasuredValue(String value, Date registerDate, String unit) {
		setValue(value);
		setRegisterDate(registerDate);
		setUnit(unit);
	}
	
	public String showData() {
		StringBuffer sb = new StringBuffer();
		sb.append("value: ");				sb.append(value);				sb.append('\n');
		sb.append("registerDate: ");		sb.append(registerDate);		sb.append('\n');
		sb.append("unit: ");				sb.append(unit);				sb.append('\n');
		return sb.toString();
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public Element getXML(Document doc) {
		// TODO Auto-generated method stub
		Element md = doc.createElement("Sample");
        md.setAttribute("unit", this.unit);
        md.setAttribute("timestamp", this.registerDate.toString());

        
        Element value = doc.createElement("Value");
        value.appendChild(doc.createTextNode(this.value));
        md.appendChild(value);
        
        Element capacity = doc.createElement("Capacity");
        value.appendChild(doc.createTextNode(""));
        md.appendChild(value);
        
        return md;
	}
	
	
}
