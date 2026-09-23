package com.management.accountant.service;

import java.util.Map;

/**
 * 预算API集成Service接口
 * 
 * @description 预算API集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationApiService {

    /**
     * 注册API
     * 
     * @param params 注册参数
     * @return API信息
     */
    Map<String, Object> registerApi(Map<String, Object> params);

    /**
     * 调用API
     * 
     * @param params 调用参数
     * @return 调用结果
     */
    Map<String, Object> invokeApi(Map<String, Object> params);

    /**
     * API认证
     * 
     * @param params 认证参数
     * @return 认证结果
     */
    Map<String, Object> authenticateApi(Map<String, Object> params);

    /**
     * API监控
     * 
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorApi(Map<String, Object> params);

    /**
     * API文档生成
     * 
     * @param params 文档参数
     * @return 文档信息
     */
    Map<String, Object> generateDocumentation(Map<String, Object> params);

    /**
     * API版本管理
     * 
     * @param params 版本参数
     * @return 版本信息
     */
    Map<String, Object> manageVersion(Map<String, Object> params);

    /**
     * API限流控制
     *
     * @param params 限流参数
     * @return 限流信息
     */
    Map<String, Object> rateLimit(Map<String, Object> params);

    /**
     * 获取配置信息
     *
     * @param configId 配置ID
     * @return 配置信息
     */
    Map<String, Object> getConfig(String configId);

    /**
     * 删除配置
     *
     * @param configId 配置ID
     */
    void deleteConfig(String configId);

    /**
     * 获取配置分页列表
     *
     * @param params 查询参数
     * @return 分页数据
     */
    Map<String, Object> getConfigPage(Map<String, Object> params);

    /**
     * 测试连接
     *
     * @param configId 配置ID
     * @return 测试结果
     */
    Map<String, Object> testConnection(String configId);
}

