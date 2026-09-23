package com.financial.sharing.oracle.service;

import com.financial.sharing.oracle.dto.SqlPerformanceReportDTO;
import com.financial.sharing.oracle.dto.QueryOptimizationDTO;
import com.hbfk.util.JsonBean;

import java.util.List;
import java.util.Map;

/**
 * SQL性能优化服务
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface SqlOptimizationService {

    /**
     * 分析慢查询
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param minExecuteTime 最小执行时间(毫秒)
     * @return 慢查询列表
     */
    JsonBean analyzeSlowQueries(String startDate, String endDate, Long minExecuteTime);

    /**
     * 获取表空间使用情况
     *
     * @return 表空间统计
     */
    JsonBean getTablespaceUsage();

    /**
     * 分析索引使用情况
     *
     * @param tableName 表名
     * @return 索引使用统计
     */
    JsonBean analyzeIndexUsage(String tableName);

    /**
     * 获取SQL执行计划
     *
     * @param sql SQL语句
     * @return 执行计划
     */
    JsonBean getExecutionPlan(String sql);

    /**
     * 生成性能优化报告
     *
     * @param reportType 报告类型(daily/weekly/monthly)
     * @return 性能报告
     */
    JsonBean generatePerformanceReport(String reportType);

    /**
     * 获取数据库等待事件
     *
     * @return 等待事件统计
     */
    JsonBean getWaitEvents();

    /**
     * 分析表碎片
     *
     * @param schema 模式名
     * @return 碎片分析结果
     */
    JsonBean analyzeTableFragmentation(String schema);

    /**
     * 生成索引建议
     *
     * @param tableName 表名
     * @return 索引优化建议
     */
    JsonBean generateIndexRecommendations(String tableName);

    /**
     * 监控长时间运行的查询
     *
     * @param threshold 阈值(秒)
     * @return 长时间运行查询列表
     */
    JsonBean monitorLongRunningQueries(Long threshold);

    /**
     * 批量优化查询
     *
     * @param optimizations 优化配置列表
     * @return 优化结果
     */
    JsonBean batchOptimizeQueries(List<QueryOptimizationDTO> optimizations);

    /**
     * 获取表统计信息
     *
     * @param tableName 表名
     * @return 统计信息
     */
    JsonBean getTableStatistics(String tableName);

    /**
     * 更新表统计信息
     *
     * @param tableName 表名
     * @param estimatePercent 估算百分比
     * @return 执行结果
     */
    JsonBean updateTableStatistics(String tableName, Integer estimatePercent);

    /**
     * 检查缺失的索引
     *
     * @param schema 模式名
     * @return 缺失索引建议
     */
    JsonBean checkMissingIndexes(String schema);

    /**
     * 获取SQL执行历史
     *
     * @param sqlId SQL ID
     * @param days 天数
     * @return 执行历史
     */
    JsonBean getSqlExecutionHistory(String sqlId, Integer days);

    /**
     * 生成性能监控看板数据
     *
     * @return 监控数据
     */
    JsonBean generatePerformanceDashboard();
}