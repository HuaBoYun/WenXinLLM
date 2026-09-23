package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算数据映射配置实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_DATA_MAPPING")
public class BudgetDataMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据映射ID (主键)
     */
    @TableId(value = "MAPPING_ID", type = IdType.ASSIGN_UUID)
    private String mappingId;

    /**
     * 映射编码
     */
    @TableField("MAPPING_CODE")
    private String mappingCode;

    /**
     * 映射名称
     */
    @TableField("MAPPING_NAME")
    private String mappingName;

    /**
     * 映射类型 (FIELD/TABLE/ENTITY/VALUE)
     */
    @TableField("MAPPING_TYPE")
    private String mappingType;

    /**
     * 源系统类型
     */
    @TableField("SOURCE_SYSTEM_TYPE")
    private String sourceSystemType;

    /**
     * 源系统ID
     */
    @TableField("SOURCE_SYSTEM_ID")
    private String sourceSystemId;

    /**
     * 源对象名称(表名/实体名)
     */
    @TableField("SOURCE_OBJECT_NAME")
    private String sourceObjectName;

    /**
     * 源字段名称
     */
    @TableField("SOURCE_FIELD_NAME")
    private String sourceFieldName;

    /**
     * 源字段类型
     */
    @TableField("SOURCE_FIELD_TYPE")
    private String sourceFieldType;

    /**
     * 源字段描述
     */
    @TableField("SOURCE_FIELD_DESC")
    private String sourceFieldDesc;

    /**
     * 目标系统类型
     */
    @TableField("TARGET_SYSTEM_TYPE")
    private String targetSystemType;

    /**
     * 目标系统ID
     */
    @TableField("TARGET_SYSTEM_ID")
    private String targetSystemId;

    /**
     * 目标对象名称(表名/实体名)
     */
    @TableField("TARGET_OBJECT_NAME")
    private String targetObjectName;

    /**
     * 目标字段名称
     */
    @TableField("TARGET_FIELD_NAME")
    private String targetFieldName;

    /**
     * 目标字段类型
     */
    @TableField("TARGET_FIELD_TYPE")
    private String targetFieldType;

    /**
     * 目标字段描述
     */
    @TableField("TARGET_FIELD_DESC")
    private String targetFieldDesc;

    /**
     * 转换规则类型 (DIRECT/FORMULA/LOOKUP/SCRIPT)
     */
    @TableField("TRANSFORM_TYPE")
    private String transformType;

    /**
     * 转换规则表达式
     */
    @TableField("TRANSFORM_EXPRESSION")
    private String transformExpression;

    /**
     * 转换脚本(Groovy/JavaScript)
     */
    @TableField("TRANSFORM_SCRIPT")
    private String transformScript;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 是否必填
     */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 验证规则 (JSON格式)
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 值映射表 (JSON格式)
     */
    @TableField("VALUE_MAPPING_TABLE")
    private String valueMappingTable;

    /**
     * 映射优先级
     */
    @TableField("PRIORITY")
    private Integer priority;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 映射状态 (ACTIVE/INACTIVE/TESTING)
     */
    @TableField("MAPPING_STATUS")
    private String mappingStatus;

    /**
     * 最后使用时间
     */
    @TableField("LAST_USED_TIME")
    private Date lastUsedTime;

    /**
     * 使用次数
     */
    @TableField("USAGE_COUNT")
    private Integer usageCount;

    /**
     * 成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /**
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;

}

