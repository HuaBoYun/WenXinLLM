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
 * 坏账回收保存参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArBadDebtRecoverySaveParam", description = "坏账回收保存参数")
public class ArBadDebtRecoverySaveParam {

    @ApiModelProperty(value = "回收ID（更新时必填）")
    private String recoveryId;

    @ApiModelProperty(value = "原核销ID", required = true)
    @NotBlank(message = "原核销ID不能为空")
    private String writeOffId;

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "回收金额", required = true)
    @NotNull(message = "回收金额不能为空")
    private BigDecimal recoveryAmount;

    @ApiModelProperty(value = "回收方式(1现金回收 2以物抵债 3债务重组 4法院执行)", required = true)
    @NotNull(message = "回收方式不能为空")
    private Integer recoveryMethod;

    @ApiModelProperty(value = "回收日期")
    private LocalDate recoveryDate;

    @ApiModelProperty(value = "备注")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remarks;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "操作人ID")
    private String operatorId;
}

