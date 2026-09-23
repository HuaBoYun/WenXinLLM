package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetDatabaseIntegration;

import java.util.List;

/**
 * 预算数据库集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetDatabaseIntegrationService extends IService<BudgetDatabaseIntegration> {

    /**
     * 根据数据库类型查询集成列表
     * 
     * @param dbType 数据库类型
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listByDbType(String dbType);

    /**
     * 根据同步模式查询集成列表
     * 
     * @param syncMode 同步模式
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listBySyncMode(String syncMode);

    /**
     * 根据同步频率查询集成列表
     * 
     * @param syncFrequency 同步频率
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listBySyncFrequency(String syncFrequency);

    /**
     * 查询启用的数据库集成列表
     * 
     * @return 集成列表
     */
    List<BudgetDatabaseIntegration> listEnabled();

    /**
     * 执行数据库同步
     * 
     * @param dbId 数据库集成ID
     * @return 同步结果
     */
    boolean executeSync(String dbId);

    /**
     * 测试数据库连接
     * 
     * @param dbId 数据库集成ID
     * @return 测试结果
     */
    boolean testConnection(String dbId);

    /**
     * 更新同步统计
     * 
     * @param dbId 数据库集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 是否更新成功
     */
    boolean updateSyncStatistics(String dbId, boolean success, Integer recordCount);

    /**
     * 批量启用/禁用数据库集成
     * 
     * @param dbIds 数据库集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> dbIds, boolean enabled);
}

