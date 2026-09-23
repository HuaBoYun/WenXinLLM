package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 存货主表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_MASTER")
public class InventoryMasterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 存货ID
     */
    @TableId(value = "INVENTORY_ID", type = IdType.ASSIGN_ID)
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
     * 分类ID
     */
    @TableField("CATEGORY_ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @TableField("CATEGORY_NAME")
    private String categoryName;

    /**
     * 规格型号
     */
    @TableField("SPECIFICATION")
    private String specification;

    /**
     * 型号
     */
    @TableField("MODEL")
    private String model;

    /**
     * 计量单位ID
     */
    @TableField("UNIT_ID")
    private Long unitId;

    /**
     * 计量单位名称
     */
    @TableField("UNIT_NAME")
    private String unitName;

    /**
     * 默认仓库ID
     */
    @TableField("WAREHOUSE_ID")
    private Long warehouseId;

    /**
     * 默认仓库名称
     */
    @TableField("WAREHOUSE_NAME")
    private String warehouseName;

    /**
     * 计价方法(1-移动平均法,2-先进先出法,3-加权平均法,4-个别计价法)
     */
    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

    /**
     * 默认价格
     */
    @TableField("DEFAULT_PRICE")
    private BigDecimal defaultPrice;

    /**
     * 当前库存数量
     */
    @TableField("CURRENT_QUANTITY")
    private BigDecimal currentQuantity;

    /**
     * 最小库存量
     */
    @TableField("MIN_QUANTITY")
    private BigDecimal minQuantity;

    /**
     * 最大库存量
     */
    @TableField("MAX_QUANTITY")
    private BigDecimal maxQuantity;

    /**
     * 是否批次管理(0-否,1-是)
     */
    @TableField("BATCH_MANAGED")
    private Integer batchManaged;

    /**
     * 保质期(天)
     */
    @TableField("SHELF_LIFE_DAYS")
    private Integer shelfLifeDays;

    /**
     * 状态(0-禁用,1-启用)
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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

