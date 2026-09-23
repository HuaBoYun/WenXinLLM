package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 预算差异分析实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_VARIANCE_ANALYSIS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetVarianceAnalysis {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分析编码
     */
    private String analysisCode;

    /**
     * 分析名称
     */
    private String analysisName;

    /**
     * 预算ID
     */
    private String budgetId;

    /**
     * 预算年度
     */
    private Integer budgetYear;

    /**
     * 预算期间
     */
    private String budgetPeriod;

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 预算科目ID
     */
    private String accountId;

    /**
     * 预算科目名称
     */
    private String accountName;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 差异金额
     */
    private BigDecimal varianceAmount;

    /**
     * 差异率(%)
     */
    private BigDecimal varianceRate;

    /**
     * 差异类型(favorable-有利差异, unfavorable-不利差异)
     */
    private String varianceType;

    /**
     * 差异原因
     */
    private String varianceReason;

    /**
     * 差异分析说明
     */
    private String analysisDescription;

    /**
     * 改进措施
     */
    private String improvementMeasures;

    /**
     * 分析结果
     */
    private String analysisResult;

    /**
     * 分析维度(department-部门, project-项目, cost_center-成本中心)
     */
    private String analysisDimension;

    /**
     * 分析状态(draft-草稿, analyzing-分析中, completed-已完成)
     */
    private String analysisStatus;

    /**
     * 分析人
     */
    private String analyzedBy;

    /**
     * 分析时间
     */
    private Date analyzedTime;

    /**
     * 审核人
     */
    private String reviewedBy;

    /**
     * 审核时间
     */
    private Date reviewedTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
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
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnalysisCode() {
        return analysisCode;
    }

    public void setAnalysisCode(String analysisCode) {
        this.analysisCode = analysisCode;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(String budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getVarianceAmount() {
        return varianceAmount;
    }

    public void setVarianceAmount(BigDecimal varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public BigDecimal getVarianceRate() {
        return varianceRate;
    }

    public void setVarianceRate(BigDecimal varianceRate) {
        this.varianceRate = varianceRate;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public void setVarianceType(String varianceType) {
        this.varianceType = varianceType;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public void setVarianceReason(String varianceReason) {
        this.varianceReason = varianceReason;
    }

    public String getAnalysisDescription() {
        return analysisDescription;
    }

    public void setAnalysisDescription(String analysisDescription) {
        this.analysisDescription = analysisDescription;
    }

    public String getImprovementMeasures() {
        return improvementMeasures;
    }

    public void setImprovementMeasures(String improvementMeasures) {
        this.improvementMeasures = improvementMeasures;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getAnalysisDimension() {
        return analysisDimension;
    }

    public void setAnalysisDimension(String analysisDimension) {
        this.analysisDimension = analysisDimension;
    }

    public String getAnalysisStatus() {
        return analysisStatus;
    }

    public void setAnalysisStatus(String analysisStatus) {
        this.analysisStatus = analysisStatus;
    }

    public String getAnalyzedBy() {
        return analyzedBy;
    }

    public void setAnalyzedBy(String analyzedBy) {
        this.analyzedBy = analyzedBy;
    }

    public Date getAnalyzedTime() {
        return analyzedTime;
    }

    public void setAnalyzedTime(Date analyzedTime) {
        this.analyzedTime = analyzedTime;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedTime() {
        return reviewedTime;
    }

    public void setReviewedTime(Date reviewedTime) {
        this.reviewedTime = reviewedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    // ========== 前端兼容别名字段（只读，不写库） ==========

    /**
     * 前端用 budgetAccountName 显示预算科目名称，映射到 accountName
     */
    @JsonProperty("budgetAccountName")
    public String getBudgetAccountName() {
        return this.accountName;
    }

    /**
     * 前端用 analysisDate 显示分析日期，映射到 analyzedTime（格式化为 yyyy-MM-dd）
     */
    @JsonProperty("analysisDate")
    public String getAnalysisDate() {
        if (this.analyzedTime == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(this.analyzedTime);
    }

    /**
     * 前端用 responsiblePerson 显示责任人，映射到 reviewedBy
     */
    @JsonProperty("responsiblePerson")
    public String getResponsiblePerson() {
        return this.reviewedBy;
    }

    /**
     * 前端用 responsiblePersonName 显示责任人名称，同样映射 reviewedBy
     */
    @JsonProperty("responsiblePersonName")
    public String getResponsiblePersonName() {
        return this.reviewedBy;
    }

    // ========== 前端传入的额外字段，不写库，仅用于接收并转换 ==========

    /**
     * 前端传入的分析周期数组 [startDate, endDate]，不对应数据库字段。
     * Jackson 反序列化时会调用此 setter，将第一个元素赋给 analyzedTime。
     */
    @JsonProperty("analysisPeriod")
    public void setAnalysisPeriod(List<String> analysisPeriod) {
        if (analysisPeriod != null && !analysisPeriod.isEmpty() && analysisPeriod.get(0) != null) {
            try {
                // 解析 ISO 8601 格式（如 "2026-04-07T16:00:00.000Z"）
                String raw = analysisPeriod.get(0);
                // 先尝试完整的 ISO 格式
                java.text.SimpleDateFormat sdf;
                if (raw.contains("T")) {
                    sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                } else {
                    sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                }
                this.analyzedTime = sdf.parse(raw);
            } catch (Exception e) {
                // 解析失败时保留当前值，不影响主流程
            }
        }
    }
}

