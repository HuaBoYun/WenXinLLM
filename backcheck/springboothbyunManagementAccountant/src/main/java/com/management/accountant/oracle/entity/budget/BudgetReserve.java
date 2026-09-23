package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算保留实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_RESERVE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetReserve implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保留ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String reserveId;

    /**
     * 保留编码
     */
    @ExcelField(title = "保留编码", sort = 10, width = 4000)
    private String reserveCode;

    /**
     * 保留名称
     */
    @ExcelField(title = "保留名称", sort = 20, width = 6000)
    private String reserveName;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 保留类型 (CONTINGENCY/STRATEGIC/OPERATIONAL)
     */
    @ExcelField(title = "保留类型", sort = 30, width = 3000)
    private String reserveType;

    /**
     * 保留金额
     */
    @ExcelField(title = "保留金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal reserveAmount;

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
     * 保留原因
     */
    @ExcelField(title = "保留原因", sort = 70, width = 6000)
    private String reserveReason;

    /**
     * 保留状态 (RESERVED/RELEASED/EXPIRED)
     */
    @ExcelField(title = "保留状态", sort = 80, width = 3000)
    private String reserveStatus;

    /**
     * 保留日期
     */
    @ExcelField(title = "保留日期", sort = 90, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date reserveDate;

    /**
     * 到期日期
     */
    @ExcelField(title = "到期日期", sort = 100, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /**
     * 保留人
     */
    @ExcelField(title = "保留人", sort = 110, width = 3000)
    private String reserveBy;

    /**
     * 保留描述
     */
    @ExcelField(title = "保留描述", sort = 120, width = 8000)
    private String reserveDescription;

    /**
     * 组织单元ID
     */
    private String organizationId;

    /**
     * 组织单元名称（非持久化，查询时填充）
     */
    @TableField(exist = false)
    private String organizationName;

    /**
     * 预算科目ID
     */
    private String budgetAccountId;

    /**
     * 预算科目名称（非持久化，查询时填充）
     */
    @TableField(exist = false)
    private String budgetAccountName;

    /**
     * 创建日期（用户选择）
     */
    private Date createDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批备注
     */
    private String approveRemark;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 执行时间
     */
    private Date executeTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelField(title = "创建时间", sort = 130, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
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
     * 使用方式 (APPROVAL-审批使用, AUTO-自动使用, MANUAL-手动使用)
     */
    private String usageMethod;

    /**
     * 单次使用限额
     */
    private BigDecimal singleLimit;

    /**
     * 优先级 (HIGH-高, MEDIUM-中, LOW-低)
     */
    private String priority;

    /**
     * 是否启用储备 (0-否, 1-是)
     */
    private Integer isActive;

    /**
     * 是否允许转移 (0-否, 1-是)
     */
    private Integer allowTransfer;

    /**
     * 是否自动释放 (0-否, 1-是)
     */
    private Integer autoRelease;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getReserveId() { return reserveId; }
    public void setReserveId(String reserveId) { this.reserveId = reserveId; }

    public String getReserveCode() { return reserveCode; }
    public void setReserveCode(String reserveCode) { this.reserveCode = reserveCode; }

    public String getReserveName() { return reserveName; }
    public void setReserveName(String reserveName) { this.reserveName = reserveName; }

    public String getBudgetId() { return budgetId; }
    public void setBudgetId(String budgetId) { this.budgetId = budgetId; }

    public String getReserveType() { return reserveType; }
    public void setReserveType(String reserveType) { this.reserveType = reserveType; }

    public BigDecimal getReserveAmount() { return reserveAmount; }
    public void setReserveAmount(BigDecimal reserveAmount) { this.reserveAmount = reserveAmount; }

    public BigDecimal getUsedAmount() { return usedAmount; }
    public void setUsedAmount(BigDecimal usedAmount) { this.usedAmount = usedAmount; }

    public BigDecimal getAvailableAmount() { return availableAmount; }
    public void setAvailableAmount(BigDecimal availableAmount) { this.availableAmount = availableAmount; }

    public String getReserveReason() { return reserveReason; }
    public void setReserveReason(String reserveReason) { this.reserveReason = reserveReason; }

    public String getReserveStatus() { return reserveStatus; }
    public void setReserveStatus(String reserveStatus) { this.reserveStatus = reserveStatus; }

    public Date getReserveDate() { return reserveDate; }
    public void setReserveDate(Date reserveDate) { this.reserveDate = reserveDate; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public String getReserveBy() { return reserveBy; }
    public void setReserveBy(String reserveBy) { this.reserveBy = reserveBy; }

    public String getReserveDescription() { return reserveDescription; }
    public void setReserveDescription(String reserveDescription) { this.reserveDescription = reserveDescription; }

    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getBudgetAccountId() { return budgetAccountId; }
    public void setBudgetAccountId(String budgetAccountId) { this.budgetAccountId = budgetAccountId; }

    public String getBudgetAccountName() { return budgetAccountName; }
    public void setBudgetAccountName(String budgetAccountName) { this.budgetAccountName = budgetAccountName; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getApproveRemark() { return approveRemark; }
    public void setApproveRemark(String approveRemark) { this.approveRemark = approveRemark; }

    public Date getApproveTime() { return approveTime; }
    public void setApproveTime(Date approveTime) { this.approveTime = approveTime; }

    public Date getExecuteTime() { return executeTime; }
    public void setExecuteTime(Date executeTime) { this.executeTime = executeTime; }

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

    public String getUsageMethod() { return usageMethod; }
    public void setUsageMethod(String usageMethod) { this.usageMethod = usageMethod; }

    public BigDecimal getSingleLimit() { return singleLimit; }
    public void setSingleLimit(BigDecimal singleLimit) { this.singleLimit = singleLimit; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Integer getAllowTransfer() { return allowTransfer; }
    public void setAllowTransfer(Integer allowTransfer) { this.allowTransfer = allowTransfer; }

    public Integer getAutoRelease() { return autoRelease; }
    public void setAutoRelease(Integer autoRelease) { this.autoRelease = autoRelease; }
}

