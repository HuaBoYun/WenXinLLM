package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字段映射保存参数
 */
@Data
@ApiModel("字段映射保存参数")
public class FieldMappingSaveParam implements Serializable {

    @ApiModelProperty(value = "租户ID", hidden = true)
    @JsonIgnore
    private Long tenantId;

    @ApiModelProperty(value = "创建用户ID", hidden = true)
    @JsonIgnore
    private Long createUser;

    @ApiModelProperty(value = "更新用户ID", hidden = true)
    @JsonIgnore
    private Long updateUser;

    @ApiModelProperty(value = "映射ID，新增时为空", example = "1")
    private Long mappingId;

    @ApiModelProperty(value = "数据源ID", example = "1", required = true)
    @NotNull(message = "数据源ID不能为空")
    private Long dataSourceId;

    @ApiModelProperty(value = "目标表名", example = "TBL_FINANCIAL_DATA", required = true)
    @NotBlank(message = "目标表名不能为空")
    private String targetTable;

    @ApiModelProperty(value = "源字段名", example = "amount", required = true)
    @NotBlank(message = "源字段名不能为空")
    private String sourceField;

    @ApiModelProperty(value = "目标字段名", example = "AMOUNT", required = true)
    @NotBlank(message = "目标字段名不能为空")
    private String targetField;

    @ApiModelProperty(value = "字段类型", example = "DECIMAL")
    private String fieldType;

    @ApiModelProperty(value = "字段长度", example = "20")
    private Integer fieldLength;

    @ApiModelProperty(value = "是否可为空", example = "true")
    private Boolean isNullable = true;

    @ApiModelProperty(value = "默认值", example = "0")
    private String defaultValue;

    @ApiModelProperty(value = "转换规则", example = "UPPER(SOURCE_FIELD)")
    private String transformationRule;

    @ApiModelProperty(value = "验证规则", example = "NUMERIC")
    private String validationRule;

    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean isEnabled = true;

    @ApiModelProperty(value = "描述", example = "金额字段映射")
    private String description;
}