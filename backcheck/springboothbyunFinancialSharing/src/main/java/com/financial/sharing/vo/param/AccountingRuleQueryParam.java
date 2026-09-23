package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会计规则查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountingRuleQueryParam extends PageableParam {

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
     * 适用事项类型
     */
    private String transactionType;

    /**
     * 是否启用(0否1是)
     */
    private Integer isEnabled;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;
}
