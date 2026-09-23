package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算体系活动VO
 * 
 * @description 预算体系最近活动记录
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算体系活动")
public class BudgetSystemActivityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动ID")
    private String activityId;

    @ApiModelProperty("活动类型")
    private String type;

    @ApiModelProperty("活动标题")
    private String title;

    @ApiModelProperty("活动描述")
    private String description;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("活动时间")
    private String time;

    @ApiModelProperty("活动状态")
    private String status;

    @ApiModelProperty("模块名称")
    private String module;
}

