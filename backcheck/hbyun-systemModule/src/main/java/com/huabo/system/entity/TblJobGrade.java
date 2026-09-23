package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_JOB_GRADE")
@Schema(name="职级表")
public class TblJobGrade implements Serializable {

    @TableId(value="GRADEID",type = IdType.INPUT)
    private BigDecimal gradeid;
    
    @Schema(name="职级编码")
    @TableField("GRADECODE")
    private String gradeCode;
    
    @Schema(name="职级名称")
    @TableField("ALIASNAME")
    private String aliasName;
    
    
    @Schema(name="职级分类id")
    @TableField("CATEGORY")
    private String category;
    
    @Schema(name="职级分类名称")
    @TableField("CATEGORYNAME")
    private String categoryName;
    
    @Schema(name="职级描述")
    @TableField("DESCRIPTION")
    private String description;
     
    @Schema(name="存储同步前职级id")
    @TableField("HISTORYCODE")
    private String historyCode;

    @Schema(name="数据来源")
    @TableField("DATASOURCE")
    private String dataSource;
}
