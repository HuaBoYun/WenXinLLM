package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_DERIVATIVES_VALUATION_HISTORY")
public class TblDerivativesValuationHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.AUTO)
    private Long historyId;

    @TableField("VALUATION_DATE")
    private String valuationDate;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("TOTAL_VALUE")
    private BigDecimal totalValue;

    @TableField("DAILY_CHANGE")
    private BigDecimal dailyChange;

    @TableField("DAILY_CHANGE_RATE")
    private BigDecimal dailyChangeRate;

    @TableField("REMARK")
    private String remark;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("DEL_FLAG")
    private String delFlag;

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }
    public String getValuationDate() { return valuationDate; }
    public void setValuationDate(String valuationDate) { this.valuationDate = valuationDate; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    public BigDecimal getDailyChange() { return dailyChange; }
    public void setDailyChange(BigDecimal dailyChange) { this.dailyChange = dailyChange; }
    public BigDecimal getDailyChangeRate() { return dailyChangeRate; }
    public void setDailyChangeRate(BigDecimal dailyChangeRate) { this.dailyChangeRate = dailyChangeRate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
}

