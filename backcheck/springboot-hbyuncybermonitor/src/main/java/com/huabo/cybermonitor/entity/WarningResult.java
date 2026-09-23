package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@TableName("TBL_WARNING_RESULT")
public class WarningResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private BigDecimal warningid;

    private LocalDateTime warningdate;

    private Long staffid;

    private String indicatorcount;

    private String rulecount;

    private String modelcount;

    public BigDecimal getWarningid() {
        return warningid;
    }

    public void setWarningid(BigDecimal warningid) {
        this.warningid = warningid;
    }
    public LocalDateTime getWarningdate() {
        return warningdate;
    }

    public void setWarningdate(LocalDateTime warningdate) {
        this.warningdate = warningdate;
    }
    public Long getStaffid() {
        return staffid;
    }

    public void setStaffid(Long staffid) {
        this.staffid = staffid;
    }
    public String getIndicatorcount() {
        return indicatorcount;
    }

    public void setIndicatorcount(String indicatorcount) {
        this.indicatorcount = indicatorcount;
    }
    public String getRulecount() {
        return rulecount;
    }

    public void setRulecount(String rulecount) {
        this.rulecount = rulecount;
    }
    public String getModelcount() {
        return modelcount;
    }

    public void setModelcount(String modelcount) {
        this.modelcount = modelcount;
    }

    @Override
    public String toString() {
        return "WarningResult{" +
            "warningid=" + warningid +
            ", warningdate=" + warningdate +
            ", staffid=" + staffid +
            ", indicatorcount=" + indicatorcount +
            ", rulecount=" + rulecount +
            ", modelcount=" + modelcount +
        "}";
    }
}
