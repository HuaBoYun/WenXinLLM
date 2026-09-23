package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAnalysisReport;
import com.management.accountant.oracle.entity.budget.BudgetReportShare;
import com.management.accountant.oracle.entity.budget.BudgetReportSchedule;
import com.management.accountant.oracle.entity.budget.BudgetReportTemplate;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算分析报告Service接口
 * 
 * @description 预算分析报告业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetAnalysisReportService {

    /**
     * 创建分析报告
     * 
     * @param report 报告对象
     * @return 创建后的报告对象
     */
    BudgetAnalysisReport create(BudgetAnalysisReport report);

    /**
     * 根据ID查询报告
     * 
     * @param id 报告ID
     * @return 报告对象
     */
    BudgetAnalysisReport getById(String id);

    /**
     * 更新分析报告
     * 
     * @param report 报告对象
     */
    void update(BudgetAnalysisReport report);

    /**
     * 删除分析报告
     * 
     * @param id 报告ID
     */
    void delete(String id);

    /**
     * 分页查询报告列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetAnalysisReport> getPage(Map<String, Object> params);

    /**
     * 生成分析报告
     * 
     * @param params 生成参数
     * @return 生成的报告对象
     */
    BudgetAnalysisReport generate(Map<String, Object> params);

    Map<String, Object> getReportStats();

    Map<String, Object> getReportTemplates();

    /**
     * 批量导出报告（返回导出结果信息）
     */
    Map<String, Object> batchExport(java.util.List<String> ids);

    /**
     * 重新生成报告
     */
    BudgetAnalysisReport regenerate(String id);

    /**
     * 复制报告
     */
    BudgetAnalysisReport copy(String id);

    // ---- 分享 ----
    BudgetReportShare shareReport(Map<String, Object> params);
    List<BudgetReportShare> getSharesByReportId(String reportId);

    // ---- 定时生成 ----
    BudgetReportSchedule createSchedule(Map<String, Object> params);
    List<BudgetReportSchedule> getSchedulesByReportId(String reportId);
    void updateScheduleStatus(String scheduleId, Integer status);
    void deleteSchedule(String scheduleId);

    // ---- 模板管理 ----
    PageResult<BudgetReportTemplate> getTemplatePage(Map<String, Object> params);
    BudgetReportTemplate createTemplate(BudgetReportTemplate template);
    void updateTemplate(BudgetReportTemplate template);
    void deleteTemplate(String templateId);
}

