package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算执行实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_EXECUTION")
public class BudgetExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 执行ID (主键, VARCHAR(32))
     */
    @TableId(value = "EXECUTION_ID", type = IdType.ASSIGN_UUID)
    private String executionId;

    /**
     * 关联规则ID
     */
    @TableField("RULE_ID")
    private String ruleId;

    /**
     * 预算类型
     */
    @TableField("BUDGET_TYPE")
    @ExcelField(title = "预算类型", sort = 10, width = 3000)
    private String budgetType;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    @ExcelField(title = "业务类型", sort = 20, width = 3000)
    private String businessType;

    /**
     * 业务ID
     */
    @TableField("BUSINESS_ID")
    @ExcelField(title = "业务ID", sort = 30, width = 4000)
    private String businessId;

    /**
     * 金额
     */
    @TableField("AMOUNT")
    @ExcelField(title = "金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal amount;

    /**
     * 执行时间
     */
    @TableField("EXECUTION_TIME")
    @ExcelField(title = "执行时间", sort = 50, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date executionTime;

    /**
     * 执行人ID
     */
    @TableField("EXECUTION_USER")
    @ExcelField(title = "执行人ID", sort = 60, width = 3000)
    private String executionUser;

    /**
     * 执行人姓名
     */
    @TableField("EXECUTION_USER_NAME")
    @ExcelField(title = "执行人", sort = 70, width = 3000)
    private String executionUserName;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ExcelField(title = "备注", sort = 80, width = 6000)
    private String remark;

}

