/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DataTypesUtils {
	
	public static final String STANDARD_STORAGE_UNIT_DEFAULT="byte * 2^30";
	public static final String STANDARD_BANDWIDTH_UNIT_DEFAULT="byte per second * 2^10";
	public static final String STANDARD_STORAGE_UNIT_EXP="byte\\*2\\^\\d*";
	public static final String STANDARD_BANDWIDTH_UNIT_EXP="byte per second\\*2\\^\\d*";
	
	public static Map<String, String> storageUnitsConversion = new HashMap<String, String>();
	static {
		storageUnitsConversion.put("GB", "byte * 2^30");
		storageUnitsConversion.put("MB", "byte * 2^20");
		storageUnitsConversion.put("KB", "byte * 2^10");
	}
	
	public static String serializeXML(Document doc) throws IllegalArgumentException {
		StringWriter sw = new StringWriter();
		DOMSource domSource = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(sw);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			serializer.setOutputProperty(OutputKeys.INDENT,"yes");
			
			
			serializer.transform(domSource, streamResult);
			
		} catch (TransformerConfigurationException e) {
			throw new IllegalArgumentException("Error transforming XML content. The OVF document may be malformed: " + e.getMessage());
		} catch (TransformerException e) {
			throw new IllegalArgumentException("Error transforming XML content. The OVF document may be malformed." + e.getMessage());
		}
		
		return sw.toString();
	}
	
	public static String date2String(long milis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		return sdf.format(new Date(milis));
	}
	
	public static Date string2Date(String source) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		return sdf.parse(source);
	}
	
	public static double getStorageUnitConversion(String unit1) {
		return getStorageUnitConversion(unit1, STANDARD_STORAGE_UNIT_DEFAULT);
	}
	
	public static double getStorageUnitConversion(String unit1, String unit2) {
		
		String unit1Cleaned = unit1.replaceAll(" ", "");
		String unit2Cleaned = unit2.replaceAll(" ", "");
		
		if (!unit1Cleaned.matches(STANDARD_STORAGE_UNIT_EXP)) 
			return 1.0;
		
		if (!unit2Cleaned.matches(STANDARD_STORAGE_UNIT_EXP)) 
			return 1.0;

		long firstPower = Long.valueOf(unit1Cleaned.substring(unit1Cleaned.indexOf("^")+1));
		long secondPower = Long.valueOf(unit2Cleaned.substring(unit2Cleaned.indexOf("^")+1));
		
		long finalPower = firstPower - secondPower;
		
		return Math.pow(2, finalPower);
	}
	
	public static boolean isStorageUnit(String unit) {
		return unit.matches(STANDARD_STORAGE_UNIT_EXP);
	}

	public static boolean isBandwidthUnit(String unit) {
		return unit.matches(STANDARD_BANDWIDTH_UNIT_EXP);
	}

}
