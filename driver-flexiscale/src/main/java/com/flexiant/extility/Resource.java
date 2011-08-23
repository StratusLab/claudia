/**
 * Resource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Resource  implements java.io.Serializable {
    private java.lang.String uuid;

    private java.lang.String resource_type;

    private java.lang.String resource_type_description;

    public Resource() {
    }

    public Resource(
           java.lang.String uuid,
           java.lang.String resource_type,
           java.lang.String resource_type_description) {
           this.uuid = uuid;
           this.resource_type = resource_type;
           this.resource_type_description = resource_type_description;
    }


    /**
     * Gets the uuid value for this Resource.
     * 
     * @return uuid
     */
    public java.lang.String getUuid() {
        return uuid;
    }


    /**
     * Sets the uuid value for this Resource.
     * 
     * @param uuid
     */
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }


    /**
     * Gets the resource_type value for this Resource.
     * 
     * @return resource_type
     */
    public java.lang.String getResource_type() {
        return resource_type;
    }


    /**
     * Sets the resource_type value for this Resource.
     * 
     * @param resource_type
     */
    public void setResource_type(java.lang.String resource_type) {
        this.resource_type = resource_type;
    }


    /**
     * Gets the resource_type_description value for this Resource.
     * 
     * @return resource_type_description
     */
    public java.lang.String getResource_type_description() {
        return resource_type_description;
    }


    /**
     * Sets the resource_type_description value for this Resource.
     * 
     * @param resource_type_description
     */
    public void setResource_type_description(java.lang.String resource_type_description) {
        this.resource_type_description = resource_type_description;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Resource)) return false;
        Resource other = (Resource) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.uuid==null && other.getUuid()==null) || 
             (this.uuid!=null &&
              this.uuid.equals(other.getUuid()))) &&
            ((this.resource_type==null && other.getResource_type()==null) || 
             (this.resource_type!=null &&
              this.resource_type.equals(other.getResource_type()))) &&
            ((this.resource_type_description==null && other.getResource_type_description()==null) || 
             (this.resource_type_description!=null &&
              this.resource_type_description.equals(other.getResource_type_description())));
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
        if (getUuid() != null) {
            _hashCode += getUuid().hashCode();
        }
        if (getResource_type() != null) {
            _hashCode += getResource_type().hashCode();
        }
        if (getResource_type_description() != null) {
            _hashCode += getResource_type_description().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Resource.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Resource"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resource_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resource_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resource_type_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resource_type_description"));
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
