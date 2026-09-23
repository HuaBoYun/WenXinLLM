package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预算执行查询参数
 */
@Data
@ApiModel("预算执行查询参数")
public class BudgetExecutionQueryParam {

    @ApiModelProperty("账套ID")
    private Integer bookId;

    @ApiModelProperty("租户ID")
    private Integer tenantId;

    @ApiModelProperty("期间(YYYY-MM)")
    private String period;

    @ApiModelProperty("中心类型: all/production/management/sales")
    private String centerType;

    @ApiModelProperty("前N个")
    private Integer topN;
}
