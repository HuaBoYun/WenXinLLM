package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-销户申请实体类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_CLOSING")
public class TblGtAccountClosing implements Serializable {
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
     * 账户号码（对应表字段 ACCOUNT_NUMBER）
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNo;

    /**
     * 销户原因
     */
    @TableField("CLOSING_REASON")
    private String closingReason;

    /**
     * 余额处理方式
     */
    @TableField("BALANCE_HANDLING")
    private String balanceHandling;

    /**
     * 转账目标账户号码
     */
    @TableField("TRANSFER_ACCOUNT_NUMBER")
    private String transferAccountNumber;

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
     * 完成日期
     */
    @TableField("COMPLETE_DATE")
    private LocalDate completeDate;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }

    public BigDecimal getAccountId() { return accountId; }
    public void setAccountId(BigDecimal accountId) { this.accountId = accountId; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }

    public String getClosingReason() { return closingReason; }
    public void setClosingReason(String closingReason) { this.closingReason = closingReason; }

    public String getBalanceHandling() { return balanceHandling; }
    public void setBalanceHandling(String balanceHandling) { this.balanceHandling = balanceHandling; }

    public String getTransferAccountNumber() { return transferAccountNumber; }
    public void setTransferAccountNumber(String transferAccountNumber) { this.transferAccountNumber = transferAccountNumber; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public BigDecimal getApproverId() { return approverId; }
    public void setApproverId(BigDecimal approverId) { this.approverId = approverId; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }

    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }

    public LocalDate getCompleteDate() { return completeDate; }
    public void setCompleteDate(LocalDate completeDate) { this.completeDate = completeDate; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public BigDecimal getOrgId() { return orgId; }
    public void setOrgId(BigDecimal orgId) { this.orgId = orgId; }

    public BigDecimal getCreateUser() { return createUser; }
    public void setCreateUser(BigDecimal createUser) { this.createUser = createUser; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public BigDecimal getUpdateUser() { return updateUser; }
    public void setUpdateUser(BigDecimal updateUser) { this.updateUser = updateUser; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
