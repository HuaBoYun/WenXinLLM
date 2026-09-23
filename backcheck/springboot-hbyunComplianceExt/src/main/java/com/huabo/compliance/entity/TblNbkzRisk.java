package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-01
 */
@TableName("TBL_NBKZ_RISK")
@Schema(name="TblNbkzRisk对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblNbkzRisk implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal riskid;

    @Schema(name="风险编号")
    private String risknumber;

    @Schema(name="风险名称")
    private String riskname;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(name="发生日期")
    private LocalDateTime occureddate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(name="发现日期")
    private LocalDateTime discovereddate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(name="创建日期")
    private LocalDateTime createdate;

    @Schema(name="责任部门")
    private BigDecimal orgid;

    @Schema(name="所属模块")
    private String stype;

    private BigDecimal createorid;
    // 新加属性  发现人
    @TableField(exist = false)
    private String    realname;


    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Schema(name="相关部门")
    private String sysorgid;

    @Schema(name="风险描述")
    private String riskdes;

    @Schema(name="事件说明")
    private String riskeventdescription;

    @Schema(name="损失事件定性类别")
    private String losseventcategory;

    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public String getRisknumber() {
        return risknumber;
    }

    public void setRisknumber(String risknumber) {
        this.risknumber = risknumber;
    }
    public String getRiskname() {
        return riskname;
    }

    public void setRiskname(String riskname) {
        this.riskname = riskname;
    }
    public LocalDateTime getOccureddate() {
        return occureddate;
    }

    public void setOccureddate(LocalDateTime occureddate) {
        this.occureddate = occureddate;
    }
    public LocalDateTime getDiscovereddate() {
        return discovereddate;
    }

    public void setDiscovereddate(LocalDateTime discovereddate) {
        this.discovereddate = discovereddate;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }
    public BigDecimal getCreateorid() {
        return createorid;
    }

    public void setCreateorid(BigDecimal createorid) {
        this.createorid = createorid;
    }
    public String getSysorgid() {
        return sysorgid;
    }

    public void setSysorgid(String sysorgid) {
        this.sysorgid = sysorgid;
    }
    public String getRiskdes() {
        return riskdes;
    }

    public void setRiskdes(String riskdes) {
        this.riskdes = riskdes;
    }
    public String getRiskeventdescription() {
        return riskeventdescription;
    }

    public void setRiskeventdescription(String riskeventdescription) {
        this.riskeventdescription = riskeventdescription;
    }
    public String getLosseventcategory() {
        return losseventcategory;
    }

    public void setLosseventcategory(String losseventcategory) {
        this.losseventcategory = losseventcategory;
    }

    @Override
    public String toString() {
        return "TblNbkzRisk{" +
            "riskid=" + riskid +
            ", risknumber=" + risknumber +
            ", riskname=" + riskname +
            ", occureddate=" + occureddate +
            ", discovereddate=" + discovereddate +
            ", createdate=" + createdate +
            ", orgid=" + orgid +
            ", stype=" + stype +
            ", createorid=" + createorid +
            ", sysorgid=" + sysorgid +
            ", riskdes=" + riskdes +
            ", riskeventdescription=" + riskeventdescription +
            ", losseventcategory=" + losseventcategory +
        "}";
    }
}
