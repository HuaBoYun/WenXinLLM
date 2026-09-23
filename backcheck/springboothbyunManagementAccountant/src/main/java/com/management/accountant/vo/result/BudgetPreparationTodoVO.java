package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算编制待办事项VO
 * 
 * @description 预算编制待办任务
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算编制待办事项")
public class BudgetPreparationTodoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("待办ID")
    private String todoId;

    @ApiModelProperty("待办类型")
    private String type;

    @ApiModelProperty("待办标题")
    private String title;

    @ApiModelProperty("待办描述")
    private String description;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("截止时间(deadline)")
    private String deadline;

    @ApiModelProperty("截止时间(dueDate，与deadline同值，兼容前端)")
    private String dueDate;

    @ApiModelProperty("任务状态")
    private String status;

    @ApiModelProperty("负责人")
    private String assignee;

    @ApiModelProperty("关联任务ID")
    private String taskId;

    @ApiModelProperty("关联任务名称")
    private String taskName;
}

