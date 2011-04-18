package com.telefonica.claudia.slm.monitoring.report;

import java.io.File;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.slm.monitoring.FrequencyEnum;

public class Utils {

	public static void documentToString(File file) 
 	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			Element root = doc.getDocumentElement();
			String s = root.toString();
		}
		catch(Exception e)
		{	e.printStackTrace();
		}
	}
	
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}	
	
	public static Calendar getFinishDate(FrequencyEnum frequency, Calendar day) {
		Calendar end;
		switch (frequency) {
		case REALTIME:
			end = new GregorianCalendar();
			end.set(Calendar.SECOND, 0);
			break;
		default:
			end = (Calendar) day.clone();
			end.add(Calendar.DAY_OF_YEAR, -1);
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 0);	
			break;
		}
		return end;
	}
	
	public static Calendar getInitDate(FrequencyEnum frequency, Calendar day) {
		Calendar init = (Calendar) day.clone();
		switch (frequency) {
			case REALTIME:
				init = new GregorianCalendar();
				init.add(Calendar.MINUTE, -30);
				init.set(Calendar.SECOND, 0);
				break;
			case DAILY: 
				init.add(Calendar.DAY_OF_YEAR, -1);
				init.set(Calendar.HOUR_OF_DAY, 0);
				init.set(Calendar.MINUTE, 0);
				init.set(Calendar.SECOND, 0);
				break;
			case WEEKLY: 
				init.add(Calendar.DAY_OF_YEAR, -7);
				init.set(Calendar.HOUR_OF_DAY, 0);
				init.set(Calendar.MINUTE, 0);
				init.set(Calendar.SECOND, 0);
				break;
			case QUARTERLY:
				init.add(Calendar.MONTH, -3);
				init.set(Calendar.HOUR_OF_DAY, 0);
				init.set(Calendar.MINUTE, 0);
				init.set(Calendar.SECOND, 0);				
				break;
			case MONTHLY: 
				init.add(Calendar.MONTH, -1);
				init.set(Calendar.HOUR_OF_DAY, 0);
				init.set(Calendar.MINUTE, 0);
				init.set(Calendar.SECOND, 0);				
				break;
			case YEARLY: 
				init.add(Calendar.YEAR, -1);
				init.set(Calendar.HOUR_OF_DAY, 0);
				init.set(Calendar.MINUTE, 0);
				init.set(Calendar.SECOND, 0);				
				break;			
		}	
		return init;
	}
	
	private static String buildParameters(Calendar init, Calendar end, String interval) {
		String parameters = 
			"&from=" + MonitoringSampleData.dateToTimestamp(init.getTime()) +
			"&to=" + MonitoringSampleData.dateToTimestamp(end.getTime()) +
			"&interval=" + interval;
		return parameters;
	}
	
	public static String getMonitoringParameters(Calendar init, Calendar finish, String interval) {
		return buildParameters(init, finish, interval);
	}
	
	public static String getMonitoringParameters(FrequencyEnum frequency , Calendar day) {
		Calendar init = Utils.getInitDate(frequency,day);
		Calendar finish = Utils.getFinishDate(frequency,day);

		return buildParameters(init, finish, ReportGenerator.INTERVALS[frequency.ordinal()]);
	}
	
	public static Calendar roundToInterval(Calendar date, int intervalo){
		date.set(Calendar.SECOND,0);
		int minuto = date.get(Calendar.MINUTE);
		int cociente = minuto / intervalo;
		int resto = minuto % intervalo;
		if (resto > (intervalo/2)){
			minuto = (cociente * intervalo) + intervalo;
		}else{
			minuto = cociente * intervalo;
		}
		date.set(Calendar.MINUTE,minuto);
		return date;
	}
}