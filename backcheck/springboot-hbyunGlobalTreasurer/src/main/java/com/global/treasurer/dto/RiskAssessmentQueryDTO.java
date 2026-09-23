package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 风险评估查询DTO
 * 对应已存在的 TBL_RISK_ASSESSMENT 表
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
public class RiskAssessmentQueryDTO {
    /** 页码 */
    private Integer page = 1;

    /** 页大小 */
    private Integer limit = 10;

    /** 企业ID */
    private String enterpriseId;

    /** 风险ID（评估编号，模糊搜索） */
    private String riskId;

    /** 风险类别 */
    private String riskCategory;

    /** 风险项 */
    private String riskItem;

    /** 风险等级 */
    private String riskLevel;

    /** 状态 */
    private String status;

    /** 评估开始日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assessmentDateStart;

    /** 评估结束日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assessmentDateEnd;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(String enterpriseId) { this.enterpriseId = enterpriseId; }
    public String getRiskId() { return riskId; }
    public void setRiskId(String riskId) { this.riskId = riskId; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public String getRiskItem() { return riskItem; }
    public void setRiskItem(String riskItem) { this.riskItem = riskItem; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getAssessmentDateStart() { return assessmentDateStart; }
    public void setAssessmentDateStart(Date assessmentDateStart) { this.assessmentDateStart = assessmentDateStart; }
    public Date getAssessmentDateEnd() { return assessmentDateEnd; }
    public void setAssessmentDateEnd(Date assessmentDateEnd) { this.assessmentDateEnd = assessmentDateEnd; }


    public int getPage() { return page != null ? page : 1; }
    public void setPage(int page) { this.page = page; }
    public int getLimit() { return limit != null ? limit : 10; }
    public void setLimit(int limit) { this.limit = limit; }
}
