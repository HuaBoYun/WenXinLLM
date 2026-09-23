package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_PERFORMANCE_LINK")
public class GzctSalaryPerformanceLink extends Model<GzctSalaryPerformanceLink> {

    @TableId(value = "LINK_ID", type = IdType.ASSIGN_UUID)
    private String linkId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("WAGE_GROWTH_RATE")
    private BigDecimal wageGrowthRate;

    @TableField("PROFIT_GROWTH_RATE")
    private BigDecimal profitGrowthRate;

    @TableField("LINK_COEFFICIENT")
    private BigDecimal linkCoefficient;

    @TableField("IS_REASONABLE")
    private String isReasonable;

    @TableField("REVENUE_GROWTH_RATE")
    private BigDecimal revenueGrowthRate;

    @TableField("LABOR_PRODUCTIVITY_RATE")
    private BigDecimal laborProductivityRate;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
