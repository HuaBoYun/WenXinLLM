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
 * @since 2022-07-22
 */
@TableName("TBL_MONITOR_MODELRESULT")
public class MonitorModelresult implements Serializable {
    public static Integer YJYJ = 1;
    public static Integer ZJ=0;

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal resultid;

    private LocalDateTime savetime;

    private BigDecimal staffid;

    private BigDecimal modelid;

    private BigDecimal solutionresultid;

    private String signid;

    private BigDecimal indexsql;

    private BigDecimal isdata;

    public BigDecimal getResultid() {
        return resultid;
    }

    public void setResultid(BigDecimal resultid) {
        this.resultid = resultid;
    }
    public LocalDateTime getSavetime() {
        return savetime;
    }

    public void setSavetime(LocalDateTime savetime) {
        this.savetime = savetime;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getModelid() {
        return modelid;
    }

    public void setModelid(BigDecimal modelid) {
        this.modelid = modelid;
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
    public BigDecimal getIndexsql() {
        return indexsql;
    }

    public void setIndexsql(BigDecimal indexsql) {
        this.indexsql = indexsql;
    }
    public BigDecimal getIsdata() {
        return isdata;
    }

    public void setIsdata(BigDecimal isdata) {
        this.isdata = isdata;
    }

    @Override
    public String toString() {
        return "MonitorModelresult{" +
            "resultid=" + resultid +
            ", savetime=" + savetime +
            ", staffid=" + staffid +
            ", modelid=" + modelid +
            ", solutionresultid=" + solutionresultid +
            ", signid=" + signid +
            ", indexsql=" + indexsql +
            ", isdata=" + isdata +
        "}";
    }
}
