package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 计量单位表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_UNIT")
public class InventoryUnitEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "UNIT_ID", type = IdType.ASSIGN_ID)
    private Long unitId;

    @TableField("UNIT_CODE")
    private String unitCode;

    @TableField("UNIT_NAME")
    private String unitName;

    @TableField("UNIT_SYMBOL")
    private String unitSymbol;

    @TableField("BASE_UNIT_ID")
    private Long baseUnitId;

    @TableField("CONVERSION_RATE")
    private BigDecimal conversionRate;

    @TableField("CATEGORY_ID")
    private Long categoryId;

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

