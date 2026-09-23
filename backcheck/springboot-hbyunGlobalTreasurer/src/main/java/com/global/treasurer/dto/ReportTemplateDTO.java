package com.global.treasurer.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表模板DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-10
 */
public class ReportTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private String templateId;

    /** 模板代码 */
    private String templateCode;

    /** 模板名称 */
    private String templateName;

    /** 监管机构ID */
    private String authorityId;

    /** 模板类型 */
    private String templateType;

    /** 模板版本 */
    private String templateVersion;

    /** 模板内容 */
    private String templateContent;

    /** 模板文件路径 */
    private String templateFilePath;

    /** 生效日期 */
    private Date effectiveDate;

    /** 失效日期 */
    private Date expiryDate;

    /** 是否启用 */
    private Integer isEnabled;

    /** 报告频率 */
    private String reportFrequency;

    /** 描述 */
    private String description;

    /** 公司ID */
    private String companyId;

    /** 备注 */
    private String remark;

    // Getters and Setters
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getAuthorityId() { return authorityId; }
    public void setAuthorityId(String authorityId) { this.authorityId = authorityId; }
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    public String getTemplateVersion() { return templateVersion; }
    public void setTemplateVersion(String templateVersion) { this.templateVersion = templateVersion; }
    public String getTemplateContent() { return templateContent; }
    public void setTemplateContent(String templateContent) { this.templateContent = templateContent; }
    public String getTemplateFilePath() { return templateFilePath; }
    public void setTemplateFilePath(String templateFilePath) { this.templateFilePath = templateFilePath; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getReportFrequency() { return reportFrequency; }
    public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

