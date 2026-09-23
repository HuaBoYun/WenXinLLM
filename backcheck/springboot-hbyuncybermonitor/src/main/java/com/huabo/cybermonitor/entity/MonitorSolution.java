package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-21
 */

@TableName("TBL_MONITOR_SOLUTION")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class MonitorSolution implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value="SOLUTIONID",type=IdType.INPUT)
    private BigDecimal solutionid;

    private String solutioncode;

    private String solutionname;

    private String solutionstatus;

    private BigDecimal staffid;

    private LocalDateTime createdate;

    private String memo;

    private BigDecimal orgid;

    private BigDecimal runstatus;

    private BigDecimal type;

    private String exefrequncy;


    @TableField(exist = false)
    private String realname;

    @TableField(exist = false)
    private Set<Indicator> tblMonitorSolutionIndicators=new HashSet<>();

    public Set<Indicator> getTblMonitorSolutionIndicators() {
        return tblMonitorSolutionIndicators;
    }

    public void setTblMonitorSolutionIndicators(Set<Indicator> tblMonitorSolutionIndicators) {
        this.tblMonitorSolutionIndicators = tblMonitorSolutionIndicators;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public BigDecimal getSolutionid() {
        return solutionid;
    }


    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }
    public String getSolutioncode() {
        return solutioncode;
    }

    public void setSolutioncode(String solutioncode) {
        this.solutioncode = solutioncode;
    }
    public String getSolutionname() {
        return solutionname;
    }

    public void setSolutionname(String solutionname) {
        this.solutionname = solutionname;
    }
    public String getSolutionstatus() {
        return solutionstatus;
    }

    public void setSolutionstatus(String solutionstatus) {
        this.solutionstatus = solutionstatus;
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
    public BigDecimal getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(BigDecimal runstatus) {
        this.runstatus = runstatus;
    }
    public BigDecimal getType() {
        return type;
    }

    public void setType(BigDecimal type) {
        this.type = type;
    }
    public String getExefrequncy() {
        return exefrequncy;
    }

    public void setExefrequncy(String exefrequncy) {
        this.exefrequncy = exefrequncy;
    }

    @Override
    public String toString() {
        return "MonitorSolution{" +
            "solutionid=" + solutionid +
            ", solutioncode=" + solutioncode +
            ", solutionname=" + solutionname +
            ", solutionstatus=" + solutionstatus +
            ", staffid=" + staffid +
            ", createdate=" + createdate +
            ", memo=" + memo +
            ", orgid=" + orgid +
            ", runstatus=" + runstatus +
            ", type=" + type +
            ", exefrequncy=" + exefrequncy +
        "}";
    }
}
