package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 规则凭证查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RuleVoucherQueryParam extends PageableParam {

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型(1期末损益结转2普通规则凭证)
     */
    private Integer ruleType;

    /**
     * 执行期间
     */
    private String executionPeriod;

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
