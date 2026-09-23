package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_PROCUREMENT_SUPPLIER")
public class GzctProcurementSupplier {

    @TableId
    private String id;

    private String supplierCode;

    private String supplierName;

    private String unifiedCreditCode;

    private String industry;

    private String creditRating;

    private String region;

    private String qualification;

    private BigDecimal totalTradeAmount;

    private Integer tradeCount;

    private String isBlacklisted;

    private String blacklistReason;

    private String supplierType;

    private String qualificationLevel;

    private BigDecimal totalPurchase;

    private BigDecimal purchaseRatio;

    private String isRelated;

    private String relatedCompany;

    private String blacklistStatus;

    private String riskLevel;

    private LocalDate lastAuditDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;
}
