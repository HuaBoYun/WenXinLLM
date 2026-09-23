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
@TableName("TBL_RISK_RISK_ATT")
public class RiskRiskAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal riskresid;

    private String riskresname;

    private BigDecimal attid;

    private BigDecimal riskid;

    private String riskresdes;

    private String riskrescode;

    private Risk risk;

    private Attachment attachment;

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public BigDecimal getRiskresid() {
        return riskresid;
    }

    public void setRiskresid(BigDecimal riskresid) {
        this.riskresid = riskresid;
    }
    public String getRiskresname() {
        return riskresname;
    }

    public void setRiskresname(String riskresname) {
        this.riskresname = riskresname;
    }
    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public String getRiskresdes() {
        return riskresdes;
    }

    public void setRiskresdes(String riskresdes) {
        this.riskresdes = riskresdes;
    }
    public String getRiskrescode() {
        return riskrescode;
    }

    public void setRiskrescode(String riskrescode) {
        this.riskrescode = riskrescode;
    }

    @Override
    public String toString() {
        return "RiskRiskAtt{" +
            "riskresid=" + riskresid +
            ", riskresname=" + riskresname +
            ", attid=" + attid +
            ", riskid=" + riskid +
            ", riskresdes=" + riskresdes +
            ", riskrescode=" + riskrescode +
        "}";
    }
}
