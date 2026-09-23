package com.huabo.system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 风险内控审计维度DTO
 */
@Data
public class RiskAuditDimensionDTO {

    /**
     * X轴年月数据
     */
    private List<String> categories;

    /**
     * 系列数据
     */
    private List<SeriesData> series;

    @Data
    public static class SeriesData {
        /**
         * 系列名称
         */
        private String name;

        /**
         * 数值数组
         */
        private List<BigDecimal> data;
    }
}
