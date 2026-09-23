package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 存货分类表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_CATEGORY")
public class InventoryCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CATEGORY_ID", type = IdType.ASSIGN_ID)
    private Long categoryId;

    @TableField("CATEGORY_CODE")
    private String categoryCode;

    @TableField("CATEGORY_NAME")
    private String categoryName;

    @TableField("PARENT_ID")
    private Long parentId;

    @TableField("CATEGORY_LEVEL")
    private Integer categoryLevel;

    @TableField("CATEGORY_PATH")
    private String categoryPath;

    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

    @TableField("DEFAULT_UNIT_ID")
    private Long defaultUnitId;

    @TableField("DEFAULT_WAREHOUSE_ID")
    private Long defaultWarehouseId;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("STATUS")
    private Integer status;

    @TableField("REMARK")
    private String remark;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATER_ID", fill = FieldFill.INSERT_UPDATE)
    private String updaterId;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("DELETED")
    @TableLogic
    private Integer deleted;
}

