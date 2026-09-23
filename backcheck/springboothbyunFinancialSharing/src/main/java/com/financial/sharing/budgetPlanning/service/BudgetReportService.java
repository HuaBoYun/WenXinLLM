package com.financial.sharing.budgetPlanning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.budgetPlanning.dto.BudgetReportQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetReport;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 预算报表Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetReportService extends IService<TblBudgetReport> {

    /**
     * 查询报表配置列表（分页）
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetReport> getReportList(BudgetReportQueryParam param);

    /**
     * 根据ID查询报表配置
     * 
     * @param reportId 报表ID
     * @return 报表配置
     */
    TblBudgetReport getReportById(String reportId);

    /**
     * 创建报表配置
     * 
     * @param report 报表配置
     * @return 创建结果
     */
    Map<String, Object> createReport(TblBudgetReport report);

    /**
     * 更新报表配置
     * 
     * @param report 报表配置
     * @return 更新结果
     */
    Map<String, Object> updateReport(TblBudgetReport report);

    /**
     * 删除报表配置
     * 
     * @param reportIds 报表ID列表
     * @return 删除结果
     */
    Map<String, Object> deleteReports(List<String> reportIds);

    /**
     * 查询预算明细数据
     * 
     * @param param 查询参数
     * @return 明细数据
     */
    PageInfo<Map<String, Object>> queryBudgetDetailData(BudgetReportQueryParam param);

    /**
     * 查询预算汇总数据
     * 
     * @param param 查询参数
     * @return 汇总数据
     */
    List<Map<String, Object>> queryBudgetSummaryData(BudgetReportQueryParam param);

    /**
     * 查询预算对比数据
     * 
     * @param param 查询参数
     * @return 对比数据
     */
    List<Map<String, Object>> queryBudgetCompareData(BudgetReportQueryParam param);

    /**
     * 查询预算趋势数据
     * 
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> queryBudgetTrendData(BudgetReportQueryParam param);

    /**
     * 导出报表数据
     * 
     * @param param 查询参数
     * @return 导出结果
     */
    Map<String, Object> exportReportData(BudgetReportQueryParam param);
}

