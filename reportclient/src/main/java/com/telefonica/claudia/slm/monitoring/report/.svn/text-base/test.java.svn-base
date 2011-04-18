package com.telefonica.claudia.slm.monitoring.report;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.telefonica.claudia.slm.common.DbManager;
import com.telefonica.claudia.slm.monitoring.FrequencyEnum;
import com.telefonica.claudia.slm.monitoring.MonitoringSample;
import com.telefonica.claudia.slm.monitoring.NodeDirectory;
import com.telefonica.claudia.slm.monitoring.report.export.ExportException;
import com.telefonica.claudia.smi.URICreation;


public class test {
	private static String URL="jdbc:mysql://10.95.129.34:3306/ClaudiaDB";
	private static String USER="claudiauser";
	private static String PASSWORD="claudiapass";
	
	public static void main3(String[] args) {
		
		try {
			//ReportGenerator.generateAllReports(FrequencyEnum.DAILY);
			Calendar day = new GregorianCalendar();
			ReportGenerator.generateReport(FrequencyEnum.DAILY, day, "http://10.95.129.34:8183/api/org/tid34/vdc/testing/vapp/tesging_ser/miguel/1");	
			
		} catch (ExportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main2(String[] args) {

		
		DbManager dbManager = DbManager.createDbManager(URL, false,USER, PASSWORD);
		List<NodeDirectory> list = dbManager.getList(NodeDirectory.class);
		System.out.println("size():"+list.size());
		for (int i = 0; i < list.size(); i++) {
			NodeDirectory node = list.get(i);
			
			System.out.print("\nFqn:"+node.getFqnString());
			System.out.print(" Tipo:"+node.getTipo());
			System.out.print(" Status():"+node.getStatus());
			System.out.print(" FechaCreacion():"+node.getFechaCreacion());
			if (node.getTipo()==4){
				System.out.println("getURIVapp:"+URICreation.getURIVEEReplica(node.getFqnString()));		
			}
		
		}
	}
	
	public static void main(String[] args) {
		DbManager dbManager = DbManager.createDbManager(URL, false,USER, PASSWORD);

		/*
				List<MonitoringSample> list = dbManager.executeQueryList(MonitoringSample.class, "select max(datetime) from "+MonitoringSample.class.getName()+ " where associatedObject_internalId = 6 AND  measure_type ='cpuUsageMinimum'");
				for (int j = 0; j < list.size(); j++) {
					System.out.println(list.get(j).getValue()+" "+list.get(j).getDatetime());
				}
*/
		Timestamp timestamp = (Timestamp) dbManager.executeQuery("select max(datetime) from "+MonitoringSample.class.getName()+ " where associatedObject_internalId = 6 AND  measure_type ='cpuUsageMinimum'");
		System.out.println("o2s "+timestamp.toString());
		
	}
}