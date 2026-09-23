package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付单据实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PAYABLE_DOCUMENT")
public class TblPayableDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 单据ID */
    @TableId(value = "DOCUMENT_ID", type = IdType.ASSIGN_UUID)
    private String documentId;

    /** 单据编号 */
    @TableField("DOCUMENT_NO")
    private String documentNo;

    /** 供应商ID */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 应付金额 */
    @TableField("PAYABLE_AMOUNT")
    private BigDecimal payableAmount;

    /** 已付金额 */
    @TableField("PAID_AMOUNT")
    private BigDecimal paidAmount;

    /** 未付金额 */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /** 单据状态(0:草稿,1:待审核,2:已审核,3:已拒绝) */
    @TableField("DOCUMENT_STATUS")
    private Integer documentStatus;

    /** 到期日期 */
    @TableField("DUE_DATE")
    private LocalDate dueDate;

    /** 币种 */
    @TableField("CURRENCY")
    private String currency;

    /** 汇率 */
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    /** 业务类型(1:采购应付,2:费用应付,3:其他应付) */
    @TableField("BUSINESS_TYPE")
    private Integer businessType;

    /** 摘要 */
    @TableField("SUMMARY")
    private String summary;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

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

    /** 业务类型名称 */
    @TableField(exist = false)
    private String businessTypeName;

    /** 创建人姓名 */
    @TableField(exist = false)
    private String createByName;

    /** 更新人姓名 */
    @TableField(exist = false)
    private String updateByName;
}

