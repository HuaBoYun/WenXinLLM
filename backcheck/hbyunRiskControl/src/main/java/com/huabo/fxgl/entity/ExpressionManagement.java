package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表达式管理实体类
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_EXPRESSION_MANAGEMENT")
@Schema(name="ExpressionManagement对象", description="表达式管理")
public class ExpressionManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "表达式ID")
    @TableId(value = "EXPRESSION_ID", type = IdType.ASSIGN_ID)
    private String expressionId;

    @Schema(name = "表达式编码")
    @TableField("EXPRESSION_CODE")
    private String expressionCode;

    @Schema(name = "表达式名称")
    @TableField("EXPRESSION_NAME")
    private String expressionName;

    @Schema(name = "表达式类型(LOGICAL/ARITHMETIC/COMPARISON/FUNCTION)")
    @TableField("EXPRESSION_TYPE")
    private String expressionType;

    @Schema(name = "表达式分类(FINANCIAL_AUDIT/RISK_CONTROL/COMPLIANCE_CHECK/DATA_VALIDATION)")
    @TableField("EXPRESSION_CATEGORY")
    private String expressionCategory;

    @Schema(name = "输出类型(BOOLEAN/NUMBER/STRING/DATE)")
    @TableField("OUTPUT_TYPE")
    private String outputType;

    @Schema(name = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name = "表达式内容")
    @TableField("EXPRESSION_CONTENT")
    private String expressionContent;

    @Schema(name = "是否启用(Y/N)")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @Schema(name = "是否模板(Y/N)")
    @TableField("IS_TEMPLATE")
    private String isTemplate;

    @Schema(name = "复杂度等级(1-5)")
    @TableField("COMPLEXITY_LEVEL")
    private Integer complexityLevel;

    @Schema(name = "标签(JSON数组格式)")
    @TableField("TAGS")
    private String tags;

    @Schema(name = "源数据配置(JSON格式)")
    @TableField("SOURCE_DATA_CONFIG")
    private String sourceDataConfig;

    @Schema(name = "目标数据配置(JSON格式)")
    @TableField("TARGET_DATA_CONFIG")
    private String targetDataConfig;

    @Schema(name = "比对规则配置(JSON格式)")
    @TableField("COMPARISON_RULES")
    private String comparisonRules;

    @Schema(name = "执行次数")
    @TableField("EXECUTION_COUNT")
    private Integer executionCount;

    @Schema(name = "成功次数")
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    @Schema(name = "最后执行时间")
    @TableField("LAST_EXECUTION_TIME")
    private Date lastExecutionTime;

    @Schema(name = "最后执行结果(SUCCESS/FAILED)")
    @TableField("LAST_EXECUTION_RESULT")
    private String lastExecutionResult;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
