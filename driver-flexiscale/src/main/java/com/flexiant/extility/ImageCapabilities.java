/**
 * ImageCapabilities.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class ImageCapabilities  implements java.io.Serializable {
    private java.lang.Boolean can_clone;

    private java.lang.Boolean can_snapshot;

    private java.lang.Boolean can_image;

    private java.lang.Boolean can_have_additional_disks;

    private java.lang.Boolean can_be_secondary_disk;

    private java.lang.Boolean can_console;

    private java.lang.Boolean can_start;

    private java.lang.Boolean can_create_server;

    private java.lang.Boolean can_be_detached_from_server;

    public ImageCapabilities() {
    }

    public ImageCapabilities(
           java.lang.Boolean can_clone,
           java.lang.Boolean can_snapshot,
           java.lang.Boolean can_image,
           java.lang.Boolean can_have_additional_disks,
           java.lang.Boolean can_be_secondary_disk,
           java.lang.Boolean can_console,
           java.lang.Boolean can_start,
           java.lang.Boolean can_create_server,
           java.lang.Boolean can_be_detached_from_server) {
           this.can_clone = can_clone;
           this.can_snapshot = can_snapshot;
           this.can_image = can_image;
           this.can_have_additional_disks = can_have_additional_disks;
           this.can_be_secondary_disk = can_be_secondary_disk;
           this.can_console = can_console;
           this.can_start = can_start;
           this.can_create_server = can_create_server;
           this.can_be_detached_from_server = can_be_detached_from_server;
    }


    /**
     * Gets the can_clone value for this ImageCapabilities.
     * 
     * @return can_clone
     */
    public java.lang.Boolean getCan_clone() {
        return can_clone;
    }


    /**
     * Sets the can_clone value for this ImageCapabilities.
     * 
     * @param can_clone
     */
    public void setCan_clone(java.lang.Boolean can_clone) {
        this.can_clone = can_clone;
    }


    /**
     * Gets the can_snapshot value for this ImageCapabilities.
     * 
     * @return can_snapshot
     */
    public java.lang.Boolean getCan_snapshot() {
        return can_snapshot;
    }


    /**
     * Sets the can_snapshot value for this ImageCapabilities.
     * 
     * @param can_snapshot
     */
    public void setCan_snapshot(java.lang.Boolean can_snapshot) {
        this.can_snapshot = can_snapshot;
    }


    /**
     * Gets the can_image value for this ImageCapabilities.
     * 
     * @return can_image
     */
    public java.lang.Boolean getCan_image() {
        return can_image;
    }


    /**
     * Sets the can_image value for this ImageCapabilities.
     * 
     * @param can_image
     */
    public void setCan_image(java.lang.Boolean can_image) {
        this.can_image = can_image;
    }


    /**
     * Gets the can_have_additional_disks value for this ImageCapabilities.
     * 
     * @return can_have_additional_disks
     */
    public java.lang.Boolean getCan_have_additional_disks() {
        return can_have_additional_disks;
    }


    /**
     * Sets the can_have_additional_disks value for this ImageCapabilities.
     * 
     * @param can_have_additional_disks
     */
    public void setCan_have_additional_disks(java.lang.Boolean can_have_additional_disks) {
        this.can_have_additional_disks = can_have_additional_disks;
    }


    /**
     * Gets the can_be_secondary_disk value for this ImageCapabilities.
     * 
     * @return can_be_secondary_disk
     */
    public java.lang.Boolean getCan_be_secondary_disk() {
        return can_be_secondary_disk;
    }


    /**
     * Sets the can_be_secondary_disk value for this ImageCapabilities.
     * 
     * @param can_be_secondary_disk
     */
    public void setCan_be_secondary_disk(java.lang.Boolean can_be_secondary_disk) {
        this.can_be_secondary_disk = can_be_secondary_disk;
    }


    /**
     * Gets the can_console value for this ImageCapabilities.
     * 
     * @return can_console
     */
    public java.lang.Boolean getCan_console() {
        return can_console;
    }


    /**
     * Sets the can_console value for this ImageCapabilities.
     * 
     * @param can_console
     */
    public void setCan_console(java.lang.Boolean can_console) {
        this.can_console = can_console;
    }


    /**
     * Gets the can_start value for this ImageCapabilities.
     * 
     * @return can_start
     */
    public java.lang.Boolean getCan_start() {
        return can_start;
    }


    /**
     * Sets the can_start value for this ImageCapabilities.
     * 
     * @param can_start
     */
    public void setCan_start(java.lang.Boolean can_start) {
        this.can_start = can_start;
    }


    /**
     * Gets the can_create_server value for this ImageCapabilities.
     * 
     * @return can_create_server
     */
    public java.lang.Boolean getCan_create_server() {
        return can_create_server;
    }


    /**
     * Sets the can_create_server value for this ImageCapabilities.
     * 
     * @param can_create_server
     */
    public void setCan_create_server(java.lang.Boolean can_create_server) {
        this.can_create_server = can_create_server;
    }


    /**
     * Gets the can_be_detached_from_server value for this ImageCapabilities.
     * 
     * @return can_be_detached_from_server
     */
    public java.lang.Boolean getCan_be_detached_from_server() {
        return can_be_detached_from_server;
    }


    /**
     * Sets the can_be_detached_from_server value for this ImageCapabilities.
     * 
     * @param can_be_detached_from_server
     */
    public void setCan_be_detached_from_server(java.lang.Boolean can_be_detached_from_server) {
        this.can_be_detached_from_server = can_be_detached_from_server;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImageCapabilities)) return false;
        ImageCapabilities other = (ImageCapabilities) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.can_clone==null && other.getCan_clone()==null) || 
             (this.can_clone!=null &&
              this.can_clone.equals(other.getCan_clone()))) &&
            ((this.can_snapshot==null && other.getCan_snapshot()==null) || 
             (this.can_snapshot!=null &&
              this.can_snapshot.equals(other.getCan_snapshot()))) &&
            ((this.can_image==null && other.getCan_image()==null) || 
             (this.can_image!=null &&
              this.can_image.equals(other.getCan_image()))) &&
            ((this.can_have_additional_disks==null && other.getCan_have_additional_disks()==null) || 
             (this.can_have_additional_disks!=null &&
              this.can_have_additional_disks.equals(other.getCan_have_additional_disks()))) &&
            ((this.can_be_secondary_disk==null && other.getCan_be_secondary_disk()==null) || 
             (this.can_be_secondary_disk!=null &&
              this.can_be_secondary_disk.equals(other.getCan_be_secondary_disk()))) &&
            ((this.can_console==null && other.getCan_console()==null) || 
             (this.can_console!=null &&
              this.can_console.equals(other.getCan_console()))) &&
            ((this.can_start==null && other.getCan_start()==null) || 
             (this.can_start!=null &&
              this.can_start.equals(other.getCan_start()))) &&
            ((this.can_create_server==null && other.getCan_create_server()==null) || 
             (this.can_create_server!=null &&
              this.can_create_server.equals(other.getCan_create_server()))) &&
            ((this.can_be_detached_from_server==null && other.getCan_be_detached_from_server()==null) || 
             (this.can_be_detached_from_server!=null &&
              this.can_be_detached_from_server.equals(other.getCan_be_detached_from_server())));
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
        if (getCan_clone() != null) {
            _hashCode += getCan_clone().hashCode();
        }
        if (getCan_snapshot() != null) {
            _hashCode += getCan_snapshot().hashCode();
        }
        if (getCan_image() != null) {
            _hashCode += getCan_image().hashCode();
        }
        if (getCan_have_additional_disks() != null) {
            _hashCode += getCan_have_additional_disks().hashCode();
        }
        if (getCan_be_secondary_disk() != null) {
            _hashCode += getCan_be_secondary_disk().hashCode();
        }
        if (getCan_console() != null) {
            _hashCode += getCan_console().hashCode();
        }
        if (getCan_start() != null) {
            _hashCode += getCan_start().hashCode();
        }
        if (getCan_create_server() != null) {
            _hashCode += getCan_create_server().hashCode();
        }
        if (getCan_be_detached_from_server() != null) {
            _hashCode += getCan_be_detached_from_server().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImageCapabilities.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ImageCapabilities"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_clone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_clone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_snapshot");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_snapshot"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_image");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_image"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_have_additional_disks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_have_additional_disks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_be_secondary_disk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_be_secondary_disk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_console");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_console"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_start");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_start"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_create_server");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_create_server"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("can_be_detached_from_server");
        elemField.setXmlName(new javax.xml.namespace.QName("", "can_be_detached_from_server"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
