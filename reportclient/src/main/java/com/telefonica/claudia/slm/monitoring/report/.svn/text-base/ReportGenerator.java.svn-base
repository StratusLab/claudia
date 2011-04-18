package com.telefonica.claudia.slm.monitoring.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.monitoring.FrequencyEnum;
import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.export.CSVExport;
import com.telefonica.claudia.slm.monitoring.report.export.ExportException;
import com.telefonica.claudia.slm.monitoring.report.export.IExporter;
import com.telefonica.claudia.smi.URICreation;

public class ReportGenerator {
	
	private static final Logger logger = Logger.getLogger(ReportGenerator.class);
	
	public static final String[] INTERVALS = {"5m","5m","30m","2h","2h","1d"};
	public static final int[] INTERVALS_MINUTES = { 5, 5, 30, 60 * 2, 60 *2, 60*24};
	
	private static final IExporter exporter = new CSVExport();
	private static final MonitoringRestClient client = new MonitoringRestClient();
	
	public static void generateReport(FrequencyEnum frequency, Calendar day, String url) throws ExportException {
		TypeEntityEnum[] typeEntities = TypeEntityEnum.values();
		for (TypeEntityEnum typeEntity : typeEntities) {
			BaseMonitoringData data = 
				client.getMonitoringData(typeEntity, frequency, day, url);

			String rootDir;
			try {
				Properties properties = new Properties();
				properties.load(new FileInputStream(MonitoringRestClient.PATH_TO_PROPERTIES_FILE));
				rootDir = properties.getProperty("rootReportDirectory");
			} catch (IOException e) {
				rootDir = ".";
			}
				
			try{
				File f = new File(rootDir + getFilePath(url)+"/Reports/"+frequency.toString().toLowerCase()+"/"+typeEntity);
				f.mkdirs();
				f = new File(rootDir + getFilePath(url)+"/Reports/"+frequency.toString().toLowerCase()+"/"+typeEntity+"/"+getFileName(frequency, day)+".csv");
				f.createNewFile();
				
				OutputStream os = new FileOutputStream(f);
				data.completeData(frequency, day, ReportGenerator.INTERVALS_MINUTES[frequency.ordinal()]);
				exporter.export(data, os);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void generateAllReports(FrequencyEnum frequency) throws ExportException {
		Calendar today = new GregorianCalendar();
		generateAllReports(frequency, today);
	}
	public static void generateAllReports(FrequencyEnum frequency, Calendar day) throws ExportException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd");
		String todayString = formatter.format(day.getTime());
		
		String[] urlVdcs = client.getVDCs();
		if (urlVdcs!= null && urlVdcs.length>0){
			for (int i = 0; i < urlVdcs.length; i++) {
				/* Generate VDC Report */
				logger.debug("VDC URL:"+urlVdcs[i]);
				generateReport(frequency, day, urlVdcs[i]);
				String[] urlServices = client.getServices(urlVdcs[i]);
				if (urlServices!= null && urlServices.length>0){
					for (int j = 0; j < urlServices.length; j++) {
						/* Generate Service Report */
						logger.debug("Service URL:"+urlServices[j]);
						generateReport(frequency, day,urlServices[j]);
						String[] urlVms = client.getVMs(urlServices[j]);
						if (urlVms!= null && urlVms.length>0){
							for (int k = 0; k < urlVms.length; k++) {
								/* Generate VM Report */
								logger.debug("VM URL:"+urlVms[k]);
								ReportGenerator.generateReport(frequency, day,urlVms[k]);
							}
						}else{
							System.err.println("Error al recuperar VMs del Servicio ["+urlServices[j]+"] del VDC ["+urlVdcs[i]+"]."+todayString);
							logger.error("Error al recuperar VMs del Servicio ["+urlServices[j]+"] del VDC ["+urlVdcs[i]+"]."+todayString);
						}
					}
				}else{
					System.err.println("Error al recuperar Servicios del VDC ["+urlVdcs[i]+"]."+todayString);
					logger.error("Error al recuperar Servicios del VDC ["+urlVdcs[i]+"]."+todayString);
				}					
			}
		}else{
			System.err.println("Error al recuperar VDCs."+todayString);
			logger.error("Error al recuperar VDCs."+todayString);
		}
	}
	
	private static String getFilePath(String url){
		String vdc = null;
		String service = null;
		String vm = null;
		try{
			String fqn = URICreation.getFQNFromURL(url);
			vdc = URICreation.getVDC(fqn);
			vdc = vdc.substring(vdc.lastIndexOf(".")+1);
			service = URICreation.getService(fqn);
			service = service.substring(service.lastIndexOf(".")+1);
			vm = URICreation.getVEE(fqn);
			vm = vm.substring(vm.lastIndexOf(".")+1);
		}catch (Exception e) {
		}	
		
		StringWriter sw = new StringWriter();
		sw.append("/VDC/"+vdc);
		if (service!=null){
			sw.append("/Services/"+service);
			if (vm!=null){
				sw.append("/VMS/"+vm);
			}
		}
		return sw.toString();
	}
	
	public static String getFileName(FrequencyEnum frequency, Calendar day){
		String filename = "";
		switch (frequency) {
			case DAILY: 
				filename = getDailyFileName(day); 
				break;
			case WEEKLY: 
				filename = getWeeklyFileName(day);
				break;
			case QUARTERLY:
				filename = getQuarterlyFileName(day); 
				break;
			case MONTHLY: 
				filename = getMonthlyFileName(day);
				break;
			case YEARLY: 
				filename = getYearlyFileName(day); 
				break;			
		}
		return filename;
	}
	public static String getDailyFileName(Calendar day) {
		
		Calendar init = Utils.getInitDate(FrequencyEnum.DAILY, day);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd");
		return (formatter.format(init.getTime()));
	}
	
	public static String getWeeklyFileName(Calendar day) {
		Calendar init = Utils.getInitDate(FrequencyEnum.WEEKLY, day);
		Calendar end = Utils.getFinishDate(FrequencyEnum.WEEKLY, day);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd");
		return (formatter.format(init.getTime())+"_"+formatter.format(end.getTime()));
	}	

	public static String getMonthlyFileName(Calendar day) {
		Calendar init = Utils.getInitDate(FrequencyEnum.MONTHLY, day);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM");
		return (formatter.format(init.getTime()));
	}	

	public static String getQuarterlyFileName(Calendar day) {
		Calendar init = Utils.getInitDate(FrequencyEnum.QUARTERLY, day);
		Calendar end = Utils.getFinishDate(FrequencyEnum.QUARTERLY, day);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM");
		return (formatter.format(init.getTime())+"_"+formatter.format(end.getTime()));
	}
	
	public static String getYearlyFileName(Calendar day) {
		Calendar init = Utils.getInitDate(FrequencyEnum.YEARLY, day);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return (formatter.format(init.getTime()));
	}		
	
	public static void main(String[] args) {
		try{
			if (args.length==1){
				if (args[0].toLowerCase().equals(FrequencyEnum.DAILY.name().toLowerCase()) ){
					generateAllReports(FrequencyEnum.DAILY);
				}
			}
			else{
				System.out.println("Se necesita un paremetro indicando el tipo de report (daily | weekly | monthly | quarterly | yearly)");
			}
		}catch (Exception e) {
			System.err.println("Error al generar reports. "+e.getMessage());
			e.printStackTrace();
		}
	}
}
