package com.management.accountant.service;

import java.util.Map;

/**
 * 预算穿透查询Service接口
 *
 * @description 预算穿透查询业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetDrillDownService {

    /**
     * 执行穿透查询
     *
     * @param params 查询参数
     * @return 查询结果
     */
    Map<String, Object> executeDrillDown(Map<String, Object> params);

    /**
     * 获取穿透路径
     *
     * @param params 查询参数
     * @return 路径数据
     */
    Map<String, Object> getDrillDownPath(Map<String, Object> params);

    /**
     * 获取明细数据
     *
     * @param params 查询参数
     * @return 明细数据
     */
    Map<String, Object> getDetailData(Map<String, Object> params);

    /**
     * 导出穿透数据
     *
     * @param params 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportDrillDownData(Map<String, Object> params);

    /**
     * 获取穿透查询列表
     *
     * @param params 查询参数
     * @return 查询列表
     */
    Map<String, Object> getDrillDownList(Map<String, Object> params);

    /**
     * 获取穿透查询统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getDrillDownStats(Map<String, Object> params);

    /**
     * 获取可用维度列表
     *
     * @param params 查询参数
     * @return 维度列表
     */
    Map<String, Object> getAvailableDimensions(Map<String, Object> params);

    /**
     * 创建穿透查询
     *
     * @param params 查询参数
     * @return 创建结果
     */
    Map<String, Object> createDrillDown(Map<String, Object> params);

    /**
     * 更新穿透查询
     *
     * @param params 更新参数
     */
    void updateDrillDown(Map<String, Object> params);

    /**
     * 删除穿透查询
     *
     * @param queryId 查询ID
     */
    void deleteDrillDown(String queryId);

    /**
     * 复制穿透查询
     *
     * @param queryId 查询ID
     * @return 复制结果
     */
    Map<String, Object> copyDrillDown(String queryId);

    /**
     * 执行穿透查询（按ID）
     *
     * @param queryId 查询ID
     * @return 执行结果
     */
    Map<String, Object> runDrillDown(String queryId);

    /**
     * 停止穿透查询
     *
     * @param queryId 查询ID
     */
    void stopDrillDown(String queryId);

    /**
     * 优化穿透查询
     *
     * @param queryId 查询ID
     * @return 优化结果
     */
    Map<String, Object> optimizeDrillDown(String queryId);

    /**
     * 导出穿透查询结果（按ID）
     *
     * @param queryId 查询ID
     * @return 导出结果
     */
    Map<String, Object> exportDrillDownById(String queryId);

    /**
     * 获取穿透查询结果
     *
     * @param queryId 查询ID
     * @return 查询结果
     */
    Map<String, Object> getDrillDownResult(String queryId);

    /**
     * 获取穿透查询日志
     *
     * @param queryId 查询ID
     * @return 日志列表
     */
    Map<String, Object> getDrillDownLogs(String queryId);
}

