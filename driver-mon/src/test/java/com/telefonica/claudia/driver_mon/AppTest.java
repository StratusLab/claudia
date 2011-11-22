package com.telefonica.claudia.driver_mon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;

import org.restlet.data.Form;
import org.restlet.data.Reference;

import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValue;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;
import com.telefonica.claudia.smi.util.Bean2Xml;
import com.telefonica.claudia.smi.util.Util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest  {

	public static void main(String args[]) throws Exception, Exception,
			Exception {
		/*
		 * Properties prop = new Properties(); try { prop.load(new
		 * FileInputStream("./conf/tcloud.properties")); } catch
		 * (FileNotFoundException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 */
	/*	Set<PersistenceProvider> providers =findAllProviders();
		Map <String, Object> configuration = new HashMap<String, Object>();
		configuration.put("hibernate.connection.url", "jdbc:mysql://maquetacollectd.hi.inet:3306/monitoring");
		configuration.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.put("hibernate.connection.username", "claudia");
		configuration.put("hibernate.connection.password", "ClaudiaPass");
		configuration.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		
		configuration.put("hibernate.c3p0.min_size", "5");
		configuration.put("hibernate.c3p0.max_size", "50");
		configuration.put("hibernate.c3p0.timeout", "5000");
		configuration.put("hibernate.c3p0.max_statements", "100");

				
		
		 EntityManagerFactory emf = null;
	        if (providers.size() == 0) {
	            findAllProviders();
	        }
	        for (PersistenceProvider provider : providers) {
	            emf = provider.createEntityManagerFactory("ClaudiaPU", configuration);
	            if (emf != null){
	                break;
	            }
	        }
	        if (emf == null) {
	            throw new PersistenceException("No Persistence provider for EntityManager named " + "ClaudiaPU");
	        }*/
	     
		
		
	     /*   for (String s : names) {
	            try{
	                providers.add((PersistenceProvider)loader.loadClass(s).newInstance());
	            } catch (ClassNotFoundException exc){
	            } catch (InstantiationException exc){
	            } catch (IllegalAccessException exc){
	            }*/
	      
	        
	        
	/*	Properties prop = null;
		SMMonitoringNoJMSDriver fd = new SMMonitoringNoJMSDriver(prop);

		try {
			//getVdcMeasureDescriptorList(orgId, vdcId,);
			//System.out.println(fd.getVdcMeasureDescriptorList("tid", "4caast"));
			//getVappMeasureDescriptorList(orgId, vdcId, vappIds);
			//comprobar vappId en vez de Ids
			//System.out.println(fd.getVappMeasureDescriptorList("tid", "4caast",  "demo"));
			ArrayList<String> vappNames = new ArrayList();
			vappNames.add("monitoring");
			vappNames.add("collector");
			vappNames.add("1");
			MeasureDescriptorList list = fd.getVappMeasureDescriptorList("tid", "tid", vappNames);
			
			System.out.println (list.getXML());
			
			
			
			for (MeasureDescriptor md: list.getMeasureDescriptors())
			{
				System.out.println ("------------------");
				System.out.println (md.getHref());
				System.out.println (md.getName());
				System.out.println (md.getValueType());
				System.out.println (md.getMeasurementTypeId());
			}
			
		//	System.out.println (Bean2Xml.toString(list));
			
		 MeasuredValueFilter valueFilter= new MeasuredValueFilter();
	/*		 Reference urlReplica = new Reference("http://109.231.84.52:8182/api/org/es_tid/vdc/cc1/vapp/edu/vm/1/monitor/sedMemory/values");

				String s = "cpu";
				if (Util.isNumber(s)) {
					valueFilter.setSamples(Integer.parseInt(s));
				} else {
					valueFilter.setSamples(0);
			
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'");
				try {
					String from = "2011-08-24T11:36:00Z";
					if (from != null) {
						valueFilter.setFrom(formatter.parse(from));
					}
				} catch (ParseException pe) {
					System.out.println("wrong param : 'from' format is wrong");
				}
				
				try {
					String to = "2011-08-24T11:40:00Z";
					if (to != null) {
						valueFilter.setTo(formatter.parse(to));
					}
				} catch (ParseException pe) {
					System.out.println("wrong param : 'to' format is wrong");
				}
						
				String interval = "300s";
				if (interval != null) {
					long secondsInterval = Util.convertTimeInterval(interval);
					if (secondsInterval > 0) {
						valueFilter.setInterval(secondsInterval);
					}
				}
			}*/
			
	/*			MeasureDescriptor henar = 	list.getMeasureDescriptors().get(0);
				List<MeasureDescriptor> array = new ArrayList<MeasureDescriptor>();
				array.add(henar);
				
			List<MeasuredValueList> dd = fd.getMeasuredValueList(array, valueFilter);
			System.out.println (dd.get(0).getXML());
			
			for (MeasuredValueList d: dd)
			{
			//	System.out.println (Bean2Xml.toString(d));
				System.out.println (d.getHref());
				for (MeasuredValue ddd: d.getMeasuredValues())
				{
				System.out.println ("------------------");
				System.out.println (ddd.getUnit());
				System.out.println (ddd.getValue());
				}
			}
			
			MeasuredValueList value = 	dd.get(0);
			List<MeasuredValueList> heanr2= new ArrayList<MeasuredValueList>();
			heanr2.add(value);
			
			//System.out.println (Bean2Xml.toString(dd));
		//	MeasureDescriptor md = fd.getHwItemMeasureDescriptor("tid", "tid", vappNames, "cpu", "cpuNice");
		//	 list = fd.getHwItemMeasureDescriptorList("tid", "tid", vappNames, "cpu");
	
		/*	for (MeasureDescriptor md: list.getMeasureDescriptors())
			{
				System.out.println ("------------------");
				System.out.println (md.getHref());
				System.out.println (md.getName());
				System.out.println (md.getValueType());
			}*/
			
			
			//System.out.println(fd.getHwItemMeasureDescriptorList("tid", "4caast", "demo","monitorized"));
			//getVappMeasureDescriptorList(orgId, vdcId, vappIds);
			//comprobar vappIds 
			//System.out.println(fd.getVappMeasureDescriptorList("tid", "4caast", ["demo","monitorized"]));
			//getHwItemMeasureDescriptorList(orgId, vdcId,vappIds, hwItemId);
			//System.out.println(fd.getHwItemMeasureDescriptorList("tid", "4caast", ["demo","monitorized"]));
			//
			//
			//getNetMeasureDescriptorList(orgId, vdcId, netId);
			//System.out.println(fd.getNetMeasureDescriptorList("tid", "4caast", ));
			//getVdcMeasureDescriptor(orgId, vdcId,measureId);
			//System.out.println(fd.getVdcMeasureDescriptor("tid", "4caast", ));
			//getMeasuredValueList(md, valueFilter);
			//System.out.println(fd.getMeasuredValueList(, ));*/
			
			
			
/*		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	
	/**
	 * @return the suite of tests being tested
	 */
/*	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	
	
/*	 private static Set<PersistenceProvider>  findAllProviders()  {
		   Set<PersistenceProvider> providers = new HashSet<PersistenceProvider>();
	        ClassLoader loader = Thread.currentThread().getContextClassLoader();
	        Enumeration<URL> resources = null;
			try {
				resources = loader.getResources("META-INF/services/" + PersistenceProvider.class.getName());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        Set<String> names = new HashSet<String>();
	        while (resources.hasMoreElements()) {
	            URL url = resources.nextElement();
	            System.out.println (url.toString());
	            InputStream is = null;
				try {
					is = url.openStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            try {
	                names.addAll(AppTest.providerNamesFromReader(new BufferedReader(new InputStreamReader(is))));
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
	                try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        }
	        for (String s : names) {
	            try{
	                providers.add((PersistenceProvider)loader.loadClass(s).newInstance());
	            } catch (ClassNotFoundException exc){
	            } catch (InstantiationException exc){
	            } catch (IllegalAccessException exc){
	            }
	        }
	        return providers;
	    }
	 

	  

	    private static Set<String> providerNamesFromReader(BufferedReader reader) throws IOException {
	        Set<String> names = new HashSet<String>();
	       Pattern nonCommentPattern = Pattern.compile("^([^#]+)");
	        String line;
	        while ((line = reader.readLine()) != null) {
	            line = line.trim();
	            Matcher m = nonCommentPattern.matcher(line);
	            if (m.find()) {
	                names.add(m.group().trim());
	            }
	        }
	        return names;
	    }*/
	}
}
