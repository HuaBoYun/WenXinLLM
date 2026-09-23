package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 账龄分析查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@ApiModel(value = "ArAgingAnalysisQueryParam", description = "账龄分析查询参数")
public class ArAgingAnalysisQueryParam {

    @ApiModelProperty(value = "分析日期", required = true)
    @NotNull(message = "分析日期不能为空")
    private LocalDate analysisDate;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "账龄区间(0-30/31-60/61-90/91-180/180+)")
    private String agingRange;

    @ApiModelProperty(value = "风险等级(1低 2中 3高 4极高)")
    private Integer riskLevel;

    @ApiModelProperty(value = "是否仅显示逾期")
    private Boolean onlyOverdue;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

