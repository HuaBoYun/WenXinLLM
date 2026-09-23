package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_BUDGET")
public class GzctEnterpriseBudget extends Model<GzctEnterpriseBudget> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("BUDGET_YEAR")
    private String budgetYear;

    @TableField("BUDGET_TYPE")
    private String budgetType;

    @TableField("CATEGORY")
    private String category;

    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    @TableField("EXECUTION_RATE")
    private BigDecimal executionRate;

    @TableField("STATUS")
    private String status;

    @TableField("APPROVE_TIME")
    private LocalDateTime approveTime;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
