package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_EXPENSE")
public class GzctFinExpense {

    @TableId(value = "EXPENSE_ID", type = IdType.ASSIGN_UUID)
    private String expenseId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("EXPENSE_CATEGORY")
    private String expenseCategory;

    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    @TableField("EXECUTION_RATE")
    private BigDecimal executionRate;

    @TableField("PERIOD")
    private String period;

    @TableField("IS_ABNORMAL")
    private String isAbnormal;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

