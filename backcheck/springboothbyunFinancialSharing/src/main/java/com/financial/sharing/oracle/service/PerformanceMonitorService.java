package com.financial.sharing.oracle.service;

import com.financial.sharing.oracle.dto.PerformanceAlertDTO;
import com.financial.sharing.oracle.dto.PerformanceMetricDTO;
import com.hbfk.util.JsonBean;

import java.util.List;
import java.util.Map;

/**
 * 性能监控服务
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface PerformanceMonitorService {

    /**
     * 获取实时性能指标
     *
     * @return 性能指标列表
     */
    JsonBean getRealTimeMetrics();

    /**
     * 获取性能趋势数据
     *
     * @param metricName 指标名称
     * @param timeRange 时间范围(1h/6h/24h/7d/30d)
     * @return 趋势数据
     */
    JsonBean getPerformanceTrend(String metricName, String timeRange);

    /**
     * 设置性能告警规则
     *
     * @param alert 告警配置
     * @return 设置结果
     */
    JsonBean setPerformanceAlert(PerformanceAlertDTO alert);

    /**
     * 获取告警列表
     *
     * @param status 告警状态
     * @return 告警列表
     */
    JsonBean getAlertList(String status);

    /**
     * 获取系统健康状态
     *
     * @return 健康状态
     */
    JsonBean getSystemHealthStatus();

    /**
     * 获取资源使用情况
     *
     * @return 资源使用统计
     */
    JsonBean getResourceUsage();

    /**
     * 监控SQL执行情况
     *
     * @param sqlPattern SQL模式
     * @return 监控数据
     */
    JsonBean monitorSqlExecution(String sqlPattern);

    /**
     * 获取锁等待情况
     *
     * @return 锁等待统计
     */
    JsonBean getLockWaitStatistics();

    /**
     * 分析并发情况
     *
     * @return 并发分析报告
     */
    JsonBean analyzeConcurrency();

    /**
     * 监控会话活动
     *
     * @return 会话活动统计
     */
    JsonBean monitorSessionActivity();

    /**
     * 获取慢查询趋势
     *
     * @param timeRange 时间范围
     * @return 慢查询趋势
     */
    JsonBean getSlowQueryTrend(String timeRange);

    /**
     * 分析性能瓶颈
     *
     * @return 瓶颈分析报告
     */
    JsonBean analyzePerformanceBottlenecks();

    /**
     * 生成性能优化建议
     *
     * @return 优化建议列表
     */
    JsonBean generateOptimizationRecommendations();

    /**
     * 获取缓存性能
     *
     * @return 缓存性能指标
     */
    JsonBean getCachePerformance();

    /**
     * 监控I/O性能
     *
     * @return I/O性能统计
     */
    JsonBean monitorIOPerformance();

    /**
     * 获取内存使用情况
     *
     * @return 内存使用统计
     */
    JsonBean getMemoryUsage();

    /**
     * 分析PGA使用情况
     *
     * @return PGA使用分析
     */
    JsonBean analyzePGAUsage();
}