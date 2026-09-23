package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 融资配置DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@ApiModel(value = "FinancingConfigDTO", description = "融资配置数据传输对象")
@Data
public class FinancingConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("参数ID")
    private Long paramId;

    @ApiModelProperty("参数类型")
    private String paramType;

    @ApiModelProperty("参数名称")
    private String paramName;

    @ApiModelProperty("参数值")
    private String paramValue;

    @ApiModelProperty("参数描述")
    private String description;

    @ApiModelProperty("状态(ENABLE-启用,DISABLE-禁用)")
    private String status;

    @ApiModelProperty("备注")
    private String remark;

    // Getter和Setter
    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
