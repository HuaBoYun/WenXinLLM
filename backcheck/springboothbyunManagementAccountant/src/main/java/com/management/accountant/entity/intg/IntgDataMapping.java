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
 * 数据映射配置实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_intg_data_mapping")
public class IntgDataMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 映射ID
     */
    @TableId(value = "mapping_id", type = IdType.ASSIGN_UUID)
    private String mappingId;

    /**
     * 映射编码
     */
    @TableField("mapping_code")
    private String mappingCode;

    /**
     * 映射名称
     */
    @TableField("mapping_name")
    private String mappingName;

    /**
     * 系统配置ID
     */
    @TableField("config_id")
    private String configId;

    /**
     * 映射类型 (TABLE_TO_TABLE, FIELD_TO_FIELD, OBJECT_TO_OBJECT, API_TO_API)
     */
    @TableField("mapping_type")
    private String mappingType;

    /**
     * 映射方向 (IMPORT, EXPORT, BIDIRECTIONAL)
     */
    @TableField("mapping_direction")
    private String mappingDirection;

    /**
     * 源系统类型
     */
    @TableField("source_system_type")
    private String sourceSystemType;

    /**
     * 源系统名称
     */
    @TableField("source_system_name")
    private String sourceSystemName;

    /**
     * 源表名/接口名
     */
    @TableField("source_table_name")
    private String sourceTableName;

    /**
     * 源字段名
     */
    @TableField("source_field_name")
    private String sourceFieldName;

    /**
     * 源字段类型
     */
    @TableField("source_field_type")
    private String sourceFieldType;

    /**
     * 源字段长度
     */
    @TableField("source_field_length")
    private Integer sourceFieldLength;

    /**
     * 源字段精度
     */
    @TableField("source_field_precision")
    private Integer sourceFieldPrecision;

    /**
     * 源字段是否必填
     */
    @TableField("source_field_required")
    private Boolean sourceFieldRequired;

    /**
     * 源字段默认值
     */
    @TableField("source_field_default")
    private String sourceFieldDefault;

    /**
     * 源字段描述
     */
    @TableField("source_field_description")
    private String sourceFieldDescription;

    /**
     * 源查询条件
     */
    @TableField("source_query_condition")
    private String sourceQueryCondition;

    /**
     * 源排序条件
     */
    @TableField("source_order_condition")
    private String sourceOrderCondition;

    /**
     * 目标系统类型
     */
    @TableField("target_system_type")
    private String targetSystemType;

    /**
     * 目标系统名称
     */
    @TableField("target_system_name")
    private String targetSystemName;

    /**
     * 目标表名/接口名
     */
    @TableField("target_table_name")
    private String targetTableName;

    /**
     * 目标字段名
     */
    @TableField("target_field_name")
    private String targetFieldName;

    /**
     * 目标字段类型
     */
    @TableField("target_field_type")
    private String targetFieldType;

    /**
     * 目标字段长度
     */
    @TableField("target_field_length")
    private Integer targetFieldLength;

    /**
     * 目标字段精度
     */
    @TableField("target_field_precision")
    private Integer targetFieldPrecision;

    /**
     * 目标字段是否必填
     */
    @TableField("target_field_required")
    private Boolean targetFieldRequired;

    /**
     * 目标字段默认值
     */
    @TableField("target_field_default")
    private String targetFieldDefault;

    /**
     * 目标字段描述
     */
    @TableField("target_field_description")
    private String targetFieldDescription;

    /**
     * 转换规则类型 (DIRECT, FORMULA, LOOKUP, SCRIPT, CONSTANT)
     */
    @TableField("transform_rule_type")
    private String transformRuleType;

    /**
     * 转换规则表达式
     */
    @TableField("transform_rule_expression")
    private String transformRuleExpression;

    /**
     * 转换脚本
     */
    @TableField("transform_script")
    private String transformScript;

    /**
     * 转换脚本语言 (JAVASCRIPT, GROOVY, PYTHON, SQL)
     */
    @TableField("transform_script_language")
    private String transformScriptLanguage;

    /**
     * 数据验证规则
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 数据清洗规则
     */
    @TableField("cleansing_rules")
    private String cleansingRules;

    /**
     * 数据格式化规则
     */
    @TableField("formatting_rules")
    private String formattingRules;

    /**
     * 错误处理策略 (SKIP, DEFAULT, STOP, LOG)
     */
    @TableField("error_handling_strategy")
    private String errorHandlingStrategy;

    /**
     * 默认值策略 (NULL, EMPTY, CONSTANT, FORMULA)
     */
    @TableField("default_value_strategy")
    private String defaultValueStrategy;

    /**
     * 映射优先级 (1-10, 1最高)
     */
    @TableField("mapping_priority")
    private Integer mappingPriority;

    /**
     * 映射权重
     */
    @TableField("mapping_weight")
    private Integer mappingWeight;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否必须
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 是否唯一
     */
    @TableField("is_unique")
    private Boolean isUnique;

    /**
     * 是否索引
     */
    @TableField("is_indexed")
    private Boolean isIndexed;

    /**
     * 是否加密
     */
    @TableField("is_encrypted")
    private Boolean isEncrypted;

    /**
     * 是否压缩
     */
    @TableField("is_compressed")
    private Boolean isCompressed;

    /**
     * 数据类型转换配置
     */
    @TableField("type_conversion_config")
    private String typeConversionConfig;

    /**
     * 字符编码转换配置
     */
    @TableField("charset_conversion_config")
    private String charsetConversionConfig;

    /**
     * 时区转换配置
     */
    @TableField("timezone_conversion_config")
    private String timezoneConversionConfig;

    /**
     * 数值转换配置
     */
    @TableField("numeric_conversion_config")
    private String numericConversionConfig;

    /**
     * 日期转换配置
     */
    @TableField("date_conversion_config")
    private String dateConversionConfig;

    /**
     * 映射状态 (ACTIVE, INACTIVE, TESTING, ERROR)
     */
    @TableField("mapping_status")
    private String mappingStatus;

    /**
     * 映射版本
     */
    @TableField("mapping_version")
    private String mappingVersion;

    /**
     * 上一版本ID
     */
    @TableField("previous_version_id")
    private String previousVersionId;

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
     * 最后执行时间
     */
    @TableField("last_execution_time")
    private LocalDateTime lastExecutionTime;

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
     * 平均执行时间 (毫秒)
     */
    @TableField("avg_execution_time")
    private Long avgExecutionTime;

    /**
     * 最大执行时间 (毫秒)
     */
    @TableField("max_execution_time")
    private Long maxExecutionTime;

    /**
     * 最小执行时间 (毫秒)
     */
    @TableField("min_execution_time")
    private Long minExecutionTime;

    /**
     * 数据量统计
     */
    @TableField("data_volume_stats")
    private String dataVolumeStats;

    /**
     * 性能统计
     */
    @TableField("performance_stats")
    private String performanceStats;

    /**
     * 质量统计
     */
    @TableField("quality_stats")
    private String qualityStats;

    /**
     * 映射标签
     */
    @TableField("mapping_tags")
    private String mappingTags;

    /**
     * 映射分类
     */
    @TableField("mapping_category")
    private String mappingCategory;

    /**
     * 映射环境 (DEV, TEST, UAT, PROD)
     */
    @TableField("mapping_environment")
    private String mappingEnvironment;

    /**
     * 扩展配置 (JSON格式)
     */
    @TableField("extended_config")
    private String extendedConfig;

    /**
     * 映射描述
     */
    @TableField("mapping_description")
    private String mappingDescription;

    /**
     * 映射备注
     */
    @TableField("mapping_remarks")
    private String mappingRemarks;

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
     * 映射执行历史
     */
    @TableField(exist = false)
    private List<Object> executionHistory;

    /**
     * 数据样例
     */
    @TableField(exist = false)
    private Map<String, Object> dataSample;

    /**
     * 映射测试结果
     */
    @TableField(exist = false)
    private Map<String, Object> testResult;

    /**
     * 映射验证结果
     */
    @TableField(exist = false)
    private Map<String, Object> validationResult;

    /**
     * 性能指标
     */
    @TableField(exist = false)
    private Map<String, Object> performanceMetrics;
}
