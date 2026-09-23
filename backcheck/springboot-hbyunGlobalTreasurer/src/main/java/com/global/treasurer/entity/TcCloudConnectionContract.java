package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 云直连签约实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_CLOUD_CONNECTION_CONTRACT")
public class TcCloudConnectionContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /**
     * 银行代码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 账户ID
     */
    @TableField("ACCOUNT_ID")
    private String accountId;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 合同类型
     */
    @TableField("CONTRACT_TYPE")
    private String contractType;

    /**
     * 合同状态(APPLYING-申请中,REVIEWING-审核中,SIGNED-已签约,ACTIVE-已生效,TERMINATED-已终止)
     */
    @TableField("CONTRACT_STATUS")
    private String contractStatus;

    /**
     * 申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("APPLICATION_DATE")
    private Date applicationDate;

    /**
     * 资质验证状态
     */
    @TableField("QUALIFICATION_VERIFICATION_STATUS")
    private String qualificationVerificationStatus;

    /**
     * 资质验证时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("QUALIFICATION_VERIFICATION_TIME")
    private Date qualificationVerificationTime;

    /**
     * 协议签署状态
     */
    @TableField("AGREEMENT_SIGNING_STATUS")
    private String agreementSigningStatus;

    /**
     * 协议签署时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("AGREEMENT_SIGNING_TIME")
    private Date agreementSigningTime;

    /**
     * 服务激活状态
     */
    @TableField("SERVICE_ACTIVATION_STATUS")
    private String serviceActivationStatus;

    /**
     * 服务激活时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("SERVICE_ACTIVATION_TIME")
    private Date serviceActivationTime;

    /**
     * 合同生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("CONTRACT_EFFECTIVE_DATE")
    private Date contractEffectiveDate;

    /**
     * 合同到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("CONTRACT_EXPIRE_DATE")
    private Date contractExpireDate;

    /**
     * 续签提醒天数
     */
    @TableField("RENEWAL_REMINDER_DAYS")
    private Integer renewalReminderDays;

    /**
     * 下次续签日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("NEXT_RENEWAL_DATE")
    private Date nextRenewalDate;

    /**
     * 终止原因
     */
    @TableField("TERMINATION_REASON")
    private String terminationReason;

    /**
     * 终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("TERMINATION_DATE")
    private Date terminationDate;

    /**
     * 审批人
     */
    @TableField("APPROVAL_USER")
    private String approvalUser;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 状态(1-启用,0-停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public String getQualificationVerificationStatus() { return qualificationVerificationStatus; }
    public void setQualificationVerificationStatus(String qualificationVerificationStatus) { this.qualificationVerificationStatus = qualificationVerificationStatus; }
    public Date getQualificationVerificationTime() { return qualificationVerificationTime; }
    public void setQualificationVerificationTime(Date qualificationVerificationTime) { this.qualificationVerificationTime = qualificationVerificationTime; }
    public String getAgreementSigningStatus() { return agreementSigningStatus; }
    public void setAgreementSigningStatus(String agreementSigningStatus) { this.agreementSigningStatus = agreementSigningStatus; }
    public Date getAgreementSigningTime() { return agreementSigningTime; }
    public void setAgreementSigningTime(Date agreementSigningTime) { this.agreementSigningTime = agreementSigningTime; }
    public String getServiceActivationStatus() { return serviceActivationStatus; }
    public void setServiceActivationStatus(String serviceActivationStatus) { this.serviceActivationStatus = serviceActivationStatus; }
    public Date getServiceActivationTime() { return serviceActivationTime; }
    public void setServiceActivationTime(Date serviceActivationTime) { this.serviceActivationTime = serviceActivationTime; }
    public Date getContractEffectiveDate() { return contractEffectiveDate; }
    public void setContractEffectiveDate(Date contractEffectiveDate) { this.contractEffectiveDate = contractEffectiveDate; }
    public Date getContractExpireDate() { return contractExpireDate; }
    public void setContractExpireDate(Date contractExpireDate) { this.contractExpireDate = contractExpireDate; }
    public Integer getRenewalReminderDays() { return renewalReminderDays; }
    public void setRenewalReminderDays(Integer renewalReminderDays) { this.renewalReminderDays = renewalReminderDays; }
    public Date getNextRenewalDate() { return nextRenewalDate; }
    public void setNextRenewalDate(Date nextRenewalDate) { this.nextRenewalDate = nextRenewalDate; }
    public String getTerminationReason() { return terminationReason; }
    public void setTerminationReason(String terminationReason) { this.terminationReason = terminationReason; }
    public Date getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }
    public String getApprovalUser() { return approvalUser; }
    public void setApprovalUser(String approvalUser) { this.approvalUser = approvalUser; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }

}
