package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算编制进度VO
 * 
 * @description 预算编制进度汇总
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算编制进度")
public class BudgetPreparationProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织单元ID")
    private String organizationId;

    @ApiModelProperty("组织单元名称")
    private String organizationName;

    @ApiModelProperty("任务总数")
    private Integer totalTasks = 0;

    @ApiModelProperty("已完成任务数")
    private Integer completedTasks = 0;

    @ApiModelProperty("进行中任务数")
    private Integer inProgressTasks = 0;

    @ApiModelProperty("未开始任务数")
    private Integer notStartedTasks = 0;

    @ApiModelProperty("完成率(%)")
    private Double completionRate = 0.0;

    @ApiModelProperty("预算金额")
    private Double budgetAmount = 0.0;

    @ApiModelProperty("已填报金额")
    private Double filledAmount = 0.0;

    @ApiModelProperty("填报率(%)")
    private Double fillRate = 0.0;
}

