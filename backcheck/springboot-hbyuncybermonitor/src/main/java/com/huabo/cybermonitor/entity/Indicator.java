package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_INDICATOR")
public class Indicator implements Serializable {

    private static final long serialVersionUID = 1L;
    /**是否行业库  0标识否*/
    public static final Integer IS_HY0 = 0;
    /**是否行业库  1标识是*/
    public static final Integer IS_HY1 = 1;
    /**是否行业库  2标识是通用行业*/
    public static final Integer IS_HY2 = 2;

    @TableId("INDICATORID")
    private BigDecimal indicatorid;
    @TableField("INDICATORCODE")
    private String indicatorcode;
    @TableField("INDICATORNAME")
    private String indicatorname;

    @TableField("FORMULA")
    private String formula;

    @TableField("INDICATORDES")
    private String indicatordes;
    @TableField("MEMO")
    private String memo;
    @TableField("ORGID")
    private Integer orgid;
    @TableField("DEPARTMENTINCHARGE")
    private String departmentincharge;
    @TableField("INDICATORSTATUS")
    private String indicatorstatus;
    @TableField("AUDITINGSTATUS")
    private String auditingstatus;

    @TableField("CREATEDATE")
    private LocalDateTime createdate;

    @TableField("UNITTYPE")
    private String unittype;

    @TableField("FORMULADES")
    private String formulades;

    @TableField("INDCATID")
    private BigDecimal indcatid;

    @TableField("RUNSTATUS")
    private BigDecimal runstatus;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("CONNECTIONSTRINGS")
    private String connectionstrings;

    @TableField("INDICATORDB")
    private Integer indicatordb;
    @TableField("FORLUMACHS")
    private String forlumachs;

    @TableField(exist = false)
    private String  realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public String getIndicatorcode() {
        return indicatorcode;
    }

    public void setIndicatorcode(String indicatorcode) {
        this.indicatorcode = indicatorcode;
    }
    public String getIndicatorname() {
        return indicatorname;
    }

    public void setIndicatorname(String indicatorname) {
        this.indicatorname = indicatorname;
    }
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
    public String getIndicatordes() {
        return indicatordes;
    }

    public void setIndicatordes(String indicatordes) {
        this.indicatordes = indicatordes;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }
    public String getDepartmentincharge() {
        return departmentincharge;
    }

    public void setDepartmentincharge(String departmentincharge) {
        this.departmentincharge = departmentincharge;
    }
    public String getIndicatorstatus() {
        return indicatorstatus;
    }

    public void setIndicatorstatus(String indicatorstatus) {
        this.indicatorstatus = indicatorstatus;
    }
    public String getAuditingstatus() {
        return auditingstatus;
    }

    public void setAuditingstatus(String auditingstatus) {
        this.auditingstatus = auditingstatus;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public String getUnittype() {
        return unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }
    public String getFormulades() {
        return formulades;
    }

    public void setFormulades(String formulades) {
        this.formulades = formulades;
    }
    public BigDecimal getIndcatid() {
        return indcatid;
    }

    public void setIndcatid(BigDecimal indcatid) {
        this.indcatid = indcatid;
    }
    public BigDecimal getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(BigDecimal runstatus) {
        this.runstatus = runstatus;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getConnectionstrings() {
        return connectionstrings;
    }

    public void setConnectionstrings(String connectionstrings) {
        this.connectionstrings = connectionstrings;
    }
    public Integer getIndicatordb() {
        return indicatordb;
    }

    public void setIndicatordb(Integer indicatordb) {
        this.indicatordb = indicatordb;
    }
    public String getForlumachs() {
        return forlumachs;
    }

    public void setForlumachs(String forlumachs) {
        this.forlumachs = forlumachs;
    }

    @Override
    public String toString() {
        return "Indicator{" +
            "indicatorid=" + indicatorid +
            ", indicatorcode=" + indicatorcode +
            ", indicatorname=" + indicatorname +
            ", formula=" + formula +
            ", indicatordes=" + indicatordes +
            ", memo=" + memo +
            ", orgid=" + orgid +
            ", departmentincharge=" + departmentincharge +
            ", indicatorstatus=" + indicatorstatus +
            ", auditingstatus=" + auditingstatus +
            ", createdate=" + createdate +
            ", unittype=" + unittype +
            ", formulades=" + formulades +
            ", indcatid=" + indcatid +
            ", runstatus=" + runstatus +
            ", staffid=" + staffid +
            ", connectionstrings=" + connectionstrings +
            ", indicatordb=" + indicatordb +
            ", forlumachs=" + forlumachs +
        "}";
    }
}
