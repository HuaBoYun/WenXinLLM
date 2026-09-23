package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 融资报表查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingReportQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 报表名称(模糊查询) */
    private String reportName;

    /** 报表类型 */
    private String reportType;

    /** 生成状态 */
    private String generationStatus;

    /** 模板ID */
    private Long templateId;

    /** 公司ID */
    private Long companyId;

    /** 生成人 */
    private Long generatedBy;

    /** 报表周期类型 */
    private String periodType;

    /** 生成开始时间 */
    private Date generationTimeStart;

    /** 生成结束时间 */
    private Date generationTimeEnd;

    /** 周期开始时间 */
    private Date periodStartDateStart;

    /** 周期开始时间结束 */
    private Date periodStartDateEnd;

    /** 周期结束时间开始 */
    private Date periodEndDateStart;

    /** 周期结束时间结束 */
    private Date periodEndDateEnd;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getGenerationStatus() { return generationStatus; }
    public void setGenerationStatus(String generationStatus) { this.generationStatus = generationStatus; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(Long generatedBy) { this.generatedBy = generatedBy; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public Date getGenerationTimeStart() { return generationTimeStart; }
    public void setGenerationTimeStart(Date generationTimeStart) { this.generationTimeStart = generationTimeStart; }
    public Date getGenerationTimeEnd() { return generationTimeEnd; }
    public void setGenerationTimeEnd(Date generationTimeEnd) { this.generationTimeEnd = generationTimeEnd; }
    public Date getPeriodStartDateStart() { return periodStartDateStart; }
    public void setPeriodStartDateStart(Date periodStartDateStart) { this.periodStartDateStart = periodStartDateStart; }
    public Date getPeriodStartDateEnd() { return periodStartDateEnd; }
    public void setPeriodStartDateEnd(Date periodStartDateEnd) { this.periodStartDateEnd = periodStartDateEnd; }
    public Date getPeriodEndDateStart() { return periodEndDateStart; }
    public void setPeriodEndDateStart(Date periodEndDateStart) { this.periodEndDateStart = periodEndDateStart; }
    public Date getPeriodEndDateEnd() { return periodEndDateEnd; }
    public void setPeriodEndDateEnd(Date periodEndDateEnd) { this.periodEndDateEnd = periodEndDateEnd; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
