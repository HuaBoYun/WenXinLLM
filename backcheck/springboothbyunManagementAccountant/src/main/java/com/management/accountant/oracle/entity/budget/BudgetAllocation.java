package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算分配实体
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_ALLOCATION")
public class BudgetAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ALLOCATION_ID", type = IdType.ASSIGN_UUID)
    private String allocationId;

    @TableField("ALLOCATION_CODE")
    private String allocationCode;

    @TableField("ALLOCATION_NAME")
    private String allocationName;

    @TableField("ALLOCATION_TYPE")
    private String allocationType;

    @TableField("ALLOCATION_METHOD")
    private String allocationMethod;

    @TableField("SOURCE_BUDGET_ID")
    private String sourceBudgetId;

    /** 总金额 */
    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /** 已分配金额 */
    @TableField("ALLOCATED_AMOUNT")
    private BigDecimal allocatedAmount;

    /** 剩余金额 */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /** 分配状态 DRAFT/CONFIRMED/CANCELLED */
    @TableField("ALLOCATION_STATUS")
    private String allocationStatus;

    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    @TableField("DIMENSION_TYPE")
    private String dimensionType;

    @TableField("ALLOCATION_RULES")
    private String allocationRules;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private Integer delFlag;

    @TableField("TENANT_ID")
    private String tenantId;

    // ---- 以下字段在实际表中不存在，忽略映射 ----
    @TableField(exist = false)
    private String targetBudgetId;

    @TableField(exist = false)
    private BigDecimal allocationAmount;

    @TableField(exist = false)
    private BigDecimal allocationRatio;

    @TableField(exist = false)
    private String allocationRule;

    @TableField(exist = false)
    private Date effectiveDate;

    @TableField(exist = false)
    private String allocationDescription;

    // ========== 显式 Getter/Setter ==========
    public String getAllocationId() { return allocationId; }
    public void setAllocationId(String allocationId) { this.allocationId = allocationId; }
    public String getAllocationCode() { return allocationCode; }
    public void setAllocationCode(String allocationCode) { this.allocationCode = allocationCode; }
    public String getAllocationName() { return allocationName; }
    public void setAllocationName(String allocationName) { this.allocationName = allocationName; }
    public String getAllocationType() { return allocationType; }
    public void setAllocationType(String allocationType) { this.allocationType = allocationType; }
    public String getAllocationMethod() { return allocationMethod; }
    public void setAllocationMethod(String allocationMethod) { this.allocationMethod = allocationMethod; }
    public String getSourceBudgetId() { return sourceBudgetId; }
    public void setSourceBudgetId(String sourceBudgetId) { this.sourceBudgetId = sourceBudgetId; }
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public java.math.BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(java.math.BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public java.math.BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(java.math.BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getAllocationStatus() { return allocationStatus; }
    public void setAllocationStatus(String allocationStatus) { this.allocationStatus = allocationStatus; }
    public Integer getBudgetYear() { return budgetYear; }
    public void setBudgetYear(Integer budgetYear) { this.budgetYear = budgetYear; }
    public String getAllocationRule() { return allocationRule; }
    public void setAllocationRule(String allocationRule) { this.allocationRule = allocationRule; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getAllocationDescription() { return allocationDescription; }
    public void setAllocationDescription(String allocationDescription) { this.allocationDescription = allocationDescription; }
}


