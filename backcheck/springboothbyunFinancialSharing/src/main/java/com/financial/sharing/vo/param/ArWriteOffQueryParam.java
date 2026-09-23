package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 核销记录查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArWriteOffQueryParam", description = "核销记录查询参数")
public class ArWriteOffQueryParam extends PageableParam {

    @ApiModelProperty(value = "收款单ID")
    private String receiptId;

    @ApiModelProperty(value = "应收单ID")
    private String receivableId;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "核销方式(1全额核销 2部分核销)")
    private Integer writeOffType;

    @ApiModelProperty(value = "核销日期开始")
    private LocalDate writeOffDateStart;

    @ApiModelProperty(value = "核销日期结束")
    private LocalDate writeOffDateEnd;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

