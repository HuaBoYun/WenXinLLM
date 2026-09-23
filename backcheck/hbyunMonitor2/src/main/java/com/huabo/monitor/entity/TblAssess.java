package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSESS")
@Schema(name="TblAssess对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssess extends FlexibleFieldEntity implements Serializable {

    public static String CREATE = "1";
    public static String START = "2";
    public static String END = "3";
    public static String JISUAN = "4";

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name="评价项目ID")
	@TableField("ASSID")
    private BigDecimal assid;

    @Schema(name="评价项目编号")
    @TableField("ASSESSID")
    private String assessid;

    @Schema(name="开始时间")
    @TableField("STARTDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startdate;

    @Schema(name="结束时间")
    @TableField("ENDDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    @Schema(name="备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name="状态(1创建2启动)")
    @TableField("ASSSTATUS")
    private String assstatus;

    @Schema(name="发起日期")
    @TableField("ASSSTARTDAY")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assstartday;

    @Schema(name="评价组织")
    @TableField("ASSORGS")
    private String assorgs;

    @Schema(name="评价项目名称")
    @TableField("ASSESSNAME")
    private String assessname;

    @Schema(name="评价对象")
    @TableField("ASSSPONSOR")
    private String asssponsor;

    @Schema(name="归档人")
    @TableField("ARCHIVEPERSON")
    private String archiveperson;

    @Schema(name="归档时间")
    @TableField("ARCHIVETIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date archivetime;

    @Schema(name="初步评价等级")
    @TableField("PRELIMINARYASSLEVEL")
    private String preliminaryasslevel;

    @Schema(name="初步评价评分")
    @TableField("PRELIMINARYASSSCORE")
    private BigDecimal preliminaryassscore;

    @Schema(name="校正级别")
    @TableField("ADUSTLEVEL")
    private String adustlevel;

    @Schema(name="校正原因")
    @TableField("ADJUSTRESON")
    private String adjustreson;
    
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;

    @Schema
    @TableField("ANALYSISSUMMARY")
    private String analysissummary;

    @Schema(name="最终得分")
    @TableField("FINALSCORE")
    private BigDecimal finalscore;
    
    @Schema(name="评价计划")
    @TableField("PLANID")
    private BigDecimal planid;
    
    @Schema(name="评价计划")
    @TableField(exist=false)
    private String planname;

    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    

    //TBL_ASSESSTEMPLE 外键
    @Schema(name="外键")
    @TableField("ASSTEMID")
    private BigDecimal asstemid;

    
    @Schema(name="评价时间")
    @TableField("ASSESSDATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assessdate;

    //TBL_STAFF 外键
    @Schema(name="评价负责人")
    @TableField("LEADERID")
    private BigDecimal leaderid;

    
    @Schema(name="关联orgid")
    @TableField("TBLCOMANY")
    private String tblcomany;
    
    @TableField(exist=false)
    private String  templename;
    
    @Schema(name="审批状态")
    @TableField(exist=false)
    private Integer status;

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
    
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    
    
    
    
    @Schema(name="评价小组名称")
    private String assteamname;
    
    @Schema(name="评价小组组长")
    private String assteamlead;
    
    //组长名称
    @TableField(exist = false)
    private String assteamleadname;
    
    //组员ids
    @TableField(exist = false)
    private String assteammemberids;
    //组员名称
    @TableField(exist = false)
    private String assteammember;
    
    
    @Schema(name="校正级别")
    @TableField(exist = false)
	private String checklevel;

	@Schema(name="校正原因")
	@TableField(exist = false)
	private String checkreason;

	@Schema(name="初步级别")
	@TableField(exist = false)
	private String finallevel;

	@Schema(name="初步评分")
	@TableField(exist = false)
	private String finalscorenew;
    
    

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
    
 
    @Schema(name = "所属公司")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name="下发消息拼接")
	@TableField(exist = false)
	private String jsonString;
    
}
