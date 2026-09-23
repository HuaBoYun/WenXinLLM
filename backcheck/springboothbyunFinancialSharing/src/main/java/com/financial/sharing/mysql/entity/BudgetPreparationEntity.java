package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算编制实体类
 * 对应表：TBL_BUDGET_PREPARATION
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_BUDGET_PREPARATION")
public class BudgetPreparationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预算ID
     */
    @TableId(value = "BUDGET_ID", type = IdType.ASSIGN_ID)
    private String budgetId;

    /**
     * 预算编号
     */
    @TableField("BUDGET_NO")
    private String budgetNo;

    /**
     * 预算名称
     */
    @TableField("BUDGET_NAME")
    private String budgetName;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private String budgetYear;

    /**
     * 预算类型（ANNUAL-年度预算, QUARTERLY-季度预算, MONTHLY-月度预算）
     */
    @TableField("BUDGET_TYPE")
    private String budgetType;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 成本中心ID
     */
    @TableField("COST_CENTER_ID")
    private String costCenterId;

    /**
     * 部门ID
     */
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 编制人ID
     */
    @TableField("PREPARER_ID")
    private String preparerId;

    /**
     * 编制人姓名
     */
    @TableField("PREPARER_NAME")
    private String preparerName;

    /**
     * 编制日期
     */
    @TableField("PREPARATION_DATE")
    private Date preparationDate;

    /**
     * 审批状态（0-草稿, 1-待审批, 2-审批中, 3-已审批, 4-已驳回）
     */
    @TableField("APPROVAL_STATUS")
    private Integer approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("APPROVER_ID")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 审批意见
     */
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

