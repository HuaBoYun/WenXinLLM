package com.financial.sharing.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应付单据保存参数
 * @author system
 * @since 2025-01-05
 */
@Data
@ApiModel("应付单据保存参数")
public class PayableDocumentSaveParam {

    @ApiModelProperty("单据ID(新增时为空)")
    private String documentId;

    @ApiModelProperty("单据编号")
    private String documentNo;

    @NotBlank(message = "供应商ID不能为空")
    @ApiModelProperty(value = "供应商ID", required = true)
    private String supplierId;

    @NotNull(message = "应付金额不能为空")
    @ApiModelProperty(value = "应付金额", required = true)
    private BigDecimal payableAmount;

    @ApiModelProperty("已付金额")
    private BigDecimal paidAmount;

    @ApiModelProperty("到期日期")
    private LocalDate dueDate;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("汇率")
    private BigDecimal exchangeRate;

    @NotNull(message = "业务类型不能为空")
    @ApiModelProperty(value = "业务类型(1:采购应付,2:费用应付,3:其他应付)", required = true)
    private Integer businessType;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("备注")
    private String remarks;
}

