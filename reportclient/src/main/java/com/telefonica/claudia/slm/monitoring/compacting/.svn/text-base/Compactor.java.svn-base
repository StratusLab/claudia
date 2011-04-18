package com.telefonica.claudia.slm.monitoring.compacting;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.telefonica.claudia.slm.common.DbManager;
import com.telefonica.claudia.slm.monitoring.MonitoringSample;
import com.telefonica.claudia.slm.monitoring.NodeDirectory;
import com.telefonica.claudia.slm.monitoring.report.OtherTypeEntityEnum;
import com.telefonica.claudia.slm.monitoring.report.TypeEntityEnum;

public class Compactor {
	
	public enum COMPACTOR_TYPE {HOUR_TYPE, DAY_TYPE};
	
	private static final Logger logger = Logger.getLogger(Compactor.class);
	private final String DB_URL;
	private final String DB_USER;
	private final String DB_PASSWORD;	

	public static final String PATH_TO_PROPERTIES_FILE = "./conf/reportClient.properties";
	private static final Properties properties = new Properties();	
	private DbManager dbManager;

	public Compactor() {		
		try {
			properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
			DB_URL = properties.getProperty("bd.url");
			DB_USER = properties.getProperty("bd.user");
			DB_PASSWORD = properties.getProperty("bd.password");
			dbManager = DbManager.createDbManager(DB_URL, false,DB_USER, DB_PASSWORD);
		} catch (IOException e) {
			logger.error("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
			throw new RuntimeException("Unable to load properties from " + PATH_TO_PROPERTIES_FILE);
		}
	}	
		
	private List<NodeDirectory> getNodeDirectories(){
		DbManager dbManager = DbManager.getDbManager();
		List<NodeDirectory> list = dbManager.getList(NodeDirectory.class);
		return list;
	}
	
	public void compactData(Calendar from, Calendar to, COMPACTOR_TYPE compactorType){
		List<NodeDirectory> nodeDirectories = getNodeDirectories();
		for (Iterator iterator = nodeDirectories.iterator(); iterator.hasNext();) {
			NodeDirectory nodeDirectory = (NodeDirectory) iterator.next();
			if (nodeDirectory.getTipo() == NodeDirectory.TYPE_REPLICA || 
				nodeDirectory.getTipo() == NodeDirectory.TYPE_NIC){				
				compactDataNode(nodeDirectory, from, to, compactorType);
			}
		}
	}
	
	private void compactDataNode(NodeDirectory nodeDirectory, Calendar from, Calendar to, COMPACTOR_TYPE compactorType){
		String fqn = nodeDirectory.getFqnString();
		logger.debug("Compacting data from node: ="+fqn+"\n From:"+calendar2String(from)+" To:"+calendar2String(to));
		String groupedBy;
		if (compactorType.equals(COMPACTOR_TYPE.HOUR_TYPE)){
			groupedBy = " group by ms.year, ms.month, ms.day, ms.hour, ms.associatedObject.internalId ";
		}
		else{
			groupedBy = " group by ms.year, ms.month, ms.day, ms.associatedObject.internalId ";
		}
		TypeEntityEnum[] typeEntities = TypeEntityEnum.values();
		OtherTypeEntityEnum[] otherTypeEntities = OtherTypeEntityEnum.values();
		ArrayList<String> AllTypeEntities = new ArrayList<String>();
		for (TypeEntityEnum typeEntity : typeEntities) {
			AllTypeEntities.add(typeEntity.name());
		}
		for (OtherTypeEntityEnum otherTypeEntity : otherTypeEntities) {
			AllTypeEntities.add(otherTypeEntity.name());
		}
		
		EntityManager em= dbManager.getEntityManager();
		EntityTransaction t =em.getTransaction();
	
		try{
		
			for (int i = 0; i < AllTypeEntities.size(); i++) {
				String measures = properties.getProperty(AllTypeEntities.get(i) + ".measures"); // ie. "cpu.measures"
				String measureType[] = measures.split(",");
				for (int j = 0; j < measureType.length; j++) {
					Query queryResult0 = em.createQuery(
							"select ms.value from MonitoringSample ms " +
							" where ms.associatedObject.fqn = :subject" +
							" and ms.measureType= '" + measureType[j] +"'"+
							" and ms.datetime between :from and :to ");
					queryResult0.setParameter("subject", fqn);
					queryResult0.setParameter("from", from.getTime());
					queryResult0.setParameter("to", to.getTime());
					
					int numInitialRecors = queryResult0.getResultList().size();
					Query queryResult = em.createQuery(
					"select avg(ms.value), min(ms.datetime), min(ms.measureType), max(ms.unit) from MonitoringSample ms " +
					" where ms.associatedObject.fqn = :subject" +
					" and ms.measureType= '" + measureType[j] +"'"+
					" and ms.datetime between :from and :to " +
					groupedBy+
					" order by ms.datetime desc");
					
					queryResult.setParameter("subject", fqn);
					queryResult.setParameter("from", from.getTime());
					queryResult.setParameter("to", to.getTime());
					
					List<Object[]> list = queryResult.getResultList();
					int numGroupedRecors = queryResult.getResultList().size();
					if (numInitialRecors>numGroupedRecors){
						logger.debug("measureType:"+ measureType[j]+". Compacting from "+numInitialRecors+" records to "+list.size());
						Set<MonitoringSample> monitoringSamples = new HashSet<MonitoringSample>();
						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							Object[] data = (Object[]) iterator.next();
							Double value = (Double) data[0];
							Date date = (Date) data[1];
							String type = (String) data[2];
							String unit = (String) data[3];
							
							MonitoringSample ms = new MonitoringSample(nodeDirectory,date,type,value.toString(),unit);
							
							monitoringSamples.add(ms);
						}
						
						// Se borran los datos
						t.begin();
						queryResult = em.createQuery("delete from MonitoringSample " +
								" where associatedObject.internalId = :objectId" + 
								" and measureType = :type" + 
								" and datetime between :from and :to ");
	
						queryResult.setParameter("objectId", nodeDirectory.getObjectId());
						queryResult.setParameter("type", measureType[j]);
						queryResult.setParameter("from", from.getTime());
						queryResult.setParameter("to", to.getTime());
						queryResult.executeUpdate();
						t.commit();
						
	
						// Se almacenan los nuevos datos compactados
						t.begin();
						dbManager.save(monitoringSamples);
						t.commit();
					}
				}
			}		
		} catch (Error e) {
			e.printStackTrace();
			t.rollback();
			throw e;
		} finally {
			em.clear();
			em.close();
		}
	}

	public static String calendar2String(Calendar cal){
		String DATE_FORMAT = "dd/MM/yyyy kk:mm.ss";
		if (cal!=null){
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);	    
			return sdf.format(cal.getTime());
		}
		return "";		
	}

	public static void defaultCompact() {

		Calendar t1 = new GregorianCalendar();
		t1.add(Calendar.MONTH, -6);
		t1.set(Calendar.HOUR_OF_DAY, 0);
		t1.set(Calendar.MINUTE, 0);
		t1.set(Calendar.SECOND, 0);
		
		Calendar t2 = new GregorianCalendar();
		t2.add(Calendar.MONTH, -3);
		t2.set(Calendar.HOUR_OF_DAY, 0);
		t2.set(Calendar.MINUTE, 0);
		t2.set(Calendar.SECOND, 0);

		Calendar t3 = new GregorianCalendar();
		t3.add(Calendar.DAY_OF_YEAR, -7);
		t3.set(Calendar.HOUR_OF_DAY, 0);
		t3.set(Calendar.MINUTE, 0);
		t3.set(Calendar.SECOND, 0);

		logger.debug("compactData HourInterval. From:"+calendar2String(t2)+" To:"+calendar2String(t3));
		logger.debug("compactData DayInterval. From:"+calendar2String(t1)+" To:"+calendar2String(t2));
		Compactor compactor = new Compactor();
		compactor.compactData(t2, t3, COMPACTOR_TYPE.HOUR_TYPE);
		compactor.compactData(t1, t2, COMPACTOR_TYPE.DAY_TYPE);
	}
	
	private static Calendar getCalendar(String parameter) {
		int parameterType;
		if (parameter.endsWith("d")){
			parameter = parameter.substring(0,parameter.length()-1);
			parameterType = Calendar.DAY_OF_YEAR;
		}
		else if (parameter.endsWith("m")){
			parameter = parameter.substring(0,parameter.length()-1);
			parameterType = Calendar.MONTH;			
		}
		else{
			System.err.println("Error in from/to parameter. Parameter:"+parameter);
			info();
			return null;
		}
		Calendar calendar = null;
		try{
			int timeAgo = Integer.parseInt(parameter);
			calendar = new GregorianCalendar();
			calendar.add(parameterType, timeAgo*-1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}catch (Exception e) {
			System.err.println("Error in from/to parameter. Parameter:"+parameter);
			info();
		}
		return calendar;

	}	
	public static void compactAllData(String from, String to, String compactType) {
		if (compactType.equals("hour")){
			Calendar dateFrom = getCalendar(from);
			Calendar dateTo = getCalendar(to);
			if (dateFrom!=null && dateTo!=null){
				logger.debug("compactData HourInterval. From:"+calendar2String(dateFrom)+" To:"+calendar2String(dateTo));	
				Compactor compactor = new Compactor();				
				compactor.compactData(dateFrom, dateTo, COMPACTOR_TYPE.HOUR_TYPE);
			}
		}
		else if (compactType.equals("day")){
			Calendar dateFrom = getCalendar(from);
			Calendar dateTo = getCalendar(to);
			if (dateFrom!=null && dateTo!=null){
				logger.debug("compactData DayInterval. From:"+calendar2String(dateFrom)+" To:"+calendar2String(dateTo));					
				Compactor compactor = new Compactor();
				compactor.compactData(dateFrom, dateTo, COMPACTOR_TYPE.DAY_TYPE);
			}
		}
		else{
			System.err.print("Error in compactType. CompactType param:"+compactType);
			info();
		}
	}
	
	public static void info() {
		System.out.println("Usage:");
		System.out.println("\tCompactor -d Default compaction. [(-f 6m -t 3m -c day) and (-f 3m -t 7d -c hour)]");
		System.out.println("\tCompactor -f (number of days or months) -t (number of days or months) -c [h|d](compact to hour interval or day interval).");
		System.out.println("\t\t -f Number of days/monts to compact From. Example: -f 7d (From 7 days ago), -f 2m From 2 Months ago");
		System.out.println("\t\t -t Number of days/monts to compact To. Example: -t 1d (To 1 day ago), -f 1m To 1 Month ago");
		System.out.println("\t\t -c Options: -c hour .Compact data to hour interval. -c day .Compact data to day interval");
	}
	public static void main(String[] args) {
		if (args.length ==1){
			if ((args[0].equals("-d")) ||
				 (args[0].equals("-default"))){
				defaultCompact();
			}
		} else if (args.length == 6){
			String from = null;
			String to = null;
			String compactType = null;
			if (args[0].equals("-f")){
				from = args[1];
			}
			if (args[2].equals("-t")){
				to = args[3];
			}
			if (args[4].equals("-c")){
				compactType = args[5];
			}
			if (from!= null && to != null && compactType != null){
				compactAllData(from, to, compactType);
			}
			else{
				info();
			}
		}else{
			info();
		}		
	}
}
