package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 收款单查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArReceiptQueryParam", description = "收款单查询参数")
public class ArReceiptQueryParam extends PageableParam {

    @ApiModelProperty(value = "收款单号")
    private String receiptNo;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "收款状态(0待收款 1已收款 2已核销 3已取消)")
    private Integer receiptStatus;

    @ApiModelProperty(value = "收款方式(1现金 2银行转账 3支票 4承兑汇票)")
    private Integer paymentMethod;

    @ApiModelProperty(value = "收款日期开始")
    private LocalDate receiptDateStart;

    @ApiModelProperty(value = "收款日期结束")
    private LocalDate receiptDateEnd;

    @ApiModelProperty(value = "创建日期开始")
    private LocalDate createDateStart;

    @ApiModelProperty(value = "创建日期结束")
    private LocalDate createDateEnd;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

