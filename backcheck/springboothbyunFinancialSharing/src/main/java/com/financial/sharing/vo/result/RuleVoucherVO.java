package com.financial.sharing.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则凭证返回对象
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class RuleVoucherVO {

    /**
     * 规则凭证ID
     */
    private Long ruleVoucherId;

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
     * 规则类型名称
     */
    private String ruleTypeName;

    /**
     * 凭证模板(JSON格式)
     */
    private String voucherTemplate;

    /**
     * 执行期间
     */
    private String executionPeriod;

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
