/**
 * ProductOffer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class ProductOffer  implements java.io.Serializable {
    private java.lang.Long product_offer_id;

    private java.lang.String description;

    private com.flexiant.extility.ProductComponent[] components;

    private java.lang.Double units;

    private java.lang.Double initial_charge;

    private java.lang.Double repeat_charge;

    private java.lang.Double minimum_units;

    public ProductOffer() {
    }

    public ProductOffer(
           java.lang.Long product_offer_id,
           java.lang.String description,
           com.flexiant.extility.ProductComponent[] components,
           java.lang.Double units,
           java.lang.Double initial_charge,
           java.lang.Double repeat_charge,
           java.lang.Double minimum_units) {
           this.product_offer_id = product_offer_id;
           this.description = description;
           this.components = components;
           this.units = units;
           this.initial_charge = initial_charge;
           this.repeat_charge = repeat_charge;
           this.minimum_units = minimum_units;
    }


    /**
     * Gets the product_offer_id value for this ProductOffer.
     * 
     * @return product_offer_id
     */
    public java.lang.Long getProduct_offer_id() {
        return product_offer_id;
    }


    /**
     * Sets the product_offer_id value for this ProductOffer.
     * 
     * @param product_offer_id
     */
    public void setProduct_offer_id(java.lang.Long product_offer_id) {
        this.product_offer_id = product_offer_id;
    }


    /**
     * Gets the description value for this ProductOffer.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ProductOffer.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the components value for this ProductOffer.
     * 
     * @return components
     */
    public com.flexiant.extility.ProductComponent[] getComponents() {
        return components;
    }


    /**
     * Sets the components value for this ProductOffer.
     * 
     * @param components
     */
    public void setComponents(com.flexiant.extility.ProductComponent[] components) {
        this.components = components;
    }


    /**
     * Gets the units value for this ProductOffer.
     * 
     * @return units
     */
    public java.lang.Double getUnits() {
        return units;
    }


    /**
     * Sets the units value for this ProductOffer.
     * 
     * @param units
     */
    public void setUnits(java.lang.Double units) {
        this.units = units;
    }


    /**
     * Gets the initial_charge value for this ProductOffer.
     * 
     * @return initial_charge
     */
    public java.lang.Double getInitial_charge() {
        return initial_charge;
    }


    /**
     * Sets the initial_charge value for this ProductOffer.
     * 
     * @param initial_charge
     */
    public void setInitial_charge(java.lang.Double initial_charge) {
        this.initial_charge = initial_charge;
    }


    /**
     * Gets the repeat_charge value for this ProductOffer.
     * 
     * @return repeat_charge
     */
    public java.lang.Double getRepeat_charge() {
        return repeat_charge;
    }


    /**
     * Sets the repeat_charge value for this ProductOffer.
     * 
     * @param repeat_charge
     */
    public void setRepeat_charge(java.lang.Double repeat_charge) {
        this.repeat_charge = repeat_charge;
    }


    /**
     * Gets the minimum_units value for this ProductOffer.
     * 
     * @return minimum_units
     */
    public java.lang.Double getMinimum_units() {
        return minimum_units;
    }


    /**
     * Sets the minimum_units value for this ProductOffer.
     * 
     * @param minimum_units
     */
    public void setMinimum_units(java.lang.Double minimum_units) {
        this.minimum_units = minimum_units;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProductOffer)) return false;
        ProductOffer other = (ProductOffer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.product_offer_id==null && other.getProduct_offer_id()==null) || 
             (this.product_offer_id!=null &&
              this.product_offer_id.equals(other.getProduct_offer_id()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.components==null && other.getComponents()==null) || 
             (this.components!=null &&
              java.util.Arrays.equals(this.components, other.getComponents()))) &&
            ((this.units==null && other.getUnits()==null) || 
             (this.units!=null &&
              this.units.equals(other.getUnits()))) &&
            ((this.initial_charge==null && other.getInitial_charge()==null) || 
             (this.initial_charge!=null &&
              this.initial_charge.equals(other.getInitial_charge()))) &&
            ((this.repeat_charge==null && other.getRepeat_charge()==null) || 
             (this.repeat_charge!=null &&
              this.repeat_charge.equals(other.getRepeat_charge()))) &&
            ((this.minimum_units==null && other.getMinimum_units()==null) || 
             (this.minimum_units!=null &&
              this.minimum_units.equals(other.getMinimum_units())));
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
        if (getProduct_offer_id() != null) {
            _hashCode += getProduct_offer_id().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getComponents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getComponents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getComponents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUnits() != null) {
            _hashCode += getUnits().hashCode();
        }
        if (getInitial_charge() != null) {
            _hashCode += getInitial_charge().hashCode();
        }
        if (getRepeat_charge() != null) {
            _hashCode += getRepeat_charge().hashCode();
        }
        if (getMinimum_units() != null) {
            _hashCode += getMinimum_units().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProductOffer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ProductOffer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("product_offer_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "product_offer_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("components");
        elemField.setXmlName(new javax.xml.namespace.QName("", "components"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "ProductComponent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("", "units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initial_charge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "initial_charge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repeat_charge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "repeat_charge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minimum_units");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minimum_units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
