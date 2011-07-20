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
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.PaasUtils;

public class PaaSObtainIP {
	
	public static void main (String arg [])
	{
		String xmlFileName = "src/test/resources/sunpaas.xml";

		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "ANONYMOUSSERVICE");
			p.parse();

			// Manually populate the replicas to continue the test
			ServiceApplication sa = p.getServiceApplication();
			
			Iterator<VEE> vees = sa.getVEEs().iterator();
			VEE master = vees.next();
			VEE executor = vees.next();

			VEEReplica vee1 = new VEEReplica(master);
			VEEReplica vee2 = new VEEReplica(executor);

			master.registerVEEReplica(vee1);
			executor.registerVEEReplica(vee2);
			
			VEE veetest = null;
			for (VEE vee: sa.getVEEs())
			{
				veetest = vee;
				for (NICConf nic: vee.getNICsConf())
				{
					Network net = nic.getNetwork();
					net.setNetworkAddresses( new String [] {"10.76.56.54", "245.456.456.456"});
				}
			}
			Document doc = sa.toXML();
			
			PaasUtils paas = new PaasUtils();
			
			
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
			
		   String [] argd = paas.getServicePaaSIpFromXML (stringOut.toString());
			for (int i=0; i<argd.length; i++)
			{
				System.out.println (argd[i]);
			}
			
			doc = veetest.toXML();
			
			format    = new OutputFormat (doc); 
            // as a String
             stringOut = new StringWriter ();    
            serial   = new XMLSerializer (stringOut, 
                                                        format);
            try {
    			serial.serialize(doc);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            // Display the XML
            System.out.println("XML " + stringOut.toString());
            String  tt = paas.getVeePaaSIpFromXML (stringOut.toString());
            System.out.println (tt);
            
		
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
