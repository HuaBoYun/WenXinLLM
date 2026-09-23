package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PERFORMANCE_ASSESSMENT")
public class GzctPerformanceAssessment extends Model<GzctPerformanceAssessment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("EVALUATION_NAME")
    private String evaluationName;

    @TableField("EVALUATION_TYPE")
    private String evaluationType;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("EVALUATEE")
    private String evaluatee;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("RATING")
    private String rating;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("EVALUATOR")
    private String evaluator;

    @TableField("EVALUATION_DATE")
    private String evaluationDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
