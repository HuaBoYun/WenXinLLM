package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetMessageQueueIntegration;

import java.util.List;

/**
 * 预算消息队列集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetMessageQueueIntegrationService extends IService<BudgetMessageQueueIntegration> {

    /**
     * 根据消息队列类型查询集成列表
     * 
     * @param mqType 消息队列类型
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMqType(String mqType);

    /**
     * 根据消息模式查询集成列表
     * 
     * @param messageMode 消息模式
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMessageMode(String messageMode);

    /**
     * 根据消息格式查询集成列表
     * 
     * @param messageFormat 消息格式
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMessageFormat(String messageFormat);

    /**
     * 查询启用的消息队列集成列表
     * 
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listEnabled();

    /**
     * 发送消息
     * 
     * @param mqId 消息队列集成ID
     * @param message 消息内容
     * @return 发送结果
     */
    boolean sendMessage(String mqId, String message);

    /**
     * 接收消息
     * 
     * @param mqId 消息队列集成ID
     * @return 消息内容
     */
    String receiveMessage(String mqId);

    /**
     * 测试消息队列连接
     * 
     * @param mqId 消息队列集成ID
     * @return 测试结果
     */
    boolean testConnection(String mqId);

    /**
     * 更新消息统计
     * 
     * @param mqId 消息队列集成ID
     * @param sendSuccess 发送成功数
     * @param receiveSuccess 接收成功数
     * @return 是否更新成功
     */
    boolean updateMessageStatistics(String mqId, Integer sendSuccess, Integer receiveSuccess);

    /**
     * 批量启用/禁用消息队列集成
     * 
     * @param mqIds 消息队列集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> mqIds, boolean enabled);
}

