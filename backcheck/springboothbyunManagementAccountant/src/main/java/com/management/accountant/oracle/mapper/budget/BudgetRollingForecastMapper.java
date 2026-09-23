package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetRollingForecast;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算滚动预测Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetRollingForecastMapper extends BaseMapper<BudgetRollingForecast> {

    /**
     * 根据预算ID查询滚动预测列表
     * 
     * @param budgetId 预算ID
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据预算年度查询滚动预测列表
     * 
     * @param budgetYear 预算年度
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据组织ID查询滚动预测列表
     * 
     * @param organizationId 组织ID
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 查询最新版本的滚动预测列表
     * 
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> selectLatestVersions();

    /**
     * 根据预测状态查询滚动预测列表
     * 
     * @param forecastStatus 预测状态
     * @return 滚动预测列表
     */
    List<BudgetRollingForecast> selectByForecastStatus(@Param("forecastStatus") String forecastStatus);
}

