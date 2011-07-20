package com.telefonica.claudia.paastests;

import org.w3c.dom.Document;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.PaasUtils;


public class IsServicePaaSAware {
	
     public static void main (String args [])
     {
    	 String xmlFileName = "src/test/resources/sunpaas.xml";


 			try
 			{
 			SMConfiguration.loadProperties();
 			
 			System.out.println("------------------------------------------------------------------------------------");
 			System.out.println("procesing " + xmlFileName);
 			
 			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "ANONYMOUSSERVICE");
 			p.parse();
 			
 			

 			// Manually populate the replicas to continue the test
 			ServiceApplication sa = p.getServiceApplication();
 			
 			for (VEE vee: sa.getVEEs())
 			{
 				for (NICConf nic: vee.getNICsConf())
 				{
 					Network net = nic.getNetwork();
 					net.setNetworkAddresses( new String [] {"10.76.56.54", "245.456.456.456"});
 				}
 			}
 			Document doc = sa.toXML();
 			
 			PaasUtils paas = new PaasUtils();
 			
 			for (VEE vee: sa.getVEEs())
 			{
 				System.out.println ("VEE " + vee.getVEEName()+ " " + paas.isPaaSAware(vee));
 			}
 			}
 			catch (Exception e)
 			{
 				e.printStackTrace();
 			}
     }

}
