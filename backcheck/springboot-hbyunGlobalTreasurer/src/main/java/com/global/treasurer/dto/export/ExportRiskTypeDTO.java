package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblRiskType;
import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 风险类型导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-25
 */
public class ExportRiskTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "风险类型编码", sort = 1, words = 18)
    private String riskTypeCode;

    @ExcelField(title = "风险类型名称", sort = 2, words = 20)
    private String riskTypeName;

    @ExcelField(title = "风险类别", sort = 3, words = 12)
    private String riskCategory;

    @ExcelField(title = "风险描述", sort = 4, words = 30)
    private String riskDescription;

    @ExcelField(title = "影响程度", sort = 5, words = 10)
    private String impactLevel;

    @ExcelField(title = "发生概率", sort = 6, words = 10)
    private String probabilityLevel;

    @ExcelField(title = "风险等级", sort = 7, words = 10)
    private String riskLevel;

    @ExcelField(title = "控制措施", sort = 8, words = 30)
    private String controlMeasures;

    @ExcelField(title = "负责部门", sort = 9, words = 15)
    private String responsibleDepartment;

    @ExcelField(title = "负责人", sort = 10, words = 12)
    private String responsiblePerson;

    @ExcelField(title = "审查频率", sort = 11, words = 12)
    private String reviewFrequency;

    @ExcelField(title = "状态", sort = 12, words = 8)
    private String status;

    @ExcelField(title = "创建时间", sort = 13, words = 20)
    private String createTime;

    @ExcelField(title = "备注", sort = 14, words = 25)
    private String remark;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportRiskTypeDTO fromEntity(TblRiskType entity) {
        if (entity == null) return null;
        ExportRiskTypeDTO dto = new ExportRiskTypeDTO();
        dto.setRiskTypeCode(entity.getRiskTypeCode());
        dto.setRiskTypeName(entity.getRiskTypeName());
        dto.setRiskCategory(convertCategory(entity.getRiskCategory()));
        dto.setRiskDescription(entity.getRiskDescription());
        dto.setImpactLevel(convertLevel(entity.getImpactLevel()));
        dto.setProbabilityLevel(convertLevel(entity.getProbabilityLevel()));
        dto.setRiskLevel(convertLevel(entity.getRiskLevel()));
        dto.setControlMeasures(entity.getControlMeasures());
        dto.setResponsibleDepartment(entity.getResponsibleDepartment());
        dto.setResponsiblePerson(entity.getResponsiblePerson());
        dto.setReviewFrequency(entity.getReviewFrequency());
        dto.setStatus(entity.getIsEnabled() != null && entity.getIsEnabled() == 1 ? "启用" : "禁用");
        dto.setCreateTime(entity.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getCreateTime()) : null);
        dto.setRemark(entity.getRemark());
        return dto;
    }

    private static String convertCategory(String category) {
        if (category == null) return "";
        switch (category) {
            case "MARKET": return "市场风险";
            case "CREDIT": return "信用风险";
            case "LIQUIDITY": return "流动性风险";
            case "OPERATIONAL": return "操作风险";
            case "COMPLIANCE": return "合规风险";
            case "REPUTATION": return "声誉风险";
            default: return category;
        }
    }

    private static String convertLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "LOW": return "低";
            case "MEDIUM": return "中";
            case "HIGH": return "高";
            case "CRITICAL": return "严重";
            default: return level;
        }
    }

    // Getters and Setters
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
    public String getResponsibleDepartment() { return responsibleDepartment; }
    public void setResponsibleDepartment(String responsibleDepartment) { this.responsibleDepartment = responsibleDepartment; }
    public String getResponsiblePerson() { return responsiblePerson; }
    public void setResponsiblePerson(String responsiblePerson) { this.responsiblePerson = responsiblePerson; }
    public String getReviewFrequency() { return reviewFrequency; }
    public void setReviewFrequency(String reviewFrequency) { this.reviewFrequency = reviewFrequency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

