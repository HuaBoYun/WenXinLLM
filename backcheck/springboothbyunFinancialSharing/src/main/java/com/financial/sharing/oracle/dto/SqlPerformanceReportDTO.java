package com.financial.sharing.oracle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SQL性能报告DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class SqlPerformanceReportDTO {

    /**
     * 报告ID
     */
    private String reportId;

    /**
     * 报告类型(daily/weekly/monthly)
     */
    private String reportType;

    /**
     * 报告生成时间
     */
    private Date generateTime;

    /**
     * 报告开始时间
     */
    private Date startTime;

    /**
     * 报告结束时间
     */
    private Date endTime;

    /**
     * 性能指标
     */
    private Map<String, Object> performanceMetrics;

    /**
     * TOP慢查询列表
     */
    private List<Map<String, Object>> topSlowQueries;

    /**
     * 表空间使用情况
     */
    private List<Map<String, Object>> tablespaces;

    /**
     * 等待事件统计
     */
    private List<Map<String, Object>> waitEvents;

    /**
     * 活跃会话数
     */
    private Long activeSessions;

    /**
     * 缓存命中率
     */
    private BigDecimal cacheHitRatio;

    /**
     * 优化建议数量
     */
    private Integer recommendationCount;

    /**
     * 性能评分(0-100)
     */
    private BigDecimal performanceScore;

    /**
     * 关键问题
     */
    private List<String> criticalIssues;

    /**
     * 优化建议
     */
    private List<String> recommendations;
}