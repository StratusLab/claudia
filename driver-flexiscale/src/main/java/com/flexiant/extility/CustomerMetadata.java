/**
 * CustomerMetadata.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class CustomerMetadata  implements java.io.Serializable {
    private java.lang.String public_metadata;

    private java.lang.String private_metadata;

    private java.lang.String restricted_metadata;

    private java.lang.String system_metadata;

    public CustomerMetadata() {
    }

    public CustomerMetadata(
           java.lang.String public_metadata,
           java.lang.String private_metadata,
           java.lang.String restricted_metadata,
           java.lang.String system_metadata) {
           this.public_metadata = public_metadata;
           this.private_metadata = private_metadata;
           this.restricted_metadata = restricted_metadata;
           this.system_metadata = system_metadata;
    }


    /**
     * Gets the public_metadata value for this CustomerMetadata.
     * 
     * @return public_metadata
     */
    public java.lang.String getPublic_metadata() {
        return public_metadata;
    }


    /**
     * Sets the public_metadata value for this CustomerMetadata.
     * 
     * @param public_metadata
     */
    public void setPublic_metadata(java.lang.String public_metadata) {
        this.public_metadata = public_metadata;
    }


    /**
     * Gets the private_metadata value for this CustomerMetadata.
     * 
     * @return private_metadata
     */
    public java.lang.String getPrivate_metadata() {
        return private_metadata;
    }


    /**
     * Sets the private_metadata value for this CustomerMetadata.
     * 
     * @param private_metadata
     */
    public void setPrivate_metadata(java.lang.String private_metadata) {
        this.private_metadata = private_metadata;
    }


    /**
     * Gets the restricted_metadata value for this CustomerMetadata.
     * 
     * @return restricted_metadata
     */
    public java.lang.String getRestricted_metadata() {
        return restricted_metadata;
    }


    /**
     * Sets the restricted_metadata value for this CustomerMetadata.
     * 
     * @param restricted_metadata
     */
    public void setRestricted_metadata(java.lang.String restricted_metadata) {
        this.restricted_metadata = restricted_metadata;
    }


    /**
     * Gets the system_metadata value for this CustomerMetadata.
     * 
     * @return system_metadata
     */
    public java.lang.String getSystem_metadata() {
        return system_metadata;
    }


    /**
     * Sets the system_metadata value for this CustomerMetadata.
     * 
     * @param system_metadata
     */
    public void setSystem_metadata(java.lang.String system_metadata) {
        this.system_metadata = system_metadata;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CustomerMetadata)) return false;
        CustomerMetadata other = (CustomerMetadata) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.public_metadata==null && other.getPublic_metadata()==null) || 
             (this.public_metadata!=null &&
              this.public_metadata.equals(other.getPublic_metadata()))) &&
            ((this.private_metadata==null && other.getPrivate_metadata()==null) || 
             (this.private_metadata!=null &&
              this.private_metadata.equals(other.getPrivate_metadata()))) &&
            ((this.restricted_metadata==null && other.getRestricted_metadata()==null) || 
             (this.restricted_metadata!=null &&
              this.restricted_metadata.equals(other.getRestricted_metadata()))) &&
            ((this.system_metadata==null && other.getSystem_metadata()==null) || 
             (this.system_metadata!=null &&
              this.system_metadata.equals(other.getSystem_metadata())));
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
        if (getPublic_metadata() != null) {
            _hashCode += getPublic_metadata().hashCode();
        }
        if (getPrivate_metadata() != null) {
            _hashCode += getPrivate_metadata().hashCode();
        }
        if (getRestricted_metadata() != null) {
            _hashCode += getRestricted_metadata().hashCode();
        }
        if (getSystem_metadata() != null) {
            _hashCode += getSystem_metadata().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CustomerMetadata.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "CustomerMetadata"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("public_metadata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "public_metadata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("private_metadata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "private_metadata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restricted_metadata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "restricted_metadata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("system_metadata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "system_metadata"));
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
