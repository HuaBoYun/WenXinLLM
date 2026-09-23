package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_HR_PERFORMANCE")
public class GzctEnterpriseHrPerformance extends Model<GzctEnterpriseHrPerformance> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("EMPLOYEE_ID")
    private String employeeId;

    @TableField("EMPLOYEE_NAME")
    private String employeeName;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("POSITION")
    private String position;

    @TableField("ASSESSMENT_YEAR")
    private String assessmentYear;

    @TableField("ASSESSMENT_PERIOD")
    private String assessmentPeriod;

    @TableField("WORK_PERFORMANCE")
    private BigDecimal workPerformance;

    @TableField("ABILITY_EVALUATION")
    private BigDecimal abilityEvaluation;

    @TableField("ATTITUDE_EVALUATION")
    private BigDecimal attitudeEvaluation;

    @TableField("TOTAL_SCORE")
    private BigDecimal totalScore;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("GRADE")
    private String grade;

    @TableField("EVALUATOR")
    private String evaluator;

    @TableField("EVALUATE_TIME")
    private LocalDateTime evaluateTime;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
