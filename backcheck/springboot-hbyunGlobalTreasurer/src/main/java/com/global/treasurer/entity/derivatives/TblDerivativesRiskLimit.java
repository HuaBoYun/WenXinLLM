package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_DERIVATIVES_RISK_LIMIT")
public class TblDerivativesRiskLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LIMIT_ID", type = IdType.AUTO)
    private Long limitId;

    @TableField("LIMIT_TYPE")
    private String limitType;

    @TableField("LIMIT_AMOUNT")
    private BigDecimal limitAmount;

    @TableField("USED_AMOUNT")
    private BigDecimal usedAmount;

    @TableField("AVAILABLE_AMOUNT")
    private BigDecimal availableAmount;

    @TableField("USAGE_RATIO")
    private BigDecimal usageRatio;

    @TableField("CURRENCY")
    private String currency;

    @TableField("STATUS")
    private String status;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;

    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public BigDecimal getLimitAmount() { return limitAmount; }
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
    public BigDecimal getUsedAmount() { return usedAmount; }
    public void setUsedAmount(BigDecimal usedAmount) { this.usedAmount = usedAmount; }
    public BigDecimal getAvailableAmount() { return availableAmount; }
    public void setAvailableAmount(BigDecimal availableAmount) { this.availableAmount = availableAmount; }
    public BigDecimal getUsageRatio() { return usageRatio; }
    public void setUsageRatio(BigDecimal usageRatio) { this.usageRatio = usageRatio; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}

