package com.huabo.audit.util;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *@ClassName BaseVo
 *@Description 实体类公共属性
 *@Author ZiYao
 *@Date 2022/4/12 16:25
 *@Version 1.0
 **/
@Schema(name="实体类公共属性")
@Data
@Accessors(chain = true)
public abstract class BaseVo extends Model {
	
	@Schema(name="每页分页数量")
    Integer pageSize=15;
    
    @Schema(name="当前页数")
    Integer pageNum=1;
    
    @Schema(name="当前页数")
    Integer pageNumber=1;

    @Schema(name="创建时间开始")
    private String createTimeBegin;

    @Schema(name = "筛选条件 创建日期结束时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTimeEnd;
    
    @Schema(name = "当前登录用户可以查看的部门数据 为空默认自己创建，不需传入")
    String deptIds;

    /**
     * 字段排序sql
     * @param sortFields
     * @param sortFlag
     * @return
     */
    public static String getOrderSqlByFields(String sortFields, String sortFlag, String defaultField){
        StringBuilder builder = new StringBuilder();
        if(StrUtil.isNotEmpty(sortFields) && StrUtil.isNotEmpty(sortFlag)){
            builder.append(" order by ").append(sortFields).append(" ").append(sortFlag).append(" ");
            return builder.toString();
        }else{
            builder.append(" order by ").append(defaultField).append(" desc ").append(" ");
            return builder.toString();
        }
    }
}
