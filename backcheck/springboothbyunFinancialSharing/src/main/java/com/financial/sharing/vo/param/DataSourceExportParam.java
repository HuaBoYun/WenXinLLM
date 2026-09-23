package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据源导出参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("数据源导出参数")
public class DataSourceExportParam {

    @ApiModelProperty("数据源ID")
    private Long dataSourceId;

    @ApiModelProperty("导出格式：excel,csv,json")
    private String exportFormat = "excel";

    @ApiModelProperty("导出条件")
    private String exportCondition;

    @ApiModelProperty("字段列表")
    private List<String> fieldList;

    @ApiModelProperty("排序字段")
    private String sortField;

    @ApiModelProperty("排序方向：asc,desc")
    private String sortDirection = "asc";

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("操作员ID")
    private Long operatorId;
}