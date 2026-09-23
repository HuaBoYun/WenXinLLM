package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.RollingForecastQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecast;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 滚动预测Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface RollingForecastService {

    /**
     * 创建预测任务
     * 
     * @param forecast 预测任务
     */
    void createForecast(TblRollingForecast forecast);

    /**
     * 修改预测任务
     * 
     * @param forecast 预测任务
     */
    void updateForecast(TblRollingForecast forecast);

    /**
     * 查询预测任务列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblRollingForecast> getForecastList(RollingForecastQueryParam param);

    /**
     * 根据ID查询预测任务
     * 
     * @param forecastId 预测任务ID
     * @return 预测任务
     */
    TblRollingForecast getForecastById(String forecastId);

    /**
     * 根据ID查询预测任务(包含数据明细)
     * 
     * @param forecastId 预测任务ID
     * @return 预测任务
     */
    TblRollingForecast getForecastWithData(String forecastId);

    /**
     * 删除预测任务
     * 
     * @param forecastId 预测任务ID
     */
    void deleteForecast(String forecastId);

    /**
     * 批量删除预测任务
     * 
     * @param forecastIds 预测任务ID列表
     */
    void batchDeleteForecast(List<String> forecastIds);

    /**
     * 提交预测任务
     * 
     * @param forecastId 预测任务ID
     */
    void submitForecast(String forecastId);

    /**
     * 审批预测任务
     * 
     * @param forecastId 预测任务ID
     * @param approved 是否通过
     * @param opinion 审批意见
     */
    void approveForecast(String forecastId, Boolean approved, String opinion);

    /**
     * 撤销预测任务
     * 
     * @param forecastId 预测任务ID
     */
    void withdrawForecast(String forecastId);

    /**
     * 查询预测任务统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getForecastStatistics();

    /**
     * 生成预测数据（基于基准数据）
     * 
     * @param forecastId 预测任务ID
     */
    void generateForecastData(String forecastId);
}

