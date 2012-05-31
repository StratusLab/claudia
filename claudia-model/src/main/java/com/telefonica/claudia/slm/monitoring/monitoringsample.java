/*

(c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
Reserved.

The copyright to the software program(s) is property of Telefonica I+D.
The program(s) may be used and or copied only with the express written
consent of Telefonica I+D or in accordance with the terms and conditions
stipulated in the agreement/contract under which the program(s) have
been supplied.

*/

package com.telefonica.claudia.slm.monitoring;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class monitoringsample implements PersistentObject {
	
	public monitoringsample() {
	}
	
	public monitoringsample(nodedirectory associatedObject, Date date, String measure_type, String value, String unit) {
		this.setAssociatedObject(associatedObject);
		this.setDatetime(date);
		this.setMeasureType(measure_type);
		this.setValue(value);
		this.setUnit(unit);
	}
	
	@Id
	@GeneratedValue
	public long id;
	
	@ManyToOne
	private nodedirectory associatedObject;
	
	@Basic
	@Column(name="datetime")
	private Date datetime;

	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	
	@Basic
	@Column(name="value")
	private String value;
	
	@Basic
	@Column(name="measure_type", length=30)
	private String measure_type;
	
	@Basic
	@Column(name="unit", length=30)
	private String unit;

	public long getObjectId() {
		return id;
	}

	public void setAssociatedObject(nodedirectory associatedObject) {
		this.associatedObject = associatedObject;
	}

	public nodedirectory getAssociatedObject() {
		return associatedObject;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
		
		// Parse the date in its components
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		this.year=cal.get(Calendar.YEAR);
		this.month=cal.get(Calendar.MONTH);
		this.day=cal.get(Calendar.DAY_OF_MONTH);
		this.hour=cal.get(Calendar.HOUR_OF_DAY);
		this.minute=cal.get(Calendar.MINUTE);
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setMeasureType(String measure_type) {
		this.measure_type = measure_type;
	}

	public String getMeasureType() {
		return measure_type;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getHour() {
		return hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getMinute() {
		return minute;
	}
}
