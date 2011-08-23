/**
 * RemoteDisk.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class RemoteDisk  implements java.io.Serializable {
    private java.lang.String url;

    private java.lang.String checksum;

    private java.lang.String conn_user;

    private java.lang.String conn_pass;

    private java.lang.String disk_name;

    private java.lang.Long disk_productoffer_id;

    private java.lang.Boolean boot_from_this_disk;

    public RemoteDisk() {
    }

    public RemoteDisk(
           java.lang.String url,
           java.lang.String checksum,
           java.lang.String conn_user,
           java.lang.String conn_pass,
           java.lang.String disk_name,
           java.lang.Long disk_productoffer_id,
           java.lang.Boolean boot_from_this_disk) {
           this.url = url;
           this.checksum = checksum;
           this.conn_user = conn_user;
           this.conn_pass = conn_pass;
           this.disk_name = disk_name;
           this.disk_productoffer_id = disk_productoffer_id;
           this.boot_from_this_disk = boot_from_this_disk;
    }


    /**
     * Gets the url value for this RemoteDisk.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this RemoteDisk.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the checksum value for this RemoteDisk.
     * 
     * @return checksum
     */
    public java.lang.String getChecksum() {
        return checksum;
    }


    /**
     * Sets the checksum value for this RemoteDisk.
     * 
     * @param checksum
     */
    public void setChecksum(java.lang.String checksum) {
        this.checksum = checksum;
    }


    /**
     * Gets the conn_user value for this RemoteDisk.
     * 
     * @return conn_user
     */
    public java.lang.String getConn_user() {
        return conn_user;
    }


    /**
     * Sets the conn_user value for this RemoteDisk.
     * 
     * @param conn_user
     */
    public void setConn_user(java.lang.String conn_user) {
        this.conn_user = conn_user;
    }


    /**
     * Gets the conn_pass value for this RemoteDisk.
     * 
     * @return conn_pass
     */
    public java.lang.String getConn_pass() {
        return conn_pass;
    }


    /**
     * Sets the conn_pass value for this RemoteDisk.
     * 
     * @param conn_pass
     */
    public void setConn_pass(java.lang.String conn_pass) {
        this.conn_pass = conn_pass;
    }


    /**
     * Gets the disk_name value for this RemoteDisk.
     * 
     * @return disk_name
     */
    public java.lang.String getDisk_name() {
        return disk_name;
    }


    /**
     * Sets the disk_name value for this RemoteDisk.
     * 
     * @param disk_name
     */
    public void setDisk_name(java.lang.String disk_name) {
        this.disk_name = disk_name;
    }


    /**
     * Gets the disk_productoffer_id value for this RemoteDisk.
     * 
     * @return disk_productoffer_id
     */
    public java.lang.Long getDisk_productoffer_id() {
        return disk_productoffer_id;
    }


    /**
     * Sets the disk_productoffer_id value for this RemoteDisk.
     * 
     * @param disk_productoffer_id
     */
    public void setDisk_productoffer_id(java.lang.Long disk_productoffer_id) {
        this.disk_productoffer_id = disk_productoffer_id;
    }


    /**
     * Gets the boot_from_this_disk value for this RemoteDisk.
     * 
     * @return boot_from_this_disk
     */
    public java.lang.Boolean getBoot_from_this_disk() {
        return boot_from_this_disk;
    }


    /**
     * Sets the boot_from_this_disk value for this RemoteDisk.
     * 
     * @param boot_from_this_disk
     */
    public void setBoot_from_this_disk(java.lang.Boolean boot_from_this_disk) {
        this.boot_from_this_disk = boot_from_this_disk;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RemoteDisk)) return false;
        RemoteDisk other = (RemoteDisk) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.checksum==null && other.getChecksum()==null) || 
             (this.checksum!=null &&
              this.checksum.equals(other.getChecksum()))) &&
            ((this.conn_user==null && other.getConn_user()==null) || 
             (this.conn_user!=null &&
              this.conn_user.equals(other.getConn_user()))) &&
            ((this.conn_pass==null && other.getConn_pass()==null) || 
             (this.conn_pass!=null &&
              this.conn_pass.equals(other.getConn_pass()))) &&
            ((this.disk_name==null && other.getDisk_name()==null) || 
             (this.disk_name!=null &&
              this.disk_name.equals(other.getDisk_name()))) &&
            ((this.disk_productoffer_id==null && other.getDisk_productoffer_id()==null) || 
             (this.disk_productoffer_id!=null &&
              this.disk_productoffer_id.equals(other.getDisk_productoffer_id()))) &&
            ((this.boot_from_this_disk==null && other.getBoot_from_this_disk()==null) || 
             (this.boot_from_this_disk!=null &&
              this.boot_from_this_disk.equals(other.getBoot_from_this_disk())));
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
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getChecksum() != null) {
            _hashCode += getChecksum().hashCode();
        }
        if (getConn_user() != null) {
            _hashCode += getConn_user().hashCode();
        }
        if (getConn_pass() != null) {
            _hashCode += getConn_pass().hashCode();
        }
        if (getDisk_name() != null) {
            _hashCode += getDisk_name().hashCode();
        }
        if (getDisk_productoffer_id() != null) {
            _hashCode += getDisk_productoffer_id().hashCode();
        }
        if (getBoot_from_this_disk() != null) {
            _hashCode += getBoot_from_this_disk().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RemoteDisk.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "RemoteDisk"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checksum");
        elemField.setXmlName(new javax.xml.namespace.QName("", "checksum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conn_user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "conn_user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conn_pass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "conn_pass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("disk_productoffer_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_productoffer_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boot_from_this_disk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "boot_from_this_disk"));
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
