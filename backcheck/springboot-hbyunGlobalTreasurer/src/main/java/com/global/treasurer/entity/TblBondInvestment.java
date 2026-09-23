package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 债券投资实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Data
@TableName("TBL_BOND_INVESTMENT")
@ApiModel(value = "TblBondInvestment", description = "债券投资")
public class TblBondInvestment implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("债券投资ID")
    @TableId(value = "INVESTMENT_ID", type = IdType.ASSIGN_ID)
    private Long investmentId;

    @ApiModelProperty("投资编号")
    @TableField("INVESTMENT_NO")
    private String investmentNo;

    @ApiModelProperty("债券代码")
    @TableField("BOND_CODE")
    private String bondCode;

    @ApiModelProperty("债券名称")
    @TableField("BOND_NAME")
    private String bondName;

    @ApiModelProperty("债券类型")
    @TableField("BOND_TYPE")
    private String bondType;

    @ApiModelProperty("发行机构")
    @TableField("ISSUER")
    private String issuer;

    @ApiModelProperty("信用评级")
    @TableField("CREDIT_RATING")
    private String creditRating;

    @ApiModelProperty("购买价格")
    @TableField("PURCHASE_PRICE")
    private BigDecimal purchasePrice;

    @ApiModelProperty("购买数量")
    @TableField("PURCHASE_QUANTITY")
    private Integer purchaseQuantity;

    @ApiModelProperty("投资金额")
    @TableField("INVESTMENT_AMOUNT")
    private BigDecimal investmentAmount;

    @ApiModelProperty("面值")
    @TableField("FACE_VALUE")
    private BigDecimal faceValue;

    @ApiModelProperty("票面利率")
    @TableField("COUPON_RATE")
    private BigDecimal couponRate;

    @ApiModelProperty("到期收益率")
    @TableField("YIELD_TO_MATURITY")
    private BigDecimal yieldToMaturity;

    @ApiModelProperty("付息频率")
    @TableField("COUPON_FREQUENCY")
    private String couponFrequency;

    @ApiModelProperty("购买日期")
    @TableField("PURCHASE_DATE")
    private Date purchaseDate;

    @ApiModelProperty("到期日期")
    @TableField("MATURITY_DATE")
    private Date maturityDate;

    @ApiModelProperty("下次付息日期")
    @TableField("NEXT_COUPON_DATE")
    private Date nextCouponDate;

    @ApiModelProperty("累计利息")
    @TableField("ACCUMULATED_INTEREST")
    private BigDecimal accumulatedInterest;

    @ApiModelProperty("当前市值")
    @TableField("CURRENT_MARKET_VALUE")
    private BigDecimal currentMarketValue;

    @ApiModelProperty("未实现盈亏")
    @TableField("UNREALIZED_PNL")
    private BigDecimal unrealizedPnl;

    @ApiModelProperty("风险等级")
    @TableField("RISK_LEVEL")
    private String riskLevel;

    @ApiModelProperty("投资状态")
    @TableField("INVESTMENT_STATUS")
    private String investmentStatus;

    @ApiModelProperty("币种代码")
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @ApiModelProperty("投资计划ID")
    @TableField("PLAN_ID")
    private Long planId;

    @ApiModelProperty("投资计划编号")
    @TableField("PLAN_NO")
    private String planNo;

    @ApiModelProperty("公司ID")
    @TableField("COMPANY_ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @ApiModelProperty("删除标记")
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @ApiModelProperty("创建人ID")
    @TableField("CREATED_BY")
    private Long createdBy;

    @ApiModelProperty("创建人姓名")
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    @ApiModelProperty("创建时间")
    @TableField("CREATED_TIME")
    private Timestamp createdTime;

    @ApiModelProperty("更新人ID")
    @TableField("UPDATED_BY")
    private Long updatedBy;

    @ApiModelProperty("更新人姓名")
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    @ApiModelProperty("更新时间")
    @TableField("UPDATED_TIME")
    private Timestamp updatedTime;

    @ApiModelProperty("备注")
    @TableField("REMARK")
    private String remark;

    // Getter和Setter方法

    public Long getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(Long investmentId) {
        this.investmentId = investmentId;
    }

    public String getInvestmentNo() {
        return investmentNo;
    }

    public void setInvestmentNo(String investmentNo) {
        this.investmentNo = investmentNo;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public String getBondType() {
        return bondType;
    }

    public void setBondType(String bondType) {
        this.bondType = bondType;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public BigDecimal getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }

    public BigDecimal getYieldToMaturity() {
        return yieldToMaturity;
    }

    public void setYieldToMaturity(BigDecimal yieldToMaturity) {
        this.yieldToMaturity = yieldToMaturity;
    }

    public String getCouponFrequency() {
        return couponFrequency;
    }

    public void setCouponFrequency(String couponFrequency) {
        this.couponFrequency = couponFrequency;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getNextCouponDate() {
        return nextCouponDate;
    }

    public void setNextCouponDate(Date nextCouponDate) {
        this.nextCouponDate = nextCouponDate;
    }

    public BigDecimal getAccumulatedInterest() {
        return accumulatedInterest;
    }

    public void setAccumulatedInterest(BigDecimal accumulatedInterest) {
        this.accumulatedInterest = accumulatedInterest;
    }

    public BigDecimal getCurrentMarketValue() {
        return currentMarketValue;
    }

    public void setCurrentMarketValue(BigDecimal currentMarketValue) {
        this.currentMarketValue = currentMarketValue;
    }

    public BigDecimal getUnrealizedPnl() {
        return unrealizedPnl;
    }

    public void setUnrealizedPnl(BigDecimal unrealizedPnl) {
        this.unrealizedPnl = unrealizedPnl;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getInvestmentStatus() {
        return investmentStatus;
    }

    public void setInvestmentStatus(String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // 兼容性getter方法 - getCurrentPrice作为getCurrentMarketValue的别名
    public BigDecimal getCurrentPrice() {
        return this.currentMarketValue;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentMarketValue = currentPrice;
    }
}
