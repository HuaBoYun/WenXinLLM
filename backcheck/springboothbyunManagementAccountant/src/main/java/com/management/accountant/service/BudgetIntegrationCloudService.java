package com.management.accountant.service;

import java.util.Map;

/**
 * 预算云平台集成Service接口
 * 
 * @description 预算云平台集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationCloudService {

    /**
     * 配置云平台
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> configCloud(Map<String, Object> params);

    /**
     * 对象存储操作
     * 
     * @param params 操作参数
     * @return 操作结果
     */
    Map<String, Object> operateStorage(Map<String, Object> params);

    /**
     * 云数据库操作
     * 
     * @param params 操作参数
     * @return 操作结果
     */
    Map<String, Object> operateDatabase(Map<String, Object> params);

    /**
     * 云函数调用
     * 
     * @param params 调用参数
     * @return 调用结果
     */
    Map<String, Object> invokeFunction(Map<String, Object> params);

    /**
     * 云监控
     * 
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorCloud(Map<String, Object> params);

    /**
     * 云日志查询
     * 
     * @param params 查询参数
     * @return 日志信息
     */
    Map<String, Object> queryLog(Map<String, Object> params);

    /**
     * 云资源管理
     * 
     * @param params 管理参数
     * @return 资源信息
     */
    Map<String, Object> manageResource(Map<String, Object> params);
}

