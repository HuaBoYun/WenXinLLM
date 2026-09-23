package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 缺陷标准实体类
 */
@Data
@TableName("TBL_YJPT_BUGCRITERION")
@ApiModel("缺陷标准")
public class TblNbsjBugCriterion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "缺陷标准ID")
    private Integer bugcriid;

    @ApiModelProperty(value = "缺陷级别")
    private String bugcrilevel;
}

