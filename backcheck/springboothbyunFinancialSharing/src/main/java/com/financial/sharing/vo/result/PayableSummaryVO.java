package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 应付汇总VO
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付汇总VO")
public class PayableSummaryVO {

    @ApiModelProperty("总应付金额")
    private BigDecimal totalPayable;

    @ApiModelProperty("已付金额")
    private BigDecimal totalPaid;

    @ApiModelProperty("未付金额")
    private BigDecimal totalUnpaid;

    @ApiModelProperty("逾期金额")
    private BigDecimal overdueAmount;

    @ApiModelProperty("本月应付")
    private BigDecimal monthPayable;

    @ApiModelProperty("本月已付")
    private BigDecimal monthPaid;

    @ApiModelProperty("待审核单据数")
    private Integer pendingAuditCount;

    @ApiModelProperty("逾期单据数")
    private Integer overdueCount;

    @ApiModelProperty("供应商总数")
    private Integer supplierCount;

    @ApiModelProperty("本月新增供应商数")
    private Integer newSupplierCount;

    @ApiModelProperty("票据总金额")
    private BigDecimal totalBillAmount;

    @ApiModelProperty("即将到期票据金额")
    private BigDecimal dueSoonBillAmount;
}

