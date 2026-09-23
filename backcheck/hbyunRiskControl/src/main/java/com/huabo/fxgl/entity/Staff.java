package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author kangjx
 * @since 2022-07-15
 */
@Data
@TableName("TBL_STAFF")
@Component
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Staff  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT) 
    private BigDecimal staffid;

    private String realname;

    private String fixedphone;

    private String address;

    private String email;

    private String miblephone;

    private String memo;

    private BigDecimal orgid;

    private String username;

    private String password;

    private BigDecimal status;

    private BigDecimal roleid;

    /**
     * 入司时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createtime;

    private BigDecimal jobid;

    private BigDecimal outsideid;

    private String outsideopendid;

    private String emailcode;

    /**
     * 密码修改时间
     */
    private String updatetime;

    /**
     * 数据来源;
     */
    private String datasource;

    /**
     * 历史id值;
     */
    private String historycode;

    private String historydepartmentid;

    /**
     * 主管部门ids
     */
    private String manageorgs;

    private String manageorgnames;

    private String roleidstrs;

    private String pkymstaffid;

    @TableField(exist = false)
    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getFixedphone() {
        return fixedphone;
    }

    public void setFixedphone(String fixedphone) {
        this.fixedphone = fixedphone;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getMiblephone() {
        return miblephone;
    }

    public void setMiblephone(String miblephone) {
        this.miblephone = miblephone;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }
    public BigDecimal getRoleid() {
        return roleid;
    }

    public void setRoleid(BigDecimal roleid) {
        this.roleid = roleid;
    }
    public BigDecimal getJobid() {
        return jobid;
    }

    public void setJobid(BigDecimal jobid) {
        this.jobid = jobid;
    }
    public BigDecimal getOutsideid() {
        return outsideid;
    }

    public void setOutsideid(BigDecimal outsideid) {
        this.outsideid = outsideid;
    }
    public String getOutsideopendid() {
        return outsideopendid;
    }

    public void setOutsideopendid(String outsideopendid) {
        this.outsideopendid = outsideopendid;
    }
    public String getEmailcode() {
        return emailcode;
    }

    public void setEmailcode(String emailcode) {
        this.emailcode = emailcode;
    }


    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }
    public String getHistorycode() {
        return historycode;
    }

    public void setHistorycode(String historycode) {
        this.historycode = historycode;
    }
    public String getHistorydepartmentid() {
        return historydepartmentid;
    }

    public void setHistorydepartmentid(String historydepartmentid) {
        this.historydepartmentid = historydepartmentid;
    }
    public String getManageorgs() {
        return manageorgs;
    }

    public void setManageorgs(String manageorgs) {
        this.manageorgs = manageorgs;
    }
    public String getManageorgnames() {
        return manageorgnames;
    }

    public void setManageorgnames(String manageorgnames) {
        this.manageorgnames = manageorgnames;
    }
    public String getRoleidstrs() {
        return roleidstrs;
    }

    public void setRoleidstrs(String roleidstrs) {
        this.roleidstrs = roleidstrs;
    }
    public String getPkymstaffid() {
        return pkymstaffid;
    }

    public void setPkymstaffid(String pkymstaffid) {
        this.pkymstaffid = pkymstaffid;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffid=" + staffid +
                ", realname=" + realname +
                ", fixedphone=" + fixedphone +
                ", address=" + address +
                ", email=" + email +
                ", miblephone=" + miblephone +
                ", memo=" + memo +
                ", orgid=" + orgid +
                ", username=" + username +
                ", password="REDACTED", status=" + status +
                ", roleid=" + roleid +
                ", createtime=" + createtime +
                ", jobid=" + jobid +
                ", outsideid=" + outsideid +
                ", outsideopendid=" + outsideopendid +
                ", emailcode=" + emailcode +
                ", updatetime=" + updatetime +
                ", datasource=" + datasource +
                ", historycode=" + historycode +
                ", historydepartmentid=" + historydepartmentid +
                ", manageorgs=" + manageorgs +
                ", manageorgnames=" + manageorgnames +
                ", roleidstrs=" + roleidstrs +
                ", pkymstaffid=" + pkymstaffid +
                "}";
    }
}
