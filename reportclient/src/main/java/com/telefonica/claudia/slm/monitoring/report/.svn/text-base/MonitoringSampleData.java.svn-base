package com.telefonica.claudia.slm.monitoring.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class MonitoringSampleData implements Comparable<MonitoringSampleData>{
	
	private static Logger log = Logger.getLogger(MonitoringSampleData.class);
	public static String DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'";

	private Date timestamp;
	private String value;
	private String unit;
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public static Date timestampToDate(String timestampString) {
		Date convertedDate = null;

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			if (timestampString != null) {
				convertedDate = formatter.parse(timestampString);
			}
		} catch (ParseException pe) {
			log.warn("Wrong format :"+timestampString+" does not match pattern:"+DATE_FORMAT);
		}
		return convertedDate;
	}	
	
	public static String dateToTimestamp(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			if (date != null) {
				return (formatter.format(date));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}	
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getTimestampAsString(String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			if (timestamp != null) {
				return (formatter.format(timestamp));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
		
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public int compareTo(MonitoringSampleData o) {
		return this.getTimestamp().compareTo(o.getTimestamp());
	}
}
