package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预付款实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PREPAYMENT")
public class TblPrepayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 预付款ID */
    @TableId(value = "PREPAYMENT_ID", type = IdType.ASSIGN_UUID)
    private String prepaymentId;

    /** 预付款单号 */
    @TableField("PREPAYMENT_NO")
    private String prepaymentNo;

    /** 供应商ID */
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 预付金额 */
    @TableField("PREPAYMENT_AMOUNT")
    private BigDecimal prepaymentAmount;

    /** 已冲销金额 */
    @TableField("OFFSET_AMOUNT")
    private BigDecimal offsetAmount;

    /** 剩余金额 */
    @TableField("REMAINING_AMOUNT")
    private BigDecimal remainingAmount;

    /** 预付状态(0:未冲销,1:部分冲销,2:全部冲销) */
    @TableField("PREPAYMENT_STATUS")
    private Integer prepaymentStatus;

    /** 预付日期 */
    @TableField("PREPAYMENT_DATE")
    private LocalDate prepaymentDate;

    /** 付款方式 */
    @TableField("PAYMENT_METHOD")
    private Integer paymentMethod;

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
}

