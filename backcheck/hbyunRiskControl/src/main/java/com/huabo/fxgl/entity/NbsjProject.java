package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@TableName("TBL_NBSJ_PROJECT")
public class NbsjProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审计项目ID
     */
    @TableId
    private BigDecimal projectid;

    /**
     * 审计项目名称
     */
    private String prjoectname;

    /**
     * 计划年份
     */
    private String planyear;

    /**
     * 项目来源
     */
    private String projectsource;

    /**
     * 开始时间
     */
    private LocalDateTime startdate;

    /**
     * 结束时间
     */
    private LocalDateTime enddate;

    /**
     * 项目经理
     */
    private BigDecimal pmid;

    /**
     * 审计模板
     */
    private BigDecimal tempid;

    /**
     * 费用
     */
    private BigDecimal costs;

    /**
     * 审计目标和范围
     */
    private String purpose;

    /**
     * 审计内容和重点
     */
    private String scopes;

    /**
     * 审计程序和方法
     */
    private String pursuant;

    /**
     * 备注
     */
    private String comments;

    /**
     * 主审人
     */
    private BigDecimal umpireid;

    /**
     * 质控经理
     */
    private BigDecimal controlid;

    /**
     * 审计类型
     */
    private String audittype;

    /**
     * 项目编号
     */
    private String projectcode;

    /**
     * 状态
     */
    private BigDecimal status;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime updatetime;

    /**
     * 选择的项目
     */
    private BigDecimal currentstatre;

    /**
     * 创建人
     */
    private BigDecimal createstaffid;

    /**
     * 指派项目经理时间
     */
    private LocalDateTime assigbedpmtime;

    /**
     * 指派主审人时间
     */
    private LocalDateTime assigbedumpetime;

    /**
     * 指派质控经理时间
     */
    private LocalDateTime assigbedcontroltime;

    /**
     * 计划编号
     */
    private BigDecimal planid;

    /**
     * 审计对象组织
     */
    private BigDecimal auditorgid;

    /**
     * 指引模板
     */
    private BigDecimal tempzyid;

    /**
     * 修改模板时间
     */
    private BigDecimal updatestatus;

    /**
     * 隶属组织
     */
    private BigDecimal orgid;

    /**
     * 完成时间
     */
    private LocalDateTime finishtime;

    /**
     * 实施时间
     */
    private LocalDateTime implementtime;

    /**
     * 审批状态
     */
    private Integer examinetype;

    /**
     * 相关内容
     */
    private String proDesc;

    /**
     * 审计方式
     */
    private String proSjfs;

    /**
     * 审计对象人
     */
    private BigDecimal auditstaffid;

    private String sjlx;

    private String sjzr;

    private String yqjcqk;

    private BigDecimal ejfhr;

    private BigDecimal templeteid;

    private BigDecimal staffid;

    private String auditorg;

    private BigDecimal fpstatus;

    private BigDecimal planprojectid;

    private String pprojectname;

    private String targetname;

    private String orgids;

    private String orgidnames;

    private BigDecimal externalassig;

    private BigDecimal pcount;

    private BigDecimal pstatus;

    private String filcode;

    private String filname;

    private String implementaion;

    private String cospomsordepartment;

    private String timerequirement;

    private String auditbasis;

    private String implementaionsteps;

    private String auditrequirements;

    public BigDecimal getProjectid() {
        return projectid;
    }

    public void setProjectid(BigDecimal projectid) {
        this.projectid = projectid;
    }
    public String getPrjoectname() {
        return prjoectname;
    }

    public void setPrjoectname(String prjoectname) {
        this.prjoectname = prjoectname;
    }
    public String getPlanyear() {
        return planyear;
    }

    public void setPlanyear(String planyear) {
        this.planyear = planyear;
    }
    public String getProjectsource() {
        return projectsource;
    }

    public void setProjectsource(String projectsource) {
        this.projectsource = projectsource;
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
    public BigDecimal getPmid() {
        return pmid;
    }

    public void setPmid(BigDecimal pmid) {
        this.pmid = pmid;
    }
    public BigDecimal getTempid() {
        return tempid;
    }

    public void setTempid(BigDecimal tempid) {
        this.tempid = tempid;
    }
    public BigDecimal getCosts() {
        return costs;
    }

    public void setCosts(BigDecimal costs) {
        this.costs = costs;
    }
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
    public String getPursuant() {
        return pursuant;
    }

    public void setPursuant(String pursuant) {
        this.pursuant = pursuant;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public BigDecimal getUmpireid() {
        return umpireid;
    }

    public void setUmpireid(BigDecimal umpireid) {
        this.umpireid = umpireid;
    }
    public BigDecimal getControlid() {
        return controlid;
    }

    public void setControlid(BigDecimal controlid) {
        this.controlid = controlid;
    }
    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }
    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }
    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }
    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }
    public LocalDateTime getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(LocalDateTime updatetime) {
        this.updatetime = updatetime;
    }
    public BigDecimal getCurrentstatre() {
        return currentstatre;
    }

    public void setCurrentstatre(BigDecimal currentstatre) {
        this.currentstatre = currentstatre;
    }
    public BigDecimal getCreatestaffid() {
        return createstaffid;
    }

    public void setCreatestaffid(BigDecimal createstaffid) {
        this.createstaffid = createstaffid;
    }
    public LocalDateTime getAssigbedpmtime() {
        return assigbedpmtime;
    }

    public void setAssigbedpmtime(LocalDateTime assigbedpmtime) {
        this.assigbedpmtime = assigbedpmtime;
    }
    public LocalDateTime getAssigbedumpetime() {
        return assigbedumpetime;
    }

    public void setAssigbedumpetime(LocalDateTime assigbedumpetime) {
        this.assigbedumpetime = assigbedumpetime;
    }
    public LocalDateTime getAssigbedcontroltime() {
        return assigbedcontroltime;
    }

    public void setAssigbedcontroltime(LocalDateTime assigbedcontroltime) {
        this.assigbedcontroltime = assigbedcontroltime;
    }
    public BigDecimal getPlanid() {
        return planid;
    }

    public void setPlanid(BigDecimal planid) {
        this.planid = planid;
    }
    public BigDecimal getAuditorgid() {
        return auditorgid;
    }

    public void setAuditorgid(BigDecimal auditorgid) {
        this.auditorgid = auditorgid;
    }
    public BigDecimal getTempzyid() {
        return tempzyid;
    }

    public void setTempzyid(BigDecimal tempzyid) {
        this.tempzyid = tempzyid;
    }
    public BigDecimal getUpdatestatus() {
        return updatestatus;
    }

    public void setUpdatestatus(BigDecimal updatestatus) {
        this.updatestatus = updatestatus;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public LocalDateTime getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(LocalDateTime finishtime) {
        this.finishtime = finishtime;
    }
    public LocalDateTime getImplementtime() {
        return implementtime;
    }

    public void setImplementtime(LocalDateTime implementtime) {
        this.implementtime = implementtime;
    }
    public Integer getExaminetype() {
        return examinetype;
    }

    public void setExaminetype(Integer examinetype) {
        this.examinetype = examinetype;
    }
    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }
    public String getProSjfs() {
        return proSjfs;
    }

    public void setProSjfs(String proSjfs) {
        this.proSjfs = proSjfs;
    }
    public BigDecimal getAuditstaffid() {
        return auditstaffid;
    }

    public void setAuditstaffid(BigDecimal auditstaffid) {
        this.auditstaffid = auditstaffid;
    }
    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }
    public String getSjzr() {
        return sjzr;
    }

    public void setSjzr(String sjzr) {
        this.sjzr = sjzr;
    }
    public String getYqjcqk() {
        return yqjcqk;
    }

    public void setYqjcqk(String yqjcqk) {
        this.yqjcqk = yqjcqk;
    }
    public BigDecimal getEjfhr() {
        return ejfhr;
    }

    public void setEjfhr(BigDecimal ejfhr) {
        this.ejfhr = ejfhr;
    }
    public BigDecimal getTempleteid() {
        return templeteid;
    }

    public void setTempleteid(BigDecimal templeteid) {
        this.templeteid = templeteid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getAuditorg() {
        return auditorg;
    }

    public void setAuditorg(String auditorg) {
        this.auditorg = auditorg;
    }
    public BigDecimal getFpstatus() {
        return fpstatus;
    }

    public void setFpstatus(BigDecimal fpstatus) {
        this.fpstatus = fpstatus;
    }
    public BigDecimal getPlanprojectid() {
        return planprojectid;
    }

    public void setPlanprojectid(BigDecimal planprojectid) {
        this.planprojectid = planprojectid;
    }
    public String getPprojectname() {
        return pprojectname;
    }

    public void setPprojectname(String pprojectname) {
        this.pprojectname = pprojectname;
    }
    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }
    public String getOrgids() {
        return orgids;
    }

    public void setOrgids(String orgids) {
        this.orgids = orgids;
    }
    public String getOrgidnames() {
        return orgidnames;
    }

    public void setOrgidnames(String orgidnames) {
        this.orgidnames = orgidnames;
    }
    public BigDecimal getExternalassig() {
        return externalassig;
    }

    public void setExternalassig(BigDecimal externalassig) {
        this.externalassig = externalassig;
    }
    public BigDecimal getPcount() {
        return pcount;
    }

    public void setPcount(BigDecimal pcount) {
        this.pcount = pcount;
    }
    public BigDecimal getPstatus() {
        return pstatus;
    }

    public void setPstatus(BigDecimal pstatus) {
        this.pstatus = pstatus;
    }
    public String getFilcode() {
        return filcode;
    }

    public void setFilcode(String filcode) {
        this.filcode = filcode;
    }
    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }
    public String getImplementaion() {
        return implementaion;
    }

    public void setImplementaion(String implementaion) {
        this.implementaion = implementaion;
    }
    public String getCospomsordepartment() {
        return cospomsordepartment;
    }

    public void setCospomsordepartment(String cospomsordepartment) {
        this.cospomsordepartment = cospomsordepartment;
    }
    public String getTimerequirement() {
        return timerequirement;
    }

    public void setTimerequirement(String timerequirement) {
        this.timerequirement = timerequirement;
    }
    public String getAuditbasis() {
        return auditbasis;
    }

    public void setAuditbasis(String auditbasis) {
        this.auditbasis = auditbasis;
    }
    public String getImplementaionsteps() {
        return implementaionsteps;
    }

    public void setImplementaionsteps(String implementaionsteps) {
        this.implementaionsteps = implementaionsteps;
    }
    public String getAuditrequirements() {
        return auditrequirements;
    }

    public void setAuditrequirements(String auditrequirements) {
        this.auditrequirements = auditrequirements;
    }

    @Override
    public String toString() {
        return "NbsjProject{" +
            "projectid=" + projectid +
            ", prjoectname=" + prjoectname +
            ", planyear=" + planyear +
            ", projectsource=" + projectsource +
            ", startdate=" + startdate +
            ", enddate=" + enddate +
            ", pmid=" + pmid +
            ", tempid=" + tempid +
            ", costs=" + costs +
            ", purpose=" + purpose +
            ", scopes=" + scopes +
            ", pursuant=" + pursuant +
            ", comments=" + comments +
            ", umpireid=" + umpireid +
            ", controlid=" + controlid +
            ", audittype=" + audittype +
            ", projectcode=" + projectcode +
            ", status=" + status +
            ", createtime=" + createtime +
            ", updatetime=" + updatetime +
            ", currentstatre=" + currentstatre +
            ", createstaffid=" + createstaffid +
            ", assigbedpmtime=" + assigbedpmtime +
            ", assigbedumpetime=" + assigbedumpetime +
            ", assigbedcontroltime=" + assigbedcontroltime +
            ", planid=" + planid +
            ", auditorgid=" + auditorgid +
            ", tempzyid=" + tempzyid +
            ", updatestatus=" + updatestatus +
            ", orgid=" + orgid +
            ", finishtime=" + finishtime +
            ", implementtime=" + implementtime +
            ", examinetype=" + examinetype +
            ", proDesc=" + proDesc +
            ", proSjfs=" + proSjfs +
            ", auditstaffid=" + auditstaffid +
            ", sjlx=" + sjlx +
            ", sjzr=" + sjzr +
            ", yqjcqk=" + yqjcqk +
            ", ejfhr=" + ejfhr +
            ", templeteid=" + templeteid +
            ", staffid=" + staffid +
            ", auditorg=" + auditorg +
            ", fpstatus=" + fpstatus +
            ", planprojectid=" + planprojectid +
            ", pprojectname=" + pprojectname +
            ", targetname=" + targetname +
            ", orgids=" + orgids +
            ", orgidnames=" + orgidnames +
            ", externalassig=" + externalassig +
            ", pcount=" + pcount +
            ", pstatus=" + pstatus +
            ", filcode=" + filcode +
            ", filname=" + filname +
            ", implementaion=" + implementaion +
            ", cospomsordepartment=" + cospomsordepartment +
            ", timerequirement=" + timerequirement +
            ", auditbasis=" + auditbasis +
            ", implementaionsteps=" + implementaionsteps +
            ", auditrequirements=" + auditrequirements +
        "}";
    }
}
