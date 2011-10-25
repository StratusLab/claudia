/**
 * 
 */
package com.telefonica.claudia.smi.provisioning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.SectionAlreadyPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;



/**
 * @author henar
 *
 */
public class GetOperationsUtils {


	public static Element getVirtualHardwareSystem (Document doc, String href, String cpu, String memory, String disk, String ip )
	{
		Element virtualHardware = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","VirtualHardwareSection");
		Element virtualHardwarelink = doc.createElement("Link");
		virtualHardwarelink.setAttribute("rel", "add");
		virtualHardwarelink.setAttribute("type", "application/ovf.item+xml");
		virtualHardwarelink.setAttribute("href", href+"/hw/");
		
		virtualHardware.appendChild(virtualHardwarelink);
		
		
		int count = 1;
		Element itemcpu = getItemElement (doc, href,count++, 0, cpu, null, null);
		virtualHardware.appendChild(itemcpu);
		Element itemmemory = getItemElement (doc, href, count++, 1, memory, null, null);
		virtualHardware.appendChild(itemmemory);
	
		Element itemdisk = getItemElement (doc, href,  count++, 2, disk, "ovf://disk/" +"disk"   ,null);
		virtualHardware.appendChild(itemdisk);
		Element itemnetwork = getItemElement (doc, href,  count++, 3, "0", null  ,ip);
		virtualHardware.appendChild(itemnetwork);
		
		
		return virtualHardware;
	
	}
	
	public static Element getItemElement (Document doc, String href, int instance, int type, String value, String diskhost, String ip)
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
		
		
		if (!value.equals("0"))
		{
		Element VirtualQuantityCPU = doc.createElementNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData","VirtualQuantity");
		Text test6 = doc.createTextNode(""+value);
		VirtualQuantityCPU.appendChild(test6);
		
		item.appendChild(VirtualQuantityCPU);
		}
		return item;
		

	}
	
	public static Element getInstallProductInVirtualMachine(Document doc, String username, String password) throws ParserConfigurationException, XMLException, SAXException, IOException, SectionAlreadyPresentException, InvalidSectionException
	{
      
		Element product = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","ProductSection");

		Element category = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Category");
	    category.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "msgid", "org.fourcaast.rec");
	    category.appendChild(doc.createTextNode("REC Attribute"));
	    product.appendChild(category);
	    
	    Element propertyusername = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Property");
	    propertyusername.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "key", "org.fourcaast.rec.username");
	    propertyusername.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "value", username);
	  
	    product.appendChild(propertyusername);
	    
	    
	    Element propertypassword = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1","Property");
	    propertypassword.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "key", "org.fourcaast.rec.password");
	    propertypassword.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "value", password);
	  
	    product.appendChild(propertypassword);
	    
	    
		return product;

		

	}

}
