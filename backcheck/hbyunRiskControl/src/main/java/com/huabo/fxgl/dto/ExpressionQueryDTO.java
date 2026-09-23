package com.huabo.fxgl.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表达式查询DTO
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Data
@Schema(name="ExpressionQueryDTO对象", description="表达式查询参数")
public class ExpressionQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(name = "页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(name = "表达式名称")
    private String expressionName;

    @Schema(name = "表达式类型(LOGICAL/ARITHMETIC/COMPARISON/FUNCTION)")
    private String expressionType;

    @Schema(name = "表达式分类(FINANCIAL_AUDIT/RISK_CONTROL/COMPLIANCE_CHECK/DATA_VALIDATION)")
    private String expressionCategory;

    @Schema(name = "是否启用(Y/N)")
    private String isEnabled;

    @Schema(name = "是否模板(Y/N)")
    private String isTemplate;

    @Schema(name = "创建人")
    private String createUser;

    @Schema(name = "开始时间")
    private String startTime;

    @Schema(name = "结束时间")
    private String endTime;
}
