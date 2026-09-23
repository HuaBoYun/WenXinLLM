package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算体系健康度VO
 * 
 * @description 预算体系健康度评估数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算体系健康度")
public class BudgetSystemHealthVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总体评分")
    private Double overallScore = 0.0;

    @ApiModelProperty("健康度详情")
    private List<HealthDetail> details = new ArrayList<>();

    @Data
    @ApiModel("健康度详情")
    public static class HealthDetail implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("指标标识")
        private String key;

        @ApiModelProperty("指标名称")
        private String label;

        @ApiModelProperty("指标得分")
        private Double score = 0.0;
    }
}

