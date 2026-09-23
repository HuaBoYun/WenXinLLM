package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购项目台账实体 - 采购供应链穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PROCUREMENT_PROJECT")
public class TblProcurementProject {

    @TableId(value = "PROJECT_ID", type = IdType.ASSIGN_UUID)
    private String projectId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("PROJECT_CODE")
    private String projectCode;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("PROCUREMENT_TYPE")
    private String procurementType;

    @TableField("PROCUREMENT_METHOD")
    private String procurementMethod;

    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @TableField("WINNING_AMOUNT")
    private BigDecimal winningAmount;

    @TableField("MARKET_REF_PRICE")
    private BigDecimal marketRefPrice;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @TableField("PAID_AMOUNT")
    private BigDecimal paidAmount;

    @TableField("DELIVERY_DATE")
    private LocalDate deliveryDate;

    @TableField("ACTUAL_DELIVERY")
    private LocalDate actualDelivery;

    @TableField("PROJECT_STATUS")
    private String projectStatus;

    @TableField("IS_RELATED_PARTY")
    private String isRelatedParty;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

