package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 会计规则保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class AccountingRuleSaveParam {

    /**
     * 规则ID（更新时必填）
     */
    private Long ruleId;

    /**
     * 规则编码
     */
    @NotBlank(message = "规则编码不能为空")
    @Size(max = 50, message = "规则编码长度不能超过50个字符")
    private String ruleCode;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    @Size(max = 200, message = "规则名称长度不能超过200个字符")
    private String ruleName;

    /**
     * 规则类型(1分录规则2分发规则3转换规则)
     */
    @NotNull(message = "规则类型不能为空")
    private Integer ruleType;

    /**
     * 适用事项类型
     */
    @NotBlank(message = "适用事项类型不能为空")
    @Size(max = 50, message = "适用事项类型长度不能超过50个字符")
    private String transactionType;

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
     * 账簿ID
     */
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
