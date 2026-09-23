package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-01
 */
@TableName("TBL_AMORG_PROBLEM")
@Schema(name="TblAmorgProblem对象")

public class TblAmorgProblem implements Serializable {

    private static final long serialVersionUID = 1L;
    // 外键
    private BigDecimal problemid;

    private BigDecimal amorgid;

    private BigDecimal bugid;

    private BigDecimal riskid;

    private BigDecimal sheetid;

    public BigDecimal getProblemid() {
        return problemid;
    }

    public void setProblemid(BigDecimal problemid) {
        this.problemid = problemid;
    }
    public BigDecimal getAmorgid() {
        return amorgid;
    }

    public void setAmorgid(BigDecimal amorgid) {
        this.amorgid = amorgid;
    }
    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }
    public BigDecimal getRiskid() {
        return riskid;
    }

    public void setRiskid(BigDecimal riskid) {
        this.riskid = riskid;
    }
    public BigDecimal getSheetid() {
        return sheetid;
    }

    public void setSheetid(BigDecimal sheetid) {
        this.sheetid = sheetid;
    }

    @Override
    public String toString() {
        return "TblAmorgProblem{" +
            "problemid=" + problemid +
            ", amorgid=" + amorgid +
            ", bugid=" + bugid +
            ", riskid=" + riskid +
            ", sheetid=" + sheetid +
        "}";
    }
}
