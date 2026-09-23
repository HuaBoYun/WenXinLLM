package com.huabo.cybermonitor.entity;

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
 * @since 2022-08-12
 */
@TableName("TBL_MONITOR_PREWARNING")
public class MonitorPrewarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal prewarningid;

    private LocalDateTime savedatetime;

    private String memo;

    private BigDecimal ruleid;

    private BigDecimal staffid;

    private BigDecimal solutionresultid;

    private String signid;

    private BigDecimal resultcount;

    private BigDecimal datalinkid;

    private BigDecimal modelid;

    public BigDecimal getPrewarningid() {
        return prewarningid;
    }

    public void setPrewarningid(BigDecimal prewarningid) {
        this.prewarningid = prewarningid;
    }
    public LocalDateTime getSavedatetime() {
        return savedatetime;
    }

    public void setSavedatetime(LocalDateTime savedatetime) {
        this.savedatetime = savedatetime;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getSolutionresultid() {
        return solutionresultid;
    }

    public void setSolutionresultid(BigDecimal solutionresultid) {
        this.solutionresultid = solutionresultid;
    }
    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }
    public BigDecimal getResultcount() {
        return resultcount;
    }

    public void setResultcount(BigDecimal resultcount) {
        this.resultcount = resultcount;
    }
    public BigDecimal getDatalinkid() {
        return datalinkid;
    }

    public void setDatalinkid(BigDecimal datalinkid) {
        this.datalinkid = datalinkid;
    }
    public BigDecimal getModelid() {
        return modelid;
    }

    public void setModelid(BigDecimal modelid) {
        this.modelid = modelid;
    }

    @Override
    public String toString() {
        return "MonitorPrewarning{" +
            "prewarningid=" + prewarningid +
            ", savedatetime=" + savedatetime +
            ", memo=" + memo +
            ", ruleid=" + ruleid +
            ", staffid=" + staffid +
            ", solutionresultid=" + solutionresultid +
            ", signid=" + signid +
            ", resultcount=" + resultcount +
            ", datalinkid=" + datalinkid +
            ", modelid=" + modelid +
        "}";
    }
}
