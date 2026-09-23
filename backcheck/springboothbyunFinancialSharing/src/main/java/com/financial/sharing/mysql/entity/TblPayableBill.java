package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付票据实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PAYABLE_BILL")
public class TblPayableBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 票据ID */
    @TableId(value = "BILL_ID", type = IdType.ASSIGN_UUID)
    private String billId;

    /** 票据号 */
    @TableField("BILL_NO")
    private String billNo;

    /** 票据类型(bank_acceptance/commercial_acceptance/check/promissory_note) */
    @TableField("BILL_TYPE")
    private String billType;

    /** 供应商ID */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 票据金额 */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /** 出票日期 */
    @TableField("ISSUE_DATE")
    private LocalDate issueDate;

    /** 到期日期 */
    @TableField("DUE_DATE")
    private LocalDate dueDate;

    /** 状态(active/due/paid/endorsed/cancelled) */
    @TableField("STATUS")
    private String status;

    /** 承兑银行 */
    @TableField("BANK_NAME")
    private String bankName;

    /** 银行账号 */
    @TableField("BANK_ACCOUNT")
    private String bankAccount;

    /** 出票人 */
    @TableField("DRAWER")
    private String drawer;

    /** 收款人 */
    @TableField("PAYEE")
    private String payee;

    /** 票据描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

    /** 审核状态(0:待审核,1:已审核,2:已拒绝) */
    @TableField("AUDIT_STATUS")
    private Integer auditStatus;

    /** 审核人 */
    @TableField("AUDIT_USER")
    private String auditUser;

    /** 审核时间 */
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    /** 审核意见 */
    @TableField("AUDIT_COMMENTS")
    private String auditComments;

    /** 创建时间 */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人 */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /** 更新人 */
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 租户ID */
    @TableField("TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;

    // ========== 非持久化字段 ==========

    /** 供应商名称 */
    @TableField(exist = false)
    private String supplierName;

    /** 票据类型名称 */
    @TableField(exist = false)
    private String billTypeName;

    /** 剩余天数 */
    @TableField(exist = false)
    private Integer remainingDays;
}

