/**
 * Customer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Customer  implements java.io.Serializable {
    private java.lang.String customer_uuid;

    private java.lang.String user_uuid;

    public Customer() {
    }

    public Customer(
           java.lang.String customer_uuid,
           java.lang.String user_uuid) {
           this.customer_uuid = customer_uuid;
           this.user_uuid = user_uuid;
    }


    /**
     * Gets the customer_uuid value for this Customer.
     * 
     * @return customer_uuid
     */
    public java.lang.String getCustomer_uuid() {
        return customer_uuid;
       
        
    }


    /**
     * Sets the customer_uuid value for this Customer.
     * 
     * @param customer_uuid
     */
    public void setCustomer_uuid(java.lang.String customer_uuid) {
        this.customer_uuid = customer_uuid;
    }


    /**
     * Gets the user_uuid value for this Customer.
     * 
     * @return user_uuid
     */
    public java.lang.String getUser_uuid() {
        return user_uuid;
    }


    /**
     * Sets the user_uuid value for this Customer.
     * 
     * @param user_uuid
     */
    public void setUser_uuid(java.lang.String user_uuid) {
        this.user_uuid = user_uuid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.customer_uuid==null && other.getCustomer_uuid()==null) || 
             (this.customer_uuid!=null &&
              this.customer_uuid.equals(other.getCustomer_uuid()))) &&
            ((this.user_uuid==null && other.getUser_uuid()==null) || 
             (this.user_uuid!=null &&
              this.user_uuid.equals(other.getUser_uuid())));
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
        if (getCustomer_uuid() != null) {
            _hashCode += getCustomer_uuid().hashCode();
        }
        if (getUser_uuid() != null) {
            _hashCode += getUser_uuid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Customer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Customer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customer_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customer_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_uuid"));
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
