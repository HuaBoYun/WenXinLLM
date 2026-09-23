package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商台账实体 - 采购供应链穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SUPPLIER")
public class TblSupplier {

    @TableId(value = "SUPPLIER_ID", type = IdType.ASSIGN_UUID)
    private String supplierId;

    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("SUPPLIER_TYPE")
    private String supplierType;

    @TableField("QUALIFICATION_LEVEL")
    private String qualificationLevel;

    @TableField("COOPERATION_STATUS")
    private String cooperationStatus;

    @TableField("UNIFIED_CREDIT_CODE")
    private String unifiedCreditCode;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("REGION")
    private String region;

    @TableField("CREDIT_RATING")
    private String creditRating;

    @TableField("QUALIFICATION")
    private String qualification;

    @TableField("IS_BLACKLISTED")
    private String isBlacklisted;

    @TableField("BLACKLIST_REASON")
    private String blacklistReason;

    @TableField("TOTAL_TRADE_AMOUNT")
    private BigDecimal totalTradeAmount;

    @TableField("TRADE_COUNT")
    private Integer tradeCount;

    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @TableField("DELIVERY_SCORE")
    private BigDecimal deliveryScore;

    @TableField("SERVICE_SCORE")
    private BigDecimal serviceScore;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

