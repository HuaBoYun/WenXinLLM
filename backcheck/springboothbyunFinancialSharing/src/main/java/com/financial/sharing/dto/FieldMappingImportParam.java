package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字段映射导入参数
 */
@Data
@ApiModel("字段映射导入参数")
public class FieldMappingImportParam implements Serializable {

    @ApiModelProperty(value = "租户ID", hidden = true)
    @JsonIgnore
    private Long tenantId;

    @ApiModelProperty(value = "创建用户ID", hidden = true)
    @JsonIgnore
    private Long createUser;

    @ApiModelProperty(value = "数据源ID", example = "1", required = true)
    @NotNull(message = "数据源ID不能为空")
    private Long dataSourceId;

    @ApiModelProperty(value = "目标表名", example = "TBL_FINANCIAL_DATA")
    private String targetTable;

    @ApiModelProperty(value = "是否覆盖现有映射", example = "false")
    private Boolean overwrite = false;

    @ApiModelProperty(value = "导入数据", required = true)
    @NotNull(message = "导入数据不能为空")
    private Object importData;
}