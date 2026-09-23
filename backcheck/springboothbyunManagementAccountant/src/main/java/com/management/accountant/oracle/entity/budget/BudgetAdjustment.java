package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算调整实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_ADJUSTMENT")
public class BudgetAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调整ID (主键)
     */
    @TableId(value = "ADJUSTMENT_ID", type = IdType.ASSIGN_UUID)
    private String adjustmentId;

    /**
     * 调整编码
     */
    @TableField("ADJUSTMENT_CODE")
    @ExcelField(title = "调整编码", sort = 10, width = 4000)
    private String adjustmentCode;

    /**
     * 调整类型
     * INCREASE: 增加
     * DECREASE: 减少
     * TRANSFER: 转移
     */
    @TableField("ADJUSTMENT_TYPE")
    @ExcelField(title = "调整类型", sort = 20, width = 3000)
    private String adjustmentType;

    /**
     * 原预算ID
     */
    @TableField("ORIGINAL_BUDGET_ID")
    private String originalBudgetId;

    /**
     * 调整金额
     */
    @TableField("ADJUSTMENT_AMOUNT")
    @ExcelField(title = "调整金额", sort = 30, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal adjustmentAmount;

    /**
     * 调整前金额（原始金额）
     */
    @TableField("ORIGINAL_AMOUNT")
    @ExcelField(title = "调整前金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal originalAmount;

    /**
     * 调整后金额
     */
    @TableField("ADJUSTED_AMOUNT")
    @ExcelField(title = "调整后金额", sort = 50, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal adjustedAmount;

    /**
     * 调整原因
     */
    @TableField("ADJUSTMENT_REASON")
    @ExcelField(title = "调整原因", sort = 60, width = 6000)
    private String adjustmentReason;

    /**
     * 调整状态 DRAFT/PENDING/APPROVED/REJECTED/CANCELLED
     */
    @TableField("ADJUSTMENT_STATUS")
    @ExcelField(title = "调整状态", sort = 70, width = 3000)
    private String adjustmentStatus;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 申请人ID
     */
    @TableField("APPLICANT_ID")
    private String applicantId;

    /**
     * 申请人姓名
     */
    @TableField("APPLICANT_NAME")
    @ExcelField(title = "申请人", sort = 80, width = 3000)
    private String applicantName;

    /**
     * 申请日期
     */
    @TableField("APPLY_DATE")
    @ExcelField(title = "申请时间", sort = 90, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyDate;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    @ExcelField(title = "审批人", sort = 100, width = 3000)
    private String approverName;

    /**
     * 审批日期
     */
    @TableField("APPROVE_DATE")
    @ExcelField(title = "审批时间", sort = 110, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date approveDate;

    /**
     * 审批意见
     */
    @TableField("APPROVE_COMMENT")
    private String approveComment;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ExcelField(title = "备注", sort = 120, width = 6000)
    private String remark;

    // ---- 以下字段在实际表中不存在，忽略映射 ----
    @TableField(exist = false)
    private BigDecimal beforeAmount;

    @TableField(exist = false)
    private BigDecimal afterAmount;

    @TableField(exist = false)
    private Date applicationTime;

    @TableField(exist = false)
    private Date approvalTime;

    @TableField(exist = false)
    private Date executionTime;

    @TableField(exist = false)
    private String attachments;

    @TableField(exist = false)
    private Date submitTime;

    @TableField(exist = false)
    private Date approveTime;

    @TableField(exist = false)
    private Date executeTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 140, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getAdjustmentId() { return adjustmentId; }
    public void setAdjustmentId(String adjustmentId) { this.adjustmentId = adjustmentId; }

    public String getAdjustmentCode() { return adjustmentCode; }
    public void setAdjustmentCode(String adjustmentCode) { this.adjustmentCode = adjustmentCode; }

    public String getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }

    public String getOriginalBudgetId() { return originalBudgetId; }
    public void setOriginalBudgetId(String originalBudgetId) { this.originalBudgetId = originalBudgetId; }

    public BigDecimal getAdjustmentAmount() { return adjustmentAmount; }
    public void setAdjustmentAmount(BigDecimal adjustmentAmount) { this.adjustmentAmount = adjustmentAmount; }

    public BigDecimal getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }

    public BigDecimal getAdjustedAmount() { return adjustedAmount; }
    public void setAdjustedAmount(BigDecimal adjustedAmount) { this.adjustedAmount = adjustedAmount; }

    public String getAdjustmentReason() { return adjustmentReason; }
    public void setAdjustmentReason(String adjustmentReason) { this.adjustmentReason = adjustmentReason; }

    public String getAdjustmentStatus() { return adjustmentStatus; }
    public void setAdjustmentStatus(String adjustmentStatus) { this.adjustmentStatus = adjustmentStatus; }

    public Integer getBudgetYear() { return budgetYear; }
    public void setBudgetYear(Integer budgetYear) { this.budgetYear = budgetYear; }

    public String getBudgetPeriod() { return budgetPeriod; }
    public void setBudgetPeriod(String budgetPeriod) { this.budgetPeriod = budgetPeriod; }

    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getApplicantId() { return applicantId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public Date getApplyDate() { return applyDate; }
    public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }

    public String getApproverId() { return approverId; }
    public void setApproverId(String approverId) { this.approverId = approverId; }

    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }

    public Date getApproveDate() { return approveDate; }
    public void setApproveDate(Date approveDate) { this.approveDate = approveDate; }

    public String getApproveComment() { return approveComment; }
    public void setApproveComment(String approveComment) { this.approveComment = approveComment; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public BigDecimal getBeforeAmount() { return beforeAmount; }
    public void setBeforeAmount(BigDecimal beforeAmount) { this.beforeAmount = beforeAmount; }

    public BigDecimal getAfterAmount() { return afterAmount; }
    public void setAfterAmount(BigDecimal afterAmount) { this.afterAmount = afterAmount; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}

