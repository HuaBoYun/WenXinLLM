package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_FORM_CONTROLRULE")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class FormControlrule implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    private BigDecimal ruleid;

    private String ruleno;

    private String rulename;

    private BigDecimal rulestatus;

    private String rulememo;

    private BigDecimal ruletype;

    private String bookid;

    private String rulesql;

    private BigDecimal createstaff;

    private LocalDateTime createtime;

    private BigDecimal deptid;

    private BigDecimal orgid;

    private String ruletip;

    private String ruledatetype;

    private String dateusername;

    private String dateuserpwd;

    private String dateurl;

    private String returnresult;

    private BigDecimal fieldid;

    public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }
    public String getRuleno() {
        return ruleno;
    }

    public void setRuleno(String ruleno) {
        this.ruleno = ruleno;
    }
    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }
    public BigDecimal getRulestatus() {
        return rulestatus;
    }

    public void setRulestatus(BigDecimal rulestatus) {
        this.rulestatus = rulestatus;
    }
    public String getRulememo() {
        return rulememo;
    }

    public void setRulememo(String rulememo) {
        this.rulememo = rulememo;
    }
    public BigDecimal getRuletype() {
        return ruletype;
    }

    public void setRuletype(BigDecimal ruletype) {
        this.ruletype = ruletype;
    }
    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
    public String getRulesql() {
        return rulesql;
    }

    public void setRulesql(String rulesql) {
        this.rulesql = rulesql;
    }
    public BigDecimal getCreatestaff() {
        return createstaff;
    }

    public void setCreatestaff(BigDecimal createstaff) {
        this.createstaff = createstaff;
    }
    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }
    public BigDecimal getDeptid() {
        return deptid;
    }

    public void setDeptid(BigDecimal deptid) {
        this.deptid = deptid;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getRuletip() {
        return ruletip;
    }

    public void setRuletip(String ruletip) {
        this.ruletip = ruletip;
    }
    public String getRuledatetype() {
        return ruledatetype;
    }

    public void setRuledatetype(String ruledatetype) {
        this.ruledatetype = ruledatetype;
    }
    public String getDateusername() {
        return dateusername;
    }

    public void setDateusername(String dateusername) {
        this.dateusername = dateusername;
    }
    public String getDateuserpwd() {
        return dateuserpwd;
    }

    public void setDateuserpwd(String dateuserpwd) {
        this.dateuserpwd = dateuserpwd;
    }
    public String getDateurl() {
        return dateurl;
    }

    public void setDateurl(String dateurl) {
        this.dateurl = dateurl;
    }
    public String getReturnresult() {
        return returnresult;
    }

    public void setReturnresult(String returnresult) {
        this.returnresult = returnresult;
    }
    public BigDecimal getFieldid() {
        return fieldid;
    }

    public void setFieldid(BigDecimal fieldid) {
        this.fieldid = fieldid;
    }

    @Override
    public String toString() {
        return "FormControlrule{" +
                "ruleid=" + ruleid +
                ", ruleno=" + ruleno +
                ", rulename=" + rulename +
                ", rulestatus=" + rulestatus +
                ", rulememo=" + rulememo +
                ", ruletype=" + ruletype +
                ", bookid=" + bookid +
                ", rulesql=" + rulesql +
                ", createstaff=" + createstaff +
                ", createtime=" + createtime +
                ", deptid=" + deptid +
                ", orgid=" + orgid +
                ", ruletip=" + ruletip +
                ", ruledatetype=" + ruledatetype +
                ", dateusername=" + dateusername +
                ", dateuserpwd="REDACTED", dateurl=" + dateurl +
                ", returnresult=" + returnresult +
                ", fieldid=" + fieldid +
                "}";
    }

}
