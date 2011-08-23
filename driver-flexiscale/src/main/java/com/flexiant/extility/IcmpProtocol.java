/**
 * IcmpProtocol.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class IcmpProtocol  implements java.io.Serializable {
    private java.lang.Integer icmp_protocol_id;

    private java.lang.String description;

    public IcmpProtocol() {
    }

    public IcmpProtocol(
           java.lang.Integer icmp_protocol_id,
           java.lang.String description) {
           this.icmp_protocol_id = icmp_protocol_id;
           this.description = description;
    }


    /**
     * Gets the icmp_protocol_id value for this IcmpProtocol.
     * 
     * @return icmp_protocol_id
     */
    public java.lang.Integer getIcmp_protocol_id() {
        return icmp_protocol_id;
    }


    /**
     * Sets the icmp_protocol_id value for this IcmpProtocol.
     * 
     * @param icmp_protocol_id
     */
    public void setIcmp_protocol_id(java.lang.Integer icmp_protocol_id) {
        this.icmp_protocol_id = icmp_protocol_id;
    }


    /**
     * Gets the description value for this IcmpProtocol.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this IcmpProtocol.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IcmpProtocol)) return false;
        IcmpProtocol other = (IcmpProtocol) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.icmp_protocol_id==null && other.getIcmp_protocol_id()==null) || 
             (this.icmp_protocol_id!=null &&
              this.icmp_protocol_id.equals(other.getIcmp_protocol_id()))) &&
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
        if (getIcmp_protocol_id() != null) {
            _hashCode += getIcmp_protocol_id().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IcmpProtocol.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "IcmpProtocol"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icmp_protocol_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "icmp_protocol_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
