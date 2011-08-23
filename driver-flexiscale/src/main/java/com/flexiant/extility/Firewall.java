/**
 * Firewall.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Firewall  implements java.io.Serializable {
    private java.lang.Long firewall_id;

    private java.lang.String firewall_uuid;

    private java.lang.String ip_address;

    private java.lang.String default_inbound_policy;

    private java.lang.String default_outbound_policy;

    private java.lang.Long vdc_id;

    private java.lang.String address_type;

    public Firewall() {
    }

    public Firewall(
           java.lang.Long firewall_id,
           java.lang.String firewall_uuid,
           java.lang.String ip_address,
           java.lang.String default_inbound_policy,
           java.lang.String default_outbound_policy,
           java.lang.Long vdc_id,
           java.lang.String address_type) {
           this.firewall_id = firewall_id;
           this.firewall_uuid = firewall_uuid;
           this.ip_address = ip_address;
           this.default_inbound_policy = default_inbound_policy;
           this.default_outbound_policy = default_outbound_policy;
           this.vdc_id = vdc_id;
           this.address_type = address_type;
    }


    /**
     * Gets the firewall_id value for this Firewall.
     * 
     * @return firewall_id
     */
    public java.lang.Long getFirewall_id() {
        return firewall_id;
    }


    /**
     * Sets the firewall_id value for this Firewall.
     * 
     * @param firewall_id
     */
    public void setFirewall_id(java.lang.Long firewall_id) {
        this.firewall_id = firewall_id;
    }


    /**
     * Gets the firewall_uuid value for this Firewall.
     * 
     * @return firewall_uuid
     */
    public java.lang.String getFirewall_uuid() {
        return firewall_uuid;
    }


    /**
     * Sets the firewall_uuid value for this Firewall.
     * 
     * @param firewall_uuid
     */
    public void setFirewall_uuid(java.lang.String firewall_uuid) {
        this.firewall_uuid = firewall_uuid;
    }


    /**
     * Gets the ip_address value for this Firewall.
     * 
     * @return ip_address
     */
    public java.lang.String getIp_address() {
        return ip_address;
    }


    /**
     * Sets the ip_address value for this Firewall.
     * 
     * @param ip_address
     */
    public void setIp_address(java.lang.String ip_address) {
        this.ip_address = ip_address;
    }


    /**
     * Gets the default_inbound_policy value for this Firewall.
     * 
     * @return default_inbound_policy
     */
    public java.lang.String getDefault_inbound_policy() {
        return default_inbound_policy;
    }


    /**
     * Sets the default_inbound_policy value for this Firewall.
     * 
     * @param default_inbound_policy
     */
    public void setDefault_inbound_policy(java.lang.String default_inbound_policy) {
        this.default_inbound_policy = default_inbound_policy;
    }


    /**
     * Gets the default_outbound_policy value for this Firewall.
     * 
     * @return default_outbound_policy
     */
    public java.lang.String getDefault_outbound_policy() {
        return default_outbound_policy;
    }


    /**
     * Sets the default_outbound_policy value for this Firewall.
     * 
     * @param default_outbound_policy
     */
    public void setDefault_outbound_policy(java.lang.String default_outbound_policy) {
        this.default_outbound_policy = default_outbound_policy;
    }


    /**
     * Gets the vdc_id value for this Firewall.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this Firewall.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the address_type value for this Firewall.
     * 
     * @return address_type
     */
    public java.lang.String getAddress_type() {
        return address_type;
    }


    /**
     * Sets the address_type value for this Firewall.
     * 
     * @param address_type
     */
    public void setAddress_type(java.lang.String address_type) {
        this.address_type = address_type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Firewall)) return false;
        Firewall other = (Firewall) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.firewall_id==null && other.getFirewall_id()==null) || 
             (this.firewall_id!=null &&
              this.firewall_id.equals(other.getFirewall_id()))) &&
            ((this.firewall_uuid==null && other.getFirewall_uuid()==null) || 
             (this.firewall_uuid!=null &&
              this.firewall_uuid.equals(other.getFirewall_uuid()))) &&
            ((this.ip_address==null && other.getIp_address()==null) || 
             (this.ip_address!=null &&
              this.ip_address.equals(other.getIp_address()))) &&
            ((this.default_inbound_policy==null && other.getDefault_inbound_policy()==null) || 
             (this.default_inbound_policy!=null &&
              this.default_inbound_policy.equals(other.getDefault_inbound_policy()))) &&
            ((this.default_outbound_policy==null && other.getDefault_outbound_policy()==null) || 
             (this.default_outbound_policy!=null &&
              this.default_outbound_policy.equals(other.getDefault_outbound_policy()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this.address_type==null && other.getAddress_type()==null) || 
             (this.address_type!=null &&
              this.address_type.equals(other.getAddress_type())));
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
        if (getFirewall_id() != null) {
            _hashCode += getFirewall_id().hashCode();
        }
        if (getFirewall_uuid() != null) {
            _hashCode += getFirewall_uuid().hashCode();
        }
        if (getIp_address() != null) {
            _hashCode += getIp_address().hashCode();
        }
        if (getDefault_inbound_policy() != null) {
            _hashCode += getDefault_inbound_policy().hashCode();
        }
        if (getDefault_outbound_policy() != null) {
            _hashCode += getDefault_outbound_policy().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (getAddress_type() != null) {
            _hashCode += getAddress_type().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Firewall.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Firewall"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("default_inbound_policy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "default_inbound_policy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("default_outbound_policy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "default_outbound_policy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("address_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "address_type"));
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
