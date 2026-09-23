package com.financial.sharing.mysql.entity;

import java.util.Date;

/**
 * 成本分析报告表实体类
 * @author AI Assistant
 * @date 2025-12-23
 */
public class CostAnalysisReportEntity {

    private String reportId;          // 报告ID
    private String reportName;        // 报告名称
    private String reportType;        // 报告类型（PDF:PDF文件 EXCEL:Excel文件）
    private String startPeriod;       // 起始期间
    private String endPeriod;         // 结束期间
    private String centerIds;         // 成本中心ID列表（逗号分隔）
    private String filePath;          // 文件存储路径
    private Long fileSize;            // 文件大小（字节）
    private String generatorId;       // 生成人ID
    private Date generateTime;        // 生成时间
    private Long bookId;              // 账簿ID
    private Long tenantId;            // 租户ID
    private Date createTime;          // 创建时间
    private Integer isDeleted;        // 删除标识（0:未删除 1:已删除）
    private Long version;             // 版本号（乐观锁）

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getCenterIds() {
        return centerIds;
    }

    public void setCenterIds(String centerIds) {
        this.centerIds = centerIds;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(String generatorId) {
        this.generatorId = generatorId;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
