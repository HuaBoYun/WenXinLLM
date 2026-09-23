package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.common.BaseVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName BaseQueryVo
 * @Description 基础查询VO
 * @Author ZiYao
 * @Date 2022/4/12 16:25
 * @Version 1.0
 **/
@ApiModel(description = "基础查询")
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseQueryVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "查询ID")
    private String id;

    @ApiModelProperty(value = "查询名称")
    private String name;

    @ApiModelProperty(value = "查询类型")
    private String type;

    @ApiModelProperty(value = "查询状态")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;
}

