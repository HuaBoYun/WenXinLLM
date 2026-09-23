package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetMessageQueueIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算消息队列集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetMessageQueueIntegrationMapper extends BaseMapper<BudgetMessageQueueIntegration> {

    /**
     * 根据消息队列类型查询集成列表
     * 
     * @param mqType 消息队列类型
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMqType(@Param("mqType") String mqType);

    /**
     * 根据消息模式查询集成列表
     * 
     * @param messageMode 消息模式
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMessageMode(@Param("messageMode") String messageMode);

    /**
     * 根据消息格式查询集成列表
     * 
     * @param messageFormat 消息格式
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listByMessageFormat(@Param("messageFormat") String messageFormat);

    /**
     * 查询启用的消息队列集成列表
     * 
     * @return 集成列表
     */
    List<BudgetMessageQueueIntegration> listEnabled();

    /**
     * 更新消息统计
     * 
     * @param mqId 消息队列集成ID
     * @param sendSuccess 发送成功数
     * @param receiveSuccess 接收成功数
     * @return 更新记录数
     */
    int updateMessageStatistics(@Param("mqId") String mqId, 
                               @Param("sendSuccess") Integer sendSuccess, 
                               @Param("receiveSuccess") Integer receiveSuccess);
}

