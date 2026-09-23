package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

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
@TableName("TBL_BUG_OUTERRULE")
public class BugOuterrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
	@Schema
    private BigDecimal bugid;

	@Schema
    private BigDecimal outrulid;

	@Schema
    @TableField(exist = false)
    private Integer outerid;

    public Integer getOuterid() {
        return outerid;
    }

    public void setOuterid(Integer outerid) {
        this.outerid = outerid;
    }

    public BigDecimal getBugid() {
        return bugid;
    }

    public void setBugid(BigDecimal bugid) {
        this.bugid = bugid;
    }
    public BigDecimal getOutrulid() {
        return outrulid;
    }

    public void setOutrulid(BigDecimal outrulid) {
        this.outrulid = outrulid;
    }

    @Override
    public String toString() {
        return "BugOuterrule{" +
            "bugid=" + bugid +
            ", outrulid=" + outrulid +
        "}";
    }
}
