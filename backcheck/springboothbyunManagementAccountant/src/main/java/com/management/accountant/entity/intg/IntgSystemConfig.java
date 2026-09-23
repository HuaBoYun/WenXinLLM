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
 * ERP系统集成配置实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_system_config")
public class IntgSystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "config_id", type = IdType.ASSIGN_UUID)
    private String configId;

    /**
     * 配置编码
     */
    @TableField("config_code")
    private String configCode;

    /**
     * 配置名称
     */
    @TableField("config_name")
    private String configName;

    /**
     * 系统类型 (ERP, CRM, OA, WMS, MES, PLM, SCM, HRM, BI, OTHER)
     */
    @TableField("system_type")
    private String systemType;

    /**
     * 系统名称
     */
    @TableField("system_name")
    private String systemName;

    /**
     * 系统版本
     */
    @TableField("system_version")
    private String systemVersion;

    /**
     * 系统厂商
     */
    @TableField("system_vendor")
    private String systemVendor;

    /**
     * 连接类型 (DATABASE, WEBSERVICE, REST_API, SOAP, FTP, FILE, MESSAGE_QUEUE)
     */
    @TableField("connection_type")
    private String connectionType;

    /**
     * 连接地址
     */
    @TableField("connection_url")
    private String connectionUrl;

    /**
     * 连接端口
     */
    @TableField("connection_port")
    private Integer connectionPort;

    /**
     * 数据库类型 (MYSQL, ORACLE, SQLSERVER, POSTGRESQL, DM, OTHER)
     */
    @TableField("database_type")
    private String databaseType;

    /**
     * 数据库名称
     */
    @TableField("database_name")
    private String databaseName;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码 (加密存储)
     */
    @TableField("password")
    private String password;

    /**
     * 连接池配置
     */
    @TableField("connection_pool_config")
    private String connectionPoolConfig;

    /**
     * 超时设置 (秒)
     */
    @TableField("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 重试间隔 (秒)
     */
    @TableField("retry_interval")
    private Integer retryInterval;

    /**
     * SSL配置
     */
    @TableField("ssl_config")
    private String sslConfig;

    /**
     * 认证方式 (BASIC, OAUTH, TOKEN, CERTIFICATE, NONE)
     */
    @TableField("auth_type")
    private String authType;

    /**
     * 认证配置
     */
    @TableField("auth_config")
    private String authConfig;

    /**
     * API密钥
     */
    @TableField("api_key")
    private String apiKey;

    /**
     * 访问令牌
     */
    @TableField("access_token")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * 令牌过期时间
     */
    @TableField("token_expires_at")
    private LocalDateTime tokenExpiresAt;

    /**
     * 数据格式 (JSON, XML, CSV, EXCEL, TEXT, BINARY)
     */
    @TableField("data_format")
    private String dataFormat;

    /**
     * 字符编码 (UTF-8, GBK, GB2312, ISO-8859-1)
     */
    @TableField("charset")
    private String charset;

    /**
     * 压缩方式 (NONE, GZIP, ZIP, RAR)
     */
    @TableField("compression_type")
    private String compressionType;

    /**
     * 加密方式 (NONE, AES, DES, RSA)
     */
    @TableField("encryption_type")
    private String encryptionType;

    /**
     * 加密密钥
     */
    @TableField("encryption_key")
    private String encryptionKey;

    /**
     * 同步方式 (REAL_TIME, SCHEDULED, MANUAL, EVENT_DRIVEN)
     */
    @TableField("sync_mode")
    private String syncMode;

    /**
     * 同步频率 (cron表达式)
     */
    @TableField("sync_frequency")
    private String syncFrequency;

    /**
     * 同步方向 (IMPORT, EXPORT, BIDIRECTIONAL)
     */
    @TableField("sync_direction")
    private String syncDirection;

    /**
     * 批处理大小
     */
    @TableField("batch_size")
    private Integer batchSize;

    /**
     * 并发线程数
     */
    @TableField("thread_count")
    private Integer threadCount;

    /**
     * 错误处理策略 (STOP, SKIP, RETRY, ROLLBACK)
     */
    @TableField("error_handling")
    private String errorHandling;

    /**
     * 日志级别 (DEBUG, INFO, WARN, ERROR)
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 是否启用监控
     */
    @TableField("monitoring_enabled")
    private Boolean monitoringEnabled;

    /**
     * 监控配置
     */
    @TableField("monitoring_config")
    private String monitoringConfig;

    /**
     * 告警配置
     */
    @TableField("alert_config")
    private String alertConfig;

    /**
     * 性能配置
     */
    @TableField("performance_config")
    private String performanceConfig;

    /**
     * 缓存配置
     */
    @TableField("cache_config")
    private String cacheConfig;

    /**
     * 扩展配置 (JSON格式)
     */
    @TableField("extended_config")
    private String extendedConfig;

    /**
     * 配置状态 (ACTIVE, INACTIVE, TESTING, MAINTENANCE, DEPRECATED)
     */
    @TableField("config_status")
    private String configStatus;

    /**
     * 配置优先级 (1-10, 1最高)
     */
    @TableField("config_priority")
    private Integer configPriority;

    /**
     * 配置权重
     */
    @TableField("config_weight")
    private Integer configWeight;

    /**
     * 配置标签
     */
    @TableField("config_tags")
    private String configTags;

    /**
     * 配置分类
     */
    @TableField("config_category")
    private String configCategory;

    /**
     * 配置环境 (DEV, TEST, UAT, PROD)
     */
    @TableField("config_environment")
    private String configEnvironment;

    /**
     * 配置版本
     */
    @TableField("config_version")
    private String configVersion;

    /**
     * 上一版本ID
     */
    @TableField("previous_version_id")
    private String previousVersionId;

    /**
     * 是否为默认配置
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否只读
     */
    @TableField("is_readonly")
    private Boolean isReadonly;

    /**
     * 生效时间
     */
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("expiry_time")
    private LocalDateTime expiryTime;

    /**
     * 最后连接时间
     */
    @TableField("last_connection_time")
    private LocalDateTime lastConnectionTime;

    /**
     * 最后同步时间
     */
    @TableField("last_sync_time")
    private LocalDateTime lastSyncTime;

    /**
     * 连接状态 (CONNECTED, DISCONNECTED, ERROR, TESTING)
     */
    @TableField("connection_status")
    private String connectionStatus;

    /**
     * 同步状态 (SUCCESS, FAILED, RUNNING, PENDING)
     */
    @TableField("sync_status")
    private String syncStatus;

    /**
     * 健康状态 (HEALTHY, WARNING, ERROR, UNKNOWN)
     */
    @TableField("health_status")
    private String healthStatus;

    /**
     * 健康检查时间
     */
    @TableField("health_check_time")
    private LocalDateTime healthCheckTime;

    /**
     * 配置描述
     */
    @TableField("config_description")
    private String configDescription;

    /**
     * 配置备注
     */
    @TableField("config_remarks")
    private String configRemarks;

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
     * 数据映射配置列表
     */
    @TableField(exist = false)
    private List<Object> dataMappings;

    /**
     * 同步日志列表
     */
    @TableField(exist = false)
    private List<Object> syncLogs;

    /**
     * 连接测试结果
     */
    @TableField(exist = false)
    private Map<String, Object> connectionTestResult;

    /**
     * 性能统计信息
     */
    @TableField(exist = false)
    private Map<String, Object> performanceStats;

    /**
     * 健康检查结果
     */
    @TableField(exist = false)
    private Map<String, Object> healthCheckResult;

    /**
     * 配置验证结果
     */
    @TableField(exist = false)
    private Map<String, Object> validationResult;
}
