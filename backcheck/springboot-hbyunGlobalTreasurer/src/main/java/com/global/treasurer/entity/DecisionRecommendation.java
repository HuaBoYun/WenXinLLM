package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 决策建议实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_DECISION_RECOMMENDATION")
public class DecisionRecommendation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 建议ID
     */
    @TableId(value = "RECOMMENDATION_ID", type = IdType.AUTO)
    private Long recommendationId;

    /**
     * 建议编号
     */
    private String recommendationNo;

    /**
     * 建议名称
     */
    private String recommendationName;

    /**
     * 建议标题
     */
    @TableField(exist = false)
    private String recommendationTitle;

    /**
     * 风险等级
     */
    @TableField(exist = false)
    private String riskLevel;

    /**
     * 建议类型(INVESTMENT/FINANCING/RISK_CONTROL/CASH_MANAGEMENT/OPTIMIZATION)
     */
    private String recommendationType;

    /**
     * 关联模型ID
     */
    private Long modelId;

    /**
     * 关联分析ID
     */
    private Long analysisId;

    /**
     * 建议内容
     */
    private String recommendationContent;

    /**
     * 预期收益
     */
    private String expectedBenefit;

    /**
     * 风险评估
     */
    private String riskAssessment;

    /**
     * 实施计划
     */
    private String implementationPlan;

    /**
     * 优先级(HIGH/MEDIUM/LOW)
     */
    private String priority;

    /**
     * 置信水平(0-100)
     */
    private BigDecimal confidenceLevel;

    /**
     * 建议状态(PENDING/REVIEWED/APPROVED/REJECTED/IMPLEMENTED)
     */
    private String recommendationStatus;

    /**
     * 审核人ID
     */
    private Long reviewUser;

    /**
     * 审核时间
     */
    private LocalDateTime reviewDate;

    /**
     * 审核意见
     */
    private String reviewOpinion;

    /**
     * 批准人ID
     */
    private Long approvalUser;

    /**
     * 批准时间
     */
    private LocalDateTime approvalDate;

    /**
     * 批准意见
     */
    private String approvalOpinion;

    /**
     * 实施人ID
     */
    private Long implementationUser;

    /**
     * 实施时间
     */
    private LocalDateTime implementationDate;

    /**
     * 实施结果
     */
    private String implementationResult;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生成时间
     */
    @TableField(exist = false)
    private LocalDateTime generateTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    private String delFlag;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }
    public String getRecommendationNo() { return recommendationNo; }
    public void setRecommendationNo(String recommendationNo) { this.recommendationNo = recommendationNo; }
    public String getRecommendationName() { return recommendationName; }
    public void setRecommendationName(String recommendationName) { this.recommendationName = recommendationName; }
    public String getRecommendationTitle() { return recommendationTitle; }
    public void setRecommendationTitle(String recommendationTitle) { this.recommendationTitle = recommendationTitle; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }
    public String getRecommendationContent() { return recommendationContent; }
    public void setRecommendationContent(String recommendationContent) { this.recommendationContent = recommendationContent; }
    public String getExpectedBenefit() { return expectedBenefit; }
    public void setExpectedBenefit(String expectedBenefit) { this.expectedBenefit = expectedBenefit; }
    public String getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(String riskAssessment) { this.riskAssessment = riskAssessment; }
    public String getImplementationPlan() { return implementationPlan; }
    public void setImplementationPlan(String implementationPlan) { this.implementationPlan = implementationPlan; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public BigDecimal getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(BigDecimal confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    public String getRecommendationStatus() { return recommendationStatus; }
    public void setRecommendationStatus(String recommendationStatus) { this.recommendationStatus = recommendationStatus; }
    public Long getReviewUser() { return reviewUser; }
    public void setReviewUser(Long reviewUser) { this.reviewUser = reviewUser; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
    public String getReviewOpinion() { return reviewOpinion; }
    public void setReviewOpinion(String reviewOpinion) { this.reviewOpinion = reviewOpinion; }
    public Long getApprovalUser() { return approvalUser; }
    public void setApprovalUser(Long approvalUser) { this.approvalUser = approvalUser; }
    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public Long getImplementationUser() { return implementationUser; }
    public void setImplementationUser(Long implementationUser) { this.implementationUser = implementationUser; }
    public LocalDateTime getImplementationDate() { return implementationDate; }
    public void setImplementationDate(LocalDateTime implementationDate) { this.implementationDate = implementationDate; }
    public String getImplementationResult() { return implementationResult; }
    public void setImplementationResult(String implementationResult) { this.implementationResult = implementationResult; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getGenerateTime() { return generateTime; }
    public void setGenerateTime(LocalDateTime generateTime) { this.generateTime = generateTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }


}
