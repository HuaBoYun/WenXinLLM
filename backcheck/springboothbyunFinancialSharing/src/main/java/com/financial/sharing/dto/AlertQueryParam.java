package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 异常预警查询参数
 */
@Data
@ApiModel("异常预警查询参数")
public class AlertQueryParam {

    @ApiModelProperty("账套ID")
    private Integer bookId;

    @ApiModelProperty("租户ID")
    private Integer tenantId;

    @ApiModelProperty("预警类型: all/budget_exceed/cost_anomaly")
    private String alertType;

    @ApiModelProperty("预警级别: all/high/medium/low")
    private String alertLevel;

    @ApiModelProperty("页码")
    private Integer pageNumber;

    @ApiModelProperty("页大小")
    private Integer pageSize;
}
