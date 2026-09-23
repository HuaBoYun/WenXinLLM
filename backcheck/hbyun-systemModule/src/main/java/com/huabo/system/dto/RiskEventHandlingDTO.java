package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 风险事件处理DTO
 */
@Data
public class RiskEventHandlingDTO {

    /**
     * 状态名称
     */
    private String name;

    /**
     * 数量
     */
    private BigDecimal value;
}
