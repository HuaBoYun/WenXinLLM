package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 映射配置查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "MappingConfigQueryParam", description = "映射配置查询参数")
public class MappingConfigQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "映射编码")
    private String mappingCode;

    @ApiModelProperty(value = "映射名称")
    private String mappingName;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "映射类型")
    private String mappingType;

    @ApiModelProperty(value = "是否启用")
    private String isEnabled;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}

