package com.management.accountant.entity.ncv65;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NCV65全面预算系统 - 预算公式实体类
 * 
 * @description 预算公式管理实体，支持预算计算公式的定义和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("NCV65_BUDGET_FORMULA")
public class BudgetFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 公式编码
     */
    @TableField("FORMULA_CODE")
    private String formulaCode;

    /**
     * 公式名称
     */
    @TableField("FORMULA_NAME")
    private String formulaName;

    /**
     * 公式类型：CALCULATION-计算公式，ALLOCATION-分配公式，FORECAST-预测公式，ANALYSIS-分析公式
     */
    @TableField("FORMULA_TYPE")
    private String formulaType;

    /**
     * 公式分类：REVENUE-收入类，EXPENSE-支出类，ASSET-资产类，LIABILITY-负债类
     */
    @TableField("FORMULA_CATEGORY")
    private String formulaCategory;

    /**
     * 公式级别：BASIC-基础公式，ADVANCED-高级公式，CUSTOM-自定义公式
     */
    @TableField("FORMULA_LEVEL")
    private String formulaLevel;

    /**
     * 公式状态：ACTIVE-激活，INACTIVE-停用，DRAFT-草稿，TESTING-测试中
     */
    @TableField("FORMULA_STATUS")
    private String formulaStatus;

    /**
     * 公式版本
     */
    @TableField("FORMULA_VERSION")
    private String formulaVersion;

    /**
     * 适用组织ID
     */
    @TableField("APPLICABLE_ORGANIZATION_ID")
    private String applicableOrganizationId;

    /**
     * 适用科目ID
     */
    @TableField("APPLICABLE_ACCOUNT_ID")
    private String applicableAccountId;

    /**
     * 适用指标ID
     */
    @TableField("APPLICABLE_INDICATOR_ID")
    private String applicableIndicatorId;

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
     * 公式表达式
     */
    @TableField("FORMULA_EXPRESSION")
    private String formulaExpression;

    /**
     * 公式脚本
     */
    @TableField("FORMULA_SCRIPT")
    private String formulaScript;

    /**
     * 公式语言：JAVASCRIPT-JavaScript，GROOVY-Groovy，PYTHON-Python，SQL-SQL
     */
    @TableField("FORMULA_LANGUAGE")
    private String formulaLanguage;

    /**
     * 输入参数定义（JSON格式）
     */
    @TableField("INPUT_PARAMETERS")
    private String inputParameters;

    /**
     * 输出参数定义（JSON格式）
     */
    @TableField("OUTPUT_PARAMETERS")
    private String outputParameters;

    /**
     * 变量定义（JSON格式）
     */
    @TableField("VARIABLE_DEFINITIONS")
    private String variableDefinitions;

    /**
     * 常量定义（JSON格式）
     */
    @TableField("CONSTANT_DEFINITIONS")
    private String constantDefinitions;

    /**
     * 函数定义（JSON格式）
     */
    @TableField("FUNCTION_DEFINITIONS")
    private String functionDefinitions;

    /**
     * 依赖公式列表（JSON格式）
     */
    @TableField("DEPENDENT_FORMULAS")
    private String dependentFormulas;

    /**
     * 数据源配置（JSON格式）
     */
    @TableField("DATA_SOURCE_CONFIG")
    private String dataSourceConfig;

    /**
     * 计算规则（JSON格式）
     */
    @TableField("CALCULATION_RULES")
    private String calculationRules;

    /**
     * 验证规则（JSON格式）
     */
    @TableField("VALIDATION_RULES")
    private String validationRules;

    /**
     * 精度设置
     */
    @TableField("PRECISION_SETTINGS")
    private String precisionSettings;

    /**
     * 舍入规则：ROUND-四舍五入，CEIL-向上取整，FLOOR-向下取整
     */
    @TableField("ROUNDING_RULE")
    private String roundingRule;

    /**
     * 小数位数
     */
    @TableField("DECIMAL_PLACES")
    private Integer decimalPlaces;

    /**
     * 执行顺序
     */
    @TableField("EXECUTION_ORDER")
    private Integer executionOrder;

    /**
     * 是否自动执行
     */
    @TableField("IS_AUTO_EXECUTE")
    private Boolean isAutoExecute;

    /**
     * 执行频率：REAL_TIME-实时，DAILY-每日，WEEKLY-每周，MONTHLY-每月
     */
    @TableField("EXECUTION_FREQUENCY")
    private String executionFrequency;

    /**
     * 执行时间配置
     */
    @TableField("EXECUTION_TIME_CONFIG")
    private String executionTimeConfig;

    /**
     * 错误处理方式：STOP-停止执行，WARNING-警告继续，DEFAULT-使用默认值
     */
    @TableField("ERROR_HANDLING")
    private String errorHandling;

    /**
     * 默认值设置
     */
    @TableField("DEFAULT_VALUE_SETTINGS")
    private String defaultValueSettings;

    /**
     * 公式描述
     */
    @TableField("FORMULA_DESCRIPTION")
    private String formulaDescription;

    /**
     * 使用说明
     */
    @TableField("USAGE_INSTRUCTIONS")
    private String usageInstructions;

    /**
     * 计算示例
     */
    @TableField("CALCULATION_EXAMPLE")
    private String calculationExample;

    /**
     * 测试用例（JSON格式）
     */
    @TableField("TEST_CASES")
    private String testCases;

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
     * 发布人
     */
    @TableField("PUBLISHED_BY")
    private String publishedBy;

    /**
     * 发布时间
     */
    @TableField("PUBLISHED_TIME")
    private LocalDateTime publishedTime;

    /**
     * 最后执行时间
     */
    @TableField("LAST_EXECUTION_TIME")
    private LocalDateTime lastExecutionTime;

    /**
     * 执行次数
     */
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    /**
     * 成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 平均执行时间（毫秒）
     */
    @TableField("AVERAGE_EXECUTION_TIME")
    private Long averageExecutionTime;

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
     * 公式类型常量
     */
    public static final String FORMULA_TYPE_CALCULATION = "CALCULATION";
    public static final String FORMULA_TYPE_ALLOCATION = "ALLOCATION";
    public static final String FORMULA_TYPE_FORECAST = "FORECAST";
    public static final String FORMULA_TYPE_ANALYSIS = "ANALYSIS";

    /**
     * 公式分类常量
     */
    public static final String FORMULA_CATEGORY_REVENUE = "REVENUE";
    public static final String FORMULA_CATEGORY_EXPENSE = "EXPENSE";
    public static final String FORMULA_CATEGORY_ASSET = "ASSET";
    public static final String FORMULA_CATEGORY_LIABILITY = "LIABILITY";

    /**
     * 公式级别常量
     */
    public static final String FORMULA_LEVEL_BASIC = "BASIC";
    public static final String FORMULA_LEVEL_ADVANCED = "ADVANCED";
    public static final String FORMULA_LEVEL_CUSTOM = "CUSTOM";

    /**
     * 公式状态常量
     */
    public static final String FORMULA_STATUS_ACTIVE = "ACTIVE";
    public static final String FORMULA_STATUS_INACTIVE = "INACTIVE";
    public static final String FORMULA_STATUS_DRAFT = "DRAFT";
    public static final String FORMULA_STATUS_TESTING = "TESTING";

    /**
     * 适用范围常量
     */
    public static final String APPLICABLE_SCOPE_ALL = "ALL";
    public static final String APPLICABLE_SCOPE_SPECIFIC = "SPECIFIC";
    public static final String APPLICABLE_SCOPE_CONDITIONAL = "CONDITIONAL";

    /**
     * 公式语言常量
     */
    public static final String FORMULA_LANGUAGE_JAVASCRIPT = "JAVASCRIPT";
    public static final String FORMULA_LANGUAGE_GROOVY = "GROOVY";
    public static final String FORMULA_LANGUAGE_PYTHON = "PYTHON";
    public static final String FORMULA_LANGUAGE_SQL = "SQL";

    /**
     * 舍入规则常量
     */
    public static final String ROUNDING_RULE_ROUND = "ROUND";
    public static final String ROUNDING_RULE_CEIL = "CEIL";
    public static final String ROUNDING_RULE_FLOOR = "FLOOR";

    /**
     * 执行频率常量
     */
    public static final String EXECUTION_FREQUENCY_REAL_TIME = "REAL_TIME";
    public static final String EXECUTION_FREQUENCY_DAILY = "DAILY";
    public static final String EXECUTION_FREQUENCY_WEEKLY = "WEEKLY";
    public static final String EXECUTION_FREQUENCY_MONTHLY = "MONTHLY";

    /**
     * 错误处理方式常量
     */
    public static final String ERROR_HANDLING_STOP = "STOP";
    public static final String ERROR_HANDLING_WARNING = "WARNING";
    public static final String ERROR_HANDLING_DEFAULT = "DEFAULT";
}
