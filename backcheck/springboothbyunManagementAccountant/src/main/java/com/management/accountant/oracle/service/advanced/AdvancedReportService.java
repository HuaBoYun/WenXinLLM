package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.AdvancedReport;
import com.management.accountant.oracle.entity.advanced.ReportConfig;
import com.management.accountant.oracle.entity.advanced.ReportGenHistory;
import com.management.accountant.oracle.entity.advanced.ReportSubscriber;

import java.util.List;
import java.util.Map;

public interface AdvancedReportService {
    List<AdvancedReport> selectList(Map<String, Object> params);
    Page<AdvancedReport> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    AdvancedReport selectById(String reportId);
    boolean insert(AdvancedReport report);
    boolean update(AdvancedReport report);
    boolean deleteById(String reportId);
    boolean copyReport(String reportId);
    boolean generateReport(String reportId);
    Map<String, Object> getStats();
    List<ReportSubscriber> getSubscribers(String reportId);
    List<ReportGenHistory> getGenerationHistory(String reportId);
    ReportConfig getReportConfig(String reportId);
    Map<String, Object> getTypeStats();
    boolean updateSchedule(String reportId, String frequency);
    boolean addSubscriber(ReportSubscriber subscriber);
    boolean removeSubscriber(String subscriberId);
}
