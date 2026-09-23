package com.huabo.cybermonitor.entity;

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
 * @since 2022-08-11
 */
@TableName("TBL_MONITOR_INDICATORRESULT")
public class MonitorIndicatorresult implements Serializable {

    private static final long serialVersionUID = 1L;
    public static Integer YJYJ = 1;
    public static Integer ZJ=0;


    private BigDecimal resultid;

    private Float score;

    private BigDecimal indicatorid;

    private BigDecimal staffid;

    private BigDecimal solutionresultid;

    private LocalDateTime savetime;

    private BigDecimal source;

    private String sign;

    private String tolerance;

    private String executeid;

    public BigDecimal getResultid() {
        return resultid;
    }

    public void setResultid(BigDecimal resultid) {
        this.resultid = resultid;
    }
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
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
    public LocalDateTime getSavetime() {
        return savetime;
    }

    public void setSavetime(LocalDateTime savetime) {
        this.savetime = savetime;
    }
    public BigDecimal getSource() {
        return source;
    }

    public void setSource(BigDecimal source) {
        this.source = source;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }
    public String getExecuteid() {
        return executeid;
    }

    public void setExecuteid(String executeid) {
        this.executeid = executeid;
    }

    @Override
    public String toString() {
        return "MonitorIndicatorresult{" +
            "resultid=" + resultid +
            ", score=" + score +
            ", indicatorid=" + indicatorid +
            ", staffid=" + staffid +
            ", solutionresultid=" + solutionresultid +
            ", savetime=" + savetime +
            ", source=" + source +
            ", sign=" + sign +
            ", tolerance=" + tolerance +
            ", executeid=" + executeid +
        "}";
    }
}
