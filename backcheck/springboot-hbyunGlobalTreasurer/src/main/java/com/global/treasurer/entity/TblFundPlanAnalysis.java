package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 资金计划分析实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@TableName("TBL_FUND_PLAN_ANALYSIS")
public class TblFundPlanAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 分析ID */
    @TableId(value = "ANALYSIS_ID", type = IdType.ASSIGN_ID)
    private Long analysisId;

    /** 分析编号 */
    @TableField("ANALYSIS_NO")
    private String analysisNo;

    /** 计划ID */
    @TableField("PLAN_ID")
    private Long planId;

    /** 分析类型 */
    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    /** 分析周期 */
    @TableField("ANALYSIS_PERIOD")
    private String analysisPeriod;

    /** 质量分数 */
    @TableField("QUALITY_SCORE")
    private Integer qualityScore;

    /** 是否有效(0-无效,1-有效) */
    @TableField("IS_VALID")
    private Integer isValid;

    /** 有效期至 */
    @TableField("VALID_UNTIL")
    private Date validUntil;

    /** 价值评估(HIGH-高价值,MEDIUM-中等价值,LOW-低价值) */
    @TableField("VALUE_ASSESSMENT")
    private String valueAssessment;

    /** 模板ID */
    @TableField("TEMPLATE_ID")
    private Long templateId;

    /** 分析内容 */
    @TableField("ANALYSIS_CONTENT")
    private String analysisContent;

    /** 验证说明 */
    @TableField("VALIDATE_NOTES")
    private String validateNotes;

    /** 删除标志(0-正常,1-删除) */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATED_BY")
    private Long createdBy;

    /** 创建人姓名 */
    @TableField("CREATED_BY_NAME")
    private String createdByName;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private Long updatedBy;

    /** 更新人姓名 */
    @TableField("UPDATED_BY_NAME")
    private String updatedByName;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private Long orgId;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }
    public String getAnalysisNo() { return analysisNo; }
    public void setAnalysisNo(String analysisNo) { this.analysisNo = analysisNo; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    public String getAnalysisPeriod() { return analysisPeriod; }
    public void setAnalysisPeriod(String analysisPeriod) { this.analysisPeriod = analysisPeriod; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public Integer getIsValid() { return isValid; }
    public void setIsValid(Integer isValid) { this.isValid = isValid; }
    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }
    public String getValueAssessment() { return valueAssessment; }
    public void setValueAssessment(String valueAssessment) { this.valueAssessment = valueAssessment; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getAnalysisContent() { return analysisContent; }
    public void setAnalysisContent(String analysisContent) { this.analysisContent = analysisContent; }
    public String getValidateNotes() { return validateNotes; }
    public void setValidateNotes(String validateNotes) { this.validateNotes = validateNotes; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
