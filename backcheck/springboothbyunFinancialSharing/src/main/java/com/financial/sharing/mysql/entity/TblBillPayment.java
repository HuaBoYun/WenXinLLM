package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 票据兑付记录实体类
 * @author system
 * @since 2025-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BILL_PAYMENT")
public class TblBillPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 兑付ID */
    @TableId(value = "PAYMENT_ID", type = IdType.ASSIGN_UUID)
    private String paymentId;

    /** 兑付编号 */
    @TableField("PAYMENT_NO")
    private String paymentNo;

    /** 票据ID */
    @TableField("BILL_ID")
    private String billId;

    /** 兑付日期 */
    @TableField("PAYMENT_DATE")
    private LocalDate paymentDate;

    /** 兑付金额 */
    @TableField("PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;

    /** 兑付银行 */
    @TableField("PAYMENT_BANK")
    private String paymentBank;

    /** 兑付账号 */
    @TableField("PAYMENT_ACCOUNT")
    private String paymentAccount;

    /** 实际到账金额 */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /** 手续费 */
    @TableField("FEE")
    private BigDecimal fee;

    /** 兑付方式(1:现金,2:转账) */
    @TableField("PAYMENT_TYPE")
    private Integer paymentType;

    /** 兑付说明 */
    @TableField("DESCRIPTION")
    private String description;

    /** 状态(processing/confirmed/failed) */
    @TableField("STATUS")
    private String status;

    /** 备注 */
    @TableField("REMARKS")
    private String remarks;

    /** 创建时间 */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 创建人 */
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    /** 租户ID */
    @TableField("TENANT_ID")
    private String tenantId;

    /** 逻辑删除标记 */
    @TableLogic
    @TableField("IS_DELETED")
    private Integer isDeleted;
}

