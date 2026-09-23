package com.management.accountant.service;

import java.util.Map;

/**
 * 预算消息队列集成Service接口
 * 
 * @description 预算消息队列集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationMessageQueueService {

    /**
     * 配置消息队列
     * 
     * @param params 配置参数
     * @return 配置信息
     */
    Map<String, Object> configMessageQueue(Map<String, Object> params);

    /**
     * 发送消息
     * 
     * @param params 消息参数
     * @return 发送结果
     */
    Map<String, Object> sendMessage(Map<String, Object> params);

    /**
     * 接收消息
     * 
     * @param params 接收参数
     * @return 接收结果
     */
    Map<String, Object> receiveMessage(Map<String, Object> params);

    /**
     * 订阅主题
     * 
     * @param params 订阅参数
     * @return 订阅信息
     */
    Map<String, Object> subscribeTopic(Map<String, Object> params);

    /**
     * 消息确认
     * 
     * @param params 确认参数
     * @return 确认结果
     */
    Map<String, Object> acknowledgeMessage(Map<String, Object> params);

    /**
     * 死信队列处理
     * 
     * @param params 处理参数
     * @return 处理结果
     */
    Map<String, Object> handleDeadLetter(Map<String, Object> params);

    /**
     * 消息监控
     * 
     * @param params 监控参数
     * @return 监控信息
     */
    Map<String, Object> monitorMessages(Map<String, Object> params);
}

