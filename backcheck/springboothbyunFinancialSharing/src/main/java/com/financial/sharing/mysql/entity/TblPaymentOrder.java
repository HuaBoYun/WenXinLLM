package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 付款单实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PAYMENT_ORDER")
public class TblPaymentOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 付款ID */
    @TableId(value = "PAYMENT_ID", type = IdType.ASSIGN_UUID)
    private String paymentId;

    /** 付款单号 */
    @TableField("PAYMENT_NO")
    private String paymentNo;

    /** 供应商ID */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 付款金额 */
    @TableField("PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;

    /** 已核销金额 */
    @TableField("WRITE_OFF_AMOUNT")
    private BigDecimal writeOffAmount;

    /** 未核销金额 */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /** 付款状态(0:待付款,1:已付款,2:已核销,3:已取消) */
    @TableField("PAYMENT_STATUS")
    private Integer paymentStatus;

    /** 付款日期 */
    @TableField("PAYMENT_DATE")
    private LocalDate paymentDate;

    /** 付款方式(1:现金,2:银行转账,3:支票,4:承兑汇票) */
    @TableField("PAYMENT_METHOD")
    private Integer paymentMethod;

    /** 银行账号 */
    @TableField("BANK_ACCOUNT")
    private String bankAccount;

    /** 摘要 */
    @TableField("SUMMARY")
    private String summary;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

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

    /** 付款方式名称 */
    @TableField(exist = false)
    private String paymentMethodName;
}

