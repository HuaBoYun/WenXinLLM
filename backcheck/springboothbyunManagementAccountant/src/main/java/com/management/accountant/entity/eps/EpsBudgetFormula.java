package com.management.accountant.entity.eps;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算公式表
 * 对应表：tbl_eps_budget_formula
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_eps_budget_formula")
public class EpsBudgetFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公式ID，主键
     */
    @TableId(value = "formula_id", type = IdType.AUTO)
    private Long formulaId;

    /**
     * 所属预算体系ID
     */
    @TableField("system_id")
    private Long systemId;

    /**
     * 所属模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 公式编码，唯一标识
     */
    @TableField("formula_code")
    private String formulaCode;

    /**
     * 公式名称
     */
    @TableField("formula_name")
    private String formulaName;

    /**
     * 公式描述
     */
    @TableField("formula_description")
    private String formulaDescription;

    /**
     * 公式类型：CALCULATION-计算公式/VALIDATION-验证公式/ALLOCATION-分摊公式
     */
    @TableField("formula_type")
    private String formulaType;

    /**
     * 公式分类：ARITHMETIC-算术运算/STATISTICAL-统计函数/FINANCIAL-财务函数/LOGICAL-逻辑函数
     */
    @TableField("formula_category")
    private String formulaCategory;

    /**
     * 公式表达式
     */
    @TableField("formula_expression")
    private String formulaExpression;

    /**
     * 公式语法：EXCEL-Excel语法/SQL-SQL语法/JAVASCRIPT-JavaScript语法/PYTHON-Python语法
     */
    @TableField("formula_syntax")
    private String formulaSyntax;

    /**
     * 目标科目ID
     */
    @TableField("target_subject_id")
    private Long targetSubjectId;

    /**
     * 目标科目编码
     */
    @TableField("target_subject_code")
    private String targetSubjectCode;

    /**
     * 目标科目名称
     */
    @TableField("target_subject_name")
    private String targetSubjectName;

    /**
     * 依赖科目，JSON格式存储
     */
    @TableField("dependent_subjects")
    private String dependentSubjects;

    /**
     * 参数定义，JSON格式存储
     */
    @TableField("parameter_definitions")
    private String parameterDefinitions;

    /**
     * 变量定义，JSON格式存储
     */
    @TableField("variable_definitions")
    private String variableDefinitions;

    /**
     * 常量定义，JSON格式存储
     */
    @TableField("constant_definitions")
    private String constantDefinitions;

    /**
     * 执行顺序
     */
    @TableField("execution_order")
    private Integer executionOrder;

    /**
     * 执行条件
     */
    @TableField("execution_condition")
    private String executionCondition;

    /**
     * 执行频率：ONCE-一次性/PERIOD-按期间/REALTIME-实时
     */
    @TableField("execution_frequency")
    private String executionFrequency;

    /**
     * 是否自动执行：0-否/1-是
     */
    @TableField("auto_execute")
    private Integer autoExecute;

    /**
     * 错误处理方式：IGNORE-忽略/WARNING-警告/STOP-停止
     */
    @TableField("error_handling")
    private String errorHandling;

    /**
     * 精度设置
     */
    @TableField("precision_setting")
    private Integer precisionSetting;

    /**
     * 舍入规则：ROUND-四舍五入/CEIL-向上取整/FLOOR-向下取整
     */
    @TableField("rounding_rule")
    private String roundingRule;

    /**
     * 默认值
     */
    @TableField("default_value")
    private BigDecimal defaultValue;

    /**
     * 最小值
     */
    @TableField("min_value")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @TableField("max_value")
    private BigDecimal maxValue;

    /**
     * 验证规则，JSON格式存储
     */
    @TableField("validation_rules")
    private String validationRules;

    /**
     * 测试数据，JSON格式存储
     */
    @TableField("test_data")
    private String testData;

    /**
     * 测试结果，JSON格式存储
     */
    @TableField("test_result")
    private String testResult;

    /**
     * 最后测试时间
     */
    @TableField("last_test_time")
    private LocalDateTime lastTestTime;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

    /**
     * 最后使用时间
     */
    @TableField("last_used_time")
    private LocalDateTime lastUsedTime;

    /**
     * 性能统计，JSON格式存储
     */
    @TableField("performance_stats")
    private String performanceStats;

    /**
     * 是否系统公式：0-否/1-是
     */
    @TableField("is_system")
    private Integer isSystem;

    /**
     * 是否可编辑：0-否/1-是
     */
    @TableField("is_editable")
    private Integer isEditable;

    /**
     * 是否共享：0-否/1-是
     */
    @TableField("is_shared")
    private Integer isShared;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 标签，多个标签用逗号分隔
     */
    @TableField("tags")
    private String tags;

    /**
     * 状态：DRAFT-草稿/ACTIVE-激活/INACTIVE-停用/ARCHIVED-归档
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
