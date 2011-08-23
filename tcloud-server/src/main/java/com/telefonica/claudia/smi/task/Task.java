/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.task;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;


public abstract class Task implements Runnable, Serializable {

	public enum TaskStatus {QUEUED, WAITING, RUNNING, SUCCESS, ERROR, CANCELLED};
	
	protected transient Thread executor;
	
	public static class TaskError implements Serializable {
		public String majorCode;
		public String minorCode;
		public String message;
		public String vendorSpecificErrorCode;
	}
	
	protected String returnMsg;
	private long id;
	protected TaskStatus status;
	protected long startTime;
	protected long endTime;
	private long expireTime;
	protected TaskError error;
	
	private String uri;
	private String uriParent;
	
	private String uriResource;
	private String typeResource;
	private String ownerUri;
	
	public Task() {
		this.setStatus(TaskStatus.QUEUED);
		this.setStartTime(System.currentTimeMillis());
		
		this.executor = new Thread(this);
	}
	
	public void setUriParent(String uriParent) {
		this.setUri(uriParent + "/" + this.id);
		this.uriParent= uriParent;
	}
	
	public void setResource(String uriResource, String typeResource) {
		this.setUriResource(uriResource);
		this.setTypeResource(typeResource);
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
	
	public void start() {
		executor.start();
	}
	
	public abstract void execute();
	
	public void setTaskId(long id) {
		this.id= id;
	}
	
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
			
			root.setAttribute("href", getUri());
			
			root.setAttribute("startTime", DataTypesUtils.date2String(getStartTime()));
			root.setAttribute("endTime", DataTypesUtils.date2String(getEndTime()));
			
			if (getExpireTime() > 0) {
				root.setAttribute("expiryTime", DataTypesUtils.date2String(getExpireTime()));
			}
			
			root.setAttribute("status", getStatus().toString().toLowerCase());
			
			doc.appendChild(root);
			
			if (getStatus() == TaskStatus.ERROR) {
				Element errorNode = doc.createElement("error");
				
				if (getError()!= null) {
					errorNode.setAttribute("message", getError().message);
					errorNode.setAttribute("majorErrorCode", getError().majorCode);
					errorNode.setAttribute("minorErrorCode", getError().minorCode);
					errorNode.setAttribute("vendorSpecificErrorCode", getError().vendorSpecificErrorCode);
				} else {
					errorNode.setAttribute("message", "Unknown error");
				}
				
				root.appendChild(errorNode);
			}
			
			if (getStatus() == TaskStatus.SUCCESS && getUriResource()!=null) {
				Element resultNode = doc.createElement("result");
				
				resultNode.setAttribute("href", getUriResource());
				resultNode.setAttribute("type", getTypeResource());
				
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

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	protected void setStatus(TaskStatus status) {
		this.status = status;
	}

	public void setError(TaskError error) {
		this.error = error;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public String getUriParent() {
		return uriParent;
	}

	public void setUriResource(String uriResource) {
		this.uriResource = uriResource;
	}

	public String getUriResource() {
		return uriResource;
	}

	public void setTypeResource(String typeResource) {
		this.typeResource = typeResource;
	}

	public String getTypeResource() {
		return typeResource;
	}
}
