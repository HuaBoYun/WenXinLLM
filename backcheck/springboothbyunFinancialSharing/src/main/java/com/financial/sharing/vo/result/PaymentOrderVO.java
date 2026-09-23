package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 付款单VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("付款单VO")
public class PaymentOrderVO {

    @ApiModelProperty("付款ID")
    private String paymentId;

    @ApiModelProperty("付款单号")
    private String paymentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("已核销金额")
    private BigDecimal writeOffAmount;

    @ApiModelProperty("未核销金额")
    private BigDecimal remainingAmount;

    @ApiModelProperty("付款状态(0:待付款,1:已付款,2:已核销,3:已取消)")
    private Integer paymentStatus;

    @ApiModelProperty("付款状态名称")
    private String paymentStatusName;

    @ApiModelProperty("付款日期")
    private LocalDate paymentDate;

    @ApiModelProperty("付款方式")
    private Integer paymentMethod;

    @ApiModelProperty("付款方式名称")
    private String paymentMethodName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人姓名")
    private String createByName;
}

