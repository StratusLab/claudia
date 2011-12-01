/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.template;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.xml.OVFSerializer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.Envelope;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.Item;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;
import org.dmtf.schemas.ovf.envelope._1.VirtualDiskDescType;
import org.dmtf.schemas.ovf.envelope._1.VirtualHardwareSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;

import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.utils.Constants;
import com.telefonica.claudia.smi.utils.NetworkUtils;
import com.telefonica.claudia.smi.utils.OneProperties;

/**
 * Manages the OpenNebula template. Gives information about OVF and 
 * translates it into OpenNebula template 
 * 
 * @author luismarcos.ayllon
 *
 */
public class OneTemplateUtils {
	
	private static Logger log = Logger.getLogger(OneTemplateUtils.class);
	
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

	public static final String ONE_VM_NIC_COLLECTION = "NICS";
	public static final String ONE_VM_NIC = "NIC";
	public static final String ONE_VM_NIC_PARAM_IP = "ip";
	public static final String ONE_VM_NIC_PARAM_NETWORK = "network";
	
	public static final String ONE_NET_ID = "ID";
	public static final String ONE_NET_NAME = "NAME";
	public static final String ONE_NET_BRIDGE = "BRIDGE";
	public static final String ONE_NET_TYPE = "TYPE";
	public static final String ONE_NET_ADDRESS = "NETWORK_ADDRESS";
	public static final String ONE_NET_SIZE = "NETWORK_SIZE";
	
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
	
	public static final String MIGRABILITY_LOWER_CASE = "migrability";
	public static final String MIGRABILITY_UPPER_CASE = "MIGRABILITY";
	public static final String MIGRABILITY_NULL = "none";
	
	private static final HashMap<String, String> textMigrability;
	
	static {
	    // MIGRABILITY TAG 
	    textMigrability = new HashMap<String, String>();
	    textMigrability.put("cross-host", "HOST");
		textMigrability.put("cross-sitehost", "SITE");
		textMigrability.put("none", "NONE");
		//  FULL??
	}
	
	private static final String DEBUGGING_CONSOLE = "RAW = [ type =\"kvm\", data =\"<devices><serial type='pty'><source path='/dev/pts/5'/><target port='0'/></serial><console type='pty' tty='/dev/pts/5'><source path='/dev/pts/5'/><target port='0'/></console></devices>\" ]";

	/**
	 * Gives and translates information about virtual machine into OpenNebula template 
	 * 
	 * @param props
	 * 			Properties about translation
	 * @param xml
	 * 			The OVF to be translated
	 * @param veeFqn
	 * 			The FQN of the VEE
	 * @return
	 * 			A translation of the OVF into OpenNebula template
	 * @throws Exception
	 */
	public static String TCloud2ONEVM(String xml, String veeFqn) throws Exception {
		
		String hypervisorKernel = PropertyManager.getInstance().getProperty(OneProperties.KERNEL_PROPERTY);
		String hypervisorInitrd = PropertyManager.getInstance().getProperty(OneProperties.INITRD_PROPERTY);
		String customizationPort = PropertyManager.getInstance().getProperty(OneProperties.CUSTOMIZATION_PORT_PROPERTY); 
		String environmentRepositoryPath = PropertyManager.getInstance().getProperty(OneProperties.ENVIRONMENT_PROPERTY); 
		String serverHost = PropertyManager.getInstance().getProperty(OneProperties.KEY_HOST); 
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			
			if (!doc.getFirstChild().getNodeName().equals(TCloudConstants.TAG_INSTANTIATE_OVF)) {
				log.error("Element <"+TCloudConstants.TAG_INSTANTIATE_OVF+"> not found.");
				throw new Exception("Element <"+TCloudConstants.TAG_INSTANTIATE_OVF+"> not found.");
			}
			
			Element root = (Element) doc.getFirstChild();
			
			String replicaName = root.getAttribute("name");
			
			NodeList envelopeItems = doc.getElementsByTagNameNS("*", "Envelope");
			
			if (envelopeItems.getLength() != 1) {
				log.error("Envelope items not found.");
				throw new Exception("Envelope items not found.");
			}
			
			// Extract the IP from the aspects section
			Map<String, String> ipOnNetworkMap = new HashMap<String, String>();
			
			NodeList aspects = doc.getElementsByTagNameNS("*", "Aspect");
			
			for (int i=0; i < aspects.getLength(); i++) {
				Element aspect = (Element) aspects.item(i);
				
				if (aspect.getAttribute("name").equals("IP Config")) {
					
					NodeList properties = aspect.getElementsByTagNameNS("*", "Property");
					
					for (int j=0; j < properties.getLength(); j++) {
						Element property = (Element) properties.item(j);
						
						NodeList keys = property.getElementsByTagNameNS("*", "Key");
						NodeList values = property.getElementsByTagNameNS("*", "Value");
						
						if (keys.getLength() >0 && values.getLength()>0) {
							ipOnNetworkMap.put(keys.item(0).getTextContent(), values.item(0).getTextContent());
						}
					}
				}
			}
			
			// Extract the ovf sections and pass them to the OVF manager to be processed.
			Document ovfDoc = builder.newDocument();
			ovfDoc.appendChild(ovfDoc.importNode(envelopeItems.item(0), true));
			OVFSerializer ovfSerializer = OVFSerializer.getInstance();
			ovfSerializer.setValidateXML(false);
			Envelope envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(DataTypesUtils.serializeXML(ovfDoc).getBytes()));
			
			ContentType entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
			
			if (entityInstance instanceof VirtualSystemType) {
				
				VirtualSystemType vs = (VirtualSystemType) entityInstance;
				
				VirtualHardwareSectionType vh = OVFEnvelopeUtils.getSection(vs, VirtualHardwareSectionType.class);
				String virtualizationType = vh.getSystem().getVirtualSystemType().getValue();
				
				StringBuffer allParametersString  = new StringBuffer();
				
			    // Migrability ....
				allParametersString.append(ONE_VM_NAME).append(ASSIGNATION_SYMBOL).append(replicaName).append(LINE_SEPARATOR);
				String migrability = getAtributeVirtualSystem (vs, MIGRABILITY_LOWER_CASE);
				if (migrability==null || migrability.equals(""))
					migrability=MIGRABILITY_NULL;
				allParametersString.append(MIGRABILITY_UPPER_CASE).append(ASSIGNATION_SYMBOL).append(textMigrability.get(migrability)).append(LINE_SEPARATOR);
				allParametersString.append(ONE_VM_OS).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
				
				String diskRoot;
				if (virtualizationType.toLowerCase().equals("kvm")) {
					diskRoot = "h";
					allParametersString.append(ONE_VM_OS_PARAM_BOOT).append(ASSIGNATION_SYMBOL).append("hd").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				} else {
					diskRoot = "s";
					allParametersString.append(ONE_VM_OS_PARAM_INITRD).append(ASSIGNATION_SYMBOL).append(hypervisorInitrd).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
					allParametersString.append(ONE_VM_OS_PARAM_KERNEL).append(ASSIGNATION_SYMBOL).append(hypervisorKernel).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				}
				
				allParametersString.append(ONE_VM_OS_PARAM_ROOT).append(ASSIGNATION_SYMBOL).append(diskRoot + "da1").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
				
				allParametersString.append(ONE_CONTEXT).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
				allParametersString.append("CustomizationUrl").append(ASSIGNATION_SYMBOL).append("\"" + Constants.PROTOCOL + serverHost + ":" + customizationPort + "/"+ replicaName+ "\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				allParametersString.append("files").append(ASSIGNATION_SYMBOL).append("\"" + environmentRepositoryPath + "/"+ replicaName + "/ovf-env.xml" + "\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				allParametersString.append("target").append(ASSIGNATION_SYMBOL).append("\"" + diskRoot + "dc"+ "\"").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);

				if (vh.getSystem() != null && vh.getSystem().getVirtualSystemType()!= null && 
						vh.getSystem().getVirtualSystemType().getValue() != null &&
						vh.getSystem().getVirtualSystemType().getValue().equals("vjsc"))
				{
					allParametersString.append("HYPERVISOR").append(ASSIGNATION_SYMBOL).append("VJSC").append(LINE_SEPARATOR);
				}	


				char sdaId = 'a';
				List<Item> items = vh.getItems();
		        for (Iterator<Item> iteratorRASD = items.iterator(); iteratorRASD.hasNext();) {
	                Item item = (Item) iteratorRASD.next();

					/* Get the resource type and process it accordingly */
	                int rsType = new Integer(item.getResourceType().getValue().trim());

	                double quantity = 1;
	                if (item.getVirtualQuantity() != null && item.getAllocationUnits() != null) {
	                        quantity = (item.getVirtualQuantity().getValue().intValue()*DataTypesUtils.getStorageUnitConversion(item.getAllocationUnits().getValue().trim()));
	                }

					switch (rsType) {
						case OVFEnvelopeUtils.ResourceTypeCPU:
	
							for (int k = 0; k < quantity; k++) {
								allParametersString.append(ONE_VM_CPU).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
								allParametersString.append(ONE_VM_VCPU).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
							}
	
							break;
							
						case OVFEnvelopeUtils.ResourceTypeDISK:
							
							/*
							 * The rasd:HostResource will follow the pattern
							 * 'ovf://disk/<id>' where id is the ovf:diskId of some
							 * <Disk>
							 */
							String hostRes = item.getHostResources().get(0).getValue();
							StringTokenizer st = new StringTokenizer(hostRes, "/");
	
							/*
							 * Only ovf:/<file|disk>/<n> format is valid, accodring
							 * OVF spec
							 */
							if (st.countTokens() != 3) {
								throw new IllegalArgumentException("malformed HostResource value (" + hostRes + ")");
							}
							if (!(st.nextToken().equals("ovf:"))) {
								throw new IllegalArgumentException("HostResource must start with ovf: (" + hostRes + ")");
							}
							String hostResType = st.nextToken();
							if (!(hostResType.equals("disk") || hostResType.equals("file"))) {
								throw new IllegalArgumentException("HostResource type must be either disk or file: (" + hostRes + ")");
							}
							String hostResId = st.nextToken();
	
							String fileRef = null;
							String capacity = null;
							if (hostResType.equals("disk")) {
								/* This type involves an indirection level */
								DiskSectionType ds = null;
								ds = OVFEnvelopeUtils.getSection(envelope, DiskSectionType.class);
								List<VirtualDiskDescType> disks = ds.getDisks();
								
								for (Iterator<VirtualDiskDescType> iteratorDk = disks.iterator(); iteratorDk.hasNext();) {
									VirtualDiskDescType disk = iteratorDk.next();
	
									String diskId = disk.getDiskId();
									if (diskId.equals(hostResId)) {
										
										fileRef = disk.getFileRef();
										capacity = disk.getCapacity();
										
										break;
									}
								}
							} else {
								throw new IllegalArgumentException("File type not supported in Disk sections.");
							}
	
							/* Throw exceptions in the case of missing information */
							if (fileRef == null) {
								throw new IllegalArgumentException("file reference can not be found for disk: " + hostRes);
							}

							
							URL url = null;
							String digest = null;
	
							ReferencesType ref = envelope.getReferences();
							List<FileType> files = ref.getFiles();
	
							for (Iterator<FileType> iteratorFl = files.iterator(); iteratorFl.hasNext();) {
								FileType fl = iteratorFl.next();
								if (fl.getId().equals(fileRef)) {
									try {
										url = new URL(fl.getHref());
									} catch (MalformedURLException e) {
										throw new IllegalArgumentException("problems parsing disk href: " + e.getMessage());
									}
	
									/*
									 * If capacity was not set using ovf:capacity in
									 * <Disk>, try to get it know frm <File>
									 * ovf:size
									 */
									if (capacity == null && fl.getSize() != null) {
										capacity = fl.getSize().toString();
									}
									
									/* Try to get the digest */
						            Map<QName, String> attributesFile = fl.getOtherAttributes();
									QName digestAtt = new QName("http://schemas.telefonica.com/claudia/ovf","digest");
									digest = attributesFile.get(digestAtt);
				
									break;
								}
							}
	
							/* Throw exceptions in the case of missing information */
							if (capacity == null) {
								throw new IllegalArgumentException("capacity can not be set for disk " + hostRes);
							}
							if (url == null) {
								throw new IllegalArgumentException("url can not be set for disk " + hostRes);
							}
							
							if (digest == null) {
								log.debug("md5sum digest was not found for disk " + hostRes);
							}
							
							String urlDisk = url.toString();
							
							if (urlDisk.contains("file:/"))
								urlDisk = urlDisk.replace("file:/", "file:///");
							
							
							File filesystem = new File("/dev/" + diskRoot + "d" + sdaId);
							
							allParametersString.append(ONE_VM_DISK).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
							allParametersString.append(ONE_VM_DISK_PARAM_IMAGE).append(ASSIGNATION_SYMBOL).append(urlDisk).append(MULT_CONF_SEPARATOR);
							
							if (virtualizationType.toLowerCase().equals("kvm")) {
								allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(diskRoot + "d" + sdaId).append(MULT_CONF_SEPARATOR);
							} else
								allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(filesystem.getAbsolutePath()).append(MULT_CONF_SEPARATOR);
							allParametersString.append(ONE_VM_DISK_PARAM_SIZE).append(ASSIGNATION_SYMBOL).append(capacity).append(MULT_CONF_SEPARATOR);
							allParametersString.append(ONE_VM_DISK_PARAM_DIGEST).append(ASSIGNATION_SYMBOL).append(digest);
							allParametersString.append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
							
							sdaId++;
							break;
							
						case OVFEnvelopeUtils.ResourceTypeMEMORY:
							allParametersString.append(ONE_VM_MEMORY).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
							break;
							
						case OVFEnvelopeUtils.ResourceTypeNIC:
							String fqnNet = URICreation.getService(veeFqn) + ".networks." + item.getConnections().get(0).getValue();
							allParametersString.append(ONE_VM_NIC).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
							
							allParametersString.append(ONE_VM_NIC_PARAM_NETWORK).append(ASSIGNATION_SYMBOL).append(fqnNet).append(MULT_CONF_SEPARATOR).
								append(ONE_VM_NIC_PARAM_IP).append(ASSIGNATION_SYMBOL).append(ipOnNetworkMap.get(fqnNet)).
								append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
	
							break;
						default:
							throw new IllegalArgumentException("unknown hw type: " + rsType);
					}
				}
				
				allParametersString.append(LINE_SEPARATOR).append(DEBUGGING_CONSOLE).append(LINE_SEPARATOR);
				

				log.debug("VM data sent:\n\n" + allParametersString.toString() + "\n\n");
				System.out.println("VM data sent:\n\n" + allParametersString.toString() + "\n\n");
				return allParametersString.toString();
				
				
			} else {
				throw new IllegalArgumentException("OVF malformed. No VirtualSystemType found.");
			}
			
		} catch (IOException e1) {
			log.error("OVF of the virtual machine was not well formed or it contained some errors.");
			throw new Exception("OVF of the virtual machine was not well formed or it contained some errors: " + e1.getMessage());
		} catch (ParserConfigurationException e) {
			log.error("Error configuring parser: " + e.getMessage());
			throw new Exception("Error configuring parser: " + e.getMessage());
		} catch (FactoryConfigurationError e) {
			log.error("Error retrieving parser: " + e.getMessage());
			throw new Exception("Error retrieving parser: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error configuring a XML Builder.");
			throw new Exception("Error configuring a XML Builder: " + e.getMessage());
		}
	}
	
	public static String ONEVM2TCloud(String ONETemplate) {
		// TODO: ONE Template to TCloud translation
		return "";
	}
	
	/**
	 * Gives and translates information about virtual network into OpenNebula template 
	 * 
	 * @param xml
	 * 			The OVF to be translated
	 * @return
	 * 			A translation of the OVF into OpenNebula template
	 * @throws Exception
	 */
	public static String TCloud2ONENet(String xml) throws Exception {
		
		try {
		    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			
		    Element root = (Element) doc.getFirstChild();
		    String fqn = root.getAttribute(TCloudConstants.ATTR_NETWORK_NAME);
		    
			NodeList netmaskList = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_NETMASK);
			NodeList baseAddressList = doc.getElementsByTagName(TCloudConstants.TAG_NETWORK_BASE_ADDRESS);
			
			StringBuffer allParametersString  = new StringBuffer();
			
			// If there is a net mask, calculate the size of the net counting it's bits. 
			if (netmaskList.getLength() >0) {
				Element netmask = (Element) netmaskList.item(0);
				
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
				int size = (int) Math.pow(2, 32.0-NetworkUtils.getBitNumber(result));
				
				if (size < 8)
					size = 8;
				else
					size -= 2;

				allParametersString.append(ONE_NET_SIZE).append(ASSIGNATION_SYMBOL).append(size).append(LINE_SEPARATOR);
			}
			
			if (baseAddressList.getLength()>0) {
				allParametersString.append(ONE_NET_ADDRESS).append(ASSIGNATION_SYMBOL).append(baseAddressList.item(0).getTextContent()).append(LINE_SEPARATOR);
			}
			
			// Translate the simple data to RPC format
			allParametersString.append(ONE_NET_NAME).append(ASSIGNATION_SYMBOL).append(fqn).append(LINE_SEPARATOR);
			
			// Add the net Type
			allParametersString.append(ONE_NET_TYPE).append(ASSIGNATION_SYMBOL).append("RANGED").append(LINE_SEPARATOR);
			allParametersString.append(ONE_NET_BRIDGE).append(ASSIGNATION_SYMBOL).append("br0").append(LINE_SEPARATOR);
			
			log.debug("Network data sent:\n\n" + allParametersString.toString() + "\n\n");
			
			return allParametersString.toString();
			
		} catch (IOException e1) {
			log.error("OVF of the virtual machine was not well formed or it contained some errors.");
			throw new Exception("OVF of the virtual machine was not well formed or it contained some errors: " + e1.getMessage());
		} catch (ParserConfigurationException e) {
			log.error("Error configuring parser: " + e.getMessage());
			throw new Exception("Error configuring parser: " + e.getMessage());
		} catch (FactoryConfigurationError e) {
			log.error("Error retrieving parser: " + e.getMessage());
			throw new Exception("Error retrieving parser: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error configuring a XML Builder.");
			throw new Exception("Error configuring a XML Builder: " + e.getMessage());
		}
	}
	
	public static String ONENet2TCloud(String ONETemplate) {
		return "";
	}
	
	/**
	 * 
	 * 
	 * @param vs
	 * @param attribute
	 * @return
	 * @throws NumberFormatException
	 */
	private static String getAtributeVirtualSystem(VirtualSystemType vs, String attribute) throws NumberFormatException {
		Iterator itr = vs.getOtherAttributes().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry e = (Map.Entry)itr.next();
			
			if ((e.getKey()).equals(new QName ("http://schemas.telefonica.com/claudia/ovf", attribute)))
				return (String)e.getValue();
			
			
		}
		return "";
			
	}
	
	
	/**
	 * Gets information about virtual system from OVF
	 * 
	 * @param envelope
	 * @return
	 * @throws Exception
	 */
	private static ArrayList<VirtualSystemType> getVirtualSystem (Envelope envelope) throws Exception {

		ContentType entityInstance = null;
		ArrayList<VirtualSystemType> virtualSystems = new ArrayList ();
		try {
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
		} catch (EmptyEnvelopeException e) {

			log.error(e);
		}
		
		HashMap<String,VirtualSystemType> virtualsystems =  new HashMap();
		
		if (entityInstance instanceof VirtualSystemType) {
			
			virtualSystems.add((VirtualSystemType)entityInstance);

		} else if (entityInstance instanceof VirtualSystemCollectionType) {
			VirtualSystemCollectionType virtualSystemCollectionType = (VirtualSystemCollectionType) entityInstance;

			for (VirtualSystemType vs : OVFEnvelopeUtils.getVirtualSystems(virtualSystemCollectionType)) 
			{

				
				virtualSystems.add(vs);
			}

		}//End for
		return virtualSystems;
	} 

}
