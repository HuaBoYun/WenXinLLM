package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成本估算服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface CostEstimationService {

    // ==================== 估算方案管理 ====================

    /**
     * 分页查询估算方案列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getSchemeList(Map<String, Object> param);

    /**
     * 保存或更新估算方案
     */
    MyJsonBean saveOrUpdateScheme(Map<String, Object> schemeData);

    /**
     * 删除估算方案
     */
    MyJsonBean deleteScheme(String schemeId);

    /**
     * 批量删除估算方案
     */
    MyJsonBean batchDeleteScheme(List<String> schemeIds);

    /**
     * 获取估算方案详情
     */
    MyJsonBean<Map<String, Object>> getSchemeById(String schemeId);

    /**
     * 启用/停用估算方案
     */
    MyJsonBean toggleSchemeStatus(String schemeId, Integer isEnabled);

    // ==================== 成本模型管理 ====================

    /**
     * 分页查询成本模型列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getModelList(Map<String, Object> param);

    /**
     * 保存或更新成本模型
     */
    MyJsonBean saveOrUpdateModel(Map<String, Object> modelData);

    /**
     * 删除成本模型
     */
    MyJsonBean deleteModel(String modelId);

    /**
     * 获取成本模型详情
     */
    MyJsonBean<Map<String, Object>> getModelById(String modelId);

    /**
     * 测试成本模型
     * testData 可能是 JSON 字符串或结构化 Map，由实现内部容错解析
     */
    MyJsonBean testModel(String modelId, Object testData);

    /**
     * 获取模型选项列表
     */
    MyJsonBean<List<Map<String, Object>>> getModelOptions(String bookId, String tenantId);

    // ==================== 预算编制管理 ====================

    /**
     * 分页查询预算编制列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getBudgetList(Map<String, Object> param);

    /**
     * 保存或更新预算编制
     */
    MyJsonBean saveOrUpdateBudget(Map<String, Object> budgetData);

    /**
     * 删除预算编制
     */
    MyJsonBean deleteBudget(String budgetId);

    /**
     * 获取预算编制详情
     */
    MyJsonBean<Map<String, Object>> getBudgetById(String budgetId);

    /**
     * 提交预算审批
     */
    MyJsonBean submitBudgetApproval(String budgetId);

    /**
     * 审批预算
     */
    MyJsonBean approveBudget(String budgetId, Map<String, Object> approvalData);

    /**
     * 批量审批预算
     */
    MyJsonBean batchApproveBudget(List<String> budgetIds, Map<String, Object> approvalData);

    /**
     * 获取预算统计数据
     */
    MyJsonBean<Map<String, Object>> getBudgetStatistics(String budgetYear, String bookId, String tenantId);

    // ==================== 成本模拟管理 ====================

    /**
     * 分页查询成本模拟列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getSimulationList(Map<String, Object> param);

    /**
     * 执行成本模拟
     */
    MyJsonBean executeSimulation(Map<String, Object> simulationData);

    /**
     * 获取模拟结果
     */
    MyJsonBean<Map<String, Object>> getSimulationResult(String simulationId);

    /**
     * 删除成本模拟
     */
    MyJsonBean deleteSimulation(String simulationId);

    // ==================== 差异分析管理 ====================

    /**
     * 分页查询差异分析列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getVarianceAnalysisList(Map<String, Object> param);

    /**
     * 执行差异分析
     */
    MyJsonBean executeVarianceAnalysis(Map<String, Object> analysisData);

    /**
     * 获取差异分析详情
     */
    MyJsonBean<Map<String, Object>> getVarianceAnalysisById(String analysisId);

    /**
     * 获取差异统计数据
     */
    MyJsonBean<Map<String, Object>> getVarianceStatistics(String analysisPeriod, String bookId, String tenantId);

    /**
     * 获取差异趋势数据
     */
    MyJsonBean<List<Map<String, Object>>> getVarianceTrend(String startPeriod, String endPeriod, String bookId, String tenantId);

    // ==================== 估算报告管理 ====================

    /**
     * 分页查询估算报告列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getReportList(Map<String, Object> param);

    /**
     * 生成估算报告
     */
    MyJsonBean generateReport(Map<String, Object> reportData);

    /**
     * 获取报告详情
     */
    MyJsonBean<Map<String, Object>> getReportById(String reportId);

    /**
     * 发布报告
     */
    MyJsonBean publishReport(String reportId);

    /**
     * 物理删除报告
     */
    MyJsonBean deleteReport(String reportId);

    /**
     * 导出报告
     */
    void exportReport(String reportId, HttpServletResponse response);

    // ==================== 估算首页统计 ====================

    /**
     * 获取成本估算首页统计概览
     * 返回 { budgetProjects, totalBudget, actualCost, varianceRate }
     */
    MyJsonBean<Map<String, Object>> getCostEstimateSummary(Map<String, Object> param);

    // ==================== 通用：模型状态切换 / 导入 / 导出 ====================

    /**
     * 启用/停用成本模型（单字段更新）
     */
    MyJsonBean toggleModelStatus(String modelId, Integer isEnabled);

    /**
     * 批量导入估算数据
     * param 支持：{ type: 'scheme'|'model', list: [...] }
     * 占位实现：循环调用 saveOrUpdate
     */
    MyJsonBean batchImportEstimateData(Map<String, Object> param);

    /**
     * 导出估算数据为 CSV 流
     * param 支持任意查询条件，导出当前条件下的方案/模型列表
     */
    void exportEstimateData(Map<String, Object> param, HttpServletResponse response);
}

