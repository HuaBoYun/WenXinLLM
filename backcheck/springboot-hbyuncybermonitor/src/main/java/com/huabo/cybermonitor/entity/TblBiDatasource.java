package com.huabo.cybermonitor.entity;

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
 * @since 2022-09-07
 */
@TableName("TBL_BI_DATASOURCE")
@Schema(name="TblBiDatasource对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblBiDatasource implements Serializable {


    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    private BigDecimal dsid;

    private String dsname;

    private String dstype;

    private String sqlstring;

    private BigDecimal orgid;

    private String memo1;

    private String memo2;

    private BigDecimal staffid;

    private LocalDateTime createdate;

    private BigDecimal forbidden;

    private String isleaf;

    private BigDecimal fatherid;

    private String dsdes;

    public BigDecimal getDsid() {
        return dsid;
    }

    public void setDsid(BigDecimal dsid) {
        this.dsid = dsid;
    }
    public String getDsname() {
        return dsname;
    }

    public void setDsname(String dsname) {
        this.dsname = dsname;
    }
    public String getDstype() {
        return dstype;
    }

    public void setDstype(String dstype) {
        this.dstype = dstype;
    }
    public String getSqlstring() {
        return sqlstring;
    }

    public void setSqlstring(String sqlstring) {
        this.sqlstring = sqlstring;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getMemo1() {
        return memo1;
    }

    public void setMemo1(String memo1) {
        this.memo1 = memo1;
    }
    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public BigDecimal getForbidden() {
        return forbidden;
    }

    public void setForbidden(BigDecimal forbidden) {
        this.forbidden = forbidden;
    }
    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }
    public BigDecimal getFatherid() {
        return fatherid;
    }

    public void setFatherid(BigDecimal fatherid) {
        this.fatherid = fatherid;
    }
    public String getDsdes() {
        return dsdes;
    }

    public void setDsdes(String dsdes) {
        this.dsdes = dsdes;
    }

    @Override
    public String toString() {
        return "TblBiDatasource{" +
            "dsid=" + dsid +
            ", dsname=" + dsname +
            ", dstype=" + dstype +
            ", sqlstring=" + sqlstring +
            ", orgid=" + orgid +
            ", memo1=" + memo1 +
            ", memo2=" + memo2 +
            ", staffid=" + staffid +
            ", createdate=" + createdate +
            ", forbidden=" + forbidden +
            ", isleaf=" + isleaf +
            ", fatherid=" + fatherid +
            ", dsdes=" + dsdes +
        "}";
    }
}
