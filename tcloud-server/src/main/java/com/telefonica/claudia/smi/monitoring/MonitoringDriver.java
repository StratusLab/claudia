/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.monitoring;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;

public interface MonitoringDriver {

	public MeasureDescriptorList getVdcMeasureDescriptorList(String orgName, String vdcName) 
	throws MonitorException;

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName, String vdcName, List<String> vappNames) 
	throws MonitorException;

	public MeasureDescriptorList getNetMeasureDescriptorList(String orgName, String vdcName, String netName)
	throws MonitorException;

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,	String vdcName, List<String> vappNames, String hwItemName)
	throws MonitorException;
	
	public MeasureDescriptor getVdcMeasureDescriptor(String orgName, String vdcName, String measureId) 
	throws MonitorException;

	public MeasureDescriptor getVappMeasureDescriptor(String orgName, String vdcName, List<String> vappNames, String measureId) 
	throws MonitorException;

	public MeasureDescriptor getNetMeasureDescriptor(String orgName, String vdcName, String netName, String measureId)
	throws MonitorException;

	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,	String vdcName, List<String> vappNames, String hwItemName, String measureId)
	throws MonitorException;

	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md, MeasuredValueFilter filter)
	throws MonitorException;
	
	public List<MeasuredValueList> getMeasuredValueList(List<MeasureDescriptor> md, MeasuredValueFilter filter)
	throws MonitorException;

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames)
			throws MonitorException;

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName)
			throws MonitorException;

	public MeasureDescriptor getVappMeasureDescriptor(String orgName, String vdcName,
			ArrayList<String> vappNames, String measureId)
			throws MonitorException;

	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,
			String vdcName, ArrayList<String> vappNames, String hwItemName,
			String measureId) throws MonitorException;	
}