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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeasuredValueFilter implements Serializable {
	private static final long serialVersionUID = 4786088525357990361L;

	private int samples = 1;
	private Date from;
	private Date to;
	
	/**
	 * Interval between samples (in seconds)
	 */
	private Long interval;
	
	public MeasuredValueFilter() {
	/*	SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'");
		try {
			String from = "2011-08-24T11:36:00Z";
			if (from != null) {
				this.from = formatter.parse(from);
			}
		} catch (ParseException pe) {
			System.out.println("wrong param : 'from' format is wrong");
		}
		
		try {
			String to = "2011-08-24T11:40:00Z";
			if (to != null) {
				this.to = formatter.parse(to);
			}
		} catch (ParseException pe) {
			System.out.println("wrong param : 'to' format is wrong");
		}*/
		
	/*	this.to =  new java.util.Date("2011-08-24 14:45:39");
		long lnMilisegundos = to.getTime() - 3*60*1000;
		java.util.Date antesDate = new java.util.Date(lnMilisegundos);
		this.from = antesDate;*/
		Date currentDAte = new java.util.Date();
		long lnMilisegundosto = currentDAte.getTime() - 2*60*60*1000;
		long lnMilisegundosfrom = lnMilisegundosto - 3*60*1000;
		this.interval = new Long(300);
		this.to= new java.util.Date(lnMilisegundosto);
		this.from= new java.util.Date(lnMilisegundosfrom);
	}
	
	public MeasuredValueFilter(int samples, Date from, Date to, Long interval) {
		this.samples = samples;
		this.from = from;
		this.to = to;
		this.interval = interval;
	}
	
	public int getSamples() {
		return samples;
	}
	public void setSamples(int samples) {
		this.samples = samples;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public Long getInterval() {
		return interval;
	}
	public void setInterval(Long interval) {
		this.interval = interval;
	}
}
