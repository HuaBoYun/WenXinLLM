package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警记录表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_ALERT_RECORD")
public class InventoryAlertRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_ID)
    private Long recordId;

    @TableField("RULE_ID")
    private Long ruleId;

    @TableField("INVENTORY_ID")
    private Long inventoryId;

    @TableField("INVENTORY_CODE")
    private String inventoryCode;

    @TableField("INVENTORY_NAME")
    private String inventoryName;

    @TableField("WAREHOUSE_ID")
    private Long warehouseId;

    @TableField("WAREHOUSE_NAME")
    private String warehouseName;

    @TableField("ALERT_TYPE")
    private String alertType;

    @TableField("ALERT_TYPE_NAME")
    private String alertTypeName;

    @TableField("ALERT_LEVEL")
    private Integer alertLevel;

    @TableField("CURRENT_QUANTITY")
    private BigDecimal currentQuantity;

    @TableField("ALERT_VALUE")
    private BigDecimal alertValue;

    @TableField("ALERT_MESSAGE")
    private String alertMessage;

    @TableField("PROCESS_STATUS")
    private Integer processStatus;

    @TableField("PROCESSOR_ID")
    private String processorId;

    @TableField("PROCESSOR_NAME")
    private String processorName;

    @TableField("PROCESS_TIME")
    private LocalDateTime processTime;

    @TableField("PROCESS_REMARK")
    private String processRemark;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

