package com.management.accountant.service;

import java.util.Map;

/**
 * 预算Web服务集成Service接口
 * 
 * @description 预算Web服务集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationWebServiceService {

    /**
     * 注册Web服务
     * 
     * @param params 注册参数
     * @return 服务信息
     */
    Map<String, Object> registerWebService(Map<String, Object> params);

    /**
     * 调用Web服务
     * 
     * @param params 调用参数
     * @return 调用结果
     */
    Map<String, Object> invokeWebService(Map<String, Object> params);

    /**
     * WSDL解析
     * 
     * @param params 解析参数
     * @return 解析结果
     */
    Map<String, Object> parseWsdl(Map<String, Object> params);

    /**
     * 服务发现
     * 
     * @param params 发现参数
     * @return 发现结果
     */
    Map<String, Object> discoverService(Map<String, Object> params);

    /**
     * 服务健康检查
     * 
     * @param params 检查参数
     * @return 健康状态
     */
    Map<String, Object> checkHealth(Map<String, Object> params);

    /**
     * 负载均衡配置
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> configLoadBalance(Map<String, Object> params);

    /**
     * 服务熔断配置
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> configCircuitBreaker(Map<String, Object> params);
}

