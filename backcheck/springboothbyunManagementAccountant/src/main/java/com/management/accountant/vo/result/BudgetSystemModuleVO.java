package com.management.accountant.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预算体系模块VO
 * 
 * @description 预算体系功能模块信息
 * @author AI Assistant
 * @date 2025-01-30
 */
@Data
@ApiModel("预算体系模块")
public class BudgetSystemModuleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模块标识")
    private String key;

    @ApiModelProperty("模块标题")
    private String title;

    @ApiModelProperty("模块描述")
    private String description;

    @ApiModelProperty("模块图标")
    private String icon;

    @ApiModelProperty("数据项数量")
    private Integer itemCount = 0;

    @ApiModelProperty("最后更新时间")
    private String lastUpdate;

    @ApiModelProperty("跳转路径")
    private String path;
}

