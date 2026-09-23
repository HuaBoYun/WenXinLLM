package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-账户检查实体类
 * 对应数据库表: TBL_GT_ACCOUNT_CHECK
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_CHECK")
public class TblGtAccountCheck implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 检查ID
     */
    @TableId(value = "CHECK_ID", type = IdType.ASSIGN_ID)
    private Long checkId;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private Long accountId;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 检查类型: BALANCE-余额检查, TRANSACTION-交易检查, STATUS-状态检查, COMPLIANCE-合规检查
     */
    @TableField("CHECK_TYPE")
    private String checkType;

    /**
     * 检查状态: PENDING-待检查, CHECKING-检查中, COMPLETED-已完成, FAILED-失败
     */
    @TableField("CHECK_STATUS")
    private String checkStatus;

    /**
     * 检查日期
     */
    @TableField("CHECK_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkDate;

    /**
     * 检查结果: PASS-通过, WARNING-警告, ERROR-错误
     */
    @TableField("CHECK_RESULT")
    private String checkResult;

    /**
     * 检查内容详情
     */
    @TableField("CHECK_CONTENT")
    private String checkContent;

    /**
     * 发现的问题
     */
    @TableField("PROBLEM_FOUND")
    private String problemFound;

    /**
     * 处理建议
     */
    @TableField("SUGGESTION")
    private String suggestion;

    /**
     * 检查人ID
     */
    @TableField("CHECK_USER")
    private Long checkUser;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private BigDecimal approverId;

    /**
     * 审批日期
     */
    @TableField("APPROVAL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 更新人ID
     */
    @TableField("UPDATE_USER")
    private BigDecimal updateUser;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // ========== 以下为扩展字段，用于前端显示，不映射到数据库 ==========

    /**
     * 账户名称（扩展字段，用于显示）
     */
    @TableField(exist = false)
    private String accountName;

    /**
     * 检查人姓名（扩展字段，用于显示）
     */
    @TableField(exist = false)
    private String checkerName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getCheckId() { return checkId; }
    public void setCheckId(Long checkId) { this.checkId = checkId; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }
    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }
    public LocalDate getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDate checkDate) { this.checkDate = checkDate; }
    public String getCheckResult() { return checkResult; }
    public void setCheckResult(String checkResult) { this.checkResult = checkResult; }
    public String getCheckContent() { return checkContent; }
    public void setCheckContent(String checkContent) { this.checkContent = checkContent; }
    public String getProblemFound() { return problemFound; }
    public void setProblemFound(String problemFound) { this.problemFound = problemFound; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public Long getCheckUser() { return checkUser; }
    public void setCheckUser(Long checkUser) { this.checkUser = checkUser; }
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
    public BigDecimal getUpdateUser() { return updateUser; }
    public void setUpdateUser(BigDecimal updateUser) { this.updateUser = updateUser; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getCheckerName() { return checkerName; }
    public void setCheckerName(String checkerName) { this.checkerName = checkerName; }

}
