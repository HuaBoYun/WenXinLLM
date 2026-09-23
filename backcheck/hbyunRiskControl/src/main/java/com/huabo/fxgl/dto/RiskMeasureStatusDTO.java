package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 风险措施状态 DTO
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
public class RiskMeasureStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 未完成措施数量
     */
    private Integer nullRisklevelCount;

    /**
     * 完成措施数量
     */
    private Integer notNullRisklevelCount;
}

