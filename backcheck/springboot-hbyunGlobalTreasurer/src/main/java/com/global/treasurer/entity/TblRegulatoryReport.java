package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 监管报告实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_REGULATORY_REPORT")
public class TblRegulatoryReport implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 报告ID */
    @TableId(type = IdType.ASSIGN_UUID)
    private String reportId;

    /** 报告编号 */
    private String reportNo;

    /** 报告名称 */
    private String reportName;

    /** 模板ID */
    private String templateId;

    /** 监管机构ID */
    private String authorityId;

    /** 报告期间 */
    private String reportPeriod;

    /** 报告日期 */
    private Date reportDate;

    /** 截止日期 */
    private Date dueDate;

    /** 提交日期 */
    private Date submitDate;

    /** 报告状态(DRAFT/GENERATED/VALIDATED/SUBMITTED/ACCEPTED/REJECTED) */
    private String reportStatus;

    /** 报告内容 */
    private String reportContent;

    /** 报告文件路径 */
    private String reportFilePath;

    /** 提交方式 */
    private String submissionMethod;

    /** 确认编号 */
    private String acknowledgmentNo;

    /** 拒绝原因 */
    private String rejectReason;

    /** 生成时间 */
    private Date generatedTime;

    /** 验证时间 */
    private Date validatedTime;

    /** 公司ID */
    private String companyId;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;

    /** 监管机构名称（非数据库字段，JOIN查询带出） */
    @TableField(exist = false)
    private String authorityName;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public String getReportNo() { return reportNo; }
    public void setReportNo(String reportNo) { this.reportNo = reportNo; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getAuthorityId() { return authorityId; }
    public void setAuthorityId(String authorityId) { this.authorityId = authorityId; }
    public String getReportPeriod() { return reportPeriod; }
    public void setReportPeriod(String reportPeriod) { this.reportPeriod = reportPeriod; }
    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public Date getSubmitDate() { return submitDate; }
    public void setSubmitDate(Date submitDate) { this.submitDate = submitDate; }
    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }
    public String getReportContent() { return reportContent; }
    public void setReportContent(String reportContent) { this.reportContent = reportContent; }
    public String getReportFilePath() { return reportFilePath; }
    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
    public String getSubmissionMethod() { return submissionMethod; }
    public void setSubmissionMethod(String submissionMethod) { this.submissionMethod = submissionMethod; }
    public String getAcknowledgmentNo() { return acknowledgmentNo; }
    public void setAcknowledgmentNo(String acknowledgmentNo) { this.acknowledgmentNo = acknowledgmentNo; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public Date getGeneratedTime() { return generatedTime; }
    public void setGeneratedTime(Date generatedTime) { this.generatedTime = generatedTime; }
    public Date getValidatedTime() { return validatedTime; }
    public void setValidatedTime(Date validatedTime) { this.validatedTime = validatedTime; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getAuthorityName() { return authorityName; }
    public void setAuthorityName(String authorityName) { this.authorityName = authorityName; }

}
