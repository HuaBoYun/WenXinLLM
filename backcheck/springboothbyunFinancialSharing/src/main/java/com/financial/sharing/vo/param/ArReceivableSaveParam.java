package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收单据保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArReceivableSaveParam", description = "应收单据保存参数")
public class ArReceivableSaveParam {

    @ApiModelProperty(value = "应收单据ID（更新时必填）")
    private String receivableId;

    @ApiModelProperty(value = "单据编号")
    @Size(max = 50, message = "单据编号长度不能超过50个字符")
    private String documentNo;

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "业务类型(1销售收入 2服务收入 3租赁收入 4其他收入)", required = true)
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

    @ApiModelProperty(value = "应收金额", required = true)
    @NotNull(message = "应收金额不能为空")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "到期日期", required = true)
    @NotNull(message = "到期日期不能为空")
    private LocalDate dueDate;

    @ApiModelProperty(value = "币种")
    @Size(max = 10, message = "币种长度不能超过10个字符")
    private String currency;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "备注")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remarks;

    @ApiModelProperty(value = "单据状态(0草稿 1待审核 2已审核 3已拒绝)")
    private Integer documentStatus;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

