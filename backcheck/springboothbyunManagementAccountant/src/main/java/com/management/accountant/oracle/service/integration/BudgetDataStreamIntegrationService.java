package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetDataStreamIntegration;

import java.util.List;

/**
 * 预算数据流集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetDataStreamIntegrationService extends IService<BudgetDataStreamIntegration> {

    /**
     * 根据数据流类型查询集成列表
     * 
     * @param streamType 数据流类型
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listByStreamType(String streamType);

    /**
     * 根据数据源类型查询集成列表
     * 
     * @param sourceType 数据源类型
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listBySourceType(String sourceType);

    /**
     * 根据处理模式查询集成列表
     * 
     * @param processMode 处理模式
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listByProcessMode(String processMode);

    /**
     * 查询启用的数据流集成列表
     * 
     * @return 集成列表
     */
    List<BudgetDataStreamIntegration> listEnabled();

    /**
     * 启动数据流
     * 
     * @param streamId 数据流集成ID
     * @return 启动结果
     */
    boolean startStream(String streamId);

    /**
     * 停止数据流
     * 
     * @param streamId 数据流集成ID
     * @return 停止结果
     */
    boolean stopStream(String streamId);

    /**
     * 重启数据流
     * 
     * @param streamId 数据流集成ID
     * @return 重启结果
     */
    boolean restartStream(String streamId);

    /**
     * 更新处理统计
     * 
     * @param streamId 数据流集成ID
     * @param totalRecords 总记录数
     * @param successRecords 成功记录数
     * @param failureRecords 失败记录数
     * @return 是否更新成功
     */
    boolean updateProcessStatistics(String streamId, Long totalRecords, Long successRecords, Long failureRecords);

    /**
     * 批量启用/禁用数据流集成
     * 
     * @param streamIds 数据流集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> streamIds, boolean enabled);
}

