package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_FORM_MODEL")
public class FormModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal id;

    /**
     * 字段名
     */
    private String modelField;

    /**
     * 字段中文名称
     */
    private String modelName;

    /**
     * 描述
     */
    private String modelDes;

    /**
     * 顺序
     */
    private Long modelPriority;

    /**
     * 页面标签类型
     */
    private String dataType;

    /**
     * 是否正行显示
     */
    private Long single;

    /**
     * 标签显示位置
     */
    private Long helpposition;

    /**
     * 页面临时value
     */
    private String modelValue;

    private Long modularId;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }
    public String getModelField() {
        return modelField;
    }

    public void setModelField(String modelField) {
        this.modelField = modelField;
    }
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelDes() {
        return modelDes;
    }

    public void setModelDes(String modelDes) {
        this.modelDes = modelDes;
    }
    public Long getModelPriority() {
        return modelPriority;
    }

    public void setModelPriority(Long modelPriority) {
        this.modelPriority = modelPriority;
    }
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public Long getSingle() {
        return single;
    }

    public void setSingle(Long single) {
        this.single = single;
    }
    public Long getHelpposition() {
        return helpposition;
    }

    public void setHelpposition(Long helpposition) {
        this.helpposition = helpposition;
    }
    public String getModelValue() {
        return modelValue;
    }

    public void setModelValue(String modelValue) {
        this.modelValue = modelValue;
    }
    public Long getModularId() {
        return modularId;
    }

    public void setModularId(Long modularId) {
        this.modularId = modularId;
    }

    @Override
    public String toString() {
        return "FormModel{" +
            "id=" + id +
            ", modelField=" + modelField +
            ", modelName=" + modelName +
            ", modelDes=" + modelDes +
            ", modelPriority=" + modelPriority +
            ", dataType=" + dataType +
            ", single=" + single +
            ", helpposition=" + helpposition +
            ", modelValue=" + modelValue +
            ", modularId=" + modularId +
        "}";
    }
}
