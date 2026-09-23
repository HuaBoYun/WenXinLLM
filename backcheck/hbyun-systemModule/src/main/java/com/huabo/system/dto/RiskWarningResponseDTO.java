package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 风险预警响应率DTO
 */
@Data
public class RiskWarningResponseDTO {

    /**
     * 状态列表
     */
    private List<StatusItem> list;

    @Data
    public static class StatusItem {
        /**
         * 状态
         */
        private Integer s;

        /**
         * 数量
         */
        private BigDecimal c;
    }
}
