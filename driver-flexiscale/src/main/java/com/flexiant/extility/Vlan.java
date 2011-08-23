/**
 * Vlan.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Vlan  implements java.io.Serializable {
    private java.lang.Long vlan_id;

    private java.lang.String vlan_name;

    private java.lang.String vlan_uuid;

    private java.lang.Long vdc_id;

    private java.lang.Boolean _private;

    private com.flexiant.extility.Subnet[] subnets;

    public Vlan() {
    }

    public Vlan(
           java.lang.Long vlan_id,
           java.lang.String vlan_name,
           java.lang.String vlan_uuid,
           java.lang.Long vdc_id,
           java.lang.Boolean _private,
           com.flexiant.extility.Subnet[] subnets) {
           this.vlan_id = vlan_id;
           this.vlan_name = vlan_name;
           this.vlan_uuid = vlan_uuid;
           this.vdc_id = vdc_id;
           this._private = _private;
           this.subnets = subnets;
    }


    /**
     * Gets the vlan_id value for this Vlan.
     * 
     * @return vlan_id
     */
    public java.lang.Long getVlan_id() {
        return vlan_id;
    }


    /**
     * Sets the vlan_id value for this Vlan.
     * 
     * @param vlan_id
     */
    public void setVlan_id(java.lang.Long vlan_id) {
        this.vlan_id = vlan_id;
    }


    /**
     * Gets the vlan_name value for this Vlan.
     * 
     * @return vlan_name
     */
    public java.lang.String getVlan_name() {
        return vlan_name;
    }


    /**
     * Sets the vlan_name value for this Vlan.
     * 
     * @param vlan_name
     */
    public void setVlan_name(java.lang.String vlan_name) {
        this.vlan_name = vlan_name;
    }


    /**
     * Gets the vlan_uuid value for this Vlan.
     * 
     * @return vlan_uuid
     */
    public java.lang.String getVlan_uuid() {
        return vlan_uuid;
    }


    /**
     * Sets the vlan_uuid value for this Vlan.
     * 
     * @param vlan_uuid
     */
    public void setVlan_uuid(java.lang.String vlan_uuid) {
        this.vlan_uuid = vlan_uuid;
    }


    /**
     * Gets the vdc_id value for this Vlan.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this Vlan.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the _private value for this Vlan.
     * 
     * @return _private
     */
    public java.lang.Boolean get_private() {
        return _private;
    }


    /**
     * Sets the _private value for this Vlan.
     * 
     * @param _private
     */
    public void set_private(java.lang.Boolean _private) {
        this._private = _private;
    }


    /**
     * Gets the subnets value for this Vlan.
     * 
     * @return subnets
     */
    public com.flexiant.extility.Subnet[] getSubnets() {
        return subnets;
    }


    /**
     * Sets the subnets value for this Vlan.
     * 
     * @param subnets
     */
    public void setSubnets(com.flexiant.extility.Subnet[] subnets) {
        this.subnets = subnets;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Vlan)) return false;
        Vlan other = (Vlan) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vlan_id==null && other.getVlan_id()==null) || 
             (this.vlan_id!=null &&
              this.vlan_id.equals(other.getVlan_id()))) &&
            ((this.vlan_name==null && other.getVlan_name()==null) || 
             (this.vlan_name!=null &&
              this.vlan_name.equals(other.getVlan_name()))) &&
            ((this.vlan_uuid==null && other.getVlan_uuid()==null) || 
             (this.vlan_uuid!=null &&
              this.vlan_uuid.equals(other.getVlan_uuid()))) &&
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this._private==null && other.get_private()==null) || 
             (this._private!=null &&
              this._private.equals(other.get_private()))) &&
            ((this.subnets==null && other.getSubnets()==null) || 
             (this.subnets!=null &&
              java.util.Arrays.equals(this.subnets, other.getSubnets())));
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
        if (getVlan_id() != null) {
            _hashCode += getVlan_id().hashCode();
        }
        if (getVlan_name() != null) {
            _hashCode += getVlan_name().hashCode();
        }
        if (getVlan_uuid() != null) {
            _hashCode += getVlan_uuid().hashCode();
        }
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (get_private() != null) {
            _hashCode += get_private().hashCode();
        }
        if (getSubnets() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubnets());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubnets(), i);
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
        new org.apache.axis.description.TypeDesc(Vlan.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Vlan"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("vlan_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vlan_uuid"));
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
        elemField.setFieldName("_private");
        elemField.setXmlName(new javax.xml.namespace.QName("", "private"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subnets");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subnets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Subnet"));
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
