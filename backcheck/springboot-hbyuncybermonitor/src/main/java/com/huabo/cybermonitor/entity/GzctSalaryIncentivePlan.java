package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_INCENTIVE_PLAN")
public class GzctSalaryIncentivePlan extends Model<GzctSalaryIncentivePlan> {

    @TableId(value = "PLAN_ID", type = IdType.ASSIGN_UUID)
    private String planId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("PLAN_NAME")
    private String planName;

    @TableField("PLAN_TYPE")
    private String planType;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("PARTICIPANT_COUNT")
    private Integer participantCount;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("PROGRESS_STATUS")
    private String progressStatus;

    @TableField("STATUS")
    private String status;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("IS_COMPLIANT")
    private String isCompliant;

    @TableField("COMPLIANT_ISSUE")
    private String compliantIssue;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
