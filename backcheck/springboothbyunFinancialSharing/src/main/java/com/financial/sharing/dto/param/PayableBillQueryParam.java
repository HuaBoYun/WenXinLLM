package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应付票据查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付票据查询参数")
public class PayableBillQueryParam {

    @ApiModelProperty("当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 15;

    @ApiModelProperty("票据号")
    private String billNo;

    @ApiModelProperty("票据类型(bank_acceptance/commercial_acceptance/check/promissory_note)")
    private String billType;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称(模糊查询)")
    private String supplierName;

    @ApiModelProperty("状态(active/due/paid/endorsed/cancelled)")
    private String status;

    @ApiModelProperty("出票日期开始")
    private LocalDate issueDateStart;

    @ApiModelProperty("出票日期结束")
    private LocalDate issueDateEnd;

    @ApiModelProperty("到期日期开始")
    private LocalDate dueDateStart;

    @ApiModelProperty("到期日期结束")
    private LocalDate dueDateEnd;

    @ApiModelProperty("最小金额")
    private BigDecimal minAmount;

    @ApiModelProperty("最大金额")
    private BigDecimal maxAmount;

    @ApiModelProperty("承兑银行")
    private String bankName;

    @ApiModelProperty("关键字(票据号/供应商名称)")
    private String keyword;
}

