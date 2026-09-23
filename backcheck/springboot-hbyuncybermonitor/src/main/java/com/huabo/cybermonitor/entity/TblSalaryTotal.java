package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工资总额台账实体 - 薪酬分配穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SALARY_TOTAL")
public class TblSalaryTotal {

    @TableId(value = "SALARY_ID", type = IdType.ASSIGN_UUID)
    private String salaryId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("REPORT_MONTH")
    private String reportMonth;

    @TableField("BUDGET_TOTAL")
    private BigDecimal budgetTotal;

    @TableField("ACTUAL_TOTAL")
    private BigDecimal actualTotal;

    @TableField("EMPLOYEE_COUNT")
    private Integer employeeCount;

    @TableField("AVG_SALARY")
    private BigDecimal avgSalary;

    @TableField("EXEC_BASE_SALARY")
    private BigDecimal execBaseSalary;

    @TableField("EXEC_PERF_SALARY")
    private BigDecimal execPerfSalary;

    @TableField("EXEC_TERM_INCENTIVE")
    private BigDecimal execTermIncentive;

    @TableField("REVENUE")
    private BigDecimal revenue;

    @TableField("NET_PROFIT")
    private BigDecimal netProfit;

    @TableField("LABOR_PRODUCTIVITY")
    private BigDecimal laborProductivity;

    @TableField("LABOR_COST_RATE")
    private BigDecimal laborCostRate;

    @TableField("WAGE_GROWTH_RATE")
    private BigDecimal wageGrowthRate;

    @TableField("PROFIT_GROWTH_RATE")
    private BigDecimal profitGrowthRate;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

