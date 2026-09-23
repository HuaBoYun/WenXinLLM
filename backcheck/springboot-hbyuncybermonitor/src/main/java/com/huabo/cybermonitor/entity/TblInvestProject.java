package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 投资项目台账实体 - 投资穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVEST_PROJECT")
public class TblInvestProject {

    @TableId(value = "PROJECT_ID", type = IdType.ASSIGN_UUID)
    private String projectId;

    @TableField("PROJECT_CODE")
    private String projectCode;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("INVEST_TYPE")
    private String investType;

    @TableField("INVEST_CATEGORY")
    private String investCategory;

    @TableField("INVEST_AMOUNT")
    private BigDecimal investAmount;

    @TableField("INVEST_METHOD")
    private String investMethod;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("REGION")
    private String region;

    @TableField("DECISION_LEVEL")
    private String decisionLevel;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("PROJECT_STATUS")
    private String projectStatus;

    @TableField("EXPECTED_RETURN")
    private BigDecimal expectedReturn;

    @TableField("ACTUAL_RETURN")
    private BigDecimal actualReturn;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("PLAN_PROGRESS")
    private BigDecimal planProgress;

    @TableField("ACTUAL_PROGRESS")
    private BigDecimal actualProgress;

    @TableField("IMPAIRMENT_AMOUNT")
    private BigDecimal impairmentAmount;

    @TableField("POST_EVAL_STATUS")
    private String postEvalStatus;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

