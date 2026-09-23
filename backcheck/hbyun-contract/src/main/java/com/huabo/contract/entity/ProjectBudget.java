package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目预算表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_budget")
public class ProjectBudget implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预算编号
     */
    @TableField("budget_no")
    private String budgetNo;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 预算名称
     */
    @TableField("budget_name")
    private String budgetName;

    /**
     * 预算类型(1:初步预算,2:详细预算,3:执行预算,4:调整预算)
     */
    @TableField("budget_type")
    private Integer budgetType;

    /**
     * 预算版本
     */
    @TableField("budget_version")
    private String budgetVersion;

    /**
     * 预算总金额
     */
    @TableField("total_budget")
    private BigDecimal totalBudget;

    /**
     * 预算状态(1:草稿,2:待审核,3:已审核,4:已批准,5:已驳回)
     */
    @TableField("budget_status")
    private Integer budgetStatus;

    /**
     * 预算期间开始日期
     */
    @TableField("budget_period_start")
    private Date budgetPeriodStart;

    /**
     * 预算期间结束日期
     */
    @TableField("budget_period_end")
    private Date budgetPeriodEnd;

    /**
     * 预算依据
     */
    @TableField("budget_basis")
    private String budgetBasis;

    /**
     * 预算假设
     */
    @TableField("budget_assumptions")
    private String budgetAssumptions;

    /**
     * 风险应急费率(%)
     */
    @TableField("risk_contingency_rate")
    private BigDecimal riskContingencyRate;

    /**
     * 风险应急费金额
     */
    @TableField("risk_contingency_amount")
    private BigDecimal riskContingencyAmount;

    /**
     * 管理费率(%)
     */
    @TableField("management_fee_rate")
    private BigDecimal managementFeeRate;

    /**
     * 管理费金额
     */
    @TableField("management_fee_amount")
    private BigDecimal managementFeeAmount;

    /**
     * 利润率(%)
     */
    @TableField("profit_rate")
    private BigDecimal profitRate;

    /**
     * 利润金额
     */
    @TableField("profit_amount")
    private BigDecimal profitAmount;

    /**
     * 税率(%)
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;

    /**
     * 税金
     */
    @TableField("tax_amount")
    private BigDecimal taxAmount;

    /**
     * 编制人ID
     */
    @TableField("budgeter_id")
    private Long budgeterId;

    /**
     * 编制日期
     */
    @TableField("budget_date")
    private Date budgetDate;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核日期
     */
    @TableField("review_date")
    private Date reviewDate;

    /**
     * 审核意见
     */
    @TableField("review_comments")
    private String reviewComments;

    /**
     * 批准人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 批准日期
     */
    @TableField("approval_date")
    private Date approvalDate;

    /**
     * 批准意见
     */
    @TableField("approval_comments")
    private String approvalComments;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 获取预算类型名称
     */
    public String getBudgetTypeName() {
        if (budgetType == null) {
            return "";
        }
        switch (budgetType) {
            case 1:
                return "初步预算";
            case 2:
                return "详细预算";
            case 3:
                return "执行预算";
            case 4:
                return "调整预算";
            default:
                return "未知";
        }
    }

    /**
     * 获取预算状态名称
     */
    public String getBudgetStatusName() {
        if (budgetStatus == null) {
            return "";
        }
        switch (budgetStatus) {
            case 1:
                return "草稿";
            case 2:
                return "待审核";
            case 3:
                return "已审核";
            case 4:
                return "已批准";
            case 5:
                return "执行中";
            case 6:
                return "已完成";
            case 7:
                return "已作废";
            default:
                return "未知";
        }
    }

    /**
     * 获取预算状态颜色
     */
    public String getBudgetStatusColor() {
        if (budgetStatus == null) {
            return "#909399";
        }
        switch (budgetStatus) {
            case 1:
                return "#909399"; // 灰色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#409EFF"; // 蓝色
            case 4:
                return "#67C23A"; // 绿色
            case 5:
                return "#67C23A"; // 绿色
            case 6:
                return "#67C23A"; // 绿色
            case 7:
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否可以编辑
     */
    public boolean canEdit() {
        return budgetStatus != null && (budgetStatus == 1 || budgetStatus == 3);
    }

    /**
     * 判断是否可以审核
     */
    public boolean canReview() {
        return budgetStatus != null && budgetStatus == 2;
    }

    /**
     * 判断是否可以批准
     */
    public boolean canApprove() {
        return budgetStatus != null && budgetStatus == 3;
    }

    /**
     * 判断是否已批准
     */
    public boolean isApproved() {
        return budgetStatus != null && budgetStatus >= 4;
    }

    /**
     * 判断是否执行中
     */
    public boolean isInProgress() {
        return budgetStatus != null && budgetStatus == 5;
    }

    /**
     * 判断是否已完成
     */
    public boolean isCompleted() {
        return budgetStatus != null && budgetStatus == 6;
    }

    /**
     * 判断是否已作废
     */
    public boolean isVoided() {
        return budgetStatus != null && budgetStatus == 7;
    }

    /**
     * 获取预算总金额显示文本
     */
    public String getTotalBudgetText() {
        if (totalBudget == null) {
            return "未设定";
        }
        if (totalBudget.compareTo(new BigDecimal("10000")) >= 0) {
            return totalBudget.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return totalBudget.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 判断是否为大额预算（总金额>=1000万）
     */
    public boolean isLargeBudget() {
        return totalBudget != null && totalBudget.compareTo(new BigDecimal("10000000")) >= 0;
    }
}
