package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算编制统计数据VO
 * 
 * @description 预算编制首页统计数据
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算编制统计数据")
public class BudgetPreparationStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编制任务总数")
    private Integer totalTasks = 0;

    @ApiModelProperty("任务增长率(%)")
    private Double taskGrowth = 0.0;

    @ApiModelProperty("进行中任务数")
    private Integer inProgressTasks = 0;

    @ApiModelProperty("完成率(%)")
    private Double progressRate = 0.0;

    @ApiModelProperty("已完成任务数")
    private Integer completedTasks = 0;

    @ApiModelProperty("预算总金额")
    private Double totalBudgetAmount = 0.0;

    @ApiModelProperty("金额增长率(%)")
    private Double amountGrowth = 0.0;

    @ApiModelProperty("待审批任务数")
    private Integer pendingApprovals = 0;

    @ApiModelProperty("审批减少率(%)")
    private Double approvalDecrease = 0.0;

    @ApiModelProperty("待审批任务数(旧字段兼容)")
    private Integer pendingApprovalTasks = 0;

    @ApiModelProperty("预算版本总数")
    private Integer totalVersions = 0;

    @ApiModelProperty("当前版本数")
    private Integer currentVersions = 0;

    @ApiModelProperty("预算模板总数")
    private Integer totalTemplates = 0;

    @ApiModelProperty("使用中模板数")
    private Integer activeTemplates = 0;

    @ApiModelProperty("总体完成率(%)")
    private Double completionRate = 0.0;

    @ApiModelProperty("按时完成率(%)")
    private Double onTimeRate = 0.0;
}

