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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.smi.util.Util;

public class MeasuredValueList implements Serializable {

	private static final long serialVersionUID = -3573610103879093074L;
	
	private String href;
	private List<MeasuredValue> measuredValues;
	private MeasureDescriptor descriptor;
	
	public MeasuredValueList(MeasureDescriptor descriptor) {
		measuredValues = new ArrayList<MeasuredValue>();
		this.descriptor = descriptor;
	}

	public MeasuredValueList() {
		measuredValues = new ArrayList<MeasuredValue>();
	}

	public boolean add(MeasuredValue mv) {
		return measuredValues.add(mv);
	}
	
	public List<MeasuredValue> getMeasuredValues() {
		return measuredValues;
	}
	
	public void setHref(String identifier) {
		String rawIdentifier = Util.removeParameters(identifier);		
		this.href = rawIdentifier;
	}

	public String getHref() {
		return href;
	}

	public String getDescriptorHref() {
		return href.replace("/values", "");
	}

	public MeasureDescriptor getDescriptor() {
		return descriptor;
	}
	
	public String getXML () throws ParserConfigurationException
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();   
        
        Element measureValueList = doc.createElement("Measure");
        measureValueList.setAttribute("href", this.getHref());
        
      
   
        doc.appendChild(measureValueList);
        

        for (MeasuredValue md: this.getMeasuredValues())
        {
        	Element mde = md.getXML (doc);
        	measureValueList.appendChild(mde);
        }
        
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
}
