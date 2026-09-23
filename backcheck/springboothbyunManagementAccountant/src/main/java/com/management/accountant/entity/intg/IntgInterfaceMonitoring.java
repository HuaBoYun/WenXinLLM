package com.management.accountant.entity.intg;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口监控实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_interface_monitoring")
public class IntgInterfaceMonitoring implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监控ID，主键
     */
    @TableId(value = "monitor_id", type = IdType.ASSIGN_ID)
    private String monitorId;

    /**
     * 接口名称
     */
    @TableField("interface_name")
    private String interfaceName;

    /**
     * 接口URL
     */
    @TableField("interface_url")
    private String interfaceUrl;

    /**
     * 接口类型：REST-REST接口/SOAP-SOAP接口/RPC-RPC接口/GRAPHQL-GraphQL接口
     */
    @TableField("interface_type")
    private String interfaceType;

    /**
     * 监控类型：HEALTH-健康检查/PERFORMANCE-性能监控/AVAILABILITY-可用性监控
     */
    @TableField("monitoring_type")
    private String monitoringType;

    /**
     * 检查间隔（秒）
     */
    @TableField("check_interval")
    private Integer checkInterval;

    /**
     * 超时时间（秒）
     */
    @TableField("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 期望响应，JSON格式存储
     */
    @TableField("expected_response")
    private String expectedResponse;

    /**
     * 告警阈值，JSON格式存储
     */
    @TableField("alert_thresholds")
    private String alertThresholds;

    /**
     * 通知配置，JSON格式存储
     */
    @TableField("notification_config")
    private String notificationConfig;

    /**
     * 最后检查时间
     */
    @TableField("last_check_time")
    private LocalDateTime lastCheckTime;

    /**
     * 最后响应时间（毫秒）
     */
    @TableField("last_response_time")
    private Long lastResponseTime;

    /**
     * 最后状态码
     */
    @TableField("last_status_code")
    private Integer lastStatusCode;

    /**
     * 24小时错误次数
     */
    @TableField("error_count_24h")
    private Integer errorCount24h;

    /**
     * 监控状态：ACTIVE-激活/PAUSED-暂停/STOPPED-停止
     */
    @TableField("monitoring_status")
    private String monitoringStatus;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;

    // 扩展字段

    /**
     * 监控编码
     */
    @TableField("monitor_code")
    private String monitorCode;

    /**
     * 监控描述
     */
    @TableField("monitor_description")
    private String monitorDescription;

    /**
     * 关联API ID
     */
    @TableField("api_id")
    private String apiId;

    /**
     * 监控分组
     */
    @TableField("monitor_group")
    private String monitorGroup;

    /**
     * 监控标签，JSON格式存储
     */
    @TableField("monitor_tags")
    private String monitorTags;

    /**
     * 监控优先级：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("monitor_priority")
    private String monitorPriority;

    /**
     * 监控负责人
     */
    @TableField("monitor_owner")
    private String monitorOwner;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 监控环境：DEV-开发/TEST-测试/UAT-用户验收/PROD-生产
     */
    @TableField("monitor_environment")
    private String monitorEnvironment;

    /**
     * 监控区域
     */
    @TableField("monitor_region")
    private String monitorRegion;

    /**
     * 监控节点
     */
    @TableField("monitor_node")
    private String monitorNode;

    /**
     * 请求头配置，JSON格式存储
     */
    @TableField("request_headers")
    private String requestHeaders;

    /**
     * 请求参数，JSON格式存储
     */
    @TableField("request_parameters")
    private String requestParameters;

    /**
     * 请求体
     */
    @TableField("request_body")
    private String requestBody;

    /**
     * 认证配置，JSON格式存储
     */
    @TableField("auth_config")
    private String authConfig;

    /**
     * SSL配置，JSON格式存储
     */
    @TableField("ssl_config")
    private String sslConfig;

    /**
     * 代理配置，JSON格式存储
     */
    @TableField("proxy_config")
    private String proxyConfig;

    /**
     * 重试配置，JSON格式存储
     */
    @TableField("retry_config")
    private String retryConfig;

    /**
     * 断路器配置，JSON格式存储
     */
    @TableField("circuit_breaker_config")
    private String circuitBreakerConfig;

    /**
     * 健康检查配置，JSON格式存储
     */
    @TableField("health_check_config")
    private String healthCheckConfig;

    /**
     * 性能基线，JSON格式存储
     */
    @TableField("performance_baseline")
    private String performanceBaseline;

    /**
     * 可用性目标（百分比）
     */
    @TableField("availability_target")
    private Double availabilityTarget;

    /**
     * 响应时间目标（毫秒）
     */
    @TableField("response_time_target")
    private Long responseTimeTarget;

    /**
     * 吞吐量目标（TPS）
     */
    @TableField("throughput_target")
    private Integer throughputTarget;

    /**
     * 错误率目标（百分比）
     */
    @TableField("error_rate_target")
    private Double errorRateTarget;

    /**
     * 监控数据保留天数
     */
    @TableField("data_retention_days")
    private Integer dataRetentionDays;

    /**
     * 是否启用告警：0-否/1-是
     */
    @TableField("alert_enabled")
    private Boolean alertEnabled;

    /**
     * 告警静默时间（分钟）
     */
    @TableField("alert_silence_minutes")
    private Integer alertSilenceMinutes;

    /**
     * 告警升级配置，JSON格式存储
     */
    @TableField("alert_escalation_config")
    private String alertEscalationConfig;

    /**
     * 自动恢复配置，JSON格式存储
     */
    @TableField("auto_recovery_config")
    private String autoRecoveryConfig;

    /**
     * 扩展属性，JSON格式存储
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
}
