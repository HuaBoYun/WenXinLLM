package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 坏账准备查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArBadDebtProvisionQueryParam", description = "坏账准备查询参数")
public class ArBadDebtProvisionQueryParam extends PageableParam {

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "计提方法(1账龄分析法 2余额百分比法 3销售百分比法 4个别认定法)")
    private Integer provisionMethod;

    @ApiModelProperty(value = "计提状态(0待确认 1已确认 2已调整)")
    private Integer provisionStatus;

    @ApiModelProperty(value = "计提日期开始")
    private LocalDate provisionDateStart;

    @ApiModelProperty(value = "计提日期结束")
    private LocalDate provisionDateEnd;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;
}

