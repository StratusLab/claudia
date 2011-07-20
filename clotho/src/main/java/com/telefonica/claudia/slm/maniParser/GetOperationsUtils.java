/**
 * 
 */
package com.telefonica.claudia.slm.maniParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.Disk;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;

/**
 * @author henar
 *
 */
public class GetOperationsUtils {

	public static Element getVirtualHardwareSystem (Document doc, String href, VEEReplica replicat )
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
			else if (network.getNICConf().getNetwork().getNetworkAddresses()!=null)
			{
				if ( network.getNICConf().getNetwork().getNetworkAddresses()[0]!=null)
				ip = network.getNICConf().getNetwork().getNetworkAddresses()[0];
			}
			Element itemnetwork = getItemElement (doc, href,  count++, 3, 0, null  ,ip);
			virtualHardware.appendChild(itemnetwork);
		}
		
		return virtualHardware;
	
	}
	
	public static Element getItemElement (Document doc, String href, int instance, int type, int value, String diskhost, String ip)
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
