package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@TableName("TBL_BUG_ATT")
@AllArgsConstructor
@NoArgsConstructor
public class BugAtt implements Serializable {

    private static final long serialVersionUID = 1L;
//    @TableId
    private BigDecimal bugid;
    private BigDecimal attid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }

    @Override
    public String toString() {
        return "BugAtt{" +
            "attid=" + attid +
            ", bugid=" + bugid +
        "}";
    }
}
