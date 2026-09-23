package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 预算科目实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_ACCOUNT")
public class BudgetAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科目ID
     */
    @TableId(value = "ACCOUNT_ID", type = IdType.ASSIGN_UUID)
    private String accountId;

    /**
     * 科目编码
     */
    @TableField("ACCOUNT_CODE")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 科目类型 (REVENUE/COST/EXPENSE/ASSET/LIABILITY)
     */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /**
     * 科目分类
     */
    @TableField("ACCOUNT_CATEGORY")
    private String accountCategory;

    /**
     * 上级科目ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 科目层级
     */
    @TableField("ACCOUNT_LEVEL")
    private Integer accountLevel;

    /**
     * 科目路径
     */
    @TableField("ACCOUNT_PATH")
    private String accountPath;

    /**
     * 是否末级 (0-否 1-是)
     */
    @TableField("IS_LEAF")
    private Integer isLeaf;

    /**
     * 借贷方向 (DEBIT/CREDIT)
     */
    @TableField("BALANCE_DIRECTION")
    private String balanceDirection;

    /**
     * 排序序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private java.math.BigDecimal budgetAmount;

    /**
     * 是否启用 (0-否 1-是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 科目描述
     */
    @TableField("ACCOUNT_DESCRIPTION")
    private String accountDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 科目映射列表 (非数据库字段，仅用于查询返回)
     */
    @TableField(exist = false)
    private List<BudgetAccountMapping> accountMappings;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountCategory() { return accountCategory; }
    public void setAccountCategory(String accountCategory) { this.accountCategory = accountCategory; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public Integer getAccountLevel() { return accountLevel; }
    public void setAccountLevel(Integer accountLevel) { this.accountLevel = accountLevel; }

    public String getAccountPath() { return accountPath; }
    public void setAccountPath(String accountPath) { this.accountPath = accountPath; }

    public Integer getIsLeaf() { return isLeaf; }
    public void setIsLeaf(Integer isLeaf) { this.isLeaf = isLeaf; }

    public String getBalanceDirection() { return balanceDirection; }
    public void setBalanceDirection(String balanceDirection) { this.balanceDirection = balanceDirection; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public java.math.BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(java.math.BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }

    public String getAccountDescription() { return accountDescription; }
    public void setAccountDescription(String accountDescription) { this.accountDescription = accountDescription; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }

    public List<BudgetAccountMapping> getAccountMappings() { return accountMappings; }
    public void setAccountMappings(List<BudgetAccountMapping> accountMappings) { this.accountMappings = accountMappings; }
}

