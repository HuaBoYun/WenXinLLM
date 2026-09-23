package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-17
 */
@TableName("TBL_REPORT")
@Schema(name="TblReport对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
@Data
public class TblReport extends FlexibleFieldEntity  implements Serializable {
    public final static Integer SPZ=1;//审批中
    public final static Integer XTZ=2;//需调整
    public final static Integer YTG=3;//已通过
    public final static Integer YZZ=4;//已终止
    public final static Integer FHZ=5;//复核中
    public final static Integer FHTZ=6;//复核调整
    public final static Integer ZQYJ=7;//征求意见
    public final static Integer ZQYJTZ=8;//征求意见调整
    public final static Integer FHTG=9;//复核通过
    public final static Integer FHZZ=10;//复核终止
    public final static Integer ZQYJTG=11;//征求意见通过
    public final static Integer ZQYJZZ=12;//征求意见终止
    private static final long serialVersionUID = 1L;


    @TableId(type= IdType.INPUT)
    private BigDecimal reportid;//主键（自增）

	@TableField("REPORTNAME")
	@Schema(name = "报告名称")
    private String reportname;//报告名称

	@Schema(name = "报告时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField("REPORTTIME")
    private Date reporttime;//

    @TableField("REPORTTYPE")
	@Schema(name = "报告类型")
    private String reporttype;//

    @TableField("REPORTMODE")
 	@Schema(name = "报告方式")
    private String reportmode;//定期报告

    @TableField("REPORTER")
   	@Schema(name = "报告人")
    private String reporter;//报告人

    @TableField("REPORTDEPARTMENT")
   	@Schema(name = "报告部门")
    private String reportdepartment;//报告部门

    @TableField("REPORTTEMPID")
	@Schema
    private BigDecimal reporttempid;

    @TableField("REPORTSTATUS")
   	@Schema
    private String reportstatus;//报告状态

    private String reportfile;//报告文件

    private String memo;//备注

    @TableField("TYPE")
   	@Schema(name = "类型")
    private String type;//类型，如fx,nk,nbsj

    private String repdesc;

    private BigDecimal projectid;//项目id
    
    @TableField("LINKDEPTID")
   	@Schema
    private BigDecimal linkDeptId;

    
    @TableField("ORGID")
   	@Schema(name = "报告部门")
    private BigDecimal orgid;

    private String yjdes;

    private BigDecimal fhstaffid;

    private BigDecimal zqyjstaffid;

    private String reportcode;

    private LocalDateTime sendtime;//邮件发送时间

    private String fhstaffname;

    private BigDecimal reporterid;

    private BigDecimal reportdepartmentid;

    private String zqyjstaffname;
    
    private Integer status;//审批状态

    @TableField("CREATESTAFFID")
   	@Schema(name = "报告人")
    private BigDecimal createstaffid;
    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
    @TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name = "创建时间")
    private Date createtime;
    
    
    

    public BigDecimal getReportid() {
        return reportid;
    }

    public void setReportid(BigDecimal reportid) {
        this.reportid = reportid;
    }
    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }
    public String getReporttype() {
        return reporttype;
    }

    public void setReporttype(String reporttype) {
        this.reporttype = reporttype;
    }
    public String getReportmode() {
        return reportmode;
    }

    public void setReportmode(String reportmode) {
        this.reportmode = reportmode;
    }
    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    public String getReportdepartment() {
        return reportdepartment;
    }

    public void setReportdepartment(String reportdepartment) {
        this.reportdepartment = reportdepartment;
    }
    public BigDecimal getReporttempid() {
        return reporttempid;
    }

    public void setReporttempid(BigDecimal reporttempid) {
        this.reporttempid = reporttempid;
    }
    
    public String getReportstatus() {
        return reportstatus;
    }
    public void setReportstatus(String reportstatus) {
        this.reportstatus = reportstatus;
    }
    
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getReportfile() {
        return reportfile;
    }

    public void setReportfile(String reportfile) {
        this.reportfile = reportfile;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getRepdesc() {
        return repdesc;
    }

    public void setRepdesc(String repdesc) {
        this.repdesc = repdesc;
    }
    public BigDecimal getProjectid() {
        return projectid;
    }

    public void setProjectid(BigDecimal projectid) {
        this.projectid = projectid;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getYjdes() {
        return yjdes;
    }

    public void setYjdes(String yjdes) {
        this.yjdes = yjdes;
    }
    public BigDecimal getFhstaffid() {
        return fhstaffid;
    }

    public void setFhstaffid(BigDecimal fhstaffid) {
        this.fhstaffid = fhstaffid;
    }
    public BigDecimal getZqyjstaffid() {
        return zqyjstaffid;
    }

    public void setZqyjstaffid(BigDecimal zqyjstaffid) {
        this.zqyjstaffid = zqyjstaffid;
    }
    public String getReportcode() {
        return reportcode;
    }

    public void setReportcode(String reportcode) {
        this.reportcode = reportcode;
    }
    public LocalDateTime getSendtime() {
        return sendtime;
    }

    public void setSendtime(LocalDateTime sendtime) {
        this.sendtime = sendtime;
    }
    public String getFhstaffname() {
        return fhstaffname;
    }

    public void setFhstaffname(String fhstaffname) {
        this.fhstaffname = fhstaffname;
    }
    public BigDecimal getReporterid() {
        return reporterid;
    }

    public void setReporterid(BigDecimal reporterid) {
        this.reporterid = reporterid;
    }
    public BigDecimal getReportdepartmentid() {
        return reportdepartmentid;
    }

    public void setReportdepartmentid(BigDecimal reportdepartmentid) {
        this.reportdepartmentid = reportdepartmentid;
    }
    public String getZqyjstaffname() {
        return zqyjstaffname;
    }

    public void setZqyjstaffname(String zqyjstaffname) {
        this.zqyjstaffname = zqyjstaffname;
    }

    @Override
    public String toString() {
        return "TblReport{" +
            "reportid=" + reportid +
            ", reportname=" + reportname +
            ", reporttime=" + reporttime +
            ", reporttype=" + reporttype +
            ", reportmode=" + reportmode +
            ", reporter=" + reporter +
            ", reportdepartment=" + reportdepartment +
            ", reporttempid=" + reporttempid +
            ", reportstatus=" + reportstatus +
            ", reportfile=" + reportfile +
            ", memo=" + memo +
            ", type=" + type +
            ", repdesc=" + repdesc +
            ", projectid=" + projectid +
            ", orgid=" + orgid +
            ", yjdes=" + yjdes +
            ", fhstaffid=" + fhstaffid +
            ", zqyjstaffid=" + zqyjstaffid +
            ", reportcode=" + reportcode +
            ", sendtime=" + sendtime +
            ", fhstaffname=" + fhstaffname +
            ", reporterid=" + reporterid +
            ", reportdepartmentid=" + reportdepartmentid +
            ", zqyjstaffname=" + zqyjstaffname +
        "}";
    }
}
