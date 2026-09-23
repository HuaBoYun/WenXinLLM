package com.global.treasurer.dto;

// import lombok.Data; // 已移除

/**
 * 风险类型查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
public class RiskTypeQueryDTO {
    /** 页码 */
    private Integer pageNum = 1;

    /** 页大小 */
    private Integer pageSize = 10;

    /** 风险类型编码 */
    private String riskTypeCode;

    /** 风险类型名称 */
    private String riskTypeName;

    /** 风险类别 */
    private String riskCategory;

    /** 影响程度 */
    private String impactLevel;

    /** 发生概率 */
    private String probabilityLevel;

    /** 状态 */
    private Integer isEnabled;

    /** 组织ID */
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRiskTypeCode() { return riskTypeCode; }
    public void setRiskTypeCode(String riskTypeCode) { this.riskTypeCode = riskTypeCode; }
    public String getRiskTypeName() { return riskTypeName; }
    public void setRiskTypeName(String riskTypeName) { this.riskTypeName = riskTypeName; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public String getImpactLevel() { return impactLevel; }
    public void setImpactLevel(String impactLevel) { this.impactLevel = impactLevel; }
    public String getProbabilityLevel() { return probabilityLevel; }
    public void setProbabilityLevel(String probabilityLevel) { this.probabilityLevel = probabilityLevel; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
