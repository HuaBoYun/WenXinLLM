package com.huabo.fxgl.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表达式保存DTO
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Data
@Schema(name="ExpressionSaveDTO对象", description="表达式保存参数")
public class ExpressionSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "表达式ID(更新时必填)")
    private String expressionId;

    @Schema(name = "表达式编码", required = true)
    private String expressionCode;

    @Schema(name = "表达式名称", required = true)
    private String expressionName;

    @Schema(name = "表达式类型", required = true)
    private String expressionType;

    @Schema(name = "表达式分类", required = true)
    private String expressionCategory;

    @Schema(name = "输出类型")
    private String outputType;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "表达式内容", required = true)
    private String expressionContent;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "是否模板(Y/N)")
    private String isTemplate;

    @Schema(name = "复杂度等级(1-5)")
    private Integer complexityLevel;

    @Schema(name = "标签(JSON数组格式)")
    private String tags;

    @Schema(name = "源数据配置(JSON格式)")
    private String sourceDataConfig;

    @Schema(name = "目标数据配置(JSON格式)")
    private String targetDataConfig;

    @Schema(name = "比对规则配置(JSON格式)")
    private String comparisonRules;
}
