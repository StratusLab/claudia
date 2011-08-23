/**
 * FirewallProtocol.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class FirewallProtocol  implements java.io.Serializable {
    private int firewall_protocol_id;

    private java.lang.String name;

    public FirewallProtocol() {
    }

    public FirewallProtocol(
           int firewall_protocol_id,
           java.lang.String name) {
           this.firewall_protocol_id = firewall_protocol_id;
           this.name = name;
    }


    /**
     * Gets the firewall_protocol_id value for this FirewallProtocol.
     * 
     * @return firewall_protocol_id
     */
    public int getFirewall_protocol_id() {
        return firewall_protocol_id;
    }


    /**
     * Sets the firewall_protocol_id value for this FirewallProtocol.
     * 
     * @param firewall_protocol_id
     */
    public void setFirewall_protocol_id(int firewall_protocol_id) {
        this.firewall_protocol_id = firewall_protocol_id;
    }


    /**
     * Gets the name value for this FirewallProtocol.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FirewallProtocol.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FirewallProtocol)) return false;
        FirewallProtocol other = (FirewallProtocol) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.firewall_protocol_id == other.getFirewall_protocol_id() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        _hashCode += getFirewall_protocol_id();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FirewallProtocol.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "FirewallProtocol"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_protocol_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_protocol_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
