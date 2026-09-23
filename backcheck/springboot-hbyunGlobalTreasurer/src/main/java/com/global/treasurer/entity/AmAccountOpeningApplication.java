package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账户开户申请实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_opening_application")
public class AmAccountOpeningApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 申请单号
     */
    @TableField("application_no")
    private String applicationNo;

    /**
     * 账户名称
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 账户英文名称
     */
    @TableField("account_english_name")
    private String accountEnglishName;

    /**
     * 银行机构ID
     */
    @TableField("bank_institution_id")
    private Long bankInstitutionId;

    /**
     * 账户类型ID
     */
    @TableField("account_type_id")
    private Long accountTypeId;

    /**
     * 币种代码
     */
    @TableField("currency_code")
    private String currencyCode;

    /**
     * 账户性质：BASIC-基本户，GENERAL-一般户，SPECIAL-专用户，TEMPORARY-临时户
     */
    @TableField("account_nature")
    private String accountNature;

    /**
     * 账户用途：OPERATING-经营，INVESTMENT-投资，FINANCING-融资，SETTLEMENT-结算
     */
    @TableField("account_purpose")
    private String accountPurpose;

    /**
     * 所属组织ID
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 责任人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    /**
     * 使用部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 成本中心
     */
    @TableField("cost_center")
    private String costCenter;

    /**
     * 申请原因
     */
    @TableField("application_reason")
    private String applicationReason;

    /**
     * 预期开户日期
     */
    @TableField("expected_opening_date")
    private LocalDate expectedOpeningDate;

    /**
     * 印鉴组合ID
     */
    @TableField("seal_combination_id")
    private Long sealCombinationId;

    /**
     * 申请状态：DRAFT-草稿，SUBMITTED-已提交，APPROVED-已审批，REJECTED-已拒绝，PROCESSING-办理中，COMPLETED-已完成
     */
    @TableField("application_status")
    private String applicationStatus;

    /**
     * 审批状态：PENDING-待审批，APPROVED-已通过，REJECTED-已拒绝
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 办理时间
     */
    @TableField("processing_time")
    private LocalDateTime processingTime;

    /**
     * 完成时间
     */
    @TableField("completion_time")
    private LocalDateTime completionTime;

    /**
     * 实际开户账号
     */
    @TableField("actual_account_no")
    private String actualAccountNo;

    /**
     * 实际开户日期
     */
    @TableField("actual_opening_date")
    private LocalDate actualOpeningDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：1-有效，0-无效
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountEnglishName() { return accountEnglishName; }
    public void setAccountEnglishName(String accountEnglishName) { this.accountEnglishName = accountEnglishName; }
    public Long getBankInstitutionId() { return bankInstitutionId; }
    public void setBankInstitutionId(Long bankInstitutionId) { this.bankInstitutionId = bankInstitutionId; }
    public Long getAccountTypeId() { return accountTypeId; }
    public void setAccountTypeId(Long accountTypeId) { this.accountTypeId = accountTypeId; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getAccountNature() { return accountNature; }
    public void setAccountNature(String accountNature) { this.accountNature = accountNature; }
    public String getAccountPurpose() { return accountPurpose; }
    public void setAccountPurpose(String accountPurpose) { this.accountPurpose = accountPurpose; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getResponsiblePersonId() { return responsiblePersonId; }
    public void setResponsiblePersonId(Long responsiblePersonId) { this.responsiblePersonId = responsiblePersonId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
    public String getApplicationReason() { return applicationReason; }
    public void setApplicationReason(String applicationReason) { this.applicationReason = applicationReason; }
    public LocalDate getExpectedOpeningDate() { return expectedOpeningDate; }
    public void setExpectedOpeningDate(LocalDate expectedOpeningDate) { this.expectedOpeningDate = expectedOpeningDate; }
    public Long getSealCombinationId() { return sealCombinationId; }
    public void setSealCombinationId(Long sealCombinationId) { this.sealCombinationId = sealCombinationId; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public LocalDateTime getSubmitTime() { return submitTime; }
    public void setSubmitTime(LocalDateTime submitTime) { this.submitTime = submitTime; }
    public LocalDateTime getApprovalTime() { return approvalTime; }
    public void setApprovalTime(LocalDateTime approvalTime) { this.approvalTime = approvalTime; }
    public LocalDateTime getProcessingTime() { return processingTime; }
    public void setProcessingTime(LocalDateTime processingTime) { this.processingTime = processingTime; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    public String getActualAccountNo() { return actualAccountNo; }
    public void setActualAccountNo(String actualAccountNo) { this.actualAccountNo = actualAccountNo; }
    public LocalDate getActualOpeningDate() { return actualOpeningDate; }
    public void setActualOpeningDate(LocalDate actualOpeningDate) { this.actualOpeningDate = actualOpeningDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
