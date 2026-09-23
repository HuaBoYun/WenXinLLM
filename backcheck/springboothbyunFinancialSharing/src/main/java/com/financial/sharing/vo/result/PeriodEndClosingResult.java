package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 期末结账结果
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
@ApiModel("期末结账结果")
public class PeriodEndClosingResult {

    @ApiModelProperty(value = "结账记录ID")
    private String closingId;

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "结账类型")
    private String closingType; // MONTHLY, QUARTERLY, YEARLY

    @ApiModelProperty(value = "结账期间")
    private String closingPeriod;

    @ApiModelProperty(value = "结账状态")
    private String closingStatus; // PENDING, PROCESSING, COMPLETED, FAILED

    @ApiModelProperty(value = "结账时间")
    private Date closingTime;

    @ApiModelProperty(value = "结账人ID")
    private Long closingUserId;

    @ApiModelProperty(value = "结账人姓名")
    private String closingUserName;

    @ApiModelProperty(value = "结账说明")
    private String closingDescription;

    @ApiModelProperty(value = "凭证数量")
    private Integer voucherCount;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "处理时长（秒）")
    private Long duration;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "反结账原因")
    private String reverseReason;

    @ApiModelProperty(value = "反结账时间")
    private Date reverseTime;

    @ApiModelProperty(value = "反结账人ID")
    private Long reverseUserId;

    @ApiModelProperty(value = "反结账人姓名")
    private String reverseUserName;
}