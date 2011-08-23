/**
 * SSHKey.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class SSHKey  implements java.io.Serializable {
    private java.lang.Long key_id;

    private java.lang.String username;

    private java.lang.String public_key;

    private java.lang.String description;

    public SSHKey() {
    }

    public SSHKey(
           java.lang.Long key_id,
           java.lang.String username,
           java.lang.String public_key,
           java.lang.String description) {
           this.key_id = key_id;
           this.username = username;
           this.public_key = public_key;
           this.description = description;
    }


    /**
     * Gets the key_id value for this SSHKey.
     * 
     * @return key_id
     */
    public java.lang.Long getKey_id() {
        return key_id;
    }


    /**
     * Sets the key_id value for this SSHKey.
     * 
     * @param key_id
     */
    public void setKey_id(java.lang.Long key_id) {
        this.key_id = key_id;
    }


    /**
     * Gets the username value for this SSHKey.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this SSHKey.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the public_key value for this SSHKey.
     * 
     * @return public_key
     */
    public java.lang.String getPublic_key() {
        return public_key;
    }


    /**
     * Sets the public_key value for this SSHKey.
     * 
     * @param public_key
     */
    public void setPublic_key(java.lang.String public_key) {
        this.public_key = public_key;
    }


    /**
     * Gets the description value for this SSHKey.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this SSHKey.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SSHKey)) return false;
        SSHKey other = (SSHKey) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.key_id==null && other.getKey_id()==null) || 
             (this.key_id!=null &&
              this.key_id.equals(other.getKey_id()))) &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.public_key==null && other.getPublic_key()==null) || 
             (this.public_key!=null &&
              this.public_key.equals(other.getPublic_key()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription())));
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
        if (getKey_id() != null) {
            _hashCode += getKey_id().hashCode();
        }
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPublic_key() != null) {
            _hashCode += getPublic_key().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SSHKey.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "SSHKey"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "key_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("public_key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "public_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
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
