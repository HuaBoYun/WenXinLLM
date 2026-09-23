package com.management.accountant.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算任务DTO
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Data
@ApiModel("预算任务DTO")
public class BudgetTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务编码")
    private String taskCode;

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("预算ID")
    private String budgetId;

    @ApiModelProperty("预算名称")
    private String budgetName;

    @ApiModelProperty("负责人ID")
    private String assignee;

    @ApiModelProperty("负责人姓名")
    private String assigneeName;

    @ApiModelProperty("分配时间")
    private Date assignTime;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("进度")
    private Integer progress;

    @ApiModelProperty("开始日期")
    private Date startDate;

    @ApiModelProperty("截止日期")
    private Date dueDate;

    @ApiModelProperty("完成时间")
    private Date completeTime;

    @ApiModelProperty("任务结果")
    private String taskResult;

    @ApiModelProperty("委派给(用户ID)")
    private String delegatedTo;

    @ApiModelProperty("委派给(用户姓名)")
    private String delegatedToName;

    @ApiModelProperty("委派时间")
    private Date delegateTime;

    @ApiModelProperty("委派原因")
    private String delegateReason;

    @ApiModelProperty("父任务ID")
    private String parentTaskId;

    @ApiModelProperty("任务配置")
    private String taskConfig;

    @ApiModelProperty("附件信息")
    private String attachments;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公司ID")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;
}

