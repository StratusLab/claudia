/**
 * Server.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Server  implements java.io.Serializable {
    private java.lang.Long server_id;

    private java.lang.String server_name;

    private java.lang.String server_uuid;

    private java.lang.String server_key;

    private java.lang.String image_server_hash;

    private java.lang.Integer status;

    private java.lang.Long vdc_id;

    private int processors;

    private java.lang.Integer memory;

    private com.flexiant.extility.ImageTemplate image_template;

    private java.lang.Long disk_capacity;

    private com.flexiant.extility.Disk[] disks;

    private com.flexiant.extility.NetworkInterface[] network_interfaces;

    private java.lang.String initial_user;

    private java.lang.String initial_password;

    private java.lang.Long uptime;

    private java.lang.String[] ip_addresses;

    private java.lang.Integer boot_from_disk_id;

    private long[] server_product_offers;

    public Server() {
    }

    public Server(
           java.lang.Long server_id,
           java.lang.String server_name,
           java.lang.String server_uuid,
           java.lang.String server_key,
           java.lang.String image_server_hash,
           java.lang.Integer status,
           java.lang.Long vdc_id,
           int processors,
           java.lang.Integer memory,
           com.flexiant.extility.ImageTemplate image_template,
           java.lang.Long disk_capacity,
           com.flexiant.extility.Disk[] disks,
           com.flexiant.extility.NetworkInterface[] network_interfaces,
           java.lang.String initial_user,
           java.lang.String initial_password,
           java.lang.Long uptime,
           java.lang.String[] ip_addresses,
           java.lang.Integer boot_from_disk_id,
           long[] server_product_offers) {
           this.server_id = server_id;
           this.server_name = server_name;
           this.server_uuid = server_uuid;
           this.server_key = server_key;
           this.image_server_hash = image_server_hash;
           this.status = status;
           this.vdc_id = vdc_id;
           this.processors = processors;
           this.memory = memory;
           this.image_template = image_template;
           this.disk_capacity = disk_capacity;
           this.disks = disks;
           this.network_interfaces = network_interfaces;
           this.initial_user = initial_user;
           this.initial_password = initial_password;
           this.uptime = uptime;
           this.ip_addresses = ip_addresses;
           this.boot_from_disk_id = boot_from_disk_id;
           this.server_product_offers = server_product_offers;
    }


    /**
     * Gets the server_id value for this Server.
     * 
     * @return server_id
     */
    public java.lang.Long getServer_id() {
        return server_id;
    }


    /**
     * Sets the server_id value for this Server.
     * 
     * @param server_id
     */
    public void setServer_id(java.lang.Long server_id) {
        this.server_id = server_id;
    }


    /**
     * Gets the server_name value for this Server.
     * 
     * @return server_name
     */
    public java.lang.String getServer_name() {
        return server_name;
    }


    /**
     * Sets the server_name value for this Server.
     * 
     * @param server_name
     */
    public void setServer_name(java.lang.String server_name) {
        this.server_name = server_name;
    }


    /**
     * Gets the server_uuid value for this Server.
     * 
     * @return server_uuid
     */
    public java.lang.String getServer_uuid() {
        return server_uuid;
    }


    /**
     * Sets the server_uuid value for this Server.
     * 
     * @param server_uuid
     */
    public void setServer_uuid(java.lang.String server_uuid) {
        this.server_uuid = server_uuid;
    }


    /**
     * Gets the server_key value for this Server.
     * 
     * @return server_key
     */
    public java.lang.String getServer_key() {
        return server_key;
    }


    /**
     * Sets the server_key value for this Server.
     * 
     * @param server_key
     */
    public void setServer_key(java.lang.String server_key) {
        this.server_key = server_key;
    }


    /**
     * Gets the image_server_hash value for this Server.
     * 
     * @return image_server_hash
     */
    public java.lang.String getImage_server_hash() {
        return image_server_hash;
    }


    /**
     * Sets the image_server_hash value for this Server.
     * 
     * @param image_server_hash
     */
    public void setImage_server_hash(java.lang.String image_server_hash) {
        this.image_server_hash = image_server_hash;
    }


    /**
     * Gets the status value for this Server.
     * 
     * @return status
     */
    public java.lang.Integer getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Server.
     * 
     * @param status
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }


    /**
     * Gets the vdc_id value for this Server.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this Server.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the processors value for this Server.
     * 
     * @return processors
     */
    public int getProcessors() {
        return processors;
    }


    /**
     * Sets the processors value for this Server.
     * 
     * @param processors
     */
    public void setProcessors(int processors) {
        this.processors = processors;
    }


    /**
     * Gets the memory value for this Server.
     * 
     * @return memory
     */
    public java.lang.Integer getMemory() {
        return memory;
    }


    /**
     * Sets the memory value for this Server.
     * 
     * @param memory
     */
    public void setMemory(java.lang.Integer memory) {
        this.memory = memory;
    }


    /**
     * Gets the image_template value for this Server.
     * 
     * @return image_template
     */
    public com.flexiant.extility.ImageTemplate getImage_template() {
        return image_template;
    }


    /**
     * Sets the image_template value for this Server.
     * 
     * @param image_template
     */
    public void setImage_template(com.flexiant.extility.ImageTemplate image_template) {
        this.image_template = image_template;
    }


    /**
     * Gets the disk_capacity value for this Server.
     * 
     * @return disk_capacity
     */
    public java.lang.Long getDisk_capacity() {
        return disk_capacity;
    }


    /**
     * Sets the disk_capacity value for this Server.
     * 
     * @param disk_capacity
     */
    public void setDisk_capacity(java.lang.Long disk_capacity) {
        this.disk_capacity = disk_capacity;
    }


    /**
     * Gets the disks value for this Server.
     * 
     * @return disks
     */
    public com.flexiant.extility.Disk[] getDisks() {
        return disks;
    }


    /**
     * Sets the disks value for this Server.
     * 
     * @param disks
     */
    public void setDisks(com.flexiant.extility.Disk[] disks) {
        this.disks = disks;
    }


    /**
     * Gets the network_interfaces value for this Server.
     * 
     * @return network_interfaces
     */
    public com.flexiant.extility.NetworkInterface[] getNetwork_interfaces() {
        return network_interfaces;
    }


    /**
     * Sets the network_interfaces value for this Server.
     * 
     * @param network_interfaces
     */
    public void setNetwork_interfaces(com.flexiant.extility.NetworkInterface[] network_interfaces) {
        this.network_interfaces = network_interfaces;
    }


    /**
     * Gets the initial_user value for this Server.
     * 
     * @return initial_user
     */
    public java.lang.String getInitial_user() {
        return initial_user;
    }


    /**
     * Sets the initial_user value for this Server.
     * 
     * @param initial_user
     */
    public void setInitial_user(java.lang.String initial_user) {
        this.initial_user = initial_user;
    }


    /**
     * Gets the initial_password value for this Server.
     * 
     * @return initial_password
     */
    public java.lang.String getInitial_password() {
        return initial_password;
    }


    /**
     * Sets the initial_password value for this Server.
     * 
     * @param initial_password
     */
    public void setInitial_password(java.lang.String initial_password) {
        this.initial_password = initial_password;
    }


    /**
     * Gets the uptime value for this Server.
     * 
     * @return uptime
     */
    public java.lang.Long getUptime() {
        return uptime;
    }


    /**
     * Sets the uptime value for this Server.
     * 
     * @param uptime
     */
    public void setUptime(java.lang.Long uptime) {
        this.uptime = uptime;
    }


    /**
     * Gets the ip_addresses value for this Server.
     * 
     * @return ip_addresses
     */
    public java.lang.String[] getIp_addresses() {
        return ip_addresses;
    }


    /**
     * Sets the ip_addresses value for this Server.
     * 
     * @param ip_addresses
     */
    public void setIp_addresses(java.lang.String[] ip_addresses) {
        this.ip_addresses = ip_addresses;
    }


    /**
     * Gets the boot_from_disk_id value for this Server.
     * 
     * @return boot_from_disk_id
     */
    public java.lang.Integer getBoot_from_disk_id() {
        return boot_from_disk_id;
    }


    /**
     * Sets the boot_from_disk_id value for this Server.
     * 
     * @param boot_from_disk_id
     */
    public void setBoot_from_disk_id(java.lang.Integer boot_from_disk_id) {
        this.boot_from_disk_id = boot_from_disk_id;
    }


    /**
     * Gets the server_product_offers value for this Server.
     * 
     * @return server_product_offers
     */
    public long[] getServer_product_offers() {
        return server_product_offers;
    }


    /**
     * Sets the server_product_offers value for this Server.
     * 
     * @param server_product_offers
     */
    public void setServer_product_offers(long[] server_product_offers) {
        this.server_product_offers = server_product_offers;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Server)) return false;
        Server other = (Server) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.server_id==null && other.getServer_id()==null) || 
             (this.server_id!=null &&
              this.server_id.equals(other.getServer_id()))) &&
            ((this.server_name==null && other.getServer_name()==null) || 
             (this.server_name!=null &&
              this.server_name.equals(other.getServer_name()))) &&
            ((this.server_uuid==null && other.getServer_uuid()==null) || 
             (this.server_uuid!=null &&
              this.server_uuid.equals(other.getServer_uuid()))) &&
            ((this.server_key==null && other.getServer_key()==null) || 
             (this.server_key!=null &&
              this.server_key.equals(other.getServer_key()))) &&
            ((this.image_server_hash==null && other.getImage_server_hash()==null) || 
             (this.image_server_hash!=null &&
              this.image_server_hash.equals(other.getImage_server_hash()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            this.processors == other.getProcessors() &&
            ((this.memory==null && other.getMemory()==null) || 
             (this.memory!=null &&
              this.memory.equals(other.getMemory()))) &&
            ((this.image_template==null && other.getImage_template()==null) || 
             (this.image_template!=null &&
              this.image_template.equals(other.getImage_template()))) &&
            ((this.disk_capacity==null && other.getDisk_capacity()==null) || 
             (this.disk_capacity!=null &&
              this.disk_capacity.equals(other.getDisk_capacity()))) &&
            ((this.disks==null && other.getDisks()==null) || 
             (this.disks!=null &&
              java.util.Arrays.equals(this.disks, other.getDisks()))) &&
            ((this.network_interfaces==null && other.getNetwork_interfaces()==null) || 
             (this.network_interfaces!=null &&
              java.util.Arrays.equals(this.network_interfaces, other.getNetwork_interfaces()))) &&
            ((this.initial_user==null && other.getInitial_user()==null) || 
             (this.initial_user!=null &&
              this.initial_user.equals(other.getInitial_user()))) &&
            ((this.initial_password==null && other.getInitial_password()==null) || 
             (this.initial_password!=null &&
              this.initial_password.equals(other.getInitial_password()))) &&
            ((this.uptime==null && other.getUptime()==null) || 
             (this.uptime!=null &&
              this.uptime.equals(other.getUptime()))) &&
            ((this.ip_addresses==null && other.getIp_addresses()==null) || 
             (this.ip_addresses!=null &&
              java.util.Arrays.equals(this.ip_addresses, other.getIp_addresses()))) &&
            ((this.boot_from_disk_id==null && other.getBoot_from_disk_id()==null) || 
             (this.boot_from_disk_id!=null &&
              this.boot_from_disk_id.equals(other.getBoot_from_disk_id()))) &&
            ((this.server_product_offers==null && other.getServer_product_offers()==null) || 
             (this.server_product_offers!=null &&
              java.util.Arrays.equals(this.server_product_offers, other.getServer_product_offers())));
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
        if (getServer_id() != null) {
            _hashCode += getServer_id().hashCode();
        }
        if (getServer_name() != null) {
            _hashCode += getServer_name().hashCode();
        }
        if (getServer_uuid() != null) {
            _hashCode += getServer_uuid().hashCode();
        }
        if (getServer_key() != null) {
            _hashCode += getServer_key().hashCode();
        }
        if (getImage_server_hash() != null) {
            _hashCode += getImage_server_hash().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        _hashCode += getProcessors();
        if (getMemory() != null) {
            _hashCode += getMemory().hashCode();
        }
        if (getImage_template() != null) {
            _hashCode += getImage_template().hashCode();
        }
        if (getDisk_capacity() != null) {
            _hashCode += getDisk_capacity().hashCode();
        }
        if (getDisks() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDisks());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDisks(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNetwork_interfaces() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNetwork_interfaces());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNetwork_interfaces(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInitial_user() != null) {
            _hashCode += getInitial_user().hashCode();
        }
        if (getInitial_password() != null) {
            _hashCode += getInitial_password().hashCode();
        }
        if (getUptime() != null) {
            _hashCode += getUptime().hashCode();
        }
        if (getIp_addresses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIp_addresses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIp_addresses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBoot_from_disk_id() != null) {
            _hashCode += getBoot_from_disk_id().hashCode();
        }
        if (getServer_product_offers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getServer_product_offers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getServer_product_offers(), i);
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
        new org.apache.axis.description.TypeDesc(Server.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Server"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_server_hash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_server_hash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("processors");
        elemField.setXmlName(new javax.xml.namespace.QName("", "processors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("memory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "memory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ImageTemplate"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disk_capacity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disk_capacity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Disk"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("network_interfaces");
        elemField.setXmlName(new javax.xml.namespace.QName("", "network_interfaces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "NetworkInterface"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initial_user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "initial_user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initial_password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "initial_password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uptime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uptime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip_addresses");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip_addresses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boot_from_disk_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "boot_from_disk_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server_product_offers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "server_product_offers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
