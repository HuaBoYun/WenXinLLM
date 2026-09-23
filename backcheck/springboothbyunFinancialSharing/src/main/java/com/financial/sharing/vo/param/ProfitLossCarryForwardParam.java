package com.financial.sharing.vo.param;

import lombok.Data;

/**
 * 损益结转参数
 *
 * @author system
 * @since 2024-12-08
 */
@Data
public class ProfitLossCarryForwardParam {

    /**
     * 结转期间
     */
    private String carryForwardPeriod;

    /**
     * 结转方式：ACCOUNT_BASED-科目法，STATEMENT_BASED-报表法
     */
    private String carryForwardMethod;

    /**
     * 本年利润科目代码
     */
    private String currentYearProfitSubject;

    /**
     * 凭证类型：GENERAL-通用，TRANSFER-转账，CARRY_FORWARD-结转
     */
    private String voucherType;

    /**
     * 结转说明
     */
    private String carryForwardDescription;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 结转ID（用于反结转）
     */
    private String carryForwardId;

    /**
     * 反结转原因
     */
    private String reason;
}