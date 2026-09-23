package com.financial.sharing.dataCollection.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据源配置查询参数
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "DataSourceQueryParam", description = "数据源配置查询参数")
public class DataSourceQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据源编码")
    private String sourceCode;

    @ApiModelProperty(value = "数据源名称")
    private String sourceName;

    @ApiModelProperty(value = "数据源类型")
    private String sourceType;

    @ApiModelProperty(value = "是否启用")
    private String isEnabled;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize = 10;
}

