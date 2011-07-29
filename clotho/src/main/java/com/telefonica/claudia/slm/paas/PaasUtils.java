package com.telefonica.claudia.slm.paas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;

import com.telefonica.claudia.slm.deployment.hwItems.DiskConf;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.hwItems.Product;
import com.telefonica.claudia.slm.maniParser.GetOperationsUtils;
import com.telefonica.claudia.slm.paas.vmiHandler.SDCClient;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;

//import es.test.GetVEE;
public class PaasUtils {

	Logger logger = null;
	
	public PaasUtils (Logger logger)
	{
		this.logger = logger;
	}
	
	public PaasUtils ()
	{
		
	}
	
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
	
	public String getIPVeeReplica (VEE veeReplica)
	{
		TCloudClient vmi = new TCloudClient(SMConfiguration.getInstance().getSdcUrl());
		String xmlvee = null;
		try {
			xmlvee = vmi.getVEEReplicaXML(veeReplica);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getVeePaaSIpFromXML(xmlvee);
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
	
	public void addProductsToVEE (VirtualSystemType vs,VEE vee )
	{
		List<ProductSectionType> productsSection = null;

		productsSection = OVFEnvelopeUtils.getProductSections(vs);

		
		for (ProductSectionType productSection: productsSection)
		{
		   Product product = addProduct(productSection);
		   if (product==null)
			   continue;
			vee.addProduct(product);
		}
	}
	
/*	public void addOSToVEE (VirtualSystemType vs,VEE vee )
	{
		OperatingSystemSectionType osSection = null;

		try {
			osSection = OVFEnvelopeUtils.getSection(vs, OperatingSystemSectionType.class);
		} catch (SectionNotPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (InvalidSectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		if (osSection.getDescription() != null)
		{
			String os = osSection.getDescription().getValue();
			vee.addOS(os);
		}
		
		
	}*/
	
	private Product addProduct (ProductSectionType productSection)
	{
		if (productSection.getProduct()== null)
			return null;
		Product product = new Product (productSection.getProduct().getValue());
		logger.info("Product name " +productSection.getProduct().getValue() );

		if (productSection.getVersion()!=null)
		{
		product.setProductVersion(productSection.getVersion().getValue());
		logger.info("Product version " +productSection.getVersion().getValue() );
		}
		if (productSection.getVendor()!=null)
		{
		product.setProductVendor(productSection.getVendor().getValue());
		logger.info("Product vendor " +productSection.getVendor().getValue() );
		}
		if (productSection.getProductUrl()!=null)
		{
		product.setProductUrl(productSection.getProductUrl().getValue());
		logger.info("Product url " +productSection.getProductUrl().getValue() );
		}
		
		List<Object> sections = productSection.getCategoryOrProperty();
		
		for (Object prop : sections)
		{
			
			if (prop instanceof ProductSectionType.Property)
			{
				ProductSectionType.Property property = (ProductSectionType.Property )prop;
	
				logger.info ("Product property " + property.getKey() + " : " + property.getValue());
				product.addProperty(property.getKey(), property.getValue());
				
			}
			
		}
		return product;
	}
	
	public static String tooString (Document doc)
	{
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
        return stringOut.toString();
	}
	
	public void installProduct (Product product, String ip)
	{
		SDCClient sdc = new SDCClient ("http://109.231.82.11:8080/sdc/");
		try {
			sdc.installProduct(product, ip);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
