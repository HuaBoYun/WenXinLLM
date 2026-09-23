package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.util.Date;

/**
 * 风险类型DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
public class RiskTypeDTO {
    /** 风险类型ID */
    private Long riskTypeId;

    /** 风险类型编码 */
    private String riskTypeCode;

    /** 风险类型名称 */
    private String riskTypeName;

    /** 风险类别 */
    private String riskCategory;

    /** 风险描述 */
    private String riskDescription;

    /** 影响程度 */
    private String impactLevel;

    /** 发生概率 */
    private String probabilityLevel;

    /** 风险等级 */
    private String riskLevel;

    /** 控制措施 */
    private String controlMeasures;

    /** 监控指标 */
    private String monitoringIndicators;

    /** 应对策略 */
    private String responseStrategy;

    /** 负责部门 */
    private String responsibleDepartment;

    /** 负责人 */
    private String responsiblePerson;

    /** 审查频率 */
    private String reviewFrequency;

    /** 下次审查日期 */
    private Date nextReviewDate;

    /** 状态 */
    private Integer isEnabled;

    /** 备注 */
    private String remark;

    /** 组织ID */
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRiskTypeId() { return riskTypeId; }
    public void setRiskTypeId(Long riskTypeId) { this.riskTypeId = riskTypeId; }
    public String getRiskTypeCode() { return riskTypeCode; }
    public void setRiskTypeCode(String riskTypeCode) { this.riskTypeCode = riskTypeCode; }
    public String getRiskTypeName() { return riskTypeName; }
    public void setRiskTypeName(String riskTypeName) { this.riskTypeName = riskTypeName; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }
    public String getImpactLevel() { return impactLevel; }
    public void setImpactLevel(String impactLevel) { this.impactLevel = impactLevel; }
    public String getProbabilityLevel() { return probabilityLevel; }
    public void setProbabilityLevel(String probabilityLevel) { this.probabilityLevel = probabilityLevel; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getControlMeasures() { return controlMeasures; }
    public void setControlMeasures(String controlMeasures) { this.controlMeasures = controlMeasures; }
    public String getMonitoringIndicators() { return monitoringIndicators; }
    public void setMonitoringIndicators(String monitoringIndicators) { this.monitoringIndicators = monitoringIndicators; }
    public String getResponseStrategy() { return responseStrategy; }
    public void setResponseStrategy(String responseStrategy) { this.responseStrategy = responseStrategy; }
    public String getResponsibleDepartment() { return responsibleDepartment; }
    public void setResponsibleDepartment(String responsibleDepartment) { this.responsibleDepartment = responsibleDepartment; }
    public String getResponsiblePerson() { return responsiblePerson; }
    public void setResponsiblePerson(String responsiblePerson) { this.responsiblePerson = responsiblePerson; }
    public String getReviewFrequency() { return reviewFrequency; }
    public void setReviewFrequency(String reviewFrequency) { this.reviewFrequency = reviewFrequency; }
    public Date getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(Date nextReviewDate) { this.nextReviewDate = nextReviewDate; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

}
