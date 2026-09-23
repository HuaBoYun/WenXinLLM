package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CostCenterQueryParam;
import com.financial.sharing.vo.param.CostCenterSaveParam;
import com.financial.sharing.vo.result.CostCenterVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成本中心服务接口
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface CostCenterService {

    /**
     * 分页查询成本中心列表
     */
    MyJsonBean<PageResult<CostCenterVO>> getCostCenterList(CostCenterQueryParam param);

    /**
     * 保存或更新成本中心
     */
    MyJsonBean saveOrUpdateCostCenter(CostCenterSaveParam param);

    /**
     * 根据ID查询成本中心详情
     */
    MyJsonBean<CostCenterVO> getCostCenterById(String centerId);

    /**
     * 删除成本中心
     */
    MyJsonBean deleteCostCenter(String centerId);

    /**
     * 批量删除成本中心
     */
    MyJsonBean batchDeleteCostCenter(List<String> centerIds);

    /**
     * 启用/停用成本中心
     */
    MyJsonBean enableCostCenter(String centerId, Integer isEnabled);

    /**
     * 批量更新成本中心状态
     */
    MyJsonBean batchUpdateCostCenterStatus(List<String> centerIds, Integer isEnabled);

    /**
     * 获取成本中心选项列表（用于下拉框）
     */
    MyJsonBean<List<Map<String, Object>>> getCostCenterOptions(String bookId, String tenantId);

    /**
     * 导出成本中心数据
     */
    void exportCostCenter(CostCenterQueryParam param, HttpServletResponse response);

    /**
     * 导入成本中心数据
     */
    MyJsonBean importCostCenter(String filePath);

    // ==================== 成本预算相关方法 ====================

    /**
     * 分页查询成本预算列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getCostBudgetList(Map<String, Object> param);

    /**
     * 保存或更新成本预算
     */
    MyJsonBean saveOrUpdateCostBudget(Map<String, Object> param);

    /**
     * 删除成本预算
     */
    MyJsonBean deleteCostBudget(String budgetId);

    /**
     * 获取成本预算统计数据
     */
    MyJsonBean<Map<String, Object>> getCostBudgetStats(String period);

    /**
     * 获取成本预算详情
     */
    MyJsonBean<Map<String, Object>> getCostBudgetById(String budgetId);

    /**
     * 批量审批成本预算
     */
    MyJsonBean batchApproveCostBudget(List<String> budgetIds, Map<String, Object> auditData);

    /**
     * 获取成本预算监控数据
     */
    MyJsonBean<Map<String, Object>> getCostBudgetMonitorData(Map<String, Object> params);

    /**
     * 获取成本预算分析数据
     */
    MyJsonBean<Map<String, Object>> getCostBudgetAnalysisData(Map<String, Object> params);

    // ==================== 新增接口：成本中心统计概览 ====================

    /**
     * 获取成本中心统计概览
     */
    MyJsonBean<Map<String, Object>> getCostCenterStats(String period, Long bookId, Long tenantId);

    // ==================== 新增接口：成本预算管理 ====================

    /**
     * 获取预算概览
     */
    MyJsonBean<Map<String, Object>> getBudgetOverview(String period, Long bookId, Long tenantId);

    /**
     * 调整成本预算
     */
    MyJsonBean<Map<String, Object>> adjustCostBudget(String budgetId, Map<String, Object> adjustData);

    /**
     * 导出成本预算报告
     */
    void exportBudgetReport(HttpServletResponse response, String budgetYear, String budgetType,
                            String centerId, Long bookId, Long tenantId);

    // ==================== 新增接口：成本预算审批流程 ====================

    /**
     * 提交预算审批
     */
    MyJsonBean submitBudgetApproval(String budgetId, Map<String, Object> submitData);

    /**
     * 审批预算
     */
    MyJsonBean approveBudget(String budgetId, Map<String, Object> approvalData);

    /**
     * 获取预算执行情况
     */
    MyJsonBean<Map<String, Object>> getBudgetExecution(String budgetId, String period);

    // ==================== 新增接口：成本中心树形结构 ====================

    /**
     * 获取成本中心树形结构
     */
    MyJsonBean<List<Map<String, Object>>> getCostCenterTree(String bookId, String tenantId);

    // ==================== 新增接口：成本控制管理 ====================

    /**
     * 获取成本控制概览
     */
    MyJsonBean<Map<String, Object>> getControlOverview(String period, Long bookId, Long tenantId);

    /**
     * 批量删除成本控制
     */
    MyJsonBean<Map<String, Object>> batchDeleteCostControl(List<String> controlIds, Long bookId, Long tenantId);

    /**
     * 批量启用/停用成本控制
     */
    MyJsonBean<Map<String, Object>> batchUpdateControlStatus(List<String> controlIds, Integer isEnabled,
                                                        Long bookId, Long tenantId);

    /**
     * 获取成本控制预警列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getControlAlerts(String period, String alertType,
                                                            Long bookId, Long tenantId,
                                                            Integer pageNumber, Integer pageSize);

    /**
     * 处理成本控制预警
     */
    MyJsonBean<Map<String, Object>> handleControlAlert(String alertId, Map<String, Object> handleData);

    // ==================== 新增接口：成本分析管理 ====================

    /**
     * 获取成本对比分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostCompareAnalysis(String startPeriod, String endPeriod,
                                                            List<String> centerIds,
                                                            Long bookId, Long tenantId);

    /**
     * 获取成本差异分析
     */
    MyJsonBean<Map<String, Object>> getCostVarianceAnalysis(String period, String centerId,
                                                        Long bookId, Long tenantId);

    /**
     * 获取成本绩效评价
     */
    MyJsonBean<Map<String, Object>> getCostPerformanceEvaluation(String period, String centerId,
                                                            Long bookId, Long tenantId);

    /**
     * 生成成本分析报告
     */
    MyJsonBean<Map<String, Object>> generateAnalysisReport(Map<String, Object> params);

    // ==================== 成本控制相关接口 ====================

    /**
     * 分页查询成本控制列表
     */
    MyJsonBean<Map<String, Object>> getCostControlList(Map<String, Object> param);

    /**
     * 保存或更新成本控制规则
     */
    MyJsonBean<Map<String, Object>> saveOrUpdateCostControl(Map<String, Object> param);

    /**
     * 单条切换成本控制启用状态
     */
    MyJsonBean<Map<String, Object>> updateCostControlStatus(String controlId, Integer isEnabled);

    /**
     * 获取成本控制统计数据
     */
    MyJsonBean<Map<String, Object>> getCostControlStats(Map<String, Object> param);

    // ==================== 成本分析相关接口 ====================

    /**
     * 获取成本分析数据
     */
    MyJsonBean<Map<String, Object>> getCostAnalysisData(Map<String, Object> param);

    /**
     * 获取成本结构分析
     */
    MyJsonBean<Map<String, Object>> getCostStructureAnalysis(Map<String, Object> param);

    /**
     * 获取成本趋势分析
     */
    MyJsonBean<List<Map<String, Object>>> getCostTrendAnalysis(Map<String, Object> param);

    /**
     * 生成成本分析报告
     */
    MyJsonBean<Map<String, Object>> generateCostAnalysisReport(Map<String, Object> param);

    /**
     * 获取分摊规则列表
     */
    MyJsonBean getAllocationRules(String bookId, String tenantId);
}

