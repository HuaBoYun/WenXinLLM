package com.management.accountant.service;

import java.util.Map;

/**
 * 期间报告Service接口
 *
 * @author AI Agent
 * @date 2026-03-31
 */
public interface PeriodReportService {
    Map<String, Object> getReportData(String periodId);
    Map<String, Object> getDetailPage(Map<String, Object> params);
    void exportReport(String periodId, javax.servlet.http.HttpServletResponse response);
}
