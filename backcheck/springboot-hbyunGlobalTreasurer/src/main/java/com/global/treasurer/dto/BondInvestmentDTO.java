package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 债券投资DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@ApiModel(value = "BondInvestmentDTO", description = "债券投资数据传输对象")
@Data
public class BondInvestmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("债券投资ID")
    private Long investmentId;

    @ApiModelProperty("投资编号")
    private String investmentNo;

    @ApiModelProperty("债券代码")
    private String bondCode;

    @ApiModelProperty("债券名称")
    private String bondName;

    @ApiModelProperty("债券类型")
    private String bondType;

    @ApiModelProperty("发行机构")
    private String issuer;

    @ApiModelProperty("信用评级")
    private String creditRating;

    @ApiModelProperty("购买价格")
    private BigDecimal purchasePrice;

    @ApiModelProperty("购买数量")
    private Integer purchaseQuantity;

    @ApiModelProperty("投资金额")
    private BigDecimal investmentAmount;

    @ApiModelProperty("面值")
    private BigDecimal faceValue;

    @ApiModelProperty("票面利率")
    private BigDecimal couponRate;

    @ApiModelProperty("到期收益率")
    private BigDecimal yieldToMaturity;

    @ApiModelProperty("付息频率")
    private String couponFrequency;

    @ApiModelProperty("购买日期")
    private Date purchaseDate;

    @ApiModelProperty("到期日期")
    private Date maturityDate;

    @ApiModelProperty("下次付息日期")
    private Date nextCouponDate;

    @ApiModelProperty("累计利息")
    private BigDecimal accumulatedInterest;

    @ApiModelProperty("当前市值")
    private BigDecimal currentMarketValue;

    @ApiModelProperty("未实现盈亏")
    private BigDecimal unrealizedPnl;

    @ApiModelProperty("风险等级")
    private String riskLevel;

    @ApiModelProperty("投资状态")
    private String investmentStatus;

    @ApiModelProperty("币种代码")
    private String currencyCode;

    @ApiModelProperty("投资计划ID")
    private Long planId;

    @ApiModelProperty("投资计划编号")
    private String planNo;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("删除标记")
    private Integer deleteFlag;

    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @ApiModelProperty("创建人姓名")
    private String createdByName;

    @ApiModelProperty("创建时间")
    private java.sql.Timestamp createdTime;

    @ApiModelProperty("更新人ID")
    private Long updatedBy;

    @ApiModelProperty("更新人姓名")
    private String updatedByName;

    @ApiModelProperty("更新时间")
    private java.sql.Timestamp updatedTime;

    @ApiModelProperty("备注")
    private String remark;

    // Getter和Setter
    public Long getInvestmentId() { return investmentId; }
    public void setInvestmentId(Long investmentId) { this.investmentId = investmentId; }

    public String getInvestmentNo() { return investmentNo; }
    public void setInvestmentNo(String investmentNo) { this.investmentNo = investmentNo; }

    public String getBondCode() { return bondCode; }
    public void setBondCode(String bondCode) { this.bondCode = bondCode; }

    public String getBondName() { return bondName; }
    public void setBondName(String bondName) { this.bondName = bondName; }

    public String getBondType() { return bondType; }
    public void setBondType(String bondType) { this.bondType = bondType; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public BigDecimal getInvestmentAmount() { return investmentAmount; }
    public void setInvestmentAmount(BigDecimal investmentAmount) { this.investmentAmount = investmentAmount; }

    public BigDecimal getFaceValue() { return faceValue; }
    public void setFaceValue(BigDecimal faceValue) { this.faceValue = faceValue; }

    public BigDecimal getCouponRate() { return couponRate; }
    public void setCouponRate(BigDecimal couponRate) { this.couponRate = couponRate; }

    public BigDecimal getYieldToMaturity() { return yieldToMaturity; }
    public void setYieldToMaturity(BigDecimal yieldToMaturity) { this.yieldToMaturity = yieldToMaturity; }

    public String getCouponFrequency() { return couponFrequency; }
    public void setCouponFrequency(String couponFrequency) { this.couponFrequency = couponFrequency; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

    public Date getNextCouponDate() { return nextCouponDate; }
    public void setNextCouponDate(Date nextCouponDate) { this.nextCouponDate = nextCouponDate; }

    public BigDecimal getAccumulatedInterest() { return accumulatedInterest; }
    public void setAccumulatedInterest(BigDecimal accumulatedInterest) { this.accumulatedInterest = accumulatedInterest; }

    public BigDecimal getCurrentMarketValue() { return currentMarketValue; }
    public void setCurrentMarketValue(BigDecimal currentMarketValue) { this.currentMarketValue = currentMarketValue; }

    public BigDecimal getUnrealizedPnl() { return unrealizedPnl; }
    public void setUnrealizedPnl(BigDecimal unrealizedPnl) { this.unrealizedPnl = unrealizedPnl; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getInvestmentStatus() { return investmentStatus; }
    public void setInvestmentStatus(String investmentStatus) { this.investmentStatus = investmentStatus; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public java.sql.Timestamp getCreatedTime() { return createdTime; }
    public void setCreatedTime(java.sql.Timestamp createdTime) { this.createdTime = createdTime; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }

    public java.sql.Timestamp getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(java.sql.Timestamp updatedTime) { this.updatedTime = updatedTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
