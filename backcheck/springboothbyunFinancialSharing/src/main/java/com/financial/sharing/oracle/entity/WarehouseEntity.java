package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 仓库表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_WAREHOUSE")
public class WarehouseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "WAREHOUSE_ID", type = IdType.ASSIGN_ID)
    private Long warehouseId;

    @TableField("WAREHOUSE_CODE")
    private String warehouseCode;

    @TableField("WAREHOUSE_NAME")
    private String warehouseName;

    @TableField("WAREHOUSE_TYPE")
    private String warehouseType;

    @TableField("WAREHOUSE_TYPE_NAME")
    private String warehouseTypeName;

    @TableField("ADDRESS")
    private String address;

    @TableField("CONTACT_PERSON")
    private String contactPerson;

    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @TableField("CAPACITY")
    private BigDecimal capacity;

    @TableField("USED_CAPACITY")
    private BigDecimal usedCapacity;

    @TableField("MANAGER_ID")
    private String managerId;

    @TableField("MANAGER_NAME")
    private String managerName;

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

