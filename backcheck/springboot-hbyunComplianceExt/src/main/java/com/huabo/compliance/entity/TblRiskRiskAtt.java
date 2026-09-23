package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-13
 */
@TableName("TBL_RISK_RISK_ATT")
@Schema(name="TblRiskRiskAtt对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblRiskRiskAtt implements Serializable {


    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    private BigDecimal riskresid;

    private String riskresname;

    private BigDecimal attid;

    private BigDecimal riskid;

    private String riskresdes;

    private String riskrescode;

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
        return "TblRiskRiskAtt{" +
            "riskresid=" + riskresid +
            ", riskresname=" + riskresname +
            ", attid=" + attid +
            ", riskid=" + riskid +
            ", riskresdes=" + riskresdes +
            ", riskrescode=" + riskrescode +
        "}";
    }
}
