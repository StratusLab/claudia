/**
 * ImageTemplate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class ImageTemplate  implements java.io.Serializable {
    private java.lang.Long image_template_id;

    private java.lang.String image_template_name;

    private java.lang.String image_template_uuid;

    private java.lang.String image_template_key;

    private java.lang.Integer image_template_size;

    private java.lang.Boolean image_template_canmodify;

    private java.lang.String image_template_default_username;

    private java.lang.Boolean image_template_generate_password;

    public ImageTemplate() {
    }

    public ImageTemplate(
           java.lang.Long image_template_id,
           java.lang.String image_template_name,
           java.lang.String image_template_uuid,
           java.lang.String image_template_key,
           java.lang.Integer image_template_size,
           java.lang.Boolean image_template_canmodify,
           java.lang.String image_template_default_username,
           java.lang.Boolean image_template_generate_password) {
           this.image_template_id = image_template_id;
           this.image_template_name = image_template_name;
           this.image_template_uuid = image_template_uuid;
           this.image_template_key = image_template_key;
           this.image_template_size = image_template_size;
           this.image_template_canmodify = image_template_canmodify;
           this.image_template_default_username = image_template_default_username;
           this.image_template_generate_password = image_template_generate_password;
    }


    /**
     * Gets the image_template_id value for this ImageTemplate.
     * 
     * @return image_template_id
     */
    public java.lang.Long getImage_template_id() {
        return image_template_id;
    }


    /**
     * Sets the image_template_id value for this ImageTemplate.
     * 
     * @param image_template_id
     */
    public void setImage_template_id(java.lang.Long image_template_id) {
        this.image_template_id = image_template_id;
    }


    /**
     * Gets the image_template_name value for this ImageTemplate.
     * 
     * @return image_template_name
     */
    public java.lang.String getImage_template_name() {
        return image_template_name;
    }


    /**
     * Sets the image_template_name value for this ImageTemplate.
     * 
     * @param image_template_name
     */
    public void setImage_template_name(java.lang.String image_template_name) {
        this.image_template_name = image_template_name;
    }


    /**
     * Gets the image_template_uuid value for this ImageTemplate.
     * 
     * @return image_template_uuid
     */
    public java.lang.String getImage_template_uuid() {
        return image_template_uuid;
    }


    /**
     * Sets the image_template_uuid value for this ImageTemplate.
     * 
     * @param image_template_uuid
     */
    public void setImage_template_uuid(java.lang.String image_template_uuid) {
        this.image_template_uuid = image_template_uuid;
    }


    /**
     * Gets the image_template_key value for this ImageTemplate.
     * 
     * @return image_template_key
     */
    public java.lang.String getImage_template_key() {
        return image_template_key;
    }


    /**
     * Sets the image_template_key value for this ImageTemplate.
     * 
     * @param image_template_key
     */
    public void setImage_template_key(java.lang.String image_template_key) {
        this.image_template_key = image_template_key;
    }


    /**
     * Gets the image_template_size value for this ImageTemplate.
     * 
     * @return image_template_size
     */
    public java.lang.Integer getImage_template_size() {
        return image_template_size;
    }


    /**
     * Sets the image_template_size value for this ImageTemplate.
     * 
     * @param image_template_size
     */
    public void setImage_template_size(java.lang.Integer image_template_size) {
        this.image_template_size = image_template_size;
    }


    /**
     * Gets the image_template_canmodify value for this ImageTemplate.
     * 
     * @return image_template_canmodify
     */
    public java.lang.Boolean getImage_template_canmodify() {
        return image_template_canmodify;
    }


    /**
     * Sets the image_template_canmodify value for this ImageTemplate.
     * 
     * @param image_template_canmodify
     */
    public void setImage_template_canmodify(java.lang.Boolean image_template_canmodify) {
        this.image_template_canmodify = image_template_canmodify;
    }


    /**
     * Gets the image_template_default_username value for this ImageTemplate.
     * 
     * @return image_template_default_username
     */
    public java.lang.String getImage_template_default_username() {
        return image_template_default_username;
    }


    /**
     * Sets the image_template_default_username value for this ImageTemplate.
     * 
     * @param image_template_default_username
     */
    public void setImage_template_default_username(java.lang.String image_template_default_username) {
        this.image_template_default_username = image_template_default_username;
    }


    /**
     * Gets the image_template_generate_password value for this ImageTemplate.
     * 
     * @return image_template_generate_password
     */
    public java.lang.Boolean getImage_template_generate_password() {
        return image_template_generate_password;
    }


    /**
     * Sets the image_template_generate_password value for this ImageTemplate.
     * 
     * @param image_template_generate_password
     */
    public void setImage_template_generate_password(java.lang.Boolean image_template_generate_password) {
        this.image_template_generate_password = image_template_generate_password;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImageTemplate)) return false;
        ImageTemplate other = (ImageTemplate) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.image_template_id==null && other.getImage_template_id()==null) || 
             (this.image_template_id!=null &&
              this.image_template_id.equals(other.getImage_template_id()))) &&
            ((this.image_template_name==null && other.getImage_template_name()==null) || 
             (this.image_template_name!=null &&
              this.image_template_name.equals(other.getImage_template_name()))) &&
            ((this.image_template_uuid==null && other.getImage_template_uuid()==null) || 
             (this.image_template_uuid!=null &&
              this.image_template_uuid.equals(other.getImage_template_uuid()))) &&
            ((this.image_template_key==null && other.getImage_template_key()==null) || 
             (this.image_template_key!=null &&
              this.image_template_key.equals(other.getImage_template_key()))) &&
            ((this.image_template_size==null && other.getImage_template_size()==null) || 
             (this.image_template_size!=null &&
              this.image_template_size.equals(other.getImage_template_size()))) &&
            ((this.image_template_canmodify==null && other.getImage_template_canmodify()==null) || 
             (this.image_template_canmodify!=null &&
              this.image_template_canmodify.equals(other.getImage_template_canmodify()))) &&
            ((this.image_template_default_username==null && other.getImage_template_default_username()==null) || 
             (this.image_template_default_username!=null &&
              this.image_template_default_username.equals(other.getImage_template_default_username()))) &&
            ((this.image_template_generate_password==null && other.getImage_template_generate_password()==null) || 
             (this.image_template_generate_password!=null &&
              this.image_template_generate_password.equals(other.getImage_template_generate_password())));
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
        if (getImage_template_id() != null) {
            _hashCode += getImage_template_id().hashCode();
        }
        if (getImage_template_name() != null) {
            _hashCode += getImage_template_name().hashCode();
        }
        if (getImage_template_uuid() != null) {
            _hashCode += getImage_template_uuid().hashCode();
        }
        if (getImage_template_key() != null) {
            _hashCode += getImage_template_key().hashCode();
        }
        if (getImage_template_size() != null) {
            _hashCode += getImage_template_size().hashCode();
        }
        if (getImage_template_canmodify() != null) {
            _hashCode += getImage_template_canmodify().hashCode();
        }
        if (getImage_template_default_username() != null) {
            _hashCode += getImage_template_default_username().hashCode();
        }
        if (getImage_template_generate_password() != null) {
            _hashCode += getImage_template_generate_password().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImageTemplate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ImageTemplate"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_canmodify");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_canmodify"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_default_username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_default_username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image_template_generate_password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "image_template_generate_password"));
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
