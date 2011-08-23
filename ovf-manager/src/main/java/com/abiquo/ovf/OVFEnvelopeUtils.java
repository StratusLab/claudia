/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is available at https://abicloud.svn.sourceforge.net/svnroot/abicloud
 *
 * The Initial Developer of the Original Code is Soluciones Grid, S.L. (www.abiquo.com),
 * Consell de Cent 296 principal 2º, 08007 Barcelona, Spain.
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s):
 *     Telefónica Investigación y Desarrollo S.A.U. (http://www.tid.es)
 *     Emilio Vargas 6, 28043 Madrid, Spain.
 *
 */

/**
 * This project aims to be OVF1.0.0 DSP8023(Envelope) and DSP8027(Environment) complaint @see DMTF
 * page for details {@link http://schemas.dmtf.org/ovf/}, but for now VMWare only can deploy OVF
 * packages form the 0.9, so compatibility is take into order.
 * <p>
 * Manipulate objects on the OVF-envelope name space. Hide from other classes the use of JAXB.
 * <p>
 * It intends to be an utility similar to ''open-ovf'', see the complete feature list for this
 * project on:
 * <li>{@link http://open-ovf.wiki.sourceforge.net/open-ovf+command+line+interface}</li><br>
 * also the virtualBox OVF code is checked<br>
 * <li>{@link http ://www.virtualbox.org/browser/trunk/src/VBox/Main/ApplianceImpl.cpp?rev=16306}</li>
 * <p>
 * References to check:
 * <li>{@link http://server.dzone.com/news/a-review-ovf-a-systems-managem}</li>
 * <li>{@link http ://stage.vambenepe.com/archives/382}</li>
 * <li>{@http://grantmcwilliams.com/index.php/virtualization/blog/}</li>
 * <p>
 */
package com.abiquo.ovf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.AnnotationSectionType;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.DeploymentOptionSectionType;
import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.EulaSectionType;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.InstallSectionType;
import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.NetworkSectionType;
import org.dmtf.schemas.ovf.envelope._1.ObjectFactory;
import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.RASDType;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;
import org.dmtf.schemas.ovf.envelope._1.ResourceAllocationSectionType;
import org.dmtf.schemas.ovf.envelope._1.SectionType;
import org.dmtf.schemas.ovf.envelope._1.StartupSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualDiskDescType;
import org.dmtf.schemas.ovf.envelope._1.VirtualHardwareSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abiquo.cim.CIMTypesUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.IdNotFoundException;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;
import com.abiquo.ovf.exceptions.SectionAlreadyPresentException;
import com.abiquo.ovf.exceptions.SectionException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.section.OVFDiskUtils;
import com.abiquo.ovf.section.OVFNetworkUtils;
import com.abiquo.ovf.section.OVFProductUtils;
import com.abiquo.ovf.section.OVFVirtualHadwareSectionUtils;
import com.abiquo.ovf.section.OVFVirtualSystemUtils;
import com.telefonica.claudia.ovf.AffinitySectionType;
import com.telefonica.claudia.ovf.AntiAffinityType;
import com.telefonica.claudia.ovf.AspectsSectionType;
import com.telefonica.claudia.ovf.AvailabilitySectionType;
import com.telefonica.claudia.ovf.DeploymentSectionType;
import com.telefonica.claudia.ovf.DiskMappingSectionType;
import com.telefonica.claudia.ovf.ElasticArraySectionType;
import com.telefonica.claudia.ovf.FirewallSectionType;
import com.telefonica.claudia.ovf.KPISectionType;
import com.telefonica.claudia.ovf.LoadBalancerSectionType;
import com.telefonica.claudia.ovf.PerformanceObjectiveSectionType;
import com.telefonica.claudia.ovf.utils.OVFGeneralUtils;

/**
 * This utility provide basic operation on the highest level OVF envelope
 * entities, providing methods to get/create Section on the Envelope or Content
 * (VirtualSystem and VirtualSystemCollection). <br>
 * <li>Creates Envelope, VirtualSystem an VirtualSystemCollection</li><br>
 * <li>Attach sections into Envelope, VirtualSystem or VirtualSystemCollection</li>
 * <br>
 * TODO add ''final'' modifier on all the input parameters
 */
public class OVFEnvelopeUtils {
	private final static Logger log = LoggerFactory.getLogger(OVFEnvelopeUtils.class);

	/** Generated factory to create XML OVF-elements in OVF name space . */
	private final static org.dmtf.schemas.ovf.envelope._1.ObjectFactory envelopFactory = new org.dmtf.schemas.ovf.envelope._1.ObjectFactory();

	/** Utility methods for the Reference element. */
	public final static OVFReferenceUtils fileReference = new OVFReferenceUtils();

	/** Utility methods for the Disk section element. */
	public final static OVFDiskUtils diskSection = new OVFDiskUtils();

	/** Utility methods for the Network section element. */
	public final static OVFNetworkUtils networkSection = new OVFNetworkUtils();

	/** Utility methods for the Product section element. */
	public final static OVFProductUtils productSection = new OVFProductUtils();

	/** Utility methods for the Virtual Hardware section element. */
	public final static OVFVirtualHadwareSectionUtils hardwareSection = new OVFVirtualHadwareSectionUtils();

	/*
	 * Values (from CIM_ResourceAllocationSettingData ResourceType property
	 * value map)
	 */
	public final static int ResourceTypeCPU = 3;
	public final static int ResourceTypeMEMORY = 4;
	public final static int ResourceTypeNIC = 10;
	public final static int ResourceTypeDISK = 17;
	
	/**
	 * Using the Core Metadata Section table from the OVF specification
	 * determine when a specific section is allowed on the OVF envelope element.
	 * 
	 * @param sectionType
	 *            , a section class to be checked if is complain to appear on
	 *            the envelope.
	 * @throws InvalidSectionException
	 *             , if the section type is not allowed on the envelope.
	 */
	public static <T extends SectionType> void checkEnvelopeSection(Class<T> sectionType) throws InvalidSectionException {
		if (!DiskSectionType.class.equals(sectionType) 
			& !NetworkSectionType.class.equals(sectionType) 
			& !DeploymentOptionSectionType.class.equals(sectionType)
			& !VirtualHardwareSectionType.class.equals(sectionType) 
			& !ProductSectionType.class.equals(sectionType)
			& !KPISectionType.class.equals(sectionType)
			& !DeploymentSectionType.class.equals(sectionType)
			& !AffinitySectionType.class.equals(sectionType)
			& !AspectsSectionType.class.equals(sectionType)
			& !DiskMappingSectionType.class.equals(sectionType)
			& !AvailabilitySectionType.class.equals(sectionType)
			& !PerformanceObjectiveSectionType.class.equals(sectionType)
			) 
		{		
			throw new InvalidSectionException("Envelope", sectionType);
		}
	}

	/**
	 * Using the Core Metadata Section table from the OVF specification
	 * determine when a specific section is allowed on the content element
	 * (could be VirtualSystem or VirtualSystemCollection).
	 * 
	 * @param vsystem
	 *            , a VirtualSystem or a VirtualSystemCollection class.
	 * @param sectionType
	 *            , a section class to be checked if is complain to appear on
	 *            the given Content (virtual system or collection).
	 * @throws InvalidSectionException
	 *             , if the section type is not allowed on the content element.
	 */
	public static <T extends SectionType> void checkContentSection(Class<? extends ContentType> vsystem, Class<T> sectionType) throws InvalidSectionException {
		// is an VirtualSystemCollectionType
		if (VirtualSystemCollectionType.class.equals(vsystem)) {
			if (!StartupSectionType.class.equals(sectionType) 
				& !EulaSectionType.class.equals(sectionType) 
				& !ProductSectionType.class.equals(sectionType)
				& !AnnotationSectionType.class.equals(sectionType) 
				& !ResourceAllocationSectionType.class.equals(sectionType) 
				& !VirtualHardwareSectionType.class.equals(sectionType)) {
				throw new InvalidSectionException("VirtualSystemCollection", sectionType);
			}

		} else
		// is an VirtualSystemType
		{

			if (!VirtualHardwareSectionType.class.equals(sectionType) 
				& !InstallSectionType.class.equals(sectionType)
				& !OperatingSystemSectionType.class.equals(sectionType)
				& !EulaSectionType.class.equals(sectionType) 
				& !ProductSectionType.class.equals(sectionType) 
				& !AnnotationSectionType.class.equals(sectionType)
				& !VirtualHardwareSectionType.class.equals(sectionType)
				& !ElasticArraySectionType.class.equals(sectionType)
				& !AvailabilitySectionType.class.equals(sectionType)
				& !PerformanceObjectiveSectionType.class.equals(sectionType)
				& !DeploymentSectionType.class.equals(sectionType)
				& !AntiAffinityType.class.equals(sectionType)
				& !FirewallSectionType.class.equals(sectionType)
				& !LoadBalancerSectionType.class.equals(sectionType)
			) {
				throw new InvalidSectionException("VirtualSystem", sectionType);
			}

		}

	}

	/**
	 * Instantiate the desired implementation of a section type.
	 * 
	 * @param sectionType
	 *            , the section class to be instantiated.
	 * @param msg
	 *            , a message content to be attached on the instantiated section
	 * @return the required section object.
	 */
	public static <T extends SectionType> T createSection(Class<T> sectionType, String msg) throws SectionException {
		T section;
		try {
			section = sectionType.newInstance();

			section.setInfo(CIMTypesUtils.createMsg(msg, null));
			// section.setRequired(---) TODO set Required (but it change if the
			// secction if for envelope/vs or vsc ...)
		} catch (Exception e) {
			throw new SectionException("Section " + sectionType.getName() + " can not be instantiated ");
		}

		return section;
	}

	/**
	 * Gets the specified section form the provided OVFEnvelope.
	 * 
	 * @param envelope
	 *            , the OVF envelope to be inspected
	 * @param sectionType
	 *            , the desired section to be extracted form the envelope.
	 * @return the section if present on the highest level envelope content.
	 * @throws SectionNotPresentException
	 *             , when there is not the desired section defined.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SectionType> T getSection(EnvelopeType envelope, Class<T> sectionType) throws SectionNotPresentException, InvalidSectionException {
		SectionType section;

		checkEnvelopeSection(sectionType);

		for (JAXBElement<? extends SectionType> jxbsection : envelope.getSection()) {
			section = jxbsection.getValue();

			if (sectionType.isInstance(section)) {
				return (T) section;
			}
		}

		throw new SectionNotPresentException(sectionType);
	}

	/**
	 * Gets the specified section form provided VirtualSystem.
	 * 
	 * @param vsystem
	 *            , the virtual system or virtual system collection to be
	 *            inspected
	 * @param sectionType
	 *            , the desired section to be extracted form the virtual system
	 *            or virtual system collection.
	 * @return the section if present on the virtual system or virtual system
	 *         collection.
	 * @throws SectionNotPresentException
	 *             , when there is not the desired section defined.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SectionType> T getSection(ContentType vsystem, Class<T> sectionType) throws SectionNotPresentException, InvalidSectionException {
		SectionType section;

		checkContentSection(vsystem.getClass(), sectionType);

		for (JAXBElement<? extends SectionType> jxbsection : vsystem.getSection()) {
			section = jxbsection.getValue();

			if (sectionType.isInstance(section)) {
				return (T) section;
			}
		}

		throw new SectionNotPresentException(sectionType);
	}

	/**
	 * Adds the specified section on the provided OVFEnvelope.
	 * 
	 * @param envelope
	 *            , the OVF envelope to be modified.
	 * @param sectionType
	 *            , the desired section to be added on the envelope.
	 * @throws SectionAlreadyPresentException
	 *             , when a sectionType is already defined on the envelope
	 *             level.
	 */
	public static <T extends SectionType> void addSection(EnvelopeType envelope, T section) throws SectionAlreadyPresentException, InvalidSectionException {

		try {
			getSection(envelope, section.getClass());

			throw new SectionAlreadyPresentException(section.getClass());
		} catch (SectionNotPresentException e1) {
			// so lets add it
		}
		
		//TODO ElasticArraySection???? add to VSC not to Envelope???

		try {
			if (section instanceof DiskSectionType)
				envelope.getSection().add(envelopFactory.createDiskSection((DiskSectionType) section));
			else if (section instanceof NetworkSectionType)
				envelope.getSection().add(envelopFactory.createNetworkSection((NetworkSectionType) section));
			else if (section instanceof DeploymentOptionSectionType)
				envelope.getSection().add(envelopFactory.createDeploymentOptionSection((DeploymentOptionSectionType) section));
			else if (section instanceof AffinitySectionType)
				envelope.getSection().add(envelopFactory.createAffinitySection((AffinitySectionType) section));
			else if (section instanceof AspectsSectionType)
				envelope.getSection().add(envelopFactory.createAspectsSection((AspectsSectionType) section));
			else if (section instanceof DeploymentSectionType)
				envelope.getSection().add(envelopFactory.createDeploymentSection((DeploymentSectionType) section));
			else if (section instanceof DiskMappingSectionType)
				envelope.getSection().add(envelopFactory.createDiskMappingSection((DiskMappingSectionType) section));
			else if (section instanceof KPISectionType)
				envelope.getSection().add(envelopFactory.createKPISection((KPISectionType) section));
			else if (section instanceof AvailabilitySectionType)
				envelope.getSection().add(envelopFactory.createAvailabilitySection((AvailabilitySectionType) section));
			else if (section instanceof PerformanceObjectiveSectionType)
				envelope.getSection().add(envelopFactory.createPerformanceObjectiveSection((PerformanceObjectiveSectionType) section));
			else
				envelope.getSection().add(envelopFactory.createSection(section));
		} catch (Exception e) // InstantiationException or
								// IllegalAccessException
		{
			log.error("The class " + section.getClass().getCanonicalName() + " can not be instantiated");
		}
	}

	/**
	 * Add the specified section form the provided virtual system.
	 * 
	 * @param vsystem
	 *            , the virtual system or virtual system collection to be
	 *            modified
	 * @param sectionType
	 *            , the desired section to be added on the virtual system or
	 *            virtual system collection.
	 * @throws SectionAlreadyPresentException
	 *             , when a sectionType is already defined on the virtual system
	 *             or collection (note: only VirtualHardware, Product and Eula
	 *             sections can appear more than once).
	 */
	public static void addSection(ContentType vsystem, SectionType section) throws SectionAlreadyPresentException, InvalidSectionException {

		try {
			getSection(vsystem, section.getClass());

			// note: only VirtualHardware, Product and Eula sections can appear
			// more than once
			if (!FirewallSectionType.class.equals(section.getClass()) & !LoadBalancerSectionType.class.equals(section.getClass()) & !ProductSectionType.class.equals(section.getClass()) & !EulaSectionType.class.equals(section.getClass()) & !VirtualHardwareSectionType.class.equals(section.getClass())

			) {
				throw new SectionAlreadyPresentException(section.getClass());
			}
		} catch (SectionNotPresentException e1) {
			// so lets create it
		}

		try {
			if (section instanceof OperatingSystemSectionType)
				vsystem.getSection().add(envelopFactory.createOperatingSystemSection((OperatingSystemSectionType) section));
			else if (section instanceof ProductSectionType)
				vsystem.getSection().add(envelopFactory.createProductSection((ProductSectionType) section));
			else if (section instanceof VirtualHardwareSectionType)
				vsystem.getSection().add(envelopFactory.createVirtualHardwareSection((VirtualHardwareSectionType) section));
			else if (section instanceof ElasticArraySectionType)
				vsystem.getSection().add(envelopFactory.createElasticArraySection((ElasticArraySectionType) section));
			else if (section instanceof FirewallSectionType)
				vsystem.getSection().add(envelopFactory.createFirewallSection((FirewallSectionType) section));
			else if (section instanceof LoadBalancerSectionType)
				vsystem.getSection().add(envelopFactory.createLoadBalancerSection((LoadBalancerSectionType) section));
			else
				vsystem.getSection().add(envelopFactory.createSection(section));
		} catch (Exception e) // InstantiationException or
								// IllegalAccessException
		{
			log.error("The class " + section.getClass().getCanonicalName() + " can not be instantiated");
		}
	}

	/*******************************************************************************
	 * VITTUAL SYSTEM / COLLECTION CONTENT
	 *******************************************************************************/

	/**
	 * Creates an empty virtual system with tcloud params.
	 * 
	 * @param vsId
	 *            , the required virtual system identifier.
	 * @param name
	 *            , the optional name for the virtual system.
	 * @param info
	 *            , the optional attached information.
	 * @param min
	 *            , to specify the minimum number of instances in the array.
	 * @param max
	 *            , to specify the maximum number of instances in the array.
	 * @param initial
	 *            , to specify the initial number of instances in the array.
	 * @return an empty (any section on it) virtual system with the provided Id,
	 *         name and information message.
	 */
	public static VirtualSystemType createVirtualSystem(String vsId, String name, String info, Integer min, Integer max, Integer initial) throws RequiredAttributeException {
		if (vsId == null) {
			throw new RequiredAttributeException("VirtualSystem attribute ID");
		}

		VirtualSystemType vs = new VirtualSystemType();
		vs.setId(vsId);

		vs.setInfo(CIMTypesUtils.createMsg(info, null));
		vs.setName(CIMTypesUtils.createMsg(name, null));

		try {

			QName minAtt = new QName("http://schemas.telefonica.com/claudia/ovf", "min");
			OVFVirtualSystemUtils.addOtherAttributes(vs, minAtt, Integer.toString(min.intValue()));

		} catch (IdAlreadyExistsException e) {
			log.error("Attribute min already defined");
		}
		catch (RequiredAttributeException e) {
			log.error(e.getLocalizedMessage());
		}
		try {

			QName maxAtt = new QName("http://schemas.telefonica.com/claudia/ovf", "max");
			OVFVirtualSystemUtils.addOtherAttributes(vs, maxAtt, Integer.toString(max.intValue()));

		} catch (IdAlreadyExistsException e) {
			log.error("Attribute max already defined");
		}
		catch (RequiredAttributeException e) {
			log.error(e.getLocalizedMessage());
		}
		try {

			QName initialAtt = new QName("http://schemas.telefonica.com/claudia/ovf", "initial");
			OVFVirtualSystemUtils.addOtherAttributes(vs, initialAtt, Integer.toString(initial.intValue()));

		} catch (IdAlreadyExistsException e) {
			log.error("Attribute initial already defined");
		}
		catch (RequiredAttributeException e) {
			log.error(e.getLocalizedMessage());
		}

		return vs;
	}

	/**
	 * Creates an empty virtual system collection.
	 * 
	 * @param vsId
	 *            , the required virtual system identifier.
	 * @param name
	 *            , the optional name for the virtual system collection.
	 * @param info
	 *            , the optional attached information
	 * @return an empty (any section on it) virtual system with the provided Id,
	 *         name and information message.
	 */
	public static VirtualSystemCollectionType createVirtualSystemCollection(String vsId, String name, String info) throws RequiredAttributeException {
		if (vsId == null) {
			throw new RequiredAttributeException("VirutalSystem attribute ID");
		}

		VirtualSystemCollectionType vsc = new VirtualSystemCollectionType();
		vsc.setId(vsId);

		vsc.setInfo(CIMTypesUtils.createMsg(info, null));
		vsc.setName(CIMTypesUtils.createMsg(name, null));

		return vsc;
	}

	/**
	 * Check if the given envelope contains more than one virtual system. (if
	 * there is a virtual system collection)
	 * 
	 * @return true if there is only one virtual system on the provided OVF
	 *         envelope content.
	 * @throws EmptyEnvelopeException
	 *             it the envelope do not contain any virtual system or virtual
	 *             system collection
	 */
	public static boolean isOneVirtualSystem(EnvelopeType envelope) throws EmptyEnvelopeException {
		boolean isVirtualSystem;

		ContentType entity = envelope.getContent().getValue();

		if (entity == null) {
			throw new EmptyEnvelopeException();
		}

		if (entity instanceof VirtualSystemType) {
			isVirtualSystem = true;
		} else if (entity instanceof VirtualSystemCollectionType) {
			isVirtualSystem = false;
		} else {
			throw new EmptyEnvelopeException("Invalid envelope, it do not contains virtualsytem or virtualsystemcollections its a " + entity.getClass().getCanonicalName());
		}

		return isVirtualSystem;
	}

	/**
	 * Gets the virtual system collection presents on the OVF envelope.
	 * 
	 * @return the highest level virtual system collection or virtual system
	 *         collection
	 * @throws EmptyEnvelopeException
	 *             if the provided OVF envelope do not contain any content
	 */
	public static ContentType getTopLevelVirtualSystemContent(EnvelopeType envelope) throws EmptyEnvelopeException {
		ContentType vs;
		JAXBElement<? extends ContentType> content = envelope.getContent();

		if (content != null) {
			vs = content.getValue();

			if (vs == null) {
				throw new EmptyEnvelopeException();
			}
		} else {
			throw new EmptyEnvelopeException();
		}

		return vs;
	}

	/**
	 * Extract all the virtual systems (and virtual system collections) form a
	 * collection.
	 * 
	 * @return all the virtual system and collections contained on the provided
	 *         virtual system collection.
	 */
	public static Set<ContentType> getVirtualSystemsFromCollection(VirtualSystemCollectionType vsCollection) {
		Set<ContentType> systems = new HashSet<ContentType>();

		List<JAXBElement<? extends ContentType>> vsCollectContent = vsCollection.getContent();

		for (JAXBElement<? extends ContentType> elem : vsCollectContent) {
			ContentType entity = elem.getValue();

			systems.add(entity);
		}

		return systems;
	}

	/**
	 * Extract all the virtual systems (and virtual system collections) form a
	 * collection.
	 * 
	 * @return all the virtual system and collections contained on the provided
	 *         virtual system collection.
	 */
	public static List<VirtualSystemType> getVirtualSystems(VirtualSystemCollectionType vsCollection) {
		List<VirtualSystemType> systems = new LinkedList<VirtualSystemType>();

		List<JAXBElement<? extends ContentType>> vsCollectContent = vsCollection.getContent();

		for (JAXBElement<? extends ContentType> elem : vsCollectContent) {
			ContentType entity = elem.getValue();

			if (entity instanceof VirtualSystemType) {
				systems.add((VirtualSystemType) entity);
			} else // collection
			{
				systems.addAll(getVirtualSystems((VirtualSystemCollectionType) entity));
			}

		}

		return systems;
	}

	/**
	 * Adds a new virtual system on the virtual system collection.
	 * 
	 * @param collection
	 *            , the virtual system collection to be changed.
	 * @param system
	 *            , the virtual system or virtual system collection to add.
	 */
	public static <T extends ContentType> void addVirtualSystem(VirtualSystemCollectionType collection, T system) throws IdAlreadyExistsException {
		log.debug("Adding virutal system id:" + system.getId() + " to collection " + collection.getId());

		checkVirtualSystemId(collection, system.getId());

		if (system instanceof VirtualSystemType) {
			collection.getContent().add(envelopFactory.createVirtualSystem((VirtualSystemType) system));
		} else if (system instanceof VirtualSystemCollectionType) {
			collection.getContent().add(envelopFactory.createVirtualSystem((VirtualSystemType) system));
		} else {
			log.error("The provided content type is not a virtual system or vs collection, its a {}", system.getClass().getCanonicalName());
		}
	}

	/**
	 * Check if there is some other Content (virtual system or system
	 * collection) with the same if on the given virtual system collection.
	 * 
	 * @param vscollection
	 *            , a virtual system collection to be check.
	 * @param vsId
	 *            , the identifier to be assert it is unique.
	 * @throws IdAlreadyExists
	 *             it there is some other virtual system with the same id.
	 */
	public static void checkVirtualSystemId(VirtualSystemCollectionType vscollection, final String vsId) throws IdAlreadyExistsException {
		ContentType content;

		for (JAXBElement<? extends ContentType> jxbcontent : vscollection.getContent()) {
			content = jxbcontent.getValue();

			if (vsId.equals(content.getId())) {
				throw new IdAlreadyExistsException("Virtual system id " + vsId + " on collectino " + vscollection.getId());
			}
		}
	}

	/***
	 * TODO doc : call with vs and vsc
	 * 
	 * @throws IdAlreadyExistsException
	 * */
	public static <T extends ContentType> void addVirtualSystem(EnvelopeType envelope, T contentType) throws IdAlreadyExistsException {
		if (VirtualSystemType.class.isInstance(contentType)) {
			addVirtualSystem(envelope, (VirtualSystemType) contentType);
		} else if (VirtualSystemCollectionType.class.isInstance(contentType)) {
			addVirtualSystemCollection(envelope, (VirtualSystemCollectionType) contentType);
		} else {
			// TODO throw invalid content exception
		}
	}

	/**
	 * Adds a new virtual system on the OVF envelope. If there is a virtual
	 * system collection is added there (highest level collection), if there is
	 * a previous virtual system a new virtual system collection is created to
	 * wrap both, if there is any virtual system/collection simply added.
	 * 
	 * @param envelope
	 *            , the OVFenvelope to be changed.
	 * @param system
	 *            , the virtual system to add.
	 * @throws IdAlreadyExists
	 */
	public static void addVirtualSystem(EnvelopeType envelope, VirtualSystemType system) throws IdAlreadyExistsException {
		log.debug("Adding virutal system id:" + system.getId());

		JAXBElement<VirtualSystemType> elementSystem = envelopFactory.createVirtualSystem(system);

		if (envelope.getContent() == null) // the envelope do not contain any
											// previous VirtualSystem
		{
			envelope.setContent(elementSystem);
		} else
		// envelope already contains some virtual systems (or collection)
		{
			ContentType entity = envelope.getContent().getValue();

			if (entity instanceof VirtualSystemType) {
				final String msg = "Adding virutal system on an envelope witch already habe a virtual system," + " a new VirtualSystemCollection will be created to wrap both";
				log.warn(msg);

				VirtualSystemType entityPrevRoot = (VirtualSystemType) entity;
				VirtualSystemCollectionType collection = new VirtualSystemCollectionType();

				// TODO require some Section to be moved ??? (think on
				// product/eula ....)
				collection.setInfo(CIMTypesUtils.createMsg(msg, null));
				collection.setId("wrap_" + entityPrevRoot.getId() + "_" + system.getId());

				collection.getContent().add(envelopFactory.createVirtualSystem(entityPrevRoot));// previous
				collection.getContent().add(envelopFactory.createVirtualSystem(system));// actual

				envelope.setContent(envelopFactory.createVirtualSystemCollection(collection));
			} else if (entity instanceof VirtualSystemCollectionType) {
				final String msg = "There is a virtual system collection already";
				log.debug(msg);

				checkVirtualSystemId((VirtualSystemCollectionType) entity, system.getId());

				VirtualSystemCollectionType collection = (VirtualSystemCollectionType) entity;
				collection.getContent().add(elementSystem);
			} else {
				log.error("Invalid envelope, it do not contains virtualsytem or virtualsystemcollections its a " + entity.getClass().getCanonicalName());
			}
		}// some previous content
	}

	/**
	 * Check if the provided virtual system collection is or contains the given
	 * collection Id.
	 * 
	 * @param collection
	 *            , the virtual system collection to be check and inspected its
	 *            elements.
	 * @param collecitonId
	 *            , the desired collection Id.
	 * @return the virtual system collection (provided or nested on) with Id
	 *         equals to provided.
	 * @throws IdNotFoundException
	 *             if the collection and any of its nested elements have the
	 *             desired Id.
	 */
	private static VirtualSystemCollectionType getVirtualSystemCollection(VirtualSystemCollectionType collection, final String collectionId) throws IdNotFoundException {
		for (JAXBElement<? extends ContentType> system : collection.getContent()) {
			ContentType entitySystem = system.getValue();

			if (entitySystem instanceof VirtualSystemCollectionType) {
				try {
					return getVirtualSystemCollection((VirtualSystemCollectionType) entitySystem, collectionId);
				} catch (IdNotFoundException e) {
					// check if there is more virtual systems collections nested
					// the given
					// collection.
				}
			}
		}// subcollections

		throw new IdNotFoundException("Virtual system collection " + collectionId + " not found");
	}

	/**
	 * Adds a new virtual system collection on the OVF envelope. If there is a
	 * virtual system collection is added there (highest level collection), if
	 * there is a previous virtual system this is added on the collection, if
	 * there is any virtual system/collection simply added.
	 * 
	 * @param envelope
	 *            , the OVFenvelope to be changed.
	 * @param collection
	 *            , the virtual system collection to add. TODO throw except if
	 *            id already on the present collection
	 * @throws IdAlreadyExists
	 */
	public static void addVirtualSystemCollection(EnvelopeType envelope, VirtualSystemCollectionType collection) throws IdAlreadyExistsException {
		log.debug("Adding virutal system collection id:" + collection.getId());

		JAXBElement<VirtualSystemCollectionType> elementCollection = envelopFactory.createVirtualSystemCollection(collection);

		if (envelope.getContent() == null) // the envelope do not contain any
											// previous VirtualSystem
		{
			envelope.setContent(elementCollection);
		} else
		// envelope already contains some virtual systems (or collection)
		{
			ContentType entity = envelope.getContent().getValue();

			if (entity instanceof VirtualSystemType) {
				final String msg = "Adding virutal system collection on an envelope witch already habe a virtual system," + " previous virtual system added to collection";
				// TODO perhaps better to have collection{vs, vsc}
				log.warn(msg);

				// TODO performance: use prev pointer
				collection.getContent().add(envelopFactory.createVirtualSystem((VirtualSystemType) entity));

				envelope.setContent(envelopFactory.createVirtualSystemCollection(collection));
				// TODO perfomance : can i use elementCollection ?
			} else if (entity instanceof VirtualSystemCollectionType) {
				final String msg = "There is a virtual system collection already";
				log.debug(msg);

				VirtualSystemCollectionType prevCollection = (VirtualSystemCollectionType) entity;

				checkVirtualSystemId(prevCollection, elementCollection.getValue().getId());

				prevCollection.getContent().add(elementCollection);
			} else {
				log.error("Invalid envelope, it do not contains virtualsytem or virtualsystemcollections its a " + entity.getClass().getCanonicalName());

				// TODO throw or remove
			}
		}// some previous content
	}

	/**
	 * Adds a virtual system collection on the given virtual system collection.
	 * 
	 * @param envelope
	 *            , the OVFenvelope to be changed.
	 * @param collection
	 *            , the virtual system collection to add.
	 * @param collectionID
	 *            , the virtual system collection identifier on the envelope.
	 * @throws IdNotFoundException
	 *             if the provided envelope do not contain a virtual system
	 *             collection with provided ID.
	 */
	public static void addVirtualSystemCollection(EnvelopeType envelope, VirtualSystemCollectionType collection, String collectionId) throws IdNotFoundException {

		log.debug("Adding virutal system collection id:" + collection.getId() + " to collection: " + collectionId);

		JAXBElement<VirtualSystemCollectionType> elementCollection = envelopFactory.createVirtualSystemCollection(collection);

		if (envelope.getContent() == null) // the envelope do not contain any
											// previous VirtualSystem
		{
			final String msg = "The provided envelope do not conatain any virutal system or collection";
			throw new IdNotFoundException(msg);
		} else
		// envelope already contains some virtual systems (or collection)
		{
			ContentType entity = envelope.getContent().getValue();

			if (entity instanceof VirtualSystemType) {
				final String msg = "The provided envelope only contains a virutal system";
				throw new IdNotFoundException(msg);
			} else if (entity instanceof VirtualSystemCollectionType) {
				VirtualSystemCollectionType targetCollection = getVirtualSystemCollection((VirtualSystemCollectionType) entity, collectionId);

				targetCollection.getContent().add(elementCollection);
				// TODO check id not exist
			} else {
				log.error("Invalid envelope, it do not contains virtualsytem or virtualsystemcollections its a " + entity.getClass().getCanonicalName());

				// TODO throw or remove
			}
		}// some previous content
	}

	/**
	 * Gets all the virtual disk descriptions on the disk section for the
	 * provided OVF envelope.
	 * 
	 * @param envelope
	 *            , the OVFenvelope to be inspected.
	 * @return all the defined disk descriptions on the OVF envelope.
	 */
	public static Set<VirtualDiskDescType> getAllVirtualDiskDescription(EnvelopeType envelope) throws SectionNotPresentException {
		DiskSectionType sectionDisk = null;
		Set<VirtualDiskDescType> disks = new HashSet<VirtualDiskDescType>();

		try {
			sectionDisk = getSection(envelope, DiskSectionType.class);
		} catch (InvalidSectionException e) {
			// network is allowed on the envelope
		}

		disks.addAll(sectionDisk.getDisk());

		return disks;
	}
		
	/**
	 * Extract *all* the product sections for a collection.
	 * 
	 * @return all the product sections on the provided virtual system collection.
	 */
	/*
	 * FIXME: Accordingly to the OVF Environment document production rules in DSP0243 1.0.0 lines 1322-1325, 
	 * all the VirtualSystems in a given VirtualSystemCollection get the Properties defined in 
	 * ProductSections placed at VirtualSystemCollection level: "The PropertySection contains the 
	 * key/value pairs defined for all the properties specified in the OVF descriptor for the 
	 * current virtual machine, as well as properties specified for the *immediate parent* 
	 * VirtualSystemCollection, if one exists". Currently, we are considering *all* the containment
	 * tree, which although is ok for RESERVOIR (we only have one VirtualSystemCollection conatiement
	 * layer) is not following the standard in strict sense.
	 */
	public static List<ProductSectionType> getAllProductSections(VirtualSystemCollectionType vsCollection) {
		List<ProductSectionType> ps = new LinkedList<ProductSectionType>();
		
		// Add product sections at VirtualSystemCollection level
		ps.addAll(getProductSections(vsCollection));

		// Explore contained VirtualSystems and VirtualSystemCollections
		List<JAXBElement<? extends ContentType>> vsCollectContent = vsCollection.getContent();
		for (JAXBElement<? extends ContentType> elemContent : vsCollectContent) {
			ContentType entity = elemContent.getValue();
			
			if (entity instanceof VirtualSystemType) { 
				// Add product sections at VirtualSystemType level
				ps.addAll(getProductSections(entity));
			}
			else if (entity instanceof VirtualSystemCollectionType) {
				// Continue adding child elements
				ps.addAll(getAllProductSections((VirtualSystemCollectionType) entity));
			}

		}

		return ps;
	}

	/**
	 * Extract the product sections for a VirtualSystem or VirtualSystemCollection. In
	 * the latter case it refers to *local* ProductSections, i.e. without going into
	 * containing VirtualSystems or VirtualSystemCollections (for that, use getAllProductSections
	 * instead)
	 * 
	 * @return all the product sections on the provided virtual system or virtual system collection.
	 */
	public static List<ProductSectionType> getProductSections(ContentType entity) {
		List<ProductSectionType> ps = new LinkedList<ProductSectionType>();
		
		List<JAXBElement<? extends SectionType>> sections = entity.getSection();
		for (JAXBElement<? extends SectionType> elemSection : sections) {
			SectionType section = elemSection.getValue();
			System.out.println (section);
			if (section instanceof ProductSectionType) {
				System.out.println (section);
				ps.add((ProductSectionType) section);
			}
		}
		
		return ps;
	}

	/**
	 * Get the parent VirtualSystemCollection that contains a given VirtualSystem and a top
	 * VirtualSystemCollection
	 * 
	 * @return all the product sections on the provided virtual system collection.
	 * 
	 * FIXME: add exceptions properly when the vs is not in the vsc or the
	 * (replace the 'return null') 
	 */ 
	public static VirtualSystemCollectionType getContainingVirtualSystemCollection(VirtualSystemCollectionType vsc, VirtualSystemType vs) {
		
		/* FIXME: Maybe not too efficient, but I don't see any other way... Using
		 * the envelop as part of the invocation parameters is a bit unsmart, I would
		 * like to get rid of it (Fermin) */
		
		List<JAXBElement<? extends ContentType>> vsCollectContent = vsc.getContent();
		
		for (JAXBElement<? extends ContentType> elemContent : vsCollectContent) {
			ContentType entity = elemContent.getValue();
			
			if (entity instanceof VirtualSystemType) {
				if (vs.getId().equals(entity.getId())) {
					return vsc;
				}
			}
			else if (entity instanceof VirtualSystemCollectionType) {
				/* Search recursively inside the VirtualSystemCollection */
				return getContainingVirtualSystemCollection(vsc, vs);
			}

		}	
		
		/* If this point is reached, then the VirtualSystem has no parent */
		return null;
	}
	
	/**
	 * Get a given content (VirtualSystem or VirtualSystemCollection) by String
	 * @param vsc
	 * @param id
	 * @return
	 * FIXME: null equals to not found. Return exception instead?
	 */
	public static ContentType getContentTypeByString(VirtualSystemCollectionType vsc, String id) {
				
		List<JAXBElement<? extends ContentType>> vsCollectContent = vsc.getContent();
		
		for (JAXBElement<? extends ContentType> elemContent : vsCollectContent) {
			
			ContentType entity = elemContent.getValue();
			
			if (entity.getId().equals(id)) {
				return entity;
			}
			
			else if (entity instanceof VirtualSystemCollectionType) {
				/* Search recursively inside the VirtualSystemCollection */
				return getContentTypeByString(vsc, id);
			}

		}	
		
		/* If this point is reached, then the VirtualSystem has not been found */
		return null;
	}
	
	/**
	 * Analyzes the ProductSection for the given vs and returns a HashMap with the
	 * numer of required IPs for each network, based on the analysis of @IP(net) and
	 * @IP(net,alias) macros
	 * 
	 * @return
	 */
	public static HashMap<String,Integer> getRequiredIPByNetwork(EnvelopeType envel, VirtualSystemType vs) {
	
		// FIXME: put the MACROS in OVFEnvironemenUtils in a place that they can be reach
		// by both classes, so we don't need to do the following line explictly
		String IP_MACRO = "IP";		
		
		HashMap<String, ArrayList<String>> aliasMap = new HashMap<String,ArrayList<String>>();
				
		try {
			/* Get the ProductSections for that VM */
			VirtualSystemCollectionType topVsc;
			topVsc = (VirtualSystemCollectionType) OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envel);
			VirtualSystemCollectionType parentVsc = OVFEnvelopeUtils.getContainingVirtualSystemCollection(topVsc, vs);
			List<ProductSectionType> listProductSection = new LinkedList<ProductSectionType>();
			listProductSection.addAll(OVFEnvelopeUtils.getProductSections(vs));
			listProductSection.addAll(OVFEnvelopeUtils.getProductSections(parentVsc));		

			/* Search in the ProductSections */
			for (Iterator<ProductSectionType> iteratorPs = listProductSection.iterator(); iteratorPs.hasNext();) {
				ProductSectionType prodSection = (ProductSectionType) iteratorPs.next();
				List<Property> propertyList = OVFProductUtils.getAllProperties(prodSection);
				for (Iterator<Property> iterator = propertyList.iterator(); iterator.hasNext();) {
					Property property = (Property) iterator.next();
	
					Map<QName, String> attributes = property.getOtherAttributes();
					QName cloudConfigurable = new QName("http://schemas.telefonica.com/claudia/ovf","cloudConfigurable");
					if ( Boolean.valueOf(attributes.get(cloudConfigurable)))
					{
						String value = property.getValue();
						if (value.contains("@"+IP_MACRO))
						{
							String prefix = "@" + IP_MACRO + "(";	
							String macroValue = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")"));
					
							/* Two possibilities to take into account: @IP(net) or @IP(net,alias). Note that
							 * alias = "" in the case @IP(net) is used */
							String network;
							String alias = "";
							StringTokenizer macroValueTokenizer = new StringTokenizer(macroValue,",");
							network = macroValueTokenizer.nextToken();
							if (macroValueTokenizer.hasMoreElements()) {
								alias = macroValueTokenizer.nextToken();
							}
							
							/* In the network already exist in the aliasMap? */
							if (aliasMap.get(network)!= null) {
								/* Is the alias in the list associated to the network? */
								if (!aliasMap.get(network).contains(alias)) {
									/* If not, add it to the list */
									ArrayList<String> l = aliasMap.get(network);
									l.add(alias);
									aliasMap.put(network, l);
								}
							}
							else {
								/* If not, create the network and add the first alias */
								ArrayList<String> l = new ArrayList<String>();
								l.add(alias);
								aliasMap.put(network,l);
							}
						}
					}
				}
			}
		} catch (EmptyEnvelopeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Analize the number of alias in each network */
		HashMap<String,Integer> ips = new HashMap<String,Integer>();

		for ( Iterator<String> i = aliasMap.keySet().iterator(); i.hasNext() ; ) {
			String net = i.next();
			ips.put(net, new Integer(aliasMap.get(net).size()));
		}
		
		return ips;
	}

	/**
	 * Performs a macro replacement in the ProductSections of the Envelope passed as argument 
	 * in the context of the VirtualSystem passed as argument (i.e. the VirtualSystem itself
	 * and possibly the containing VirtualSystemCollection). The other ProductSections are not
	 * touched. Note that if the Envelope contains just one VirtualSystem all the ProductSections
	 * are processed 
	 * 
	 * @param env
	 * @param vs
	 * @param instanceNumber
	 * @param domain
	 * @param serviceId
	 * @param monitoringChannel
	 * @param ips
	 * @param netmasks
	 * @param dnsServers
	 * @param gateways
	 * @param entryPoints
	 * @return
	 * @throws EmptyEnvelopeException 
	 */
	public static void inEnvolopeMacroReplacement(EnvelopeType env,
			VirtualSystemType vs,
			int instanceNumber, 
			String domain, 
			String serviceId, 
			String monitoringChannel, 
			HashMap<String,ArrayList<String>> ips, 
			HashMap<String, String> netmasks, 
			HashMap<String, String> dnsServers, 
			HashMap<String, String> gateways,
			HashMap<String, HashMap<String, String> > entryPoints)  
	throws 
	EmptyEnvelopeException,
	IPNotFoundException,
	DNSServerNotFoundException, 
	NetmaskNotFoundException,
	GatewayNotFoundException,
	PrecedentTierEntryPointNotFoundException, 
	NotEnoughIPsInPoolException, 
	PoolNameNotFoundException {

		VirtualSystemCollectionType parentVsc = null;
		if (!OVFEnvelopeUtils.isOneVirtualSystem(env)) {
			parentVsc = OVFEnvelopeUtils.getContainingVirtualSystemCollection((VirtualSystemCollectionType) OVFEnvelopeUtils.getTopLevelVirtualSystemContent(env), vs);
		} 
		
		/* Accumulating ProducSections in the scope of the VirtualSystem*/
		List<ProductSectionType> listProductSection = new LinkedList<ProductSectionType>();
		listProductSection.addAll(OVFEnvelopeUtils.getProductSections(vs));
		if (parentVsc != null) {
			listProductSection.addAll(OVFEnvelopeUtils.getProductSections(parentVsc));
		}
		
    	/* Create a tag to register IPs for alias, dual key: (network, alias) -> ip */
    	HashMap<String, HashMap<String,String>> registeredAliasIPs = new HashMap<String, HashMap<String,String>>();
    	
		for (Iterator<ProductSectionType> iteratorPs = listProductSection.iterator(); iteratorPs.hasNext();) {
			ProductSectionType prodSection = (ProductSectionType) iteratorPs.next();
			
			/* Processing properties one by one */
			List<Property> propertyList = OVFProductUtils.getAllProperties(prodSection);
			for (Iterator<Property> iterator = propertyList.iterator(); iterator.hasNext();) {
				Property property = (Property) iterator.next();
				 
				Map<QName, String> attributes = property.getOtherAttributes();
	        	QName cloudConfigurable = new QName("http://schemas.telefonica.com/claudia/ovf","cloudConfigurable");
	        	
	        	String effectiveValue;
	        	if ( Boolean.valueOf(attributes.get(cloudConfigurable)))
				{
					effectiveValue = OVFGeneralUtils.macroReplacement(property.getValue(), 
							instanceNumber, 
							domain, 
							serviceId, 
							monitoringChannel, 
							ips, 
							netmasks, 
							dnsServers, 
							gateways, 
							entryPoints, 
							registeredAliasIPs);
					
					property.setValue(effectiveValue);
				}
			}   
		}
		
	}

	/**
     * Split the given Service OVF file into as many files as VirtualSystems are found in it.
     *
     * @param env
     *         A service OVF file. It has to contain a VirtualSystemCollection item.
     *
     * @return
     *         A list of OVF Envelopes, each one representing the information of a Virtual System.
     * @throws Exception
     */
    public static HashMap<String, EnvelopeType> splitOvf(EnvelopeType env) throws Exception {
       
        HashMap<String, EnvelopeType> results  = new HashMap<String, EnvelopeType>();
       
        ContentType entityInstance = null;
        try {
            entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(env);
        } catch (EmptyEnvelopeException e) {

            log.error("Empty envelope found: " + e);
        }
        if (entityInstance instanceof VirtualSystemType) {
           
            // If the entity is already a VirtualSystem, return a list with the virtual system itself.
            results.put(entityInstance.getId(), env);

        } else if (entityInstance instanceof VirtualSystemCollectionType) {
            VirtualSystemCollectionType virtualSystemCollectionType = (VirtualSystemCollectionType) entityInstance;
           
            List<ProductSectionType> productSections=null;
           
            productSections = OVFEnvelopeUtils.getProductSections(virtualSystemCollectionType);
           
            for (VirtualSystemType vs : OVFEnvelopeUtils.getVirtualSystems(virtualSystemCollectionType)) {
               
                // Create the envelope
                ObjectFactory ovfFactory = new ObjectFactory();
                EnvelopeType vsEnv = ovfFactory.createEnvelopeType();
               
                // Add the VirtualSystem to the envelope
                try {
                    ReferencesType references = ovfFactory.createReferencesType();
                    vsEnv.setReferences(references);
                    DiskSectionType diskSection = ovfFactory.createDiskSectionType();
                    diskSection.setInfo(new MsgType());
                   
                    NetworkSectionType networkSection = ovfFactory.createNetworkSectionType();
                    networkSection.setInfo(new MsgType());

                    if (productSections!=null)
                        for (ProductSectionType ps: productSections) {
                            try {
                                OVFEnvelopeUtils.addSection(vs, ps);
                            } catch (Exception snpe) {
                                log.warn("Product section not found: " + snpe.getMessage());
                            }
                        }
                   
                    OVFEnvelopeUtils.addSection(vsEnv, diskSection);
                    OVFEnvelopeUtils.addSection(vsEnv, networkSection);
                   
                    // Search for the disks and add them to the DiskSection and ReferenceSection
                    // of the child VirtualSystem
                    VirtualHardwareSectionType vh = null;
                   
                    try {
                        vh = OVFEnvelopeUtils.getSection(vs, VirtualHardwareSectionType.class);
                   
                        List<RASDType> items = vh.getItem();
                        for (Iterator<RASDType> iteratorRASD = items.iterator(); iteratorRASD.hasNext();) {
                            RASDType item = (RASDType) iteratorRASD.next();

                            /* Get the resource type and process it accordingly */
                            int rsType = new Integer(item.getResourceType().getValue());
                            log.debug("hw type: " + rsType);

                            switch (rsType) {
                                case ResourceTypeDISK:
                                    String hostRes = item.getHostResource().get(0).getValue();
                                    log.debug("hostresource: " + hostRes);
                                    StringTokenizer st = new StringTokenizer(hostRes, "/");
                                   
                                    if (st.countTokens() != 3) {
                                        throw new Exception("malformed HostResource value (" + hostRes + ")");
                                    }
                                    if (!(st.nextToken().equals("ovf:"))) {
                                        throw new Exception("HostResource must start with ovf: (" + hostRes + ")");
                                    }
                                    String hostResType = st.nextToken();
                                    if (!(hostResType.equals("disk") || hostResType.equals("file"))) {
                                        throw new Exception("HostResource type must be either disk or file: (" + hostRes + ")");
                                    }
                                    String hostResId = st.nextToken();
                                   
                                    if (hostResType.equals("disk")) {
                                        /* This type involves an indirection level */
                                        DiskSectionType ds = null;
                                        String fileRef = null;
                                       
                                        try {
                                            ds = OVFEnvelopeUtils.getSection(env, DiskSectionType.class);
                                        } catch (SectionNotPresentException e) {
                                            log.error(e.getMessage());
                                        } catch (InvalidSectionException e) {
                                            log.error(e.getMessage());
                                        }
                                       
                                        // Fill the disk section
                                        List<VirtualDiskDescType> disks = ds.getDisk();
                                        for (Iterator<VirtualDiskDescType> iteratorDk = disks.iterator(); iteratorDk.hasNext();) {
                                            VirtualDiskDescType disk = iteratorDk.next();
                                           
                                            fileRef = disk.getFileRef();
                                            String diskId = disk.getDiskId();
                                            if (diskId.equals(hostResId)) {
                                                OVFDiskUtils.addDisk(diskSection, disk);
                                                break;
                                            }
                                        }
                                       
                                        // Fill the Reference section
                                        if (fileRef == null) {
                                            throw new SectionException("File reference can not be found for disk: " + hostRes);
                                        }
                                       
                                        ReferencesType ref = env.getReferences();
                                        List<FileType> files = ref.getFile();
                                       
                                        for (Iterator<FileType> iteratorFl = files.iterator(); iteratorFl.hasNext();) {
                                            FileType fl = iteratorFl.next();
                                            if (fl.getId().equals(fileRef)) {
                                                OVFReferenceUtils.addFile(references, fl);
                                            }
                                        }
                                    }

                                    break;
                                   
                                case ResourceTypeNIC:

                                    String netName = item.getConnection().get(0).getValue();

                                    NetworkSectionType ns = OVFEnvelopeUtils.getSection(env, NetworkSectionType.class);
                                    List<NetworkSectionType.Network> networks = ns.getNetwork();
                                    for (Iterator<NetworkSectionType.Network> iteratorN = networks.iterator(); iteratorN.hasNext();) {
                                        org.dmtf.schemas.ovf.envelope._1.NetworkSectionType.Network netInSection = iteratorN.next();
                                       
                                        if (netInSection.getName().equals(netName))
                                            OVFNetworkUtils.addNetwork(networkSection, netInSection);
                                    }

                                    break;
                            }
                        }

                    } catch (SectionNotPresentException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (InvalidSectionException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    OVFEnvelopeUtils.addVirtualSystem(vsEnv, vs);
                   
                } catch (IdAlreadyExistsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }   
               
                // Add the new Envelope to the results list
                results.put(vs.getId(), vsEnv);
            }
        }
       
        return results;
    }
	
}
