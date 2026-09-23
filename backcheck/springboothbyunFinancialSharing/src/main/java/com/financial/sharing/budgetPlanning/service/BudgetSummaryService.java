package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetSummaryQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetSummary;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 预算数据汇总Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetSummaryService {

    /**
     * 执行汇总
     * 
     * @param modelId 预算模型ID
     * @param period 预算期间
     * @param version 预算版本
     * @param summaryType 汇总类型
     * @param summaryMethod 汇总方法
     */
    void executeSummary(String modelId, String period, String version, String summaryType, String summaryMethod);

    /**
     * 查询汇总记录列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetSummary> getSummaryList(BudgetSummaryQueryParam param);

    /**
     * 根据ID查询汇总记录
     * 
     * @param summaryId 汇总ID
     * @return 汇总记录
     */
    TblBudgetSummary getSummaryById(String summaryId);

    /**
     * 根据条件查询汇总记录
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param version 版本
     * @param summaryType 汇总类型
     * @return 汇总记录列表
     */
    List<TblBudgetSummary> getSummaryByCondition(String modelId, String period, String version, String summaryType);

    /**
     * 删除汇总记录
     * 
     * @param summaryId 汇总ID
     */
    void deleteSummary(String summaryId);

    /**
     * 批量删除汇总记录
     * 
     * @param summaryIds 汇总ID列表
     */
    void batchDeleteSummary(List<String> summaryIds);

    /**
     * 重新执行汇总
     * 
     * @param summaryId 汇总ID
     */
    void reExecuteSummary(String summaryId);

    /**
     * 查询汇总统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getSummaryStatistics();

    /**
     * 导出汇总数据(写入 HttpServletResponse 流, 由前端浏览器接收 xlsx 下载)
     *
     * @param summaryId 汇总ID(必传, 用于定位同一批次的汇总记录)
     * @param response  HTTP响应
     */
    void exportSummary(String summaryId, javax.servlet.http.HttpServletResponse response);
}

