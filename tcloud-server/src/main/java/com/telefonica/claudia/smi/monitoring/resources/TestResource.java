package com.telefonica.claudia.smi.monitoring.resources;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.betwixt.io.BeanWriter;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.MonitoringApplication;
import com.telefonica.claudia.smi.monitoring.MonitoringDriver;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;

//Test with http://localhost:8182/api/org/xxx/vdc/es.tid.customers.testCustomer.services.NOMBRE_SERVICIO/monitor

public class TestResource extends Resource {
	
	private String orgId = "es_tid";
	private String vdcId = "dani";
	private String vappId = "servi";
	private String vappId2 = "VEEMaster";
	private String vappId3 = "1";
	private String hwitemId = "cpus.1";
	
	public static final String TYPE_VDC = "application/vnd.telefonica.tcloud.vdc+xml";

	public TestResource(Context context, Request request, Response response) {
        super(context, request, response);
        
        /*
        orgId = (String) request.getAttributes().get("org-id");
        vdcId = (String) request.getAttributes().get("vdc-id");
        vappId = (String) request.getAttributes().get("vapp-id");
        vappId2 = (String) request.getAttributes().get("vee-id");
        vappId3 = (String) request.getAttributes().get("vm-id");
        hwitemId = (String) request.getAttributes().get("hwitem-id");
        */
        
		System.out.println("org:" + orgId + " -- vdc: " + vdcId + " -- vapp: " + vappId);
		System.out.println("vee:" + vappId2 + " -- vm: " + vappId3 + " -- hwItem: " + hwitemId);

        
        System.out.println("TestResource() created");
        
        getVariants().add(new Variant(MediaType.TEXT_XML));
	}
	
	public Representation represent(Variant variant) throws ResourceException {
		
		System.out.println("TestResource.represent");//log
		
		MonitoringDriver driver = (MonitoringDriver) getContext().getAttributes().get(MonitoringApplication.ATTR_PLUGIN_MONITORING);
		
		MeasureDescriptorList mdl = null;
		try {
			hwitemId = "cpus.1";
			System.out.println("### VDC example ###");
			orgId = "es_tid";
			vdcId = "dani";
			mdl = driver.getVdcMeasureDescriptorList(orgId, vdcId);
			System.out.println(toString(mdl));
			
			System.out.println("### VApps examples ###");
			ArrayList<String> al = new ArrayList<String>();
			
			System.out.println("### Service ###");
			vappId = "servi";
			al.add(vappId);
			mdl = driver.getVappMeasureDescriptorList(orgId, vdcId, al);
			System.out.println(toString(mdl));
			String measureId = "kpis.queueLength";
			MeasureDescriptor mdes = driver.getVappMeasureDescriptor(orgId, vdcId, al, measureId);
			System.out.println(toString(mdes));
		//	MeasuredValueList mvl = driver.getMeasuredValueList(mdes, 10);
		//	System.out.println(toString(mvl));
			
			System.out.println("### VEE ###");
			vappId2 = "VEEMaster";
			al.add(vappId2);
			mdl = driver.getVappMeasureDescriptorList(orgId, vdcId, al);
			System.out.println(toString(mdl));
			
			System.out.println("### Replica ###");
			vappId3 = "1";
			al.add(vappId3);
			mdl = driver.getVappMeasureDescriptorList(orgId, vdcId, al);
			System.out.println(toString(mdl));
			measureId = "cpus.1";
			mdes = driver.getVappMeasureDescriptor(orgId, vdcId, al, measureId);
			System.out.println(toString(mdes));
			//mvl= driver.getMeasuredValueList(mdes, 10);
			//System.out.println(toString(mvl));
			
			System.out.println("### HwItem examples ###");
			mdl = driver.getHwItemMeasureDescriptorList(orgId, vdcId, al, hwitemId);
			System.out.println(toString(mdl));
			
			System.out.println("### Network ###");
			orgId = "es_tid";
			vdcId = "Dani";
			String networkId = "services.Service5.networks.sge_net";
			mdl = driver.getNetMeasureDescriptorList(orgId, vdcId, networkId);
			System.out.println(toString(mdl));
			
			

		} catch (MonitorException e) {
			System.out.println(e.getMessage());
			//return getUnknownElementErrorRepresentation(e.getMessage());
		}
		//setMeasureDescriptorListHrefsLinks(mdl, "application/vnd.telefonica.tcloud.vapp+xml");	
		//return new StringRepresentation(toString(mdl), MediaType.TEXT_XML);
		return new StringRepresentation("Here!", MediaType.TEXT_XML);
	}
	
	private static String toString(Object obj) {
		StringWriter stringWriter = new StringWriter();
		stringWriter.write("<?xml version='1.0' encoding='iso-8859-1' ?>\n");
		
		// Create a BeanWriter which writes to our prepared stream
		BeanWriter beanWriter = new BeanWriter(stringWriter);
		
		// Configure betwixt
		beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
		beanWriter.getBindingConfiguration().setMapIDs(false);
		beanWriter.enablePrettyPrint();
		
		// Date conversion
		//beanWriter.getBindingConfiguration().setObjectStringConverter(new DateStringConverter());
	
		// Write and return the object
		String ret = null;
		try {
			beanWriter.write(obj);
			ret =  stringWriter.toString();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				beanWriter.close();
				stringWriter.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
		
	}
	
}
