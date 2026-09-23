package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算场景分析实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_SCENARIO_ANALYSIS")
public class BudgetScenarioAnalysis {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ANALYSIS_CODE")
    private String analysisCode;

    @TableField("ANALYSIS_NAME")
    private String analysisName;

    @TableField("BUDGET_ID")
    private String budgetId;

    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    @TableField("ORGANIZATION_ID")
    private String organizationId;

    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    @TableField("SCENARIO_TYPE")
    private String scenarioType;

    @TableField("SCENARIO_DESCRIPTION")
    private String scenarioDescription;

    @TableField("SCENARIO_ASSUMPTIONS")
    private String scenarioAssumptions;

    @TableField("BASE_AMOUNT")
    private BigDecimal baseAmount;

    @TableField("SCENARIO_AMOUNT")
    private BigDecimal scenarioAmount;

    @TableField("DIFFERENCE_AMOUNT")
    private BigDecimal differenceAmount;

    @TableField("DIFFERENCE_RATE")
    private BigDecimal differenceRate;

    @TableField("KEY_VARIABLES")
    private String keyVariables;

    @TableField("INFLUENCING_FACTORS")
    private String influencingFactors;

    @TableField("PROBABILITY")
    private BigDecimal probability;

    @TableField("RISK_ASSESSMENT")
    private String riskAssessment;

    @TableField("RESPONSE_STRATEGY")
    private String responseStrategy;

    @TableField("ANALYSIS_DESCRIPTION")
    private String analysisDescription;

    @TableField("RECOMMENDED_ACTIONS")
    private String recommendedActions;

    @TableField("ANALYSIS_DIMENSION")
    private String analysisDimension;

    @TableField("ANALYSIS_STATUS")
    private String analysisStatus;

    @TableField("ANALYZED_BY")
    private String analyzedBy;

    @TableField("ANALYZED_TIME")
    private Date analyzedTime;

    @TableField("REVIEWED_BY")
    private String reviewedBy;

    @TableField("REVIEWED_TIME")
    private Date reviewedTime;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;
}

