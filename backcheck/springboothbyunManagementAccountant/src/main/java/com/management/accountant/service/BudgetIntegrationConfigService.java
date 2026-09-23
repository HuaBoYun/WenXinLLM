package com.management.accountant.service;

import java.util.Map;

/**
 * 预算集成配置Service接口
 * 
 * @description 预算集成配置业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationConfigService {

    /**
     * 创建集成配置
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> createConfig(Map<String, Object> params);

    /**
     * 更新集成配置
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> updateConfig(Map<String, Object> params);

    /**
     * 删除集成配置
     * 
     * @param params 删除参数
     * @return 删除结果
     */
    Map<String, Object> deleteConfig(Map<String, Object> params);

    /**
     * 查询集成配置
     * 
     * @param params 查询参数
     * @return 配置信息
     */
    Map<String, Object> queryConfig(Map<String, Object> params);

    /**
     * 测试集成配置
     * 
     * @param params 测试参数
     * @return 测试结果
     */
    Map<String, Object> testConfig(Map<String, Object> params);

    /**
     * 字段映射配置
     * 
     * @param params 映射参数
     * @return 映射信息
     */
    Map<String, Object> configMapping(Map<String, Object> params);

    /**
     * 调度配置
     * 
     * @param params 调度参数
     * @return 调度信息
     */
    Map<String, Object> configSchedule(Map<String, Object> params);
}

