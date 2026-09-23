package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 坏账核销查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArBadDebtWriteOffQueryParam", description = "坏账核销查询参数")
public class ArBadDebtWriteOffQueryParam extends PageableParam {

    @ApiModelProperty(value = "核销单号")
    private String writeOffNo;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "核销状态(0待审核 1已审核 2已核销 3已拒绝)")
    private Integer writeOffStatus;

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

