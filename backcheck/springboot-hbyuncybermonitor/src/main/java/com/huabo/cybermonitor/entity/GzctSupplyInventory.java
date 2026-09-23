package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应链库存管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SUPPLY_INVENTORY")
public class GzctSupplyInventory extends Model<GzctSupplyInventory> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("MATERIAL_CODE")
    private String materialCode;

    @TableField("MATERIAL_NAME")
    private String materialName;

    @TableField("CATEGORY")
    private String category;

    @TableField("SPECIFICATION")
    private String specification;

    @TableField("UNIT")
    private String unit;

    @TableField("CURRENT_STOCK")
    private Integer currentStock;

    @TableField("MIN_STOCK")
    private Integer minStock;

    @TableField("MAX_STOCK")
    private Integer maxStock;

    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice;

    @TableField("TOTAL_VALUE")
    private BigDecimal totalValue;

    @TableField("WAREHOUSE")
    private String warehouse;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("STOCK_STATUS")
    private String stockStatus;

    @TableField("LAST_UPDATE_DATE")
    private LocalDate lastUpdateDate;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
