package com.huabo.fxgl.entity;

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
@TableName("TBL_RISK_RISKEVENT")
public class RiskRiskevent implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal riskid;

    private BigDecimal riseveid;

    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public BigDecimal getRiseveid() {
        return riseveid;
    }

    public void setRiseveid(BigDecimal riseveid) {
        this.riseveid = riseveid;
    }

    @Override
    public String toString() {
        return "RiskRiskevent{" +
            "riskid=" + riskid +
            ", riseveid=" + riseveid +
        "}";
    }
}
