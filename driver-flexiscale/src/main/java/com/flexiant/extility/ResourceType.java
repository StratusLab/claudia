/**
 * ResourceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class ResourceType  implements java.io.Serializable {
    private java.lang.Integer type_id;

    private java.lang.String type_description;

    public ResourceType() {
    }

    public ResourceType(
           java.lang.Integer type_id,
           java.lang.String type_description) {
           this.type_id = type_id;
           this.type_description = type_description;
    }


    /**
     * Gets the type_id value for this ResourceType.
     * 
     * @return type_id
     */
    public java.lang.Integer getType_id() {
        return type_id;
    }


    /**
     * Sets the type_id value for this ResourceType.
     * 
     * @param type_id
     */
    public void setType_id(java.lang.Integer type_id) {
        this.type_id = type_id;
    }


    /**
     * Gets the type_description value for this ResourceType.
     * 
     * @return type_description
     */
    public java.lang.String getType_description() {
        return type_description;
    }


    /**
     * Sets the type_description value for this ResourceType.
     * 
     * @param type_description
     */
    public void setType_description(java.lang.String type_description) {
        this.type_description = type_description;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResourceType)) return false;
        ResourceType other = (ResourceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.type_id==null && other.getType_id()==null) || 
             (this.type_id!=null &&
              this.type_id.equals(other.getType_id()))) &&
            ((this.type_description==null && other.getType_description()==null) || 
             (this.type_description!=null &&
              this.type_description.equals(other.getType_description())));
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
        if (getType_id() != null) {
            _hashCode += getType_id().hashCode();
        }
        if (getType_description() != null) {
            _hashCode += getType_description().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResourceType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ResourceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type_description"));
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
