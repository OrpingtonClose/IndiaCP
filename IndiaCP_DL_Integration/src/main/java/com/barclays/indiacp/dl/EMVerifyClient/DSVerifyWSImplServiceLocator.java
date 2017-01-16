/**
 * DSVerifyWSImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.barclays.indiacp.dl.EMVerifyClient;

public class DSVerifyWSImplServiceLocator extends org.apache.axis.client.Service implements DSVerifyWSImplService {

    public DSVerifyWSImplServiceLocator() {
    }


    public DSVerifyWSImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DSVerifyWSImplServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DSVerifyWSImplPort
    private String DSVerifyWSImplPort_address = "http://secmsgdemo.emudhra.com:8282/emas/services/dsverifyWS";

    public String getDSVerifyWSImplPortAddress() {
        return DSVerifyWSImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String DSVerifyWSImplPortWSDDServiceName = "DSVerifyWSImplPort";

    public String getDSVerifyWSImplPortWSDDServiceName() {
        return DSVerifyWSImplPortWSDDServiceName;
    }

    public void setDSVerifyWSImplPortWSDDServiceName(String name) {
        DSVerifyWSImplPortWSDDServiceName = name;
    }

    public DSVerifyWS getDSVerifyWSImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DSVerifyWSImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDSVerifyWSImplPort(endpoint);
    }

    public DSVerifyWS getDSVerifyWSImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DSVerifyWSImplServiceSoapBindingStub _stub = new DSVerifyWSImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDSVerifyWSImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDSVerifyWSImplPortEndpointAddress(String address) {
        DSVerifyWSImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (DSVerifyWS.class.isAssignableFrom(serviceEndpointInterface)) {
                DSVerifyWSImplServiceSoapBindingStub _stub = new DSVerifyWSImplServiceSoapBindingStub(new java.net.URL(DSVerifyWSImplPort_address), this);
                _stub.setPortName(getDSVerifyWSImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
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
        String inputPortName = portName.getLocalPart();
        if ("DSVerifyWSImplPort".equals(inputPortName)) {
            return getDSVerifyWSImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ds.ws.emas/", "DSVerifyWSImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ds.ws.emas/", "DSVerifyWSImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("DSVerifyWSImplPort".equals(portName)) {
            setDSVerifyWSImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
