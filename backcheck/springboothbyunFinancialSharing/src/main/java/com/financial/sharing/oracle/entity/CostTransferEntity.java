package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本结转表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COST_TRANSFER")
public class CostTransferEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结转ID
     */
    @TableId(value = "TRANSFER_ID", type = IdType.ASSIGN_ID)
    private Long transferId;

    /**
     * 结转单号
     */
    @TableField("TRANSFER_NO")
    private String transferNo;

    /**
     * 结转期间(YYYY-MM)
     */
    @TableField("TRANSFER_PERIOD")
    private String transferPeriod;

    /**
     * 结转类型(SALES_COST-销售成本,PRODUCTION_COST-生产成本,PERIOD_EXPENSE-期间费用,COST_VARIANCE-成本差异)
     */
    @TableField("TRANSFER_TYPE")
    private String transferType;

    /**
     * 结转类型名称
     */
    @TableField("TRANSFER_TYPE_NAME")
    private String transferTypeName;

    /**
     * 存货数量
     */
    @TableField("INVENTORY_COUNT")
    private Integer inventoryCount;

    /**
     * 结转金额
     */
    @TableField("TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 结转状态(0-待结转,1-已结转,2-已撤销)
     */
    @TableField("TRANSFER_STATUS")
    private Integer transferStatus;

    /**
     * 凭证数量
     */
    @TableField("VOUCHER_COUNT")
    private Integer voucherCount;

    /**
     * 凭证ID列表(逗号分隔)
     */
    @TableField("VOUCHER_IDS")
    private String voucherIds;

    /**
     * 结转时间
     */
    @TableField("TRANSFER_TIME")
    private LocalDateTime transferTime;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

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

    /**
     * 删除标识(0-未删除,1-已删除)
     */
    @TableField("DELETED")
    @TableLogic
    private Integer deleted;
}

