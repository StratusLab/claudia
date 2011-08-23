/**
 * DiskSnapshot.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class DiskSnapshot  implements java.io.Serializable {
    private java.lang.Long disk_id;

    private java.lang.Long snapshot_id;

    private java.lang.String snapshot_name;

    private java.util.Calendar timestamp;

    private java.lang.Long vdc_id;

    private java.lang.Integer snapshot_capacity;

    private java.lang.String snapshot_uuid;

    public DiskSnapshot() {
    }

    public DiskSnapshot(
           java.lang.Long disk_id,
           java.lang.Long snapshot_id,
           java.lang.String snapshot_name,
           java.util.Calendar timestamp,
           java.lang.Long vdc_id,
           java.lang.Integer snapshot_capacity,
           java.lang.String snapshot_uuid) {
           this.disk_id = disk_id;
           this.snapshot_id = snapshot_id;
           this.snapshot_name = snapshot_name;
           this.timestamp = timestamp;
           this.vdc_id = vdc_id;
           this.snapshot_capacity = snapshot_capacity;
           this.snapshot_uuid = snapshot_uuid;
    }


    /**
     * Gets the disk_id value for this DiskSnapshot.
     * 
     * @return disk_id
     */
    public java.lang.Long getDisk_id() {
        return disk_id;
    }


    /**
     * Sets the disk_id value for this DiskSnapshot.
     * 
     * @param disk_id
     */
    public void setDisk_id(java.lang.Long disk_id) {
        this.disk_id = disk_id;
    }


    /**
     * Gets the snapshot_id value for this DiskSnapshot.
     * 
     * @return snapshot_id
     */
    public java.lang.Long getSnapshot_id() {
        return snapshot_id;
    }


    /**
     * Sets the snapshot_id value for this DiskSnapshot.
     * 
     * @param snapshot_id
     */
    public void setSnapshot_id(java.lang.Long snapshot_id) {
        this.snapshot_id = snapshot_id;
    }


    /**
     * Gets the snapshot_name value for this DiskSnapshot.
     * 
     * @return snapshot_name
     */
    public java.lang.String getSnapshot_name() {
        return snapshot_name;
    }


    /**
     * Sets the snapshot_name value for this DiskSnapshot.
     * 
     * @param snapshot_name
     */
    public void setSnapshot_name(java.lang.String snapshot_name) {
        this.snapshot_name = snapshot_name;
    }


    /**
     * Gets the timestamp value for this DiskSnapshot.
     * 
     * @return timestamp
     */
    public java.util.Calendar getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp value for this DiskSnapshot.
     * 
     * @param timestamp
     */
    public void setTimestamp(java.util.Calendar timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Gets the vdc_id value for this DiskSnapshot.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this DiskSnapshot.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the snapshot_capacity value for this DiskSnapshot.
     * 
     * @return snapshot_capacity
     */
    public java.lang.Integer getSnapshot_capacity() {
        return snapshot_capacity;
    }


    /**
     * Sets the snapshot_capacity value for this DiskSnapshot.
     * 
     * @param snapshot_capacity
     */
    public void setSnapshot_capacity(java.lang.Integer snapshot_capacity) {
        this.snapshot_capacity = snapshot_capacity;
    }


    /**
     * Gets the snapshot_uuid value for this DiskSnapshot.
     * 
     * @return snapshot_uuid
     */
    public java.lang.String getSnapshot_uuid() {
        return snapshot_uuid;
    }


    /**
     * Sets the snapshot_uuid value for this DiskSnapshot.
     * 
     * @param snapshot_uuid
     */
    public void setSnapshot_uuid(java.lang.String snapshot_uuid) {
        this.snapshot_uuid = snapshot_uuid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DiskSnapshot)) return false;
        DiskSnapshot other = (DiskSnapshot) obj;
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
            ((this.snapshot_id==null && other.getSnapshot_id()==null) || 
             (this.snapshot_id!=null &&
              this.snapshot_id.equals(other.getSnapshot_id()))) &&
            ((this.snapshot_name==null && other.getSnapshot_name()==null) || 
             (this.snapshot_name!=null &&
              this.snapshot_name.equals(other.getSnapshot_name()))) &&
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this.snapshot_capacity==null && other.getSnapshot_capacity()==null) || 
             (this.snapshot_capacity!=null &&
              this.snapshot_capacity.equals(other.getSnapshot_capacity()))) &&
            ((this.snapshot_uuid==null && other.getSnapshot_uuid()==null) || 
             (this.snapshot_uuid!=null &&
              this.snapshot_uuid.equals(other.getSnapshot_uuid())));
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
        if (getSnapshot_id() != null) {
            _hashCode += getSnapshot_id().hashCode();
        }
        if (getSnapshot_name() != null) {
            _hashCode += getSnapshot_name().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (getSnapshot_capacity() != null) {
            _hashCode += getSnapshot_capacity().hashCode();
        }
        if (getSnapshot_uuid() != null) {
            _hashCode += getSnapshot_uuid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DiskSnapshot.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "DiskSnapshot"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("snapshot_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snapshot_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("snapshot_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snapshot_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("snapshot_capacity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snapshot_capacity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("snapshot_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snapshot_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
