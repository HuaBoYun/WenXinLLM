package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_INDICATOR_FLOW")
public class IndicatorFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal indicatorid;

    private BigDecimal flowid;

    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public BigDecimal getFlowid() {
        return flowid;
    }

    public void setFlowid(BigDecimal flowid) {
        this.flowid = flowid;
    }

    @Override
    public String toString() {
        return "IndicatorFlow{" +
            "indicatorid=" + indicatorid +
            ", flowid=" + flowid +
        "}";
    }
}
