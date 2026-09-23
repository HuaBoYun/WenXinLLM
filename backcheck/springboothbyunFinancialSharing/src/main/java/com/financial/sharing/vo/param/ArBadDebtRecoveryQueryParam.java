package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 坏账回收查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArBadDebtRecoveryQueryParam", description = "坏账回收查询参数")
public class ArBadDebtRecoveryQueryParam extends PageableParam {

    @ApiModelProperty(value = "回收单号")
    private String recoveryNo;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "回收方式(1现金回收 2以物抵债 3债务重组 4法院执行)")
    private Integer recoveryMethod;

    @ApiModelProperty(value = "回收日期开始")
    private LocalDate recoveryDateStart;

    @ApiModelProperty(value = "回收日期结束")
    private LocalDate recoveryDateEnd;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

