/**
 * NotifyTodoRemoveContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.landray.kmss.sys.notify.webservice;

public class NotifyTodoRemoveContext  implements java.io.Serializable {
    private String appName;

    private String key;

    private String modelId;

    private String modelName;

    private int optType;

    private String others;

    private String param1;

    private String param2;

    private String targets;

    private int type;

    public NotifyTodoRemoveContext() {
    }

    public NotifyTodoRemoveContext(
           String appName,
           String key,
           String modelId,
           String modelName,
           int optType,
           String others,
           String param1,
           String param2,
           String targets,
           int type) {
           this.appName = appName;
           this.key = key;
           this.modelId = modelId;
           this.modelName = modelName;
           this.optType = optType;
           this.others = others;
           this.param1 = param1;
           this.param2 = param2;
           this.targets = targets;
           this.type = type;
    }


    /**
     * Gets the appName value for this NotifyTodoRemoveContext.
     * 
     * @return appName
     */
    public String getAppName() {
        return appName;
    }


    /**
     * Sets the appName value for this NotifyTodoRemoveContext.
     * 
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }


    /**
     * Gets the key value for this NotifyTodoRemoveContext.
     * 
     * @return key
     */
    public String getKey() {
        return key;
    }


    /**
     * Sets the key value for this NotifyTodoRemoveContext.
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }


    /**
     * Gets the modelId value for this NotifyTodoRemoveContext.
     * 
     * @return modelId
     */
    public String getModelId() {
        return modelId;
    }


    /**
     * Sets the modelId value for this NotifyTodoRemoveContext.
     * 
     * @param modelId
     */
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }


    /**
     * Gets the modelName value for this NotifyTodoRemoveContext.
     * 
     * @return modelName
     */
    public String getModelName() {
        return modelName;
    }


    /**
     * Sets the modelName value for this NotifyTodoRemoveContext.
     * 
     * @param modelName
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


    /**
     * Gets the optType value for this NotifyTodoRemoveContext.
     * 
     * @return optType
     */
    public int getOptType() {
        return optType;
    }


    /**
     * Sets the optType value for this NotifyTodoRemoveContext.
     * 
     * @param optType
     */
    public void setOptType(int optType) {
        this.optType = optType;
    }


    /**
     * Gets the others value for this NotifyTodoRemoveContext.
     * 
     * @return others
     */
    public String getOthers() {
        return others;
    }


    /**
     * Sets the others value for this NotifyTodoRemoveContext.
     * 
     * @param others
     */
    public void setOthers(String others) {
        this.others = others;
    }


    /**
     * Gets the param1 value for this NotifyTodoRemoveContext.
     * 
     * @return param1
     */
    public String getParam1() {
        return param1;
    }


    /**
     * Sets the param1 value for this NotifyTodoRemoveContext.
     * 
     * @param param1
     */
    public void setParam1(String param1) {
        this.param1 = param1;
    }


    /**
     * Gets the param2 value for this NotifyTodoRemoveContext.
     * 
     * @return param2
     */
    public String getParam2() {
        return param2;
    }


    /**
     * Sets the param2 value for this NotifyTodoRemoveContext.
     * 
     * @param param2
     */
    public void setParam2(String param2) {
        this.param2 = param2;
    }


    /**
     * Gets the targets value for this NotifyTodoRemoveContext.
     * 
     * @return targets
     */
    public String getTargets() {
        return targets;
    }


    /**
     * Sets the targets value for this NotifyTodoRemoveContext.
     * 
     * @param targets
     */
    public void setTargets(String targets) {
        this.targets = targets;
    }


    /**
     * Gets the type value for this NotifyTodoRemoveContext.
     * 
     * @return type
     */
    public int getType() {
        return type;
    }


    /**
     * Sets the type value for this NotifyTodoRemoveContext.
     * 
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof NotifyTodoRemoveContext)) return false;
        NotifyTodoRemoveContext other = (NotifyTodoRemoveContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.appName==null && other.getAppName()==null) || 
             (this.appName!=null &&
              this.appName.equals(other.getAppName()))) &&
            ((this.key==null && other.getKey()==null) || 
             (this.key!=null &&
              this.key.equals(other.getKey()))) &&
            ((this.modelId==null && other.getModelId()==null) || 
             (this.modelId!=null &&
              this.modelId.equals(other.getModelId()))) &&
            ((this.modelName==null && other.getModelName()==null) || 
             (this.modelName!=null &&
              this.modelName.equals(other.getModelName()))) &&
            this.optType == other.getOptType() &&
            ((this.others==null && other.getOthers()==null) || 
             (this.others!=null &&
              this.others.equals(other.getOthers()))) &&
            ((this.param1==null && other.getParam1()==null) || 
             (this.param1!=null &&
              this.param1.equals(other.getParam1()))) &&
            ((this.param2==null && other.getParam2()==null) || 
             (this.param2!=null &&
              this.param2.equals(other.getParam2()))) &&
            ((this.targets==null && other.getTargets()==null) || 
             (this.targets!=null &&
              this.targets.equals(other.getTargets()))) &&
            this.type == other.getType();
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
        if (getAppName() != null) {
            _hashCode += getAppName().hashCode();
        }
        if (getKey() != null) {
            _hashCode += getKey().hashCode();
        }
        if (getModelId() != null) {
            _hashCode += getModelId().hashCode();
        }
        if (getModelName() != null) {
            _hashCode += getModelName().hashCode();
        }
        _hashCode += getOptType();
        if (getOthers() != null) {
            _hashCode += getOthers().hashCode();
        }
        if (getParam1() != null) {
            _hashCode += getParam1().hashCode();
        }
        if (getParam2() != null) {
            _hashCode += getParam2().hashCode();
        }
        if (getTargets() != null) {
            _hashCode += getTargets().hashCode();
        }
        _hashCode += getType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotifyTodoRemoveContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.notify.sys.kmss.landray.com/", "notifyTodoRemoveContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modelId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modelId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modelName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modelName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "optType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("others");
        elemField.setXmlName(new javax.xml.namespace.QName("", "others"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("param1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "param1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("param2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "param2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targets");
        elemField.setXmlName(new javax.xml.namespace.QName("", "targets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
