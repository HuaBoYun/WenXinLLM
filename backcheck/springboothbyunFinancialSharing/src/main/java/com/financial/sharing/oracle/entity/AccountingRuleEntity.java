package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会计规则实体类 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_ACCOUNTING_RULE")
public class AccountingRuleEntity {

    /**
     * 规则ID
     */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private Long ruleId;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 规则类型(1分录规则2分发规则3转换规则)
     */
    @TableField("RULE_TYPE")
    private Integer ruleType;

    /**
     * 适用事项类型
     */
    @TableField("TRANSACTION_TYPE")
    private String transactionType;

    /**
     * 规则条件(JSON格式)
     */
    @TableField("RULE_CONDITION")
    private String ruleCondition;

    /**
     * 规则动作(JSON格式)
     */
    @TableField("RULE_ACTION")
    private String ruleAction;

    /**
     * 优先级
     */
    @TableField("PRIORITY")
    private Integer priority;

    /**
     * 是否启用(0否1是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR", fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 更新人
     */
    @TableField(value = "UPDATER", fill = FieldFill.INSERT_UPDATE)
    private Long updater;
}
