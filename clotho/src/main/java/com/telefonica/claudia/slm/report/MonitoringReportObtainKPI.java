package com.telefonica.claudia.slm.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.Rule;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.ServiceKPI;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.report.PersistenceClient;
import com.telefonica.claudia.smi.URICreation;

public class MonitoringReportObtainKPI extends Thread {
	
	private VEE sap= null;
	private String metric = null;
	private String kpi = null;
	private String type = null;
	public MonitoringReportObtainKPI (String type, VEE sap, String metric, String kpi)
	{
		this.metric = metric;
		this.kpi = kpi;
		this.sap=sap;
		this.type=type;
	}
	
	
	
	public  void obtainMetricsValue(String urlmonitor) throws IOException {

		
		// monitor http://84.21.173.142:8182/api/org/CESGA/vdc/test/vapp/dd/node/1/monitor
			
			
		PersistenceClient pc = new PersistenceClient();
		
	//	String monitor = urlmonitor + "/monitor";	
		String monitor = "http://84.21.173.142:8182/api/org/CESGA/vdc/test/vapp/dd/node/1/monitor";
		ArrayList<String> measurelist = pc.findmeasures(monitor);
		for (Iterator iterator2 = measurelist.iterator(); iterator2.hasNext();) {
			String measure = (String) iterator2.next();	

			String valuexml=pc.getmeasure(monitor, measure);
			String measurefqn = monitor+"/"+measure;
				//logger.info(" monitor values: " + monitor+ " "+measure); 
			pc.sendvalue(valuexml,measurefqn,measure, type);

		}

		

	}
	
	static Runnable periodicGetMonValues= new Runnable(){
		public void run() {
			PersistenceClient pc = new PersistenceClient();
			
		}	
	};
	
	public static void main(String args [])
	{
		try {
			SMConfiguration.loadProperties();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String xmlFileName = "src/test/resources/4caastpaascontextualization.xml";
		VEE executor = null;
		VEE master =null;
		ServiceApplication sa=null;

		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("ccc"), "ss");
			p.parse();

			// Manually populate the replicas to continue the test
			sa = p.getServiceApplication();
			Iterator<VEE> vees = sa.getVEEs().iterator();
			master = vees.next();
			executor = vees.next();

			VEEReplica vee1 = new VEEReplica(master);
			VEEReplica vee2 = new VEEReplica(executor);

			master.registerVEEReplica(vee1);
			executor.registerVEEReplica(vee2);
			
			for (VEE vee: sa.getVEEs())
			{
				for (VEEReplica rep: vee.getVEEReplicas())
				{
				for (NIC nic: rep.getNICs())
				{
					
					nic.addIPAddress("109.231.79.228");
					
				}
				}
			}
		}
		catch (Exception e )
		{
			e.printStackTrace();
		}
		
		for (Rule rule: sa.getServiceRules())
		{
			rule.buildRule(true);
		}
	//	for (VEE vee: sa.getVEEs())
	//	{
		//	if (vee.getBalancedBy()!= null)
		//	{
				for (ServiceKPI kpi: sa.getServiceKPIs())
				{
					MonitoringReportObtainKPI report = null;

					report = new MonitoringReportObtainKPI(kpi.getKPIType(), executor, kpi.getKPIName(), kpi.getKPIName());
					
				    report.run();
				}
				
		//	}
			
	//	}
		
	}
	
	public void run() {
		
	/*	String urlmonitor  = null;
		if (type.equals("AGENT"))
		{
		   String uriservice = URICreation.getURIService(sap.getFQN().toString());
		   urlmonitor = "http://"+SMConfiguration.getInstance().getSMIHost() + ":" +
		   SMConfiguration.getInstance().getSMIPort() + uriservice+"/monitor";
		}
		else
		{
			String uriservice = URICreation.getURIVEE(sap.getFQN().toString()+"/1");
			urlmonitor = "http://"+SMConfiguration.getInstance().getSMIHost() + ":" +
			SMConfiguration.getInstance().getSMIPort() + uriservice+"/monitor";
		}
		
		while (true)
		{
		   obtainData(urlmonitor);
		   try{
			   //do what you want to do before sleeping
			   Thread.currentThread().sleep(10000);//sleep for 1000 ms
			   //do what you want to do after sleeptig
			 }
			 catch(Exception ie){
			 //If this thread was intrrupted by nother thread
			 }
		}*/
        
    }
	
	public void obtainData (String urlmonitor)
	{
		PersistenceClient pc = new PersistenceClient();
		if (type.equals("AGENT"))
			metric=kpi;
		

		String valuexml = null;
		try {
			valuexml = pc.getmeasure(urlmonitor, metric);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String measurefqn = urlmonitor+"/"+metric;
			//logger.info(" monitor values: " + monitor+ " "+measure); 
		pc.sendvalue(valuexml,measurefqn,kpi, "AGENT");
	}
		


}
