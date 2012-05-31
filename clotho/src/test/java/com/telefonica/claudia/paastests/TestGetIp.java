package com.telefonica.claudia.paastests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.PaasUtils;

public class TestGetIp {
	
	public static void main (String args []){
		
		String xmlFileName = "src/test/resources/4caastpaas.xml";
		ServiceApplication sa = null;

		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "test27");
			p.parse();

			// Manually populate the replicas to continue the test
			sa = p.getServiceApplication();
			Iterator<VEE> vees = sa.getVEEs().iterator();
			VEE master = vees.next();
		//	VEE executor = vees.next();

			VEEReplica vee1 = new VEEReplica(master);
		//	VEEReplica vee2 = new VEEReplica(executor);

			master.registerVEEReplica(vee1);
	//		executor.registerVEEReplica(vee2);
			
			
		}
		catch (Exception e)
		{e.printStackTrace();}
	
	PaasUtils paas = new PaasUtils ();
	
	DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
	try {
		docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse("./src/test/resources/vapp.xml");
		HashMap ip = paas.getVeePaaSSetIpFromXML (doc);
		
		Iterator it = ip.entrySet().iterator();
		
		while (it.hasNext()) {
		  Map.Entry e = (Map.Entry)it.next();
		  System.out.println(e.getKey() + " " + e.getValue());
		  for (VEE vee: sa.getVEEs())
			{
				
			    for (VEEReplica replica: vee.getVEEReplicas())
			    {
			    for (NIC nic: replica.getNICs())
				{
					System.out.println (" Redes .. " + nic.getNICConf().getNetwork().getName());
					
					if (e.getKey().equals(nic.getNICConf().getNetwork().getName()))
					{
					    System.out.println ("Anyadiendo ip .. " +  e.getValue());
						nic.addIPAddress((String)e.getValue());
					}
			    	
			    //	Network net = nic.getNetwork();
				//	net.setNetworkAddresses( new String [] {"109.231.79.228", "245.456.456.456"});
				}
			    }
			}
		  }
	} catch (ParserConfigurationException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	
	

   }
}
