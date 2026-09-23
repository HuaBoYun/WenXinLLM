package com.global.treasurer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行贷款DTO
 * 用于接收前端传递的贷款申请数据
 *
 * @author HuaBo Cloud
 * @since 2025-01-14
 */
public class BankLoanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 贷款ID */
    private Long loanId;

    /** 申请编号 */
    private String applicationNo;

    /** 融资计划ID */
    private Long planId;

    /** 贷款类型 (CREDIT_LINE, TERM_LOAN, REVOLVING_LOAN, MORTGAGE, GUARANTEE) */
    private String loanType;

    /** 银行代码/ID */
    private String bankCode;

    /** 银行名称 */
    private String bankName;

    /** 贷款金额 */
    private BigDecimal loanAmount;

    /** 币种 (CNY, USD, EUR, JPY) */
    private String currencyCode;

    /** 贷款期限 */
    private Integer loanTerm;

    /** 期限单位 (DAY, MONTH, YEAR) */
    private String termUnit;

    /** 利率 */
    private BigDecimal interestRate;

    /** 利率类型 (FIXED, FLOATING, MIXED) */
    private String rateType;

    /** 还款方式 (EQUAL_INSTALLMENT, EQUAL_PRINCIPAL, INTEREST_ONLY, BALLOON) */
    private String repaymentMethod;

    /** 担保类型 (CREDIT, MORTGAGE, PLEDGE, GUARANTEE) */
    private String guaranteeType;

    /** 担保金额/价值 */
    private BigDecimal guaranteeValue;

    /** 贷款用途/名称 */
    private String loanPurpose;

    /** 申请状态 */
    private String applicationStatus;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // Getter and Setter methods

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Integer getLoanTerm() { return loanTerm; }
    public void setLoanTerm(Integer loanTerm) { this.loanTerm = loanTerm; }

    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }

    public String getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(String repaymentMethod) { this.repaymentMethod = repaymentMethod; }

    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }

    public BigDecimal getGuaranteeValue() { return guaranteeValue; }
    public void setGuaranteeValue(BigDecimal guaranteeValue) { this.guaranteeValue = guaranteeValue; }

    public String getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(String loanPurpose) { this.loanPurpose = loanPurpose; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
