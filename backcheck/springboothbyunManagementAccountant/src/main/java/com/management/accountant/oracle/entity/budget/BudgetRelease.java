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
 * 预算释放实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_RELEASE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetRelease implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 释放ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String releaseId;

    /**
     * 释放编码
     */
    @ExcelField(title = "释放编码", sort = 10, width = 4000)
    private String releaseCode;

    /**
     * 释放标题（对应数据库 RELEASE_TITLE）
     */
    @ExcelField(title = "释放标题", sort = 15, width = 6000)
    private String releaseTitle;

    /**
     * 释放名称（对应数据库 RELEASE_NAME）
     */
    @ExcelField(title = "释放名称", sort = 20, width = 6000)
    private String releaseName;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 组织单元ID（对应数据库 ORGANIZATION_ID）
     */
    private String organizationId;

    /**
     * 预算科目ID（对应数据库 BUDGET_ACCOUNT_ID）
     */
    private String budgetAccountId;

    /**
     * 计划释放日期（对应数据库 PLANNED_RELEASE_DATE）
     */
    private Date plannedReleaseDate;

    /**
     * 审批人（@TableField exist=false，数据库用 APPROVE_BY）
     */
    @TableField(exist = false)
    private String approver;

    /**
     * 释放类型 (FULL/PARTIAL/CONDITIONAL)
     */
    @ExcelField(title = "释放类型", sort = 30, width = 3000)
    private String releaseType;

    /**
     * 释放金额
     */
    @ExcelField(title = "释放金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal releaseAmount;

    /**
     * 释放原因
     */
    @ExcelField(title = "释放原因", sort = 50, width = 6000)
    private String releaseReason;

    /**
     * 释放状态 (PENDING/APPROVED/RELEASED/REJECTED)
     */
    @ExcelField(title = "释放状态", sort = 60, width = 3000)
    private String releaseStatus;

    /**
     * 申请日期
     */
    @ExcelField(title = "申请日期", sort = 70, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date applyDate;

    /**
     * 释放日期
     */
    @ExcelField(title = "释放日期", sort = 80, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date releaseDate;

    /**
     * 申请人
     */
    @ExcelField(title = "申请人", sort = 90, width = 3000)
    private String applyBy;

    /**
     * 审批人
     */
    @ExcelField(title = "审批人", sort = 100, width = 3000)
    private String approveBy;

    /**
     * 审批日期
     */
    @ExcelField(title = "审批日期", sort = 110, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date approveDate;

    /**
     * 释放描述
     */
    @ExcelField(title = "释放描述", sort = 120, width = 8000)
    private String releaseDescription;

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

    // ========== 展示字段（非数据库列，查询后填充） ==========

    /**
     * 组织单元名称（展示用）
     */
    @TableField(exist = false)
    private String organizationName;

    /**
     * 预算科目名称（展示用）
     */
    @TableField(exist = false)
    private String budgetAccountName;

    /**
     * 审批状态（展示用，与 releaseStatus 区分）
     */
    @TableField(exist = false)
    private String approvalStatus;

    /**
     * 申请人姓名（展示用）
     */
    @TableField(exist = false)
    private String applicant;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getReleaseCode() {
        return releaseCode;
    }

    public void setReleaseCode(String releaseCode) {
        this.releaseCode = releaseCode;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public BigDecimal getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(BigDecimal releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public String getReleaseReason() {
        return releaseReason;
    }

    public void setReleaseReason(String releaseReason) {
        this.releaseReason = releaseReason;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(String applyBy) {
        this.applyBy = applyBy;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public void setReleaseDescription(String releaseDescription) {
        this.releaseDescription = releaseDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApproveRemark() {
        return approveRemark;
    }

    public void setApproveRemark(String approveRemark) {
        this.approveRemark = approveRemark;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getReleaseTitle() {
        return releaseTitle;
    }

    public void setReleaseTitle(String releaseTitle) {
        this.releaseTitle = releaseTitle;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getBudgetAccountId() {
        return budgetAccountId;
    }

    public void setBudgetAccountId(String budgetAccountId) {
        this.budgetAccountId = budgetAccountId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBudgetAccountName() {
        return budgetAccountName;
    }

    public void setBudgetAccountName(String budgetAccountName) {
        this.budgetAccountName = budgetAccountName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }
}

