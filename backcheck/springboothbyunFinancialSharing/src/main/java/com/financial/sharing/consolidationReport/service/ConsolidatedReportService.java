package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.ConsolidatedReportQueryParam;
import com.financial.sharing.consolidationReport.dto.ReportCompareResult;
import com.financial.sharing.consolidationReport.entity.TblConsolidatedReport;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 合并报表Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidatedReportService {

    /**
     * 查询合并报表列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblConsolidatedReport> getReportList(ConsolidatedReportQueryParam param);

    /**
     * 查询合并报表列表(不分页)
     * 
     * @param param 查询参数
     * @return 合并报表列表
     */
    List<TblConsolidatedReport> getReportListNoPage(ConsolidatedReportQueryParam param);

    /**
     * 根据ID查询合并报表
     * 
     * @param reportId 合并报表ID
     * @return 合并报表
     */
    TblConsolidatedReport getReportById(String reportId);

    /**
     * 生成合并报表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param reportType 报表类型(可选,为空则生成所有类型)
     * @param regenerate 是否重新生成
     * @return 生成结果
     */
    Map<String, Object> generateReport(String modelId, String period, String reportType, boolean regenerate);

    /**
     * 删除合并报表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param reportType 报表类型(可选,为空则删除所有类型)
     */
    void deleteReport(String modelId, String period, String reportType);

    /**
     * 确认合并报表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param reportType 报表类型(可选,为空则确认所有类型)
     */
    void confirmReport(String modelId, String period, String reportType);

    /**
     * 根据模型ID和期间查询报表类型列表
     *
     * @param modelId 模型ID
     * @param period 期间
     * @return 报表类型列表
     */
    List<String> getReportTypeList(String modelId, String period);

    /**
     * 导出合并报表到Excel
     *
     * @param modelId 模型ID
     * @param period 期间
     * @param reportType 报表类型
     * @param response HTTP响应
     */
    void exportReportToExcel(String modelId, String period, String reportType, HttpServletResponse response);

    /**
     * 对比两个期间的合并报表
     *
     * @param modelId 模型ID
     * @param period1 期间1
     * @param period2 期间2
     * @param reportType 报表类型
     * @return 对比结果列表
     */
    List<ReportCompareResult> compareReports(String modelId, String period1, String period2, String reportType);
}

