package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 风险审查情况分析 DTO
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
public class RiskReviewAnalysisDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织名称
     */
    private String orgname;

    /**
     * 风险审查数量
     */
    private Integer riskReviewCount;
}

