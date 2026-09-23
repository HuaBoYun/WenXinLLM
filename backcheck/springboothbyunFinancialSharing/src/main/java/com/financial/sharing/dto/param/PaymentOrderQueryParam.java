package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 付款单查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("付款单查询参数")
public class PaymentOrderQueryParam {

    @ApiModelProperty("当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 15;

    @ApiModelProperty("付款单号")
    private String paymentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称(模糊查询)")
    private String supplierName;

    @ApiModelProperty("付款状态(0:待付款,1:已付款,2:已核销,3:已取消)")
    private Integer paymentStatus;

    @ApiModelProperty("付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票)")
    private Integer paymentMethod;

    @ApiModelProperty("付款日期开始")
    private LocalDate paymentDateStart;

    @ApiModelProperty("付款日期结束")
    private LocalDate paymentDateEnd;

    @ApiModelProperty("最小付款金额")
    private BigDecimal minAmount;

    @ApiModelProperty("最大付款金额")
    private BigDecimal maxAmount;

    @ApiModelProperty("关键字(付款单号/供应商名称)")
    private String keyword;
}

