package com.telefonica.claudia.driver_mon;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValue;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueFilter;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;

public class TCloudMonitoringClient {

	private static Logger logger = Logger.getLogger(TCloudMonitoringClient.class);
	
	private Client client;
	
	public TCloudMonitoringClient() {
		client = new Client(Protocol.HTTP);
	}
	
	public MeasuredValueList getReplicaMeasure(String url, MeasuredValueFilter filter, MeasureDescriptor descriptor) throws MonitorException {
		
		Reference urlTask = new Reference(url);
		
		if (filter.getFrom()!=null)
			urlTask.addQueryParameter("from", DataTypesUtils.date2String(filter.getFrom().getTime()));
		
		if (filter.getTo()!=null)
			urlTask.addQueryParameter("to", DataTypesUtils.date2String(filter.getTo().getTime()));
		
		if (filter.getSamples()!=0)
			urlTask.addQueryParameter("samples", String.valueOf(filter.getSamples()));
		
		if (filter.getInterval()!=0)
			urlTask.addQueryParameter("interval", String.valueOf(filter.getInterval()));
			
		logger.debug("Asking for measures for veereplica at url: " + urlTask);
		
		Response response = client.get(urlTask);
		MeasuredValueList mvl = new MeasuredValueList(descriptor);
		
		switch (response.getStatus().getCode()) {
		
			case 401:	// Unauthorized
			case 403:	// Forbidden
				throw new MonitorException("Forbidden: " + response.getStatus().getDescription());
				
			case 400:	// Bad Request
			case 404:	// Not found
				throw new MonitorException("Bad Request: " + response.getStatus().getDescription());
			
			case 501:
			case 500:
				throw new MonitorException("Internal error: " + response.getStatus().getDescription());
				

			case 202:
			case 201:		
			case 200:

				try {

					DomRepresentation domR = response.getEntityAsDom();
					domR.setNamespaceAware(true);
					Document responseXml = domR.getDocument();
					
					NodeList samples = responseXml.getElementsByTagNameNS("*", "Sample");
					
					for (int i=0; i< samples.getLength(); i++) {
						Element sample= (Element) samples.item(i);
						
						String units = DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT;
						String date= sample.getAttribute("timestamp");
						
						double convertedValue = Double.parseDouble(sample.getAttribute("value"))*DataTypesUtils.getStorageUnitConversion(units);
						
						MeasuredValue mv = new MeasuredValue(""+convertedValue, DataTypesUtils.string2Date(date), units);
						
						mvl.add(mv);
					}
					
				} catch (IOException e) {
					throw new MonitorException("I/O exception reading answer: " + response.getStatus().getDescription());
				} catch (Throwable e) {
					throw new MonitorException("Internal error while decoding the answer: " + e.getMessage());
				}
		}
		
		return mvl;
	}
}
