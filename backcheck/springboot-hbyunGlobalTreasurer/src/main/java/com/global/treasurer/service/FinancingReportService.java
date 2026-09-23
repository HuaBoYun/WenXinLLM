package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 融资报表服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancingReportService {

    List<Map<String, Object>> getReportTemplates();

    Map<String, Object> generateReport(String reportType, Map<String, Object> params);

    byte[] exportReport(String reportType, Map<String, Object> params, String format);

    List<Map<String, Object>> getScheduledReports();

    Map<String, Object> saveScheduledReport(Map<String, Object> reportConfig);

    void deleteScheduledReport(Long reportId);

    List<Map<String, Object>> getReportHistory(Map<String, Object> params);

    Map<String, Object> getFinancingSummaryReport(Long companyId, String startDate, String endDate);

    Map<String, Object> getCreditSummaryReport(Long companyId, String startDate, String endDate);

    Map<String, Object> getGuaranteeSummaryReport(Long companyId, String startDate, String endDate);

    Map<String, Object> getRepaymentScheduleReport(Long companyId, String startDate, String endDate);

    Map<String, Object> getRiskAnalysisReport(Long companyId, String startDate, String endDate);
}

