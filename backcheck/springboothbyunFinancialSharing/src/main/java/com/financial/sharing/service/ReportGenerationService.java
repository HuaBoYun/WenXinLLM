package com.financial.sharing.service;

import org.springframework.scheduling.annotation.Async;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报告生成服务接口
 */
public interface ReportGenerationService {

    /**
     * 生成财务分析报告
     */
    ReportResult generateFinancialAnalysisReport(FinancialAnalysisParam param);

    /**
     * 生成试算平衡报告
     */
    ReportResult generateTrialBalanceReport(TrialBalanceParam param);

    /**
     * 生成科目余额报告
     */
    ReportResult generateSubjectBalanceReport(SubjectBalanceParam param);

    /**
     * 生成凭证汇总报告
     */
    ReportResult generateVoucherSummaryReport(VoucherSummaryParam param);

    /**
     * 生成资产负债表
     */
    ReportResult generateBalanceSheetReport(BalanceSheetParam param);

    /**
     * 生成利润表
     */
    ReportResult generateIncomeStatementReport(IncomeStatementParam param);

    /**
     * 生成现金流量表
     */
    ReportResult generateCashFlowReport(CashFlowParam param);

    /**
     * 异步生成报告
     */
    @Async("reportTaskExecutor")
    void generateReportAsync(String reportId, ReportParam param);

    /**
     * 批量生成报告
     */
    BatchReportResult batchGenerateReports(BatchReportParam param);

    /**
     * 生成自定义报告
     */
    ReportResult generateCustomReport(CustomReportParam param);

    // 参数类定义
    class FinancialAnalysisParam {
        private Long bookId;
        private Long tenantId;
        private String startDate;
        private String endDate;
        private String analysisType; // TREND, COMPARISON, RATIO
        private String reportFormat;
        private Map<String, Object> options;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getAnalysisType() { return analysisType; }
        public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
        public Map<String, Object> getOptions() { return options; }
        public void setOptions(Map<String, Object> options) { this.options = options; }
    }

    class TrialBalanceParam {
        private Long bookId;
        private Long tenantId;
        private String asOfDate;
        private String reportFormat;
        private Boolean includeInactive;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getAsOfDate() { return asOfDate; }
        public void setAsOfDate(String asOfDate) { this.asOfDate = asOfDate; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
        public Boolean getIncludeInactive() { return includeInactive; }
        public void setIncludeInactive(Boolean includeInactive) { this.includeInactive = includeInactive; }
    }

    class SubjectBalanceParam {
        private Long bookId;
        private Long tenantId;
        private String startDate;
        private String endDate;
        private Long subjectId;
        private String subjectType;
        private String reportFormat;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectType() { return subjectType; }
        public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
    }

    class VoucherSummaryParam {
        private Long bookId;
        private Long tenantId;
        private String startDate;
        private String endDate;
        private String voucherType;
        private String voucherStatus;
        private String reportFormat;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getVoucherType() { return voucherType; }
        public void setVoucherType(String voucherType) { this.voucherType = voucherType; }
        public String getVoucherStatus() { return voucherStatus; }
        public void setVoucherStatus(String voucherStatus) { this.voucherStatus = voucherStatus; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
    }

    class BalanceSheetParam {
        private Long bookId;
        private Long tenantId;
        private String asOfDate;
        private String reportFormat;
        private Boolean includeDetails;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getAsOfDate() { return asOfDate; }
        public void setAsOfDate(String asOfDate) { this.asOfDate = asOfDate; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
        public Boolean getIncludeDetails() { return includeDetails; }
        public void setIncludeDetails(Boolean includeDetails) { this.includeDetails = includeDetails; }
    }

    class IncomeStatementParam {
        private Long bookId;
        private Long tenantId;
        private String startDate;
        private String endDate;
        private String reportFormat;
        private Boolean includeDetails;

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
        public Boolean getIncludeDetails() { return includeDetails; }
        public void setIncludeDetails(Boolean includeDetails) { this.includeDetails = includeDetails; }
    }

    class CashFlowParam {
        private Long bookId;
        private Long tenantId;
        private String startDate;
        private String endDate;
        private String reportFormat;
        private String cashFlowType; // DIRECT, INDIRECT

        // getters and setters
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getReportFormat() { return reportFormat; }
        public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }
        public String getCashFlowType() { return cashFlowType; }
        public void setCashFlowType(String cashFlowType) { this.cashFlowType = cashFlowType; }
    }

    class ReportParam {
        private String reportType;
        private Map<String, Object> parameters;
        private String templateId;
        private String outputFormat;
        private Map<String, Object> distributionConfig;

        // getters and setters
        public String getReportType() { return reportType; }
        public void setReportType(String reportType) { this.reportType = reportType; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
        public Map<String, Object> getDistributionConfig() { return distributionConfig; }
        public void setDistributionConfig(Map<String, Object> distributionConfig) { this.distributionConfig = distributionConfig; }
    }

    class BatchReportParam {
        private List<ReportParam> reports;
        private Boolean mergeIntoOne;
        private String outputFormat;

        // getters and setters
        public List<ReportParam> getReports() { return reports; }
        public void setReports(List<ReportParam> reports) { this.reports = reports; }
        public Boolean getMergeIntoOne() { return mergeIntoOne; }
        public void setMergeIntoOne(Boolean mergeIntoOne) { this.mergeIntoOne = mergeIntoOne; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    }

    class CustomReportParam {
        private String reportName;
        private String templateId;
        private Map<String, Object> dataSourceConfig;
        private Map<String, Object> parameters;
        private String outputFormat;

        // getters and setters
        public String getReportName() { return reportName; }
        public void setReportName(String reportName) { this.reportName = reportName; }
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public Map<String, Object> getDataSourceConfig() { return dataSourceConfig; }
        public void setDataSourceConfig(Map<String, Object> dataSourceConfig) { this.dataSourceConfig = dataSourceConfig; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    }

    // 结果类定义
    class ReportResult {
        private String reportId;
        private String reportName;
        private byte[] reportData;
        private String fileName;
        private String contentType;
        private long fileSize;
        private long generateTime;
        private String status;
        private String errorMessage;

        // getters and setters
        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public String getReportName() { return reportName; }
        public void setReportName(String reportName) { this.reportName = reportName; }
        public byte[] getReportData() { return reportData; }
        public void setReportData(byte[] reportData) { this.reportData = reportData; }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public long getFileSize() { return fileSize; }
        public void setFileSize(long fileSize) { this.fileSize = fileSize; }
        public long getGenerateTime() { return generateTime; }
        public void setGenerateTime(long generateTime) { this.generateTime = generateTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class BatchReportResult {
        private List<ReportResult> results;
        private Map<String, Object> summary;
        private long totalGenerateTime;

        // getters and setters
        public List<ReportResult> getResults() { return results; }
        public void setResults(List<ReportResult> results) { this.results = results; }
        public Map<String, Object> getSummary() { return summary; }
        public void setSummary(Map<String, Object> summary) { this.summary = summary; }
        public long getTotalGenerateTime() { return totalGenerateTime; }
        public void setTotalGenerateTime(long totalGenerateTime) { this.totalGenerateTime = totalGenerateTime; }
    }
}