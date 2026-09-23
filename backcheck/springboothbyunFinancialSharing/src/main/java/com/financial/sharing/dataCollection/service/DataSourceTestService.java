package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.util.MyJsonBean;

/**
 * 数据源连接测试Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface DataSourceTestService {

    /**
     * 测试数据库连接（增强版）
     *
     * @param dataSource 数据源配置
     * @return 测试结果
     */
    MyJsonBean testDatabaseConnectionEnhanced(TblDataSource dataSource);

    /**
     * 测试API连接（增强版）
     *
     * @param dataSource 数据源配置
     * @return 测试结果
     */
    MyJsonBean testApiConnectionEnhanced(TblDataSource dataSource);

    /**
     * 测试文件连接（增强版）
     *
     * @param dataSource 数据源配置
     * @return 测试结果
     */
    MyJsonBean testFileConnectionEnhanced(TblDataSource dataSource);

    /**
     * 测试财务共享连接（增强版）
     *
     * @param dataSource 数据源配置
     * @return 测试结果
     */
    MyJsonBean testFinancialSharingConnectionEnhanced(TblDataSource dataSource);

    /**
     * 批量测试数据源连接
     *
     * @param sourceIds 数据源ID列表
     * @param orgId 组织ID
     * @return 测试结果
     */
    MyJsonBean batchTestConnection(String[] sourceIds, String orgId);

    /**
     * 获取数据库表列表
     *
     * @param sourceId 数据源ID
     * @param orgId 组织ID
     * @return 表列表
     */
    MyJsonBean getDatabaseTables(String sourceId, String orgId);

    /**
     * 获取数据库表结构
     *
     * @param sourceId 数据源ID
     * @param tableName 表名
     * @param orgId 组织ID
     * @return 表结构
     */
    MyJsonBean getTableStructure(String sourceId, String tableName, String orgId);

    /**
     * 执行测试查询
     *
     * @param sourceId 数据源ID
     * @param sql 测试SQL
     * @param orgId 组织ID
     * @return 查询结果
     */
    MyJsonBean executeTestQuery(String sourceId, String sql, String orgId);
}

