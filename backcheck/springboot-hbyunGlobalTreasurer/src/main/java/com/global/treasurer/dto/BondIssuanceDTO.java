package com.global.treasurer.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债券发行DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Data
public class BondIssuanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发行ID */
    private Long issuanceId;

    /** 融资计划ID */
    private Long planId;

    /** 债券名称 */
    @NotBlank(message = "债券名称不能为空")
    private String bondName;

    /** 债券代码 */
    private String bondCode;

    /** 债券类型 */
    @NotBlank(message = "债券类型不能为空")
    private String bondType;

    /** 发行金额 */
    @NotNull(message = "发行金额不能为空")
    private BigDecimal issuanceAmount;

    /** 币种 */
    private String currencyCode;

    /** 面值 */
    private BigDecimal faceValue;

    /** 票面利率 */
    @NotNull(message = "票面利率不能为空")
    private BigDecimal couponRate;

    /** 债券期限 */
    @NotNull(message = "债券期限不能为空")
    private Integer bondTerm;

    /** 期限单位 */
    private String termUnit;

    /** 付息频率 */
    private String paymentFrequency;

    /** 主承销商 */
    private String underwriter;

    /** 受托管理人 */
    private String trustee;

    /** 评级机构 */
    private String ratingAgency;

    /** 信用评级 */
    private String creditRating;

    /** 评级日期 */
    private Date ratingDate;

    /** 评级展望 */
    private String ratingOutlook;

    /** 评级说明 */
    private String ratingNotes;

    /** 上市交易所 */
    private String listingExchange;

    /** 上市日期 */
    private Date listingDate;

    /** 证券代码 */
    private String stockCode;

    /** 上市说明 */
    private String listingNotes;

    /** 兑付类型 */
    private String redeemType;

    /** 兑付金额 */
    private BigDecimal redeemAmount;

    /** 兑付日期 */
    private Date redeemDate;

    /** 兑付说明 */
    private String redeemNotes;

    /** 发行状态 */
    private String issuanceStatus;

    /** 发行日期 */
    private Date issuanceDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // 显式添加getter方法以确保编译通过
    public Long getIssuanceId() {
        return issuanceId;
    }

    public Long getPlanId() {
        return planId;
    }

    public String getBondName() {
        return bondName;
    }

    public String getBondCode() {
        return bondCode;
    }

    public String getBondType() {
        return bondType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public BigDecimal getCouponRate() {
        return couponRate;
    }

    public Integer getBondTerm() {
        return bondTerm;
    }

    public String getTermUnit() {
        return termUnit;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public String getUnderwriter() {
        return underwriter;
    }

    public String getTrustee() {
        return trustee;
    }

    public String getRatingAgency() {
        return ratingAgency;
    }

    public BigDecimal getIssuanceAmount() {
        return issuanceAmount;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public String getRatingOutlook() {
        return ratingOutlook;
    }

    public String getRatingNotes() {
        return ratingNotes;
    }

    public String getListingExchange() {
        return listingExchange;
    }

    public Date getListingDate() {
        return listingDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getListingNotes() {
        return listingNotes;
    }

    public String getRedeemType() {
        return redeemType;
    }

    public BigDecimal getRedeemAmount() {
        return redeemAmount;
    }

    public Date getRedeemDate() {
        return redeemDate;
    }

    public String getRedeemNotes() {
        return redeemNotes;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public String getIssuanceStatus() {
        return issuanceStatus;
    }

    public Long getCompanyId() {
        return companyId;
    }

    // 显式添加setter方法以确保编译通过
    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public void setBondType(String bondType) {
        this.bondType = bondType;
    }

    public void setIssuanceAmount(BigDecimal issuanceAmount) {
        this.issuanceAmount = issuanceAmount;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }

    public void setBondTerm(Integer bondTerm) {
        this.bondTerm = bondTerm;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }

    public void setTrustee(String trustee) {
        this.trustee = trustee;
    }

    public void setRatingAgency(String ratingAgency) {
        this.ratingAgency = ratingAgency;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public void setRatingOutlook(String ratingOutlook) {
        this.ratingOutlook = ratingOutlook;
    }

    public void setRatingNotes(String ratingNotes) {
        this.ratingNotes = ratingNotes;
    }

    public void setListingExchange(String listingExchange) {
        this.listingExchange = listingExchange;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setListingNotes(String listingNotes) {
        this.listingNotes = listingNotes;
    }

    public void setRedeemType(String redeemType) {
        this.redeemType = redeemType;
    }

    public void setRedeemAmount(BigDecimal redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public void setRedeemNotes(String redeemNotes) {
        this.redeemNotes = redeemNotes;
    }

    public void setIssuanceStatus(String issuanceStatus) {
        this.issuanceStatus = issuanceStatus;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setIssuanceId(Long issuanceId) {
        this.issuanceId = issuanceId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}

