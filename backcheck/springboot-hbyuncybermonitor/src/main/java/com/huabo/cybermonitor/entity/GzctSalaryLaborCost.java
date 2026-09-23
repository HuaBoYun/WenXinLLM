package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALARY_LABOR_COST")
public class GzctSalaryLaborCost extends Model<GzctSalaryLaborCost> {

    @TableId(value = "COST_ID", type = IdType.ASSIGN_UUID)
    private String costId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("TOTAL_LABOR_COST")
    private BigDecimal totalLaborCost;

    @TableField("SALARY_COST")
    private BigDecimal salaryCost;

    @TableField("SOCIAL_COST")
    private BigDecimal socialCost;

    @TableField("WELFARE_COST")
    private BigDecimal welfareCost;

    @TableField("TRAINING_COST")
    private BigDecimal trainingCost;

    @TableField("OTHER_COST")
    private BigDecimal otherCost;

    @TableField("LABOR_COST_RATE")
    private BigDecimal laborCostRate;

    @TableField("LABOR_PRODUCTIVITY")
    private BigDecimal laborProductivity;

    @TableField("HOUSING_FUND")
    private BigDecimal housingFund;

    @TableField("YOY_CHANGE")
    private BigDecimal yoyChange;

    @TableField("PER_CAPITA_COST")
    private BigDecimal perCapitaCost;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
