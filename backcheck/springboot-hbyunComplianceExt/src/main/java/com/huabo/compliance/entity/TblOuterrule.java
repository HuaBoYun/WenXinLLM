package com.huabo.compliance.entity;


import cc.aicode.e2e.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_OUTERRULE")
@Schema(name="TblOuterrule")
public class TblOuterrule {

    private static final long serialVersionUID = 1L;
    @TableId("OUTRULID")
    private BigDecimal outrulid;
    @ExcelProperty(
            value = "文件名称"
    )
    @TableField("RULENAME")
    private String rulename;
    @ExcelProperty(
            value = "发文部门"
    )
    @TableField("PUBLISHORG")
    private String publishorg;
    @ExcelProperty(
            value = "发文日期"
    )
    @TableField("PUBLISHDATE")
    private Date publishdate;
    @ExcelProperty("发文文号")
    @TableField("RULENUMBER")
    private String rulenumber;
    @TableField("MEMO")
    private String memo;
    @TableField("OUTRULETYPE")
    private String outruletype;
    @ExcelProperty(
            value = "效力级别"
    )
    @TableField("EFFECTIVELEVEL")
    private String effectivelevel;
    @ExcelProperty(
            value = "时效性"
    )
    @TableField("TIMELINESS")
    private String timeliness;
    @ExcelProperty(
            value = "生效日期"
    )
    @TableField("TAKEEFFECTTIME")
    private Date takeeffecttime;
    @ExcelProperty("摘要")
    @TableField("SUMMARYINFO")
    private String summaryinfo;
    @ExcelProperty("正文")
    @TableField("BODYINFO")
    private String bodyinfo;
    @ExcelProperty(
            value = "录入人"
    )
    @TableField("ENTERINGPERSON")
    private String enteringperson;
    @ExcelProperty(
            value = "录入时间"
    )
    @TableField("ENTERINGTIME")
    private Date enteringtime;
    @ExcelProperty(
            value = "所属公司"
    )
    @Transient
    private String belongCompany;
    @ExcelProperty(
            value = "是否爬虫存储"
    )
    @TableField("ISPYTHONFLAG")
    private String isPythonFlag;

    @Transient
    private TblOrganization tblOrganization;
    @TableField("STATUS")
    private String status;
    @Transient
    private Set tblControlmatrixes = new HashSet(0);
    //private Set tblFlows = new HashSet(0);
    @Transient
    private Set tblBugs = new HashSet(0);
    @Transient
    private Set tblRiskevents = new HashSet(0);
    @Transient
    private Set tblAttachments = new HashSet(0);
    private int int_check = 0;
    @ExcelProperty(
            value = "编号"
    )
    @TableField("RULECODE")
    private String rulecode;
}
