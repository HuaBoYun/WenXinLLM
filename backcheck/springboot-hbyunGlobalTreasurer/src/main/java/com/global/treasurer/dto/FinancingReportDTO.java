package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 融资报表DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 模板ID */
    @NotNull(message = "报表模板不能为空")
    private Long templateId;

    /** 报表名称 */
    @NotBlank(message = "报表名称不能为空")
    private String reportName;

    /** 报表类型 */
    @NotBlank(message = "报表类型不能为空")
    private String reportType;

    /** 生成状态(PENDING-待生成,GENERATING-生成中,COMPLETED-已完成,FAILED-失败) */
    private String generationStatus;

    /** 文件路径 */
    private String filePath;

    /** 文件大小 */
    private Long fileSize;

    /** 生成时间 */
    private Date generationTime;

    /** 生成人 */
    private Long generatedBy;

    /** 生成人姓名 */
    private String generatedByName;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 报表周期类型(DAILY-日报,WEEKLY-周报,MONTHLY-月报,QUARTERLY-季报,ANNUAL-年报,CUSTOM-自定义) */
    private String periodType;

    /** 报表周期开始日期 */
    private Date periodStartDate;

    /** 报表周期结束日期 */
    private Date periodEndDate;

    /** 报表参数(JSON格式) */
    private String reportParams;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getGenerationStatus() { return generationStatus; }
    public void setGenerationStatus(String generationStatus) { this.generationStatus = generationStatus; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public Date getGenerationTime() { return generationTime; }
    public void setGenerationTime(Date generationTime) { this.generationTime = generationTime; }
    public Long getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(Long generatedBy) { this.generatedBy = generatedBy; }
    public String getGeneratedByName() { return generatedByName; }
    public void setGeneratedByName(String generatedByName) { this.generatedByName = generatedByName; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public Date getPeriodStartDate() { return periodStartDate; }
    public void setPeriodStartDate(Date periodStartDate) { this.periodStartDate = periodStartDate; }
    public Date getPeriodEndDate() { return periodEndDate; }
    public void setPeriodEndDate(Date periodEndDate) { this.periodEndDate = periodEndDate; }
    public String getReportParams() { return reportParams; }
    public void setReportParams(String reportParams) { this.reportParams = reportParams; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
