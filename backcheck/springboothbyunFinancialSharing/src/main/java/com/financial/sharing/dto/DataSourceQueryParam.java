package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源查询参数
 */
@Data
@ApiModel("数据源查询参数")
public class DataSourceQueryParam {

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置代码")
    private String configCode;

    @ApiModelProperty("数据源类型")
    private String dataSourceType;

    @ApiModelProperty("是否启用")
    private Boolean isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 10;
}