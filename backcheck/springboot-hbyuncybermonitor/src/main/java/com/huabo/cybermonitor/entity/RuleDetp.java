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
 * @since 2022-07-14
 */
@TableName("TBL_RULE_DETP")
public class RuleDetp implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal orgid;

    private BigDecimal ruleid;

    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }

    @Override
    public String toString() {
        return "RuleDetp{" +
            "orgid=" + orgid +
            ", ruleid=" + ruleid +
        "}";
    }
}
