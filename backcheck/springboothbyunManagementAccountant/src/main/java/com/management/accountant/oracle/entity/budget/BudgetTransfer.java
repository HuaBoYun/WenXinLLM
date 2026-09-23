package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.management.accountant.util.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算转移实体
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_TRANSFER")
public class BudgetTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 转移ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String transferId;

    /**
     * 转移编码
     */
    @ExcelField(title = "转移编码", sort = 10, width = 4000)
    private String transferCode;

    /**
      * 转移标题
     */
     @ExcelField(title = "转移标题", sort = 20, width = 6000)
     private String transferTitle;

    /**
      * 转移类型 (DEPARTMENT/PROJECT/ACCOUNT/PERIOD/EMERGENCY/CUSTOM)
     */
     @ExcelField(title = "转移类型", sort = 30, width = 3000)
     private String transferType;

    /**
      * 转移金额
     */
     @ExcelField(title = "转移金额", sort = 40, width = 4000, dataFormat = "#,##0.00")
     private BigDecimal transferAmount;

    /**
      * 转移原因
     */
     @ExcelField(title = "转移原因", sort = 50, width = 6000)
     private String transferReason;

    /**
      * 转移状态 (PENDING/APPROVED/EXECUTING/COMPLETED/REJECTED)
     */
     @ExcelField(title = "转移状态", sort = 60, width = 3000)
     private String transferStatus;

    /**
      * 审批状态 (PENDING/APPROVED/REJECTED)
     */
     private String approvalStatus;

    /**
      * 转出单元ID
     */
     private String fromOrganizationId;

     /**
      * 转出单元名称
      */
     @ExcelField(title = "转出单元", sort = 65, width = 4000)
     private String fromOrganizationName;

     /**
      * 转入单元ID
      */
     private String toOrganizationId;

     /**
      * 转入单元名称
      */
     @ExcelField(title = "转入单元", sort = 66, width = 4000)
     private String toOrganizationName;

     /**
      * 转出预算科目ID
      */
     private String fromBudgetAccountId;

     /**
      * 转入预算科目ID
      */
     private String toBudgetAccountId;

     /**
      * 源预算ID
      */
     private String sourceBudgetId;

     /**
      * 目标预算ID
      */
     private String targetBudgetId;

    /**
     * 申请日期
     */
    @ExcelField(title = "申请日期", sort = 70, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date applyDate;

    /**
     * 转移日期
     */
    @ExcelField(title = "转移日期", sort = 80, width = 4000, dataFormat = "yyyy-MM-dd")
    private Date transferDate;

    /**
      * 计划转移日期
      */
     private Date plannedTransferDate;

     /**
      * 申请人ID
     */
    private String applyBy;

    /**
      * 申请人姓名
      */
     @ExcelField(title = "申请人", sort = 90, width = 3000)
     private String applicant;

     /**
      * 审批人ID
     */
    private String approveBy;

    /**
      * 指定审批人
     */
     private String approver;

    /**
     * 转移描述
     */
    @ExcelField(title = "转移描述", sort = 120, width = 8000)
    private String transferDescription;

    /**
      * 紧急程度 (LOW/MEDIUM/HIGH/URGENT)
     */
     private String urgencyLevel;

    /**
      * 通知方式 (EMAIL/SMS/SYSTEM)
     */
     private String notificationMethod;

    /**
      * 转移方式 (IMMEDIATE/SCHEDULED/CONDITIONAL)
     */
     private String transferMethod;

    /**
      * 转移比例
     */
     private BigDecimal transferRatio;

     /**
      * 优先级 (HIGH/MEDIUM/LOW)
      */
     private String priority;

     /**
      * 自动审批
      */
     private Boolean autoApprove;

     /**
      * 发送通知
      */
     private Boolean sendNotification;

     /**
      * 跟踪进度
      */
     private Boolean trackProgress;

     /**
      * 备注
      */
     private String remark;

     /**
      * 审批备注
      */
     private String approveRemark;

     /**
      * 审批日期
      */
     private Date approveDate;

     /**
      * 审批时间
      */
     private Date approveTime;

     /**
      * 执行时间
      */
     private Date executeTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelField(title = "创建时间", sort = 130, width = 5000, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    private Integer delFlag;
}

