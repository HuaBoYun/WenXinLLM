package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 转移记录查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "TransferRecordQueryParam", description = "转移记录查询参数")
public class TransferRecordQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "源组织ID")
    private String fromOrgId;

    @ApiModelProperty(value = "源科目编码")
    private String fromSubjectCode;

    @ApiModelProperty(value = "目标组织ID")
    private String toOrgId;

    @ApiModelProperty(value = "目标科目编码")
    private String toSubjectCode;

    @ApiModelProperty(value = "期间")
    private String period;

    @ApiModelProperty(value = "开始期间")
    private String startPeriod;

    @ApiModelProperty(value = "结束期间")
    private String endPeriod;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据ID")
    private String sourceDocId;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize = 10;
}

