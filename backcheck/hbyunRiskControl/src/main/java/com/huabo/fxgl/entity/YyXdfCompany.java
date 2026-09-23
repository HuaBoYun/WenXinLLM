package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CJBDI企业监控信息实体类
 * 对应表：TBL_YY_CJBDI_COMPANY_MONITOR
 */
@TableName("TBL_YY_CJBDI_COMPANY_MONITOR")
public class YyXdfCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID（使用统一社会信用代码）
     */
    @TableId("COMPANYID")
    private String companyid;

    /**
     * 企业名称
     */
    @TableField("COMPANYNAME")
    private String companyname;

    /**
     * 所属分组ID
     */
    @TableField("TEAMID")
    private BigDecimal teamid;

    /**
     * 创建日期
     */
    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    /**
     * 创建人ID
     */
    @TableField("STAFFID")
    private BigDecimal staffid;

    /**
     * 组织ID
     */
    @TableField("ORGID")
    private BigDecimal orgid;

    /**
     * 报告ID
     */
    @TableField("REPORTID")
    private BigDecimal reportid;

    /**
     * 风险状况
     */
    @TableField("FXTYPE")
    private String fxtype;

    /**
     * 状态
     */
    @TableField("CSTATUS")
    private BigDecimal cstatus;

    /**
     * 统一社会信用代码
     */
    @TableField("CREDIT_CODE")
    private String creditCode;

    /**
     * 法定代表人
     */
    @TableField("LEGAL_PERSON")
    private String legalPerson;

    /**
     * 企业状态
     */
    @TableField("ENT_STATUS")
    private String entStatus;

    /**
     * 注册资本
     */
    @TableField("REG_CAP")
    private String regCap;

    /**
     * 成立日期
     */
    @TableField("ESTABLISH_DATE")
    private String establishDate;

    /**
     * 登记机关
     */
    @TableField("REG_ORG")
    private String regOrg;

    /**
     * 经营范围
     */
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    /**
     * 外部监控项ID列表（逗号分隔）
     */
    @TableField("PRICEID")
    private String priceid;

    /**
     * 内部监控项ID列表（逗号分隔）
     */
    @TableField("PAGEID")
    private String pageid;

    /**
     * 更新时间
     */
    @TableField("UPDATETIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public BigDecimal getTeamid() {
        return teamid;
    }

    public void setTeamid(BigDecimal teamid) {
        this.teamid = teamid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }

    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }

    public BigDecimal getReportid() {
        return reportid;
    }

    public void setReportid(BigDecimal reportid) {
        this.reportid = reportid;
    }

    public String getFxtype() {
        return fxtype;
    }

    public void setFxtype(String fxtype) {
        this.fxtype = fxtype;
    }

    public BigDecimal getCstatus() {
        return cstatus;
    }

    public void setCstatus(BigDecimal cstatus) {
        this.cstatus = cstatus;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getEntStatus() {
        return entStatus;
    }

    public void setEntStatus(String entStatus) {
        this.entStatus = entStatus;
    }

    public String getRegCap() {
        return regCap;
    }

    public void setRegCap(String regCap) {
        this.regCap = regCap;
    }

    public String getPriceid() {
        return priceid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }

    public String getRegOrg() {
        return regOrg;
    }

    public void setRegOrg(String regOrg) {
        this.regOrg = regOrg;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "YyXdfCompany{" +
                "companyid='" + companyid + '\'' +
                ", companyname='" + companyname + '\'' +
                ", teamid=" + teamid +
                ", createdate=" + createdate +
                ", staffid=" + staffid +
                ", orgid=" + orgid +
                ", reportid=" + reportid +
                ", fxtype='" + fxtype + '\'' +
                ", cstatus=" + cstatus +
                ", creditCode='" + creditCode + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", entStatus='" + entStatus + '\'' +
                ", regCap='" + regCap + '\'' +
                ", establishDate='" + establishDate + '\'' +
                ", regOrg='" + regOrg + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", priceid='" + priceid + '\'' +
                ", pageid='" + pageid + '\'' +
                ", updatetime=" + updatetime +
                '}';
    }
}
