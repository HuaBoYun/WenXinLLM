package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算控制健康度VO
 * 
 * @description 预算控制健康度评估数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算控制健康度")
public class BudgetControlHealthVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总体评分")
    private Double overallScore = 0.0;

    @ApiModelProperty("健康度详情")
    private List<HealthMetric> metrics = new ArrayList<>();

    @Data
    @ApiModel("健康度指标")
    public static class HealthMetric implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("指标标识")
        private String key;

        @ApiModelProperty("指标名称")
        private String label;

        @ApiModelProperty("指标值")
        private Double value = 0.0;

        @ApiModelProperty("指标单位")
        private String unit;

        @ApiModelProperty("指标状态")
        private String status;
    }
}

