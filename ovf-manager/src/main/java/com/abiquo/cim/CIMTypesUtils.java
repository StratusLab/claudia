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

import java.math.BigInteger;

import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.StringsType.Msg;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ChangeableType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ConsumerVisibility;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.MappingBehavior;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ResourceType;
import org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_virtualsystemsettingdata.Caption;
import org.dmtf.schemas.wbem.wscim._1.common.CimBoolean;
import org.dmtf.schemas.wbem.wscim._1.common.CimDateTime;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
import org.dmtf.schemas.wbem.wscim._1.common.CimUnsignedInt;
import org.dmtf.schemas.wbem.wscim._1.common.CimUnsignedLong;
import org.dmtf.schemas.wbem.wscim._1.common.CimUnsignedShort;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class CIMTypesUtils
{

    /*
     * public enum MappingBehaviorEnum { Unknown, Not Supported, Dedicated, Soft Affinity, Hard
     * Affinity, DMTF Reserved, Vendor Reserved }
     */
    /**
     * Action to take for the virtual system when the software executed by the virtual system fails.
     * Failures in this case means a failure that is detectable by the host platform, such as a
     * non-interuptable wait state condition.
     **/
    public enum AutomaticRecoveryActionTypeEnum
    {
        None(2), Restart(3), Revert_to_snapshot(4);

        private int araType;

        private AutomaticRecoveryActionTypeEnum(int numeric)
        {
            araType = numeric;
        }

        public int getNumericARAType()
        {
            return araType;
        }
    }

    /**
     * Action to take for the virtual system when the host is shut down.
     */
    public enum AutomaticShutdownActionTypeEnum
    {
        Turn_Off(2), Save_state(3), Shutdown(4);

        private int asaType;

        private AutomaticShutdownActionTypeEnum(int numeric)
        {
            asaType = numeric;
        }

        public int getNumericARAType()
        {
            return asaType;
        }
    }

    /**
     * Action to take for the virtual system when the host is started.
     */
    public enum AutomaticStartupActionTypeEnum
    {
        None(2), Restart_if_previously_active(3), Always_startup(4);

        private int asaType;

        private AutomaticStartupActionTypeEnum(int numeric)
        {
            asaType = numeric;
        }

        public int getNumericARAType()
        {
            return asaType;
        }
    }

    /**
     * usign
     * http://vmware.se/support/developer/cim-sdk/smash/u2/ga/apirefdoc/CIM_OperatingSystem.html
     */
    public enum OperatingSystemTypeEnum
    {
        Unknown(0), Other(1), MACOS(2), ATTUNIX(3), DGUX(4), DECNT(5), Tru64_UNIX(6), OpenVMS(7), HPUX(
            8), AIX(9), MVS(10), OS400(11), OS_2(12), JavaVM(13), MSDOS(14), WIN3x(15), WIN95(16), WIN98(
            17), WINNT(18), WINCE(19), NCR3000(20), NetWare(21), OSF(22), DC_OS(23), Reliant_UNIX(
            24), SCO_UnixWare(25), SCO_OpenServer(26), Sequent(27), IRIX(28), Solaris(29), SunOS(30), U6000(
            31), ASERIES(32), HP_NonStop_OS(33), HP_NonStop_OSS(34), BS2000(35), LINUX(36), Lynx(37), XENIX(
            38), VM(39), Interactive_UNIX(40), BSDUNIX(41), FreeBSD(42), NetBSD(43), GNU_Hurd(44), OS9(
            45), MACH_Kernel(46), Inferno(47), QNX(48), EPOC(49), IxWorks(50), VxWorks(51), MiNT(52), BeOS(
            53), HP_MPE(54), NextStep(55), PalmPilot(56), Rhapsody(57), Windows_2000(58), Dedicated(
            59), OS_390(60), VSE(61), TPF(62), Windows_R_Me(63), Caldera_Open_UNIX(64), OpenBSD(65),

        //
        Not_Applicable(66), Windows_XP(67), z_OS(68), Microsoft_Windows_Server_2003(69), Microsoft_Windows_Server_2003_64_Bit(
            70), Windows_XP_64_Bit(71), Windows_XP_Embedded(72), Windows_Vista(73), Windows_Vista_64_Bit(
            74), Windows_Embedded_for_Point_of_Service(75), Microsoft_Windows_Server_2008(76), Microsoft_Windows_Server_2008_64_Bit(
            77);

        private int osType;

        private OperatingSystemTypeEnum(int numeric)
        {
            osType = numeric;
        }

        public int getNumericOSType()
        {
            return osType;
        }

    }

    /**
     * http://vmware.se/support/developer/cim-sdk/smash/u2/ga/apirefdoc/VMware_StoragePool.html
     * http://docs.sun.com/source/820-4913-10/appendix1.html
     **/
    public enum CIMResourceTypeEnum
    {
        Other(1), Computer_System(2), Processor(3), Memory(4), IDE_Controller(5), Parallel_SCSI_HBA(
            6), FC_HBA(7), iSCSI_HBA(8), IB_HCA(9), Ethernet_Adapter(10), Other_Network_Adapter(11), IO_Slot(
            12), IO_Device(13), Floppy_Drive(14), CD_Drive(15), DVD_drive(16), Disk_Drive(17), Tape_Drive(
            18), Storage_Extent(19), Other_storage_device(20), Serial_port(21), Parallel_port(22), USB_Controller(
            23), Graphics_controller(24), IEEE_1394_Controller(25), Partitionable_Unit(26), Base_Partitionable_Unit(
            27), Power(28), Cooling_Capacity(29), Ethernet_Switch_Port(30), DMTF_reserved(31), Vendor_Reserved(
            32), INVALID_CIMResourceType(0);

        private int resourceType;

        private CIMResourceTypeEnum(int numeric)
        {
            resourceType = numeric;
        }

        public int getNumericResourceType()
        {
            return resourceType;
        }

        /**
         * Gets the CIMResourceTypeEnum representation from a int
         * 
         * @param value the value
         * @return the CIMResourceTypeEnum
         */
        public static CIMResourceTypeEnum fromValue(int value)
        {
            for (CIMResourceTypeEnum c : CIMResourceTypeEnum.values())
            {
                if (c.getNumericResourceType() == value)
                {
                    return c;
                }
            }
            throw new IllegalArgumentException(String.valueOf(value));
        }

    }

    /**
     * Specifies how this resource maps to underlying resourcesIf the HostResource array contains
     * any entries, this property reflects how the resource maps to those specific resources
     * 
     * @author Abiquo
     */
    public enum CIMMappingBehaviourEnum
    {
        Unknown(0), Not_supported(2), Dedicated(3), Soft_Affinity(4), Hard_Affinity(5), DMTF_Reserved(
            32767), Vendor_Reserved(65535);

        private int mappingBehaviour;

        private CIMMappingBehaviourEnum(int number)
        {
            mappingBehaviour = number;
        }

        public int getNumericMappingBehaviourType()
        {
            return mappingBehaviour;
        }
    }

    public enum ConsumerVisibilityEnum
    {
        // 0, 2, 3, 4, .., 32767..65535
        // TODO actually reserved enumerations are a range.
        Unknown(0), Passed_Through(2), Virtualized(3), Not_represented(4), DMTF_reserved(32768), Vendor_Reserved(
            65535);

        private int numeric;

        private ConsumerVisibilityEnum(int number)
        {
            numeric = number;
        }

        public int getNumericConsumerVisibilityType()
        {
            return numeric;
        }
    }

    public enum ChangeableTypeEnum
    {

        Not_Changeable_Persistent(0), Changeable_Transient(1), Changeable_Persistent(2), Not_Changeable_Transient(
            3);

        private int numeric;

        private ChangeableTypeEnum(int number)
        {
            numeric = number;
        }

        public int getNumericChangeableType()
        {
            return numeric;
        }
        
        /**
         * Gets the ChangeableTypeEnum representation from a int
         * 
         * @param value the value
         * @return the ChangeableTypeEnum
         */
        public static ChangeableTypeEnum fromValue(int value)
        {
            for (ChangeableTypeEnum c : ChangeableTypeEnum.values())
            {
                if (c.getNumericChangeableType() == value)
                {
                    return c;
                }
            }
            throw new IllegalArgumentException(String.valueOf(value));
        }
    }

    public static ChangeableType createChangeableType(ChangeableTypeEnum value)
    {
        if (value == null)
        {
            return null;
        }

        ChangeableType chang = new ChangeableType();
        chang.setValue(value.getNumericChangeableType());

        return chang;
    }

    public static org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ChangeableType createChangeableTypeRASD(
        ChangeableTypeEnum value)
    {
        if (value == null)
        {
            return null;
        }
        org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ChangeableType chang =
            new org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.ChangeableType();
        chang.setValue(value.getNumericChangeableType());

        return chang;
    }

    public static ResourceType createResourceType(CIMResourceTypeEnum value)
    {
        if (value == null)
        {
            return null;
        }

        ResourceType rt = new ResourceType();
        rt.setValue(String.valueOf(value.getNumericResourceType()));

        return rt;
    }

    /***
     * TODO check value is not null
     */

    public static CimString createString(String value)
    {
        if (value == null)
        {
            return null;
        }
        CimString cs = new CimString();
        cs.setValue(value);

        return cs;
    }

    public static CimUnsignedShort createUnsignedShort(Short value)
    {
        if (value == null)
        {
            return null;
        }

        CimUnsignedShort cs = new CimUnsignedShort();
        cs.setValue(value);

        return cs;
    }
    
    /**
     * Creates a ChangeableType (which extends from UnsignedShort) from an Integer value
     * @param value value to be converted
     * @return a ChangeableType containing an Integer value
     */
    public static ChangeableType createChangeableTypeFromInteger(Integer value)
    {
        if ( value == null)
        {
            return null;
        }
        
        ChangeableType cs = new ChangeableType();
        cs.setValue(value);        
        return cs;
    }
    
    /**
     * Creates a MappingBehaviour (which extends from UnsignedShort) from an Integer value
     * @param value value to be converted
     * @return a ChangeableType containing an Integer parsed to String value
     */
    public static MappingBehavior createMappingBehaviorFromInteger(Integer value)
    {
        if ( value == null)
        {
            return null;
        }
        
        MappingBehavior mv = new MappingBehavior();
        mv.setValue(String.valueOf(value));     
        return mv;
    }
    

    public static Msg createMsgStrings(String messageId, String value)
        throws RequiredAttributeException
    {
        Msg message = new Msg();

        if (messageId == null || message == null)
        {
            throw new RequiredAttributeException("MessageId or Content for Strings.Msg");
        }

        message.setMsgid(messageId);
        message.setValue(value);

        return message;
    }

    public static CimDateTime createCurrentTime()
    {
        /**
         * TODO to implement
         */
        throw new NotImplementedException();
    }

    public static CimUnsignedLong createUnsignedLong(Long value)
    {
        if (value == null)
        {
            return null;
        }

        CimUnsignedLong lon = new CimUnsignedLong();
        lon.setValue(BigInteger.valueOf(value));
        return lon;
    }

    public static Caption createCaptionVSSD(String value)
    {
        if (value == null)
        {
            return null;
        }
        Caption cap = new Caption();
        cap.setValue(value);
        return cap;
    }

    public static org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.Caption createCaptionRASD(
        String value)
    {
        if (value == null)
        {
            return null;
        }

        org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.Caption cap =
            new org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_resourceallocationsettingdata.Caption();
        cap.setValue(value);

        return cap;
    }

    public static MsgType createMsg(String value, String id)
    {
        MsgType msg = new MsgType();

        if (value != null)
        {
            msg.setValue(value);
        }
        if (id != null)
        {
            msg.setMsgid(id);
        }

        if (value == null && id == null)
        {
            msg = null;
        }

        return msg;
    }

    public static CimUnsignedInt createUnsignedInt(Long value)
    {
        if (value == null)
        {
            return null;
        }

        CimUnsignedInt integ = new CimUnsignedInt();
        integ.setValue(value);

        return integ;
    }

    public static CimBoolean createBoolean(Boolean value)
    {
        if (value == null)
        {
            return null;
        }

        CimBoolean boole = new CimBoolean();
        boole.setValue(value);

        return boole;
    }
    
    /**
     * This method provides the C++ int-to-boolean logic to pass any integer value to a Cimboolean object.
     * 
     * @param value integer which represents a boolean in C/C++ logic
     * @return a CimBoolean object containing the boolean representation of the integer.
     */
    public static CimBoolean createBooleanFromInt(int value)
    {
        CimBoolean boole = new CimBoolean();
        
        if (value == 1)
        {
            boole.setValue(true);
        }
        else
        {
            boole.setValue(false);
        }
        
        return boole;
    }
    
    /**
     * Create a ConsumerVisibility object from an integer
     * @param value integer to convert
     * @return a ConsumerVisibility object containing the given Integer
     */
    public static ConsumerVisibility createConsumerVisibilityFromInteger(Integer value)
    {
        if (value == null)
        {
            return null;
        }
        ConsumerVisibility cv = new ConsumerVisibility();
        cv.setValue(String.valueOf(value));
        return cv;
    }
    
    /**
     * Create a ResourceType object form an Integer
     * @param value Integer to convert
     * @return a ResourceType object containing the given Integer
     */
    public static ResourceType createResourceTypeFromInteger(Integer value)
    {
        if (value == null)
        {
            return null;
        }
     
        ResourceType rs = new ResourceType();
        rs.setValue(String.valueOf(value));
        
        return rs;
    }

}
