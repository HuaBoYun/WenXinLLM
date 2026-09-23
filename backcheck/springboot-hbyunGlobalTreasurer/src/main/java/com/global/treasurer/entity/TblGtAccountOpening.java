package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除,使用手动编写的getter/setter
// import lombok.EqualsAndHashCode; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-开户申请实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode(callSuper = false) // 已移除,使用手动编写的getter/setter
@TableName("TBL_GT_ACCOUNT_OPENING")
public class TblGtAccountOpening implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @TableId(value = "APPLICATION_ID", type = IdType.ASSIGN_ID)
    private Long applicationId;

    /**
     * 申请编号
     */
    @TableField("APPLICATION_NO")
    private String applicationNo;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private BigDecimal accountId;

    /**
     * 账户名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 账户类型
     */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /**
     * 银行编码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 联系人
     */
    @TableField("CONTACT_PERSON")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    /**
     * 申请原因
     */
    @TableField("APPLICATION_REASON")
    private String applicationReason;

    /**
     * 申请状态
     */
    @TableField("APPLICATION_STATUS")
    private String applicationStatus;

    /**
     * 申请日期
     */
    @TableField("APPLICATION_DATE")
    private LocalDate applicationDate;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private BigDecimal approverId;

    /**
     * 审批日期
     */
    @TableField("APPROVAL_DATE")
    private LocalDate approvalDate;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_OPINION")
    private String approvalOpinion;

    /**
     * 附件
     */
    @TableField("ATTACHMENTS")
    private String attachments;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 机构ID
     */
    @TableField("ORG_ID")
    private BigDecimal orgId;

    /**
     * 创建人ID
     */
    @TableField("CREATE_USER")
    private BigDecimal createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATE_USER")
    private BigDecimal updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    // 完整的getter和setter方法

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public BigDecimal getAccountId() {
        return accountId;
    }

    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getApplicationReason() {
        return applicationReason;
    }

    public void setApplicationReason(String applicationReason) {
        this.applicationReason = applicationReason;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public BigDecimal getApproverId() {
        return approverId;
    }

    public void setApproverId(BigDecimal approverId) {
        this.approverId = approverId;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalOpinion() {
        return approvalOpinion;
    }

    public void setApprovalOpinion(String approvalOpinion) {
        this.approvalOpinion = approvalOpinion;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getOrgId() {
        return orgId;
    }

    public void setOrgId(BigDecimal orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getCreateUser() {
        return createUser;
    }

    public void setCreateUser(BigDecimal createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(BigDecimal updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
