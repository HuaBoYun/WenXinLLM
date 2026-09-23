package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交易类型实体类
 * 对应数据库表：TBL_TRANSACTION_TYPE
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@TableName("TBL_TRANSACTION_TYPE")
public class TblTransactionType implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "TRANSACTION_TYPE_ID", type = IdType.INPUT)
    private Long transactionTypeId;

    @TableField("TRANSACTION_TYPE_CODE")
    private String transactionTypeCode;

    @TableField("TRANSACTION_TYPE_NAME")
    private String transactionTypeName;

    @TableField("TRANSACTION_CATEGORY")
    private String transactionCategory;

    @TableField("TRANSACTION_DIRECTION")
    private String transactionDirection;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("ACCOUNTING_SUBJECT")
    private String accountingSubject;

    @TableField("REQUIRES_APPROVAL")
    private String requiresApproval;

    @TableField("LIMIT_AMOUNT")
    private BigDecimal limitAmount;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("ORG_ID")
    private Long orgId;

    /** 子节点列表（非数据库字段） */
    @TableField(exist = false)
    private List<TblTransactionType> children;

    // 以下方法手动添加以解决编译问题

    public Long getTransactionTypeId() { return transactionTypeId; }
    public void setTransactionTypeId(Long transactionTypeId) { this.transactionTypeId = transactionTypeId; }
    public String getTransactionTypeCode() { return transactionTypeCode; }
    public void setTransactionTypeCode(String transactionTypeCode) { this.transactionTypeCode = transactionTypeCode; }
    public String getTransactionTypeName() { return transactionTypeName; }
    public void setTransactionTypeName(String transactionTypeName) { this.transactionTypeName = transactionTypeName; }
    public String getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(String transactionCategory) { this.transactionCategory = transactionCategory; }
    public String getTransactionDirection() { return transactionDirection; }
    public void setTransactionDirection(String transactionDirection) { this.transactionDirection = transactionDirection; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getAccountingSubject() { return accountingSubject; }
    public void setAccountingSubject(String accountingSubject) { this.accountingSubject = accountingSubject; }
    public String getRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(String requiresApproval) { this.requiresApproval = requiresApproval; }
    public BigDecimal getLimitAmount() { return limitAmount; }
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public List<TblTransactionType> getChildren() { return children; }
    public void setChildren(List<TblTransactionType> children) { this.children = children; }
}
