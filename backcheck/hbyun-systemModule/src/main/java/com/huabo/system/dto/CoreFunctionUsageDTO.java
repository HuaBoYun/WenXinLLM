package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 核心功能使用率DTO
 */
@Data
public class CoreFunctionUsageDTO {

    /**
     * 风险模块访问次数
     */
    private BigDecimal riskCount;

    /**
     * 内控模块访问次数
     */
    private BigDecimal controlCount;

    /**
     * 审计模块访问次数
     */
    private BigDecimal auditCount;

    /**
     * 整改模块访问次数
     */
    private BigDecimal rectifyCount;
}
