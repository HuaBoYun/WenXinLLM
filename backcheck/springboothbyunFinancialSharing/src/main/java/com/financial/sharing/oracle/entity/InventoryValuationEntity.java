package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 存货计价表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_VALUATION")
public class InventoryValuationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计价ID
     */
    @TableId(value = "VALUATION_ID", type = IdType.ASSIGN_ID)
    private Long valuationId;

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
     * 计价方法(1-移动平均法,2-先进先出法,3-加权平均法,4-个别计价法)
     */
    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

    /**
     * 单位成本
     */
    @TableField("UNIT_COST")
    private BigDecimal unitCost;

    /**
     * 当前库存数量
     */
    @TableField("CURRENT_QUANTITY")
    private BigDecimal currentQuantity;

    /**
     * 库存总值
     */
    @TableField("TOTAL_VALUE")
    private BigDecimal totalValue;

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
     * 计价期间(YYYY-MM)
     */
    @TableField("VALUATION_PERIOD")
    private String valuationPeriod;

    /**
     * 状态(0-禁用,1-启用)
     */
    @TableField("STATUS")
    private Integer status;

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
     * 从 Map 创建 Entity
     *
     * @param map 数据Map
     * @return Entity对象
     */
    public static InventoryValuationEntity fromMap(Map<String, Object> map) {
        InventoryValuationEntity entity = new InventoryValuationEntity();

        // 基本字段
        if (map.get("valuationId") != null) {
            entity.setValuationId(Long.valueOf(map.get("valuationId").toString()));
        }
        if (map.get("inventoryId") != null) {
            entity.setInventoryId(Long.valueOf(map.get("inventoryId").toString()));
        }
        if (map.get("inventoryCode") != null) {
            entity.setInventoryCode(map.get("inventoryCode").toString());
        }
        if (map.get("inventoryName") != null) {
            entity.setInventoryName(map.get("inventoryName").toString());
        }
        if (map.get("warehouseId") != null) {
            entity.setWarehouseId(Long.valueOf(map.get("warehouseId").toString()));
        }
        if (map.get("warehouseName") != null) {
            entity.setWarehouseName(map.get("warehouseName").toString());
        }
        if (map.get("pricingMethod") != null) {
            entity.setPricingMethod(Integer.valueOf(map.get("pricingMethod").toString()));
        }

        // BigDecimal 字段
        if (map.get("unitCost") != null) {
            entity.setUnitCost(new BigDecimal(map.get("unitCost").toString()));
        }
        if (map.get("currentQuantity") != null) {
            entity.setCurrentQuantity(new BigDecimal(map.get("currentQuantity").toString()));
        }
        if (map.get("totalValue") != null) {
            entity.setTotalValue(new BigDecimal(map.get("totalValue").toString()));
        }
        if (map.get("beginningQuantity") != null) {
            entity.setBeginningQuantity(new BigDecimal(map.get("beginningQuantity").toString()));
        }
        if (map.get("beginningAmount") != null) {
            entity.setBeginningAmount(new BigDecimal(map.get("beginningAmount").toString()));
        }
        if (map.get("inQuantity") != null) {
            entity.setInQuantity(new BigDecimal(map.get("inQuantity").toString()));
        }
        if (map.get("inAmount") != null) {
            entity.setInAmount(new BigDecimal(map.get("inAmount").toString()));
        }
        if (map.get("outQuantity") != null) {
            entity.setOutQuantity(new BigDecimal(map.get("outQuantity").toString()));
        }
        if (map.get("outAmount") != null) {
            entity.setOutAmount(new BigDecimal(map.get("outAmount").toString()));
        }

        // 其他字段
        if (map.get("valuationPeriod") != null) {
            entity.setValuationPeriod(map.get("valuationPeriod").toString());
        }
        if (map.get("status") != null) {
            entity.setStatus(Integer.valueOf(map.get("status").toString()));
        }
        if (map.get("tenantId") != null) {
            entity.setTenantId(Long.valueOf(map.get("tenantId").toString()));
        }

        return entity;
    }
}

