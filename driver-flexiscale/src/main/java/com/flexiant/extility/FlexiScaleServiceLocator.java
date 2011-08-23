/**
 * FlexiScaleServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.flexiant.extility;

public class FlexiScaleServiceLocator extends org.apache.axis.client.Service implements com.flexiant.extility.FlexiScaleService {

/**
 * Service for connecting to FlexiScale
 */

    public FlexiScaleServiceLocator() {
    }


    public FlexiScaleServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FlexiScaleServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FlexiScale
    private java.lang.String FlexiScale_address = "https://api.flexiscale.com/index.php";

    public java.lang.String getFlexiScaleAddress() {
        return FlexiScale_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FlexiScaleWSDDServiceName = "FlexiScale";

    public java.lang.String getFlexiScaleWSDDServiceName() {
        return FlexiScaleWSDDServiceName;
    }

    public void setFlexiScaleWSDDServiceName(java.lang.String name) {
        FlexiScaleWSDDServiceName = name;
    }

    public com.flexiant.extility.FlexiScale getFlexiScale() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FlexiScale_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFlexiScale(endpoint);
    }

    public com.flexiant.extility.FlexiScale getFlexiScale(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.flexiant.extility.FlexiScaleSoapBindingStub _stub = new com.flexiant.extility.FlexiScaleSoapBindingStub(portAddress, this);
            _stub.setPortName(getFlexiScaleWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFlexiScaleEndpointAddress(java.lang.String address) {
        FlexiScale_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.flexiant.extility.FlexiScale.class.isAssignableFrom(serviceEndpointInterface)) {
                com.flexiant.extility.FlexiScaleSoapBindingStub _stub = new com.flexiant.extility.FlexiScaleSoapBindingStub(new java.net.URL(FlexiScale_address), this);
                _stub.setPortName(getFlexiScaleWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("FlexiScale".equals(inputPortName)) {
            return getFlexiScale();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://extility.flexiant.com", "FlexiScaleService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://extility.flexiant.com", "FlexiScale"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FlexiScale".equals(portName)) {
            setFlexiScaleEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
