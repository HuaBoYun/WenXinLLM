package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数字员工实体类
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_digital_employee")
@ApiModel(value = "SsDigitalEmployee对象", description = "数字员工")
public class SsDigitalEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数字员工ID")
    @TableId(value = "robot_id", type = IdType.AUTO)
    private Long robotId;

    @ApiModelProperty(value = "数字员工名称")
    @TableField("robot_name")
    private String robotName;

    @ApiModelProperty(value = "数字员工编码")
    @TableField("robot_code")
    private String robotCode;

    @ApiModelProperty(value = "类型：RPA-机器人流程自动化/AI-人工智能/CHATBOT-聊天机器人")
    @TableField("robot_type")
    private String robotType;

    @ApiModelProperty(value = "分类：DATA_ENTRY-数据录入/DOCUMENT_PROCESSING-文档处理/CUSTOMER_SERVICE-客户服务")
    @TableField("robot_category")
    private String robotCategory;

    @ApiModelProperty(value = "数字员工状态：ACTIVE-活跃/INACTIVE-非活跃/MAINTENANCE-维护中/RETIRED-已退役")
    @TableField("robot_status")
    private String robotStatus;

    @ApiModelProperty(value = "部署状态：DEVELOPMENT-开发中/TESTING-测试中/PRODUCTION-生产中/RETIRED-已退役")
    @TableField("deployment_status")
    private String deploymentStatus;

    @ApiModelProperty(value = "优先级：1-最高，10-最低")
    @TableField("priority")
    private Integer priority;

    @ApiModelProperty(value = "能力描述，JSON格式存储")
    @TableField("capabilities")
    private String capabilities;

    @ApiModelProperty(value = "自动化流程，JSON格式存储")
    @TableField("automation_processes")
    private String automationProcesses;

    @ApiModelProperty(value = "决策规则，JSON格式存储")
    @TableField("decision_rules")
    private String decisionRules;

    @ApiModelProperty(value = "异常处理，JSON格式存储")
    @TableField("exception_handling")
    private String exceptionHandling;

    @ApiModelProperty(value = "性能指标，JSON格式存储")
    @TableField("performance_metrics")
    private String performanceMetrics;

    @ApiModelProperty(value = "学习配置，JSON格式存储")
    @TableField("learning_config")
    private String learningConfig;

    @ApiModelProperty(value = "集成配置，JSON格式存储")
    @TableField("integration_config")
    private String integrationConfig;

    @ApiModelProperty(value = "安全配置，JSON格式存储")
    @TableField("security_config")
    private String securityConfig;

    @ApiModelProperty(value = "工作时间配置，JSON格式存储")
    @TableField("work_schedule")
    private String workSchedule;

    @ApiModelProperty(value = "任务队列配置，JSON格式存储")
    @TableField("task_queue_config")
    private String taskQueueConfig;

    @ApiModelProperty(value = "监控配置，JSON格式存储")
    @TableField("monitoring_config")
    private String monitoringConfig;

    @ApiModelProperty(value = "通知配置，JSON格式存储")
    @TableField("notification_config")
    private String notificationConfig;

    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private String version;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "标签，多个标签用逗号分隔")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "所属部门ID")
    @TableField("department_id")
    private Long departmentId;

    @ApiModelProperty(value = "所属部门名称")
    @TableField("department_name")
    private String departmentName;

    @ApiModelProperty(value = "负责人ID")
    @TableField("owner_id")
    private Long ownerId;

    @ApiModelProperty(value = "负责人姓名")
    @TableField("owner_name")
    private String ownerName;

    @ApiModelProperty(value = "开发者ID")
    @TableField("developer_id")
    private Long developerId;

    @ApiModelProperty(value = "开发者姓名")
    @TableField("developer_name")
    private String developerName;

    @ApiModelProperty(value = "最后执行时间")
    @TableField("last_execution_time")
    private LocalDateTime lastExecutionTime;

    @ApiModelProperty(value = "下次执行时间")
    @TableField("next_execution_time")
    private LocalDateTime nextExecutionTime;

    @ApiModelProperty(value = "执行次数")
    @TableField("execution_count")
    private Long executionCount;

    @ApiModelProperty(value = "成功次数")
    @TableField("success_count")
    private Long successCount;

    @ApiModelProperty(value = "失败次数")
    @TableField("failure_count")
    private Long failureCount;

    @ApiModelProperty(value = "成功率，0-100")
    @TableField("success_rate")
    private BigDecimal successRate;

    @ApiModelProperty(value = "平均执行时间（秒）")
    @TableField("avg_execution_time")
    private BigDecimal avgExecutionTime;

    @ApiModelProperty(value = "最大执行时间（秒）")
    @TableField("max_execution_time")
    private BigDecimal maxExecutionTime;

    @ApiModelProperty(value = "最小执行时间（秒）")
    @TableField("min_execution_time")
    private BigDecimal minExecutionTime;

    @ApiModelProperty(value = "CPU使用率")
    @TableField("cpu_usage")
    private BigDecimal cpuUsage;

    @ApiModelProperty(value = "内存使用率")
    @TableField("memory_usage")
    private BigDecimal memoryUsage;

    @ApiModelProperty(value = "磁盘使用率")
    @TableField("disk_usage")
    private BigDecimal diskUsage;

    @ApiModelProperty(value = "网络使用率")
    @TableField("network_usage")
    private BigDecimal networkUsage;

    @ApiModelProperty(value = "错误率")
    @TableField("error_rate")
    private BigDecimal errorRate;

    @ApiModelProperty(value = "可用性")
    @TableField("availability")
    private BigDecimal availability;

    @ApiModelProperty(value = "响应时间（毫秒）")
    @TableField("response_time")
    private BigDecimal responseTime;

    @ApiModelProperty(value = "吞吐量")
    @TableField("throughput")
    private BigDecimal throughput;

    @ApiModelProperty(value = "并发数")
    @TableField("concurrency")
    private Integer concurrency;

    @ApiModelProperty(value = "队列长度")
    @TableField("queue_length")
    private Integer queueLength;

    @ApiModelProperty(value = "处理能力评分")
    @TableField("capability_score")
    private BigDecimal capabilityScore;

    @ApiModelProperty(value = "学习能力评分")
    @TableField("learning_score")
    private BigDecimal learningScore;

    @ApiModelProperty(value = "适应性评分")
    @TableField("adaptability_score")
    private BigDecimal adaptabilityScore;

    @ApiModelProperty(value = "稳定性评分")
    @TableField("stability_score")
    private BigDecimal stabilityScore;

    @ApiModelProperty(value = "安全性评分")
    @TableField("security_score")
    private BigDecimal securityScore;

    @ApiModelProperty(value = "综合评分")
    @TableField("overall_score")
    private BigDecimal overallScore;

    @ApiModelProperty(value = "激活时间")
    @TableField("activation_time")
    private LocalDateTime activationTime;

    @ApiModelProperty(value = "停用时间")
    @TableField("deactivation_time")
    private LocalDateTime deactivationTime;

    @ApiModelProperty(value = "维护开始时间")
    @TableField("maintenance_start_time")
    private LocalDateTime maintenanceStartTime;

    @ApiModelProperty(value = "维护结束时间")
    @TableField("maintenance_end_time")
    private LocalDateTime maintenanceEndTime;

    @ApiModelProperty(value = "最后健康检查时间")
    @TableField("last_health_check_time")
    private LocalDateTime lastHealthCheckTime;

    @ApiModelProperty(value = "健康状态：HEALTHY-健康/WARNING-警告/CRITICAL-严重/UNKNOWN-未知")
    @TableField("health_status")
    private String healthStatus;

    @ApiModelProperty(value = "健康检查结果")
    @TableField("health_check_result")
    private String healthCheckResult;

    @ApiModelProperty(value = "最后错误信息")
    @TableField("last_error_message")
    private String lastErrorMessage;

    @ApiModelProperty(value = "最后错误时间")
    @TableField("last_error_time")
    private LocalDateTime lastErrorTime;

    @ApiModelProperty(value = "备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty(value = "扩展字段1")
    @TableField("ext_field1")
    private String extField1;

    @ApiModelProperty(value = "扩展字段2")
    @TableField("ext_field2")
    private String extField2;

    @ApiModelProperty(value = "扩展字段3")
    @TableField("ext_field3")
    private String extField3;

    @ApiModelProperty(value = "扩展字段4")
    @TableField("ext_field4")
    private String extField4;

    @ApiModelProperty(value = "扩展字段5")
    @TableField("ext_field5")
    private String extField5;

    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "逻辑删除标志：0-未删除，1-已删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
