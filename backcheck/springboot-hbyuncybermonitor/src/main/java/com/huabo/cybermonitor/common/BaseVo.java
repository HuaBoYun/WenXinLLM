package com.huabo.cybermonitor.common;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName BaseVo
 * @Description 实体类公共属性
 * @Author ZiYao
 * @Date 2022/4/12 16:25
 * @Version 1.0
 **/
@ApiModel
@Data
@Accessors(chain = true)
public abstract class BaseVo extends Model {

    @ApiModelProperty(example = "15", value = "每页分页数量")
    Integer pageSize = 15;

    @ApiModelProperty(example = "1", dataType = "java.lang.Integer", value = "当前页数")
    Integer pageNum = 1;

    @ApiModelProperty(example = "1", dataType = "java.lang.Integer", value = "当前页数")
    Integer pageNumber = 1;

    @ApiModelProperty(example = "1999-01-01", dataType = "java.lang.String", value = "创建时间开始")
    private String createTimeBegin;

    @ApiModelProperty(example = "1999-01-01", dataType = "java.lang.String", value = "创建时间结束")
    private String createTimeEnd;

    @ApiModelProperty(value = "当前登录用户可以查看的部门数据 为空默认自己创建，不需传入")
    String deptIds;

    /**
     * 字段排序sql
     *
     * @param sortFields
     * @param sortFlag
     * @return
     */
    public static String getOrderSqlByFields(String sortFields, String sortFlag, String defaultField) {
        StringBuilder builder = new StringBuilder();
        if (StrUtil.isNotEmpty(sortFields) && StrUtil.isNotEmpty(sortFlag)) {
            builder.append(" order by ").append(sortFields).append(" ").append(sortFlag).append(" ");
            return builder.toString();
        } else {
            builder.append(" order by ").append(defaultField).append(" desc ").append(" ");
            return builder.toString();
        }
    }
}

