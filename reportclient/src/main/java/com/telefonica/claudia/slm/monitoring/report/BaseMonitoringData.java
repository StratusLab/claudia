package com.telefonica.claudia.slm.monitoring.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.telefonica.claudia.slm.monitoring.FrequencyEnum;
import com.telefonica.claudia.slm.monitoring.report.MonitoringSampleData;
import com.telefonica.claudia.slm.monitoring.report.Utils;

/**
 * Hashmap wrapper.
 */
public class BaseMonitoringData {
	private Map<String, MonitoringSampleData[]> data = new HashMap<String, MonitoringSampleData[]>();
	private String fqn;
	
	public BaseMonitoringData(String fqn){
		this.fqn = fqn;
	}
	
	public void put(String measure, MonitoringSampleData[] samples) {
		data.put(measure, samples);
	}
	
	public String getFQN() {
		return fqn;
	}
	
	public void setFQN(String fqn) {
		this.fqn = fqn;
	}	
	
	public MonitoringSampleData[] get(String measure) {
		return data.get(measure);
	}
	
	public Set<String> keys() {
		return data.keySet();
	}
	
	public void completeData(FrequencyEnum frequency, Calendar day, int minutesInterval){
		Set<String> keys = keys();
			
		
		for (String measure : keys) {
			MonitoringSampleData[] samples = data.get(measure);
			MonitoringSampleData[] aux = new MonitoringSampleData[samples.length];
			// Ponemos los datos en orden de fecha incremental. (Nos llegan a la inversa)
			
			SortedSet<MonitoringSampleData> samplesOrdered = new TreeSet<MonitoringSampleData>();			
			samplesOrdered.addAll(Arrays.asList(samples));
			Iterator<MonitoringSampleData> iterator = samplesOrdered.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				aux[i] = (MonitoringSampleData) iterator.next();
				i++;
			}
								
			// Completamos datos 
			int contador = 0;
			ArrayList<MonitoringSampleData> datosCompletados = new ArrayList<MonitoringSampleData>();
			Calendar init = Utils.getInitDate(frequency, day);
			Calendar finish = Utils.getFinishDate(frequency, day);
			
			while (init.compareTo(finish)<=0){
				if (aux.length == 0 || aux.length < contador){
					MonitoringSampleData empty = new MonitoringSampleData();
					empty.setValue("0");
					empty.setTimestamp(init.getTime());
					empty.setUnit(aux[0].getUnit());
					datosCompletados.add(empty);
				} 
				else if (aux[contador].getTimestamp().after(init.getTime())){			
					MonitoringSampleData empty = new MonitoringSampleData();
					empty.setValue("0");
					empty.setTimestamp(init.getTime());
					empty.setUnit(aux[0].getUnit());
					datosCompletados.add(empty);
				}
				else {
					datosCompletados.add(aux[contador]);
					contador++;
				}
				init.add(Calendar.MINUTE, minutesInterval);
			}
			put(measure, datosCompletados.toArray(new MonitoringSampleData[0]));
		}
	}
}
