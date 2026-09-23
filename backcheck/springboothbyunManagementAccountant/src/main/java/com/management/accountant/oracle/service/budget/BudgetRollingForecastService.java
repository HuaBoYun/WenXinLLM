package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetRollingForecast;

import java.util.List;

/**
 * 预算滚动预测Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetRollingForecastService extends IService<BudgetRollingForecast> {

    /**
     * 根据预算ID查询滚动预测列表
     * 
     * @param budgetId 预算ID
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> listByBudgetId(String budgetId);

    /**
     * 根据预算年度查询滚动预测列表
     * 
     * @param budgetYear 预算年度
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> listByBudgetYear(Integer budgetYear);

    /**
     * 根据组织ID查询滚动预测列表
     * 
     * @param organizationId 组织ID
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> listByOrganizationId(String organizationId);

    /**
     * 查询最新版本的滚动预测列表
     * 
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> listLatestVersions();

    /**
     * 根据预测状态查询滚动预测列表
     * 
     * @param forecastStatus 预测状态
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> listByForecastStatus(String forecastStatus);

    /**
     * 执行滚动预测
     * 
     * @param forecastId 预测ID
     * @return 是否成功
     */
    boolean executeRollingForecast(String forecastId);

    /**
     * 批量删除滚动预测
     * 
     * @param forecastIds 预测ID列表
     * @return 是否成功
     */
    boolean batchDeleteForecasts(List<String> forecastIds);
}

