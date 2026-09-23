package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算执行记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BUDGET_EXECUTION")
@ApiModel(value = "TblBudgetExecution", description = "预算执行记录表")
public class TblBudgetExecution implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "EXECUTION_ID")
    @ApiModelProperty(value = "执行ID")
    private String executionId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("BUDGET_TYPE")
    @ApiModelProperty(value = "预算类型")
    private String budgetType;

    @TableField("BUDGET_ID")
    @ApiModelProperty(value = "预算ID")
    private String budgetId;

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @TableField("BUSINESS_ID")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @TableField("AMOUNT")
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @TableField("EXECUTION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行时间")
    private LocalDateTime executionTime;

    @TableField("EXECUTION_USER")
    @ApiModelProperty(value = "执行人ID")
    private String executionUser;

    @TableField("EXECUTION_USER_NAME")
    @ApiModelProperty(value = "执行人姓名")
    private String executionUserName;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
