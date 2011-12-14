package com.telefonica.claudia.smi.provisioning;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.RASDType;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;
import org.dmtf.schemas.ovf.envelope._1.VirtualDiskDescType;
import org.dmtf.schemas.ovf.envelope._1.VirtualHardwareSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.section.OVFProductUtils;
import com.abiquo.ovf.xml.OVFSerializer;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.Main;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class OneVmUtilities {
	
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
	
	public static final String ONE_VERSION = "ONEVERSION";

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
	
	
	public static final String PROTOCOL = "http://";
	
	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("com.telefonica.claudia.smi.provisioning.OneVmUtilities");
	
	private OneOperations operations;
	
	private String oneversion = "2.2";
	
	private String networkBridge = null;
	
	private String environmentRepositoryPath = null;
	private String  oneSshKey = null;
	private String  customizationPort = null;
	private String  xendisk = null;
	private String  arch = null;
	private String  hypervisorInitrd = null;
	private String  hypervisorKernel = null;
	private String serverHost = null;
	
	//FALTA
	
	private String oneScriptPath = null;
	
	private static String eth0Dns = null;
	private static String eth0Gateway = null;
	private static String eth1Dns = null;
	private static String eth1Gateway = null;
	private static String netInitScript0 = null;
	private static String netInitScript1 = null;
	
	
	public OneVmUtilities (OneOperations operations, String oneversion, String networkBridge,
			String environmentRepositoryPath ,String oneScriptPath, String  oneSshKey ,String  customizationPort, String hypervisorInitrd, String hypervisorKernel,
			String xendisk, String arch, String serverHost, String netInitScript0, String netInitScript1
	     )
	{
		this.operations = operations;
		this.oneversion=oneversion;
		this.networkBridge=networkBridge;
		this.environmentRepositoryPath = environmentRepositoryPath;
		this.oneSshKey  =  oneSshKey ;
		this.customizationPort = customizationPort;
		this.hypervisorInitrd=hypervisorInitrd;
		this.hypervisorKernel=hypervisorKernel;
		this.xendisk = xendisk;
		this.arch = arch;
		this.serverHost= serverHost;
		this.netInitScript0= netInitScript0;
		this.netInitScript1= netInitScript1;
		this.oneScriptPath=oneScriptPath;
	}
	public String TCloud2ONEVM(String xml,
			String veeFqn) throws Exception {

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
			EnvelopeType envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(DataTypesUtils.serializeXML(ovfDoc).getBytes()));

			ContentType entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);

			if (entityInstance instanceof VirtualSystemType) {

				VirtualSystemType vs = (VirtualSystemType) entityInstance;

				VirtualHardwareSectionType vh = OVFEnvelopeUtils.getSection(vs, VirtualHardwareSectionType.class);
				String virtualizationType = "kvm";
				if (vh.getSystem()!= null)
					virtualizationType = vh.getSystem().getVirtualSystemType().getValue();

				
				
				ProductSectionType productSection = null;
				String hostname = "";
				String scriptListProp = "";
				String netcontext = "";
				String contextarget = "";
				String scriptListTemplate = "";
				
				try
				{
				   productSection = OVFEnvelopeUtils.getSection(vs, ProductSectionType.class);
				   hostname = this.getPropertyValue(productSection, "HOSTNAME");
				   scriptListProp = this.getPropertyValue(productSection, "SCRIPT_LIST");
				   netcontext=getNetContext(vh, veeFqn,xml, scriptListProp);
					
					
				   
				   if (scriptListProp!= null) {
					   String[] scriptList = scriptListProp.split("/");

					for (String scrt: scriptList){

						scriptListTemplate = scriptListTemplate + " "+oneScriptPath+"/"+scrt;
					}
					   }

					contextarget = this.getPropertyValue(productSection, "TARGET_CONF");
				}
				catch (SectionNotPresentException e)
				{
				  log.info("No Product Section presented");
				}


				StringBuffer allParametersString  = new StringBuffer();

				// Migrability ....

				allParametersString.append(ONE_VM_NAME).append(ASSIGNATION_SYMBOL).append(replicaName).append(LINE_SEPARATOR);


				if (!virtualizationType.toLowerCase().equals("xenhvm"))
					allParametersString.append("GRAPHICS").append(ASSIGNATION_SYMBOL).append("[type=\"vnc\",listen=\"0.0.0.0\"]").append(LINE_SEPARATOR);


				if (virtualizationType.toLowerCase().equals("kvm")) {

					allParametersString.append("REQUIREMENTS").append(ASSIGNATION_SYMBOL).append("\"HYPERVISOR=\\\"kvm\\\"\"").append(LINE_SEPARATOR);
				} else if (virtualizationType.toLowerCase().equals("xenhvm")) {
					allParametersString.append("REQUIREMENTS").append(ASSIGNATION_SYMBOL).append("\"HYPERVISOR=\\\"xen\\\"\"").append(LINE_SEPARATOR);
				}
				else if (virtualizationType.toLowerCase().equals("xen")) {
					allParametersString.append("REQUIREMENTS").append(ASSIGNATION_SYMBOL).append("\"HYPERVISOR=\\\"xen\\\"\"").append(LINE_SEPARATOR);
				}
				allParametersString.append(ONE_VM_OS).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);


				String diskRoot;
				if (virtualizationType.toLowerCase().equals("kvm")) {
					diskRoot = "h";
					allParametersString.append(ONE_VM_OS_PARAM_BOOT).append(ASSIGNATION_SYMBOL).append("hd").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				} 
				else if (virtualizationType.toLowerCase().equals("xenhvm")) {
					diskRoot = "h";
					allParametersString.append(ONE_VM_OS_PARAM_KERNEL).append(ASSIGNATION_SYMBOL).append("/usr/lib/xen/boot/hvmloader").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
				}

				else {
					diskRoot = xendisk;
					allParametersString.append(ONE_VM_OS_PARAM_INITRD).append(ASSIGNATION_SYMBOL).append(hypervisorInitrd).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
					allParametersString.append(ONE_VM_OS_PARAM_KERNEL).append(ASSIGNATION_SYMBOL).append(hypervisorKernel).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				}
				
				

				if(arch != null && arch.length()>0)
					allParametersString.append("ARCH").append(ASSIGNATION_SYMBOL).append("\"").append(arch).append("\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);

				if (!virtualizationType.toLowerCase().equals("xenhvm"))
					allParametersString.append(ONE_VM_OS_PARAM_ROOT).append(ASSIGNATION_SYMBOL).append(diskRoot + "da1").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);


				allParametersString.append(ONE_CONTEXT).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);

				if(hostname != null && hostname.length()>0) {
					allParametersString.append("hostname  = \""+hostname+"\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				}
				if(netcontext != null && netcontext.length()>0) {
					allParametersString.append(netcontext);
				}
				if (oneSshKey!=null && oneSshKey.length()>0)
				allParametersString.append("public_key").append(ASSIGNATION_SYMBOL).append(oneSshKey).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
				allParametersString.append("CustomizationUrl").append(ASSIGNATION_SYMBOL).append("\"" + PROTOCOL + serverHost + ":" + customizationPort + "/"+ replicaName+ "\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);

			
				  allParametersString.append("files").append(ASSIGNATION_SYMBOL).append("\"" + environmentRepositoryPath + "/"+ replicaName + "/ovf-env.xml" +scriptListTemplate+ "\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);

				if (contextarget!= null && contextarget.length()>0)
					allParametersString.append("target").append(ASSIGNATION_SYMBOL).append("\"" + contextarget +"\"").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
				else
				    allParametersString.append("target").append(ASSIGNATION_SYMBOL).append("\"" + diskRoot + "dd"+ "\"").append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);

				
				
				
				if (vh.getSystem() != null && vh.getSystem().getVirtualSystemType()!= null &&
						vh.getSystem().getVirtualSystemType().getValue() != null &&
						vh.getSystem().getVirtualSystemType().getValue().equals("vjsc"))
				{
					allParametersString.append("HYPERVISOR").append(ASSIGNATION_SYMBOL).append("VJSC").append(LINE_SEPARATOR);
				}


				char sdaId = 'a';

				List<RASDType> items = vh.getItem();
				boolean ispaasaware = true;
			
				for (Iterator<RASDType> iteratorRASD = items.iterator(); iteratorRASD.hasNext();) {
					RASDType item = (RASDType) iteratorRASD.next();

					/* Get the resource type and process it accordingly */
					int rsType = new Integer(item.getResourceType().getValue());

					int quantity = 1;
					if (item.getVirtualQuantity() != null) {
						quantity = item.getVirtualQuantity().getValue().intValue();
					}

					switch (rsType) {
					case ResourceTypeCPU:

						//  for (int k = 0; k < quantity; k++) {
						allParametersString.append(ONE_VM_CPU).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
						allParametersString.append(ONE_VM_VCPU).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
						//  }

						break;

					case ResourceTypeDISK:

						/*
						 * The rasd:HostResource will follow the pattern
						 * 'ovf://disk/<id>' where id is the ovf:diskId of some
						 * <Disk>
						 */
						String hostRes = item.getHostResource().get(0).getValue();
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
						String format = null;
						String target = null;
						if (hostResType.equals("disk")) {
							/* This type involves an indirection level */
							DiskSectionType ds = null;
							ds = OVFEnvelopeUtils.getSection(envelope, DiskSectionType.class);
							List<VirtualDiskDescType> disks = ds.getDisk();

							for (Iterator<VirtualDiskDescType> iteratorDk = disks.iterator(); iteratorDk.hasNext();) {
								VirtualDiskDescType disk = iteratorDk.next();

								String diskId = disk.getDiskId();
								if (diskId.equals(hostResId)) {

									fileRef = disk.getFileRef();
									capacity = disk.getCapacity();
									format = disk.getFormat();
									Map<QName, String> attributesFile = disk.getOtherAttributes();
									QName targetAtt = new QName("http://schemas.telefonica.com/claudia/ovf","target");
									 target  = attributesFile.get(targetAtt);

									if (fileRef == null) {
										log.warn("file reference can not be found for disk: " + hostRes);
										//ispaasaware = true;
									}
									else
									{
										ispaasaware = false;
									}


									break;
								}
							}
						} else {
							throw new IllegalArgumentException("File type not supported in Disk sections.");
						}

						/* Throw exceptions in the case of missing information */
						if (fileRef == null) {
							log.warn("file reference can not be found for disk: " + hostRes);
						}


						URL url = null;
						String digest = null;
					  	String driver = null;

						ReferencesType ref = envelope.getReferences();
						List<FileType> files = ref.getFile();



						for (Iterator<FileType> iteratorFl = files.iterator(); iteratorFl.hasNext();) {
							FileType fl = iteratorFl.next();
							if (fileRef!=null)
							{

								if (fl.getId().equals(fileRef)) {
									try {
										url = new URL(fl.getHref());
									} catch (MalformedURLException e) {
										throw new IllegalArgumentException("problems parsing disk href: " + e.getMessage());
									}
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

								Map<QName, String> attributesFile2 = fl.getOtherAttributes();
								QName driverAtt = new QName("http://schemas.telefonica.com/claudia/ovf","driver");
								driver = attributesFile.get(driverAtt);


								break;
							}
						}

						/* Throw exceptions in the case of missing information */
						if (capacity == null) {
							throw new IllegalArgumentException("capacity can not be set for disk " + hostRes);
						}
						if (url == null && fileRef!=null) {
							throw new IllegalArgumentException("url can not be set for disk " + hostRes);
						}

						if (digest == null) {
							log.debug("md5sum digest was not found for disk " + hostRes);
						}



						String urlDisk = null;

						if (url != null)  
						{
							urlDisk = url.toString();

							if (urlDisk.contains("file:/"))
								urlDisk = urlDisk.replace("file:/", "file:///");
						}
						

						File filesystem = new File("/dev/" + diskRoot + "d" + sdaId);

						allParametersString.append(ONE_VM_DISK).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
						if (urlDisk!= null)
							allParametersString.append(ONE_VM_DISK_PARAM_IMAGE).append(ASSIGNATION_SYMBOL).append(urlDisk).append(MULT_CONF_SEPARATOR);

						if (target != null && target.length()>0)
						{
							allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(target).append(MULT_CONF_SEPARATOR);
						}
						else
						{
						if (virtualizationType.toLowerCase().equals("kvm")) {
							allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(diskRoot + "d" + sdaId).append(MULT_CONF_SEPARATOR);
						}
						else if (virtualizationType.toLowerCase().equals("xenhvm")){
							allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(diskRoot + "d" + sdaId).append(MULT_CONF_SEPARATOR);
							
						}
						else
							allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append(filesystem.getAbsolutePath()).append(MULT_CONF_SEPARATOR);
						}
						if (format!=null)
						{

							if (format.equals("ext3"))
							{
								allParametersString.append(ONE_VM_DISK_PARAM_TYPE).append(ASSIGNATION_SYMBOL).append("fs").append(MULT_CONF_SEPARATOR); 
							}
							allParametersString.append(ONE_VM_DISK_PARAM_FORMAT).append(ASSIGNATION_SYMBOL).append(format).append(MULT_CONF_SEPARATOR);
						}
						log.info("Driver " + driver);
						if (driver!=null)
							allParametersString.append(ONE_VM_DISK_PARAM_DRIVER).append(ASSIGNATION_SYMBOL).append(driver).append(MULT_CONF_SEPARATOR);




						if (digest!=null)
							allParametersString.append(ONE_VM_DISK_PARAM_DIGEST).append(ASSIGNATION_SYMBOL).append(digest).append(MULT_CONF_SEPARATOR);
						allParametersString.append(ONE_VM_DISK_PARAM_SIZE).append(ASSIGNATION_SYMBOL).append(capacity);
						allParametersString.append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);

						sdaId++;
						break;

					case ResourceTypeMEMORY:
						allParametersString.append(ONE_VM_MEMORY).append(ASSIGNATION_SYMBOL).append(quantity).append(LINE_SEPARATOR);
						break;

					case ResourceTypeNIC:
						String fqnNet = URICreation.getService(veeFqn) + ".networks." + item.getConnection().get(0).getValue();
						log.info ("Generating tag for fqnNet " + fqnNet);
						allParametersString.append(ONE_VM_NIC).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER).append(LINE_SEPARATOR);

						allParametersString.append(ONE_NET_BRIDGE).append(ASSIGNATION_SYMBOL).append(networkBridge).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
						if (oneversion.equals("3.0"))
						{
							
							String idnet = null;
							try {
								idnet = operations.getOneNetworkId(fqnNet);
								log.info("Network information " + fqnNet + " " + idnet);
								allParametersString.append("NETWORK_ID").append(ASSIGNATION_SYMBOL).append(idnet);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								throw new IllegalArgumentException("Unknown network id");
							}
						}
						else if (fqnNet.indexOf("public")!=-1)
						{
							allParametersString.append(ONE_VM_NIC_PARAM_NETWORK).append(ASSIGNATION_SYMBOL).append("public");
						}
						else
							allParametersString.append(ONE_VM_NIC_PARAM_NETWORK).append(ASSIGNATION_SYMBOL).append(fqnNet);
						if (ipOnNetworkMap.get(fqnNet)!=null)
							allParametersString.append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR).append(ONE_VM_NIC_PARAM_IP).append(ASSIGNATION_SYMBOL).append(ipOnNetworkMap.get(fqnNet)).append(LINE_SEPARATOR);
						allParametersString.append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);

						break;
					default:
						throw new IllegalArgumentException("unknown hw type: " + rsType);
					}
				}

				if (ispaasaware)
				{
					allParametersString.append(ONE_VM_DISK).append(ASSIGNATION_SYMBOL).append(MULT_CONF_LEFT_DELIMITER);
					allParametersString.append(ONE_VM_DISK_PARAM_IMAGE).append(ASSIGNATION_SYMBOL).append("http://appliances.stratuslab.eu/images/base/ubuntu-10.04-amd64-base/1.4/ubuntu-10.04-amd64-base-1.4.img.gz").append(MULT_CONF_SEPARATOR);

					allParametersString.append(ONE_VM_DISK_PARAM_TARGET).append(ASSIGNATION_SYMBOL).append("sdc").append(MULT_CONF_SEPARATOR);

					allParametersString.append(ONE_VM_DISK_PARAM_SIZE).append(ASSIGNATION_SYMBOL).append(512);
					allParametersString.append(MULT_CONF_RIGHT_DELIMITER).append(LINE_SEPARATOR);
				}

				//allParametersString.append(LINE_SEPARATOR).append(DEBUGGING_CONSOLE).append(LINE_SEPARATOR);

				if (virtualizationType.toLowerCase().equals("xenhvm"))
					allParametersString.append("RAW=[type=\"xen\",").append(LINE_SEPARATOR).append("data=\"builder = \\\"hvm\\\"").append(LINE_SEPARATOR)
					.append("device_model = \\\"/usr/lib64/xen/bin/qemu-dm\\\"").append(LINE_SEPARATOR).append("pae = \\\"1\\\"").append(LINE_SEPARATOR)
					.append("acpi = \\\"1\\\"").append(LINE_SEPARATOR).append("localtime = \\\"0\\\"").append(LINE_SEPARATOR).append("vnc = \\\"1\\\"\"]").append(LINE_SEPARATOR);

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
			e.printStackTrace();
			throw new Exception("Error configuring a XML Builder: " + e.getMessage());
		}
	}
	
	protected static String getNetContext(VirtualHardwareSectionType vh, String veeFqn,String xml, String scriptListProp) throws Exception {

		//	log.debug("PONG2 xml" +xml+ "\n");

		StringBuffer allParametersString  = new StringBuffer();



		List<RASDType> items = vh.getItem();
		int i=0;
		for (Iterator<RASDType> iteratorRASD = items.iterator(); iteratorRASD.hasNext();) {
			RASDType item = (RASDType) iteratorRASD.next();

			/* Get the resource type and process it accordingly */
			int rsType = new Integer(item.getResourceType().getValue());

			int quantity = 1;
			if (item.getVirtualQuantity() != null) {
				quantity = item.getVirtualQuantity().getValue().intValue();
			}

			switch (rsType) {
			case ResourceTypeNIC:

				try {


					//					log.debug("PONG eth0Dns" + eth0Dns + "\n");
					//					log.debug("PONG eth0Gateway" + eth0Gateway + "\n");
					//					log.debug("PONG eth1Dns" + eth1Dns + "\n");
					//					log.debug("PONG eth1Gateway" + eth1Gateway + "\n");


					String fqnNet = URICreation.getService(veeFqn) + ".networks." + item.getConnection().get(0).getValue();

					allParametersString.append("ip_eth"+i).append(ASSIGNATION_SYMBOL).append("\"$NIC[IP, NETWORK=\\\""+fqnNet+"\\\"]\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
					String dns="";
					String gateway="";
					
					
					if(i==0){
						dns=eth0Dns;
						gateway=eth0Gateway;
					}
					if(i==1){
						dns=eth1Dns;
						gateway=eth1Gateway;
					}
					
					
     
					if(dns!=null &&dns.length()>0)
					{
						allParametersString.append("dns_eth"+i).append(ASSIGNATION_SYMBOL).append(dns).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
					}
					if(gateway != null && gateway.length()>0)
					{
						allParametersString.append("gateway_eth"+i).append(ASSIGNATION_SYMBOL).append(gateway).append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
					}

					i++;


				} catch (FactoryConfigurationError e) {
					log.error("Error retrieving parser: " + e.getMessage());
					throw new Exception("Error retrieving parser: " + e.getMessage());
				} catch (Exception e) {
					log.error("Error configuring a XML Builder.");
					throw new Exception("Error configuring a XML Builder: " + e.getMessage());
				}

				break;
			default:
				//throw new IllegalArgumentException("unknown hw type: " + rsType);
			}

		}

		StringBuffer scriptexec=new StringBuffer();
		scriptexec.append("SCRIPT_EXEC=\"");
		
		if (getNetInitScript(scriptListProp))
		{
			if (i==1){
				if(netInitScript0!= null && netInitScript0.length()>0) {
					scriptexec.append(netInitScript0);	
				}
			}
			if (i==2){
				if(netInitScript1!= null && netInitScript1.length()>0) {
					scriptexec.append(netInitScript1);	
				}
			}
		}
		
		
		
		if (scriptListProp != null && scriptListProp.length()!=0)
		{
			String[] scriptList = scriptListProp.split("/");

			String scriptListTemplate = "";

			for (String scrt: scriptList){

				if (scrt.indexOf(".py")!=-1)
				{
					if (scrt.equals("OVFParser.py")) {
						System.out.println ("python /mnt/stratuslab/"+scrt);
						scriptexec.append("; python /mnt/stratuslab/"+scrt+"");
					}
					if (scrt.equals("restful-server.py")) {
						System.out.println ("/etc/init.d/lb_server start");
						scriptexec.append("; /etc/init.d/lb_server start");
					}
					if (scrt.equals("torqueProbe.py")) {
						System.out.println ("/etc/init.d/probe start");
						scriptexec.append("; /etc/init.d/probe start");
					}
				}
				else if (scrt.indexOf(".sh")!=-1)
				{
					
					scriptexec.append("; /mnt/stratuslab/"+scrt);

				}
			}
		}
		
		if (scriptexec.length()>0){
			scriptexec.append("\"").append(MULT_CONF_SEPARATOR).append(LINE_SEPARATOR);
		}
		else {
			scriptexec.append("");
		}
		allParametersString.append(scriptexec);
		
		return allParametersString.toString();

	}
	
	
	private String getPropertyValue (ProductSectionType productSection, String property)
	{
		String value = null;
		try
		{
			Property prop = OVFProductUtils.getProperty(productSection, property);     			
			if (prop.getValue().toString()!= null)
				value =prop.getValue().toString();

		}
		catch (Exception e) 
		{
			
		}
		return value;
	}
	
	protected static boolean getNetInitScript (String scriptListProp)
	{

		if (scriptListProp== null)
			return false;
		String[] scriptList = scriptListProp.split("/");
	
		for (String scrt: scriptList){

			if (scrt.indexOf("netinit")!=-1)
			{
				return true;
			}
		}
		return false;
	
	}
	

	
	

}
