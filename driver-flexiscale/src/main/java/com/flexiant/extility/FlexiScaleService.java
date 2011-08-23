/**
 * FlexiScaleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public interface FlexiScaleService extends javax.xml.rpc.Service {

/**
 * Service for connecting to FlexiScale
 */
    public java.lang.String getFlexiScaleAddress();

    public com.flexiant.extility.FlexiScale getFlexiScale() throws javax.xml.rpc.ServiceException;

    public com.flexiant.extility.FlexiScale getFlexiScale(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
