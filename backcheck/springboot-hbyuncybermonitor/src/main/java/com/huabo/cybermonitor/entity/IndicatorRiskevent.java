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
@TableName("TBL_INDICATOR_RISKEVENT")
public class IndicatorRiskevent implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal indicatorid;

    private BigDecimal riseveid;

    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public BigDecimal getRiseveid() {
        return riseveid;
    }

    public void setRiseveid(BigDecimal riseveid) {
        this.riseveid = riseveid;
    }

    @Override
    public String toString() {
        return "IndicatorRiskevent{" +
            "indicatorid=" + indicatorid +
            ", riseveid=" + riseveid +
        "}";
    }
}
