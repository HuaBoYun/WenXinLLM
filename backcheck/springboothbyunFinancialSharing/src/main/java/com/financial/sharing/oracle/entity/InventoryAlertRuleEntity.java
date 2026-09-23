package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警规则表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_ALERT_RULE")
public class InventoryAlertRuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private Long ruleId;

    @TableField("RULE_NAME")
    private String ruleName;

    @TableField("RULE_TYPE")
    private String ruleType;

    @TableField("RULE_TYPE_NAME")
    private String ruleTypeName;

    @TableField("CATEGORY_ID")
    private Long categoryId;

    @TableField("INVENTORY_ID")
    private Long inventoryId;

    @TableField("ALERT_CONDITION")
    private String alertCondition;

    @TableField("UPPER_LIMIT")
    private BigDecimal upperLimit;

    @TableField("LOWER_LIMIT")
    private BigDecimal lowerLimit;

    @TableField("STAGNANT_DAYS")
    private Integer stagnantDays;

    @TableField("ALERT_LEVEL")
    private Integer alertLevel;

    @TableField("NOTIFY_METHOD")
    private String notifyMethod;

    @TableField("NOTIFY_RECIPIENTS")
    private String notifyRecipients;

    @TableField("STATUS")
    private Integer status;

    @TableField("REMARK")
    private String remark;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATER_ID", fill = FieldFill.INSERT_UPDATE)
    private String updaterId;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("DELETED")
    @TableLogic
    private Integer deleted;
}

