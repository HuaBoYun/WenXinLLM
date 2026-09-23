package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算冻结实体（对齐 TBL_BUDGET_FREEZE 表结构）
 */
@Data
@TableName("TBL_BUDGET_FREEZE")
public class BudgetFreeze implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 冻结ID（主键） */
    @TableId(value = "FREEZE_ID", type = IdType.ASSIGN_UUID)
    private String freezeId;

    /** 冻结编码 */
    @TableField("FREEZE_CODE")
    @ExcelField(title = "冻结编码", sort = 10, width = 4000)
    private String freezeCode;

    /** 冻结标题 */
    @TableField("FREEZE_TITLE")
    @ExcelField(title = "冻结标题", sort = 20, width = 6000)
    private String freezeTitle;

    /** 冻结类型 (FULL/PARTIAL/TEMPORARY) */
    @TableField("FREEZE_TYPE")
    @ExcelField(title = "冻结类型", sort = 30, width = 3000)
    private String freezeType;

    /** 预算ID */
    @TableField("BUDGET_ID")
    private String budgetId;

    /** 组织单元ID */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /** 组织单元名称 */
    @TableField("ORGANIZATION_NAME")
    @ExcelField(title = "组织单元", sort = 40, width = 4000)
    private String organizationName;

    /** 预算科目ID */
    @TableField("BUDGET_ACCOUNT_ID")
    private String budgetAccountId;

    /** 预算科目名称 */
    @TableField("BUDGET_ACCOUNT_NAME")
    @ExcelField(title = "预算科目", sort = 50, width = 4000)
    private String budgetAccountName;

    /** 冻结金额 */
    @TableField("FREEZE_AMOUNT")
    @ExcelField(title = "冻结金额", sort = 60, width = 4000, dataFormat = "#,##0.00")
    private BigDecimal freezeAmount;

    /** 剩余冻结金额 */
    @TableField("REMAIN_AMOUNT")
    private BigDecimal remainAmount;

    /** 已释放金额 */
    @TableField("RELEASED_AMOUNT")
    private BigDecimal releasedAmount;

    /** 冻结原因 */
    @TableField("FREEZE_REASON")
    @ExcelField(title = "冻结原因", sort = 70, width = 6000)
    private String freezeReason;

    /** 冻结说明 */
    @TableField("FREEZE_DESCRIPTION")
    private String freezeDescription;

    /** 冻结状态 (FROZEN/PARTIAL_RELEASED/RELEASED) */
    @TableField("FREEZE_STATUS")
    @ExcelField(title = "冻结状态", sort = 80, width = 3000)
    private String freezeStatus;

    /** 审批状态 (PENDING/APPROVED/REJECTED) */
    @TableField("APPROVAL_STATUS")
    @ExcelField(title = "审批状态", sort = 90, width = 3000)
    private String approvalStatus;

    /** 申请人 */
    @TableField("APPLICANT")
    @ExcelField(title = "申请人", sort = 100, width = 3000)
    private String applicant;

    /** 申请日期 */
    @TableField("APPLY_DATE")
    @ExcelField(title = "申请日期", sort = 110, width = 4000, dataFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date applyDate;

    /** 冻结日期 */
    @TableField("FREEZE_DATE")
    @ExcelField(title = "冻结日期", sort = 120, width = 4000, dataFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date freezeDate;

    /** 计划解冻日期 */
    @TableField("PLANNED_UNFREEZE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedUnfreezeDate;

    /** 冻结人ID */
    @TableField("FREEZE_BY")
    private String freezeBy;

    /** 冻结时间 */
    @TableField("FREEZE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;

    /** 释放人ID */
    @TableField("RELEASE_BY")
    private String releaseBy;

    /** 释放时间 */
    @TableField("RELEASE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;

    /** 紧急程度 (LOW/MEDIUM/HIGH/URGENT) */
    @TableField("URGENCY_LEVEL")
    private String urgencyLevel;

    /** 审批人ID */
    @TableField("APPROVER")
    private String approver;

    /** 通知方式 (EMAIL/SMS/SYSTEM) */
    @TableField("NOTIFICATION_METHOD")
    private String notificationMethod;

    /** 自动审批 (0:否 1:是) */
    @TableField("AUTO_APPROVE")
    private Integer autoApprove;

    /** 允许部分解冻 (0:否 1:是) */
    @TableField("ALLOW_PARTIAL_UNFREEZE")
    private Integer allowPartialUnfreeze;

    /** 发送通知 (0:否 1:是) */
    @TableField("SEND_NOTIFICATION")
    private Integer sendNotification;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 创建人 */
    @TableField("CREATE_BY")
    private String createBy;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    @ExcelField(title = "创建时间", sort = 130, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private String updateBy;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标志 (0:未删除 1:已删除) */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}

