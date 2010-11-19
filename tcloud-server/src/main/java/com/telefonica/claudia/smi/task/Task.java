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
package com.telefonica.claudia.smi.task;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public abstract class Task extends Thread {

	public enum TaskStatus {QUEUED, WAITING, RUNNING, SUCCESS, ERROR, CANCELLED};
	
	public static class TaskError {
		public String majorCode;
		public String minorCode;
		public String message;
		public String vendorSpecificErrorCode;
	}
	
	protected String returnMsg;
	protected long id;
	protected TaskStatus status;
	protected long startTime;
	protected long endTime;
	protected long expireTime;
	protected  TaskError error;
	
	String uri;
	String uriParent;
	
	String uriResource;
	String typeResource;
	String ownerUri;
	
	private static Long actualTaskId  =1l;
	
	private synchronized static long generateNewId() {
		actualTaskId++;
		
		return actualTaskId;
	}
	
	public Task() {
		this.id = generateNewId();
		this.status = TaskStatus.QUEUED;
		this.startTime = System.currentTimeMillis();
	}
	
	public void setUriParent(String uriParent) {
		this.uri = uriParent + "/" + this.id;
		this.uriParent= uriParent;
	}
	
	public void setResource(String uriResource, String typeResource) {
		this.uriResource = uriResource;
		this.typeResource = typeResource;
	}
	
	public String getOwnerUri() {
		return ownerUri;
	}
	
	public void setOwnerUri(String ownerUri) {
		this.ownerUri = ownerUri;
	}
	
	public void run() {
		execute();
	}
	
	public abstract void execute();
	
	public long getTaskId() {
		return id;
	}
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public Document getXmlDescription() throws IOException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc;
        
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement(TCloudConstants.TAG_TASK);
			
			root.setAttribute("href", uri);
			
			root.setAttribute("startTime", DataTypesUtils.date2String(startTime));
			root.setAttribute("endTime", DataTypesUtils.date2String(endTime));
			
			if (expireTime > 0) {
				root.setAttribute("expiryTime", DataTypesUtils.date2String(expireTime));
			}
			
			root.setAttribute("status", status.toString().toLowerCase());
			
			doc.appendChild(root);
			
			if (status == TaskStatus.ERROR) {
				Element errorNode = doc.createElement("error");
				
				if (error!= null) {
					errorNode.setAttribute("message", error.message);
					errorNode.setAttribute("majorErrorCode", error.majorCode);
					errorNode.setAttribute("minorErrorCode", error.minorCode);
					errorNode.setAttribute("vendorSpecificErrorCode", error.vendorSpecificErrorCode);
				} else {
					errorNode.setAttribute("message", "Unknown error");
				}
				
				root.appendChild(errorNode);
			}
			
			if (status == TaskStatus.SUCCESS && uriResource!=null) {
				Element resultNode = doc.createElement("result");
				
				resultNode.setAttribute("href", uriResource);
				resultNode.setAttribute("type", typeResource);
				
				root.appendChild(resultNode);
			}
			
			if (ownerUri!= null) {
				Element ownerUriNode = doc.createElement("owner");
				
				ownerUriNode.setAttribute("href", ownerUri);
				ownerUriNode.setAttribute("type", URICreation.VDC_MIME_TYPE);
				
				root.appendChild(ownerUriNode);
			}
			
		} catch (ParserConfigurationException e) {
			throw new IOException("Error writing the XML response: " + e.getMessage());
		}
        
		return doc;
	}
	
	public String getStringDescription() throws IllegalArgumentException, IOException {
		return DataTypesUtils.serializeXML(getXmlDescription());
	}
	
	public String getReturnMsg() {
		return returnMsg;
	}
	
	public TaskError getError() {
		return error;
	}
}
