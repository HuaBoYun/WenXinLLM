package com.financial.sharing.oracle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出任务统计DTO
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Data
public class ExportTaskStatisticsDTO {

    /**
     * 统计时间范围
     */
    private String timeRange;

    /**
     * 统计开始时间
     */
    private Date startTime;

    /**
     * 统计结束时间
     */
    private Date endTime;

    /**
     * 总任务数
     */
    private Long totalTasks;

    /**
     * 完成任务数
     */
    private Long completedTasks;

    /**
     * 失败任务数
     */
    private Long failedTasks;

    /**
     * 运行中任务数
     */
    private Long runningTasks;

    /**
     * 取消任务数
     */
    private Long cancelledTasks;

    /**
     * 成功率
     */
    private BigDecimal successRate;

    /**
     * 失败率
     */
    private BigDecimal failureRate;

    /**
     * 平均执行时间(秒)
     */
    private BigDecimal avgExecutionTime;

    /**
     * 最长执行时间(秒)
     */
    private Long maxExecutionTime;

    /**
     * 最短执行时间(秒)
     */
    private Long minExecutionTime;

    /**
     * 总导出记录数
     */
    private Long totalExportedRecords;

    /**
     * 平均每任务导出记录数
     */
    private BigDecimal avgRecordsPerTask;

    /**
     * 总文件大小(MB)
     */
    private BigDecimal totalFileSize;

    /**
     * 平均文件大小(MB)
     */
    private BigDecimal avgFileSize;

    /**
     * 总下载次数
     */
    private Long totalDownloads;

    /**
     * 平均每任务下载次数
     */
    private BigDecimal avgDownloadsPerTask;

    /**
     * 按任务类型统计
     */
    private List<Map<String, Object>> statisticsByType;

    /**
     * 按导出格式统计
     */
    private List<Map<String, Object>> statisticsByFormat;

    /**
     * 按小时统计的任务数量
     */
    private List<Map<String, Object>> hourlyTaskCount;

    /**
     * 按日期统计的任务数量
     */
    private List<Map<String, Object>> dailyTaskCount;

    /**
     * 错误统计
     */
    private List<Map<String, Object>> errorStatistics;

    /**
     * 性能指标
     */
    private Map<String, Object> performanceMetrics;

    /**
     * 用户统计
     */
    private List<Map<String, Object>> userStatistics;

    /**
     * 峰值时段统计
     */
    private Map<String, Object> peakHours;

    /**
     * 资源使用统计
     */
    private Map<String, Object> resourceUsage;
}