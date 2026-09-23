package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 存货核算表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_ACCOUNTING")
public class InventoryAccountingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 核算ID
     */
    @TableId(value = "ACCOUNTING_ID", type = IdType.ASSIGN_ID)
    private Long accountingId;

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
     * 仓库ID
     */
    @TableField("WAREHOUSE_ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @TableField("WAREHOUSE_NAME")
    private String warehouseName;

    /**
     * 核算期间(YYYY-MM)
     */
    @TableField("ACCOUNTING_PERIOD")
    private String accountingPeriod;

    /**
     * 计价方法(1-移动平均法,2-先进先出法,3-加权平均法,4-个别计价法)
     */
    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

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
     * 期末数量
     */
    @TableField("ENDING_QUANTITY")
    private BigDecimal endingQuantity;

    /**
     * 期末金额
     */
    @TableField("ENDING_AMOUNT")
    private BigDecimal endingAmount;

    /**
     * 单位成本
     */
    @TableField("UNIT_COST")
    private BigDecimal unitCost;

    /**
     * 成本调整额
     */
    @TableField("COST_ADJUSTMENT")
    private BigDecimal costAdjustment;

    /**
     * 核算状态(0-待核算,1-已核算,2-已调整)
     */
    @TableField("ACCOUNTING_STATUS")
    private Integer accountingStatus;

    /**
     * 是否生成凭证(0-否,1-是)
     */
    @TableField("IS_GENERATED_VOUCHER")
    private Integer isGeneratedVoucher;

    /**
     * 凭证ID列表(逗号分隔)
     */
    @TableField("VOUCHER_IDS")
    private String voucherIds;

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

    /**
     * 更新人ID
     */
    @TableField(value = "UPDATER_ID", fill = FieldFill.INSERT_UPDATE)
    private String updaterId;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

