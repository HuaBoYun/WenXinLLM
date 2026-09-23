package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业调度实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_job_schedule")
public class SsJobSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作业ID
     */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /**
     * 作业编码
     */
    @TableField("job_code")
    private String jobCode;

    /**
     * 作业名称
     */
    @TableField("job_name")
    private String jobName;

    /**
     * 作业描述
     */
    @TableField("job_description")
    private String jobDescription;

    /**
     * 作业类型
     */
    @TableField("job_type")
    private String jobType;

    /**
     * 作业分类
     */
    @TableField("job_category")
    private String jobCategory;

    /**
     * 作业状态
     */
    @TableField("job_status")
    private String jobStatus;

    /**
     * 调度状态
     */
    @TableField("schedule_status")
    private String scheduleStatus;

    /**
     * 执行状态
     */
    @TableField("execution_status")
    private String executionStatus;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 优先级权重
     */
    @TableField("priority_weight")
    private BigDecimal priorityWeight;

    /**
     * 调度表达式
     */
    @TableField("schedule_expression")
    private String scheduleExpression;

    /**
     * 调度类型
     */
    @TableField("schedule_type")
    private String scheduleType;

    /**
     * 调度策略
     */
    @TableField("schedule_strategy")
    private String scheduleStrategy;

    /**
     * 调度算法
     */
    @TableField("schedule_algorithm")
    private String scheduleAlgorithm;

    /**
     * 负载均衡策略
     */
    @TableField("load_balance_strategy")
    private String loadBalanceStrategy;

    /**
     * 执行节点
     */
    @TableField("execution_node")
    private String executionNode;

    /**
     * 执行器类型
     */
    @TableField("executor_type")
    private String executorType;

    /**
     * 执行器配置
     */
    @TableField("executor_config")
    private String executorConfig;

    /**
     * 执行参数
     */
    @TableField("execution_params")
    private String executionParams;

    /**
     * 执行命令
     */
    @TableField("execution_command")
    private String executionCommand;

    /**
     * 执行脚本
     */
    @TableField("execution_script")
    private String executionScript;

    /**
     * 超时时间(秒)
     */
    @TableField("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 重试间隔(秒)
     */
    @TableField("retry_interval")
    private Integer retryInterval;

    /**
     * 失败策略
     */
    @TableField("failure_strategy")
    private String failureStrategy;

    /**
     * 依赖作业ID列表
     */
    @TableField("dependency_jobs")
    private String dependencyJobs;

    /**
     * 依赖条件
     */
    @TableField("dependency_condition")
    private String dependencyCondition;

    /**
     * 资源需求
     */
    @TableField("resource_requirements")
    private String resourceRequirements;

    /**
     * CPU需求
     */
    @TableField("cpu_requirement")
    private BigDecimal cpuRequirement;

    /**
     * 内存需求(MB)
     */
    @TableField("memory_requirement")
    private Integer memoryRequirement;

    /**
     * 磁盘需求(MB)
     */
    @TableField("disk_requirement")
    private Integer diskRequirement;

    /**
     * 网络需求
     */
    @TableField("network_requirement")
    private String networkRequirement;

    /**
     * SLA要求
     */
    @TableField("sla_requirements")
    private String slaRequirements;

    /**
     * 预期执行时间(秒)
     */
    @TableField("expected_duration")
    private Integer expectedDuration;

    /**
     * 最大执行时间(秒)
     */
    @TableField("max_duration")
    private Integer maxDuration;

    /**
     * 平均执行时间(秒)
     */
    @TableField("avg_duration")
    private Integer avgDuration;

    /**
     * 成功率
     */
    @TableField("success_rate")
    private BigDecimal successRate;

    /**
     * 执行次数
     */
    @TableField("execution_count")
    private Integer executionCount;

    /**
     * 成功次数
     */
    @TableField("success_count")
    private Integer successCount;

    /**
     * 失败次数
     */
    @TableField("failure_count")
    private Integer failureCount;

    /**
     * 最后执行时间
     */
    @TableField("last_execution_time")
    private LocalDateTime lastExecutionTime;

    /**
     * 最后成功时间
     */
    @TableField("last_success_time")
    private LocalDateTime lastSuccessTime;

    /**
     * 最后失败时间
     */
    @TableField("last_failure_time")
    private LocalDateTime lastFailureTime;

    /**
     * 下次执行时间
     */
    @TableField("next_execution_time")
    private LocalDateTime nextExecutionTime;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 调度器ID
     */
    @TableField("scheduler_id")
    private Long schedulerId;

    /**
     * 调度器名称
     */
    @TableField("scheduler_name")
    private String schedulerName;

    /**
     * 执行用户ID
     */
    @TableField("executor_user_id")
    private Long executorUserId;

    /**
     * 执行用户名称
     */
    @TableField("executor_user_name")
    private String executorUserName;

    /**
     * 监控用户ID
     */
    @TableField("monitor_user_id")
    private Long monitorUserId;

    /**
     * 监控用户名称
     */
    @TableField("monitor_user_name")
    private String monitorUserName;

    /**
     * 通知配置
     */
    @TableField("notification_config")
    private String notificationConfig;

    /**
     * 告警配置
     */
    @TableField("alert_config")
    private String alertConfig;

    /**
     * 日志级别
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 日志保留天数
     */
    @TableField("log_retention_days")
    private Integer logRetentionDays;

    /**
     * 性能指标
     */
    @TableField("performance_metrics")
    private String performanceMetrics;

    /**
     * 质量指标
     */
    @TableField("quality_metrics")
    private String qualityMetrics;

    /**
     * 业务指标
     */
    @TableField("business_metrics")
    private String businessMetrics;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 扩展属性
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 配置版本
     */
    @TableField("config_version")
    private String configVersion;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

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
}
