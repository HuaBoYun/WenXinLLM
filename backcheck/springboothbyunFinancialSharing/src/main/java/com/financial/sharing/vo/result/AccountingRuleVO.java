package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会计规则返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountingRuleVO {

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型(1分录规则2分发规则3转换规则)
     */
    private Integer ruleType;

    /**
     * 规则类型名称
     */
    private String ruleTypeName;

    /**
     * 适用事项类型
     */
    private String transactionType;

    /**
     * 适用事项类型名称
     */
    private String transactionTypeName;

    /**
     * 规则条件(JSON格式)
     */
    private String ruleCondition;

    /**
     * 规则动作(JSON格式)
     */
    private String ruleAction;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 是否启用名称
     */
    private String isEnabledName;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;
}
