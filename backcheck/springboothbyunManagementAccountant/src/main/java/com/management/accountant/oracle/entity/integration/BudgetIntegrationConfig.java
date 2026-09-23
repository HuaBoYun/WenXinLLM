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
 * 预算集成配置实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_INTEGRATION_CONFIG")
public class BudgetIntegrationConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 集成配置ID (主键)
     */
    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_UUID)
    private String configId;

    /**
     * 配置编码
     */
    @TableField("CONFIG_CODE")
    private String configCode;

    /**
     * 配置名称
     */
    @TableField("CONFIG_NAME")
    private String configName;

    /**
     * 配置类型 (SYSTEM/INTEGRATION/SECURITY/PERFORMANCE)
     */
    @TableField("CONFIG_TYPE")
    private String configType;

    /**
     * 配置分组
     */
    @TableField("CONFIG_GROUP")
    private String configGroup;

    /**
     * 配置键
     */
    @TableField("CONFIG_KEY")
    private String configKey;

    /**
     * 配置值
     */
    @TableField("CONFIG_VALUE")
    private String configValue;

    /**
     * 配置值类型 (STRING/NUMBER/BOOLEAN/JSON/XML)
     */
    @TableField("VALUE_TYPE")
    private String valueType;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 配置描述
     */
    @TableField("CONFIG_DESC")
    private String configDesc;

    /**
     * 是否加密
     */
    @TableField("IS_ENCRYPTED")
    private Boolean isEncrypted;

    /**
     * 是否可修改
     */
    @TableField("IS_EDITABLE")
    private Boolean isEditable;

    /**
     * 是否系统配置
     */
    @TableField("IS_SYSTEM")
    private Boolean isSystem;

    /**
     * 验证规则 (正则表达式)
     */
    @TableField("VALIDATION_RULE")
    private String validationRule;

    /**
     * 取值范围说明
     */
    @TableField("VALUE_RANGE")
    private String valueRange;

    /**
     * 配置优先级
     */
    @TableField("PRIORITY")
    private Integer priority;

    /**
     * 生效范围 (GLOBAL/TENANT/USER)
     */
    @TableField("SCOPE")
    private String scope;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 配置状态 (ACTIVE/INACTIVE/DEPRECATED)
     */
    @TableField("CONFIG_STATUS")
    private String configStatus;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRY_TIME")
    private Date expiryTime;

    /**
     * 最后修改时间
     */
    @TableField("LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    /**
     * 修改次数
     */
    @TableField("MODIFY_COUNT")
    private Integer modifyCount;

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
     * 更新人 (兼容字段)
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间 (兼容字段)
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

