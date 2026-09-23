package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_DERIVATIVES_LIMIT_HISTORY")
public class TblDerivativesLimitHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.AUTO)
    private Long historyId;

    @TableField("LIMIT_ID")
    private Long limitId;

    @TableField("CHANGE_TYPE")
    private String changeType;

    @TableField("OLD_VALUE")
    private BigDecimal oldValue;

    @TableField("NEW_VALUE")
    private BigDecimal newValue;

    @TableField("OPERATOR")
    private String operator;

    @TableField("CHANGE_TIME")
    private Date changeTime;

    @TableField("ORG_ID")
    private Long orgId;

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }
    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public BigDecimal getOldValue() { return oldValue; }
    public void setOldValue(BigDecimal oldValue) { this.oldValue = oldValue; }
    public BigDecimal getNewValue() { return newValue; }
    public void setNewValue(BigDecimal newValue) { this.newValue = newValue; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public Date getChangeTime() { return changeTime; }
    public void setChangeTime(Date changeTime) { this.changeTime = changeTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}

