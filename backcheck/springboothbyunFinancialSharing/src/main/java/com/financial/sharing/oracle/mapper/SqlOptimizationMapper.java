package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * SQL优化Mapper接口
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface SqlOptimizationMapper {

    /**
     * 查询慢查询
     */
    List<Map<String, Object>> selectSlowQueries(@Param("param") Map<String, Object> params);

    /**
     * 查询表空间使用情况
     */
    List<Map<String, Object>> selectTablespaceUsage();

    /**
     * 查询索引使用情况
     */
    List<Map<String, Object>> selectIndexUsage(@Param("param") Map<String, Object> params);

    /**
     * 查询未使用的索引
     */
    List<Map<String, Object>> selectUnusedIndexes(@Param("param") Map<String, Object> params);

    /**
     * 查询执行计划
     */
    List<Map<String, Object>> selectExecutionPlan(@Param("param") Map<String, Object> params);

    /**
     * 查询性能指标
     */
    Map<String, Object> selectPerformanceMetrics();

    /**
     * 查询TOP慢查询
     */
    List<Map<String, Object>> selectTopSlowQueries(@Param("limit") Integer limit);

    /**
     * 查询资源使用情况
     */
    Map<String, Object> selectResourceUsage();

    /**
     * 查询TOP等待事件
     */
    List<Map<String, Object>> selectTopWaitEvents(@Param("limit") Integer limit);

    /**
     * 查询等待事件
     */
    List<Map<String, Object>> selectWaitEvents();

    /**
     * 查询表碎片
     */
    List<Map<String, Object>> selectTableFragmentation(@Param("param") Map<String, Object> params);

    /**
     * 查询表的列信息
     */
    List<Map<String, Object>> selectTableColumns(@Param("tableName") String tableName);

    /**
     * 查询频繁查询的列
     */
    List<Map<String, Object>> selectFrequentQueryColumns(@Param("tableName") String tableName);

    /**
     * 查询表的索引
     */
    List<Map<String, Object>> selectTableIndexes(@Param("tableName") String tableName);

    /**
     * 查询长时间运行的查询
     */
    List<Map<String, Object>> selectLongRunningQueries(@Param("param") Map<String, Object> params);

    /**
     * 查询表统计信息
     */
    Map<String, Object> selectTableStatistics(@Param("tableName") String tableName);

    /**
     * 更新表统计信息
     */
    void updateTableStatistics(@Param("param") Map<String, Object> params);

    /**
     * 查询缺失的索引
     */
    List<Map<String, Object>> selectMissingIndexes(@Param("schema") String schema);

    /**
     * 查询SQL执行历史
     */
    List<Map<String, Object>> selectSqlExecutionHistory(@Param("param") Map<String, Object> params);

    /**
     * 查询活跃会话
     */
    List<Map<String, Object>> selectActiveSessions();

    /**
     * 查询缓存命中率
     */
    Map<String, Object> selectCacheHitRatio();

    /**
     * 执行SQL
     */
    void executeSql(@Param("sql") String sql);
}