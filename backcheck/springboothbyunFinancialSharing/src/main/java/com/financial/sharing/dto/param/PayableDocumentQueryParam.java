package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应付单据查询参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付单据查询参数")
public class PayableDocumentQueryParam {

    @ApiModelProperty("当前页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 15;

    @ApiModelProperty("单据编号")
    private String documentNo;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商名称(模糊查询)")
    private String supplierName;

    @ApiModelProperty("单据状态(0:草稿,1:待审核,2:已审核,3:已拒绝)")
    private Integer documentStatus;

    @ApiModelProperty("业务类型(1:采购应付,2:费用应付,3:其他应付)")
    private Integer businessType;

    @ApiModelProperty("到期日期开始")
    private LocalDate dueDateStart;

    @ApiModelProperty("到期日期结束")
    private LocalDate dueDateEnd;

    @ApiModelProperty("创建日期开始")
    private LocalDate createDateStart;

    @ApiModelProperty("创建日期结束")
    private LocalDate createDateEnd;

    @ApiModelProperty("最小应付金额")
    private BigDecimal minAmount;

    @ApiModelProperty("最大应付金额")
    private BigDecimal maxAmount;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("关键字(单据编号/供应商名称)")
    private String keyword;
}

