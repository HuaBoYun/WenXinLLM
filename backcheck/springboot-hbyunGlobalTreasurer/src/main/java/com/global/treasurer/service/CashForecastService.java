package com.global.treasurer.service;

import com.global.treasurer.entity.CashForecastConfig;
import com.global.treasurer.entity.CashForecastResult;

import java.util.List;
import java.util.Map;

/**
 * 现金流预测服务接口
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface CashForecastService {

    /**
     * 分页查询现金流预测配置列表
     */
    Map<String, Object> getForecastConfigPage(Map<String, Object> param);

    /**
     * 根据ID查询预测配置详情
     */
    CashForecastConfig getForecastConfigById(Long configId);

    /**
     * 创建预测配置
     */
    int createForecastConfig(CashForecastConfig config);

    /**
     * 更新预测配置
     */
    int updateForecastConfig(CashForecastConfig config);

    /**
     * 批量删除预测配置
     */
    int batchDeleteConfig(List<Long> ids);

    /**
     * 执行现金流预测
     */
    int executeForecast(Long configId, String executeByName) throws Exception;

    /**
     * 分页查询预测结果列表
     */
    Map<String, Object> getForecastResultPage(Map<String, Object> param);

    /**
     * 根据配置ID查询预测结果
     */
    List<CashForecastResult> getForecastResultsByConfigId(Long configId);

    /**
     * 获取预测概览统计
     */
    Map<String, Object> getForecastOverview(Map<String, Object> params);

    /**
     * 获取趋势图数据
     */
    Map<String, Object> getForecastTrend(Map<String, Object> params);

    /**
     * 更新预测设置
     */
    int updateForecastSettings(Map<String, Object> settings, com.hbfk.entity.TblStaffUtil loginStaff);
}
