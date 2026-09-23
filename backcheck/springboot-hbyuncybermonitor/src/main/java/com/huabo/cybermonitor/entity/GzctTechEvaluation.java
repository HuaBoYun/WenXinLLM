package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TECH_EVALUATION")
public class GzctTechEvaluation extends Model<GzctTechEvaluation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("EVALUATION_NO")
    private String evaluationNo;

    @TableField("TECH_NAME")
    private String techName;

    @TableField("EVALUATION_TYPE")
    private String evaluationType;

    @TableField("APPLICANT")
    private String applicant;

    @TableField("EVALUATOR")
    private String evaluator;

    @TableField("EVALUATION_DATE")
    private LocalDate evaluationDate;

    @TableField("TECH_MATURITY")
    private Integer techMaturity;

    @TableField("MARKET_POTENTIAL")
    private Integer marketPotential;

    @TableField("COMMERCIAL_VALUE")
    private BigDecimal commercialValue;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("OVERALL_SCORE")
    private BigDecimal overallScore;

    @TableField("RECOMMENDATION")
    private String recommendation;

    @TableField("STATUS")
    private String status;

    @TableField("COMPLETION_DATE")
    private LocalDate completionDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
