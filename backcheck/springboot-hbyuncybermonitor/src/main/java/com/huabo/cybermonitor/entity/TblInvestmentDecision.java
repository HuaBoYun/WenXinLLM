package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 投资决策实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVESTMENT_DECISION")
public class TblInvestmentDecision {

    @TableId(value = "DECISION_ID", type = IdType.ASSIGN_UUID)
    private String decisionId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("INVESTMENT_TYPE")
    private String investmentType;

    @TableField("INVESTMENT_AMOUNT")
    private BigDecimal investmentAmount;

    @TableField("EXPECTED_RETURN")
    private BigDecimal expectedReturn;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("DECISION_STATUS")
    private String decisionStatus;

    @TableField("DECISION_DATE")
    private LocalDate decisionDate;

    @TableField("DECISION_MAKER")
    private String decisionMaker;

    @TableField("APPROVAL_LEVEL")
    private String approvalLevel;

    @TableField("FEASIBILITY_SCORE")
    private BigDecimal feasibilityScore;

    @TableField("STRATEGIC_FIT")
    private String strategicFit;

    @TableField("MARKET_ANALYSIS")
    private String marketAnalysis;

    @TableField("RISK_ANALYSIS")
    private String riskAnalysis;

    @TableField("CONCLUSION")
    private String conclusion;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
