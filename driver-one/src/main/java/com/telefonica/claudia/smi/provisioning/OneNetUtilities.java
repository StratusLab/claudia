package com.telefonica.claudia.smi.provisioning;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.telefonica.claudia.smi.TCloudConstants;

public class OneNetUtilities {

	public static final String ONE_NET_ID = "ID";
	public static final String ONE_NET_NAME = "NAME";
	public static final String ONE_NET_BRIDGE = "BRIDGE";
	public static final String ONE_NET_TYPE = "TYPE";
	public static final String ONE_NET_ADDRESS = "NETWORK_ADDRESS";
	public static final String ONE_NET_SIZE = "NETWORK_SIZE";
	public static final String ONE_NET_LEASES = "LEASES";
	public static final String ONE_NET_IP = "IP";
	public static final String ONE_NET_MAC = "MAC";
	
	public static final String ASSIGNATION_SYMBOL = "=";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final String MULT_CONF_LEFT_DELIMITER = "[";
	public static final String MULT_CONF_RIGHT_DELIMITER = "]";
	public static final String MULT_CONF_SEPARATOR = ",";
	public static final String QUOTE = "\"";

	private String networkBridge = null;

	public OneNetUtilities (String networkBridge)
	{
		this.networkBridge = networkBridge;
	}
	protected  String TCloud2ONENet(String xml) throws Exception {


		System.out.println (xml);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));


			Element root = (Element) doc.getFirstChild();
			//String fqn = root.getAttributeNS("http://nuba.morfeo-project.org/network",TCloudConstants.ATTR_NETWORK_NAME);
			String fqn = root.getAttribute("ns1:"+TCloudConstants.ATTR_NETWORK_NAME);
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

			NodeList netmaskList = doc.getElementsByTagName("ns1:"+TCloudConstants.TAG_NETWORK_NETMASK);
			NodeList baseAddressList = doc.getElementsByTagName("ns1:"+TCloudConstants.TAG_NETWORK_BASE_ADDRESS);

			NodeList ipLeaseList = doc.getElementsByTagName("LEASE");
			 if (ipLeaseList.getLength()>0)
			{
				int size = 0;
				if (netmaskList != null && netmaskList.getLength() >0)
				{
					size = getSizeNetwork ((Element) netmaskList.item(0));
				}
				allParametersString.append(getTCloud2IPElasedONENet (fqn, size, baseAddressList.item(0).getTextContent(), ipLeaseList));
			}
			 else if (baseAddressList.getLength()==0)
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
	
	private int getBitNumber (short[] ip) {

		if (ip == null || ip.length != 4)
			return 0;

		int bits=0;
		for (int i=0; i < 4; i++)
			for (int j=0; j< 15; j++)
				bits += ( ((short)Math.pow(2, j))& ip[i]) / Math.pow(2, j);

		return bits;
	}
	
	private int getSizeNetwork (Element netmask)
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
		else
			allParametersString.append(ONE_NET_SIZE).append(ASSIGNATION_SYMBOL).append(ipLeaseList .getLength()).append(LINE_SEPARATOR);
			
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
	
   
}
