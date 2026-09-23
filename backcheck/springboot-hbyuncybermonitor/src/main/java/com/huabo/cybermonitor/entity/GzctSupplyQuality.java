package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应链质量检验实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SUPPLY_QUALITY")
public class GzctSupplyQuality extends Model<GzctSupplyQuality> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("INSPECTION_NO")
    private String inspectionNo;

    @TableField("MATERIAL_NAME")
    private String materialName;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("INSPECTION_TYPE")
    private String inspectionType;

    @TableField("BATCH_NO")
    private String batchNo;

    @TableField("QUANTITY")
    private Integer quantity;

    @TableField("QUALIFIED_QUANTITY")
    private Integer qualifiedQuantity;

    @TableField("QUALIFICATION_RATE")
    private BigDecimal qualificationRate;

    @TableField("INSPECTOR")
    private String inspector;

    @TableField("INSPECTION_DATE")
    private LocalDate inspectionDate;

    @TableField("RESULT")
    private String result;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
