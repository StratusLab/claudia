package com.telefonica.claudia.paastests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.slm.paas.PaasUtils;

public class ScaleDownTest {
	
	public static void main (String arg [] )
	{
		String xmlFileName = "src/test/resources/glitesiteovf-1.8.13.xml";

		Parser p=null;
			try{
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			 p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "ANONYMOUSSERVICE");
			p.parse();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

			// Manually populate the replicas to continue the test
			ServiceApplication sa = p.getServiceApplication();
			Iterator<VEE> vees = sa.getVEEs().iterator();
			VEE ce = vees.next();
			VEE wn = vees.next();
			VEE se = vees.next();
			ce.setBalancer(true);
			System.out.println (wn.getFQN().toString());

			VEEReplica veece = new VEEReplica(ce);
			VEEReplica veese= new VEEReplica(se);
			VEEReplica veewn= new VEEReplica(wn);
			VEEReplica veewn2= new VEEReplica(wn);

			ce.registerVEEReplica(veece);
			se.registerVEEReplica(veese);
			wn.registerVEEReplica(veewn);
			wn.registerVEEReplica(veewn2);
			wn.addCurrentReplicas();
			wn.addCurrentReplicas();
			
			ReservoirDirectory.getInstance().registerObject(wn.getFQN(), wn);
			ReservoirDirectory.getInstance().registerObject(veewn.getFQN(), veewn);
			
			ReservoirDirectory.getInstance().registerObject(ce.getFQN(), ce);
			ReservoirDirectory.getInstance().registerObject(veece.getFQN(), veece);
			
			System.out.println ("numero de replicas " + wn.getCurrentReplicas());
			for (VEE vee: sa.getVEEs())
			{
				for (NICConf nic: vee.getNICsConf())
				{
					Network net = nic.getNetwork();
					net.setNetworkAddresses( new String [] {"62.217.120.164", "245.456.456.456"});
				}
			}
			FSM fsm = new FSM ();
			fsm.elasticityRemoveReplica(wn.getFQN().toString(), 0);
	         
	            
			 
	}

}
