package com.telefonica.claudia.slm.monitoring.report.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;

import com.telefonica.claudia.slm.monitoring.report.BaseMonitoringData;
import com.telefonica.claudia.slm.monitoring.report.MonitoringRestClient;
import com.telefonica.claudia.slm.monitoring.report.MonitoringSampleData;

public class CSVExport implements IExporter {
	private static String CSV_SEPARATOR =";";
	public static String DATE_FORMAT = "yyyy'-'MM'-'dd' 'HH':'mm':'ss";	
	
	public void export(BaseMonitoringData data, OutputStream os) throws ExportException {
		/*
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
			//data.completeData(frequency, day, ReportGenerator.INTERVALS_MINUTES[frequency.ordinal()]);
			exporter.export(data, os);
		} catch (FileNotFoundException e) {
		}		
		*/
		
		Set<String> keys = data.keys();
		MonitoringSampleData[][] samples = new MonitoringSampleData[keys.size()][];
		int filas = 0;
		int columnas = 0;
		for (String measure : keys) {
			MonitoringSampleData[] sample = data.get(measure);
			samples[columnas]= sample;
			filas = sample.length;
			columnas++;
			// TODO hay un problema por exportar "en columnas" en vez de "en filas"
		}
		
		PrintWriter pw = new PrintWriter(os);
		// Cabecera
		pw.write("Fecha"+CSV_SEPARATOR);
		for (String measure : keys) {
			pw.write(measure+CSV_SEPARATOR);
		}			
		pw.write("\n");
		pw.write(CSV_SEPARATOR);
		for (int c = 0; c < columnas; c++) {
			pw.write("("+samples[c][0].getUnit()+")");
			//pw.write("(uni)");
			if (c<columnas-1){
				pw.write(CSV_SEPARATOR);
			}
			else{
				pw.write("\n");
			}
		}		
		//DATOS
		for (int f = 0; f < filas; f++) {
			pw.write(samples[0][f].getTimestampAsString(DATE_FORMAT)+CSV_SEPARATOR);
			for (int c = 0; c < columnas; c++) {
				if (samples[c][f] != null){
					pw.write(samples[c][f].getValue());
				}
				if (c<columnas-1){
					pw.write(CSV_SEPARATOR);
				}
				else{
					pw.write("\n");
				}
			}
		}
		pw.close();
	}
}