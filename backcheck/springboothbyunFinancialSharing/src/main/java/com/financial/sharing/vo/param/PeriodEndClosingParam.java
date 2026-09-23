package com.financial.sharing.vo.param;

import lombok.Data;

/**
 * 期末结账参数
 *
 * @author system
 * @since 2024-12-08
 */
@Data
public class PeriodEndClosingParam {

    /**
     * 结账期间
     */
    private String closingPeriod;

    /**
     * 结账类型：MONTHLY-月结，QUARTERLY-季结，YEARLY-年结
     */
    private String closingType;

    /**
     * 结账说明
     */
    private String closingDescription;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 结账记录ID（用于反结账）
     */
    private String closingId;

    /**
     * 反结账原因
     */
    private String reason;

    /**
     * 操作类型：CLOSE-结账，REVERSE-反结账
     */
    private String operationType;
}