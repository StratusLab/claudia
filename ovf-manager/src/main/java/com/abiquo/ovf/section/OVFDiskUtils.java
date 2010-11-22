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

package com.abiquo.ovf.section;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.SectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualDiskDescType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.OVFReferenceUtils;
import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.IdNotFoundException;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;
import com.abiquo.ovf.exceptions.SectionException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;

public class OVFDiskUtils
{
    private final static Logger log = LoggerFactory.getLogger(OVFDiskUtils.class);

    /*******************************************************************************
     * DISK SECTION
     *******************************************************************************/
    
   private final static String KVM_URI = "http://kvm";
   
   private final static String VBOX_URI = "http://vbox";
   
   private final static String XEN_URI = "http://xen";
   
   
   
   private final static String VMWARE_URI = "http://www.vmware.com/specifications/vmdk.html#sparse";
   
   private final static String VMWARE_COMPRESSED_URI  = "http://www.vmware.com/specifications/vmdk.html#compressed";
   
   private final static String VMWARE_STREAM_OPTIMIZED_URI  = "http://www.vmware.com/interfaces/specifications/vmdk.html#streamOptimized";

    /**
     * Supported virtual disk formats.
     */
    public enum DiskFormat
    {
        VMWARE, VMWARE_COMPRESSED, VBOX, XEN, KVM, HYPERV, QEMU, LXC, OPENVZ
    } // TODO vmware_compressed =?= streamOptimized

    // VMDX, OVF, RAW, BIN, VDI, QEMM???

    public enum DiskSizeUnit
    {
        Bytes, KiloBytes, MegaBytes, GigaBytes
    } // TODO Bits, KiloBits, MegaBits, GigaBits, Words, DoubleWords, QuadWords

    

    /**
     * In order to meet the 5.2 clause an URI is required as disk format. ''the disk format shall be
     * given by a URI which identifies an unencumbered specification on how to interpret the disk
     * format'' TODO add for all the supported disk formats.
     */
    public static String formatUri(DiskFormat format)
    {
        String formatUri;

        switch (format)
        {
            case VMWARE:
                formatUri = VMWARE_URI;
                break;

            case VMWARE_COMPRESSED:
                formatUri = VMWARE_COMPRESSED_URI; // streamOptimized
                break;
            /**
             * case VBOX: break; case XEN: break; case KVM: break; case HYPERV: break; case QEMU:
             * break; case LXC: break; case OPENVZ: break;
             * TODO for KVM as well
             **/

            default:
                formatUri = "http://" + format.toString().toLowerCase(); // TODO as any vendor but vmware have URI
                // ....
                break;
        }

        return formatUri;
    }
    
    
    
    /**
     * Returns a DiskFormat object according to the format URI supplied.
     * 
     * @param formatURI
     * @return
     * @throws RequiredAttributeException 
     */
    public static DiskFormat getDiskFormat(String formatURI) throws RequiredAttributeException
    {
        DiskFormat diskFormat = null;
        
        if(formatURI == null)
        {
            throw new RequiredAttributeException("No disk format URI supplied");
        }
        
        if(formatURI.equals(VMWARE_URI) || formatURI.equals(VMWARE_STREAM_OPTIMIZED_URI))
        {
            diskFormat = DiskFormat.VMWARE;
            
        }else if(formatURI.equals(VMWARE_COMPRESSED_URI))
        {            
            diskFormat = DiskFormat.VMWARE_COMPRESSED;
            
        }else if(formatURI.equals(KVM_URI))
        {
            diskFormat = DiskFormat.KVM;
            
        }else if(formatURI.equals(VBOX_URI))
        {
            diskFormat = DiskFormat.VBOX;
            
        }else if(formatURI.equals(XEN_URI))
        {
            diskFormat = DiskFormat.XEN;
        }
        else
        {
            throw new RequiredAttributeException("No matching disk format found for the URI: " + formatURI);
        }
        
        return diskFormat;
    }

    private static BigInteger toBytes(long size, DiskSizeUnit unit)
    {
        BigInteger b1024 = BigInteger.valueOf(1024);
        BigInteger bytes = BigInteger.valueOf(size);

        switch (unit)
        {
            case GigaBytes:
                bytes = bytes.multiply(b1024);
            case MegaBytes:
                bytes = bytes.multiply(b1024);
            case KiloBytes:
                bytes = bytes.multiply(b1024);
            case Bytes:
            default:
                break;
        }

        return bytes;
    }

    /**
     * Adds a virtual disk description to mount an existing virtual image file on the provided OVF
     * envelope. If the fileId is not provided an empty disk is added. Create the DiskSection if
     * there is any DiskDesc on the OVF envelope.
     * 
     * @param envelope, the envelope containing the fileId on its references section and where the
     *            virtual disk description will be added.
     * @param diskId, the required desired disk identifier for the new virtual disk description.
     * @param fileID, the optional file identifier on the references section to be mounted on the
     *            virtual disk. If any provided its an empty disk.
     * @param format, the required file image format (hypervisor type)
     * @param capacity, the required disk capacity.
     * @param capacityUnit, the optional capacity units. If none specified assumed bytes.
     * @param populatedSize, the optional expected used size of the disk.
     * @param parentRefDiskId, the optional parent disk, modified blocks in comparison to a parent
     *            image.
     * @throw IdNotFound, it the provided fileId is not on the References section or the
     *        parentRefDiskId is not on the Disk section.
     * @throw IdAlreadyExists, it the provided diskId is already on the Disk section. TODO capacity
     *        can also be property on the envelope "${some.property}" from the ProductSection
     */
    public static void addDiskDescription(EnvelopeType envelope, String diskId, String fileId,
        DiskFormat format, Long capacity, DiskSizeUnit capacityUnit, Long populatedSize,
        String parentRefDiskId) throws IdNotFoundException, IdAlreadyExistsException
    {
        VirtualDiskDescType disk;
        FileType file = null;
        BigInteger byteCapacity;

        // TODO assert diskId/capacity not null

        // TODO log info

        disk = new VirtualDiskDescType();

        disk.setDiskId(diskId);
        disk.setFormat(formatUri(format)); // TODO use referenced file extension to infer

        if (fileId != null)
        {
            file = OVFReferenceUtils.getReferencedFile(envelope, fileId);
            disk.setFileRef(fileId);
        }

        if (capacityUnit != null & capacityUnit != DiskSizeUnit.Bytes)
        {
            disk.setCapacityAllocationUnits(capacityUnit.name());

            byteCapacity = toBytes(capacity, capacityUnit);
        }
        else
        {
            byteCapacity = BigInteger.valueOf(capacity);
        }

        // TODO if(capacity.startsWith("$"))
        disk.setCapacity(String.valueOf(capacity));

        // TODO check there aren't any disk with the same fileId. Also assert the same order with
        // fileId (references and Disk sections must match)

        if (populatedSize != null)
        {
            if (file == null)
            {
                log.error("An empty disk can not have expected used capacity");
            }

            if (populatedSize > byteCapacity.longValue())
            {
                // TODO fail
                log.error("The populate size ({}b) is higher than the capacity ({}b)",
                    populatedSize, byteCapacity.longValue());
            }

            disk.setPopulatedSize(populatedSize);
        }

        if (parentRefDiskId != null)
        {
            getDiskDescription(envelope, parentRefDiskId);

            disk.setParentRef(parentRefDiskId);
        }
        else if (file != null)
        {
            if (byteCapacity.longValue() < file.getSize().longValue())
            {
                log.error("File size ({}) higher than the specified capacity ({})", file.getSize()
                    .longValue(), byteCapacity.longValue());
            }
        }

        addDisk(envelope, disk);
    }

    /**
     * Get a specific virtual disk description.
     * 
     * @param envelope, the OVF envelope to be checked.
     * @param diskId, the desired disk identifier.
     * @throws IdNotFoundException if there is any disk with the required identifier on the envelope
     *             disk section.
     */
    public static VirtualDiskDescType getDiskDescription(EnvelopeType envelope, String diskId)
        throws IdNotFoundException
    {
        DiskSectionType sectionDisk;
        SectionType section;

        for (JAXBElement< ? extends SectionType> jxbSection : envelope.getSection())
        {
            section = jxbSection.getValue();

            if (section instanceof DiskSectionType)
            {
                sectionDisk = (DiskSectionType) section;

                for (VirtualDiskDescType vdisk : sectionDisk.getDisk())
                {
                    if (diskId.equals(vdisk.getDiskId()))
                    {
                        return vdisk;
                    }
                }// disks
            }// disk section
        }// sections

        throw new IdNotFoundException("Virtual disk description id :" + diskId);
    }

    /**
     * TODO use envelope Adds a VirtualDiskDescription to an existing VirtualSystem.
     * 
     * @throws IdAlreadyExists if the provided VirtualDiscDescription id is already on the
     *             VirutalSystem's DiskSection.
     */
    public static void addDisk(EnvelopeType envelope, VirtualDiskDescType vDisk)
        throws IdAlreadyExistsException
    {
        SectionType section;
        DiskSectionType sectionDisk = null;

        try
        {
            sectionDisk = OVFEnvelopeUtils.getSection(envelope, DiskSectionType.class);
        }
        catch (SectionNotPresentException e)
        {
            try
            {
                sectionDisk = OVFEnvelopeUtils.createSection(DiskSectionType.class, null);
                OVFEnvelopeUtils.addSection(envelope, sectionDisk);
            }
            catch (SectionException e1)
            {
                // from a SectionNotPresentException
            }
        }
        catch (InvalidSectionException invalid)
        {
            // Envelope can have disk section
        }

        for (VirtualDiskDescType vdd : sectionDisk.getDisk())
        {
            if (vDisk.getDiskId().equalsIgnoreCase(vdd.getDiskId()))
            {
                final String msg = "The VirtualDiskDescription diskId" + vDisk.getDiskId();
                throw new IdAlreadyExistsException(msg);
            }
        }

        sectionDisk.getDisk().add(vDisk);
    }

    /**
     * TODO use envelope Adds a VirtualDiskDescription to an existing VirtualSystem.
     * 
     * @throws IdAlreadyExists if the provided VirtualDiscDescription id is already on the
     *             VirutalSystem's DiskSection.
     */
    public static void addDisk(DiskSectionType diskSection, VirtualDiskDescType vDisk)
        throws IdAlreadyExistsException
    {

        for (VirtualDiskDescType vdd : diskSection.getDisk())
        {
            if (vDisk.getDiskId().equalsIgnoreCase(vdd.getDiskId()))
            {
                final String msg = "The VirtualDiskDescription diskId" + vDisk.getDiskId();
                throw new IdAlreadyExistsException(msg);
            }
        }

        diskSection.getDisk().add(vDisk);
    }

    /**
     * Adds a virtual disk description to mount an existing virtual image file. If the fileId is not
     * provided an empty disk is added. Create the DiskSection if there is any DiskDesc on the OVF
     * envelope. This method do not assure fileId is on the EnvelopeReferenceSection (use
     * addDiskDescription instead)
     * 
     * @param diskId, the required desired disk identifier for the new virtual disk description.
     * @param fileID, the optional file identifier on the references section to be mounted on the
     *            virtual disk. If any provided its an empty disk.
     * @param format, the required file image format (hypervisor type)
     * @param capacity, the required disk capacity.
     * @param capacityUnit, the optional capacity units. If none specified assumed bytes.
     * @param populatedSize, the optional expected used size of the disk.
     * @param parentRefDiskId, the optional parent disk, modified blocks in comparison to a parent
     *            image.
     * @throw IdNotFound, it the provided fileId is not on the References section or the
     *        parentRefDiskId is not on the Disk section.
     * @throw IdAlreadyExists, it the provided diskId is already on the Disk section. TODO capacity
     *        can also be property on the envelope "${some.property}" from the ProductSection
     */
    public static VirtualDiskDescType createDiskDescription(String diskId, String fileId,
        DiskFormat format, Long capacity, DiskSizeUnit capacityUnit, Long populatedSize,
        String parentRefDiskId)
    {
        VirtualDiskDescType disk;
        BigInteger byteCapacity;

        // TODO assert diskId/capacity not null
        // TODO log info

        disk = new VirtualDiskDescType();

        disk.setDiskId(diskId);
        disk.setFormat(formatUri(format)); // TODO use referenced file extension to infer

        if (fileId != null)
        {
            // file = OVFReferenceUtils.getReferencedFile(envelope, fileId);
            disk.setFileRef(fileId);
        }

        if (capacityUnit != null & capacityUnit != DiskSizeUnit.Bytes)
        {
            disk.setCapacityAllocationUnits(capacityUnit.name());

            byteCapacity = toBytes(capacity, capacityUnit);
        }
        else
        {
            byteCapacity = BigInteger.valueOf(capacity);
        }

        // TODO if(capacity.startsWith("$"))
        disk.setCapacity(String.valueOf(capacity));

        // TODO check there aren't any disk with the same fileId. Also assert the same order with
        // fileId (references and Disk sections must match)

        if (populatedSize != null)
        {
            if (fileId == null)
            {
                log.error("An empty disk can not have expected used capacity");
            }

            if (populatedSize > byteCapacity.longValue())
            {
                // TODO fail
                log.error("The populate size ({}b) is higher than the capacity ({}b)",
                    populatedSize, byteCapacity.longValue());
            }

            disk.setPopulatedSize(populatedSize);
        }

        if (parentRefDiskId != null)
        {
            // getDiskDescription(envelope, parentRefDiskId);

            disk.setParentRef(parentRefDiskId);
        }/*
          * else if (file != null) { if (byteCapacity.longValue() < file.getSize().longValue()) {
          * log.error("File size ({}) higher than the specified capacity ({})", file.getSize()
          * .longValue(), byteCapacity.longValue()); } }
          */

        return disk;
    }
    
    /**
     * Set other attributes to VirtualDiskDescType
     * 
     * @param disk the VirtualDiskDescType we work with
     * @param key Key of the Map
     * @param value value of the key
     * @throws RequiredAttributeException if key or diskSection are null throws this method
     * @throws IdAlreadyExistsException if key already inserted
     */
    public static void addOtherAttributes(VirtualDiskDescType disk, QName key, String value) throws RequiredAttributeException, IdAlreadyExistsException
    {
        if (disk == null || key == null)
        {
            throw new RequiredAttributeException("Some values are null!");
        }
        if (disk.getOtherAttributes().get(key) != null)
        {
            throw new IdAlreadyExistsException("Key already exists");
        }
        disk.getOtherAttributes().put(key, value);

    }
    

}
