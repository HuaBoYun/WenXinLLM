package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_COMPLIANCE")
public class GzctSalaryCompliance extends Model<GzctSalaryCompliance> {

    @TableId(value = "CHECK_ID", type = IdType.ASSIGN_UUID)
    private String checkId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("CHECK_YEAR")
    private String checkYear;

    @TableField("CHECK_TYPE")
    private String checkType;

    @TableField("CHECK_RESULT")
    private String checkResult;

    @TableField("ISSUE_DESC")
    private String issueDesc;

    @TableField("RECTIFICATION")
    private String rectification;

    @TableField("IS_COMPLIANT")
    private String isCompliant;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RECT_STATUS")
    private String rectStatus;

    @TableField("RECT_DEADLINE")
    private LocalDate rectDeadline;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
