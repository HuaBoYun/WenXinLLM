package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业风险等级评估实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_RISK_LEVEL")
@Schema(name="EnterpriseRiskLevel", description="企业风险等级评估")
public class EnterpriseRiskLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="风险等级ID")
    @TableId(value = "RISK_LEVEL_ID", type = IdType.ASSIGN_ID)
    private String riskLevelId;

    @Schema(name="企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name="风险等级(1-低风险,2-中风险,3-高风险,4-极高风险)")
    @TableField("RISK_LEVEL")
    private String riskLevel;

    @Schema(name="风险评分")
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    @Schema(name="评估日期")
    @TableField("EVALUATION_DATE")
    private LocalDate evaluationDate;

    @Schema(name="评估周期(MONTHLY-月度,QUARTERLY-季度,YEARLY-年度)")
    @TableField("EVALUATION_PERIOD")
    private String evaluationPeriod;

    @Schema(name="风险因素说明")
    @TableField("RISK_FACTORS")
    private String riskFactors;

    @Schema(name="评估方法")
    @TableField("EVALUATION_METHOD")
    private String evaluationMethod;

    @Schema(name="是否当前有效(Y/N)")
    @TableField("IS_CURRENT")
    private String isCurrent;

    @Schema(name="创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name="创建人")
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;
}
