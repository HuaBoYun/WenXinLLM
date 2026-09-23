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
@TableName("GZCT_ENTERPRISE_RISK_ASSESSMENT")
public class GzctEnterpriseRiskAssessment extends Model<GzctEnterpriseRiskAssessment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("RISK_NAME")
    private String riskName;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("RISK_SOURCE")
    private String riskSource;

    @TableField("PROBABILITY")
    private String probability;

    @TableField("IMPACT")
    private String impact;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    @TableField("CONTROL_MEASURES")
    private String controlMeasures;

    @TableField("STATUS")
    private String status;

    @TableField("ASSESSOR")
    private String assessor;

    @TableField("ASSESS_DATE")
    private LocalDate assessDate;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
