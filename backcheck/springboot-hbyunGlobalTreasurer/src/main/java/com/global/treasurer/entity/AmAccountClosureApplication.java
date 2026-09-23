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
 * 账户销户申请实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_closure_application")
public class AmAccountClosureApplication implements Serializable {
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
     * 账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 销户原因
     */
    @TableField("closure_reason")
    private String closureReason;

    /**
     * 预期销户日期
     */
    @TableField("expected_closure_date")
    private LocalDate expectedClosureDate;

    /**
     * 最终余额
     */
    @TableField("final_balance")
    private BigDecimal finalBalance;

    /**
     * 余额转移账户
     */
    @TableField("balance_transfer_account")
    private String balanceTransferAccount;

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
     * 实际销户日期
     */
    @TableField("actual_closure_date")
    private LocalDate actualClosureDate;

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
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getClosureReason() { return closureReason; }
    public void setClosureReason(String closureReason) { this.closureReason = closureReason; }
    public LocalDate getExpectedClosureDate() { return expectedClosureDate; }
    public void setExpectedClosureDate(LocalDate expectedClosureDate) { this.expectedClosureDate = expectedClosureDate; }
    public BigDecimal getFinalBalance() { return finalBalance; }
    public void setFinalBalance(BigDecimal finalBalance) { this.finalBalance = finalBalance; }
    public String getBalanceTransferAccount() { return balanceTransferAccount; }
    public void setBalanceTransferAccount(String balanceTransferAccount) { this.balanceTransferAccount = balanceTransferAccount; }
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
    public LocalDate getActualClosureDate() { return actualClosureDate; }
    public void setActualClosureDate(LocalDate actualClosureDate) { this.actualClosureDate = actualClosureDate; }
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
