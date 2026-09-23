package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行贷款实体类
 *
 * 数据库表结构 (TBL_BANK_LOAN):
 * LOAN_ID, LOAN_NO, LOAN_NAME, COMPANY_ID, COMPANY_NAME, BANK_ID, BANK_NAME,
 * LOAN_TYPE, LOAN_AMOUNT, OUTSTANDING_AMOUNT, CURRENCY_CODE, INTEREST_RATE,
 * RATE_TYPE, START_DATE, END_DATE, LOAN_STATUS, DELETE_FLAG,
 * CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME,
 * LOAN_PERIOD, PERIOD_UNIT, PAYMENT_METHOD, GUARANTEE_TYPE, GUARANTEE_AMOUNT, REMARK
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@TableName("TBL_BANK_LOAN")
public class TblBankLoan implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 贷款ID */
    @TableId(value = "LOAN_ID", type = IdType.ASSIGN_ID)
    private Long loanId;

    /** 申请编号 - 对应数据库 LOAN_NO */
    @TableField("LOAN_NO")
    private String applicationNo;

    /** 融资计划ID - 数据库中不存在此列，不参与insert */
    @TableField(exist = false)
    private Long planId;

    /** 贷款类型 */
    @TableField("LOAN_TYPE")
    private String loanType;

    /** 银行ID - 对应数据库 BANK_ID (NUMBER类型) */
    @TableField("BANK_ID")
    private Long bankId;

    /** 银行名称 */
    @TableField("BANK_NAME")
    private String bankName;

    /** 贷款金额 */
    @TableField("LOAN_AMOUNT")
    private BigDecimal loanAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 贷款期限 - 对应数据库 LOAN_PERIOD */
    @TableField("LOAN_PERIOD")
    private Integer loanTerm;

    /** 期限单位(MONTH-月,YEAR-年) */
    @TableField("PERIOD_UNIT")
    private String termUnit;

    /** 利率 */
    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    /** 利率类型(FIXED-固定,FLOATING-浮动) */
    @TableField("RATE_TYPE")
    private String rateType;

    /** 还款方式 - 对应数据库 PAYMENT_METHOD */
    @TableField("PAYMENT_METHOD")
    private String repaymentMethod;

    /** 担保类型 */
    @TableField("GUARANTEE_TYPE")
    private String guaranteeType;

    /** 担保金额 - 对应数据库 GUARANTEE_AMOUNT */
    @TableField("GUARANTEE_AMOUNT")
    private BigDecimal guaranteeValue;

    /** 借款用途/贷款名称 - 对应数据库 LOAN_NAME */
    @TableField("LOAN_NAME")
    private String loanPurpose;

    /** 申请状态 - 对应数据库 LOAN_STATUS */
    @TableField("LOAN_STATUS")
    private String applicationStatus;

    /** 申请日期/开始日期 - 对应数据库 START_DATE */
    @TableField("START_DATE")
    private Date applicationDate;

    /** 审批日期 - 数据库中不存在此列 */
    @TableField(exist = false)
    private Date approvalDate;

    /** 已提取金额 - 数据库中不存在此列 */
    @TableField(exist = false)
    private BigDecimal drawdownAmount;

    /** 未还本金 */
    @TableField("OUTSTANDING_AMOUNT")
    private BigDecimal outstandingAmount;

    /** 利息支出 - 数据库中不存在此列 */
    @TableField(exist = false)
    private BigDecimal interestExpense;

    /** 合同编号 - 数据库中不存在此列 */
    @TableField(exist = false)
    private String contractNo;

    /** 合同签署日期 - 数据库中不存在此列 */
    @TableField(exist = false)
    private Date contractDate;

    /** 放款日期 - 数据库中不存在此列 */
    @TableField(exist = false)
    private Date valueDate;

    /** 到期日期 - 对应数据库 END_DATE */
    @TableField("END_DATE")
    private Date maturityDate;

    /** 公司ID */
    @TableField("COMPANY_ID")
    private Long companyId;

    /** 公司名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 备注 */
    @TableField("REMARK")
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
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
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public BigDecimal getDrawdownAmount() { return drawdownAmount; }
    public void setDrawdownAmount(BigDecimal drawdownAmount) { this.drawdownAmount = drawdownAmount; }
    public BigDecimal getOutstandingAmount() { return outstandingAmount; }
    public void setOutstandingAmount(BigDecimal outstandingAmount) { this.outstandingAmount = outstandingAmount; }
    public BigDecimal getInterestExpense() { return interestExpense; }
    public void setInterestExpense(BigDecimal interestExpense) { this.interestExpense = interestExpense; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Date getContractDate() { return contractDate; }
    public void setContractDate(Date contractDate) { this.contractDate = contractDate; }
    public Date getValueDate() { return valueDate; }
    public void setValueDate(Date valueDate) { this.valueDate = valueDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
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
