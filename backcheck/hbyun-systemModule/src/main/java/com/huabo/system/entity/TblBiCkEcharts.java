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
@TableName("TBL_BI_CK_ECHARTS")
@Schema(name="TblBiCkEcharts")
public class TblBiCkEcharts implements Serializable {

    @TableId(value="CHARTID",type = IdType.INPUT)
    private BigDecimal chartid;
    //private TblBiReportMenu tblBiReportMenu;
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
   // private Set tblBiDatasources = new HashSet(0);
    @TableField("PAGETYPE")
    private String pageType;

    public TblBiCkEcharts() {
    }

//    public TblBiCkEcharts(TblBiReportMenu tblBiReportMenu) {
//        this.tblBiReportMenu = tblBiReportMenu;
//    }

    public TblBiCkEcharts(TblBiReportMenu tblBiReportMenu, String chartname, String charttype, String datatype, String memo, Double pointx, BigDecimal height, BigDecimal width, String title, Double pointy, String axisx, String axisy, String configuration, String divid) {
        //this.tblBiReportMenu = tblBiReportMenu;
        this.chartname = chartname;
        this.charttype = charttype;
        this.datatype = datatype;
        this.memo = memo;
        this.pointx = pointx;
        this.height = height;
        this.width = width;
        this.title = title;
        this.pointy = pointy;
        this.axisx = axisx;
        this.axisy = axisy;
        this.configuration = configuration;
        this.divid = divid;
        //this.tblBiDatasources = tblBiDatasources;
    }

}
