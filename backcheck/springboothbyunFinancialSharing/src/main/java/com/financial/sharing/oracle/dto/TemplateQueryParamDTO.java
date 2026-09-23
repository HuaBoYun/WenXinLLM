package com.financial.sharing.oracle.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 模板查询参数DTO
 *
 * @author system
 * @since 2024-12-19
 */
@Data
@ApiModel("模板查询参数DTO")
public class TemplateQueryParamDTO {

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板类型")
    private String templateType;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("创建时间开始")
    private Date createTimeStart;

    @ApiModelProperty("创建时间结束")
    private Date createTimeEnd;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("账簿ID")
    private Long bookId;
}