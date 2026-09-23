package com.huabo.compliance.mysql.entity;


import cc.aicode.e2e.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_INNERRULE")
@Schema(name="TblInnerrule")
public class TblInnerruleMySql {

    private static final long serialVersionUID = 1L;
    @TableId("INNRULID")
    private BigDecimal innrulid;
    @ExcelProperty(
            value = "制度名称"
    )
    @TableField("RULENAME")
    private String rulename;
    @ExcelProperty(
            value = "发文机构"
    )
    @TableField("PUBLISHORG")
    private String publishorg;
    @ExcelProperty(
            value = "生效日期"
    )
    @TableField("PUBLISHDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishdate;
    @ExcelProperty("发文文号")
    @TableField("RULENUMBER")
    private String rulenumber;
    @TableField("MEMO")
    private String memo;
    @ExcelProperty(
            value = "类别"
    )
    @TableField("INNRULETYPE")
    private String innruletype;
    @TableField("HOSTDEPARTMENT")
    private String hostdepartment;
    @TableField("COORGANIZER")
    private String coorganizer;
    @TableField("VERSION")
    private String version;
    @TableField("TAKEEFFECTTIME")
    private Date takeeffecttime;
    @TableField("SUMMARYINFO")
    private String summaryinfo;
    @ExcelProperty("类别")
    @TableField("BODYINFO")
    private String bodyinfo;
    @TableField("ENTERINGPERSON")
    private String enteringperson;
    @TableField("ENTERINGTIME")
    private Date enteringtime;
    @ExcelProperty(
            value = "状态"
    )
    @TableField("STATUS")
    private String status;
    @TableField("TIMELINESS")
    private String timeliness;
    @ExcelProperty(
            value = "制度编号"
    )
    @TableField("RULECODE")
    private String rulecode;

    @Transient
    private String orgname;
    @Transient
    private Set tblRiskevents = new HashSet(0);
    @Transient
    private Set tblBugs = new HashSet(0);
    @Transient
    private Set tblControlmatrixes = new HashSet(0);
    @Transient
    private Set tblAttachments = new HashSet(0);
    @Transient
    private TblOrganizationMySql tblOrganizationMySql;


}
