package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效评价实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_PERFORMANCE")
public class GzctStrategyPerformance extends Model<GzctStrategyPerformance> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("EVALUATION_NO")
    private String evaluationNo;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("EVALUATION_TYPE")
    private String evaluationType;

    @TableField("EVALUATOR")
    private String evaluator;

    @TableField("EVALUATION_DATE")
    private String evaluationDate;

    @TableField("TARGET_COMPLETION")
    private Integer targetCompletion;

    @TableField("KPI_SCORE")
    private BigDecimal kpiScore;

    @TableField("FINANCIAL_PERFORMANCE")
    private Integer financialPerformance;

    @TableField("OPERATIONAL_EFFICIENCY")
    private Integer operationalEfficiency;

    @TableField("CUSTOMER_SATISFACTION")
    private Integer customerSatisfaction;

    @TableField("INNOVATION_INDEX")
    private BigDecimal innovationIndex;

    @TableField("OVERALL_RATING")
    private String overallRating;

    @TableField("IMPROVEMENT_SUGGESTIONS")
    private String improvementSuggestions;

    @TableField("STATUS")
    private String status;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
