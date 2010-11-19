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
package com.telefonica.claudia.smi.monitoring.bean;

import java.io.Serializable;

import org.restlet.data.Reference;

public class MeasureDescriptor implements Serializable{
	
	/**
	 * 
	 */
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
	
	
	//set/get

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
