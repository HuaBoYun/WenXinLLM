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
 * @since 2022-07-22
 */
@TableName("TBL_MONITOR_SOLUTIONRESULT")
public class MonitorSolutionresult implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Integer YJYJ=1;
    /**
     * 监控执行类型
     */
    public static final Integer ZKZX=3;
    public static final Integer ZX=2;
    @TableId
    private BigDecimal solutionresultid;

    private LocalDateTime savetime;

    private String memo;

    private BigDecimal staffid;

    private BigDecimal solutionid;

    private BigDecimal source;

    @TableField(exist = false)
    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }
    public BigDecimal getSource() {
        return source;
    }

    public void setSource(BigDecimal source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "MonitorSolutionresult{" +
            "solutionresultid=" + solutionresultid +
            ", savetime=" + savetime +
            ", memo=" + memo +
            ", staffid=" + staffid +
            ", solutionid=" + solutionid +
            ", source=" + source +
        "}";
    }
}
