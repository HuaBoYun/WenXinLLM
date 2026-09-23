package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSESS")
@Schema(name="TblAssess对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssess implements Serializable {

    public static String CREATE = "1";
    public static String START = "2";
    public static String END = "3";
    public static String JISUAN = "4";

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name="评价项目ID")
    private BigDecimal assid;

    @Schema(name="评价项目编号")
    private String assessid;

    @Schema(name="开始时间")
    private LocalDateTime startdate;

    @Schema(name="结束时间")
    private LocalDateTime enddate;

    @Schema(name="备注")
    private String memo;

    @Schema(name="状态(1创建2启动)")
    private String assstatus;

    @Schema(name="发起日期")
    private LocalDateTime assstartday;

    @Schema(name="评价组织")
    private String assorgs;

    @Schema(name="评价项目名称")
    private String assessname;

    @Schema(name="评价对象")
    private String asssponsor;

    @Schema(name="归档人")
    private String archiveperson;

    @Schema(name="归档时间")
    private LocalDateTime archivetime;

    @Schema(name="初步评价等级")
    private String preliminaryasslevel;

    @Schema(name="初步评价评分")
    private BigDecimal preliminaryassscore;

    @Schema(name="校正级别")
    private String adustlevel;

    @Schema(name="校正原因")
    private String adjustreson;

    private String analysissummary;

    private BigDecimal finalscore;

    //TBL_ASSESSTEMPLE 外键
    private BigDecimal asstemid;
    //评价时间
    private LocalDateTime assessdate;

    //TBL_STAFF 外键
    @Schema(name="评价负责人")
    private BigDecimal leaderid;

    private String tblcomany;

    public BigDecimal getAssid() {
        return assid;
    }

    public void setAssid(BigDecimal assid) {
        this.assid = assid;
    }
    public String getAssessid() {
        return assessid;
    }

    public void setAssessid(String assessid) {
        this.assessid = assessid;
    }
    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }
    public LocalDateTime getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDateTime enddate) {
        this.enddate = enddate;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getAssstatus() {
        return assstatus;
    }

    public void setAssstatus(String assstatus) {
        this.assstatus = assstatus;
    }
    public LocalDateTime getAssstartday() {
        return assstartday;
    }

    public void setAssstartday(LocalDateTime assstartday) {
        this.assstartday = assstartday;
    }
    public String getAssorgs() {
        return assorgs;
    }

    public void setAssorgs(String assorgs) {
        this.assorgs = assorgs;
    }
    public String getAssessname() {
        return assessname;
    }

    public void setAssessname(String assessname) {
        this.assessname = assessname;
    }
    public String getAsssponsor() {
        return asssponsor;
    }

    public void setAsssponsor(String asssponsor) {
        this.asssponsor = asssponsor;
    }
    public String getArchiveperson() {
        return archiveperson;
    }

    public void setArchiveperson(String archiveperson) {
        this.archiveperson = archiveperson;
    }
    public LocalDateTime getArchivetime() {
        return archivetime;
    }

    public void setArchivetime(LocalDateTime archivetime) {
        this.archivetime = archivetime;
    }
    public String getPreliminaryasslevel() {
        return preliminaryasslevel;
    }

    public void setPreliminaryasslevel(String preliminaryasslevel) {
        this.preliminaryasslevel = preliminaryasslevel;
    }
    public BigDecimal getPreliminaryassscore() {
        return preliminaryassscore;
    }

    public void setPreliminaryassscore(BigDecimal preliminaryassscore) {
        this.preliminaryassscore = preliminaryassscore;
    }
    public String getAdustlevel() {
        return adustlevel;
    }

    public void setAdustlevel(String adustlevel) {
        this.adustlevel = adustlevel;
    }
    public String getAdjustreson() {
        return adjustreson;
    }

    public void setAdjustreson(String adjustreson) {
        this.adjustreson = adjustreson;
    }
    public String getAnalysissummary() {
        return analysissummary;
    }

    public void setAnalysissummary(String analysissummary) {
        this.analysissummary = analysissummary;
    }
    public BigDecimal getFinalscore() {
        return finalscore;
    }

    public void setFinalscore(BigDecimal finalscore) {
        this.finalscore = finalscore;
    }
    public BigDecimal getAsstemid() {
        return asstemid;
    }

    public void setAsstemid(BigDecimal asstemid) {
        this.asstemid = asstemid;
    }
    public LocalDateTime getAssessdate() {
        return assessdate;
    }

    public void setAssessdate(LocalDateTime assessdate) {
        this.assessdate = assessdate;
    }
    public BigDecimal getLeaderid() {
        return leaderid;
    }

    public void setLeaderid(BigDecimal leaderid) {
        this.leaderid = leaderid;
    }
    public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }

    @Override
    public String toString() {
        return "TblAssess{" +
            "assid=" + assid +
            ", assessid=" + assessid +
            ", startdate=" + startdate +
            ", enddate=" + enddate +
            ", memo=" + memo +
            ", assstatus=" + assstatus +
            ", assstartday=" + assstartday +
            ", assorgs=" + assorgs +
            ", assessname=" + assessname +
            ", asssponsor=" + asssponsor +
            ", archiveperson=" + archiveperson +
            ", archivetime=" + archivetime +
            ", preliminaryasslevel=" + preliminaryasslevel +
            ", preliminaryassscore=" + preliminaryassscore +
            ", adustlevel=" + adustlevel +
            ", adjustreson=" + adjustreson +
            ", analysissummary=" + analysissummary +
            ", finalscore=" + finalscore +
            ", asstemid=" + asstemid +
            ", assessdate=" + assessdate +
            ", leaderid=" + leaderid +
            ", tblcomany=" + tblcomany +
        "}";
    }
}
