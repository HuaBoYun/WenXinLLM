package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_HR_COMPENSATION")
public class GzctEnterpriseHrCompensation extends Model<GzctEnterpriseHrCompensation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("EMPLOYEE_ID")
    private String employeeId;

    @TableField("EMPLOYEE_NAME")
    private String employeeName;

    @TableField("EMPLOYEE_NO")
    private String employeeNo;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("POSITION")
    private String position;

    @TableField("LEVEL")
    private String level;

    @TableField("BASE_SALARY")
    private BigDecimal baseSalary;

    @TableField("PERFORMANCE_BONUS")
    private BigDecimal performanceBonus;

    @TableField("ALLOWANCE")
    private BigDecimal allowance;

    @TableField("TOTAL_SALARY")
    private BigDecimal totalSalary;

    @TableField("SOCIAL_INSURANCE")
    private BigDecimal socialInsurance;

    @TableField("HOUSING_FUND")
    private BigDecimal housingFund;

    @TableField("LAST_ADJUST_DATE")
    private LocalDate lastAdjustDate;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
