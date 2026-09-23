package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@TableName("TBL_YY_REPORT_MODEL")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class YyReportModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal reportid;

    private String reportname;

    private String reporturl;

    private BigDecimal annualprice;

    private BigDecimal reportprice;

    private BigDecimal orgid;

    private BigDecimal staffid;

    private String priceid;

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
    public String getReporturl() {
        return reporturl;
    }

    public void setReporturl(String reporturl) {
        this.reporturl = reporturl;
    }
    public BigDecimal getAnnualprice() {
        return annualprice;
    }

    public void setAnnualprice(BigDecimal annualprice) {
        this.annualprice = annualprice;
    }
    public BigDecimal getReportprice() {
        return reportprice;
    }

    public void setReportprice(BigDecimal reportprice) {
        this.reportprice = reportprice;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getPriceid() {
        return priceid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }

    @Override
    public String toString() {
        return "YyReportModel{" +
            "reportid=" + reportid +
            ", reportname=" + reportname +
            ", reporturl=" + reporturl +
            ", annualprice=" + annualprice +
            ", reportprice=" + reportprice +
            ", orgid=" + orgid +
            ", staffid=" + staffid +
            ", priceid=" + priceid +
        "}";
    }
}
