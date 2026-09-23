package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 融资还款管理DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-13
 */
public class FinancingRepaymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 还款计划ID */
    private Long repaymentId;

    /** 还款编号 */
    private String repaymentNo;

    /** 融资ID */
    private Long financingId;

    /** 融资编号 */
    private String financingNo;

    /** 提款ID */
    private Long drawdownId;

    /** 提款编号 */
    private String drawdownNo;

    /** 融资类型(BANK_LOAN-银行贷款 BOND-债券发行 LEASING-融资租赁) */
    private String financingType;

    /** 融资类型名称 */
    private String financingTypeName;

    /** 还款类型(PRINCIPAL-本金 INTEREST-利息 PRINCIPAL_INTEREST-本息 FEE-费用) */
    private String repaymentType;

    /** 金融机构 */
    private String financialInstitution;

    /** 还款期次 */
    private Integer repaymentPeriod;

    /** 还款方式(BANK_TRANSFER-银行转账 CASH-现金 CHECK-支票) */
    private String paymentMethod;

    /** 还款方式名称 */
    private String repaymentMethodName;

    /** 支付账户 */
    private String paymentAccount;

    /** 计划还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String planDate;

    /** 实际还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String actualRepaymentDate;

    /** 应还金额 */
    private BigDecimal repaymentAmount;

    /** 实还金额 */
    private BigDecimal actualAmount;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 应还本金 */
    private BigDecimal principalAmount;

    /** 应还利息 */
    private BigDecimal interestAmount;

    /** 罚息金额 */
    private BigDecimal penaltyAmount;

    /** 应还费用 */
    private BigDecimal feeAmount;

    /** 实还本金 */
    private BigDecimal actualPrincipalAmount;

    /** 实还利息 */
    private BigDecimal actualInterestAmount;

    /** 实还费用 */
    private BigDecimal actualFeeAmount;

    /** 实还总额 */
    private BigDecimal actualTotalAmount;

    /** 还款状态(PENDING-待还款 COMPLETED-已还款 OVERDUE-逾期 PARTIAL-部分还款) */
    private String repaymentStatus;

    /** 还款状态名称 */
    private String repaymentStatusName;

    /** 逾期天数 */
    private Integer overdueDays;

    /** 逾期利息 */
    private BigDecimal overdueInterest;

    /** 币种 */
    private String currencyCode;

    /** 汇率 */
    private BigDecimal exchangeRate;

    /** 本币金额 */
    private BigDecimal baseCurrencyAmount;

    /** 还款账户 */
    private String repaymentAccount;

    /** 收款账户 */
    private String collectionAccount;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 经办人 */
    private String operator;

    /** 审核人 */
    private String reviewer;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    /** 附件 */
    private String attachment;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 创建用户 */
    private Long createUser;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 更新用户 */
    private Long updateUser;

    /** 组织ID */
    private Long orgId;

    /** 展期次数 */
    private Integer extensionCount;

    /** 展期原因 */
    private String extensionReason;

    /** 剩余天数 */
    private Integer remainingDays;

    // ==================== Getter/Setter 方法 ====================

    public Long getRepaymentId() { return repaymentId; }
    public void setRepaymentId(Long repaymentId) { this.repaymentId = repaymentId; }

    public String getRepaymentNo() { return repaymentNo; }
    public void setRepaymentNo(String repaymentNo) { this.repaymentNo = repaymentNo; }

    public Long getFinancingId() { return financingId; }
    public void setFinancingId(Long financingId) { this.financingId = financingId; }

    public String getFinancingNo() { return financingNo; }
    public void setFinancingNo(String financingNo) { this.financingNo = financingNo; }

    public Long getDrawdownId() { return drawdownId; }
    public void setDrawdownId(Long drawdownId) { this.drawdownId = drawdownId; }

    public String getDrawdownNo() { return drawdownNo; }
    public void setDrawdownNo(String drawdownNo) { this.drawdownNo = drawdownNo; }

    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }

    public String getFinancingTypeName() { return financingTypeName; }
    public void setFinancingTypeName(String financingTypeName) { this.financingTypeName = financingTypeName; }

    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }

    public String getFinancialInstitution() { return financialInstitution; }
    public void setFinancialInstitution(String financialInstitution) { this.financialInstitution = financialInstitution; }

    public Integer getRepaymentPeriod() { return repaymentPeriod; }
    public void setRepaymentPeriod(Integer repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getRepaymentMethodName() { return repaymentMethodName; }
    public void setRepaymentMethodName(String repaymentMethodName) { this.repaymentMethodName = repaymentMethodName; }

    public String getPaymentAccount() { return paymentAccount; }
    public void setPaymentAccount(String paymentAccount) { this.paymentAccount = paymentAccount; }

    public String getPlanDate() { return planDate; }
    public void setPlanDate(String planDate) { this.planDate = planDate; }

    public String getActualRepaymentDate() { return actualRepaymentDate; }
    public void setActualRepaymentDate(String actualRepaymentDate) { this.actualRepaymentDate = actualRepaymentDate; }

    public BigDecimal getRepaymentAmount() { return repaymentAmount; }
    public void setRepaymentAmount(BigDecimal repaymentAmount) { this.repaymentAmount = repaymentAmount; }

    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }

    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public BigDecimal getFeeAmount() { return feeAmount; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }

    public BigDecimal getActualPrincipalAmount() { return actualPrincipalAmount; }
    public void setActualPrincipalAmount(BigDecimal actualPrincipalAmount) { this.actualPrincipalAmount = actualPrincipalAmount; }

    public BigDecimal getActualInterestAmount() { return actualInterestAmount; }
    public void setActualInterestAmount(BigDecimal actualInterestAmount) { this.actualInterestAmount = actualInterestAmount; }

    public BigDecimal getActualFeeAmount() { return actualFeeAmount; }
    public void setActualFeeAmount(BigDecimal actualFeeAmount) { this.actualFeeAmount = actualFeeAmount; }

    public BigDecimal getActualTotalAmount() { return actualTotalAmount; }
    public void setActualTotalAmount(BigDecimal actualTotalAmount) { this.actualTotalAmount = actualTotalAmount; }

    public String getRepaymentStatus() { return repaymentStatus; }
    public void setRepaymentStatus(String repaymentStatus) { this.repaymentStatus = repaymentStatus; }

    public String getRepaymentStatusName() { return repaymentStatusName; }
    public void setRepaymentStatusName(String repaymentStatusName) { this.repaymentStatusName = repaymentStatusName; }

    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }

    public BigDecimal getOverdueInterest() { return overdueInterest; }
    public void setOverdueInterest(BigDecimal overdueInterest) { this.overdueInterest = overdueInterest; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }

    public BigDecimal getBaseCurrencyAmount() { return baseCurrencyAmount; }
    public void setBaseCurrencyAmount(BigDecimal baseCurrencyAmount) { this.baseCurrencyAmount = baseCurrencyAmount; }

    public String getRepaymentAccount() { return repaymentAccount; }
    public void setRepaymentAccount(String repaymentAccount) { this.repaymentAccount = repaymentAccount; }

    public String getCollectionAccount() { return collectionAccount; }
    public void setCollectionAccount(String collectionAccount) { this.collectionAccount = collectionAccount; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }

    public LocalDateTime getReviewTime() { return reviewTime; }
    public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }

    public String getAttachment() { return attachment; }
    public void setAttachment(String attachment) { this.attachment = attachment; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }

    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

    public Integer getExtensionCount() { return extensionCount; }
    public void setExtensionCount(Integer extensionCount) { this.extensionCount = extensionCount; }

    public String getExtensionReason() { return extensionReason; }
    public void setExtensionReason(String extensionReason) { this.extensionReason = extensionReason; }

    public Integer getRemainingDays() { return remainingDays; }
    public void setRemainingDays(Integer remainingDays) { this.remainingDays = remainingDays; }
}
