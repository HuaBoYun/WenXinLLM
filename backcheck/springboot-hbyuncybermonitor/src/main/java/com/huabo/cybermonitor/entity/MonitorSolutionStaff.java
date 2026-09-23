package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-26
 */
@TableName("TBL_MONITOR_SOLUTION_STAFF")
public class MonitorSolutionStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal solutionid;

    private BigDecimal staffid;

    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }

    @Override
    public String toString() {
        return "MonitorSolutionStaff{" +
            "solutionid=" + solutionid +
            ", staffid=" + staffid +
        "}";
    }
}
