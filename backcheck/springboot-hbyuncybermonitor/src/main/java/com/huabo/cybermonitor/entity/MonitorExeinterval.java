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
 * @since 2022-07-30
 */
@TableName("TBL_MONITOR_EXEINTERVAL")
public class MonitorExeinterval implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal exeintervalid;

    private String exeinterval;

    private BigDecimal rulesolutionid;

    private BigDecimal modelsolutionid;

    public BigDecimal getExeintervalid() {
        return exeintervalid;
    }

    public void setExeintervalid(BigDecimal exeintervalid) {
        this.exeintervalid = exeintervalid;
    }
    public String getExeinterval() {
        return exeinterval;
    }

    public void setExeinterval(String exeinterval) {
        this.exeinterval = exeinterval;
    }
    public BigDecimal getRulesolutionid() {
        return rulesolutionid;
    }

    public void setRulesolutionid(BigDecimal rulesolutionid) {
        this.rulesolutionid = rulesolutionid;
    }
    public BigDecimal getModelsolutionid() {
        return modelsolutionid;
    }

    public void setModelsolutionid(BigDecimal modelsolutionid) {
        this.modelsolutionid = modelsolutionid;
    }

    @Override
    public String toString() {
        return "MonitorExeinterval{" +
            "exeintervalid=" + exeintervalid +
            ", exeinterval=" + exeinterval +
            ", rulesolutionid=" + rulesolutionid +
            ", modelsolutionid=" + modelsolutionid +
        "}";
    }
}
