package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblReport implements Serializable {
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


    @Schema(name="主键id（自增）")
    @TableId(type= IdType.INPUT)
    private BigDecimal reportid;//主键（自增）

    @Schema(name="报告名称")
    private String reportname;//报告名称

    @Schema(name="报告时间")
    private LocalDateTime reporttime;//报告时间

    @Schema(name="报告名称")
    private String reporttype;//对内报告

    @Schema(name="定期报告")
    private String reportmode;//定期报告

    @Schema(name="报告人")
    private String reporter;//报告人

    @Schema(name="报告部门")
    private String reportdepartment;//报告部门

    @Schema
    private BigDecimal reporttempid;

    @Schema(name="报告状态")
    private String reportstatus;//报告状态

    @Schema(name="报告文件")
    private String reportfile;//报告文件

    @Schema(name="备注")
    private String memo;//备注

    @Schema(name="类型:如fx,nk,nbsj")
    private String type;//类型，如fx,nk,nbsj

    @Schema
    private String repdesc;

    @Schema(name="项目id")
    private BigDecimal projectid;//项目id

    @Schema
    private BigDecimal orgid;

    @Schema
    private String yjdes;

    @Schema
    private BigDecimal fhstaffid;

    @Schema
    private BigDecimal zqyjstaffid;

    @Schema
    private String reportcode;

    @Schema(name="邮件发送时间")
    private LocalDateTime sendtime;//邮件发送时间

    @Schema
    private String fhstaffname;

    @Schema
    private BigDecimal reporterid;

    @Schema
    private BigDecimal reportdepartmentid;

    @Schema
    private String zqyjstaffname;

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
    public LocalDateTime getReporttime() {
        return reporttime;
    }

    public void setReporttime(LocalDateTime reporttime) {
        this.reporttime = reporttime;
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
