package com.telefonica.claudia.slm.paas;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.hwItems.DiskConf;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
public class PaasUtils {

	
	// PaaS aware ??
	public boolean isPaaSAware (VEE vee)
	{
		boolean imageurl = false;
		for (DiskConf disk: vee.getDisksConf())
		if (disk.getImageURL()!= null)
		{
			imageurl = true;
		}
		return !imageurl;
	}
	
	public String getPaaSIp (VEE vee)
	{
		for (NICConf nic: vee.getNICsConf())
		{
			Network net = nic.getNetwork();
			
			if (!net.getPrivateNet())
			{
				return net.getNetworkAddresses()[0];
			}
		}
		return null;
	}
	
	public String [] getServicePaaSIpFromXML (String xml)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   
		String  [] ips = null;
		int ipcount =0;

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			
			NodeList children =  doc.getElementsByTagName ("Children");
			System.out.println ("element children " + children.getLength());
			ips = new String [children.getLength()-1];
			
			
			for (int i=1;i<children.getLength();i++)
			{
				System.out.println ("Childrens " + children.item(i).getNodeName());
				
				Node vapp =  ((Element)children.item(i)).getElementsByTagName ("VApp").item(0);
			
				Node vh = ((Element)vapp).getElementsByTagName ("VirtualHardwareSection").item(0);
			
				
				Node itemresource = null;
				
				
				
					NodeList items = vh.getChildNodes();
					for (int k=0; k<items.getLength();k++)
					{
						NodeList dd = items.item(k).getChildNodes();
						
						
						
						for (int l=0;l<dd.getLength();l++)
						{
						
							System.out.println (dd.item(l).getNodeName());
							if (dd.item(l).getNodeName().equals("ResourceType") &&
									dd.item(l).getFirstChild().getNodeValue().equals("10"))
							{
								itemresource = items.item(k);
							}
									
						}
					}
					NodeList values = itemresource.getChildNodes();
					for (int l=0;l<values.getLength();l++)
					{
						if (values.item(l).getNodeName().equals("IPv4Address"))
						{
							ips[ipcount] = values.item(l).getFirstChild().getNodeValue();
							ipcount++;
						}
					}
				}
				
		
			
			
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ips;
	 }
	
	public String  getVeePaaSIpFromXML (Document doc)
	{
		Node vapp =  doc.getElementsByTagName ("VApp").item(0);
		String   ips = null;
		Node vh = ((Element)vapp).getElementsByTagName ("VirtualHardwareSection").item(0);
		
			
		Node itemresource = null;
		NodeList items = vh.getChildNodes();
		for (int k=0; k<items.getLength();k++)
		{
			NodeList dd = items.item(k).getChildNodes();
					
					
					
					for (int l=0;l<dd.getLength();l++)
					{
					
						System.out.println (dd.item(l).getNodeName());
						if (dd.item(l).getNodeName().equals("ResourceType") &&
								dd.item(l).getFirstChild().getNodeValue().equals("10"))
						{
							itemresource = items.item(k);
						}
								
					}
				}
				NodeList values = itemresource.getChildNodes();
				for (int l=0;l<values.getLength();l++)
				{
					if (values.item(l).getNodeName().equals("IPv4Address"))
					{
						ips= values.item(l).getFirstChild().getNodeValue();

					}
				}

	
	
	return ips;
		
	}
	public String  getVeePaaSIpFromXML (String xml)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		   
		String   ips = null;
		Document doc = null;

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			 doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			
	   return getVeePaaSIpFromXML ( doc);
			
	}
	
	
}
