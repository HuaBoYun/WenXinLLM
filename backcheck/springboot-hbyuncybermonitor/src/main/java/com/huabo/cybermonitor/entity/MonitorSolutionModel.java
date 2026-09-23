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
@TableName("TBL_MONITOR_SOLUTION_MODEL")
public class MonitorSolutionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal modelid;

    private BigDecimal solutionid;

    public BigDecimal getModelid() {
        return modelid;
    }

    public void setModelid(BigDecimal modelid) {
        this.modelid = modelid;
    }
    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }

    @Override
    public String toString() {
        return "MonitorSolutionModel{" +
            "modelid=" + modelid +
            ", solutionid=" + solutionid +
        "}";
    }
}
