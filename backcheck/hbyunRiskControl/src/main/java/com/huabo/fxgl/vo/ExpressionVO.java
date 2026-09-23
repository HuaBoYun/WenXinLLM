package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表达式VO
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Data
@Schema(name="ExpressionVO对象", description="表达式视图对象")
public class ExpressionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "表达式ID")
    private String expressionId;

    @Schema(name = "表达式编码")
    private String expressionCode;

    @Schema(name = "表达式名称")
    private String expressionName;

    @Schema(name = "表达式类型")
    private String expressionType;

    @Schema(name = "表达式类型名称")
    private String expressionTypeName;

    @Schema(name = "表达式分类")
    private String expressionCategory;

    @Schema(name = "表达式分类名称")
    private String expressionCategoryName;

    @Schema(name = "输出类型")
    private String outputType;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "表达式内容")
    private String expressionContent;

    @Schema(name = "是否启用")
    private String isEnabled;

    @Schema(name = "是否启用名称")
    private String isEnabledName;

    @Schema(name = "是否模板")
    private String isTemplate;

    @Schema(name = "是否模板名称")
    private String isTemplateName;

    @Schema(name = "复杂度等级")
    private Integer complexityLevel;

    @Schema(name = "复杂度等级名称")
    private String complexityLevelName;

    @Schema(name = "标签")
    private String tags;

    @Schema(name = "源数据配置")
    private String sourceDataConfig;

    @Schema(name = "目标数据配置")
    private String targetDataConfig;

    @Schema(name = "比对规则配置")
    private String comparisonRules;

    @Schema(name = "执行次数")
    private Integer executionCount;

    @Schema(name = "成功次数")
    private Integer successCount;

    @Schema(name = "成功率")
    private String successRate;

    @Schema(name = "最后执行时间")
    private Date lastExecutionTime;

    @Schema(name = "最后执行结果")
    private String lastExecutionResult;

    @Schema(name = "最后执行结果名称")
    private String lastExecutionResultName;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "更新人")
    private String updateUser;

    @Schema(name = "更新时间")
    private Date updateTime;
}
