package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算控制引擎实体类
 * 
 * @description 预算控制引擎管理实体，支持实时预算控制和多种控制策略
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BUDGET_CONTROL_ENGINE")
public class BudgetControlEngine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 控制引擎编码
     */
    @TableField("ENGINE_CODE")
    private String engineCode;

    /**
     * 控制引擎名称
     */
    @TableField("ENGINE_NAME")
    private String engineName;

    /**
     * 引擎类型：real_time-实时控制，batch-批量控制，scheduled-定时控制
     */
    @TableField("ENGINE_TYPE")
    private String engineType;

    /**
     * 控制模式：strict-严格控制，flexible-柔性控制，warning-预警控制
     */
    @TableField("CONTROL_MODE")
    private String controlMode;

    /**
     * 控制范围：global-全局，organization-组织，project-项目，indicator-指标
     */
    @TableField("CONTROL_SCOPE")
    private String controlScope;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORG_ID")
    private String applicableOrgId;

    /**
     * 控制规则配置（JSON格式）
     */
    @TableField("CONTROL_RULES")
    private String controlRules;

    /**
     * 控制策略配置（JSON格式）
     */
    @TableField("CONTROL_STRATEGIES")
    private String controlStrategies;

    /**
     * 预警阈值配置（JSON格式）
     */
    @TableField("WARNING_THRESHOLDS")
    private String warningThresholds;

    /**
     * 控制维度配置（JSON格式）
     */
    @TableField("CONTROL_DIMENSIONS")
    private String controlDimensions;

    /**
     * 控制指标配置（JSON格式）
     */
    @TableField("CONTROL_INDICATORS")
    private String controlIndicators;

    /**
     * 执行优先级：1-最高，2-高，3-普通，4-低，5-最低
     */
    @TableField("EXECUTION_PRIORITY")
    private Integer executionPriority;

    /**
     * 执行顺序
     */
    @TableField("EXECUTION_ORDER")
    private Integer executionOrder;

    /**
     * 是否启用并行执行
     */
    @TableField("IS_PARALLEL_ENABLED")
    private Boolean isParallelEnabled;

    /**
     * 超时时间（毫秒）
     */
    @TableField("TIMEOUT_MILLISECONDS")
    private Long timeoutMilliseconds;

    /**
     * 重试次数
     */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /**
     * 重试间隔（毫秒）
     */
    @TableField("RETRY_INTERVAL")
    private Long retryInterval;

    /**
     * 失败处理策略：ignore-忽略，retry-重试，escalate-升级，stop-停止
     */
    @TableField("FAILURE_STRATEGY")
    private String failureStrategy;

    /**
     * 日志级别：debug，info，warn，error
     */
    @TableField("LOG_LEVEL")
    private String logLevel;

    /**
     * 是否启用详细日志
     */
    @TableField("IS_DETAILED_LOG_ENABLED")
    private Boolean isDetailedLogEnabled;

    /**
     * 性能监控配置（JSON格式）
     */
    @TableField("PERFORMANCE_CONFIG")
    private String performanceConfig;

    /**
     * 缓存配置（JSON格式）
     */
    @TableField("CACHE_CONFIG")
    private String cacheConfig;

    /**
     * 引擎版本
     */
    @TableField("ENGINE_VERSION")
    private String engineVersion;

    /**
     * 最后执行时间
     */
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    /**
     * 执行次数
     */
    @TableField("EXECUTION_COUNT")
    private Long executionCount;

    /**
     * 成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Long successCount;

    /**
     * 失败次数
     */
    @TableField("FAILURE_COUNT")
    private Long failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    @TableField("AVERAGE_EXECUTION_TIME")
    private BigDecimal averageExecutionTime;

    /**
     * 最大执行时间（毫秒）
     */
    @TableField("MAX_EXECUTION_TIME")
    private Long maxExecutionTime;

    /**
     * 最小执行时间（毫秒）
     */
    @TableField("MIN_EXECUTION_TIME")
    private Long minExecutionTime;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 状态：active-激活，inactive-停用，maintenance-维护中
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @TableField("VERSION")
    @Version
    private Integer version;

    /**
     * 扩展字段1
     */
    @TableField("EXT_FIELD1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("EXT_FIELD2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("EXT_FIELD3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("EXT_FIELD4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("EXT_FIELD5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 常量定义
    public static final String ENGINE_TYPE_REAL_TIME = "real_time";
    public static final String ENGINE_TYPE_BATCH = "batch";
    public static final String ENGINE_TYPE_SCHEDULED = "scheduled";

    public static final String CONTROL_MODE_STRICT = "strict";
    public static final String CONTROL_MODE_FLEXIBLE = "flexible";
    public static final String CONTROL_MODE_WARNING = "warning";

    public static final String CONTROL_SCOPE_GLOBAL = "global";
    public static final String CONTROL_SCOPE_ORGANIZATION = "organization";
    public static final String CONTROL_SCOPE_PROJECT = "project";
    public static final String CONTROL_SCOPE_INDICATOR = "indicator";

    public static final String FAILURE_STRATEGY_IGNORE = "ignore";
    public static final String FAILURE_STRATEGY_RETRY = "retry";
    public static final String FAILURE_STRATEGY_ESCALATE = "escalate";
    public static final String FAILURE_STRATEGY_STOP = "stop";

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_MAINTENANCE = "maintenance";
}
