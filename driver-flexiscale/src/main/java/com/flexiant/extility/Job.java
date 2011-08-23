/**
 * Job.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class Job  implements java.io.Serializable {
    private java.lang.Long job_id;

    private java.lang.Integer action_id;

    private java.lang.Long item_id;

    private java.lang.String item_description;

    private java.lang.Integer status;

    private java.util.Calendar started;

    private java.util.Calendar finished;

    private java.lang.Long dependant;

    public Job() {
    }

    public Job(
           java.lang.Long job_id,
           java.lang.Integer action_id,
           java.lang.Long item_id,
           java.lang.String item_description,
           java.lang.Integer status,
           java.util.Calendar started,
           java.util.Calendar finished,
           java.lang.Long dependant) {
           this.job_id = job_id;
           this.action_id = action_id;
           this.item_id = item_id;
           this.item_description = item_description;
           this.status = status;
           this.started = started;
           this.finished = finished;
           this.dependant = dependant;
    }


    /**
     * Gets the job_id value for this Job.
     * 
     * @return job_id
     */
    public java.lang.Long getJob_id() {
        return job_id;
    }


    /**
     * Sets the job_id value for this Job.
     * 
     * @param job_id
     */
    public void setJob_id(java.lang.Long job_id) {
        this.job_id = job_id;
    }


    /**
     * Gets the action_id value for this Job.
     * 
     * @return action_id
     */
    public java.lang.Integer getAction_id() {
        return action_id;
    }


    /**
     * Sets the action_id value for this Job.
     * 
     * @param action_id
     */
    public void setAction_id(java.lang.Integer action_id) {
        this.action_id = action_id;
    }


    /**
     * Gets the item_id value for this Job.
     * 
     * @return item_id
     */
    public java.lang.Long getItem_id() {
        return item_id;
    }


    /**
     * Sets the item_id value for this Job.
     * 
     * @param item_id
     */
    public void setItem_id(java.lang.Long item_id) {
        this.item_id = item_id;
    }


    /**
     * Gets the item_description value for this Job.
     * 
     * @return item_description
     */
    public java.lang.String getItem_description() {
        return item_description;
    }


    /**
     * Sets the item_description value for this Job.
     * 
     * @param item_description
     */
    public void setItem_description(java.lang.String item_description) {
        this.item_description = item_description;
    }


    /**
     * Gets the status value for this Job.
     * 
     * @return status
     */
    public java.lang.Integer getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Job.
     * 
     * @param status
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }


    /**
     * Gets the started value for this Job.
     * 
     * @return started
     */
    public java.util.Calendar getStarted() {
        return started;
    }


    /**
     * Sets the started value for this Job.
     * 
     * @param started
     */
    public void setStarted(java.util.Calendar started) {
        this.started = started;
    }


    /**
     * Gets the finished value for this Job.
     * 
     * @return finished
     */
    public java.util.Calendar getFinished() {
        return finished;
    }


    /**
     * Sets the finished value for this Job.
     * 
     * @param finished
     */
    public void setFinished(java.util.Calendar finished) {
        this.finished = finished;
    }


    /**
     * Gets the dependant value for this Job.
     * 
     * @return dependant
     */
    public java.lang.Long getDependant() {
        return dependant;
    }


    /**
     * Sets the dependant value for this Job.
     * 
     * @param dependant
     */
    public void setDependant(java.lang.Long dependant) {
        this.dependant = dependant;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Job)) return false;
        Job other = (Job) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.job_id==null && other.getJob_id()==null) || 
             (this.job_id!=null &&
              this.job_id.equals(other.getJob_id()))) &&
            ((this.action_id==null && other.getAction_id()==null) || 
             (this.action_id!=null &&
              this.action_id.equals(other.getAction_id()))) &&
            ((this.item_id==null && other.getItem_id()==null) || 
             (this.item_id!=null &&
              this.item_id.equals(other.getItem_id()))) &&
            ((this.item_description==null && other.getItem_description()==null) || 
             (this.item_description!=null &&
              this.item_description.equals(other.getItem_description()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.started==null && other.getStarted()==null) || 
             (this.started!=null &&
              this.started.equals(other.getStarted()))) &&
            ((this.finished==null && other.getFinished()==null) || 
             (this.finished!=null &&
              this.finished.equals(other.getFinished()))) &&
            ((this.dependant==null && other.getDependant()==null) || 
             (this.dependant!=null &&
              this.dependant.equals(other.getDependant())));
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
        if (getJob_id() != null) {
            _hashCode += getJob_id().hashCode();
        }
        if (getAction_id() != null) {
            _hashCode += getAction_id().hashCode();
        }
        if (getItem_id() != null) {
            _hashCode += getItem_id().hashCode();
        }
        if (getItem_description() != null) {
            _hashCode += getItem_description().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getStarted() != null) {
            _hashCode += getStarted().hashCode();
        }
        if (getFinished() != null) {
            _hashCode += getFinished().hashCode();
        }
        if (getDependant() != null) {
            _hashCode += getDependant().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Job.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Job"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("job_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "job_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "action_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item_description"));
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
        elemField.setFieldName("started");
        elemField.setXmlName(new javax.xml.namespace.QName("", "started"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finished");
        elemField.setXmlName(new javax.xml.namespace.QName("", "finished"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dependant");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dependant"));
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
