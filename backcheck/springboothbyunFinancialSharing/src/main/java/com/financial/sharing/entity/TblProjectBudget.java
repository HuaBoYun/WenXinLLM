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
 * 项目预算表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROJECT_BUDGET")
@ApiModel(value = "TblProjectBudget", description = "项目预算表")
public class TblProjectBudget implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BUDGET_ID")
    @ApiModelProperty(value = "预算ID")
    private String budgetId;

    @TableField("PROJECT_ID")
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @TableField("BUDGET_CODE")
    @ApiModelProperty(value = "预算编码")
    private String budgetCode;

    @TableField("BUDGET_NAME")
    @ApiModelProperty(value = "预算名称")
    private String budgetName;

    @TableField("BUDGET_TYPE")
    @ApiModelProperty(value = "预算类型")
    private String budgetType;

    @TableField("BUDGET_AMOUNT")
    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @TableField("USED_AMOUNT")
    @ApiModelProperty(value = "已使用金额")
    private BigDecimal usedAmount;

    @TableField("FROZEN_AMOUNT")
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal frozenAmount;

    @TableField("YEAR")
    @ApiModelProperty(value = "年份")
    private Integer year;

    @TableField("MONTH")
    @ApiModelProperty(value = "月份")
    private Integer month;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}

