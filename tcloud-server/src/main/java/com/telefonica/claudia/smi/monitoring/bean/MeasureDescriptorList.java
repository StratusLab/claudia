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
import java.util.ArrayList;
import java.util.List;

public class MeasureDescriptorList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4345093806730536110L;
	
	private String href;
	private Link link;
	private List <MeasureDescriptor> measureDescriptors;
	
	public MeasureDescriptorList() {
		link = new Link();
		measureDescriptors = new ArrayList <MeasureDescriptor>();
	}
	
	public MeasureDescriptorList(String identifier, String type) {
		this();
		setHref(identifier);
		link.setRel("up");
		link.setType(type);
		link.setHref(getUpHref(getHref()));
	}


	public void setMeasureDescriptorsHrefs(String identifier){
		for (MeasureDescriptor md: measureDescriptors) {
			md.setHrefs(identifier);
		}
	}

	
	public boolean add(MeasureDescriptor md) {
		return measureDescriptors.add(md);
	}
	
	public MeasureDescriptor getMeasureDescriptorWithName(String name) {
		MeasureDescriptor md = null;  
		for (MeasureDescriptor mditem: measureDescriptors) {
			if (mditem.getName().equals(name)) {
				md = mditem; 
				break;
			}
		}
		return md;
	}
	
	public MeasureDescriptor getMeasureDescriptorWithId(String id) {
		MeasureDescriptor md = null;  
		for (MeasureDescriptor mditem: measureDescriptors) {
			if (mditem.getMeasurementTypeId().equals(id)) {
				md = mditem; 
				break;
			}
		}
		return md;
	}
	
	public List <MeasureDescriptor> getMeasureDescriptors() {
		return measureDescriptors;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHref() {
		return href;
	}
	
	public void setLink(Link link) {
		this.link = link;
	}

	public Link getLink() {
		return link;
	}
	
	private static String getUpHref(String href){
		StringBuilder sb = new StringBuilder(href);
		int n = sb.lastIndexOf("?");
		if (n>0) sb.delete(sb.lastIndexOf("?"),sb.length());
		sb.delete(sb.lastIndexOf("/"),sb.length());
		return sb.toString();
	}
	
//	public void setMeasureDescriptors(List <MeasureDescriptor> mdl) {
//		this.measureDescriptors = mdl;
//	}
	
}
