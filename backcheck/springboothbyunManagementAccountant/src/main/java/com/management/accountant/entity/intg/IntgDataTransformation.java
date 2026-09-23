package com.management.accountant.entity.intg;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据转换实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_data_transformation")
public class IntgDataTransformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 转换ID，主键
     */
    @TableId(value = "transformation_id", type = IdType.ASSIGN_ID)
    private String transformationId;

    /**
     * 转换名称
     */
    @TableField("transformation_name")
    private String transformationName;

    /**
     * 转换编码
     */
    @TableField("transformation_code")
    private String transformationCode;

    /**
     * 转换类型：MAPPING-字段映射/AGGREGATION-数据聚合/FILTERING-数据过滤/VALIDATION-数据验证/ENRICHMENT-数据丰富
     */
    @TableField("transformation_type")
    private String transformationType;

    /**
     * 转换方向：INBOUND-入站/OUTBOUND-出站/BIDIRECTIONAL-双向
     */
    @TableField("transformation_direction")
    private String transformationDirection;

    /**
     * 源数据格式：JSON/XML/CSV/EXCEL/DATABASE/API
     */
    @TableField("source_format")
    private String sourceFormat;

    /**
     * 目标数据格式：JSON/XML/CSV/EXCEL/DATABASE/API
     */
    @TableField("target_format")
    private String targetFormat;

    /**
     * 源数据结构，JSON Schema格式
     */
    @TableField("source_schema")
    private String sourceSchema;

    /**
     * 目标数据结构，JSON Schema格式
     */
    @TableField("target_schema")
    private String targetSchema;

    /**
     * 转换规则，JSON格式存储
     */
    @TableField("transformation_rules")
    private String transformationRules;

    /**
     * 映射配置，JSON格式存储
     */
    @TableField("mapping_config")
    private String mappingConfig;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 错误处理策略：SKIP-跳过/RETRY-重试/FAIL-失败/LOG-记录日志
     */
    @TableField("error_handling_strategy")
    private String errorHandlingStrategy;

    /**
     * 批处理大小
     */
    @TableField("batch_size")
    private Integer batchSize;

    /**
     * 并发线程数
     */
    @TableField("concurrent_threads")
    private Integer concurrentThreads;

    /**
     * 超时时间（秒）
     */
    @TableField("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 重试间隔（秒）
     */
    @TableField("retry_interval")
    private Integer retryInterval;

    /**
     * 是否启用缓存：0-否/1-是
     */
    @TableField("cache_enabled")
    private Boolean cacheEnabled;

    /**
     * 缓存配置，JSON格式存储
     */
    @TableField("cache_config")
    private String cacheConfig;

    /**
     * 是否启用监控：0-否/1-是
     */
    @TableField("monitoring_enabled")
    private Boolean monitoringEnabled;

    /**
     * 监控配置，JSON格式存储
     */
    @TableField("monitoring_config")
    private String monitoringConfig;

    /**
     * 性能统计，JSON格式存储
     */
    @TableField("performance_stats")
    private String performanceStats;

    /**
     * 最后执行时间
     */
    @TableField("last_execution_time")
    private LocalDateTime lastExecutionTime;

    /**
     * 最后执行状态：SUCCESS-成功/FAILED-失败/PARTIAL-部分成功
     */
    @TableField("last_execution_status")
    private String lastExecutionStatus;

    /**
     * 最后执行结果
     */
    @TableField("last_execution_result")
    private String lastExecutionResult;

    /**
     * 执行次数
     */
    @TableField("execution_count")
    private Long executionCount;

    /**
     * 成功次数
     */
    @TableField("success_count")
    private Long successCount;

    /**
     * 失败次数
     */
    @TableField("failure_count")
    private Long failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    @TableField("avg_execution_time")
    private Long avgExecutionTime;

    /**
     * 状态：ACTIVE-激活/INACTIVE-停用/TESTING-测试中
     */
    @TableField("status")
    private String status;

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
     * 转换描述
     */
    @TableField("transformation_description")
    private String transformationDescription;

    /**
     * 转换分类：ETL-数据抽取转换加载/ELT-数据抽取加载转换/STREAMING-流式处理/BATCH-批处理
     */
    @TableField("transformation_category")
    private String transformationCategory;

    /**
     * 转换优先级：HIGH-高/MEDIUM-中/LOW-低
     */
    @TableField("transformation_priority")
    private String transformationPriority;

    /**
     * 转换负责人
     */
    @TableField("transformation_owner")
    private String transformationOwner;

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
     * 业务域
     */
    @TableField("business_domain")
    private String businessDomain;

    /**
     * 技术域
     */
    @TableField("technical_domain")
    private String technicalDomain;

    /**
     * 数据源配置，JSON格式存储
     */
    @TableField("data_source_config")
    private String dataSourceConfig;

    /**
     * 数据目标配置，JSON格式存储
     */
    @TableField("data_target_config")
    private String dataTargetConfig;

    /**
     * 调度配置，JSON格式存储
     */
    @TableField("schedule_config")
    private String scheduleConfig;

    /**
     * 依赖关系，JSON格式存储
     */
    @TableField("dependencies")
    private String dependencies;

    /**
     * 前置条件，JSON格式存储
     */
    @TableField("preconditions")
    private String preconditions;

    /**
     * 后置操作，JSON格式存储
     */
    @TableField("post_actions")
    private String postActions;

    /**
     * 数据质量规则，JSON格式存储
     */
    @TableField("data_quality_rules")
    private String dataQualityRules;

    /**
     * 数据血缘信息，JSON格式存储
     */
    @TableField("data_lineage")
    private String dataLineage;

    /**
     * 安全配置，JSON格式存储
     */
    @TableField("security_config")
    private String securityConfig;

    /**
     * 合规要求
     */
    @TableField("compliance_requirements")
    private String complianceRequirements;

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
