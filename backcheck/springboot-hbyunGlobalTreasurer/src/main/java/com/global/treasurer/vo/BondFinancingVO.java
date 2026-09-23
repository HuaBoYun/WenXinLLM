package com.global.treasurer.vo;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债券融资VO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BondFinancingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发行ID */
    private Long issuanceId;

    /** 发行编号 */
    private String issuanceNo;

    /** 融资计划ID */
    private Long planId;

    /** 债券名称 */
    private String bondName;

    /** 债券代码 */
    private String bondCode;

    /** 债券类型 */
    private String bondType;

    /** 债券类型名称 */
    private String bondTypeName;

    /** 发行金额 */
    private BigDecimal issuanceAmount;

    /** 币种 */
    private String currencyCode;

    /** 币种名称 */
    private String currencyName;

    /** 面值 */
    private BigDecimal faceValue;

    /** 票面利率 */
    private BigDecimal couponRate;

    /** 债券期限 */
    private Integer bondTerm;

    /** 期限单位 */
    private String termUnit;

    /** 期限单位名称 */
    private String termUnitName;

    /** 付息频率 */
    private String paymentFrequency;

    /** 付息频率名称 */
    private String paymentFrequencyName;

    /** 主承销商 */
    private String underwriter;

    /** 受托管理人 */
    private String trustee;

    /** 评级机构 */
    private String ratingAgency;

    /** 信用评级 */
    private String creditRating;

    /** 信用评级名称 */
    private String creditRatingName;

    /** 上市交易所 */
    private String listingExchange;

    /** 发行状态 */
    private String issuanceStatus;

    /** 发行状态名称 */
    private String issuanceStatusName;

    /** 发行日期 */
    private Date issuanceDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 付息日 */
    private Date interestPaymentDate;

    /** 兑付日 */
    private Date redemptionDate;

    /** 实际发行金额 */
    private BigDecimal actualIssuanceAmount;

    /** 发行成本 */
    private BigDecimal issueCost;

    /** 承销费用 */
    private BigDecimal underwritingFee;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 创建人 */
    private Long createdBy;

    /** 创建人名称 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人名称 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getIssuanceId() { return issuanceId; }
    public void setIssuanceId(Long issuanceId) { this.issuanceId = issuanceId; }
    public String getIssuanceNo() { return issuanceNo; }
    public void setIssuanceNo(String issuanceNo) { this.issuanceNo = issuanceNo; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getBondName() { return bondName; }
    public void setBondName(String bondName) { this.bondName = bondName; }
    public String getBondCode() { return bondCode; }
    public void setBondCode(String bondCode) { this.bondCode = bondCode; }
    public String getBondType() { return bondType; }
    public void setBondType(String bondType) { this.bondType = bondType; }
    public String getBondTypeName() { return bondTypeName; }
    public void setBondTypeName(String bondTypeName) { this.bondTypeName = bondTypeName; }
    public BigDecimal getIssuanceAmount() { return issuanceAmount; }
    public void setIssuanceAmount(BigDecimal issuanceAmount) { this.issuanceAmount = issuanceAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }
    public BigDecimal getFaceValue() { return faceValue; }
    public void setFaceValue(BigDecimal faceValue) { this.faceValue = faceValue; }
    public BigDecimal getCouponRate() { return couponRate; }
    public void setCouponRate(BigDecimal couponRate) { this.couponRate = couponRate; }
    public Integer getBondTerm() { return bondTerm; }
    public void setBondTerm(Integer bondTerm) { this.bondTerm = bondTerm; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public String getTermUnitName() { return termUnitName; }
    public void setTermUnitName(String termUnitName) { this.termUnitName = termUnitName; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getPaymentFrequencyName() { return paymentFrequencyName; }
    public void setPaymentFrequencyName(String paymentFrequencyName) { this.paymentFrequencyName = paymentFrequencyName; }
    public String getUnderwriter() { return underwriter; }
    public void setUnderwriter(String underwriter) { this.underwriter = underwriter; }
    public String getTrustee() { return trustee; }
    public void setTrustee(String trustee) { this.trustee = trustee; }
    public String getRatingAgency() { return ratingAgency; }
    public void setRatingAgency(String ratingAgency) { this.ratingAgency = ratingAgency; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getCreditRatingName() { return creditRatingName; }
    public void setCreditRatingName(String creditRatingName) { this.creditRatingName = creditRatingName; }
    public String getListingExchange() { return listingExchange; }
    public void setListingExchange(String listingExchange) { this.listingExchange = listingExchange; }
    public String getIssuanceStatus() { return issuanceStatus; }
    public void setIssuanceStatus(String issuanceStatus) { this.issuanceStatus = issuanceStatus; }
    public String getIssuanceStatusName() { return issuanceStatusName; }
    public void setIssuanceStatusName(String issuanceStatusName) { this.issuanceStatusName = issuanceStatusName; }
    public Date getIssuanceDate() { return issuanceDate; }
    public void setIssuanceDate(Date issuanceDate) { this.issuanceDate = issuanceDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public Date getInterestPaymentDate() { return interestPaymentDate; }
    public void setInterestPaymentDate(Date interestPaymentDate) { this.interestPaymentDate = interestPaymentDate; }
    public Date getRedemptionDate() { return redemptionDate; }
    public void setRedemptionDate(Date redemptionDate) { this.redemptionDate = redemptionDate; }
    public BigDecimal getActualIssuanceAmount() { return actualIssuanceAmount; }
    public void setActualIssuanceAmount(BigDecimal actualIssuanceAmount) { this.actualIssuanceAmount = actualIssuanceAmount; }
    public BigDecimal getIssueCost() { return issueCost; }
    public void setIssueCost(BigDecimal issueCost) { this.issueCost = issueCost; }
    public BigDecimal getUnderwritingFee() { return underwritingFee; }
    public void setUnderwritingFee(BigDecimal underwritingFee) { this.underwritingFee = underwritingFee; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
