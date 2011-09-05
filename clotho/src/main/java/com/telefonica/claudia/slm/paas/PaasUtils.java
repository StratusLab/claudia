package com.telefonica.claudia.slm.paas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.SectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;

import com.telefonica.claudia.slm.deployment.hwItems.DiskConf;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.maniParser.GetOperationsUtils;
import com.telefonica.claudia.slm.paas.vmiHandler.SDCClient;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;



//import es.test.GetVEE;
public class PaasUtils {

	Logger logger = null;
	HashMap<String,Product> products = new HashMap<String,Product> ();
	
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
	
	public String getIPVeeReplica (VEEReplica veeReplica)
	{
		TCloudClient vmi = new TCloudClient("http://"+SMConfiguration.getInstance().getVEEMHost()+":"+SMConfiguration.getInstance().getVEEMPort());
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
		   if (product == null)
			   continue;
		   product.setVEE(vee);
		   vee.addProduct(product);
			
			if (product.getParentName()!=null)
			{
			   Product parent = products.get(product.getParentName());
			   parent.setProduct(product);
			}
			
			if (product==null)
				   continue;
			
			products.put(product.getName(), product);
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
		product.setVersion(productSection.getVersion().getValue());
		logger.info("Product version " +productSection.getVersion().getValue() );
		}
		if (productSection.getVendor()!=null)
		{
		product.setVendor(productSection.getVendor().getValue());
		logger.info("Product vendor " +productSection.getVendor().getValue() );
		}
		if (productSection.getProductUrl()!=null)
		{
		product.setUrl(productSection.getProductUrl().getValue());
		logger.info("Product url " +productSection.getProductUrl().getValue() );
		}
		
		List<Object> sections = productSection.getCategoryOrProperty();
		String category = "";
		
		for (Object prop : sections)
		{
			
			if (prop instanceof MsgType) //category
			{
				MsgType cat = (MsgType)prop;
				Map<QName, String> map = cat.getOtherAttributes();
				
				Iterator itr = map.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry e = (Map.Entry)itr.next();
					System.out.println("clave: "+e.getKey()+"valor:"+e.getValue());
					category=(String)e.getValue();
				}
				logger.info("Category " + category);
		
				
			}
			if (prop instanceof ProductSectionType.Property)
			{
				if (category.equals("org.4caast.instancecomponent"))
				{
					ProductSectionType.Property property = (ProductSectionType.Property )prop;
					String key = getPropertyKey (property);
					String value = getPropertyValue (property);
					
					logger.info ("Product property " + key + " : " + value);
					
					if (key.indexOf("type")!=-1)
					{
						product.setType(value);
					}
					else if (key.indexOf("id")!=-1)
					{
						product.setName(value);
					}
					else if (key.indexOf("parent")!=-1)
					{
						product.setParentName(value);
					}
				}
				else if (category.equals("org.4caast.instancecomponent.attributes"))
				{
					ProductSectionType.Property property = (ProductSectionType.Property )prop;
					
					String key = getPropertyKey (property);
					String value = getPropertyValue (property);
					logger.info ("Product property " +key + " : " + value);
					
					product.addProperty(new Property(key, value));
				}
			}
			
		}
		
		
		return product;
	}
	

	
	private String getPropertyKey (ProductSectionType.Property property)
	{
		String key = null;
		if (property.getKey()==null)
		{
			Map<QName, String> map = property.getOtherAttributes();
			
			 Iterator itr = map.entrySet().iterator();
			  while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				if ((((QName) e.getKey()).getLocalPart()).equals("key"))
					return (String)e.getValue();
				
				//return ((QName) e.getKey()).getLocalPart();
			  }
		}
		else
			return property.getKey();
		return null;
	}
	
	private String getPropertyValue (ProductSectionType.Property property)
	{
		if (property.getValue()==null || property.getValue().length()==0)
		{
			Map<QName, String> map = property.getOtherAttributes();
			
			 Iterator itr = map.entrySet().iterator();
			  while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				if ((((QName) e.getKey()).getLocalPart()).equals("value"))
					return (String)e.getValue();
				
			//	return (String) e.getValue();
			  }
		}
		else
			return property.getValue();
		return null;
	}
	private String getProductXML (Product product) throws XMLException
	{
		ProductSectionType productsection = new ProductSectionType();
		
		if (product.getName()!=null)
		{
			MsgType productname = new MsgType();
			productname.setValue(product.getName());
			productsection.setProduct(productname);
		}
		
		if (product.getVersion()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(product.getVersion());
			productsection.setVersion(mens);
		}
		
		if (product.getVendor()!=null)
		{
			MsgType mens = new MsgType();
			mens.setValue(product.getVendor());
			productsection.setVendor(mens);
		}
		
		if (product.getUrl()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(product.getUrl());
			productsection.setProductUrl(mens);
		}
		
		for (Property prop : product.getProperties())
		{
			
				ProductSectionType.Property property = new ProductSectionType.Property ();
				property.setKey(prop.getKey());
				property.setValue(prop.getValue());
				productsection.getCategoryOrProperty().add(property);
		}
		
		
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		String ovfproduct = null;
	
		ovfproduct = ovfSerializer.writeXML(productsection);
		return ovfproduct;
	
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
	
	public void installProduct (String url, Product product, String ip) throws Exception
	{
		SDCClient sdc = new SDCClient (url);
		try {
			sdc.installProduct(product, ip);
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			throw new CommunicationErrorException ("Error in the communication " + e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception ("Error" + e.getMessage());
		} 
		
	}

}
