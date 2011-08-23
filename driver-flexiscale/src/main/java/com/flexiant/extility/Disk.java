/**
 * Disk.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Disk  implements java.io.Serializable {
    private java.lang.Long disk_id;

    private java.lang.String disk_name;

    private java.lang.String disk_uuid;

    private java.lang.Long vdc_id;

    private java.lang.Integer capacity;

    private java.lang.Float usage;

    private java.lang.Long server_id;

    private java.lang.Long storage_unit_id;

    private java.lang.Long image_template_id;

    private java.lang.Integer bootable;

    private long[] disk_product_offers;

    public Disk() {
    }

    public Disk(
           java.lang.Long disk_id,
           java.lang.String disk_name,
           java.lang.String disk_uuid,
           java.lang.Long vdc_id,
           java.lang.Integer capacity,
           java.lang.Float usage,
           java.lang.Long server_id,
           java.lang.Long storage_unit_id,
           java.lang.Long image_template_id,
           java.lang.Integer bootable,
           long[] disk_product_offers) {
           this.disk_id = disk_id;
           this.disk_name = disk_name;
           this.disk_uuid = disk_uuid;
           this.vdc_id = vdc_id;
           this.capacity = capacity;
           this.usage = usage;
           this.server_id = server_id;
           this.storage_unit_id = storage_unit_id;
           this.image_template_id = image_template_id;
           this.bootable = bootable;
           this.disk_product_offers = disk_product_offers;
    }


    /**
     * Gets the disk_id value for this Disk.
     * 
     * @return disk_id
     */
    public java.lang.Long getDisk_id() {
        return disk_id;
    }


    /**
     * Sets the disk_id value for this Disk.
     * 
     * @param disk_id
     */
    public void setDisk_id(java.lang.Long disk_id) {
        this.disk_id = disk_id;
    }


    /**
     * Gets the disk_name value for this Disk.
     * 
     * @return disk_name
     */
    public java.lang.String getDisk_name() {
        return disk_name;
    }


    /**
     * Sets the disk_name value for this Disk.
     * 
     * @param disk_name
     */
    public void setDisk_name(java.lang.String disk_name) {
        this.disk_name = disk_name;
    }


    /**
     * Gets the disk_uuid value for this Disk.
     * 
     * @return disk_uuid
     */
    public java.lang.String getDisk_uuid() {
        return disk_uuid;
    }


    /**
     * Sets the disk_uuid value for this Disk.
     * 
     * @param disk_uuid
     */
    public void setDisk_uuid(java.lang.String disk_uuid) {
        this.disk_uuid = disk_uuid;
    }


    /**
     * Gets the vdc_id value for this Disk.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this Disk.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the capacity value for this Disk.
     * 
     * @return capacity
     */
    public java.lang.Integer getCapacity() {
        return capacity;
    }


    /**
     * Sets the capacity value for this Disk.
     * 
     * @param capacity
     */
    public void setCapacity(java.lang.Integer capacity) {
        this.capacity = capacity;
    }


    /**
     * Gets the usage value for this Disk.
     * 
     * @return usage
     */
    public java.lang.Float getUsage() {
        return usage;
    }


    /**
     * Sets the usage value for this Disk.
     * 
     * @param usage
     */
    public void setUsage(java.lang.Float usage) {
        this.usage = usage;
    }


    /**
     * Gets the server_id value for this Disk.
     * 
     * @return server_id
     */
    public java.lang.Long getServer_id() {
        return server_id;
    }


    /**
     * Sets the server_id value for this Disk.
     * 
     * @param server_id
     */
    public void setServer_id(java.lang.Long server_id) {
        this.server_id = server_id;
    }


    /**
     * Gets the storage_unit_id value for this Disk.
     * 
     * @return storage_unit_id
     */
    public java.lang.Long getStorage_unit_id() {
        return storage_unit_id;
    }


    /**
     * Sets the storage_unit_id value for this Disk.
     * 
     * @param storage_unit_id
     */
    public void setStorage_unit_id(java.lang.Long storage_unit_id) {
        this.storage_unit_id = storage_unit_id;
    }


    /**
     * Gets the image_template_id value for this Disk.
     * 
     * @return image_template_id
     */
    public java.lang.Long getImage_template_id() {
        return image_template_id;
    }


    /**
     * Sets the image_template_id value for this Disk.
     * 
     * @param image_template_id
     */
    public void setImage_template_id(java.lang.Long image_template_id) {
        this.image_template_id = image_template_id;
    }


    /**
     * Gets the bootable value for this Disk.
     * 
     * @return bootable
     */
    public java.lang.Integer getBootable() {
        return bootable;
    }


    /**
     * Sets the bootable value for this Disk.
     * 
     * @param bootable
     */
    public void setBootable(java.lang.Integer bootable) {
        this.bootable = bootable;
    }


    /**
     * Gets the disk_product_offers value for this Disk.
     * 
     * @return disk_product_offers
     */
    public long[] getDisk_product_offers() {
        return disk_product_offers;
    }


    /**
     * Sets the disk_product_offers value for this Disk.
     * 
     * @param disk_product_offers
     */
    public void setDisk_product_offers(long[] disk_product_offers) {
        this.disk_product_offers = disk_product_offers;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Disk)) return false;
        Disk other = (Disk) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.disk_id==null && other.getDisk_id()==null) || 
             (this.disk_id!=null &&
              this.disk_id.equals(other.getDisk_id()))) &&
            ((this.disk_name==null && other.getDisk_name()==null) || 
             (this.disk_name!=null &&
              this.disk_name.equals(other.getDisk_name()))) &&
            ((this.disk_uuid==null && other.getDisk_uuid()==null) || 
             (this.disk_uuid!=null &&
              this.disk_uuid.equals(other.getDisk_uuid()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this.capacity==null && other.getCapacity()==null) || 
             (this.capacity!=null &&
              this.capacity.equals(other.getCapacity()))) &&
            ((this.usage==null && other.getUsage()==null) || 
             (this.usage!=null &&
              this.usage.equals(other.getUsage()))) &&
            ((this.server_id==null && other.getServer_id()==null) || 
             (this.server_id!=null &&
              this.server_id.equals(other.getServer_id()))) &&
            ((this.storage_unit_id==null && other.getStorage_unit_id()==null) || 
             (this.storage_unit_id!=null &&
              this.storage_unit_id.equals(other.getStorage_unit_id()))) &&
            ((this.image_template_id==null && other.getImage_template_id()==null) || 
             (this.image_template_id!=null &&
              this.image_template_id.equals(other.getImage_template_id()))) &&
            ((this.bootable==null && other.getBootable()==null) || 
             (this.bootable!=null &&
              this.bootable.equals(other.getBootable()))) &&
            ((this.disk_product_offers==null && other.getDisk_product_offers()==null) || 
             (this.disk_product_offers!=null &&
              java.util.Arrays.equals(this.disk_product_offers, other.getDisk_product_offers())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDisk_id() != null) {
            _hashCode += getDisk_id().hashCode();
        }
        if (getDisk_name() != null) {
            _hashCode += getDisk_name().hashCode();
        }
        if (getDisk_uuid() != null) {
            _hashCode += getDisk_uuid().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (getCapacity() != null) {
            _hashCode += getCapacity().hashCode();
        }
        if (getUsage() != null) {
            _hashCode += getUsage().hashCode();
        }
        if (getServer_id() != null) {
            _hashCode += getServer_id().hashCode();
        }
        if (getStorage_unit_id() != null) {
            _hashCode += getStorage_unit_id().hashCode();
        }
        if (getImage_template_id() != null) {
            _hashCode += getImage_template_id().hashCode();
        }
        if (getBootable() != null) {
            _hashCode += getBootable().hashCode();
        }
        if (getDisk_product_offers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDisk_product_offers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDisk_product_offers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Disk.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Disk"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vdc_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vdc_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capacity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "capacity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "usage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storage_unit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storage_unit_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bootable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bootable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_product_offers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_product_offers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
