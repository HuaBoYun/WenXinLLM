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
 * @since 2022-07-13
 */
@TableName("TBL_INDICATOR_CM")
public class IndicatorCm implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal indicatorid;

    @TableId
    private BigDecimal conmatid;

    public BigDecimal getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(BigDecimal indicatorid) {
        this.indicatorid = indicatorid;
    }
    public BigDecimal getConmatid() {
        return conmatid;
    }

    public void setConmatid(BigDecimal conmatid) {
        this.conmatid = conmatid;
    }

    @Override
    public String toString() {
        return "IndicatorCm{" +
            "indicatorid=" + indicatorid +
            ", conmatid=" + conmatid +
        "}";
    }
}
