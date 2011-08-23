/**
 * FilterJobList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class FilterJobList  implements java.io.Serializable {
    private java.lang.Long num_of_jobs;

    private java.lang.Long from;

    private java.lang.Long to;

    private com.flexiant.extility.Job[] job_list;

    public FilterJobList() {
    }

    public FilterJobList(
           java.lang.Long num_of_jobs,
           java.lang.Long from,
           java.lang.Long to,
           com.flexiant.extility.Job[] job_list) {
           this.num_of_jobs = num_of_jobs;
           this.from = from;
           this.to = to;
           this.job_list = job_list;
    }


    /**
     * Gets the num_of_jobs value for this FilterJobList.
     * 
     * @return num_of_jobs
     */
    public java.lang.Long getNum_of_jobs() {
        return num_of_jobs;
    }


    /**
     * Sets the num_of_jobs value for this FilterJobList.
     * 
     * @param num_of_jobs
     */
    public void setNum_of_jobs(java.lang.Long num_of_jobs) {
        this.num_of_jobs = num_of_jobs;
    }


    /**
     * Gets the from value for this FilterJobList.
     * 
     * @return from
     */
    public java.lang.Long getFrom() {
        return from;
    }


    /**
     * Sets the from value for this FilterJobList.
     * 
     * @param from
     */
    public void setFrom(java.lang.Long from) {
        this.from = from;
    }


    /**
     * Gets the to value for this FilterJobList.
     * 
     * @return to
     */
    public java.lang.Long getTo() {
        return to;
    }


    /**
     * Sets the to value for this FilterJobList.
     * 
     * @param to
     */
    public void setTo(java.lang.Long to) {
        this.to = to;
    }


    /**
     * Gets the job_list value for this FilterJobList.
     * 
     * @return job_list
     */
    public com.flexiant.extility.Job[] getJob_list() {
        return job_list;
    }


    /**
     * Sets the job_list value for this FilterJobList.
     * 
     * @param job_list
     */
    public void setJob_list(com.flexiant.extility.Job[] job_list) {
        this.job_list = job_list;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FilterJobList)) return false;
        FilterJobList other = (FilterJobList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.num_of_jobs==null && other.getNum_of_jobs()==null) || 
             (this.num_of_jobs!=null &&
              this.num_of_jobs.equals(other.getNum_of_jobs()))) &&
            ((this.from==null && other.getFrom()==null) || 
             (this.from!=null &&
              this.from.equals(other.getFrom()))) &&
            ((this.to==null && other.getTo()==null) || 
             (this.to!=null &&
              this.to.equals(other.getTo()))) &&
            ((this.job_list==null && other.getJob_list()==null) || 
             (this.job_list!=null &&
              java.util.Arrays.equals(this.job_list, other.getJob_list())));
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
        if (getNum_of_jobs() != null) {
            _hashCode += getNum_of_jobs().hashCode();
        }
        if (getFrom() != null) {
            _hashCode += getFrom().hashCode();
        }
        if (getTo() != null) {
            _hashCode += getTo().hashCode();
        }
        if (getJob_list() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJob_list());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJob_list(), i);
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
        new org.apache.axis.description.TypeDesc(FilterJobList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "FilterJobList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_of_jobs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_of_jobs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("from");
        elemField.setXmlName(new javax.xml.namespace.QName("", "from"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("to");
        elemField.setXmlName(new javax.xml.namespace.QName("", "to"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("job_list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "job_list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "Job"));
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
