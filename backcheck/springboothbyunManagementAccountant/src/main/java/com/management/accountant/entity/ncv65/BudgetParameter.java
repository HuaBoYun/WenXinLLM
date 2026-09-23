package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算参数实体类
 * 
 * @description 预算参数管理实体，支持预算系统参数的配置和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_PARAMETER")
public class BudgetParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 参数编码
     */
    @TableField("PARAMETER_CODE")
    private String parameterCode;

    /**
     * 参数名称
     */
    @TableField("PARAMETER_NAME")
    private String parameterName;

    /**
     * 参数类型：SYSTEM-系统参数，BUSINESS-业务参数，CONFIG-配置参数，FORMULA-公式参数
     */
    @TableField("PARAMETER_TYPE")
    private String parameterType;

    /**
     * 参数分类：GENERAL-通用，CALCULATION-计算，CONTROL-控制，WORKFLOW-工作流
     */
    @TableField("PARAMETER_CATEGORY")
    private String parameterCategory;

    /**
     * 数据类型：STRING-字符串，INTEGER-整数，DECIMAL-小数，BOOLEAN-布尔，DATE-日期，JSON-JSON对象
     */
    @TableField("DATA_TYPE")
    private String dataType;

    /**
     * 参数值（字符串格式）
     */
    @TableField("PARAMETER_VALUE")
    private String parameterValue;

    /**
     * 数值型参数值
     */
    @TableField("NUMERIC_VALUE")
    private BigDecimal numericValue;

    /**
     * 布尔型参数值
     */
    @TableField("BOOLEAN_VALUE")
    private Boolean booleanValue;

    /**
     * 日期型参数值
     */
    @TableField("DATE_VALUE")
    private LocalDateTime dateValue;

    /**
     * JSON型参数值
     */
    @TableField("JSON_VALUE")
    private String jsonValue;

    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;

    /**
     * 最小值
     */
    @TableField("MIN_VALUE")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @TableField("MAX_VALUE")
    private BigDecimal maxValue;

    /**
     * 可选值列表（JSON格式）
     */
    @TableField("ALLOWED_VALUES")
    private String allowedValues;

    /**
     * 验证规则
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 格式化规则
     */
    @TableField("FORMAT_RULES")
    private String formatRules;

    /**
     * 参数单位
     */
    @TableField("PARAMETER_UNIT")
    private String parameterUnit;

    /**
     * 参数精度
     */
    @TableField("PARAMETER_PRECISION")
    private Integer parameterPrecision;

    /**
     * 参数状态：ACTIVE-激活，INACTIVE-停用，DRAFT-草稿
     */
    @TableField("PARAMETER_STATUS")
    private String parameterStatus;

    /**
     * 参数级别：SYSTEM-系统级，ORGANIZATION-组织级，USER-用户级
     */
    @TableField("PARAMETER_LEVEL")
    private String parameterLevel;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORGANIZATION_ID")
    private String applicableOrganizationId;

    /**
     * 适用用户ID
     */
    @TableField("APPLICABLE_USER_ID")
    private String applicableUserId;

    /**
     * 适用范围：ALL-全部，SPECIFIC-指定范围，CONDITIONAL-条件适用
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 适用条件（JSON格式）
     */
    @TableField("APPLICABLE_CONDITIONS")
    private String applicableConditions;

    /**
     * 是否可修改
     */
    @TableField("IS_MODIFIABLE")
    private Boolean isModifiable;

    /**
     * 是否必填
     */
    @TableField("IS_REQUIRED")
    private Boolean isRequired;

    /**
     * 是否敏感参数
     */
    @TableField("IS_SENSITIVE")
    private Boolean isSensitive;

    /**
     * 是否加密存储
     */
    @TableField("IS_ENCRYPTED")
    private Boolean isEncrypted;

    /**
     * 是否可继承
     */
    @TableField("IS_INHERITABLE")
    private Boolean isInheritable;

    /**
     * 父参数ID
     */
    @TableField("PARENT_PARAMETER_ID")
    private String parentParameterId;

    /**
     * 参数组
     */
    @TableField("PARAMETER_GROUP")
    private String parameterGroup;

    /**
     * 参数标签（JSON格式）
     */
    @TableField("PARAMETER_TAGS")
    private String parameterTags;

    /**
     * 参数描述
     */
    @TableField("PARAMETER_DESCRIPTION")
    private String parameterDescription;

    /**
     * 使用说明
     */
    @TableField("USAGE_INSTRUCTIONS")
    private String usageInstructions;

    /**
     * 示例值
     */
    @TableField("EXAMPLE_VALUE")
    private String exampleValue;

    /**
     * 变更历史
     */
    @TableField("CHANGE_HISTORY")
    private String changeHistory;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDateTime expiryDate;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 审批人
     */
    @TableField("APPROVED_BY")
    private String approvedBy;

    /**
     * 审批时间
     */
    @TableField("APPROVED_TIME")
    private LocalDateTime approvedTime;

    /**
     * 最后修改人
     */
    @TableField("LAST_MODIFIED_BY")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @TableField("LAST_MODIFIED_TIME")
    private LocalDateTime lastModifiedTime;

    /**
     * 访问次数
     */
    @TableField("ACCESS_COUNT")
    private Integer accessCount;

    /**
     * 最后访问时间
     */
    @TableField("LAST_ACCESS_TIME")
    private LocalDateTime lastAccessTime;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
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

    // ==================== 常量定义 ====================

    /**
     * 参数类型常量
     */
    public static final String PARAMETER_TYPE_SYSTEM = "SYSTEM";
    public static final String PARAMETER_TYPE_BUSINESS = "BUSINESS";
    public static final String PARAMETER_TYPE_CONFIG = "CONFIG";
    public static final String PARAMETER_TYPE_FORMULA = "FORMULA";

    /**
     * 参数分类常量
     */
    public static final String PARAMETER_CATEGORY_GENERAL = "GENERAL";
    public static final String PARAMETER_CATEGORY_CALCULATION = "CALCULATION";
    public static final String PARAMETER_CATEGORY_CONTROL = "CONTROL";
    public static final String PARAMETER_CATEGORY_WORKFLOW = "WORKFLOW";

    /**
     * 数据类型常量
     */
    public static final String DATA_TYPE_STRING = "STRING";
    public static final String DATA_TYPE_INTEGER = "INTEGER";
    public static final String DATA_TYPE_DECIMAL = "DECIMAL";
    public static final String DATA_TYPE_BOOLEAN = "BOOLEAN";
    public static final String DATA_TYPE_DATE = "DATE";
    public static final String DATA_TYPE_JSON = "JSON";

    /**
     * 参数状态常量
     */
    public static final String PARAMETER_STATUS_ACTIVE = "ACTIVE";
    public static final String PARAMETER_STATUS_INACTIVE = "INACTIVE";
    public static final String PARAMETER_STATUS_DRAFT = "DRAFT";

    /**
     * 参数级别常量
     */
    public static final String PARAMETER_LEVEL_SYSTEM = "SYSTEM";
    public static final String PARAMETER_LEVEL_ORGANIZATION = "ORGANIZATION";
    public static final String PARAMETER_LEVEL_USER = "USER";

    /**
     * 适用范围常量
     */
    public static final String APPLICABLE_SCOPE_ALL = "ALL";
    public static final String APPLICABLE_SCOPE_SPECIFIC = "SPECIFIC";
    public static final String APPLICABLE_SCOPE_CONDITIONAL = "CONDITIONAL";
}
