package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_LEADER_EVALUATION")
public class GzctLeaderEvaluation extends Model<GzctLeaderEvaluation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("LEADER_ID")
    private String leaderId;

    @TableField("LEADER_NAME")
    private String leaderName;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ASSESSMENT_YEAR")
    private String assessmentYear;

    @TableField("ASSESSMENT_TYPE")
    private String assessmentType;

    @TableField("POLITICAL_SCORE")
    private BigDecimal politicalScore;

    @TableField("ECONOMIC_SCORE")
    private BigDecimal economicScore;

    @TableField("MANAGEMENT_SCORE")
    private BigDecimal managementScore;

    @TableField("INTEGRITY_SCORE")
    private BigDecimal integrityScore;

    @TableField("TOTAL_SCORE")
    private BigDecimal totalScore;

    @TableField("GRADE")
    private String grade;

    @TableField("EVALUATOR")
    private String evaluator;

    @TableField("EVALUATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime evaluateTime;

    // 前端表格展示字段: company (所属企业别名)
    @TableField(exist = false)
    private String company;

    // 前端表格展示字段: evaluationType (考核类型别名)
    @TableField(exist = false)
    private String evaluationType;

    // 前端表格展示字段: evaluationPeriod (考核周期)
    @TableField(exist = false)
    private String evaluationPeriod;

    // 前端表格展示字段: evaluationDate (考核时间别名)
    @TableField(exist = false)
    private String evaluationDate;

    // 前端表格展示字段: status (状态)
    @TableField(exist = false)
    private String status;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
