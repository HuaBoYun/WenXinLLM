package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 集成配置规则实体
 * 定义集成配置的业务规则，支持 VALIDATION/TRANSFORM/FILTER/TRIGGER 等规则类型
 *
 * @author system
 * @date 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INTEGRATION_CONFIG_RULE")
public class IntegrationConfigRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 规则主键ID */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_UUID)
    private String ruleId;

    /** 关联的集成配置ID */
    @TableField("CONFIG_ID")
    private String configId;

    /** 规则名称 */
    @TableField("RULE_NAME")
    private String ruleName;

    /** 规则类型（VALIDATION/TRANSFORM/FILTER/TRIGGER） */
    @TableField("RULE_TYPE")
    private String ruleType;

    /** 规则条件表达式 */
    @TableField("RULE_CONDITION")
    private String ruleCondition;

    /** 规则动作表达式 */
    @TableField("RULE_ACTION")
    private String ruleAction;

    /** 优先级，数值越小优先级越高 */
    @TableField("PRIORITY")
    private Integer priority;

    /** 是否启用 */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /** 规则描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
