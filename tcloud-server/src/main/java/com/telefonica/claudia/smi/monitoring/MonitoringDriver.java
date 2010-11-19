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
package com.telefonica.claudia.smi.monitoring;

import java.util.ArrayList;

import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;

public interface MonitoringDriver {

	public MeasureDescriptorList getVdcMeasureDescriptorList(String orgName, String vdcName) 
	throws MonitorException;

	public MeasureDescriptorList getVappMeasureDescriptorList(String orgName, String vdcName, ArrayList<String> vappNames) 
	throws MonitorException;

	public MeasureDescriptorList getNetMeasureDescriptorList(String orgName, String vdcName, String netName)
	throws MonitorException;

	public MeasureDescriptorList getHwItemMeasureDescriptorList(String orgName,	String vdcName, ArrayList<String> vappNames, String hwItemName)
	throws MonitorException;
	
	public MeasureDescriptor getVdcMeasureDescriptor(String orgName, String vdcName, String measureId) 
	throws MonitorException;

	public MeasureDescriptor getVappMeasureDescriptor(String orgName, String vdcName, ArrayList<String> vappNames, String measureId) 
	throws MonitorException;

	public MeasureDescriptor getNetMeasureDescriptor(String orgName, String vdcName, String netName, String measureId)
	throws MonitorException;

	public MeasureDescriptor getHwItemMeasureDescriptor(String orgName,	String vdcName, ArrayList<String> vappNames, String hwItemName, String measureId)
	throws MonitorException;

	public MeasuredValueList getMeasuredValueList(MeasureDescriptor md,	int samples) throws MonitorException;
	
	
	
}
