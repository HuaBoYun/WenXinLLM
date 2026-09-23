package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部借款申请实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_INTERNAL_LOAN_APPLICATION")
public class InternalLoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 申请编号 */
    private String applicationNo;

    /** 借款类型 */
    private String loanType;

    /** 借款金额 */
    private BigDecimal loanAmount;

    /** 币种 */
    private String currencyCode;

    /** 借款期限(天) */
    private Integer loanTerm;

    /** 利率 */
    private BigDecimal interestRate;

    /** 利率类型(FIXED-固定,FLOATING-浮动) */
    private String interestRateType;

    /** 借款用途 */
    private String loanPurpose;

    /** 借款单位ID */
    private Long borrowerCompanyId;

    /** 借款单位名称 */
    private String borrowerCompanyName;

    /** 出借单位ID */
    private Long lenderCompanyId;

    /** 出借单位名称 */
    private String lenderCompanyName;

    /** 申请日期 */
    private Date applicationDate;

    /** 期望放款日期 */
    private Date expectedDisbursementDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 还款方式 */
    private String repaymentMethod;

    /** 申请状态(DRAFT-草稿,PENDING-待审批,APPROVED-已批准,REJECTED-已拒绝,DISBURSED-已放款,REPAID-已还款) */
    private String status;

    /** 审批人ID */
    private Long approverId;

    /** 审批人姓名 */
    private String approverName;

    /** 审批时间 */
    private Date approvalTime;

    /** 审批意见 */
    private String approvalOpinion;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getLoanTerm() { return loanTerm; }
    public void setLoanTerm(Integer loanTerm) { this.loanTerm = loanTerm; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getInterestRateType() { return interestRateType; }
    public void setInterestRateType(String interestRateType) { this.interestRateType = interestRateType; }
    public String getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(String loanPurpose) { this.loanPurpose = loanPurpose; }
    public Long getBorrowerCompanyId() { return borrowerCompanyId; }
    public void setBorrowerCompanyId(Long borrowerCompanyId) { this.borrowerCompanyId = borrowerCompanyId; }
    public String getBorrowerCompanyName() { return borrowerCompanyName; }
    public void setBorrowerCompanyName(String borrowerCompanyName) { this.borrowerCompanyName = borrowerCompanyName; }
    public Long getLenderCompanyId() { return lenderCompanyId; }
    public void setLenderCompanyId(Long lenderCompanyId) { this.lenderCompanyId = lenderCompanyId; }
    public String getLenderCompanyName() { return lenderCompanyName; }
    public void setLenderCompanyName(String lenderCompanyName) { this.lenderCompanyName = lenderCompanyName; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getExpectedDisbursementDate() { return expectedDisbursementDate; }
    public void setExpectedDisbursementDate(Date expectedDisbursementDate) { this.expectedDisbursementDate = expectedDisbursementDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
    public String getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(String repaymentMethod) { this.repaymentMethod = repaymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
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
