package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本结转明细表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COST_TRANSFER_DETAIL")
public class CostTransferDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "DETAIL_ID", type = IdType.ASSIGN_ID)
    private Long detailId;

    /**
     * 结转ID
     */
    @TableField("TRANSFER_ID")
    private Long transferId;

    /**
     * 存货ID
     */
    @TableField("INVENTORY_ID")
    private Long inventoryId;

    /**
     * 存货编码
     */
    @TableField("INVENTORY_CODE")
    private String inventoryCode;

    /**
     * 存货名称
     */
    @TableField("INVENTORY_NAME")
    private String inventoryName;

    /**
     * 期初数量
     */
    @TableField("BEGINNING_QUANTITY")
    private BigDecimal beginningQuantity;

    /**
     * 期初金额
     */
    @TableField("BEGINNING_AMOUNT")
    private BigDecimal beginningAmount;

    /**
     * 入库数量
     */
    @TableField("IN_QUANTITY")
    private BigDecimal inQuantity;

    /**
     * 入库金额
     */
    @TableField("IN_AMOUNT")
    private BigDecimal inAmount;

    /**
     * 出库数量
     */
    @TableField("OUT_QUANTITY")
    private BigDecimal outQuantity;

    /**
     * 出库金额
     */
    @TableField("OUT_AMOUNT")
    private BigDecimal outAmount;

    /**
     * 结转数量
     */
    @TableField("TRANSFER_QUANTITY")
    private BigDecimal transferQuantity;

    /**
     * 结转金额
     */
    @TableField("TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    /**
     * 单位成本
     */
    @TableField("UNIT_COST")
    private BigDecimal unitCost;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT)
    private String creatorId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

