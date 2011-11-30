package com.telefonica.claudia.context;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.OVFEnvironmentUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.paas.OVFContextualization;
import com.telefonica.claudia.slm.serviceconfiganalyzer.ServiceConfigurationAnalyzer;

public class MacroReplacementTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String xmlFileName = "./src/test/resources/nubaCU5_2.xml";

		try {
			
			SMConfiguration.loadProperties();
			
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("procesing " + xmlFileName);
			
			Parser p = new Parser(xmlFileName, new Customer("SP_STANDARD"), "ANONYMOUSSERVICE");
			p.parse();

			// Manually populate the replicas to continue the test
			ServiceApplication sa = p.getServiceApplication();
			Iterator<VEE> vees = sa.getVEEs().iterator();
			VEE db = vees.next();
			VEE balancer = vees.next();
			VEE fe = vees.next();

			VEEReplica vee1 = new VEEReplica(db);
			VEEReplica vee2 = new VEEReplica(balancer);
			VEEReplica vee3 = new VEEReplica(fe);

			db.registerVEEReplica(vee1);
			balancer.registerVEEReplica(vee2);
			fe.registerVEEReplica(vee3);
			
			
			for (VEE vee: sa.getVEEs())
			{

					for (VEEReplica veeReplica: vee.getVEEReplicas())
					{
						for (NIC nic: veeReplica.getNICs())
						{
							nic.addIPAddress("ip address");
						}
					}
					
					
			
			}
			
			OVFContextualization context = new OVFContextualization();
			
			
			
			
			for (VEE vee: sa.getVEEs())
			{
				for (VEEReplica veeReplica: vee.getVEEReplicas())
				{
					System.out.println (context.macroReplacement (veeReplica));
					
				}
			}
			
			


		
			
			
			
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());			
		}
	
	
	
		
		
		

	/*	File xmlFile = new File("./src/test/resources/nubaCU5.xml");
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		EnvelopeType envelope = null;
		try {
			ovfSerializer.setValidateXML(false);
			envelope = ovfSerializer.readXMLEnvelope(new FileInputStream(xmlFile));
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		org.dmtf.schemas.ovf.envelope._1.ContentType entityInstance = null;
		try {
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
		} catch (EmptyEnvelopeException e) {
			e.printStackTrace();
			return;
		}
		
		if (entityInstance instanceof VirtualSystemCollectionType){
	
			
			List<VirtualSystemType> vss = OVFEnvelopeUtils.getVirtualSystems((VirtualSystemCollectionType)entityInstance);
			
			for (VirtualSystemType vs: vss)
			{
				ArrayList<String> ips = new ArrayList ();
				HashMap<String,ArrayList<String>> maps = new HashMap();
				ips.add("10.33.44.44");
				maps.put("public", ips);
				
				HashMap<String, HashMap<String, String> >  entry = new HashMap();
				
				
				HashMap<String, String> dos = new HashMap();
				dos.put("balancer", "10.33.44.44");
				entry.put("public", dos);
				
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				try {
					OVFEnvironmentUtils.createOVFEnvironment((VirtualSystemType) vs,
							0,
							"es.tid",
							"serv",
							"", 
							maps,
							null,
							null,
							null,
							entry,
							output,
							true);
				} catch (IPNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DNSServerNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NetmaskNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GatewayNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PrecedentTierEntryPointNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotEnoughIPsInPoolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PoolNameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("OVF Environment file for VM [" +  output.toString()+ "] ");
				output.toString();
			}
			
		} */
		
		
			

	}

 public HashMap<String,ArrayList<String>> getFreshIPS() {	
	
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
