package com.financial.sharing.service;

import com.hbfk.util.JsonBean;
import com.financial.sharing.dto.CostTrendQueryParam;
import com.financial.sharing.dto.BudgetExecutionQueryParam;
import com.financial.sharing.dto.AlertQueryParam;

/**
 * 报表管理服务接口
 */
public interface ReportManagementService {

    /**
     * 获取管理概览统计
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    JsonBean getOverview(Integer bookId, Integer tenantId);

    /**
     * 获取成本趋势分析数据
     *
     * @param param 查询参数
     * @return 趋势数据
     */
    JsonBean getCostTrend(CostTrendQueryParam param);

    /**
     * 获取预算执行分析数据
     *
     * @param param 查询参数
     * @return 预算执行数据
     */
    JsonBean getBudgetExecution(BudgetExecutionQueryParam param);

    /**
     * 获取异常预警列表
     *
     * @param param 查询参数
     * @return 预警列表
     */
    JsonBean getAlerts(AlertQueryParam param);
}
