package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部借贷实体类
 * @author Claude
 * @date 2026-01-20
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_INTERNAL_LOAN")
@ApiModel(value = "TblInternalLoan", description = "内部借贷实体")
public class TblInternalLoan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOAN_ID", type = IdType.INPUT)
    @ApiModelProperty("借贷ID")
    private String loanId;

    @TableField("LOAN_CODE")
    @ApiModelProperty("借贷编码")
    private String loanCode;

    @TableField("LOAN_NAME")
    @ApiModelProperty("借贷名称")
    private String loanName;

    @TableField("BORROW_ACCOUNT_ID")
    @ApiModelProperty("借款账户ID")
    private String borrowAccountId;

    @TableField("BORROW_ACCOUNT_NAME")
    @ApiModelProperty("借款账户名称")
    private String borrowAccountName;

    @TableField("LEND_ACCOUNT_ID")
    @ApiModelProperty("出借账户ID")
    private String lendAccountId;

    @TableField("LEND_ACCOUNT_NAME")
    @ApiModelProperty("出借账户名称")
    private String lendAccountName;

    @TableField("LOAN_AMOUNT")
    @ApiModelProperty("借贷金额")
    private BigDecimal loanAmount;

    @TableField("LOAN_TERM")
    @ApiModelProperty("借贷期限(天)")
    private Integer loanTerm;

    @TableField("INTEREST_RATE")
    @ApiModelProperty("利率")
    private BigDecimal interestRate;

    @TableField("REPAYMENT_METHOD")
    @ApiModelProperty("还款方式")
    private String repaymentMethod;

    @TableField("LOAN_STATUS")
    @ApiModelProperty("借贷状态")
    private String loanStatus;

    @TableField("APPROVAL_STATUS")
    @ApiModelProperty("审批状态")
    private String approvalStatus;

    @TableField("APPROVER_ID")
    @ApiModelProperty("审批人ID")
    private String approverId;

    @TableField("APPROVER_NAME")
    @ApiModelProperty("审批人姓名")
    private String approverName;

    @TableField("APPROVAL_TIME")
    @ApiModelProperty("审批时间")
    private Date approvalTime;

    @TableField("LOAN_START_DATE")
    @ApiModelProperty("借款开始日期")
    private Date loanStartDate;

    @TableField("LOAN_END_DATE")
    @ApiModelProperty("借款结束日期")
    private Date loanEndDate;

    @TableField("ACTUAL_REPAY_DATE")
    @ApiModelProperty("实际还款日期")
    private Date actualRepayDate;

    @TableField("INTEREST_AMOUNT")
    @ApiModelProperty("利息金额")
    private BigDecimal interestAmount;

    @TableField("PAID_PRINCIPAL")
    @ApiModelProperty("已还本金")
    private BigDecimal paidPrincipal;

    @TableField("PAID_INTEREST")
    @ApiModelProperty("已还利息")
    private BigDecimal paidInterest;

    @TableField("CREATOR_ID")
    @ApiModelProperty("创建人ID")
    private String creatorId;

    @TableField("CREATOR_NAME")
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @TableField("DESCRIPTION")
    @ApiModelProperty("描述")
    private String description;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public String getLoanCode() { return loanCode; }
    public void setLoanCode(String loanCode) { this.loanCode = loanCode; }
    public String getLoanName() { return loanName; }
    public void setLoanName(String loanName) { this.loanName = loanName; }
    public String getBorrowAccountId() { return borrowAccountId; }
    public void setBorrowAccountId(String borrowAccountId) { this.borrowAccountId = borrowAccountId; }
    public String getBorrowAccountName() { return borrowAccountName; }
    public void setBorrowAccountName(String borrowAccountName) { this.borrowAccountName = borrowAccountName; }
    public String getLendAccountId() { return lendAccountId; }
    public void setLendAccountId(String lendAccountId) { this.lendAccountId = lendAccountId; }
    public String getLendAccountName() { return lendAccountName; }
    public void setLendAccountName(String lendAccountName) { this.lendAccountName = lendAccountName; }
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    public Integer getLoanTerm() { return loanTerm; }
    public void setLoanTerm(Integer loanTerm) { this.loanTerm = loanTerm; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getRepaymentMethod() { return repaymentMethod; }
    public void setRepaymentMethod(String repaymentMethod) { this.repaymentMethod = repaymentMethod; }
    public String getLoanStatus() { return loanStatus; }
    public void setLoanStatus(String loanStatus) { this.loanStatus = loanStatus; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApproverId() { return approverId; }
    public void setApproverId(String approverId) { this.approverId = approverId; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public Date getLoanStartDate() { return loanStartDate; }
    public void setLoanStartDate(Date loanStartDate) { this.loanStartDate = loanStartDate; }
    public Date getLoanEndDate() { return loanEndDate; }
    public void setLoanEndDate(Date loanEndDate) { this.loanEndDate = loanEndDate; }
    public Date getActualRepayDate() { return actualRepayDate; }
    public void setActualRepayDate(Date actualRepayDate) { this.actualRepayDate = actualRepayDate; }
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    public BigDecimal getPaidPrincipal() { return paidPrincipal; }
    public void setPaidPrincipal(BigDecimal paidPrincipal) { this.paidPrincipal = paidPrincipal; }
    public BigDecimal getPaidInterest() { return paidInterest; }
    public void setPaidInterest(BigDecimal paidInterest) { this.paidInterest = paidInterest; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
