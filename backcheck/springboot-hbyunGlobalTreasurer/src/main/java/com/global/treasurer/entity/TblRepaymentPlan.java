package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划实体类
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@TableName("TBL_REPAYMENT_PLAN")
public class TblRepaymentPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PLAN_ID", type = IdType.ASSIGN_ID)
    private Long planId;

    @TableField("LOAN_ID")
    private Long loanId;

    @TableField("CONTRACT_ID")
    private Long contractId;

    @TableField("PERIOD_NO")
    private Integer periodNo;

    @TableField("DUE_DATE")
    private Date dueDate;

    @TableField("PRINCIPAL_AMOUNT")
    private BigDecimal principalAmount;

    @TableField("INTEREST_AMOUNT")
    private BigDecimal interestAmount;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("PAID_PRINCIPAL")
    private BigDecimal paidPrincipal;

    @TableField("PAID_INTEREST")
    private BigDecimal paidInterest;

    @TableField("PAID_TOTAL")
    private BigDecimal paidTotal;

    @TableField("REMAINING_PRINCIPAL")
    private BigDecimal remainingPrincipal;

    @TableField("PAYMENT_DATE")
    private Date paymentDate;

    @TableField("PAYMENT_STATUS")
    private String paymentStatus;

    @TableField("OVERDUE_DAYS")
    private Integer overdueDays;

    @TableField("OVERDUE_INTEREST")
    private BigDecimal overdueInterest;

    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    @TableField("REMARK")
    private String remark;

    // Getters and Setters
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Integer getPeriodNo() { return periodNo; }
    public void setPeriodNo(Integer periodNo) { this.periodNo = periodNo; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
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
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
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
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

