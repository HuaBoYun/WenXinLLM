package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 财务雷达图展示对象
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="FinancialRadarVO", description="财务雷达图展示对象")
public class FinancialRadarVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="雷达图数据")
    private RadarData radarData;

    @Schema(name="关键指标列表")
    private List<KeyIndicator> keyIndicators;

    @Data
    @Schema(name="RadarData", description="雷达图数据")
    public static class RadarData implements Serializable {
        
        @Schema(name="维度列表")
        private List<Dimension> dimensions;

        @Schema(name="综合得分")
        private BigDecimal overallScore;

        @Schema(name="综合等级")
        private String overallLevel;
    }

    @Data
    @Schema(name="Dimension", description="雷达图维度")
    public static class Dimension implements Serializable {
        
        @Schema(name="维度编码")
        private String dimension;

        @Schema(name="维度名称")
        private String dimensionName;

        @Schema(name="得分")
        private BigDecimal score;

        @Schema(name="最大分值")
        private BigDecimal maxScore;

        @Schema(name="等级")
        private String level;
    }

    @Data
    @Schema(name="KeyIndicator", description="关键指标")
    public static class KeyIndicator implements Serializable {
        
        @Schema(name="指标编码")
        private String indicatorCode;

        @Schema(name="指标名称")
        private String indicatorName;

        @Schema(name="当前值")
        private BigDecimal currentValue;

        @Schema(name="单位")
        private String unit;

        @Schema(name="变化率")
        private BigDecimal changeRate;

        @Schema(name="变化方向(UP/DOWN/STABLE)")
        private String changeDirection;

        @Schema(name="等级")
        private String level;

        @Schema(name="颜色")
        private String color;
    }
}
