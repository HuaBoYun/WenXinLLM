package com.financial.sharing.vo.result;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 余额刷新结果
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class BalanceRefreshResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 刷新类型
     */
    private String refreshType;

    /**
     * 刷新状态（RUNNING, COMPLETED, FAILED, CANCELLED）
     */
    private String status;

    /**
     * 刷新开始时间
     */
    private Date startTime;

    /**
     * 刷新结束时间
     */
    private Date endTime;

    /**
     * 刷新耗时（毫秒）
     */
    private Long duration;

    /**
     * 总科目数
     */
    private Integer totalSubjects;

    /**
     * 已刷新科目数
     */
    private Integer refreshedSubjects;

    /**
     * 成功刷新科目数
     */
    private Integer successCount;

    /**
     * 失败科目数
     */
    private Integer failureCount;

    /**
     * 跳过科目数
     */
    private Integer skippedCount;

    /**
     * 刷新进度（0-100）
     */
    private Integer progress;

    /**
     * 当前处理科目
     */
    private String currentSubject;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 失败科目详情
     */
    private List<FailedSubject> failedSubjects;

    /**
     * 警告信息
     */
    private List<String> warnings;

    /**
     * 刷新统计
     */
    private RefreshStatistics statistics;

    /**
     * 备份信息
     */
    private BackupInfo backupInfo;

    /**
     * 性能指标
     */
    private PerformanceMetrics performance;

    /**
     * 失败科目内部类
     */
    @Data
    public static class FailedSubject {
        /**
         * 科目ID
         */
        private Long subjectId;

        /**
         * 科目编码
         */
        private String subjectCode;

        /**
         * 科目名称
         */
        private String subjectName;

        /**
         * 错误原因
         */
        private String errorReason;

        /**
         * 错误详情
         */
        private String errorDetail;

        /**
         * 重试次数
         */
        private Integer retryCount;

        /**
         * 是否已修复
         */
        private Boolean isFixed;

        /**
         * 修复方案
         */
        private String fixSolution;
    }

    /**
     * 刷新统计内部类
     */
    @Data
    public static class RefreshStatistics {
        /**
         * 刷新的凭证数量
         */
        private Long voucherCount;

        /**
         * 刷新的分录数量
         */
        private Long entryCount;

        /**
         * 余额变更的科目数
         */
        private Integer balanceChangedSubjects;

        /**
         * 余额未变更的科目数
         */
        private Integer balanceUnchangedSubjects;

        /**
         * 新增的科目余额数
         */
        private Integer newBalances;

        /**
         * 更新的科目余额数
         */
        private Integer updatedBalances;

        /**
         * 删除的科目余额数
         */
        private Integer deletedBalances;

        /**
         * 最大余额变动金额
         */
        private BigDecimal maxBalanceChange;

        /**
         * 最小余额变动金额
         */
        private BigDecimal minBalanceChange;

        /**
         * 平均余额变动金额
         */
        private BigDecimal avgBalanceChange;

        /**
         * 按科目类型统计
         */
        private Map<Integer, Long> subjectTypeStats;

        /**
         * 按余额方向统计
         */
        private Map<Integer, Long> balanceDirectionStats;
    }

    /**
     * 备份信息内部类
     */
    @Data
    public static class BackupInfo {
        /**
         * 是否已备份
         */
        private Boolean isBackedUp;

        /**
         * 备份时间
         */
        private Date backupTime;

        /**
         * 备份文件路径
         */
        private String backupFilePath;

        /**
         * 备份文件大小
         */
        private Long backupFileSize;

        /**
         * 备份记录数
         */
        private Integer backupRecordCount;

        /**
         * 备份压缩率
         */
        private Double compressionRatio;

        /**
         * 备份校验码
         */
        private String checksum;
    }

    /**
     * 性能指标内部类
     */
    @Data
    public static class PerformanceMetrics {
        /**
         * 平均处理速度（科目/秒）
         */
        private Double avgSpeed;

        /**
         * 峰值处理速度（科目/秒）
         */
        private Double peakSpeed;

        /**
         * CPU使用率（%）
         */
        private Double cpuUsage;

        /**
         * 内存使用量（MB）
         */
        private Double memoryUsage;

        /**
         * 数据库查询次数
         */
        private Long dbQueryCount;

        /**
         * 数据库查询耗时（毫秒）
        */
        private Long dbQueryTime;

        /**
         * 缓存命中率（%）
         */
        private Double cacheHitRate;

        /**
         * 批处理次数
         */
        private Integer batchCount;

        /**
         * 平均批处理大小
         */
        private Integer avgBatchSize;
    }
}