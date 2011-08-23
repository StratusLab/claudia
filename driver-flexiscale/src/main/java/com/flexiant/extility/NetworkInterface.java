/**
 * NetworkInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class NetworkInterface  implements java.io.Serializable {
    private java.lang.Long nic_id;

    private java.lang.String nic_uuid;

    private java.lang.Long server_id;

    private java.lang.Long vlan_id;

    private java.lang.String vlan_name;

    private java.lang.String mac_address;

    private java.lang.String[] ip_list;

    private com.flexiant.extility.IpAddress[] ip_details;

    public NetworkInterface() {
    }

    public NetworkInterface(
           java.lang.Long nic_id,
           java.lang.String nic_uuid,
           java.lang.Long server_id,
           java.lang.Long vlan_id,
           java.lang.String vlan_name,
           java.lang.String mac_address,
           java.lang.String[] ip_list,
           com.flexiant.extility.IpAddress[] ip_details) {
           this.nic_id = nic_id;
           this.nic_uuid = nic_uuid;
           this.server_id = server_id;
           this.vlan_id = vlan_id;
           this.vlan_name = vlan_name;
           this.mac_address = mac_address;
           this.ip_list = ip_list;
           this.ip_details = ip_details;
    }


    /**
     * Gets the nic_id value for this NetworkInterface.
     * 
     * @return nic_id
     */
    public java.lang.Long getNic_id() {
        return nic_id;
    }


    /**
     * Sets the nic_id value for this NetworkInterface.
     * 
     * @param nic_id
     */
    public void setNic_id(java.lang.Long nic_id) {
        this.nic_id = nic_id;
    }


    /**
     * Gets the nic_uuid value for this NetworkInterface.
     * 
     * @return nic_uuid
     */
    public java.lang.String getNic_uuid() {
        return nic_uuid;
    }


    /**
     * Sets the nic_uuid value for this NetworkInterface.
     * 
     * @param nic_uuid
     */
    public void setNic_uuid(java.lang.String nic_uuid) {
        this.nic_uuid = nic_uuid;
    }


    /**
     * Gets the server_id value for this NetworkInterface.
     * 
     * @return server_id
     */
    public java.lang.Long getServer_id() {
        return server_id;
    }


    /**
     * Sets the server_id value for this NetworkInterface.
     * 
     * @param server_id
     */
    public void setServer_id(java.lang.Long server_id) {
        this.server_id = server_id;
    }


    /**
     * Gets the vlan_id value for this NetworkInterface.
     * 
     * @return vlan_id
     */
    public java.lang.Long getVlan_id() {
        return vlan_id;
    }


    /**
     * Sets the vlan_id value for this NetworkInterface.
     * 
     * @param vlan_id
     */
    public void setVlan_id(java.lang.Long vlan_id) {
        this.vlan_id = vlan_id;
    }


    /**
     * Gets the vlan_name value for this NetworkInterface.
     * 
     * @return vlan_name
     */
    public java.lang.String getVlan_name() {
        return vlan_name;
    }


    /**
     * Sets the vlan_name value for this NetworkInterface.
     * 
     * @param vlan_name
     */
    public void setVlan_name(java.lang.String vlan_name) {
        this.vlan_name = vlan_name;
    }


    /**
     * Gets the mac_address value for this NetworkInterface.
     * 
     * @return mac_address
     */
    public java.lang.String getMac_address() {
        return mac_address;
    }


    /**
     * Sets the mac_address value for this NetworkInterface.
     * 
     * @param mac_address
     */
    public void setMac_address(java.lang.String mac_address) {
        this.mac_address = mac_address;
    }


    /**
     * Gets the ip_list value for this NetworkInterface.
     * 
     * @return ip_list
     */
    public java.lang.String[] getIp_list() {
        return ip_list;
    }


    /**
     * Sets the ip_list value for this NetworkInterface.
     * 
     * @param ip_list
     */
    public void setIp_list(java.lang.String[] ip_list) {
        this.ip_list = ip_list;
    }


    /**
     * Gets the ip_details value for this NetworkInterface.
     * 
     * @return ip_details
     */
    public com.flexiant.extility.IpAddress[] getIp_details() {
        return ip_details;
    }


    /**
     * Sets the ip_details value for this NetworkInterface.
     * 
     * @param ip_details
     */
    public void setIp_details(com.flexiant.extility.IpAddress[] ip_details) {
        this.ip_details = ip_details;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NetworkInterface)) return false;
        NetworkInterface other = (NetworkInterface) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nic_id==null && other.getNic_id()==null) || 
             (this.nic_id!=null &&
              this.nic_id.equals(other.getNic_id()))) &&
            ((this.nic_uuid==null && other.getNic_uuid()==null) || 
             (this.nic_uuid!=null &&
              this.nic_uuid.equals(other.getNic_uuid()))) &&
            ((this.server_id==null && other.getServer_id()==null) || 
             (this.server_id!=null &&
              this.server_id.equals(other.getServer_id()))) &&
            ((this.vlan_id==null && other.getVlan_id()==null) || 
             (this.vlan_id!=null &&
              this.vlan_id.equals(other.getVlan_id()))) &&
            ((this.vlan_name==null && other.getVlan_name()==null) || 
             (this.vlan_name!=null &&
              this.vlan_name.equals(other.getVlan_name()))) &&
            ((this.mac_address==null && other.getMac_address()==null) || 
             (this.mac_address!=null &&
              this.mac_address.equals(other.getMac_address()))) &&
            ((this.ip_list==null && other.getIp_list()==null) || 
             (this.ip_list!=null &&
              java.util.Arrays.equals(this.ip_list, other.getIp_list()))) &&
            ((this.ip_details==null && other.getIp_details()==null) || 
             (this.ip_details!=null &&
              java.util.Arrays.equals(this.ip_details, other.getIp_details())));
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
        if (getNic_id() != null) {
            _hashCode += getNic_id().hashCode();
        }
        if (getNic_uuid() != null) {
            _hashCode += getNic_uuid().hashCode();
        }
        if (getServer_id() != null) {
            _hashCode += getServer_id().hashCode();
        }
        if (getVlan_id() != null) {
            _hashCode += getVlan_id().hashCode();
        }
        if (getVlan_name() != null) {
            _hashCode += getVlan_name().hashCode();
        }
        if (getMac_address() != null) {
            _hashCode += getMac_address().hashCode();
        }
        if (getIp_list() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIp_list());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIp_list(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIp_details() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIp_details());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIp_details(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NetworkInterface.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "NetworkInterface"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nic_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nic_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nic_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nic_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("vlan_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vlan_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mac_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mac_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip_list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip_list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip_details");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip_details"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "IpAddress"));
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
