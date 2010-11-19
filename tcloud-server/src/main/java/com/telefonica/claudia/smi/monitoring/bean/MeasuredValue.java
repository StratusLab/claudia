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
import java.util.Date;

public class MeasuredValue implements Serializable{

	/**
	 * 
	 */
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
	
	
}
