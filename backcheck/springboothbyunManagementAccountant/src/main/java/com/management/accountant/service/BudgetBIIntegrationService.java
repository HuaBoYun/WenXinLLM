package com.management.accountant.service;

import java.util.Map;

/**
 * 预算BI集成Service接口
 * 
 * @description 预算BI集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetBIIntegrationService {

    /**
     * 同步数据到BI
     * 
     * @param params 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncToBI(Map<String, Object> params);

    /**
     * 获取BI报表列表
     * 
     * @return 报表列表
     */
    Map<String, Object> getBIReports();

    /**
     * 获取BI报表详情
     * 
     * @param reportId 报表ID
     * @return 报表详情
     */
    Map<String, Object> getBIReportDetail(String reportId);

    /**
     * 刷新BI数据
     * 
     * @param params 刷新参数
     * @return 刷新结果
     */
    Map<String, Object> refreshBIData(Map<String, Object> params);

    /**
     * 配置BI连接
     * 
     * @param params 配置参数
     */
    void configureBIConnection(Map<String, Object> params);

    /**
     * 测试BI连接
     * 
     * @param params 测试参数
     * @return 测试结果
     */
    Map<String, Object> testBIConnection(Map<String, Object> params);

    /**
     * 获取同步状态
     *
     * @param taskId 任务ID
     * @return 同步状态
     */
    Map<String, Object> getSyncStatus(String taskId);

    /**
     * 删除BI配置
     *
     * @param configId 配置ID
     */
    void deleteConfig(String configId);
}

