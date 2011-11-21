package com.telefonica.claudia.slm.vmiHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.deployment.NIC;
import com.telefonica.claudia.slm.deployment.Network;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VDC;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.Zone;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class TCloudSerializer {

	private static Logger logger = Logger.getLogger(TCloudSerializer.class); 
	
	private static String siteRoot;
	
	public static void setSiteRoot(String sr) {
		siteRoot=sr;
	}

	private static String getSiteRoot() {
		if (siteRoot == null) {
			siteRoot = ReservoirDirectory.ROOT_NAME_SPACE;
		}
		return siteRoot;
	}

	public static Document toXml(VEEReplica vr) {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        dbfac.setNamespaceAware(true);
        Document doc;
    	
        String organizationId = getSiteRoot().replace(".", "_");
        
        try {
			docBuilder = dbfac.newDocumentBuilder();
			
			doc = docBuilder.newDocument();
	    		
    		Element veeElement = doc.createElement("VApp");
    		doc.appendChild(veeElement);
    		
    		veeElement.setAttribute("name", vr.getFQN().toString());
    		veeElement.setAttribute("xmlns:ovf", "http://schemas.dmtf.org/ovf/envelope/1");
    		veeElement.setAttribute("xmlns:rsrvr", "http://schemas.telefonica.com/claudia/ovf");
    		veeElement.setAttribute("xmlns:vssd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_VirtualSystemSettingData");
    		veeElement.setAttribute("xmlns:rasd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData");
    		veeElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    		
    		veeElement.setAttribute("name", vr.getFQN().toString());               	
    		veeElement.setAttribute("status", vr.getVEEReplicaVmState().toString());               	
			
    		String hrefRoot = "@HOSTNAME/api/org/" + organizationId + "/vdc/" + vr.getParentVEE().getServiceApplication().getVdc().getVdcName() + "/vapp/" + vr.getParentVEE().getServiceApplication().getServiceName() + "/"  + vr.getParentVEE().getVEEName() + "/" + vr.getId();
    		veeElement.setAttribute("href", hrefRoot);
    		
    		Element monitorLink = doc.createElement("Link");
    		veeElement.appendChild(monitorLink);
    		
    		monitorLink.setAttribute("rel", "monitor:measures");
    		monitorLink.setAttribute("type", "application/vnc.telefonica.tcloud.measureDescriptorList+xml");
    		monitorLink.setAttribute("href", hrefRoot + "/monitor");
    		
    		Element templateLink = doc.createElement("Link");
    		veeElement.appendChild(templateLink);
    		
    		templateLink.setAttribute("rel", "template:take");
    		templateLink.setAttribute("type", "application/vnd.telefonica.tcloud.takeTemplate+xml");
    		templateLink.setAttribute("href", hrefRoot + "/action/templatize");
    		
    		Element upLink = doc.createElement("Link");
    		veeElement.appendChild(upLink);
    		
    		upLink.setAttribute("rel", "up");
    		upLink.setAttribute("type", "application/vnc.telefonica.tcloud.vdc+xml");
    		upLink.setAttribute("href",  "@HOSTNAME/api/org/" + organizationId + "/vdc/" + vr.getParentVEE().getServiceApplication().getVdc().getVdcName());
    		
    		for (String action: new String[] {"powerOff", "powerOn", "shutdown", "reset", "suspend"}) {  
    			
    			if (!vr.getAvailablePowerOperations().contains("power:"+action)) 
    				continue;
    			
	    		Element powerLink = doc.createElement("Link");
	    		veeElement.appendChild(powerLink);
	    		powerLink.setAttribute("rel", "power:" + action);
	    		powerLink.setAttribute("href", hrefRoot + URICreation.URI_POWER_MODIFIER.replace("{power-action}", action));
    		}
    		//	Parse the OVF and import the relevant sections 
    		Document ovfEnvelope;
    		
	    	try {
	    		ovfEnvelope = docBuilder.parse(new ByteArrayInputStream(vr.getOvfRepresentation().getBytes()));
	    		
	    		String[] sectionTypes = new String[] {"OperatingSystemSection", "VirtualHardwareSection", "NetworkSection"};
	    		
	    		for (String section: sectionTypes) {
	    			
	    			NodeList nl = ovfEnvelope.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", section);
	    			
	    			for (int i=0; i <nl.getLength(); i++) {
	    				veeElement.appendChild(doc.importNode(nl.item(i), true));
	    			}
	    		}
	    		
	    		// Populate the disk Info
	    		//--------------------------------------------------------------------------------------------------------------
	    		NodeList diskList = ovfEnvelope.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", "Disk");
	    		
	    		for (int i=0; i < diskList.getLength(); i++) {
	    			
	    			Element diskElement = (Element) diskList.item(i);
	    			String id= diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "diskId");
	    			Double capacity = Double.parseDouble(diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacity").replaceAll("[A-Za-z]", ""));
	    			String units = diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacityAllocationUnits");
	    			
	    			NodeList hostResourceList = veeElement.getElementsByTagNameNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData", "HostResource");
	    			
	    			for (int j=0; j < hostResourceList.getLength(); j++) {
	    				Element hostResource = (Element) hostResourceList.item(j);
	    				
	    				if (hostResource.getTextContent().trim().equals("ovf://disk/" + id)) {
	    					hostResource.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacity", String.valueOf(capacity * DataTypesUtils.getStorageUnitConversion(units)));
	    					hostResource.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacityAllocationUnits", DataTypesUtils.STANDARD_STORAGE_UNIT_DEFAULT);
	    				}
	    			}
	    		}
	    		
	    		// Populate the System Info
	    		//--------------------------------------------------------------------------------------------------------------
	    		NodeList sysList = veeElement.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", "System");
				
	    		for (int i=0; i < sysList.getLength(); i++) {
	    			Element system = (Element) sysList.item(i);
	    			
	    			NodeList vsysList = system.getElementsByTagNameNS(TCloudConstants.NAMESPACE_VSSD, "VirtualSystemIdentifier");
	    			
	    			if (vsysList.getLength() > 0) {
	    				Element sysElement = (Element) vsysList.item(0);
	    				
	    				sysElement.setTextContent(vr.getInfraestructureId());
	    			}
	    		}
	    		
	    		// Populate the NIC Info
	    		//--------------------------------------------------------------------------------------------------------------
	    		NodeList nicList = veeElement.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", "Item");
	    		
				for (int i =0; i < nicList.getLength(); i++) {
					Element item = (Element) nicList.item(i);
					
					NodeList resourceTypeList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "ResourceType");
					if (resourceTypeList.getLength()==0) {
						logger.warn("Network item didn't had a Resource type element.");
						continue;
					}
						
					Element resourceType = (Element) resourceTypeList.item(0);
					
					if (resourceType.getTextContent().trim().equals("10")) {
						
						NodeList connectionList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "Connection");
						if (connectionList.getLength()==0) {
							logger.error("Network item didn't had a Connection element.");
							continue;
						}
							
						Element connection = (Element) connectionList.item(0);
						
						NIC currentNic = null;
						
						for (NIC nic: vr.getNICs()) {
							if (nic.getNicConf().getNetwork().getName().trim().equals(connection.getTextContent().trim())) {
								currentNic = nic;
								break;
							}		
						}
						
						if (currentNic==null) {
							logger.error("NIC not found for network [" + connection.getTextContent().trim() + "]");
							continue;
						}
						
						Element address;
						if (currentNic.getMacAddress()!=null) {
							NodeList addresList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "Address");
							
							
							if (addresList.getLength()==0) {
								address = doc.createElementNS(TCloudConstants.NAMESPACE_RASD, "Address");
								item.appendChild(address);
							} else {
								address = (Element) addresList.item(0);
							}
							
							address.setTextContent(currentNic.getMacAddress());	
						} else {
							logger.warn("MAC address not found for NIC. Skipping.");
						}
						
						if (currentNic.getInstanceId()!= null) {
							NodeList instanceIdList = item.getElementsByTagNameNS(TCloudConstants.NAMESPACE_RASD, "InstanceID");
							
							Element instanceId;
							if (instanceIdList.getLength()==0) {
								instanceId = doc.createElementNS(TCloudConstants.NAMESPACE_RASD, "InstanceID");
								item.appendChild(instanceId);
							} else {
								instanceId = (Element) instanceIdList.item(0);
							}
							
							instanceId.setTextContent(currentNic.getInstanceId());	
						} else {
							logger.warn("Instance ID not found for NIC. Skipping.");
						}
						
						for (String ip: currentNic.getIPAddresses()) {
							address = doc.createElementNS(TCloudConstants.NAMESPACE_IEP, "IPv4Address");
							address.setTextContent(ip);
							item.appendChild(address);
						}	
						
			    		Element nicMonitorLink = doc.createElement("Link");
			    		item.appendChild(nicMonitorLink);
			    		
			    		nicMonitorLink.setAttribute("rel", "monitor");
			    		nicMonitorLink.setAttribute("type", "application/vnd.telefonica.tcloudx.monitor+xml");
			    		nicMonitorLink.setAttribute("href", hrefRoot + "/hw/networks_" + currentNic.getId() + "/monitor");
					}
				}
	    		
			} catch (SAXException e) {
				logger.error("Error parsing the OVF for replica generation: " + e.getMessage());
			} catch (IOException e) {
				logger.error("Error retrieving OVF from the data model: " + e.getMessage());
			}
			
	    	return doc;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

        return null;
	}

	public static Document toXml(VEE vee) {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        dbfac.setNamespaceAware(true);
        Document doc;
    	
        String organizationId = getSiteRoot().replace(".", "_");
        
        try {
			docBuilder = dbfac.newDocumentBuilder();
			DOMImplementation di = docBuilder.getDOMImplementation();
			doc = di.createDocument(TCloudConstants.NAMESPACE_TCLOUD, "VApp", null);
		
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns", TCloudConstants.NAMESPACE_TCLOUD);
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:tcloud", TCloudConstants.NAMESPACE_TCLOUD);
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ovf", "http://schemas.dmtf.org/ovf/envelope/1");
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:rsrvr", "http://schemas.telefonica.com/claudia/ovf");
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vssd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_VirtualSystemSettingData");
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:rasd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData");
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    		
			doc.getDocumentElement().setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:min", ""+vee.getMinReplicas());
			doc.getDocumentElement().setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:max", ""+vee.getMaxReplicas());
			doc.getDocumentElement().setAttributeNS("http://schemas.telefonica.com/claudia/ovf", "rsrvr:initial", ""+vee.getInitReplicas());	                	

			doc.getDocumentElement().setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:name", vee.getFQN().toString());
    		
    		String hrefRoot= "@HOSTNAME/api/org/" + organizationId + "/vdc/" + vee.getServiceApplication().getVdc().getVdcName() + "/vapp/" + vee.getServiceApplication().getServiceName() + "/" + vee.getVEEName();
    		doc.getDocumentElement().setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href", hrefRoot);
    		
    		Element monitorLink = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Link");
    		doc.getDocumentElement().appendChild(monitorLink);
    		
    		monitorLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:rel", "monitor:measures");
    		monitorLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
    		monitorLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href", hrefRoot + "/monitor");
    		
    		Element upLink = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Link");
    		doc.getDocumentElement().appendChild(upLink);
    		
    		upLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:rel", "up");
    		upLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:type", URICreation.VDC_MIME_TYPE);
    		upLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href",  "@HOSTNAME/api/org/" + organizationId + "/vdc/" + vee.getServiceApplication().getVdc().getVdcName());
    		
    		Element deleteLink = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Link");
    		doc.getDocumentElement().appendChild(deleteLink);
    		
    		deleteLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:rel", "remove");
    		deleteLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:type", URICreation.VDC_MIME_TYPE);
    		deleteLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href",  hrefRoot);
    		
    		Element veeChildren = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Children");
    		doc.getDocumentElement().appendChild(veeChildren);
    		
    		SortedSet<VEEReplica> orderedVEEReplicas = new TreeSet<VEEReplica>(new VEEReplicasComparator());
    		orderedVEEReplicas.addAll(vee.getVEEReplicas());
    		
    		for (String action: new String[] {"powerOff", "powerOn", "shutdown", "reset", "suspend"}) {    		
	    		Element powerLink = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Link");
	    		doc.getDocumentElement().appendChild(powerLink);
	    		powerLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:rel", "power:" + action);
	    		powerLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href", hrefRoot + "/power/action/" + action);
    		}
    		
    		for(VEEReplica veeReplica : orderedVEEReplicas) { 
    			
    			Element veeReplicaElement = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:VApp");
    			veeChildren.appendChild(veeReplicaElement);
    			
    			veeReplicaElement.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:name", veeReplica.getFQN().toString());
    			upLink.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:type", URICreation.VAPP_MIME_TYPE);
    			
    			veeReplicaElement.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD,"tcloud:href", hrefRoot +  "/" + veeReplica.getId());
    			
    			Element linkVeeReplica = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:Link");
    			veeReplicaElement.appendChild(linkVeeReplica);
    			
    			linkVeeReplica.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:rel", "monitor:measures");
    			linkVeeReplica.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
    			linkVeeReplica.setAttributeNS(TCloudConstants.NAMESPACE_TCLOUD, "tcloud:href", hrefRoot + "/" + veeReplica.getId() + "/monitor");
    		}
    		
    		//	Parse the OVF and import the relevant sections 
    		Document ovfEnvelope;
    		
	    	try {
	    		ovfEnvelope = docBuilder.parse(new ByteArrayInputStream(vee.getOvfRepresentation().getBytes()));
	    		
	    		String[] sectionTypes = new String[] {"OperatingSystemSection", "VirtualHardwareSection", "NetworkSection"};
	    		
	    		for (String section: sectionTypes) {
	    			
	    			NodeList nl = ovfEnvelope.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", section);
	    			
	    			for (int i=0; i <nl.getLength(); i++) {
	    				doc.getDocumentElement().appendChild(doc.importNode(nl.item(i), true));
	    			}
	    		}
	    		
	    		NodeList diskList = ovfEnvelope.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", "Disk");
	    		
	    		for (int i=0; i < diskList.getLength(); i++) {
	    			
	    			Element diskElement = (Element) diskList.item(i);
	    			String id= diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "diskId");
	    			String capacity = diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacity").replaceAll("[A-Za-z]", "");
	    			String units = diskElement.getAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "capacity").replaceAll("[0-9]", "");
	    			
	    			NodeList hostResourceList = doc.getDocumentElement().getElementsByTagNameNS("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData", "HostResource");
	    			
	    			for (int j=0; j < hostResourceList.getLength(); j++) {
	    				Element hostResource = (Element) hostResourceList.item(j);
	    				
	    				if (hostResource.getTextContent().equals("ovf://disk/" + id)) {
	    					hostResource.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "ovf:capacity", capacity);
	    					hostResource.setAttributeNS("http://schemas.dmtf.org/ovf/envelope/1", "ovf:capacityAllocationUnits", DataTypesUtils.storageUnitsConversion.get(units));
	    				}
	    			}
	    		}
				
			} catch (SAXException e) {
				logger.error("Error parsing the OVF for replica generation: " + e.getMessage());
			} catch (IOException e) {
				logger.error("Error retrieving OVF from the data model: " + e.getMessage());
			}
			
			DataTypesUtils.serializeXML(doc);
			
	    	return doc;

		} catch (ParserConfigurationException e) {
			
		}

        return null;
	}
	
	public static Document toXml(ServiceApplication sap) {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        dbfac.setNamespaceAware(true);
        Document doc;
    	
        String organizationId = getSiteRoot().replace(".", "_");
        
        try {
			docBuilder = dbfac.newDocumentBuilder();
			DOMImplementation di = docBuilder.getDOMImplementation();
			doc = di.createDocument(TCloudConstants.NAMESPACE_TCLOUD, "VApp", null);
	        
			Element r = doc.getDocumentElement();
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns", TCloudConstants.NAMESPACE_TCLOUD);
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:tcloud", TCloudConstants.NAMESPACE_TCLOUD);
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ovf", "http://schemas.dmtf.org/ovf/envelope/1");
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:rsrvr", "http://schemas.telefonica.com/claudia/ovf");
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vssd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_VirtualSystemSettingData");
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:rasd", "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ResourceAllocationSettingData");
			r.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			
	        String hrefRoot = "@HOSTNAME/api/org/" + organizationId + "/vdc/" + sap.getVdc().getVdcName() + "/vapp/" + sap.getServiceName();
	        r.setAttribute("name", sap.getFQN().toString());
	        r.setAttribute("href", hrefRoot);
	        
	        Element link = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	        r.appendChild(link);
	        link.setAttribute("rel", "monitor:measures");
	        link.setAttribute("type", "application/vnc.telefonica.tcloud.measureDescriptorList+xml");
	        link.setAttribute("href", hrefRoot + "/monitor");

	        Element linkAdd = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	        r.appendChild(linkAdd);
	        linkAdd.setAttribute("rel", "add");
	        linkAdd.setAttribute("type", URICreation.VAPP_MIME_TYPE);
	        linkAdd.setAttribute("href", hrefRoot + URICreation.URI_ADD_MODIFIER);
	        
	        Element linkRemove = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	        r.appendChild(linkRemove);
	        linkRemove.setAttribute("rel", "remove");
	        linkRemove.setAttribute("type", URICreation.VAPP_MIME_TYPE);
	        linkRemove.setAttribute("href", hrefRoot);
	        
	        Element linkUp = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	        r.appendChild(linkUp);
	        linkUp.setAttribute("rel", "up");
	        linkUp.setAttribute("type", URICreation.VDC_MIME_TYPE);
	        linkUp.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" + sap.getVdc().getVdcName());
	        
	        Element children = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Children");
	        r.appendChild(children);
	        
	    	for(VEE vee : sap.getVEEs()) { 
	    		
	    		Element veeElement = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Vapp");
	    		children.appendChild(veeElement);
	    		
	    		veeElement.setAttribute("name", vee.getFQN().toString());
	    		veeElement.setAttribute("href", hrefRoot + "/" + vee.getVEEName());
	    		
	    		Element monitorLink = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	    		veeElement.appendChild(monitorLink);
	    		
	    		monitorLink.setAttribute("rel", "monitor:measures");
	    		monitorLink.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
	    		monitorLink.setAttribute("href", "@HOSTNAME/api/org/" + organizationId + "/vdc/" + 
	    				sap.getVdc().getVdcName() + "/vapp/" + sap.getServiceName() + "/" + vee.getVEEName() + "/monitor");
	    		
	    		Element veeChildren = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Children");
	    		veeElement.appendChild(veeChildren);
	    		
	    		SortedSet<VEEReplica> orderedVEEReplicas = new TreeSet<VEEReplica>(new VEEReplicasComparator());
	    		orderedVEEReplicas.addAll(vee.getVEEReplicas());
	    		
	    		for(VEEReplica veeReplica : orderedVEEReplicas) { 
	    			
	    			Element veeReplicaElement = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "VApp");
	    			veeChildren.appendChild(veeReplicaElement);
	    			
	    			veeReplicaElement.setAttribute("name", veeReplica.getFQN().toString());
	    			veeReplicaElement.setAttribute("href", hrefRoot + "/" + vee.getVEEName() + "/" + veeReplica.getId());
	    			
	    			Element linkVeeReplica = doc.createElementNS(TCloudConstants.NAMESPACE_TCLOUD, "Link");
	    			veeReplicaElement.appendChild(linkVeeReplica);
	    			
	    			linkVeeReplica.setAttribute("rel", "monitor:measures");
	    			linkVeeReplica.setAttribute("type", "application/vnc.telefonica.tcloud. measureDescriptorList+xml");
	    			linkVeeReplica.setAttribute("href", hrefRoot + "/" + vee.getVEEName() + "/" + veeReplica.getId() + "/monitor");
	    		}
	    	}
	    	
    		Document ovfEnvelope;
    		
	    	try {
	    		ovfEnvelope = docBuilder.parse(new ByteArrayInputStream(sap.getOvfDescriptor().getBytes()));
	    		
	    		String[] sectionTypes = new String[] {"NetworkSection"};
	    		
	    		for (String section: sectionTypes) {
	    			
	    			NodeList nl = ovfEnvelope.getElementsByTagNameNS("http://schemas.dmtf.org/ovf/envelope/1", section);
	    			
	    			for (int i=0; i <nl.getLength(); i++) {
	    				doc.getDocumentElement().appendChild(doc.importNode(nl.item(i), true));
	    			}
	    		}
				
			} catch (SAXException e) {
				logger.error("Error parsing the OVF for replica generation: " + e.getMessage());
			} catch (IOException e) {
				logger.error("Error retrieving OVF from the data model: " + e.getMessage());
			}
	    	
	    	return doc;

		} catch (ParserConfigurationException e) { }

        return null;
	}
	
	public static Document toXml(VDC vdc) {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		
        try {
			docBuilder = dbfac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
	        Element r = doc.createElement("VDC");
	        doc.appendChild(r);

	        r.setAttribute("name", vdc.getVdcName());
	        r.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vdc.getFQN().toString()));
	        
	        Element linkUp = (Element) doc.createElement("Link");
	        linkUp.setAttribute("rel", "up");
	        linkUp.setAttribute("type", "application/vnd.telefonica.tcloud.org+xml");
	        linkUp.setAttribute("href", "@HOSTNAME" + URICreation.getURIOrg(URICreation.getOrg(vdc.getFQN().toString())));	        
	        r.appendChild(linkUp);
	        
	        Element linkAdd = (Element) doc.createElement("Link");
	        linkAdd.setAttribute("rel", "add");
	        linkAdd.setAttribute("type", "application/vnd.telefonica.tcloud.instantiateOVFParams+xml");
	        linkAdd.setAttribute("href", "@HOSTNAME" + URICreation.getURIServiceAdd(vdc.getFQN().toString()));	        
	        r.appendChild(linkAdd);
	        
	        Element linkRemove = (Element) doc.createElement("Link");
	        linkRemove.setAttribute("rel", "remove");
	        linkRemove.setAttribute("type", "application/vnd.telefonica.tcloud.vdc+xml");
	        linkRemove.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vdc.getFQN().toString()));	        
	        r.appendChild(linkRemove);
	        
	        Element linkVappList = doc.createElement("Link");
	        r.appendChild(linkVappList);
	        linkVappList.setAttribute("rel", "vapplist");
	        linkVappList.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vdc.getFQN().toString()) + "/vappList");
	        
	        Element linkMonitor = doc.createElement("Link");
	        r.appendChild(linkMonitor);
	        linkMonitor.setAttribute("rel", "monitor:measures");
	        linkMonitor.setAttribute("type", "application/vnc.telefonica.tcloud.measureDescriptorList+xml");
	        linkMonitor.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vdc.getFQN().toString()) + "/monitor");
	        
	        for (ServiceApplication srvApp: vdc.getServices()) {
	            Element link = (Element) doc.createElement("Link");
	            link.setAttribute("rel", "down");
	            link.setAttribute("type", "application/vnd.telefonica.tcloud.vapp+xml");
	            link.setAttribute("href", "@HOSTNAME" + URICreation.getURIService(srvApp.getFQN().toString()));
	            r.appendChild(link);
	        }
	        
	        if (vdc.getDescription()!=null) {
		        Element descriptionNode = (Element) doc.createElement("Description");
		        descriptionNode.appendChild(doc.createTextNode(vdc.getDescription()));
		        r.appendChild(descriptionNode);
	        }
	        
	        Element availableNetworks = (Element) doc.createElement("AvailableNetworks");
	        
	        for (Zone z: vdc.getZones()) {
	        	for (Network n: z.getNetworks()) {
	        		Element networkElmnt = (Element) doc.createElement("Network");
	        		networkElmnt.setAttribute("name", n.getName());
	        		networkElmnt.setAttribute("href", "@HOSTNAME" + URICreation.getURINet(n.getFQN().toString()));
	        		availableNetworks.appendChild(networkElmnt);
	        	}
	        }
	        
	        r.appendChild(availableNetworks);
	        
    		Element storageElement = (Element) doc.createElement("StorageCapacity");
    		r.appendChild(storageElement);
    		Element diskElement = (Element) doc.createElement("Disk");
    		storageElement.appendChild(diskElement);
    		Element unitsElement = (Element) doc.createElement("Units");
    		unitsElement.appendChild(doc.createTextNode(vdc.getDiskCapacity().getUnits()));
    		diskElement.appendChild(unitsElement);
    		Element allocatedElement = (Element) doc.createElement("Allocated");
    		allocatedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getDiskCapacity().getAllocated())));
    		diskElement.appendChild(allocatedElement);
    		Element usedElement = (Element) doc.createElement("Used");
    		usedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getDiskCapacity().getUsed())));
    		diskElement.appendChild(usedElement);
    		
    		Element computeCapacityElement = (Element) doc.createElement("ComputeCapacity");
    		r.appendChild(computeCapacityElement);
    		Element cpuElement = (Element) doc.createElement("Cpu");
    		computeCapacityElement.appendChild(cpuElement);
    		unitsElement = (Element) doc.createElement("Units");
    		unitsElement.appendChild(doc.createTextNode(vdc.getCpuCapacity().getUnits()));
    		cpuElement.appendChild(unitsElement);
    		allocatedElement = (Element) doc.createElement("Allocated");
    		allocatedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getCpuCapacity().getAllocated())));
    		cpuElement.appendChild(allocatedElement);
    		usedElement = (Element) doc.createElement("Used");
    		usedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getCpuCapacity().getUsed())));
    		cpuElement.appendChild(usedElement);
    		Element memoryElement = (Element) doc.createElement("Memory");
    		computeCapacityElement.appendChild(memoryElement);
    		unitsElement = (Element) doc.createElement("Units");
    		unitsElement.appendChild(doc.createTextNode(vdc.getMemoryCapacity().getUnits()));
    		memoryElement.appendChild(unitsElement);
    		allocatedElement = (Element) doc.createElement("Allocated");
    		allocatedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getMemoryCapacity().getAllocated())));
    		memoryElement.appendChild(allocatedElement);
    		usedElement = (Element) doc.createElement("Used");
    		usedElement.appendChild(doc.createTextNode(String.valueOf(vdc.getMemoryCapacity().getUsed())));
    		memoryElement.appendChild(usedElement);
    		
	        doc.normalizeDocument();
	        
	        return doc;
	        
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static Document getVappList(VDC vdc) {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		
        try {
			docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
	        Element r = doc.createElement("VappList");
	        doc.appendChild(r);
			
			for (ServiceApplication sap: vdc.getServices()) {
				r.appendChild(doc.importNode(TCloudSerializer.toXml(sap).getFirstChild(), true));
			}
	        
			return doc;
			
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static Document getOrganizationXML(Set<VDC> vdcList) {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        dbfac.setNamespaceAware(true);
        
        try {
			docBuilder = dbfac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
	        Element r = doc.createElement("Org");
	        doc.appendChild(r);
	        
	        r.setAttribute("name", getSiteRoot());
	        //r.setAttribute("href", "http://" + SMConfiguration.getInstance().getSMIHost() + ":" + SMConfiguration.getInstance().getSMIPort() + URICreation.getURIOrg(siteRoot));
	        r.setAttribute("href", "@HOSTNAME" + URICreation.getURIOrg(getSiteRoot()));
	        
	        for (VDC vDatacenter: vdcList) {
	            Element link = (Element) doc.createElement("Link");
	            link.setAttribute("rel", "down");
	            link.setAttribute("type", "application/vnd.telefonica.tcloud.vdc+xml");
	            link.setAttribute("href", "@HOSTNAME" + URICreation.getURIVDC(vDatacenter.getFQN().toString()));
	            r.appendChild(link);
	        }

            Element vdcAddlink = (Element) doc.createElement("Link");
            vdcAddlink.setAttribute("rel", "add");
            vdcAddlink.setAttribute("type", "application/vnd.telefonica.tcloud.vdc+xml");
            vdcAddlink.setAttribute("href", "@HOSTNAME" + URICreation.URI_VDC_ADD.replace("{org-id}", getSiteRoot().replace(".", "_")));
            r.appendChild(vdcAddlink);
	        
            Element link = (Element) doc.createElement("Link");
            link.setAttribute("rel", "tasks");
            link.setAttribute("type", "application/vnd.telefonica.tcloud.tasklist+xml");
            link.setAttribute("href", "@HOSTNAME" + URICreation.getURIOrg(getSiteRoot()) + "/task");
            r.appendChild(link);
            
            return doc;
	        
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			
			return null;
		}
	}
}

class VEEReplicasComparator implements Comparator<VEEReplica> {

	public int compare(VEEReplica replica1, VEEReplica replica2) {
		return replica2.getId() - replica1.getId();
	}
	
}
