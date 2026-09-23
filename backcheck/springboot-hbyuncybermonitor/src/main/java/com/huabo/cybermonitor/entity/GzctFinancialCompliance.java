package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务合规检查实体
 * @author huabo
 * @date 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCIAL_COMPLIANCE")
public class GzctFinancialCompliance {
    @TableId(value = "COMPLIANCE_ID", type = IdType.ASSIGN_UUID)
    private String complianceId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("CHECK_TYPE")
    private String checkType;

    @TableField("COMPLIANCE_STATUS")
    private String complianceStatus;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("COMPLIANCE_SCORE")
    private BigDecimal complianceScore;

    @TableField("VIOLATION_TYPE")
    private String violationType;

    @TableField("CHECK_METHOD")
    private String checkMethod;

    @TableField("CHECK_SCOPE")
    private String checkScope;

    @TableField("INSPECTOR")
    private String inspector;

    @TableField("CHECK_DATE")
    private LocalDateTime checkDate;

    @TableField("CHECK_DATE_START")
    private LocalDateTime checkDateStart;

    @TableField("CHECK_DATE_END")
    private LocalDateTime checkDateEnd;

    @TableField("PERIOD")
    private String period;

    @TableField("AUDIT_STATUS")
    private String auditStatus;

    @TableField("ISSUES")
    private String issues;

    @TableField("RECTIFICATION_REQUIREMENTS")
    private String rectificationRequirements;

    @TableField("RECTIFICATION_DEADLINE")
    private LocalDateTime rectificationDeadline;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("VIOLATION_NATURE")
    private String violationNature;

    @TableField("VIOLATION_DESCRIPTION")
    private String violationDescription;

    @TableField("INVOLVED_AMOUNT")
    private BigDecimal involvedAmount;

    @TableField("HANDLING_DECISION")
    private String handlingDecision;

    @TableField("RESPONSIBLE_PARTY")
    private String responsibleParty;

    @TableField("DISCOVERY_METHOD")
    private String discoveryMethod;

    @TableField("EXECUTION_STATUS")
    private String executionStatus;

    @TableField("COMPLETION_PROGRESS")
    private Integer completionProgress;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
