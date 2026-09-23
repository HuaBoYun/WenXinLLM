package com.huabo.finance.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 转换规则DTO
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@Schema(name="TransformRuleDTO", description="转换规则数据传输对象")
public class TransformRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "关联的映射索引(对应mappings数组的索引)")
    private Integer mappingIndex;

    @Schema(name = "规则类型")
    private String ruleType;

    @Schema(name = "规则名称")
    private String ruleName;

    @Schema(name = "计算表达式")
    private String expression;

    @Schema(name = "参数配置(JSON格式)")
    private String params;

    @Schema(name = "条件表达式")
    private String conditionExpr;

    @Schema(name = "关联类型(LEFT/INNER/RIGHT)")
    private String joinType;

    @Schema(name = "关联表名")
    private String joinTable;

    @Schema(name = "关联条件")
    private String joinCondition;

    @Schema(name = "聚合函数(SUM/AVG/MAX/MIN/COUNT)")
    private String aggregateFunc;

    @Schema(name = "分组字段(逗号分隔)")
    private String groupByFields;

    @Schema(name = "排序序号")
    private Integer sortOrder;

    @Schema(name = "备注")
    private String remark;
}

