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
