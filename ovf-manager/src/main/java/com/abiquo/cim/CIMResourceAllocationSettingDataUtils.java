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
 * Contributor(s): ______________________________________.
 *
 * Graphical User Interface of this software may be used under the terms
 * of the Common Public Attribution License Version 1.0 (the  "CPAL License",
 * available at http://cpal.abiquo.com), in which case the provisions of CPAL
 * License are applicable instead of those above. In relation of this portions
 * of the Code, a Legal Notice according to Exhibits A and B of CPAL Licence
 * should be provided in any distribution of the corresponding Code to Graphical
 * User Interface.
 */

package com.abiquo.cim;

import java.util.List;

import org.dmtf.schemas.ovf.envelope._1.RASDType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.CIMResourceAllocationSettingDataType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ConsumerVisibility;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.MappingBehavior;

import com.abiquo.cim.CIMTypesUtils.CIMResourceTypeEnum;
import com.abiquo.cim.CIMTypesUtils.ChangeableTypeEnum;
import com.abiquo.cim.CIMTypesUtils.ConsumerVisibilityEnum;
import com.abiquo.ovf.exceptions.RequiredAttributeException;

/**
 * TODO using descriptions from
 * http://www.vmware.com/support/developer/cim-sdk/smash/u3/ga/apirefdoc
 * /CIM_ResourceAllocationSettingData.html TODO documentation, first do the CIM_RASD the obtain the
 * RASDT
 */
public class CIMResourceAllocationSettingDataUtils
{

    // TODO todo optional
    public static RASDType createRASDTypeFromCIMRASD(CIMResourceAllocationSettingDataType rasd,
        Boolean required, String configuration, String bound)
    {
        RASDType rasdT;

        rasdT = createRASDTypeFromCIMRASD(rasd);

        rasdT.setRequired(required);
        rasdT.setConfiguration(configuration);
        rasdT.setBound(bound);

        return rasdT;
    }

    public static RASDType createRASDTypeFromCIMRASD(CIMResourceAllocationSettingDataType rasd)
    {
        RASDType rasdT = new RASDType();

        rasdT.setAddress(rasd.getAddress());
        rasdT.setAddressOnParent(rasd.getAddressOnParent());
        rasdT.setAllocationUnits(rasd.getAllocationUnits());
        rasdT.setAutomaticAllocation(rasd.getAutomaticAllocation());
        rasdT.setAutomaticDeallocation(rasd.getAutomaticDeallocation());
        rasdT.setCaption(rasd.getCaption());
        rasdT.setChangeableType(rasd.getChangeableType());
        rasdT.setConfigurationName(rasd.getConfigurationName());
        rasdT.setConsumerVisibility(rasd.getConsumerVisibility());
        rasdT.setDescription(rasd.getDescription());
        rasdT.setElementName(rasd.getElementName());
        rasdT.getHostResource().addAll(rasd.getHostResource());
        rasdT.getConnection().addAll(rasd.getConnection());
        rasdT.setGeneration(rasd.getGeneration());
        rasdT.setInstanceID(rasd.getInstanceID());
        rasdT.setLimit(rasd.getLimit());
        rasdT.setMappingBehavior(rasd.getMappingBehavior());
        rasdT.setOtherResourceType(rasd.getOtherResourceType());
        rasdT.setParent(rasd.getParent());
        rasdT.setPoolID(rasd.getPoolID());
        rasdT.setReservation(rasd.getReservation());
        rasdT.setResourceSubType(rasd.getResourceSubType());
        rasdT.setResourceType(rasd.getResourceType());
        rasdT.setVirtualQuantity(rasd.getVirtualQuantity());
        rasdT.setWeight(rasd.getWeight());

        return rasdT;
    }

    public static CIMResourceAllocationSettingDataType createResourceAllocationSettingData(
        String elementName, String instanceID, CIMResourceTypeEnum resourceType,
        String resourceSubType, String otherResourceType, String parent, String description,
        String caption, Long generation) throws RequiredAttributeException
    {
        CIMResourceAllocationSettingDataType rasd = new CIMResourceAllocationSettingDataType();

        if (elementName == null)
        {
            throw new RequiredAttributeException("VirtualSystemSettingData elementName");
        }
        if (instanceID == null)
        {
            throw new RequiredAttributeException("VirtualSystemSettingData instanceID");
        }

        rasd.setElementName(CIMTypesUtils.createString(elementName));
        rasd.setInstanceID(CIMTypesUtils.createString(instanceID));

        rasd.setResourceType(CIMTypesUtils.createResourceType(resourceType));
        rasd.setResourceSubType(CIMTypesUtils.createString(resourceSubType));
        rasd.setOtherResourceType(CIMTypesUtils.createString(otherResourceType));
        rasd.setParent(CIMTypesUtils.createString(parent));
        rasd.setDescription(CIMTypesUtils.createString(description));
        rasd.setCaption(CIMTypesUtils.createCaptionRASD(caption));
        rasd.setGeneration(CIMTypesUtils.createUnsignedLong(generation));

        return rasd;
    }

    public static CIMResourceAllocationSettingDataType createResourceAllocationSettingData(
        String elementName, String instanceID, CIMResourceTypeEnum resourceType)
        throws RequiredAttributeException
    {
        CIMResourceAllocationSettingDataType rasd = new CIMResourceAllocationSettingDataType();

        if (elementName == null)
        {
            throw new RequiredAttributeException("VirtualSystemSettingData elementName");
        }
        if (instanceID == null)
        {
            throw new RequiredAttributeException("VirtualSystemSettingData instanceID");
        }

        rasd.setElementName(CIMTypesUtils.createString(elementName));
        rasd.setInstanceID(CIMTypesUtils.createString(instanceID));

        rasd.setResourceType(CIMTypesUtils.createResourceType(resourceType));

        return rasd;
    }

    public static void setAddressToRASD(CIMResourceAllocationSettingDataType rasd, String address,
        String addressOnParen)
    {
        rasd.setAddress(CIMTypesUtils.createString(address));
        rasd.setAddressOnParent(CIMTypesUtils.createString(addressOnParen));
    }

    public static void setPoolPropertiesToRASD(CIMResourceAllocationSettingDataType rasd,
        String poolID, int weight, List<String> hostResource, String mappingBehavior)
    {
        // TODO assert weight is positive
        // TODO almost poolId is not null

        rasd.setPoolID(CIMTypesUtils.createString(poolID));
        rasd.setWeight(CIMTypesUtils.createUnsignedInt((long)weight));

        MappingBehavior mapi = new MappingBehavior();
        mapi.setValue(mappingBehavior);

        // TODO remove previous rasd.getHostResource(). ??
        if (hostResource != null)
        {
            for (String host : hostResource)
            {
                rasd.getHostResource().add(CIMTypesUtils.createString(host));
            }
        }
    }

    public static void setAllocationToRASD(CIMResourceAllocationSettingDataType rasd,
        Long virtualQuantity, String allocationUnits, Long reservation, Long limit,
        Boolean automaticAllocation, Boolean automaticDeallocation,
        ChangeableTypeEnum changeableType)
    {
        rasd.setVirtualQuantity(CIMTypesUtils.createUnsignedLong(virtualQuantity));
        rasd.setAllocationUnits(CIMTypesUtils.createString(allocationUnits));
        rasd.setReservation(CIMTypesUtils.createUnsignedLong(reservation));
        rasd.setLimit(CIMTypesUtils.createUnsignedLong(limit));
        rasd.setAutomaticAllocation(CIMTypesUtils.createBoolean(automaticAllocation));
        rasd.setAutomaticDeallocation(CIMTypesUtils.createBoolean(automaticDeallocation));
        rasd.setChangeableType(CIMTypesUtils.createChangeableTypeRASD(changeableType));
    }

    public static void setAllocationToRASD(CIMResourceAllocationSettingDataType rasd,
        Long virtualQuantity)
    {
        rasd.setVirtualQuantity(CIMTypesUtils.createUnsignedLong(virtualQuantity));
    }
    
    public static void addHostResourceToRASD(CIMResourceAllocationSettingDataType rasd,
            String hostResource)
        {
            rasd.getHostResource().add(CIMTypesUtils.createString(hostResource));
        }
    
    public static void setAddressToRASD(CIMResourceAllocationSettingDataType rasd,
            String address)
        {
            rasd.setAddress(CIMTypesUtils.createString(address));
        }

    public static void setConfigurationIdToRASD(RASDType rasd, String configurationName,
        ConsumerVisibilityEnum consumerVisisbility)
    {
        rasd.setConfigurationName(CIMTypesUtils.createString(configurationName));

        ConsumerVisibility visio = new ConsumerVisibility();
        visio.setValue(String.valueOf(consumerVisisbility.getNumericConsumerVisibilityType()));

        rasd.setConsumerVisibility(visio);
    }

    public static void addConnectionToRASD(CIMResourceAllocationSettingDataType rasd,
        String newConnection)
    {
        rasd.getConnection().add(CIMTypesUtils.createString(newConnection));
    }

    
}
