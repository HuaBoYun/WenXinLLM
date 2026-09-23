package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 融资配置查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@ApiModel(value = "FinancingConfigQueryDTO", description = "融资配置查询条件")
@Data
public class FinancingConfigQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("参数类型")
    private String paramType;

    @ApiModelProperty("参数名称")
    private String paramName;

    @ApiModelProperty("状态(ENABLE-启用,DISABLE-禁用)")
    private String status;

    @ApiModelProperty("参数值")
    private String paramValue;

    // Getter和Setter
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
