package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_EXECUTIVE")
public class GzctSalaryExecutive extends Model<GzctSalaryExecutive> {

    @TableId(value = "EXEC_ID", type = IdType.ASSIGN_UUID)
    private String execId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("EXEC_NAME")
    private String execName;

    @TableField("EXEC_POSITION")
    private String execPosition;

    @TableField("BASE_SALARY")
    private BigDecimal baseSalary;

    @TableField("PERF_SALARY")
    private BigDecimal perfSalary;

    @TableField("TERM_INCENTIVE")
    private BigDecimal termIncentive;

    @TableField("OTHER_INCOME")
    private BigDecimal otherIncome;

    @TableField("TOTAL_COMP")
    private BigDecimal totalComp;

    @TableField("EMP_AVG_SALARY")
    private BigDecimal empAvgSalary;

    @TableField("RATIO_TO_AVG")
    private BigDecimal ratioToAvg;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("IS_COMPLIANT")
    private String isCompliant;

    @TableField("COMPLIANT_ISSUE")
    private String compliantIssue;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
