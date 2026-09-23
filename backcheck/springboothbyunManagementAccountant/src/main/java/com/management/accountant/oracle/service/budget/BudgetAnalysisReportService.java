package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisReport;

import java.util.List;

/**
 * 预算分析报告Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetAnalysisReportService extends IService<BudgetAnalysisReport> {

    /**
     * 根据报告类型查询报告列表
     * 
     * @param reportType 报告类型
     * @return 报告列表
     */
    List<BudgetAnalysisReport> listByReportType(String reportType);

    /**
     * 根据预算年度查询报告列表
     * 
     * @param budgetYear 预算年度
     * @return 报告列表
     */
    List<BudgetAnalysisReport> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询报告列表
     * 
     * @param organizationId 组织ID
     * @return 报告列表
     */
    List<BudgetAnalysisReport> listByOrganizationId(String organizationId);

    /**
     * 根据报告状态查询报告列表
     * 
     * @param reportStatus 报告状态
     * @return 报告列表
     */
    List<BudgetAnalysisReport> listByReportStatus(String reportStatus);

    /**
     * 查询已发布的报告列表
     * 
     * @return 报告列表
     */
    List<BudgetAnalysisReport> listPublishedReports();

    /**
     * 生成报告
     * 
     * @param reportId 报告ID
     * @return 是否成功
     */
    boolean generateReport(String reportId);

    /**
     * 发布报告
     * 
     * @param reportId 报告ID
     * @return 是否成功
     */
    boolean publishReport(String reportId);

    /**
     * 批量删除报告
     * 
     * @param reportIds 报告ID列表
     * @return 是否成功
     */
    boolean batchDeleteReports(List<String> reportIds);
}

