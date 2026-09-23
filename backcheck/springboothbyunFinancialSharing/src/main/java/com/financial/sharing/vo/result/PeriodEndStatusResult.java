package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 期末处理状态结果
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
@ApiModel("期末处理状态结果")
public class PeriodEndStatusResult {

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "期间状态")
    private String periodStatus; // OPEN, PROCESSING, CLOSED

    @ApiModelProperty(value = "期间状态名称")
    private String periodStatusName;

    @ApiModelProperty(value = "检查状态")
    private String checkStatus; // PENDING, COMPLETED, FAILED

    @ApiModelProperty(value = "检查状态名称")
    private String checkStatusName;

    @ApiModelProperty(value = "损益结转状态")
    private String carryForwardStatus; // PENDING, COMPLETED, FAILED

    @ApiModelProperty(value = "损益结转状态名称")
    private String carryForwardStatusName;

    @ApiModelProperty(value = "结账状态")
    private String closingStatus; // PENDING, COMPLETED, FAILED

    @ApiModelProperty(value = "结账状态名称")
    private String closingStatusName;

    @ApiModelProperty(value = "是否可以损益结转")
    private Boolean canCarryForward;

    @ApiModelProperty(value = "是否可以结账")
    private Boolean canClose;

    @ApiModelProperty(value = "是否可以反结账")
    private Boolean canReverseClosing;

    @ApiModelProperty(value = "自动转账任务数")
    private Integer autoTransferTaskCount;

    @ApiModelProperty(value = "已完成自动转账任务数")
    private Integer completedTransferTaskCount;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "进度百分比")
    private Double progressPercentage;
}