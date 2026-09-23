package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 数据映射配置表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcDataMapping", description = "数据映射配置管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_DATA_MAPPING")
public class TcDataMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 映射名称
     */
    @TableField("MAPPING_NAME")
    @ApiModelProperty(value = "映射名称", required = true)
    private String mappingName;

    /**
     * 映射编码
     */
    @TableField("MAPPING_CODE")
    @ApiModelProperty(value = "映射编码")
    private String mappingCode;

    /**
     * 源系统ID
     */
    @TableField("SOURCE_SYSTEM")
    @ApiModelProperty(value = "源系统ID", required = true)
    private String sourceSystem;

    /**
     * 目标系统ID
     */
    @TableField("TARGET_SYSTEM")
    @ApiModelProperty(value = "目标系统ID", required = true)
    private String targetSystem;

    /**
     * 源字段
     */
    @TableField("SOURCE_FIELD")
    @ApiModelProperty(value = "源字段", required = true)
    private String sourceField;

    /**
     * 目标字段
     */
    @TableField("TARGET_FIELD")
    @ApiModelProperty(value = "目标字段", required = true)
    private String targetField;

    /**
     * 字段类型
     */
    @TableField("FIELD_TYPE")
    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    /**
     * 映射类型: FIELD-字段映射, VALUE-值映射, FUNCTION-函数映射
     */
    @TableField("MAPPING_TYPE")
    @ApiModelProperty(value = "映射类型", required = true)
    private String mappingType;

    /**
     * 映射规则
     */
    @TableField("MAPPING_RULE")
    @ApiModelProperty(value = "映射规则")
    private String mappingRule;

    /**
     * 转换规则
     */
    @TableField("CONVERSION_RULE")
    @ApiModelProperty(value = "转换规则")
    private String conversionRule;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    /**
     * 是否必填：1-是，0-否
     */
    @TableField("IS_REQUIRED")
    @ApiModelProperty(value = "是否必填：1-是，0-否")
    private String isRequired;

    /**
     * 校验规则
     */
    @TableField("VALIDATION_RULE")
    @ApiModelProperty(value = "校验规则")
    private String validationRule;

    /**
     * 错误处理策略
     */
    @TableField("ERROR_STRATEGY")
    @ApiModelProperty(value = "错误处理策略")
    private String errorStrategy;

    /**
     * 排序顺序
     */
    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序顺序")
    private Integer sortOrder;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcDataMapping ofId(String id) {
        TcDataMapping mapping = new TcDataMapping();
        mapping.setId(id);
        return mapping;
    }

    /**
     * 根据源系统和目标系统创建实例
     */
    public static TcDataMapping ofSystems(String sourceSystem, String targetSystem) {
        TcDataMapping mapping = new TcDataMapping();
        mapping.setSourceSystem(sourceSystem);
        mapping.setTargetSystem(targetSystem);
        return mapping;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMappingName() { return mappingName; }
    public void setMappingName(String mappingName) { this.mappingName = mappingName; }

    public String getMappingCode() { return mappingCode; }
    public void setMappingCode(String mappingCode) { this.mappingCode = mappingCode; }

    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }

    public String getTargetSystem() { return targetSystem; }
    public void setTargetSystem(String targetSystem) { this.targetSystem = targetSystem; }

    public String getSourceField() { return sourceField; }
    public void setSourceField(String sourceField) { this.sourceField = sourceField; }

    public String getTargetField() { return targetField; }
    public void setTargetField(String targetField) { this.targetField = targetField; }

    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }

    public String getMappingType() { return mappingType; }
    public void setMappingType(String mappingType) { this.mappingType = mappingType; }

    public String getMappingRule() { return mappingRule; }
    public void setMappingRule(String mappingRule) { this.mappingRule = mappingRule; }
    public String getConversionRule() { return conversionRule; }
    public void setConversionRule(String conversionRule) { this.conversionRule = conversionRule; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getIsRequired() { return isRequired; }
    public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
    public String getValidationRule() { return validationRule; }
    public void setValidationRule(String validationRule) { this.validationRule = validationRule; }
    public String getErrorStrategy() { return errorStrategy; }
    public void setErrorStrategy(String errorStrategy) { this.errorStrategy = errorStrategy; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
