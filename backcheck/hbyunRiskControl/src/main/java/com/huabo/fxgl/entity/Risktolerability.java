package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

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
@TableName("TBL_RISKTOLERABILITY")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Risktolerability implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal riskid;

    private Risk risk;

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    @TableId(type = IdType.INPUT)
    private BigDecimal toleid;

    private String rtcode;

    private BigDecimal lowerborder;

    private BigDecimal upperborder;

    private String colorstring;

    private String description;

    private String memo;

    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public BigDecimal getToleid() {
        return toleid;
    }

    public void setToleid(BigDecimal toleid) {
        this.toleid = toleid;
    }
    public String getRtcode() {
        return rtcode;
    }

    public void setRtcode(String rtcode) {
        this.rtcode = rtcode;
    }
    public BigDecimal getLowerborder() {
        return lowerborder;
    }

    public void setLowerborder(BigDecimal lowerborder) {
        this.lowerborder = lowerborder;
    }
    public BigDecimal getUpperborder() {
        return upperborder;
    }

    public void setUpperborder(BigDecimal upperborder) {
        this.upperborder = upperborder;
    }
    public String getColorstring() {
        return colorstring;
    }

    public void setColorstring(String colorstring) {
        this.colorstring = colorstring;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "Risktolerability{" +
            "riskid=" + riskid +
            ", toleid=" + toleid +
            ", rtcode=" + rtcode +
            ", lowerborder=" + lowerborder +
            ", upperborder=" + upperborder +
            ", colorstring=" + colorstring +
            ", description=" + description +
            ", memo=" + memo +
        "}";
    }
}
