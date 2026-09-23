package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算配额实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_QUOTA")
public class BudgetQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配额ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String quotaId;

    /**
     * 配额编码
     */
    @ExcelField(title = "配额编码", sort = 10, width = 4000)
    private String quotaCode;

    /**
     * 配额名称
     */
    @ExcelField(title = "配额名称", sort = 20, width = 6000)
    private String quotaName;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 配额类型 (FIXED/FLEXIBLE/DYNAMIC)
     */
    @ExcelField(title = "配额类型", sort = 30, width = 3000)
    private String quotaType;

    /**
     * 配额金额
     */
    @ExcelField(title = "配额金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal quotaAmount;

    /**
     * 已用金额
     */
    @ExcelField(title = "已用金额", sort = 50, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal usedAmount;

    /**
     * 可用金额
     */
    @ExcelField(title = "可用金额", sort = 60, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal availableAmount;

    /**
     * 配额周期 (DAILY/WEEKLY/MONTHLY/QUARTERLY/YEARLY)
     */
    @ExcelField(title = "配额周期", sort = 70, width = 3000)
    private String quotaPeriod;

    /**
     * 生效日期
     */
    @ExcelField(title = "生效日期", sort = 80, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @ExcelField(title = "失效日期", sort = 90, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     * 配额状态 (ACTIVE/INACTIVE/EXPIRED)
     */
    @ExcelField(title = "配额状态", sort = 100, width = 3000)
    private String quotaStatus;

    /**
     * 是否启用
     */
    @ExcelField(title = "是否启用", sort = 110, width = 2500)
    private Boolean isEnabled;

    /**
     * 配额描述
     */
    @ExcelField(title = "配额描述", sort = 120, width = 8000)
    private String quotaDescription;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已分配金额
     */
    @ExcelField(title = "已分配金额", sort = 130, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal allocatedAmount;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelField(title = "创建时间", sort = 140, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    private Integer delFlag;

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 分配方式 (EQUAL/PROPORTIONAL/CUSTOM)
     */
    private String allocationMethod;

    /**
     * 预警阈值（百分比）
     */
    private BigDecimal warningThreshold;

    /**
     * 控制级别 (SOFT/HARD/ADVISORY)
     */
    private String controlLevel;

    // ========== 虚拟字段（不映射数据库列，用于前端展示） ==========

    /**
     * 组织单元名称
     */
    @TableField(exist = false)
    private String organizationName;

    /**
     * 预算科目名称
     */
    @TableField(exist = false)
    private String budgetAccountName;

    /**
     * 剩余金额（计算字段）
     */
    @TableField(exist = false)
    private BigDecimal remainingAmount;

    /**
     * 使用率（计算字段，百分比）
     */
    @TableField(exist = false)
    private Double usageRate;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getQuotaId() { return quotaId; }
    public void setQuotaId(String quotaId) { this.quotaId = quotaId; }

    public String getQuotaCode() { return quotaCode; }
    public void setQuotaCode(String quotaCode) { this.quotaCode = quotaCode; }

    public String getQuotaName() { return quotaName; }
    public void setQuotaName(String quotaName) { this.quotaName = quotaName; }

    public String getBudgetId() { return budgetId; }
    public void setBudgetId(String budgetId) { this.budgetId = budgetId; }

    public String getQuotaType() { return quotaType; }
    public void setQuotaType(String quotaType) { this.quotaType = quotaType; }

    public BigDecimal getQuotaAmount() { return quotaAmount; }
    public void setQuotaAmount(BigDecimal quotaAmount) { this.quotaAmount = quotaAmount; }

    public BigDecimal getUsedAmount() { return usedAmount; }
    public void setUsedAmount(BigDecimal usedAmount) { this.usedAmount = usedAmount; }

    public BigDecimal getAvailableAmount() { return availableAmount; }
    public void setAvailableAmount(BigDecimal availableAmount) { this.availableAmount = availableAmount; }

    public String getQuotaPeriod() { return quotaPeriod; }
    public void setQuotaPeriod(String quotaPeriod) { this.quotaPeriod = quotaPeriod; }

    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public String getQuotaStatus() { return quotaStatus; }
    public void setQuotaStatus(String quotaStatus) { this.quotaStatus = quotaStatus; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public String getQuotaDescription() { return quotaDescription; }
    public void setQuotaDescription(String quotaDescription) { this.quotaDescription = quotaDescription; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }

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

    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getAllocationMethod() { return allocationMethod; }
    public void setAllocationMethod(String allocationMethod) { this.allocationMethod = allocationMethod; }

    public BigDecimal getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(BigDecimal warningThreshold) { this.warningThreshold = warningThreshold; }

    public String getControlLevel() { return controlLevel; }
    public void setControlLevel(String controlLevel) { this.controlLevel = controlLevel; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getBudgetAccountName() { return budgetAccountName; }
    public void setBudgetAccountName(String budgetAccountName) { this.budgetAccountName = budgetAccountName; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }

    public Double getUsageRate() { return usageRate; }
    public void setUsageRate(Double usageRate) { this.usageRate = usageRate; }
}

