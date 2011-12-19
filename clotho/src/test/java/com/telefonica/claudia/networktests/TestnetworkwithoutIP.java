package com.telefonica.claudia.networktests;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;


import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.vmiHandler.TCloudClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.NotEnoughResourcesException;
import com.telefonica.claudia.smi.TCloudConstants;


public class TestnetworkwithoutIP {

	public static final String ASSIGNATION_SYMBOL = "=";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static final String ONE_VM_ID = "NAME";
	public static final String ONE_VM_TYPE = "TYPE";
	public static final String ONE_VM_STATE = "STATE";
	public static final String ONE_VM_MEMORY = "MEMORY";
	public static final String ONE_VM_NAME = "NAME";
	public static final String ONE_VM_UUID = "UUID";
	public static final String ONE_VM_CPU = "CPU";
	public static final String ONE_VM_VCPU = "VCPU";

	public static final String ONE_VM_RAW_VMI = "RAW_VMI";

	public static final String ONE_VM_OS = "OS";
	public static final String ONE_VM_OS_PARAM_KERNEL = "kernel";
	public static final String ONE_VM_OS_PARAM_INITRD = "initrd";
	public static final String ONE_VM_OS_PARAM_ROOT = "root";
	public static final String ONE_VM_OS_PARAM_BOOT = "boot";

	public static final String ONE_VM_GRAPHICS = "GRAPHICS";
	public static final String ONE_VM_GRAPHICS_TYPE = "type";
	public static final String ONE_VM_GRAPHICS_LISTEN = "listen";
	public static final String ONE_VM_GRAPHICS_PORT = "port";

	public static final String ONE_VM_DISK_COLLECTION = "DISKS";
	public static final String ONE_VM_DISK = "DISK";
	public static final String ONE_VM_DISK_PARAM_IMAGE = "source";
	public static final String ONE_VM_DISK_PARAM_FORMAT = "format";
	public static final String ONE_VM_DISK_PARAM_SIZE = "size";
	public static final String ONE_VM_DISK_PARAM_TARGET = "target";
	public static final String ONE_VM_DISK_PARAM_DIGEST = "digest";
	public static final String ONE_VM_DISK_PARAM_TYPE = "type";
	public static final String ONE_VM_DISK_PARAM_DRIVER = "driver";


	public static final String ONE_VM_NIC_COLLECTION = "NICS";
	public static final String ONE_VM_NIC = "NIC";
	public static final String ONE_VM_NIC_PARAM_IP = "ip";
	public static final String ONE_VM_NIC_PARAM_NETWORK = "NETWORK";

	public static final String ONE_NET_ID = "ID";
	public static final String ONE_NET_NAME = "NAME";
	public static final String ONE_NET_BRIDGE = "BRIDGE";
	public static final String ONE_NET_TYPE = "TYPE";
	public static final String ONE_NET_ADDRESS = "NETWORK_ADDRESS";
	public static final String ONE_NET_SIZE = "NETWORK_SIZE";
	public static final String ONE_NET_LEASES = "LEASES";
	public static final String ONE_NET_IP = "IP";
	public static final String ONE_NET_MAC = "MAC";



	public static final String ONE_DISK_ID = "ID";
	public static final String ONE_DISK_NAME = "NAME";
	public static final String ONE_DISK_URL = "URL";
	public static final String ONE_DISK_SIZE = "SIZE";

	public static final String ONE_OVF_URL = "OVF";
	public static final String ONE_CONTEXT = "CONTEXT";

	public static final String RESULT_NET_ID = "ID";
	public static final String RESULT_NET_NAME = "NAME";
	public static final String RESULT_NET_ADDRESS = "NETWORK_ADDRESS";
	public static final String RESULT_NET_BRIDGE = "BRIDGE";
	public static final String RESULT_NET_TYPE = "TYPE";

	public static final String MULT_CONF_LEFT_DELIMITER = "[";
	public static final String MULT_CONF_RIGHT_DELIMITER = "]";
	public static final String MULT_CONF_SEPARATOR = ",";
	public static final String QUOTE = "\"";

	private static final int ResourceTypeCPU = 3;
	private static final int ResourceTypeMEMORY = 4;
	private static final int ResourceTypeNIC = 10;
	private static final int ResourceTypeDISK = 17;
	
	private static String networkBridge="";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			SMConfiguration.loadProperties();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		TCloudClient tcloudclient = new TCloudClient("http://84.21.173.28:8182");
		ServiceApplication serviceApplication = new ServiceApplication ();
		Network network = new Network ("es.tid.dd.dd", serviceApplication);
	    network.setFQN("es.tid.customers.otro.services.ddd");
	    network.setNetworkAddresses(new String []{"192.168.168.128","255.255.255.248" });
	    Document doc = null;
		try {
			doc = tcloudclient.getTCloudNetworkParametersNotIpManagement(network);
		//	doc = tcloudclient.getTCloudNetworkParameters(network);
			
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      

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
        
        TestnetworkwithoutIP d = new TestnetworkwithoutIP();
		try {
			System.out.println ("ONE " + d.TCloud2ONENet(stringOut.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       Set<Network> networks = new HashSet ();
        networks.add(network);
        try {
			tcloudclient.allocateNetwork(networks);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughResourcesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       
		


	}
	
	protected  String TCloud2ONENet(String xml) throws Exception {


		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));


			Element root = (Element) doc.getFirstChild();
			String fqn = root.getAttribute(TCloudConstants.ATTR_NETWORK_NAME);
			StringBuffer allParametersString  = new StringBuffer();
			
			NodeList macEnabled = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_MAC_ENABLED);
			Element firstmacenElement = (Element)macEnabled.item(0);
			String macenabled = null;
			if (firstmacenElement!=null)
			{
			NodeList textMacenList = firstmacenElement.getChildNodes();
			if (((Node)textMacenList.item(0))!=null)
			 macenabled= ((Node)textMacenList.item(0)).getNodeValue().trim();
			}

			NodeList netmaskList = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_NETMASK);
			NodeList baseAddressList = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_BASE_ADDRESS);
			
			NodeList ipLeaseList = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_IPLEASES);
			
			if (baseAddressList.getLength()==0)
			{
				allParametersString.append(getTCloud2FixedONENet (fqn));
			}
			else if (ipLeaseList.getLength()==0)
			{
				int size = 0;
				if (netmaskList.getLength() >0)
				{
				   size = getSizeNetwork ((Element) netmaskList.item(0));
				}
				allParametersString.append(getTCloud2RangedONENet (fqn, size, baseAddressList.item(0).getTextContent()));
			}
			else if (ipLeaseList.getLength()>0)
			{
				int size = 0;
				if (netmaskList.getLength() >0)
				{
				   size = getSizeNetwork ((Element) netmaskList.item(0));
				}
				allParametersString.append(getTCloud2IPElasedONENet (fqn, size, baseAddressList.item(0).getTextContent(), ipLeaseList));
			}
			
			System.out.println("Network data sent:\n\n" + allParametersString.toString() + "\n\n");

			return allParametersString.toString();

		} catch (IOException e1) {
			System.out.println("OVF of the virtual machine was not well formed or it contained some errors.");
			throw new Exception("OVF of the virtual machine was not well formed or it contained some errors: " + e1.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
			throw new Exception("Error configuring parser: " + e.getMessage());
		} catch (FactoryConfigurationError e) {
			System.out.println("Error retrieving parser: " + e.getMessage());
			throw new Exception("Error retrieving parser: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error configuring a XML Builder.");
			throw new Exception("Error configuring a XML Builder: " + e.getMessage());
		}
	}
	
	public String getTCloud2FixedONENet (String fqn)
	{
		StringBuffer allParametersString  = new StringBuffer();
		// Translate the simple data to RPC format
		allParametersString.append(ONE_NET_NAME).append(ASSIGNATION_SYMBOL).append(fqn).append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_TYPE).append(ASSIGNATION_SYMBOL).append("FIXED").append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_BRIDGE).append(ASSIGNATION_SYMBOL).append(networkBridge).append(LINE_SEPARATOR);
		return allParametersString.toString();
	}
	
	public String getTCloud2RangedONENet (String fqn, int size, String network)
	{
		
		StringBuffer allParametersString  = new StringBuffer();
		// Translate the simple data to RPC format
		allParametersString.append(ONE_NET_NAME).append(ASSIGNATION_SYMBOL).append(fqn).append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_TYPE).append(ASSIGNATION_SYMBOL).append("RANGED").append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_BRIDGE).append(ASSIGNATION_SYMBOL).append(networkBridge).append(LINE_SEPARATOR);
		if (size != 0)
		  allParametersString.append(ONE_NET_SIZE).append(ASSIGNATION_SYMBOL).append(size).append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_ADDRESS).append(ASSIGNATION_SYMBOL).append(network).append(LINE_SEPARATOR);
		return allParametersString.toString();
	}
	
	public String getTCloud2IPElasedONENet (String fqn, int size, String network, NodeList ipLeaseList)
	{
		StringBuffer allParametersString  = new StringBuffer();
		// Translate the simple data to RPC format
		allParametersString.append(ONE_NET_NAME).append(ASSIGNATION_SYMBOL).append(fqn).append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_TYPE).append(ASSIGNATION_SYMBOL).append("FIXED").append(LINE_SEPARATOR);
		allParametersString.append(ONE_NET_BRIDGE).append(ASSIGNATION_SYMBOL).append(networkBridge).append(LINE_SEPARATOR);
		if (size != 0)
		  allParametersString.append(ONE_NET_SIZE).append(ASSIGNATION_SYMBOL).append(size).append(LINE_SEPARATOR);
	//	allParametersString.append(ONE_NET_ADDRESS).append(ASSIGNATION_SYMBOL).append(network).append(LINE_SEPARATOR);
		
		
			for (int i=0; i<ipLeaseList .getLength(); i++){

				Node firstIpLeaseNode = ipLeaseList.item(i);
				if (firstIpLeaseNode.getNodeType() == Node.ELEMENT_NODE){

					Element firstIpLeaseElement = (Element)firstIpLeaseNode;
					NodeList ipList =firstIpLeaseElement.getElementsByTagName(TCloudConstants.TAG_NETWORK_IP);
					Element firstIpElement = (Element)ipList.item(0);
					NodeList textIpList = firstIpElement.getChildNodes();
					String ipString = ("IP="+((Node)textIpList.item(0)).getNodeValue().trim());

					NodeList macList =firstIpLeaseElement.getElementsByTagName(TCloudConstants.TAG_NETWORK_MAC);
					Element firstMacElement = (Element)macList.item(0);
					NodeList textMacList = firstMacElement.getChildNodes();
					String macString = ("MAC="+((Node)textMacList.item(0)).getNodeValue().trim());


					allParametersString.append(ONE_NET_LEASES).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
					allParametersString.append(ipString).append(MULT_CONF_SEPARATOR).append(macString).append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);

				}

			}
		return allParametersString.toString();
	}

    public static int getSizeNetwork (Element netmask)
    {
    	

			if (!netmask.getTextContent().matches("\\d+\\.\\d+\\.\\d+\\.\\d+"))
				throw new IllegalArgumentException("Wrong IPv4 format. Expected example: 192.168.0.0 Got: " + netmask.getTextContent());

			String[] ipBytes = netmask.getTextContent().split("\\.");

			short[] result = new short[4];
			for (int i=0; i < 4; i++) {
				try {
					result[i] = Short.parseShort(ipBytes[i]);
					if (result[i]>255) throw new NumberFormatException("Should be in the range [0-255].");
				} catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Number out of bounds. Bytes should be on the range 0-255.");
				}
			}

			// The network can host 2^n where n is the number of bits in the network address,
			// substracting the broadcast and the network value (all 1s and all 0s).
			int size = (int) Math.pow(2, 32.0-getBitNumber(result));

			if (size < 8)
				size = 8;
			else
				size -= 2;
			
			return size ;
    }

	
	public static int getBitNumber (short[] ip) {

		if (ip == null || ip.length != 4)
			return 0;

		int bits=0;
		for (int i=0; i < 4; i++)
			for (int j=0; j< 15; j++)
				bits += ( ((short)Math.pow(2, j))& ip[i]) / Math.pow(2, j);

		return bits;
	}


}
