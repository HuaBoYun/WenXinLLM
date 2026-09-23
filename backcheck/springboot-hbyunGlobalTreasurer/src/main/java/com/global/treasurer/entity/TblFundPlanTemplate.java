package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 资金计划模板实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FUND_PLAN_TEMPLATE")
public class TblFundPlanTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long templateId;

    /** 模板编号 */
    private String templateNo;

    /** 模板名称 */
    private String templateName;

    /** 模板类型(PLAN-计划模板,ANALYSIS-分析模板,FORECAST-预测模板,REPORT-报告模板,APPROVAL-审批模板) */
    private String templateType;

    /** 模板状态(ACTIVE-活跃,INACTIVE-停用) */
    private String templateStatus;

    /** 版本 */
    private String templateVersion;

    /** 使用次数 */
    private Integer usageCount;

    /** 质量分数 */
    private Integer qualityScore;

    /** 最后使用时间 */
    private Date lastUsedTime;

    /** 模板描述 */
    private String templateDescription;

    /** 模板内容(JSON格式) */
    private String templateContent;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 组织ID */
    private Long orgId;

    /** 组织名称 */
    private String orgName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getTemplateNo() { return templateNo; }
    public void setTemplateNo(String templateNo) { this.templateNo = templateNo; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    public String getTemplateStatus() { return templateStatus; }
    public void setTemplateStatus(String templateStatus) { this.templateStatus = templateStatus; }
    public String getTemplateVersion() { return templateVersion; }
    public void setTemplateVersion(String templateVersion) { this.templateVersion = templateVersion; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }
    public Date getLastUsedTime() { return lastUsedTime; }
    public void setLastUsedTime(Date lastUsedTime) { this.lastUsedTime = lastUsedTime; }
    public String getTemplateDescription() { return templateDescription; }
    public void setTemplateDescription(String templateDescription) { this.templateDescription = templateDescription; }
    public String getTemplateContent() { return templateContent; }
    public void setTemplateContent(String templateContent) { this.templateContent = templateContent; }
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
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

}
