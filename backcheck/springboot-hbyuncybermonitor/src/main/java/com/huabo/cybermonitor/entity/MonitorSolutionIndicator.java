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
@TableName("TBL_MONITOR_SOLUTION_INDICATOR")
public class MonitorSolutionIndicator implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal indicatorid;

    private BigDecimal solutionid;

    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }

    @Override
    public String toString() {
        return "MonitorSolutionIndicator{" +
            "indicatorid=" + indicatorid +
            ", solutionid=" + solutionid +
        "}";
    }
}
