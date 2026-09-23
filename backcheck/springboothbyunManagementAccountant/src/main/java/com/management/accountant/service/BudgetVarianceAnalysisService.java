package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetVarianceAnalysis;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算差异分析Service接口
 * 
 * @description 预算差异分析业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetVarianceAnalysisService {

    /**
     * 创建差异分析
     * 
     * @param variance 差异分析对象
     * @return 创建后的差异分析对象
     */
    BudgetVarianceAnalysis create(BudgetVarianceAnalysis variance);

    /**
     * 根据ID查询差异分析
     *
     * @param id 分析ID
     * @return 差异分析对象
     */
    BudgetVarianceAnalysis getById(String id);

    /**
     * 更新差异分析
     *
     * @param variance 差异分析对象
     * @return 更新后的差异分析对象
     */
    BudgetVarianceAnalysis update(BudgetVarianceAnalysis variance);

    /**
     * 删除差异分析
     *
     * @param id 分析ID
     */
    void delete(String id);

    /**
     * 批量删除差异分析
     *
     * @param ids 分析ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 分页查询差异分析列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetVarianceAnalysis> getPage(Map<String, Object> params);

    /**
     * 执行差异分析
     * 
     * @param params 分析参数
     * @return 分析结果
     */
    Map<String, Object> analyze(Map<String, Object> params);

    /**
     * 生成差异分析报告
     *
     * @param params 报告参数
     * @return 报告数据
     */
    Map<String, Object> generateReport(Map<String, Object> params);

    /**
     * 获取组织列表
     *
     * @return 组织列表
     */
    Object getOrganizations();

    /**
     * 获取预算科目列表
     *
     * @return 预算科目列表
     */
    Object getBudgetAccounts();

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    Object getUsers();

    /**
     * 更新差异分析原因
     *
     * @param params 更新参数
     */
    void updateReason(Map<String, Object> params);

    /**
     * 导出差异分析报告
     *
     * @param params 导出参数（支持ids列表或查询条件）
     * @return 导出数据列表
     */
    List<BudgetVarianceAnalysis> exportReport(Map<String, Object> params);

    /**
     * 导出单个差异分析
     *
     * @param varianceId 差异分析ID
     * @return 导出数据列表
     */
    List<BudgetVarianceAnalysis> exportSingle(String varianceId);

    Map<String, Object> getVarianceStats();

    Map<String, Object> getVarianceChartData(Map<String, Object> params);
}

