package com.huabo.system.entity;


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
@TableName("TBL_BI_CHART")
@Schema(name="TblBiChart")
public class TblBiChart {
    @TableId(value="CHARTID",type = IdType.INPUT)
    private BigDecimal chartid;
   // private TblBiPage tblBiPage;
    @TableField("CHARTNAME")
    private String chartname;
    @TableField("CHARTTYPE")
    private String charttype;
    @TableField("DATATYPE")
    private String datatype;
    @TableField("MEMO")
    private String memo;
    @TableField("POINTX")
    private Double pointx;
    @TableField("HEIGHT")
    private BigDecimal height;
    @TableField("WIDTH")
    private BigDecimal width;
    @TableField("TITLE")
    private String title;
    @TableField("POINTY")
    private Double pointy;
    @TableField("AXISX")
    private String axisx;
    @TableField("AXISY")
    private String axisy;
    @TableField("CONFIGURATION")
    private String configuration;
    @TableField("DIVID")
    private String divid;
    //private Set tblBiDatasources = new HashSet(0);
    @TableField("PAGETYPE")
    private String pageType;
}
