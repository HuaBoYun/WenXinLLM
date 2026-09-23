package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@TableName("TBL_RISK_INNERRULE")
public class RiskInnerrule implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal innrulid;

    private BigDecimal riskid;

    public BigDecimal getInnrulid() {
        return innrulid;
    }

    public void setInnrulid(BigDecimal innrulid) {
        this.innrulid = innrulid;
    }
    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }

    @Override
    public String toString() {
        return "RiskInnerrule{" +
            "innrulid=" + innrulid +
            ", riskid=" + riskid +
        "}";
    }
}
