package com.global.treasurer.entity.derivatives;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_DERIVATIVES_VALUATION")
public class TblDerivativesValuation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "VALUATION_ID", type = IdType.AUTO)
    private Long valuationId;

    @TableField("CONTRACT_CODE")
    private String contractCode;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("UNDERLYING_ASSET")
    private String underlyingAsset;

    @TableField("NOTIONAL_AMOUNT")
    private BigDecimal notionalAmount;

    @TableField("MARKET_VALUE")
    private BigDecimal marketValue;

    @TableField("PRESENT_VALUE")
    private BigDecimal presentValue;

    @TableField("UNREALIZED_PNL")
    private BigDecimal unrealizedPnL;

    @TableField("VALUATION_DATE")
    private String valuationDate;

    @TableField("VALUATION_METHOD")
    private String valuationMethod;

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

    public Long getValuationId() { return valuationId; }
    public void setValuationId(Long valuationId) { this.valuationId = valuationId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getUnderlyingAsset() { return underlyingAsset; }
    public void setUnderlyingAsset(String underlyingAsset) { this.underlyingAsset = underlyingAsset; }
    public BigDecimal getNotionalAmount() { return notionalAmount; }
    public void setNotionalAmount(BigDecimal notionalAmount) { this.notionalAmount = notionalAmount; }
    public BigDecimal getMarketValue() { return marketValue; }
    public void setMarketValue(BigDecimal marketValue) { this.marketValue = marketValue; }
    public BigDecimal getPresentValue() { return presentValue; }
    public void setPresentValue(BigDecimal presentValue) { this.presentValue = presentValue; }
    public BigDecimal getUnrealizedPnL() { return unrealizedPnL; }
    public void setUnrealizedPnL(BigDecimal unrealizedPnL) { this.unrealizedPnL = unrealizedPnL; }
    public String getValuationDate() { return valuationDate; }
    public void setValuationDate(String valuationDate) { this.valuationDate = valuationDate; }
    public String getValuationMethod() { return valuationMethod; }
    public void setValuationMethod(String valuationMethod) { this.valuationMethod = valuationMethod; }
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

