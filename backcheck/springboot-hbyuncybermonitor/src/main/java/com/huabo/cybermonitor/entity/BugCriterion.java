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
 * @since 2022-07-22
 */
@TableName("TBL_BUG_CRITERION")
public class BugCriterion implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal bugid;

    private BigDecimal bugcriid;

    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }
    public BigDecimal getBugcriid() {
        return bugcriid;
    }

    public void setBugcriid(BigDecimal bugcriid) {
        this.bugcriid = bugcriid;
    }

    @Override
    public String toString() {
        return "BugCriterion{" +
            "bugid=" + bugid +
            ", bugcriid=" + bugcriid +
        "}";
    }
}
