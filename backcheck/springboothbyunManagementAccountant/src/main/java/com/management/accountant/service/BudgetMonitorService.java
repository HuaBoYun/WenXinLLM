package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetMonitor;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算监控Service接口
 * 
 * @description 预算监控业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetMonitorService {

    /**
     * 创建监控
     * 
     * @param monitor 监控对象
     * @return 创建后的监控对象
     */
    BudgetMonitor create(BudgetMonitor monitor);

    /**
     * 根据ID查询监控
     * 
     * @param monitorId 监控ID
     * @return 监控对象
     */
    BudgetMonitor getById(String monitorId);

    /**
     * 更新监控
     * 
     * @param monitor 监控对象
     */
    void update(BudgetMonitor monitor);

    /**
     * 删除监控
     * 
     * @param monitorId 监控ID
     */
    void delete(String monitorId);

    /**
     * 分页查询监控列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetMonitor> getPage(Map<String, Object> params);

    /**
     * 启动监控
     * 
     * @param monitorId 监控ID
     */
    void start(String monitorId);

    /**
     * 停止监控
     * 
     * @param monitorId 监控ID
     */
    void stop(String monitorId);

    /**
     * 刷新监控数据
     * 
     * @param monitorId 监控ID
     * @return 刷新后的监控对象
     */
    BudgetMonitor refresh(String monitorId);

    /**
     * 获取监控统计
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取预警列表
     *
     * @return 预警监控列表
     */
    List<BudgetMonitor> getAlerts();

    /**
     * 批量启动监控
     *
     * @param params 批量启动参数
     * @return 批量启动结果
     */
    Map<String, Object> batchStart(Map<String, Object> params);

    /**
     * 批量停止监控
     *
     * @param params 批量停止参数
     * @return 批量停止结果
     */
    Map<String, Object> batchStop(Map<String, Object> params);

    /**
     * 导出监控数据
     *
     * @param params 查询参数
     * @return 监控数据列表
     */
    List<BudgetMonitor> exportData(Map<String, Object> params);
}

