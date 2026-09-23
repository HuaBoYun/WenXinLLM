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
 * @since 2022-08-15
 * @since 2022-08-05
 */
@TableName("TBL_BUG_INNERRULE")
public class BugInnerrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal bugid;

    private BigDecimal innrulid;

    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }
    public BigDecimal getInnrulid() {
        return innrulid;
    }

    public void setInnrulid(BigDecimal innrulid) {
        this.innrulid = innrulid;
    }

    @Override
    public String toString() {
        return "BugInnerrule{" +
            "bugid=" + bugid +
            ", innrulid=" + innrulid +
        "}";
    }
}
