package com.financial.sharing.business.entity;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预算主表实体类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BUDGET")
@ApiModel(value = "TblBudget", description = "预算主表")
public class TblBudget implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BUDGET_ID")
    @ApiModelProperty(value = "预算ID")
    private String budgetId;

    @TableField("BUDGET_CODE")
    @ApiModelProperty(value = "预算编号")
    private String budgetCode;

    @TableField("BUDGET_NAME")
    @ApiModelProperty(value = "预算名称")
    private String budgetName;

    @TableField("BUDGET_TYPE")
    @ApiModelProperty(value = "预算类型(OPERATING-经营预算,CAPITAL-资本预算,FINANCIAL-财务预算)")
    private String budgetType;

    @TableField("BUDGET_STATUS")
    @ApiModelProperty(value = "预算状态(DRAFT-草稿,SUBMITTED-已提交,APPROVING-审批中,APPROVED-已审批,REJECTED-已驳回,EXECUTING-执行中,CANCELLED-已取消)")
    private String budgetStatus;

    @TableField("BUDGET_YEAR")
    @ApiModelProperty(value = "预算年度")
    private String budgetYear;

    @TableField("BUDGET_PERIOD")
    @ApiModelProperty(value = "预算期间")
    private String budgetPeriod;

    @TableField("DEPARTMENT_ID")
    @ApiModelProperty(value = "部门ID")
    private String departmentId;

    @TableField("DEPARTMENT_NAME")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty(value = "总预算金额")
    private BigDecimal totalAmount;

    @TableField("EXECUTED_AMOUNT")
    @ApiModelProperty(value = "已执行金额")
    private BigDecimal executedAmount;

    @TableField("REMAINING_AMOUNT")
    @ApiModelProperty(value = "剩余预算金额")
    private BigDecimal remainingAmount;

    @TableField("APPLICANT_ID")
    @ApiModelProperty(value = "申请人ID")
    private String applicantId;

    @TableField("APPLICANT_NAME")
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @TableField("APPROVER_ID")
    @ApiModelProperty(value = "审批人ID")
    private String approverId;

    @TableField("APPROVER_NAME")
    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @TableField("APPROVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approveTime;

    @TableField("APPROVE_OPINION")
    @ApiModelProperty(value = "审批意见")
    private String approveOpinion;

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
