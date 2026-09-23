package com.management.accountant.service;

import java.util.Map;

/**
 * 预算数据库集成Service接口
 * 
 * @description 预算数据库集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationDatabaseService {

    /**
     * 配置数据源
     * 
     * @param params 配置参数
     * @return 数据源信息
     */
    Map<String, Object> configDatasource(Map<String, Object> params);

    /**
     * 测试连接
     * 
     * @param params 连接参数
     * @return 测试结果
     */
    Map<String, Object> testConnection(Map<String, Object> params);

    /**
     * 数据同步
     * 
     * @param params 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncData(Map<String, Object> params);

    /**
     * 数据迁移
     * 
     * @param params 迁移参数
     * @return 迁移结果
     */
    Map<String, Object> migrateData(Map<String, Object> params);

    /**
     * 执行查询
     * 
     * @param params 查询参数
     * @return 查询结果
     */
    Map<String, Object> executeQuery(Map<String, Object> params);

    /**
     * 数据备份
     * 
     * @param params 备份参数
     * @return 备份结果
     */
    Map<String, Object> backupData(Map<String, Object> params);

    /**
     * 数据恢复
     * 
     * @param params 恢复参数
     * @return 恢复结果
     */
    Map<String, Object> restoreData(Map<String, Object> params);
}

