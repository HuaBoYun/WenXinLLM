package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 释放记录查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "ReleaseRecordQueryParam", description = "释放记录查询参数")
public class ReleaseRecordQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务组织ID")
    private String bizOrgId;

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

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

