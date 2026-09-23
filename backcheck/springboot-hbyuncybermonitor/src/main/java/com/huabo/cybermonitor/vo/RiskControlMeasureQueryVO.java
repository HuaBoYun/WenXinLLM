package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 风险控制措施查询参数VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskControlMeasureQueryVO extends BaseVo {

    private String controlMeasureId;
    private String enterpriseId;
    private String enterpriseName;
    private String measureName;
    private String measureType;
    private String measureDescription;
    private String measureStatus;
    private String implementationDepartment;
    private String responsiblePerson;
    private LocalDate plannedImplementationDateStart;
    private LocalDate plannedImplementationDateEnd;
    private LocalDate actualImplementationDateStart;
    private LocalDate actualImplementationDateEnd;
    private LocalDate expectedCompletionDateStart;
    private LocalDate expectedCompletionDateEnd;
    private LocalDate actualCompletionDateStart;
    private LocalDate actualCompletionDateEnd;
    private BigDecimal measureCost;
    private BigDecimal costMin;
    private BigDecimal costMax;
    private String effectivenessAssessment;
    private BigDecimal effectivenessScoreMin;
    private BigDecimal effectivenessScoreMax;
    private Integer executionProgress;
    private Integer progressMin;
    private Integer progressMax;
    private String associatedRiskId;
    private String associatedRiskName;
    private String riskMitigationLevel;
    private String measurePriority;
    private String reviewStatus;
    private String reviewer;
    private LocalDateTime reviewTimeStart;
    private LocalDateTime reviewTimeEnd;
    private String approver;
    private LocalDateTime approvalTimeStart;
    private LocalDateTime approvalTimeEnd;
    private LocalDateTime updateTimeStart;
    private LocalDateTime updateTimeEnd;
    private String keyword;
    private String orderBy;
    private String orderDirection;
}

