/**
 * VDC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class VDC  implements java.io.Serializable {
    private java.lang.Long vdc_id;

    private java.lang.String vdc_name;

    private java.lang.String vdc_uuid;

    private java.lang.Integer number_of_servers;

    private java.lang.Integer number_of_disks;

    private java.lang.Integer number_of_disksnapshots;

    private java.lang.Integer number_of_images;

    private java.lang.Integer number_of_vlans;

    public VDC() {
    }

    public VDC(
           java.lang.Long vdc_id,
           java.lang.String vdc_name,
           java.lang.String vdc_uuid,
           java.lang.Integer number_of_servers,
           java.lang.Integer number_of_disks,
           java.lang.Integer number_of_disksnapshots,
           java.lang.Integer number_of_images,
           java.lang.Integer number_of_vlans) {
           this.vdc_id = vdc_id;
           this.vdc_name = vdc_name;
           this.vdc_uuid = vdc_uuid;
           this.number_of_servers = number_of_servers;
           this.number_of_disks = number_of_disks;
           this.number_of_disksnapshots = number_of_disksnapshots;
           this.number_of_images = number_of_images;
           this.number_of_vlans = number_of_vlans;
    }


    /**
     * Gets the vdc_id value for this VDC.
     * 
     * @return vdc_id
     */
    public java.lang.Long getVdc_id() {
        return vdc_id;
    }


    /**
     * Sets the vdc_id value for this VDC.
     * 
     * @param vdc_id
     */
    public void setVdc_id(java.lang.Long vdc_id) {
        this.vdc_id = vdc_id;
    }


    /**
     * Gets the vdc_name value for this VDC.
     * 
     * @return vdc_name
     */
    public java.lang.String getVdc_name() {
        return vdc_name;
    }


    /**
     * Sets the vdc_name value for this VDC.
     * 
     * @param vdc_name
     */
    public void setVdc_name(java.lang.String vdc_name) {
        this.vdc_name = vdc_name;
    }


    /**
     * Gets the vdc_uuid value for this VDC.
     * 
     * @return vdc_uuid
     */
    public java.lang.String getVdc_uuid() {
        return vdc_uuid;
    }


    /**
     * Sets the vdc_uuid value for this VDC.
     * 
     * @param vdc_uuid
     */
    public void setVdc_uuid(java.lang.String vdc_uuid) {
        this.vdc_uuid = vdc_uuid;
    }


    /**
     * Gets the number_of_servers value for this VDC.
     * 
     * @return number_of_servers
     */
    public java.lang.Integer getNumber_of_servers() {
        return number_of_servers;
    }


    /**
     * Sets the number_of_servers value for this VDC.
     * 
     * @param number_of_servers
     */
    public void setNumber_of_servers(java.lang.Integer number_of_servers) {
        this.number_of_servers = number_of_servers;
    }


    /**
     * Gets the number_of_disks value for this VDC.
     * 
     * @return number_of_disks
     */
    public java.lang.Integer getNumber_of_disks() {
        return number_of_disks;
    }


    /**
     * Sets the number_of_disks value for this VDC.
     * 
     * @param number_of_disks
     */
    public void setNumber_of_disks(java.lang.Integer number_of_disks) {
        this.number_of_disks = number_of_disks;
    }


    /**
     * Gets the number_of_disksnapshots value for this VDC.
     * 
     * @return number_of_disksnapshots
     */
    public java.lang.Integer getNumber_of_disksnapshots() {
        return number_of_disksnapshots;
    }


    /**
     * Sets the number_of_disksnapshots value for this VDC.
     * 
     * @param number_of_disksnapshots
     */
    public void setNumber_of_disksnapshots(java.lang.Integer number_of_disksnapshots) {
        this.number_of_disksnapshots = number_of_disksnapshots;
    }


    /**
     * Gets the number_of_images value for this VDC.
     * 
     * @return number_of_images
     */
    public java.lang.Integer getNumber_of_images() {
        return number_of_images;
    }


    /**
     * Sets the number_of_images value for this VDC.
     * 
     * @param number_of_images
     */
    public void setNumber_of_images(java.lang.Integer number_of_images) {
        this.number_of_images = number_of_images;
    }


    /**
     * Gets the number_of_vlans value for this VDC.
     * 
     * @return number_of_vlans
     */
    public java.lang.Integer getNumber_of_vlans() {
        return number_of_vlans;
    }


    /**
     * Sets the number_of_vlans value for this VDC.
     * 
     * @param number_of_vlans
     */
    public void setNumber_of_vlans(java.lang.Integer number_of_vlans) {
        this.number_of_vlans = number_of_vlans;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VDC)) return false;
        VDC other = (VDC) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vdc_id==null && other.getVdc_id()==null) || 
             (this.vdc_id!=null &&
              this.vdc_id.equals(other.getVdc_id()))) &&
            ((this.vdc_name==null && other.getVdc_name()==null) || 
             (this.vdc_name!=null &&
              this.vdc_name.equals(other.getVdc_name()))) &&
            ((this.vdc_uuid==null && other.getVdc_uuid()==null) || 
             (this.vdc_uuid!=null &&
              this.vdc_uuid.equals(other.getVdc_uuid()))) &&
            ((this.number_of_servers==null && other.getNumber_of_servers()==null) || 
             (this.number_of_servers!=null &&
              this.number_of_servers.equals(other.getNumber_of_servers()))) &&
            ((this.number_of_disks==null && other.getNumber_of_disks()==null) || 
             (this.number_of_disks!=null &&
              this.number_of_disks.equals(other.getNumber_of_disks()))) &&
            ((this.number_of_disksnapshots==null && other.getNumber_of_disksnapshots()==null) || 
             (this.number_of_disksnapshots!=null &&
              this.number_of_disksnapshots.equals(other.getNumber_of_disksnapshots()))) &&
            ((this.number_of_images==null && other.getNumber_of_images()==null) || 
             (this.number_of_images!=null &&
              this.number_of_images.equals(other.getNumber_of_images()))) &&
            ((this.number_of_vlans==null && other.getNumber_of_vlans()==null) || 
             (this.number_of_vlans!=null &&
              this.number_of_vlans.equals(other.getNumber_of_vlans())));
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
        if (getVdc_id() != null) {
            _hashCode += getVdc_id().hashCode();
        }
        if (getVdc_name() != null) {
            _hashCode += getVdc_name().hashCode();
        }
        if (getVdc_uuid() != null) {
            _hashCode += getVdc_uuid().hashCode();
        }
        if (getNumber_of_servers() != null) {
            _hashCode += getNumber_of_servers().hashCode();
        }
        if (getNumber_of_disks() != null) {
            _hashCode += getNumber_of_disks().hashCode();
        }
        if (getNumber_of_disksnapshots() != null) {
            _hashCode += getNumber_of_disksnapshots().hashCode();
        }
        if (getNumber_of_images() != null) {
            _hashCode += getNumber_of_images().hashCode();
        }
        if (getNumber_of_vlans() != null) {
            _hashCode += getNumber_of_vlans().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VDC.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "VDC"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vdc_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vdc_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vdc_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vdc_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vdc_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vdc_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_of_servers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_of_servers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_of_disks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_of_disks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_of_disksnapshots");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_of_disksnapshots"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_of_images");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_of_images"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_of_vlans");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_of_vlans"));
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
