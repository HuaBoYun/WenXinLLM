package com.global.treasurer.dto;

import java.io.Serializable;

/**
 * 报表模板查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-10
 */
public class ReportTemplateQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 模板代码 */
    private String templateCode;

    /** 模板名称 */
    private String templateName;

    /** 监管机构ID */
    private String authorityId;

    /** 模板类型 */
    private String templateType;

    /** 是否启用 */
    private Integer isEnabled;

    /** 公司ID */
    private String companyId;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页大小 */
    private Integer pageSize = 10;

    // Getters and Setters
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getAuthorityId() { return authorityId; }
    public void setAuthorityId(String authorityId) { this.authorityId = authorityId; }
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}

