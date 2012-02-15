package com.telefonica.claudia.slm.report;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.ServiceKPI;

public class ReportKPIValue implements Runnable{
	private static Logger reportLog = Logger.getLogger("ReportingKPI");
	static {
    	Logger.getLogger("es.tid.reservoir.serviceManager.reporting").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   
    	Logger.getLogger("es.tid.reservoir.serviceManager.reporting").setLevel(Level.INFO);    	
    }

	private ServiceApplication sap = null;
	
	
	public ReportKPIValue ( ServiceApplication sap2)
	{
	
		this.sap=sap2;
	}
	public void run ()
	{
		for (ServiceKPI kpi: sap.getServiceKPIs())
		{
			reportLog.info ("Reporting KPI " + kpi.getKPIName() + " " + kpi.getKPIVmname());	
			
		    MonitoringReportObtainKPI report = null;
				
		    if (kpi.getKPIType().equals("AGENT"))
		    {

		     report = new MonitoringReportObtainKPI(kpi.getKPIType(), sap.getFQN().toString(), kpi.getKPIName(), kpi.getKPIName(),reportLog);
		    }
		    else
		    {
			  report = new MonitoringReportObtainKPI(kpi.getKPIType(), sap.getFQN().toString()+".vees."+kpi.getKPIVmname(), 
					  kpi.getKPIName(), kpi.getKPIName(),reportLog);
		    }
				
		    report.run();
	   }
	}

}
