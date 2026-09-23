package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金调拨实体类
 * 对应表：TBL_FUND_TRANSFER
 *
 * @author AI Developer
 * @date 2025-01-15
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_TRANSFER")
public class FundTransfer {
    @TableId(type = IdType.INPUT)
    @TableField("TRANSFER_ID")
    private Long transferId;

    @TableField("TRANSFER_NO")
    private String transferNo;

    @TableField("FROM_ACCOUNT_ID")
    private String fromAccountId;

    @TableField("FROM_ACCOUNT_NUMBER")
    private String fromAccountNumber;

    @TableField("FROM_ACCOUNT_NAME")
    private String fromAccountName;

    @TableField("TO_ACCOUNT_ID")
    private String toAccountId;

    @TableField("TO_ACCOUNT_NUMBER")
    private String toAccountNumber;

    @TableField("TO_ACCOUNT_NAME")
    private String toAccountName;

    @TableField("TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    @TableField("CURRENCY_CODE")
    private String currencyCode;

    @TableField("EXPECTED_DATE")
    private Date expectedDate;

    @TableField("APPLY_DATE")
    private Date applyDate;

    @TableField("EXECUTE_DATE")
    private Date executeDate;

    @TableField("TRANSFER_STATUS")
    private String transferStatus;

    @TableField("TRANSFER_REASON")
    private String transferReason;

    @TableField("APPLICANT_NAME")
    private String applicantName;

    @TableField("APPLICANT_PHONE")
    private String applicantPhone;

    @TableField("APPROVER_ID")
    private Long approverId;

    @TableField("APPROVER_NAME")
    private String approverName;

    @TableField("APPROVAL_OPINION")
    private String approvalOpinion;

    @TableField("REMARK")
    private String remark;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField("CREATED_BY_NAME")
    private String createdByName;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private Long updatedBy;

    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // 完整的getter和setter方法

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getFromAccountName() {
        return fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApprovalOpinion() {
        return approvalOpinion;
    }

    public void setApprovalOpinion(String approvalOpinion) {
        this.approvalOpinion = approvalOpinion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
