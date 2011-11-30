/*

 (c) Copyright 2010 Telefonica, I+D. Printed in Spain (Europe). All Righ
 Reserved.

 The copyright to the software program(s) is property of Telefonica I+D.
 The program(s) may be used and or copied only with the express written
 consent of Telefonica I+D or in acordance with the terms and conditions
 stipulated in the agreement/contract under which the program(s) have
 been supplied.

 */

package es.tid.reservoir.serviceManager.maniParser.test;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.slm.common.DbManager;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.maniParser.GetOperationsUtils;
import com.telefonica.claudia.slm.maniParser.ManiParserException;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.vmiHandler.RECManagerClient;
import com.telefonica.claudia.slm.serviceconfiganalyzer.ServiceConfigurationAnalyzer;


public class GetProductSectionTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testParser2_sun() {

		String xmlFileName = "src/test/resources/4caastpaas.xml";

		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "test27");
			p.parse();

			// Manually populate the replicas to continue the test
			ServiceApplication sa = p.getServiceApplication();
			
			
			    	
	Iterator<VEE> vees = sa.getVEEs().iterator();
			VEE master = vees.next();
		//	VEE executor = vees.next();

			VEEReplica vee1 = new VEEReplica(master);
		//	VEEReplica vee2 = new VEEReplica(executor);

			master.registerVEEReplica(vee1);
	//		executor.registerVEEReplica(vee2);
			
			for (VEE vee: sa.getVEEs())
			{
				for (NICConf nic: vee.getNICsConf())
				{
					Network net = nic.getNetwork();
					net.setNetworkAddresses( new String [] {"109.231.79.228", "245.456.456.456"});
				}
			}
			
			Source source = new DOMSource(sa.toXML());
			
			
		
		TransformerFactory transformerFactory =TransformerFactory.newInstance();
			Transformer transformer = null;
			try{
			transformer = transformerFactory.newTransformer();
			}catch (javax.xml.transform.TransformerConfigurationException error){
			
			}
			
			StringWriter writer = new StringWriter();
			Result result = new StreamResult(writer);
			try{
			transformer.transform(source,result);
			}catch (javax.xml.transform.TransformerException error){
		         error.printStackTrace();
			
			}

			
			System.out.println (writer.toString());
			
			
		/*	for (VEE vee: sa.getVEEs() )
			{
			   for (Product product: vee.getProducts())
			   {
				   System.out.println (product.getProductXML());
			   }
			}*/
			
			
			
			/* Let's generate the OVF Environment for VEEMaster and VEEExecutor */
		//	p.generateEnvironments("vm",0,getFreshIPS(),netmasks,dnsServers,gateways,eps);
		

			
			
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());			
		}
	}
	
	HashMap<String,ArrayList<String>> getFreshIPS() {	
		
		ArrayList<String> net1 = new ArrayList<String>();
		net1.add("10.0.0.1");
		ArrayList<String> net2 = new ArrayList<String>();
		net2.add("10.0.1.1");
	
		HashMap<String,ArrayList<String>> ips = new HashMap<String,ArrayList<String>>();
		ips.put("1_sge_net",net1);
		ips.put("0_admin_net",net2);
	
		return ips;
	
	}
	
	
}
