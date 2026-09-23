package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字段映射查询参数
 */
@Data
@ApiModel("字段映射查询参数")
public class FieldMappingParam implements Serializable {

    @ApiModelProperty(value = "租户ID", hidden = true)
    @JsonIgnore
    private Long tenantId;

    @ApiModelProperty(value = "映射ID", example = "1")
    private Long mappingId;

    @ApiModelProperty(value = "数据源ID", example = "1")
    private Long dataSourceId;

    @ApiModelProperty(value = "目标表名", example = "TBL_FINANCIAL_DATA")
    private String targetTable;

    @ApiModelProperty(value = "源字段名", example = "amount")
    private String sourceField;

    @ApiModelProperty(value = "目标字段名", example = "AMOUNT")
    private String targetField;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean isEnabled;

    @ApiModelProperty(value = "页码", example = "1")
    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    @NotNull(message = "每页大小不能为空")
    private Integer pageSize = 10;
}