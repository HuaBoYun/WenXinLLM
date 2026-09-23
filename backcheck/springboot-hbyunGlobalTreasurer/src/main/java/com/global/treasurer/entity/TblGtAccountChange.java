package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-账户变更实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_CHANGE")
public class TblGtAccountChange implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @TableId(value = "APPLICATION_ID", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 账户名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 变更类型
     */
    @TableField("CHANGE_TYPE")
    private String changeType;

    /**
     * 变更原因
     */
    @TableField("CHANGE_REASON")
    private String changeReason;

    /**
     * 旧值
     */
    @TableField("OLD_VALUE")
    private String oldValue;

    /**
     * 新值
     */
    @TableField("NEW_VALUE")
    private String newValue;

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
     * 申请人姓名
     */
    @TableField("APPLICANT_NAME")
    private String applicantName;

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
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public BigDecimal getAccountId() { return accountId; }
    public void setAccountId(BigDecimal accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public BigDecimal getApproverId() { return approverId; }
    public void setApproverId(BigDecimal approverId) { this.approverId = approverId; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public BigDecimal getOrgId() { return orgId; }
    public void setOrgId(BigDecimal orgId) { this.orgId = orgId; }
    public BigDecimal getCreateUser() { return createUser; }
    public void setCreateUser(BigDecimal createUser) { this.createUser = createUser; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}
