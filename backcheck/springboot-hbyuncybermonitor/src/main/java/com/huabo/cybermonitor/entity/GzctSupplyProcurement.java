package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应链采购管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SUPPLY_PROCUREMENT")
public class GzctSupplyProcurement extends Model<GzctSupplyProcurement> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("PROCUREMENT_NO")
    private String procurementNo;

    @TableField("PROCUREMENT_NAME")
    private String procurementName;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("PROCUREMENT_TYPE")
    private String procurementType;

    @TableField("QUANTITY")
    private Integer quantity;

    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("APPLICANT")
    private String applicant;

    @TableField("APPROVER")
    private String approver;

    @TableField("APPLY_DATE")
    private LocalDate applyDate;

    @TableField("EXPECTED_DATE")
    private LocalDate expectedDate;

    @TableField("ACTUAL_DATE")
    private LocalDate actualDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
