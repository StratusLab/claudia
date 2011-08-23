/**
 * Subnet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Subnet  implements java.io.Serializable {
    private java.lang.Long subnet_id;

    private java.lang.String[] available_ip_list;

    private java.lang.String network_address;

    private java.lang.String gateway_address;

    private java.lang.String broadcast_address;

    private java.lang.Integer mask;

    private java.lang.Long vlan_id;

    private java.lang.Long vdc_id;

    private java.lang.String subnet_uuid;

    private java.lang.Integer version;

    private java.lang.Boolean enabled;

    public Subnet() {
    }

    public Subnet(
           java.lang.Long subnet_id,
           java.lang.String[] available_ip_list,
           java.lang.String network_address,
           java.lang.String gateway_address,
           java.lang.String broadcast_address,
           java.lang.Integer mask,
           java.lang.Long vlan_id,
           java.lang.Long vdc_id,
           java.lang.String subnet_uuid,
           java.lang.Integer version,
           java.lang.Boolean enabled) {
           this.subnet_id = subnet_id;
           this.available_ip_list = available_ip_list;
           this.network_address = network_address;
           this.gateway_address = gateway_address;
           this.broadcast_address = broadcast_address;
           this.mask = mask;
           this.vlan_id = vlan_id;
           this.vdc_id = vdc_id;
           this.subnet_uuid = subnet_uuid;
           this.version = version;
           this.enabled = enabled;
    }


    /**
     * Gets the subnet_id value for this Subnet.
     * 
     * @return subnet_id
     */
    public java.lang.Long getSubnet_id() {
        return subnet_id;
    }


    /**
     * Sets the subnet_id value for this Subnet.
     * 
     * @param subnet_id
     */
    public void setSubnet_id(java.lang.Long subnet_id) {
        this.subnet_id = subnet_id;
    }


    /**
     * Gets the available_ip_list value for this Subnet.
     * 
     * @return available_ip_list
     */
    public java.lang.String[] getAvailable_ip_list() {
        return available_ip_list;
    }


    /**
     * Sets the available_ip_list value for this Subnet.
     * 
     * @param available_ip_list
     */
    public void setAvailable_ip_list(java.lang.String[] available_ip_list) {
        this.available_ip_list = available_ip_list;
    }


    /**
     * Gets the network_address value for this Subnet.
     * 
     * @return network_address
     */
    public java.lang.String getNetwork_address() {
        return network_address;
    }


    /**
     * Sets the network_address value for this Subnet.
     * 
     * @param network_address
     */
    public void setNetwork_address(java.lang.String network_address) {
        this.network_address = network_address;
    }


    /**
     * Gets the gateway_address value for this Subnet.
     * 
     * @return gateway_address
     */
    public java.lang.String getGateway_address() {
        return gateway_address;
    }


    /**
     * Sets the gateway_address value for this Subnet.
     * 
     * @param gateway_address
     */
    public void setGateway_address(java.lang.String gateway_address) {
        this.gateway_address = gateway_address;
    }


    /**
     * Gets the broadcast_address value for this Subnet.
     * 
     * @return broadcast_address
     */
    public java.lang.String getBroadcast_address() {
        return broadcast_address;
    }


    /**
     * Sets the broadcast_address value for this Subnet.
     * 
     * @param broadcast_address
     */
    public void setBroadcast_address(java.lang.String broadcast_address) {
        this.broadcast_address = broadcast_address;
    }


    /**
     * Gets the mask value for this Subnet.
     * 
     * @return mask
     */
    public java.lang.Integer getMask() {
        return mask;
    }


    /**
     * Sets the mask value for this Subnet.
     * 
     * @param mask
     */
    public void setMask(java.lang.Integer mask) {
        this.mask = mask;
    }


    /**
     * Gets the vlan_id value for this Subnet.
     * 
     * @return vlan_id
     */
    public java.lang.Long getVlan_id() {
        return vlan_id;
    }


    /**
     * Sets the vlan_id value for this Subnet.
     * 
     * @param vlan_id
     */
    public void setVlan_id(java.lang.Long vlan_id) {
        this.vlan_id = vlan_id;
    }


    /**
     * Gets the vdc_id value for this Subnet.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this Subnet.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the subnet_uuid value for this Subnet.
     * 
     * @return subnet_uuid
     */
    public java.lang.String getSubnet_uuid() {
        return subnet_uuid;
    }


    /**
     * Sets the subnet_uuid value for this Subnet.
     * 
     * @param subnet_uuid
     */
    public void setSubnet_uuid(java.lang.String subnet_uuid) {
        this.subnet_uuid = subnet_uuid;
    }


    /**
     * Gets the version value for this Subnet.
     * 
     * @return version
     */
    public java.lang.Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this Subnet.
     * 
     * @param version
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }


    /**
     * Gets the enabled value for this Subnet.
     * 
     * @return enabled
     */
    public java.lang.Boolean getEnabled() {
        return enabled;
    }


    /**
     * Sets the enabled value for this Subnet.
     * 
     * @param enabled
     */
    public void setEnabled(java.lang.Boolean enabled) {
        this.enabled = enabled;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Subnet)) return false;
        Subnet other = (Subnet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subnet_id==null && other.getSubnet_id()==null) || 
             (this.subnet_id!=null &&
              this.subnet_id.equals(other.getSubnet_id()))) &&
            ((this.available_ip_list==null && other.getAvailable_ip_list()==null) || 
             (this.available_ip_list!=null &&
              java.util.Arrays.equals(this.available_ip_list, other.getAvailable_ip_list()))) &&
            ((this.network_address==null && other.getNetwork_address()==null) || 
             (this.network_address!=null &&
              this.network_address.equals(other.getNetwork_address()))) &&
            ((this.gateway_address==null && other.getGateway_address()==null) || 
             (this.gateway_address!=null &&
              this.gateway_address.equals(other.getGateway_address()))) &&
            ((this.broadcast_address==null && other.getBroadcast_address()==null) || 
             (this.broadcast_address!=null &&
              this.broadcast_address.equals(other.getBroadcast_address()))) &&
            ((this.mask==null && other.getMask()==null) || 
             (this.mask!=null &&
              this.mask.equals(other.getMask()))) &&
            ((this.vlan_id==null && other.getVlan_id()==null) || 
             (this.vlan_id!=null &&
              this.vlan_id.equals(other.getVlan_id()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this.subnet_uuid==null && other.getSubnet_uuid()==null) || 
             (this.subnet_uuid!=null &&
              this.subnet_uuid.equals(other.getSubnet_uuid()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.enabled==null && other.getEnabled()==null) || 
             (this.enabled!=null &&
              this.enabled.equals(other.getEnabled())));
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
        if (getSubnet_id() != null) {
            _hashCode += getSubnet_id().hashCode();
        }
        if (getAvailable_ip_list() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAvailable_ip_list());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAvailable_ip_list(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNetwork_address() != null) {
            _hashCode += getNetwork_address().hashCode();
        }
        if (getGateway_address() != null) {
            _hashCode += getGateway_address().hashCode();
        }
        if (getBroadcast_address() != null) {
            _hashCode += getBroadcast_address().hashCode();
        }
        if (getMask() != null) {
            _hashCode += getMask().hashCode();
        }
        if (getVlan_id() != null) {
            _hashCode += getVlan_id().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (getSubnet_uuid() != null) {
            _hashCode += getSubnet_uuid().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getEnabled() != null) {
            _hashCode += getEnabled().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Subnet.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Subnet"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subnet_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subnet_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("available_ip_list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "available_ip_list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("network_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "network_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gateway_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gateway_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("broadcast_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "broadcast_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mask");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vlan_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vlan_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("subnet_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subnet_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enabled");
        elemField.setXmlName(new javax.xml.namespace.QName("", "enabled"));
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
