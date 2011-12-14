package com.telefonica.claudia.driver_mon;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.junit.Test;

import com.telefonica.claudia.slm.monitoring.MeasurableElement.MeasureDescriptor;
import com.telefonica.claudia.smi.monitoring.MonitorException;
import com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptorList;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValue;
import com.telefonica.claudia.smi.monitoring.bean.MeasuredValueList;


public class SMMonitoringNoJMSDriverTest {
	
	@Test
	public void testdatabase() throws NamingException, ClassNotFoundException, JMSException {
		SMMonitoringNoJMSDriver dd = new SMMonitoringNoJMSDriver (null);
		ArrayList t = new ArrayList ();
		t.add("ss");
		t.add("VEEExecutor");
		t.add("1");
		
		try {
			MeasureDescriptorList me = dd.getVappMeasureDescriptorList ("grnet", "cc", t);
			for (com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor d:me.getMeasureDescriptors())
			{
				System.out.println (  d.getMaxValue());
				System.out.println (d.getMinValue());
				System.out.println (d.getName());
			}
			
			
		} catch (MonitorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			
		}
		
		try {
			com.telefonica.claudia.smi.monitoring.bean.MeasureDescriptor mea = dd.getVappMeasureDescriptor ("grnet", "cc", t, "dd");
			
			System.out.println ("dd" + mea.getMaxValue());
			System.out.println (mea.getMinValue());
			System.out.println (mea.getName());
			
			MeasuredValueList d = dd.getMeasuredValueList(mea, 1);
			
			
		/*	for (MeasuredValue tt:d.getMeasuredValues())
			{
				System.out.println ( "value" +  tt.getValue());
			
			}*/
		} catch (MonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
