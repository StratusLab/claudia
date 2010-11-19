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

import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.maniParser.ManiParserException;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.serviceconfiganalyzer.ServiceConfigurationAnalyzer;


public class ThalesUseCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testParser2_thales() {

		String xmlFileName = "src/test/resources/thales.xml";
	
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
			
			HashMap<String,String> netmasks = new HashMap<String,String>();
			netmasks.put("service_network","255.255.255.0");	
			
			// DNS is not used by Thales
			HashMap<String,String> dnsServers = new HashMap<String,String>();
			dnsServers.put("service_network","1.2.3.4");
			
			HashMap<String,String> gateways = new HashMap<String,String>();
			gateways.put("service_network","172.17.100.1");
			
			HashMap<String, HashMap<String,String>> eps = new HashMap<String, HashMap<String,String>>();
			HashMap<String,String> eps1 = new HashMap<String,String>();
			eps1.put("Monitoring", "172.17.10.60");
			eps1.put("Database", "172.17.10.23");
			eps1.put("Load_Balancer", "172.17.10.22 172.17.10.51 172.17.10.52 172.17.10.53"); // Load_Balancer uses 4 IPs in virtual_network1	
			eps.put("virtual_network1", eps1);
			HashMap<String,String> eps2 = new HashMap<String,String>();
			eps2.put("Load_Balancer", "172.17.100.48 172.17.100.50"); // Load_Balancer uses 2 IPs in service_network
			eps.put("service_network", eps2);
						
			// Let's generate the OVF Environment for every VirtualSystem type */
			p.generateEnvironments("Load_Balancer",0,getFreshIPS4LoadBalancer(),netmasks,dnsServers,gateways,eps);
			p.generateEnvironments("Portail",0,null,netmasks,dnsServers,gateways,eps);
			p.generateEnvironments("Service_Map",0,null,netmasks,dnsServers,gateways,eps);			
			p.generateEnvironments("Service_Portal",0,null,netmasks,dnsServers,gateways,eps);
			p.generateEnvironments("Service_Reference",0,null,netmasks,dnsServers,gateways,eps);
			p.generateEnvironments("Database",0,getFreshIPS4Database(),netmasks,dnsServers,gateways,eps);
			p.generateEnvironments("Monitoring",0,getFreshIPS4Monitoring(),netmasks,dnsServers,gateways,eps);
			
			/* Testing SCA */
			ServiceConfigurationAnalyzer sca = new ServiceConfigurationAnalyzer();
			HashMap<String,String> hm = sca.getBuildingBlockAvailabilyLabel(sa);
			for (Iterator<String> i = hm.keySet().iterator(); i.hasNext() ; ) {
				String vee = i.next();
				// FIXME: use the proper logger
				System.out.println("availability for VEE " + vee + ": " + hm.get(vee));
			}
			
			hm = sca.getBuildingBlockCapacityLabel(sa);
			for (Iterator<String> i = hm.keySet().iterator(); i.hasNext() ; ) {
				String vee = i.next();
				// FIXME: use the proper logger
				System.out.println("building block for VEE " + vee + ": " + hm.get(vee));
			}
			
			HashMap<String,ArrayList<String>> hml = sca.getAllowableSites(sa);
			for (Iterator<String> i = hml.keySet().iterator(); i.hasNext() ; ) {
				String vee = i.next();
				// FIXME: use the proper logger
				String sites = "";
				for (Iterator<String> ii = hml.get(vee).iterator(); ii.hasNext();  ) {
					String site = ii.next();
					sites += site + " ";
				}
				
				System.out.println("allowed sites for VEE " + vee + ": " + sites);
			}
			
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}
	
	HashMap<String,ArrayList<String>> getFreshIPS4LoadBalancer() {	
		
		ArrayList<String> net1 = new ArrayList<String>();
		net1.add("172.17.10.22");
		net1.add("172.17.10.51");
		net1.add("172.17.10.52");
		net1.add("172.17.10.53");
		ArrayList<String> net2 = new ArrayList<String>();
		net2.add("172.17.100.48");
		net2.add("172.17.100.50");
	
		HashMap<String,ArrayList<String>> ips = new HashMap<String,ArrayList<String>>();
		ips.put("virtual_network1",net1);
		ips.put("service_network",net2);
	
		return ips;
	
	}
	
	HashMap<String,ArrayList<String>> getFreshIPS4Monitoring() {	
		
		ArrayList<String> net1 = new ArrayList<String>();
		net1.add("172.17.10.60");
		ArrayList<String> net2 = new ArrayList<String>();
		net2.add("172.17.100.60");
	
		HashMap<String,ArrayList<String>> ips = new HashMap<String,ArrayList<String>>();
		ips.put("virtual_network1",net1);
		ips.put("service_network",net2);
	
		return ips;
	
	}
	
	HashMap<String,ArrayList<String>> getFreshIPS4Database() {	
		
		ArrayList<String> net1 = new ArrayList<String>();
		net1.add("172.17.10.23");
		ArrayList<String> net2 = new ArrayList<String>();
		net2.add("172.17.100.49");
	
		HashMap<String,ArrayList<String>> ips = new HashMap<String,ArrayList<String>>();
		ips.put("virtual_network1",net1);
		ips.put("service_network",net2);
	
		return ips;
	
	}
	
}
