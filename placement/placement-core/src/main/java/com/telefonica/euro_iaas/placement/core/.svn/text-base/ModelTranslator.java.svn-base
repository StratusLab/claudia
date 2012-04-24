package com.telefonica.euro_iaas.placement.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.RASDType;
import org.dmtf.schemas.ovf.envelope._1.VirtualHardwareSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ResourceType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_userentity.Caption;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
import org.dmtf.schemas.wbem.wscim._1.common.CimUnsignedLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.placement.model.CloudProviderList;
import com.telefonica.euro_iaas.placement.model.application.ServiceApplication;
import com.telefonica.euro_iaas.placement.model.application.VDC;
import com.telefonica.euro_iaas.placement.model.application.VEERequired;
import com.telefonica.euro_iaas.placement.model.provider.CPUConf;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.DiskConf;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf;
import com.telefonica.schemas.nuba_model.exp.ArrayCIMUserEntityType;
import com.telefonica.schemas.nuba_model.exp.CIMUserEntityType;

/**
 * @author jpuente
 *
 */
@Service
public class ModelTranslator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelTranslator.class.getName());

	/**
	 * 
	 */
	public ModelTranslator() {
		super();
	}
	
	public ArrayCIMUserEntityType getArrayUserEntity(CloudProviderList cloudProviderList){
		ArrayCIMUserEntityType array = new ArrayCIMUserEntityType();
		for (CloudProvider cp : cloudProviderList.getRelations()){
			array.getCIMUserEntityType().add(getUserEntity(cp));
		}
		
		return array;
	}
	
	public CIMUserEntityType getUserEntity(CloudProvider cloudProvider) {
		CIMUserEntityType userEntity = new CIMUserEntityType();
		
		CimString name = new CimString();
		name.setValue(cloudProvider.getName());
		
		userEntity.setElementName(name);
		userEntity.setId(cloudProvider.getName());
		
		Caption caption = new Caption();
		caption.setValue(cloudProvider.getName());
		userEntity.setCaption(caption);
		
		userEntity.setURLDRP(cloudProvider.getUri());
		
		return userEntity;
	}

	public VDC getVDC(EnvelopeType envelope) {
		VDC vdc = new VDC();
		ServiceApplication sa = new ServiceApplication();
		sa.setVeesRequired(new HashSet<VEERequired>());
		
		Set<ServiceApplication> serviceApplications = new HashSet<ServiceApplication>();
		serviceApplications.add(sa);
		vdc.setServiceApplications(serviceApplications);
		
		LOGGER.info(" envelope.getContent().getDeclaredType():" + envelope.getContent().getDeclaredType());
		addVirtualSystem(sa,  envelope.getContent().getValue());

		return vdc;
	}
	
	private void addVirtualSystem(ServiceApplication sa, ContentType contentType){
		if (contentType.getClass().equals(VirtualSystemType.class)){
			VirtualSystemType vst = (VirtualSystemType)contentType;
			VEERequired vee = new VEERequired();
			sa.getVeesRequired().add(vee);
			vee.setServiceApplication(sa);
			
			vee.setName(vst.getId());
			
			for (JAXBElement<?> element : vst.getSection()){
				LOGGER.info (" declared section type: " + element.getDeclaredType());
				if (element.getDeclaredType().equals(VirtualHardwareSectionType.class)){
					LOGGER.info("found VirtualHardwareSection");

					processVirtualHardwareSection(vee, (VirtualHardwareSectionType)element.getValue());
					//TODO the specification defines that there can be several
					//VirtualHardwareSections and the consumer of the OVF can decide
					//which one is the best for him.
					break;
				}
			}
			LOGGER.info("added VEE: " + vee);

			
		} else if (contentType.getClass().equals(VirtualSystemCollectionType.class)){
			//TODO iterate over all the VirtualSystems

			VirtualSystemCollectionType vstc = (VirtualSystemCollectionType)contentType;
			for (JAXBElement<? extends ContentType> contentJAXB : vstc.getContent()){
				ContentType ct = contentJAXB.getValue();
				addVirtualSystem(sa, ct);
			}
			
		}
	}

	private void processVirtualHardwareSection(VEERequired vee,
			VirtualHardwareSectionType hSection) {

		//Iterate over Items and add them to VEERequired 
		//TODO the description in the OVF specification is to iterate over all
		//items, and "match" all of them that are required (ovf:required=="true" or ovf:required
		//not defined. This is not what I am doing here.
		
		//list of resource types: CIM_ResourceAllocationSettingData
//	       ValueMap { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", 
//	           "11", "12", "13", "14", "15", "16", "17", "18", "19", 
//	           "20", "21", "22", "23", "24", "25", "26", "27", "28", 
//	           "29", "30", "31", "32", "33", "..", "0x8000..0xFFFF" }, 
//	        Values { "Other", "Computer System", "Processor", "Memory", 
//	           "IDE Controller", "Parallel SCSI HBA", "FC HBA", 
//	           "iSCSI HBA", "IB HCA", "Ethernet Adapter", 
//	           "Other Network Adapter", "I/O Slot", "I/O Device", 
//	           "Floppy Drive", "CD Drive", "DVD drive", "Disk Drive", 
//	           "Tape Drive", "Storage Extent", "Other Storage Device", 
//	           "Serial port", "Parallel port", "USB Controller", 
//	           "Graphics controller", "IEEE 1394 Controller", 
//	           "Partitionable Unit", "Base Partitionable Unit", "Power", 
//	           "Cooling Capacity", "Ethernet Switch Port", 
//	           "Logical Disk", "Storage Volume", "Ethernet Connection", 
//	           "DMTF reserved", "Vendor Reserved" }, 
		
		//See also:
		//http://blogs.vmware.com/vapp/2009/11/virtual-hardware-in-ovf-part-1.html
		final String RESOURCE_TYPE_PROCESSOR = "3";
		final String RESOURCE_TYPE_MEMORY = "4";
		final String RESOURCE_TYPE_DISK_DRIVE = "17";
		LOGGER.info(" items: " + hSection.getItem().size());
		for (RASDType item: hSection.getItem()){
			ResourceType resourceType = item.getResourceType();
			LOGGER.info("resourceType : " + resourceType +  "required: " + item.isRequired());
			
			//we have a problem with JAXB here... required not defined returns false, not true
			//if (item.isRequired() == true) continue;
			
			//CimString allocationUnits = item.getAllocationUnits();
			CimUnsignedLong virtualQuantity = item.getVirtualQuantity();
			int virtualQuantityValue = (virtualQuantity == null) ? 0 : virtualQuantity.getValue().intValue();
			
			if (resourceType.getValue().equals(RESOURCE_TYPE_PROCESSOR)){
				CPUConf cpuConf = new CPUConf();
				cpuConf.setQuantity(virtualQuantityValue);
				vee.setcPUConf(cpuConf);
				LOGGER.info("added cpuConf");
			} else if (resourceType.getValue().equals(RESOURCE_TYPE_MEMORY)){
				MemoryConf memoryConf = new MemoryConf();
				memoryConf.setQuantity(virtualQuantityValue);
				vee.setMemoryConf(memoryConf);
				LOGGER.info("added memoryConf");
			} else if (resourceType.getValue().equals(RESOURCE_TYPE_DISK_DRIVE)){
				DiskConf diskConf = new DiskConf();
				diskConf.setQuantity(virtualQuantityValue);
				vee.setDiskConf(diskConf);		
				LOGGER.info("added diskConf");
			}


		}
		
	}

	public CloudProviderList getCloudProviderList(
			List<CloudProvider> listAllowedProviders) {
		CloudProviderList cpl = new CloudProviderList();
		
		return cpl;
	}

	
}
