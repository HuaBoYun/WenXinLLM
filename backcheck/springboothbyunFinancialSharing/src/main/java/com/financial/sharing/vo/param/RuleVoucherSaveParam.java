package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 规则凭证保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class RuleVoucherSaveParam {

    /**
     * 规则凭证ID（更新时必填）
     */
    private Long ruleVoucherId;

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
     * 规则类型(1期末损益结转2普通规则凭证)
     */
    @NotNull(message = "规则类型不能为空")
    private Integer ruleType;

    /**
     * 凭证模板(JSON格式)
     */
    private String voucherTemplate;

    /**
     * 执行期间
     */
    @Size(max = 10, message = "执行期间长度不能超过10个字符")
    private String executionPeriod;

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
