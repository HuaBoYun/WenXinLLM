package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-26
 */
@TableName("TBL_MONITOR_SOLUTION_RULE")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class MonitorSolutionRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal ruleid;

    private BigDecimal solutionid;

    @TableId(type= IdType.INPUT)
    private BigDecimal solruleid;

    public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }
    public BigDecimal getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(BigDecimal solutionid) {
        this.solutionid = solutionid;
    }
    public BigDecimal getSolruleid() {
        return solruleid;
    }

    public void setSolruleid(BigDecimal solruleid) {
        this.solruleid = solruleid;
    }

    @Override
    public String toString() {
        return "MonitorSolutionRule{" +
            "ruleid=" + ruleid +
            ", solutionid=" + solutionid +
            ", solruleid=" + solruleid +
        "}";
    }
}
