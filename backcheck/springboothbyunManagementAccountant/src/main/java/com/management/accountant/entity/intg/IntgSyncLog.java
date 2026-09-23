package com.management.accountant.entity.intg;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 同步日志实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_sync_log")
public class IntgSyncLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "log_id", type = IdType.ASSIGN_UUID)
    private String logId;

    /**
     * 日志编码
     */
    @TableField("log_code")
    private String logCode;

    /**
     * 系统配置ID
     */
    @TableField("config_id")
    private String configId;

    /**
     * 映射配置ID
     */
    @TableField("mapping_id")
    private String mappingId;

    /**
     * 同步任务ID
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 同步批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 同步类型 (FULL, INCREMENTAL, DELTA, REAL_TIME)
     */
    @TableField("sync_type")
    private String syncType;

    /**
     * 同步方向 (IMPORT, EXPORT, BIDIRECTIONAL)
     */
    @TableField("sync_direction")
    private String syncDirection;

    /**
     * 同步模式 (MANUAL, SCHEDULED, EVENT_DRIVEN, API_TRIGGERED)
     */
    @TableField("sync_mode")
    private String syncMode;

    /**
     * 触发方式 (MANUAL, CRON, EVENT, API)
     */
    @TableField("trigger_type")
    private String triggerType;

    /**
     * 触发用户
     */
    @TableField("trigger_user")
    private String triggerUser;

    /**
     * 触发时间
     */
    @TableField("trigger_time")
    private LocalDateTime triggerTime;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 执行时长 (毫秒)
     */
    @TableField("execution_duration")
    private Long executionDuration;

    /**
     * 同步状态 (PENDING, RUNNING, SUCCESS, FAILED, CANCELLED, TIMEOUT)
     */
    @TableField("sync_status")
    private String syncStatus;

    /**
     * 执行结果 (SUCCESS, PARTIAL_SUCCESS, FAILED, ERROR)
     */
    @TableField("execution_result")
    private String executionResult;

    /**
     * 源系统名称
     */
    @TableField("source_system")
    private String sourceSystem;

    /**
     * 目标系统名称
     */
    @TableField("target_system")
    private String targetSystem;

    /**
     * 源表/接口名称
     */
    @TableField("source_table")
    private String sourceTable;

    /**
     * 目标表/接口名称
     */
    @TableField("target_table")
    private String targetTable;

    /**
     * 总记录数
     */
    @TableField("total_records")
    private Long totalRecords;

    /**
     * 处理记录数
     */
    @TableField("processed_records")
    private Long processedRecords;

    /**
     * 成功记录数
     */
    @TableField("success_records")
    private Long successRecords;

    /**
     * 失败记录数
     */
    @TableField("failed_records")
    private Long failedRecords;

    /**
     * 跳过记录数
     */
    @TableField("skipped_records")
    private Long skippedRecords;

    /**
     * 插入记录数
     */
    @TableField("inserted_records")
    private Long insertedRecords;

    /**
     * 更新记录数
     */
    @TableField("updated_records")
    private Long updatedRecords;

    /**
     * 删除记录数
     */
    @TableField("deleted_records")
    private Long deletedRecords;

    /**
     * 数据传输量 (字节)
     */
    @TableField("data_transfer_size")
    private Long dataTransferSize;

    /**
     * 平均处理速度 (记录/秒)
     */
    @TableField("avg_processing_speed")
    private Double avgProcessingSpeed;

    /**
     * 峰值处理速度 (记录/秒)
     */
    @TableField("peak_processing_speed")
    private Double peakProcessingSpeed;

    /**
     * CPU使用率 (%)
     */
    @TableField("cpu_usage")
    private Double cpuUsage;

    /**
     * 内存使用量 (MB)
     */
    @TableField("memory_usage")
    private Double memoryUsage;

    /**
     * 网络传输速度 (KB/s)
     */
    @TableField("network_speed")
    private Double networkSpeed;

    /**
     * 磁盘IO速度 (KB/s)
     */
    @TableField("disk_io_speed")
    private Double diskIoSpeed;

    /**
     * 错误代码
     */
    @TableField("error_code")
    private String errorCode;

    /**
     * 错误消息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 错误堆栈
     */
    @TableField("error_stack")
    private String errorStack;

    /**
     * 错误详情 (JSON格式)
     */
    @TableField("error_details")
    private String errorDetails;

    /**
     * 警告信息
     */
    @TableField("warning_messages")
    private String warningMessages;

    /**
     * 执行日志
     */
    @TableField("execution_log")
    private String executionLog;

    /**
     * 调试信息
     */
    @TableField("debug_info")
    private String debugInfo;

    /**
     * 性能指标 (JSON格式)
     */
    @TableField("performance_metrics")
    private String performanceMetrics;

    /**
     * 质量指标 (JSON格式)
     */
    @TableField("quality_metrics")
    private String qualityMetrics;

    /**
     * 业务指标 (JSON格式)
     */
    @TableField("business_metrics")
    private String businessMetrics;

    /**
     * 同步参数 (JSON格式)
     */
    @TableField("sync_parameters")
    private String syncParameters;

    /**
     * 同步配置快照 (JSON格式)
     */
    @TableField("config_snapshot")
    private String configSnapshot;

    /**
     * 数据样例 (JSON格式)
     */
    @TableField("data_sample")
    private String dataSample;

    /**
     * 检查点信息
     */
    @TableField("checkpoint_info")
    private String checkpointInfo;

    /**
     * 恢复信息
     */
    @TableField("recovery_info")
    private String recoveryInfo;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    @TableField("max_retry_count")
    private Integer maxRetryCount;

    /**
     * 下次重试时间
     */
    @TableField("next_retry_time")
    private LocalDateTime nextRetryTime;

    /**
     * 是否需要人工干预
     */
    @TableField("requires_manual_intervention")
    private Boolean requiresManualInterven;

    /**
     * 人工干预说明
     */
    @TableField("manual_intervention_notes")
    private String manualInterventionNotes;

    /**
     * 日志级别 (DEBUG, INFO, WARN, ERROR, FATAL)
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 日志分类
     */
    @TableField("log_category")
    private String logCategory;

    /**
     * 日志标签
     */
    @TableField("log_tags")
    private String logTags;

    /**
     * 环境信息
     */
    @TableField("environment_info")
    private String environmentInfo;

    /**
     * 版本信息
     */
    @TableField("version_info")
    private String versionInfo;

    /**
     * 扩展信息 (JSON格式)
     */
    @TableField("extended_info")
    private String extendedInfo;

    /**
     * 归档状态 (ACTIVE, ARCHIVED, DELETED)
     */
    @TableField("archive_status")
    private String archiveStatus;

    /**
     * 归档时间
     */
    @TableField("archive_time")
    private LocalDateTime archiveTime;

    /**
     * 保留期限 (天)
     */
    @TableField("retention_days")
    private Integer retentionDays;

    /**
     * 过期时间
     */
    @TableField("expiry_time")
    private LocalDateTime expiryTime;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

    // 非数据库字段

    /**
     * 系统配置信息
     */
    @TableField(exist = false)
    private IntgSystemConfig systemConfig;

    /**
     * 映射配置信息
     */
    @TableField(exist = false)
    private IntgDataMapping dataMapping;

    /**
     * 子日志列表
     */
    @TableField(exist = false)
    private List<IntgSyncLog> subLogs;

    /**
     * 相关日志列表
     */
    @TableField(exist = false)
    private List<IntgSyncLog> relatedLogs;

    /**
     * 统计信息
     */
    @TableField(exist = false)
    private Map<String, Object> statistics;

    /**
     * 分析结果
     */
    @TableField(exist = false)
    private Map<String, Object> analysisResult;
}
