package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源预览参数
 */
@Data
@ApiModel("数据源预览参数")
public class DataSourcePreviewParam {

    @ApiModelProperty("配置ID")
    private Long configId;

    @ApiModelProperty("预览行数（默认10行）")
    private Integer limit = 10;

    @ApiModelProperty("过滤条件")
    private String filterCondition;

    @ApiModelProperty("排序字段")
    private String sortField;

    @ApiModelProperty("排序方向（ASC/DESC）")
    private String sortDirection;
}