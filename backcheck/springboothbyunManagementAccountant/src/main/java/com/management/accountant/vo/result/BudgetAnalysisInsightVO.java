package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算分析洞察VO
 * 
 * @description 预算分析洞察信息
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算分析洞察")
public class BudgetAnalysisInsightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("洞察ID")
    private String insightId;

    @ApiModelProperty("洞察类型")
    private String type;

    @ApiModelProperty("洞察标题")
    private String title;

    @ApiModelProperty("洞察内容")
    private String content;

    @ApiModelProperty("严重程度")
    private String severity;

    @ApiModelProperty("影响范围")
    private String impact;

    @ApiModelProperty("建议措施")
    private String recommendation;

    @ApiModelProperty("生成时间")
    private String createTime;
}

