package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源导入参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("数据源导入参数")
public class DataSourceImportParam {

    @ApiModelProperty("数据源ID")
    private Long dataSourceId;

    @ApiModelProperty("导入文件路径")
    private String filePath;

    @ApiModelProperty("导入类型：full-全量，incremental-增量")
    private String importType = "full";

    @ApiModelProperty("是否验证数据")
    private Boolean validateData = true;

    @ApiModelProperty("失败时是否回滚")
    private Boolean rollbackOnError = true;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("操作员ID")
    private Long operatorId;
}