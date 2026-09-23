/**
 * ISysNotifyTodoWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.landray.kmss.sys.notify.webservice;

public class ISysNotifyTodoWebServiceServiceLocator extends org.apache.axis.client.Service implements ISysNotifyTodoWebServiceService {

    public ISysNotifyTodoWebServiceServiceLocator() {
    }


    public ISysNotifyTodoWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ISysNotifyTodoWebServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ISysNotifyTodoWebServicePort
    private String ISysNotifyTodoWebServicePort_address = "http://ekptest.sz-expressway.com/sys/webservice/sysNotifyTodoWebService";

    public String getISysNotifyTodoWebServicePortAddress() {
        return ISysNotifyTodoWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private String ISysNotifyTodoWebServicePortWSDDServiceName = "ISysNotifyTodoWebServicePort";

    public String getISysNotifyTodoWebServicePortWSDDServiceName() {
        return ISysNotifyTodoWebServicePortWSDDServiceName;
    }

    public void setISysNotifyTodoWebServicePortWSDDServiceName(String name) {
        ISysNotifyTodoWebServicePortWSDDServiceName = name;
    }

    public ISysNotifyTodoWebService getISysNotifyTodoWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ISysNotifyTodoWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getISysNotifyTodoWebServicePort(endpoint);
    }

    public ISysNotifyTodoWebService getISysNotifyTodoWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ISysNotifyTodoWebServiceServiceSoapBindingStub _stub = new ISysNotifyTodoWebServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getISysNotifyTodoWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setISysNotifyTodoWebServicePortEndpointAddress(String address) {
        ISysNotifyTodoWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ISysNotifyTodoWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                ISysNotifyTodoWebServiceServiceSoapBindingStub _stub = new ISysNotifyTodoWebServiceServiceSoapBindingStub(new java.net.URL(ISysNotifyTodoWebServicePort_address), this);
                _stub.setPortName(getISysNotifyTodoWebServicePortWSDDServiceName());
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
        if ("ISysNotifyTodoWebServicePort".equals(inputPortName)) {
            return getISysNotifyTodoWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.notify.sys.kmss.landray.com/", "ISysNotifyTodoWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.notify.sys.kmss.landray.com/", "ISysNotifyTodoWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("ISysNotifyTodoWebServicePort".equals(portName)) {
            setISysNotifyTodoWebServicePortEndpointAddress(address);
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
