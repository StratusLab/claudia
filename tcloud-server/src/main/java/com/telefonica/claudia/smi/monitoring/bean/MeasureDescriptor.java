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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Reference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class MeasureDescriptor implements Serializable{
	
	private static final long serialVersionUID = -467083398283039949L;
	
	private String measurementTypeId;
	
	private String description;
	private String name;
	private String valueType;
	
	private String minValue;
	private String maxValue;
	
	private String href;
	private Link link1;
	private Link link2;
	
	public MeasureDescriptor(){
		link1 = new Link();
		link1.setRel("monitor:subscribe");
		link1.setType("application/vnd.telefonica.tcloud.monitoringCallback+plain");
		link2 = new Link();
		link2.setRel("monitor:poll");
		link2.setType("application/vnd.telefonica.tcloud.measure+xml");
	}
	
	public MeasureDescriptor(String name, String id, String description,
							 String valueType, String minValue, String maxValue, String href) {
		this();
		
		setHref(href);
		setDescription(description);
		setName(name);
		setMeasurementTypeId(id);
		setMaxValue(maxValue);
		setMinValue(minValue);
		setValueType(valueType);
	}

	public MeasureDescriptor(String identifier) {
		this();
		setHrefs(identifier);
	}

	public void setHrefs(String identifier) {
		setHref(identifier + '/' + Reference.encode(name));
		link1.setHref(href + "/subscription");
		link2.setHref(href + "/values");
	}

	
	public String showData() {
		StringBuffer sb = new StringBuffer();
		sb.append("description: ");			sb.append(description);			sb.append('\n');
		sb.append("name: ");				sb.append(name);				sb.append('\n');
		sb.append("valueType: ");			sb.append(valueType);			sb.append('\n');
		return sb.toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMaxValue() {
		return maxValue;
	}
	
	public void setHref(String href) {
		if (href!=null && href.length()>1 && href.charAt(href.length()-1)=='/')
			this.href = href.substring(0, href.length()-1);
		else
			this.href = href;
	}

	public String getHref() {
		return href;
	}

	public void setLink1(Link link) {
		this.link1 = link;
	}
	
	public Link getLink1() {
		return link1;
	}
	
	public void setLink2(Link link) {
		this.link2 = link;
	}
	
	public Link getLink2() {
		return link2;
	}

	public void setMeasurementTypeId(String id) {
		this.measurementTypeId = id;
	}

	public String getMeasurementTypeId() {
		return measurementTypeId;
	}
	
	public Document getXML () throws ParserConfigurationException
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();     
        getXML (doc);
        return doc;
	}
	
	public String getString () throws ParserConfigurationException
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();     
        Element el = getXML (doc);
        doc.appendChild(el);
       
	
	 OutputFormat format    = new OutputFormat (doc); 
     // as a String
     StringWriter stringOut = new StringWriter ();    
     XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                 format);
     try {
			serial.serialize(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     // Display the XML
     System.out.println("XML " + stringOut.toString());
     return stringOut.toString();
	}
	
	public Element getXML (Document doc) throws ParserConfigurationException
	{
		
       
        Element md = doc.createElement("MeasureDescriptor");
        md.setAttribute("href", href);
        md.setAttribute("name", name);
        
        
        Element link1 = doc.createElement("Link");
        link1.setAttribute("href", this.getLink1().getHref());
        link1.setAttribute("type", this.getLink1().getType());
        link1.setAttribute("rel", this.getLink1().getRel());
        md.appendChild(link1);
        
        Element link2 = doc.createElement("Link");
        link2.setAttribute("href", this.getLink2().getHref());
        link2.setAttribute("type", this.getLink2().getType());
        link2.setAttribute("rel", this.getLink2().getRel());
        md.appendChild(link2);
        
        Element unit = doc.createElement("ValueType");
        
        unit.appendChild(doc.createTextNode(valueType));
        md.appendChild(unit);
        Element eminValue = doc.createElement("MinValue");
        
        eminValue.appendChild(doc.createTextNode(minValue));
        md.appendChild(eminValue);
        Element maxValue = doc.createElement("MaxValue");
    
        maxValue.appendChild(doc.createTextNode(getMaxValue()));
        md.appendChild(maxValue);
        
        Element description = doc.createElement("Description");

        if (getDescription() == null || getDescription().length()==0)
        	description.appendChild(doc.createTextNode(name));
        else
            description.appendChild(doc.createTextNode(getDescription()));
        md.appendChild(description);
        
        
        return md;
	}
	
	
 

}
