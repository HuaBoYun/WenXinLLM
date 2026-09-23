package com.global.treasurer.util;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 *@ClassName BaseVo
 *@Description 实体类公共属性
 *@Author ZiYao
 *@Date 2022/4/12 16:25
 *@Version 1.0
 **/
@ApiModel

@Accessors(chain = true)
public abstract class BaseVo extends Model {
    @ApiModelProperty(example = "15",value="每页分页数量")
    Integer pageSize=15;
    
    @ApiModelProperty(example = "1",dataType = "java.lang.Integer",value="当前页数")
    Integer pageNum=1;
}
