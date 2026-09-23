package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 数据源批量操作参数
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("数据源批量操作参数")
public class DataSourceBatchParam {

    @ApiModelProperty("操作类型：import-导入，export-导出，sync-同步")
    private String operationType;

    @ApiModelProperty("数据源ID列表")
    private List<Long> dataSourceIds;

    @ApiModelProperty("目标配置")
    private Object targetConfig;

    @ApiModelProperty("是否覆盖已有数据")
    private Boolean overwrite = false;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("操作员ID")
    private Long operatorId;
}