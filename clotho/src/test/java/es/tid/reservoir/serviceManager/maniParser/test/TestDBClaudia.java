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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.slm.common.DbManager;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.maniParser.ManiParserException;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.serviceconfiganalyzer.ServiceConfigurationAnalyzer;


public class TestDBClaudia {

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
			
			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "ANONYMOUSSERVICE");
			p.parse();

			// Manually populate the replicas to continue the test
			ServiceApplication sa = p.getServiceApplication();
			Iterator<VEE> vees = sa.getVEEs().iterator();
			
			for (VEE vee: sa.getVEEs() )
			{
			   for (Product product: vee.getProducts())
			   {
				   System.out.println (product.getFQN());
			   }
			}
			DbManager.getDbManager().save(sa.getFQN());
			 List<ServiceApplication> sas =  DbManager.getDbManager().getList (ServiceApplication.class);
			 System.out.println ("ANTES");
			 for (ServiceApplication s: sas)
			 {
			 System.out.println (s.getSerAppName());
			 }
			 DbManager.getDbManager().remove(sa.getFQN());
			 System.out.println ("DESPUES");
			  sas =  DbManager.getDbManager().getList (ServiceApplication.class);
			 for (ServiceApplication s: sas)
			 {
			 System.out.println (s.getSerAppName());
			 }
			
			
			
			
		

			
			
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
