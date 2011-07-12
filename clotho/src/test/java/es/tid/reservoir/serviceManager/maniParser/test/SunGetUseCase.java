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
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.Disk;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.maniParser.ManiParserException;
import com.telefonica.claudia.slm.maniParser.Parser;
import com.telefonica.claudia.slm.serviceconfiganalyzer.ServiceConfigurationAnalyzer;


public class SunGetUseCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testParser2_sun() {

		String xmlFileName = "src/test/resources/sun.xml";

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
			 Document doc = sa.toXML();
		/*	for(VEE vee : sa.getVEEs()) { 
	    		
				VEE veetest = vee;
				veetest.toXML();
				
	    	}*/
          
			
		/*	DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder;
	        Document doc;
	    	
	        String organizationId = SMConfiguration.getInstance().getSiteRoot().replace(".", "_");
	      
				docBuilder = dbfac.newDocumentBuilder();
				
				doc = docBuilder.newDocument();
				
				VEE veetest = null;
				for(VEE vee : sa.getVEEs()) { 
		    		
					veetest = vee;
					
		    	}
				
			VEEReplica replicat = null;
			for (VEEReplica replica: veetest.getVEEReplicas())
			{replicat = replica;}
            
		
			for (NIC nic: replicat.getNICs())
			{
				nic.addIPAddress("10.87.67.65");
			}
            Element veeReplicaElement = doc.createElement("VApp");
			//veeChildren.appendChild(veeReplicaElement);
            doc.appendChild(veeReplicaElement);
            
			veeReplicaElement.setAttribute("name", vee1.getFQN().toString());
			
			
			veeReplicaElement.setAttribute("href", "@HOSTNAME/api/org/"+organizationId+"/vdc/" + sa.getCustomer().getCustomerName() + "/vapp/" + sa.getSerAppName() +
											"/" + "veename"+ "/" + "veeReplica.getId()");
			
			Element linkVeeReplica = doc.createElement("Link");
			
			
			linkVeeReplica.setAttribute("rel", "monitor:measures");
			linkVeeReplica.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
			linkVeeReplica.setAttribute("href", "@HOSTNAME/api/org/reservoir/vdc/" + 
					sa.getCustomer().getCustomerName() + "/vapp/" + sa.getSerAppName() + "/" + "getVEEName()" + "/" + "veeReplica.getId()" + "/monitor");
			veeReplicaElement.appendChild(linkVeeReplica);
            
               
      /*      <Link rel="up" href="http://cloud.telefonica.com/api/org/es.tid/vdc/cc1/vapp/ss1/VEEExecutor"/>
            <Link rel="add" type="application/vnd.telefonica.tcloud.instantiateOVFParams+xml" 
            	href="http://cloud.telefonica.com/api/org/es.tid/vdc/cc1/vapp/ss1/VEEExecutor/actions/instantiateOvf"/>
            */
			
		/*	Element networkSection = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","NetworkSection");
			Element info = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Info");
			networkSection.appendChild(info);

			for (NIC nic:	replicat.getNICs())
			{
				Element network = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Network");
				network.setAttribute("name", ""+nic.getId());
				networkSection.appendChild(network);
				
			}
			
			veeReplicaElement.appendChild(networkSection);
			
			Element virtualHardware = getVirtualHardwareSystem(doc, "@HOSTNAME/api/org/"+organizationId+"/vdc/" + sa.getCustomer().getCustomerName() + "/vapp/" + sa.getSerAppName() +
					"/" + "veename"+ "/" + "veeReplica.getId()", replicat);
			veeReplicaElement.appendChild(virtualHardware);*/

		
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

	public Element getVirtualHardwareSystem (Document doc, String href, VEEReplica replicat )
	{
		Element virtualHardware = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","VirtualHardwareSection");
		Element virtualHardwarelink = doc.createElement("Link");
		virtualHardwarelink.setAttribute("rel", "add");
		virtualHardwarelink.setAttribute("type", "application/ovf.item+xml");
		virtualHardwarelink.setAttribute("href", href+"/hw/");
		
		virtualHardware.appendChild(virtualHardwarelink);
		
		
		int count = 1;
		Element itemcpu = getItemElement (doc, href,count++, 0, replicat.getCPUs().size(), null, null);
		virtualHardware.appendChild(itemcpu);
		Element itemmemory = getItemElement (doc, href, count++, 1, replicat.getMemory().getMemoryConf().getCapacity(), null, null);
		virtualHardware.appendChild(itemmemory);
	
		for (Disk disk:	replicat.getDisks())
		{
			Element itemdisk = getItemElement (doc, href,  count++, 2, disk.getDiskConf().getCapacity(), "ovf://disk/" + replicat.getVEE().getVEEName()   ,null);
			virtualHardware.appendChild(itemdisk);
		}
		
		for (NIC network:	replicat.getNICs())
		{
			String ip = null;
			if (network.getIPAddresses().size()!=0)
				ip = network.getIPAddresses().get(0);
			Element itemnetwork = getItemElement (doc, href,  count++, 3, 0, null  ,ip);
			virtualHardware.appendChild(itemnetwork);
		}
		
		return virtualHardware;
	
	}
	
	public Element getItemElement (Document doc, String href, int instance, int type, int value, String diskhost, String ip)
	{
		// "@HOSTNAME/api/org/"+organizationId+"/vdc/" + sa.getCustomer().getCustomerName() + "/vapp/" + sa.getSerAppName() +
		//"/" + "veename"+ "/" + "veeReplica.getId()"
		Element item = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Item");
		Element itemlink = doc.createElement("Link");
		itemlink.setAttribute("rel", "edit");
		itemlink.setAttribute("type", "application/ovf.item+xml+xml");
		itemlink.setAttribute("href", href+"/hw/"+instance);
		item.appendChild(itemlink);
		
		
		Element descriptionCPU = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","Description");
		descriptionCPU.setNodeValue("Number of virtual data");
		Text test1 = doc.createTextNode("Number of virtual data");
		descriptionCPU.appendChild(test1);
		item.appendChild(descriptionCPU);
		
		Element instanceCPU = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","InstanceID");
		Text test2 = doc.createTextNode(""+instance);
		instanceCPU.appendChild(test2);
		item.appendChild(instanceCPU);
		
		Element ResourceTypeCPU = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","ResourceType");
		Text test3 = null;
		instanceCPU.appendChild(test2);
		item.appendChild(instanceCPU);
		switch (type)
		{
	     case 0: // CPU
	    	 test3 = doc.createTextNode(""+3);

		  break;
	     case 1: // Memory
	    	 test3 = doc.createTextNode(""+4);
	 
	    	 break;
	     case 2: // Disk
	    	 test3 = doc.createTextNode(""+17);
	    
	    	 break;
	     case 3: // Network
	    	 test3 = doc.createTextNode(""+10);
	
	    	 break;
		}
		ResourceTypeCPU.appendChild(test3);
		item.appendChild(ResourceTypeCPU);
		
		if (type == 2) // Disk
		{
			Element HostResource = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","HostResource");
			Text test4 = doc.createTextNode(diskhost);
			HostResource.appendChild(test4);
			item.appendChild(HostResource);
			Element Parent = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","Parent");
			Text test5 = doc.createTextNode(""+4);
			Parent.appendChild(test5);
			item.appendChild(Parent);

		}
			
		else if (type == 3 & ip != null)
		{
			Element elementip = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_IPProtocolEndpoint","IPv4Address");
			Text test6 = doc.createTextNode(ip);
			elementip.appendChild(test6);
		
			item.appendChild(elementip);
		}
		
		
		if (value!=0)
		{
		Element VirtualQuantityCPU = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","VirtualQuantity");
		Text test6 = doc.createTextNode(""+value);
		VirtualQuantityCPU.appendChild(test6);
		
		item.appendChild(VirtualQuantityCPU);
		}
		return item;
		

	}
	
	
}
