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

package com.abiquo.ovf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType.Property;
import org.dmtf.schemas.ovf.environment._1.EntityType;
import org.dmtf.schemas.ovf.environment._1.EnvironmentType;
import org.dmtf.schemas.ovf.environment._1.ObjectFactory;
import org.dmtf.schemas.ovf.environment._1.PlatformSectionType;
import org.dmtf.schemas.ovf.environment._1.PropertySectionType;
import org.dmtf.schemas.ovf.environment._1.SectionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;
import com.abiquo.ovf.exceptions.SectionAlreadyPresentException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.section.OVFProductUtils;
import com.abiquo.ovf.section.OVFPropertyUtils;
import com.abiquo.ovf.xml.OVFEnvironmentSerializer;
import com.telefonica.claudia.ovf.utils.EnvironmentMetaData;
import com.telefonica.claudia.ovf.utils.OVFGeneralUtils;


public class OVFEnvironmentUtils
{

	private final static Logger log = LoggerFactory.getLogger(OVFEnvironmentUtils.class);

    private final static ObjectFactory envelopeFactory = new ObjectFactory();
  
    public static EnvironmentType createEnvironment(String id) throws RequiredAttributeException
    {
        EnvironmentType enviro = new EnvironmentType();

        if (id == null)
        {
            throw new RequiredAttributeException("Id on EnvironmentType");
        }

        enviro.setId(id);

        return enviro;
    }

    public static void addEntity(EnvironmentType environment, EntityType entity)
    {
        environment.getEntity().add(entity);
    }

    /**
     * platform or product section
     */
    public static void setSectionToEnvironment(EnvironmentType environment, SectionType section) throws SectionAlreadyPresentException
    {
        // TODO check withc sections can appear once ::: getSection(environment, section.getClass());
        
        if (section instanceof PropertySectionType)
        {            
            environment.getSection().add(
                envelopeFactory.createPropertySection((PropertySectionType) section));
        }
        else if (section instanceof PlatformSectionType)
        {
            environment.getSection().add(
                envelopeFactory.createPlatformSection((PlatformSectionType) section));
        }

        // TODO assert not exist
    }
    
    public static <T extends SectionType> T getSection(EnvironmentType environment, Class<T> sectionType) throws SectionNotPresentException
    {        
        SectionType section;
        
        for (JAXBElement< ? extends SectionType> jxbsection : environment.getSection())
        {
            section = jxbsection.getValue();

            if (sectionType.isInstance(section))
            {
                return (T) section;
            }
        }

        throw new SectionNotPresentException("Section "+sectionType.getCanonicalName());
    }
    

    public static void setPropertySectionToEntity(EntityType entity,
        PropertySectionType propertySection)
    {
        // TODO assert not exist
        entity.getSection().add(envelopeFactory.createPropertySection(propertySection));
    }

    public static EntityType createEntity(String id) throws RequiredAttributeException
    {
        EntityType entity = new EntityType();

        if (id == null)
        {
            throw new RequiredAttributeException("Id for Environment.Entity");
        }

        entity.setId(id);

        return entity;
    }

    /**
     * Generate the OVF Environment for a given VirtualSystem not contained in any given
     * VirtualSystemCollection (actually, this is a wrapper for another and more complete
     * version of this method).
     * 
     * @param vs the VirtualSystem which Environment is to be generated
     * @param instanceNumber used to fill @ macros
     * @param domain used to fill @ macros
     * @param serviceId used to fill @ macros
     * @param monitoringChannel used to fill @ macros
     * @param ips used to fill @ macros
     * @param netmasks used to fill @ macros
     * @param dnsServers used to fill @ macros
     * @param gateways used to fill @ macros
     * @param entryPoints used to fill @ macros
     * @param output the OutputStream to read the serialized Environment file
     * @param processSiblings
     * @return the id used for the VirtualSystem in the Environment file
     * @throws IPNotFoundException
     * @throws DNSServerNotFoundException
     * @throws NetmaskNotFoundException
     * @throws GatewayNotFoundException
     * @throws PrecedentTierEntryPointNotFoundException
     * @throws NotEnoughIPsInPoolException
     * @throws PoolNameNotFoundException
     */
	public static String createOVFEnvironment(VirtualSystemType vs, 
			int instanceNumber,
			String domain, 
			String serviceId, 
			String monitoringChannel,
			HashMap<String,ArrayList<String>> ips,
			HashMap<String, String> netmasks,
			HashMap<String, String> dnsServers,
			HashMap<String, String> gateways, 
			HashMap<String, HashMap<String, String> > entryPoints,
			OutputStream output,
			boolean processSiblings)
		throws IPNotFoundException,
			DNSServerNotFoundException, 
			NetmaskNotFoundException,
			GatewayNotFoundException,
			PrecedentTierEntryPointNotFoundException, 
			NotEnoughIPsInPoolException, 
			PoolNameNotFoundException {
		
		return createOVFEnvironment(null, 
				vs, 
				instanceNumber,
				domain, 
				serviceId, 
				monitoringChannel,
				ips,
				netmasks,
				dnsServers,
				gateways, 
				entryPoints,
				output,
				processSiblings); 
	}
    
    /**
     * 
     * Generate the OVF Environment for a given VirtualSystem, contanied in a given
     * VirtualSystemCollection
     * 
     * @param parentVsc the VirtualSystemContainer holder for the VirtualSystem. If null, 
     * it is assumed there is no container VirtualSystemContainer, typically the case of
     * a mono-VM OVF in which the Envelope contains directly the VirtualSystem
     * @param vs the VirtualSystem which Environment is to be generated
     * @param instanceNumber used to fill @ macros
     * @param domain used to fill @ macros
     * @param serviceId used to fill @ macros
     * @param monitoringChannel used to fill @ macros
     * @param ips used to fill @ macros
     * @param netmasks used to fill @ macros
     * @param dnsServers used to fill @ macros
     * @param gateways used to fill @ macros
     * @param entryPoints used to fill @ macros
     * @param output the OutputStream to read the serialized Environment file
     * @param processSiblings
     * @return the id used for the VirtualSystem in the Environment file
     * @throws IPNotFoundException
     * @throws DNSServerNotFoundException
     * @throws NetmaskNotFoundException
     * @throws GatewayNotFoundException
     * @throws PrecedentTierEntryPointNotFoundException
     * @throws NotEnoughIPsInPoolException
     * @throws PoolNameNotFoundException
     */
	public static String createOVFEnvironment(VirtualSystemCollectionType parentVsc, 
			VirtualSystemType vs, 
			int instanceNumber,
			String domain, 
			String serviceId, 
			String monitoringChannel,
			HashMap<String,ArrayList<String>> ips,
			HashMap<String, String> netmasks,
			HashMap<String, String> dnsServers,
			HashMap<String, String> gateways, 
			HashMap<String, HashMap<String, String> > entryPoints,
			OutputStream output,
			boolean processSiblings)
		throws IPNotFoundException,
			DNSServerNotFoundException, 
			NetmaskNotFoundException,
			GatewayNotFoundException,
			PrecedentTierEntryPointNotFoundException, 
			NotEnoughIPsInPoolException, 
			PoolNameNotFoundException {
		String idEnvironment = "";
		try {
			idEnvironment = vs.getId();
			EnvironmentType env = OVFEnvironmentUtils.createEnvironment(idEnvironment);
			
			/* PropertySection for the VirtualSystem itself */
			List<ProductSectionType> listProductSection = new LinkedList<ProductSectionType>();
			listProductSection.addAll(OVFEnvelopeUtils.getProductSections(vs));
			if (parentVsc != null) {
				listProductSection.addAll(OVFEnvelopeUtils.getProductSections(parentVsc));
			}
			PropertySectionType propertySection = processListForEnvironment(
					listProductSection, instanceNumber, domain, serviceId,
					monitoringChannel, ips, netmasks, dnsServers, gateways, entryPoints);
			OVFEnvironmentUtils.setSectionToEnvironment(env, propertySection);

			if (processSiblings && parentVsc != null) {
				/* PropertySection for sibling VirtualSystem, within Entities */
				Set<ContentType> childs = OVFEnvelopeUtils.getVirtualSystemsFromCollection(parentVsc);
				// FIXME: VirtualSystemCollections children are ignored by the moment, it is not clear
				// what to do with them accordingly to DSP0243 v1.0.0 and this is a case that never
				// happens in RESERVOIR
				for (Iterator<ContentType> iterator = childs.iterator(); iterator.hasNext();) {
					ContentType vsSb = (ContentType) iterator.next();
				
					/* Only siblings have to be processed. Note that we don't pass the ips array in this
					 * case due to the @IP(net) macro is only interpretable when applied to the VS itself,
					 * but not to sibling <Entity>s */
					if (!(vsSb.getId().equals(idEnvironment))) {
						listProductSection = new LinkedList<ProductSectionType>();
						listProductSection.addAll(OVFEnvelopeUtils.getProductSections(vsSb));					
						propertySection = processListForEnvironment(
								listProductSection, instanceNumber, domain, serviceId,
								monitoringChannel, null, netmasks, dnsServers, gateways, null);
						
						EntityType ent = OVFEnvironmentUtils.createEntity(vsSb.getId());
						OVFEnvironmentUtils.setPropertySectionToEntity(ent, propertySection);
						OVFEnvironmentUtils.addEntity(env, ent);
					}
				}
			}

			OVFEnvironmentSerializer.getInstance().writeXML(env, output);

		} catch (SectionAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idEnvironment;

	}
	
	public static List<EnvironmentMetaData> createOVFEnvironments(VirtualSystemCollectionType parentVsc,
			int instanceNumber,
			String domain, 
			String serviceId, 
			String monitoringChannel,
			HashMap<String,ArrayList<String>> ips,
			HashMap<String, String> netmasks,
			HashMap<String, String> dnsServers,
			HashMap<String, String> gateways, 
			HashMap<String, HashMap<String, String> > entryPoints,
			boolean processSiblings){
		
		List<EnvironmentMetaData> listOutPutStreamEnvironment = new ArrayList<EnvironmentMetaData>();
		
		//List<OutputStream> listOutPutStreamEnvironment = new ArrayList<OutputStream>();
		
		Set<ContentType> setVS_VSC = OVFEnvelopeUtils.getVirtualSystemsFromCollection(parentVsc);
		
		
		for (Iterator iterator = setVS_VSC.iterator(); iterator.hasNext();) {
			ContentType contentType = (ContentType) iterator.next();
			if(contentType instanceof VirtualSystemCollectionType){
				//List<OutputStream> subListOutputStreamEnvironment = createOVFEnvironments((VirtualSystemCollectionType)contentType, instanceNumber, domain, serviceId, monitoringChannel, ips, netmasks, dnsServers, gateways, entryPoints, processSiblings);
				List<EnvironmentMetaData> subListOutputStreamEnvironment = createOVFEnvironments((VirtualSystemCollectionType)contentType, instanceNumber, domain, serviceId, monitoringChannel, ips, netmasks, dnsServers, gateways, entryPoints, processSiblings);
				listOutPutStreamEnvironment.addAll(subListOutputStreamEnvironment);
			}else if(contentType instanceof VirtualSystemType){
				try {
					OutputStream ou = new ByteArrayOutputStream();
					String idEnvironment = createOVFEnvironment(parentVsc, (VirtualSystemType)contentType, instanceNumber, domain, serviceId, monitoringChannel, ips, netmasks, dnsServers, gateways, entryPoints, ou, processSiblings);
					EnvironmentMetaData idEnvironmentAndOutputStreamEnvironment = new EnvironmentMetaData(idEnvironment,ou);
					listOutPutStreamEnvironment.add(idEnvironmentAndOutputStreamEnvironment);
				} catch (IPNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DNSServerNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NetmaskNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GatewayNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PrecedentTierEntryPointNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotEnoughIPsInPoolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PoolNameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		return listOutPutStreamEnvironment;
	}
	
    public static PropertySectionType processListForEnvironment(List<ProductSectionType> listProductSection, 
    		int instanceNumber, 
    		String domain, 
    		String serviceId, 
    		String monitoringChannel, 
    		HashMap<String,ArrayList<String>> ips, 
    		HashMap<String, String> netmasks, 
    		HashMap<String, String> dnsServers, 
    		HashMap<String, String> gateways,
    		HashMap<String, HashMap<String, String> > entryPoints) 
    	throws IPNotFoundException,
    		DNSServerNotFoundException,
    		NetmaskNotFoundException,
    		GatewayNotFoundException, 
    		PrecedentTierEntryPointNotFoundException, 
    		NotEnoughIPsInPoolException, 
    		PoolNameNotFoundException {

    	/* Create a tag to register IPs for alias, dual key: (network, alias) -> ip */
    	HashMap<String, HashMap<String,String>> registeredAliasIPs = new HashMap<String, HashMap<String,String>>();
    	
    	PropertySectionType propertySection = OVFPropertyUtils.createPropertySection();
    	
    	try {
    	
		for (Iterator<ProductSectionType> iteratorPs = listProductSection.iterator(); iteratorPs.hasNext();) {
			ProductSectionType prodSection = (ProductSectionType) iteratorPs.next();
			
			List<Property> propertyList = OVFProductUtils.getAllProperties(prodSection);
			for (Iterator<Property> iterator = propertyList.iterator(); iterator.hasNext();) {
				Property property = (Property) iterator.next();

				// Calculate the actual key string (concatenating ProducSection class and instance)
				String effectiveKey = property.getKey();
				if (!prodSection.getClazz().isEmpty()) {
					effectiveKey = prodSection.getClazz() + "." + effectiveKey;
				}
				if (!prodSection.getInstance().isEmpty()) {
					effectiveKey = effectiveKey + "." + prodSection.getInstance();
				}
				 
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
				} 
	        	else {
	        		effectiveValue = property.getValue();
	        	}		
				
				OVFPropertyUtils.addProperty(propertySection,effectiveKey,effectiveValue);
			}   
		}
		
		} catch (RequiredAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return propertySection;    	
    }    
    
}

