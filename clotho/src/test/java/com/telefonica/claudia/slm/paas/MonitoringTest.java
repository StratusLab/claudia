package com.telefonica.claudia.slm.paas;

import java.util.Iterator;

import org.junit.Test;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.maniParser.ManiParserException;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.vmiHandler.NUBAMonitoringClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;


public class MonitoringTest {
	
    @Test
	
	public void setupmonitoring() {
	
	try {
		SMConfiguration.loadProperties();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	String xmlFileName = "src/test/resources/4caastpaascontextualization.xml";

	System.out.println("------------------------------------------------------------------------------------");
	System.out.println("procesing " + xmlFileName);
		
	Parser p = null;
	try {
		p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "test27");
		p.parse();
	} catch (ManiParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	// Manually populate the replicas to continue the test
	ServiceApplication sa = p.getServiceApplication();
	Iterator<VEE> vees = sa.getVEEs().iterator();
	VEE master = vees.next();
	VEE executor = vees.next();

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
	
	
	Monitoring client = new Monitoring ();
	
	client.setupMonitoring(sa);
    }
	

}
