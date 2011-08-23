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

import org.restlet.data.Reference;

public class MeasureDescriptor implements Serializable{
	
	private static final long serialVersionUID = -467083398283039949L;
	
	private String measurementTypeId;
	
	private String description;
	private String name;
	private String valueType;
	
	private String minValue;
	private String maxValue;
	
	private String href;
	private Link link1;
	private Link link2;
	
	public MeasureDescriptor(){
		link1 = new Link();
		link1.setRel("monitor:subscribe");
		link1.setType("application/vnd.telefonica.tcloud.monitoringCallback+plain");
		link2 = new Link();
		link2.setRel("monitor:poll");
		link2.setType("application/vnd.telefonica.tcloud.measure+xml");
	}
	
	public MeasureDescriptor(String name, String id, String description,
							 String valueType, String minValue, String maxValue, String href) {
		this();
		
		setHref(href);
		setDescription(description);
		setName(name);
		setMeasurementTypeId(id);
		setMaxValue(maxValue);
		setMinValue(minValue);
		setValueType(valueType);
	}

	public MeasureDescriptor(String identifier) {
		this();
		setHrefs(identifier);
	}

	public void setHrefs(String identifier) {
		setHref(identifier + '/' + Reference.encode(name));
		link1.setHref(href + "/subscription");
		link2.setHref(href + "/values");
	}

	
	public String showData() {
		StringBuffer sb = new StringBuffer();
		sb.append("description: ");			sb.append(description);			sb.append('\n');
		sb.append("name: ");				sb.append(name);				sb.append('\n');
		sb.append("valueType: ");			sb.append(valueType);			sb.append('\n');
		return sb.toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMaxValue() {
		return maxValue;
	}
	
	public void setHref(String href) {
		if (href!=null && href.length()>1 && href.charAt(href.length()-1)=='/')
			this.href = href.substring(0, href.length()-1);
		else
			this.href = href;
	}

	public String getHref() {
		return href;
	}

	public void setLink1(Link link) {
		this.link1 = link;
	}
	
	public Link getLink1() {
		return link1;
	}
	
	public void setLink2(Link link) {
		this.link2 = link;
	}
	
	public Link getLink2() {
		return link2;
	}

	public void setMeasurementTypeId(String id) {
		this.measurementTypeId = id;
	}

	public String getMeasurementTypeId() {
		return measurementTypeId;
	}

}
