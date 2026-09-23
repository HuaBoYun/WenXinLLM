package com.huabo.monitor.mysql.entity;


import cc.aicode.e2e.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_CONTROLMATRIX")
@Schema(name="TblControlmatrix")
public class TblControlmatrixMySql implements Serializable {

    public static final Integer YES_VSESION = 1;
    public static final Integer NO_VSESION = 0;
    private static final long serialVersionUID = 1L;
    @TableId("CONMATID")
    @Id
    private BigDecimal conmatid;

    @ExcelProperty(value = "流程名称")
    @TableField("FLOWNAME")
    private String flowname;

    @TableField("CONTROLTARGET")
    private String controltarget;

    @ExcelProperty(value = "风险控制点编号")
    @TableField("CONTROLNUMBER")
    private String controlnumber;

    @TableField("CONTROLNAME")
    private String controlname;

    @ExcelProperty(value = "控制责任人")
    @TableField("CONTROLMANAGER")
    private String controlmanager;

    @ExcelProperty(value = "控制频率")
    @TableField("CONTROLFREQUENCY")
    private String controlfrequency;

    @TableField("CONTROLTYPE")
    private String controltype; //控制类型

    @ExcelProperty(value = "控制手段")
    @TableField("CONTROLMETHOD")
    private String controlmethod;

    @TableField("MEMO")
    private String memo;

    @ExcelProperty(value = "风险控制点描述")
    @TableField("CONTROLDES")
    private String controldes;

    @TableField("INSIDECONTROLTARGET")
    private String insidecontroltarget;

    @ExcelProperty(value = "是否关键控制")
    @TableField("KEYCONTROL")
    private String keycontrol;

    @TableField("EFFECTIVE")
    private String effective;

    @ExcelProperty(value = "是否进行控制测试")
    @TableField("CONTROLTEST")
    private String controltest;

    @TableField("FINANCIALREPORTIDENTIFY")
    private String financialreportidentify;

    @TableField("RELATEDDEPART")
    private String relateddepart;

    @TableField("CONTROLDOCUMENT")
    private String controldocument;

    @ExcelProperty(value = "流程编号")
    @TableField("FLOWCODE")
    private String flowcode;

    @ExcelProperty(value = "流程分类")
    @TableField("TOPLEVELFLOWCAT")
    private String toplevelflowcat;

    @TableField("VERSION")
    private String version;

    @TableField("CREATEDTIME")
    private String createdtime;

    @TableField("LASTMODIFIEDTIME")
    private String lastmodifiedtime;

    @ExcelProperty(value = "控制措施")
    @TableField("CONKZCS")
    private String conkzcs;

    @TableField("VERSIONTYPE")
    private Integer versionType;

    @Transient
    private Set tblFlows = new HashSet(0);

}
