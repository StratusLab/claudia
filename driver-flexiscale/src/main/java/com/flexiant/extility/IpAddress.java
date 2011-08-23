/**
 * IpAddress.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class IpAddress  implements java.io.Serializable {
    private java.lang.String ip_address;

    private java.lang.Long nic_id;

    private java.lang.Long subnet_id;

    private java.lang.Integer dhcp;

    private java.lang.Integer version;

    public IpAddress() {
    }

    public IpAddress(
           java.lang.String ip_address,
           java.lang.Long nic_id,
           java.lang.Long subnet_id,
           java.lang.Integer dhcp,
           java.lang.Integer version) {
           this.ip_address = ip_address;
           this.nic_id = nic_id;
           this.subnet_id = subnet_id;
           this.dhcp = dhcp;
           this.version = version;
    }


    /**
     * Gets the ip_address value for this IpAddress.
     * 
     * @return ip_address
     */
    public java.lang.String getIp_address() {
        return ip_address;
    }


    /**
     * Sets the ip_address value for this IpAddress.
     * 
     * @param ip_address
     */
    public void setIp_address(java.lang.String ip_address) {
        this.ip_address = ip_address;
    }


    /**
     * Gets the nic_id value for this IpAddress.
     * 
     * @return nic_id
     */
    public java.lang.Long getNic_id() {
        return nic_id;
    }


    /**
     * Sets the nic_id value for this IpAddress.
     * 
     * @param nic_id
     */
    public void setNic_id(java.lang.Long nic_id) {
        this.nic_id = nic_id;
    }


    /**
     * Gets the subnet_id value for this IpAddress.
     * 
     * @return subnet_id
     */
    public java.lang.Long getSubnet_id() {
        return subnet_id;
    }


    /**
     * Sets the subnet_id value for this IpAddress.
     * 
     * @param subnet_id
     */
    public void setSubnet_id(java.lang.Long subnet_id) {
        this.subnet_id = subnet_id;
    }


    /**
     * Gets the dhcp value for this IpAddress.
     * 
     * @return dhcp
     */
    public java.lang.Integer getDhcp() {
        return dhcp;
    }


    /**
     * Sets the dhcp value for this IpAddress.
     * 
     * @param dhcp
     */
    public void setDhcp(java.lang.Integer dhcp) {
        this.dhcp = dhcp;
    }


    /**
     * Gets the version value for this IpAddress.
     * 
     * @return version
     */
    public java.lang.Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this IpAddress.
     * 
     * @param version
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IpAddress)) return false;
        IpAddress other = (IpAddress) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ip_address==null && other.getIp_address()==null) || 
             (this.ip_address!=null &&
              this.ip_address.equals(other.getIp_address()))) &&
            ((this.nic_id==null && other.getNic_id()==null) || 
             (this.nic_id!=null &&
              this.nic_id.equals(other.getNic_id()))) &&
            ((this.subnet_id==null && other.getSubnet_id()==null) || 
             (this.subnet_id!=null &&
              this.subnet_id.equals(other.getSubnet_id()))) &&
            ((this.dhcp==null && other.getDhcp()==null) || 
             (this.dhcp!=null &&
              this.dhcp.equals(other.getDhcp()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getIp_address() != null) {
            _hashCode += getIp_address().hashCode();
        }
        if (getNic_id() != null) {
            _hashCode += getNic_id().hashCode();
        }
        if (getSubnet_id() != null) {
            _hashCode += getSubnet_id().hashCode();
        }
        if (getDhcp() != null) {
            _hashCode += getDhcp().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IpAddress.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "IpAddress"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nic_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nic_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subnet_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subnet_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dhcp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dhcp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
