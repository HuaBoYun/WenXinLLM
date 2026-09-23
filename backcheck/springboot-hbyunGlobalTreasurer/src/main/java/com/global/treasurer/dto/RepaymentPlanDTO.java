package com.global.treasurer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 还款计划DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public class RepaymentPlanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long planId;
    private Long loanId;
    private Long contractId;
    private Integer periodNo;
    private String dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidPrincipal;
    private BigDecimal paidInterest;
    private BigDecimal paidTotal;
    private BigDecimal remainingPrincipal;
    private String paymentDate;
    private String paymentStatus;
    private Integer overdueDays;
    private BigDecimal overdueInterest;
    private String currencyCode;
    private Long companyId;
    private String companyName;
    private String remark;

    // 还款操作参数
    private BigDecimal payAmount;

    // 自动生成还款计划参数
    private String repaymentMethod;  // 还款方式: EQUAL_PRINCIPAL-等额本金, EQUAL_INSTALLMENT-等额本息
    private Integer periods;          // 期数
    private BigDecimal loanAmount;    // 贷款金额
    private BigDecimal interestRate;  // 年利率

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // Getters and Setters
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Integer getPeriodNo() { return periodNo; }
    public void setPeriodNo(Integer periodNo) { this.periodNo = periodNo; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getPaidPrincipal() { return paidPrincipal; }
    public void setPaidPrincipal(BigDecimal paidPrincipal) { this.paidPrincipal = paidPrincipal; }
    public BigDecimal getPaidInterest() { return paidInterest; }
    public void setPaidInterest(BigDecimal paidInterest) { this.paidInterest = paidInterest; }
    public BigDecimal getPaidTotal() { return paidTotal; }
    public void setPaidTotal(BigDecimal paidTotal) { this.paidTotal = paidTotal; }
    public BigDecimal getRemainingPrincipal() { return remainingPrincipal; }
    public void setRemainingPrincipal(BigDecimal remainingPrincipal) { this.remainingPrincipal = remainingPrincipal; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
    public BigDecimal getOverdueInterest() { return overdueInterest; }
    public void setOverdueInterest(BigDecimal overdueInterest) { this.overdueInterest = overdueInterest; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public BigDecimal getPayAmount() { return payAmount; }
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    public String getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(String repaymentMethod) { this.repaymentMethod = repaymentMethod; }
    public Integer getPeriods() { return periods; }
    public void setPeriods(Integer periods) { this.periods = periods; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}

