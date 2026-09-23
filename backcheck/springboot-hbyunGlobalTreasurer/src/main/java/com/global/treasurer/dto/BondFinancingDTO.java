package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 债券融资DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-14
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BondFinancingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bondId;
    private String bondCode;
    private String bondName;
    private Integer bondType;
    private String bondTypeName;
    private BigDecimal issuePrice;
    private BigDecimal faceValue;
    private BigDecimal couponRate;
    private BigDecimal issueAmount;
    private BigDecimal actualAmount;
    private LocalDate issueStartDate;
    private LocalDate issueEndDate;
    private LocalDate maturityDate;
    private Integer term;
    private Integer paymentFrequency;
    private String listingExchange;
    private String underwriter;
    private String ratingAgency;
    private String creditRating;
    private Integer issueStatus;
    private String issueStatusName;
    private BigDecimal remainingAmount;
    private BigDecimal totalInterestPaid;
    private String currency;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Integer page;
    private Integer limit;
    private String startDate;
    private String endDate;

    // 新增字段 - 用于Service层
    private Long planId;
    private Long issuanceId;
    private String currencyCode;
    private Integer bondTerm;
    private String termUnit;
    private String trustee;
    private LocalDate issuanceDate;
    private LocalDate interestPaymentDate;
    private LocalDate redemptionDate;
    private Long companyId;
    private String companyName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getBondId() { return bondId; }
    public void setBondId(Long bondId) { this.bondId = bondId; }
    public String getBondCode() { return bondCode; }
    public void setBondCode(String bondCode) { this.bondCode = bondCode; }
    public String getBondName() { return bondName; }
    public void setBondName(String bondName) { this.bondName = bondName; }
    public Integer getBondType() { return bondType; }
    public void setBondType(Integer bondType) { this.bondType = bondType; }
    public String getBondTypeName() { return bondTypeName; }
    public void setBondTypeName(String bondTypeName) { this.bondTypeName = bondTypeName; }
    public BigDecimal getIssuePrice() { return issuePrice; }
    public void setIssuePrice(BigDecimal issuePrice) { this.issuePrice = issuePrice; }
    public BigDecimal getFaceValue() { return faceValue; }
    public void setFaceValue(BigDecimal faceValue) { this.faceValue = faceValue; }
    public BigDecimal getCouponRate() { return couponRate; }
    public void setCouponRate(BigDecimal couponRate) { this.couponRate = couponRate; }
    public BigDecimal getIssueAmount() { return issueAmount; }
    public void setIssueAmount(BigDecimal issueAmount) { this.issueAmount = issueAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public LocalDate getIssueStartDate() { return issueStartDate; }
    public void setIssueStartDate(LocalDate issueStartDate) { this.issueStartDate = issueStartDate; }
    public LocalDate getIssueEndDate() { return issueEndDate; }
    public void setIssueEndDate(LocalDate issueEndDate) { this.issueEndDate = issueEndDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }
    public Integer getTerm() { return term; }
    public void setTerm(Integer term) { this.term = term; }
    public Integer getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(Integer paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getListingExchange() { return listingExchange; }
    public void setListingExchange(String listingExchange) { this.listingExchange = listingExchange; }
    public String getUnderwriter() { return underwriter; }
    public void setUnderwriter(String underwriter) { this.underwriter = underwriter; }
    public String getRatingAgency() { return ratingAgency; }
    public void setRatingAgency(String ratingAgency) { this.ratingAgency = ratingAgency; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public Integer getIssueStatus() { return issueStatus; }
    public void setIssueStatus(Integer issueStatus) { this.issueStatus = issueStatus; }
    public String getIssueStatusName() { return issueStatusName; }
    public void setIssueStatusName(String issueStatusName) { this.issueStatusName = issueStatusName; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public BigDecimal getTotalInterestPaid() { return totalInterestPaid; }
    public void setTotalInterestPaid(BigDecimal totalInterestPaid) { this.totalInterestPaid = totalInterestPaid; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getIssuanceId() { return issuanceId; }
    public void setIssuanceId(Long issuanceId) { this.issuanceId = issuanceId; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getBondTerm() { return bondTerm; }
    public void setBondTerm(Integer bondTerm) { this.bondTerm = bondTerm; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public String getTrustee() { return trustee; }
    public void setTrustee(String trustee) { this.trustee = trustee; }
    public LocalDate getIssuanceDate() { return issuanceDate; }
    public void setIssuanceDate(LocalDate issuanceDate) { this.issuanceDate = issuanceDate; }
    public LocalDate getInterestPaymentDate() { return interestPaymentDate; }
    public void setInterestPaymentDate(LocalDate interestPaymentDate) { this.interestPaymentDate = interestPaymentDate; }
    public LocalDate getRedemptionDate() { return redemptionDate; }
    public void setRedemptionDate(LocalDate redemptionDate) { this.redemptionDate = redemptionDate; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
