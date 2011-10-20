package com.telefonica.claudia.paastests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.DiskConf;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.slm.lifecyclemanager.LifecycleController;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.rulesEngine.RulesEngine;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;

public class PaaSTest {
	
	public static void main (String [] args)
	{
		PaaSTest p = new PaaSTest();
		p.testPaaS();
		Logger logger = Logger.getLogger(PaaSTest.class);
		
		
		
	}
	public void testPaaS ()
	{
		ServiceApplication sa  = null;
	
		String xmlFileName = "4caastpaas5.xml";
		
		
		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("cc1"), "m2");
			p.parse();

			// Manually populate the replicas to continue the test
			sa = p.getServiceApplication();
			completeService (sa);
			
			PaasUtils paas = new PaasUtils();
			LifecycleController lcc = new LifecycleController ();
			FSM fsm = new FSM(lcc, "hola");
			fsm.sap=sa;
		
			fsm.parser = p;
			
			TCloudClient vmi = new TCloudClient("http://localhost:8182");
			fsm.vmi = vmi;
			RulesEngine rle=new RulesEngine(lcc);    

			// "load" the rule engine into the FSM
			fsm.loadRuleEngine(rle);
			for (VEE vee: sa.getVEEs())
			{
				
				for (VEEReplica replica: vee.getVEEReplicas())
				{
				boolean bPaaSAware = paas.isPaaSAware(vee);
				 
				
	
				if (!fsm.startService ())
					return ;
				
				
				if (bPaaSAware)
				{
					String xml = paas.getVEE(replica);
					String ip = paas.getVeePaaSIpFromXML (xml);
					System.out.println ("IP " + ip);
					String [] result = paas.getVeePaaSUserNamePaaswordFromXML(xml);
					System.out.println ("username  " + result[0] + "  pass " + result[1]);
					
					for (Product product: vee.getProducts())
					{
						paas.installProduct("http://109.231.82.11:8080/sdc/",product, ip);
					}
					
				}
			}
			}
			
			
			
			
			 
	} catch (Exception e)
	{
		e.printStackTrace();
	}

	}
	
	
	
	public void completeService (ServiceApplication sa)
	{
		Iterator<VEE> vees = sa.getVEEs().iterator();
		VEE master = vees.next();


		VEEReplica vee1 = new VEEReplica(master);
		
		master.registerVEEReplica(vee1);
		
		
		for (VEE vee: sa.getVEEs())
		{
			for (NICConf nic: vee.getNICsConf())
			{
				Network net = nic.getNetwork();
				net.setNetworkAddresses( new String [] {"10.76.56.54", "245.456.456.456"});
			}
		}
		 Document doc = sa.toXML();
		 
		 OutputFormat format    = new OutputFormat (doc); 
            // as a String
            StringWriter stringOut = new StringWriter ();    
            XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                        format);
            try {
    			serial.serialize(doc);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            // Display the XML
            System.out.println("XML " + stringOut.toString());
            
	}
}
