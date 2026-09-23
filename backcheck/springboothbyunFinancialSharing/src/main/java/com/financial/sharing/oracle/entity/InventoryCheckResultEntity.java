package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 盘点结果表 - Oracle/达梦版本
 * 
 * @author system
 * @since 2026-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INVENTORY_CHECK_RESULT")
public class InventoryCheckResultEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "RESULT_ID", type = IdType.ASSIGN_ID)
    private Long resultId;

    @TableField("CHECK_ID")
    private Long checkId;

    @TableField("INVENTORY_ID")
    private Long inventoryId;

    @TableField("INVENTORY_CODE")
    private String inventoryCode;

    @TableField("INVENTORY_NAME")
    private String inventoryName;

    @TableField("LOCATION")
    private String location;

    @TableField("BOOK_QUANTITY")
    private BigDecimal bookQuantity;

    @TableField("ACTUAL_QUANTITY")
    private BigDecimal actualQuantity;

    @TableField("VARIANCE_QUANTITY")
    private BigDecimal varianceQuantity;

    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    @TableField("CHECK_RESULT")
    private String checkResult;

    @TableField("PROCESS_STATUS")
    private Integer processStatus;

    @TableField("PROCESS_REMARK")
    private String processRemark;

    @TableField("CHECKER_ID")
    private String checkerId;

    @TableField("CHECKER_NAME")
    private String checkerName;

    @TableField("CHECK_TIME")
    private LocalDateTime checkTime;

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
}

