package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 关联交易记录实体 - 财务穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_RELATED_TRANSACTION")
public class TblRelatedTransaction {

    @TableId(value = "TRANSACTION_ID", type = IdType.ASSIGN_UUID)
    private String transactionId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("COUNTERPARTY_ID")
    private String counterpartyId;

    @TableField("COUNTERPARTY_NAME")
    private String counterpartyName;

    @TableField("RELATION_TYPE")
    private String relationType;

    @TableField("TRANSACTION_TYPE")
    private String transactionType;

    @TableField("TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @TableField("MARKET_PRICE")
    private BigDecimal marketPrice;

    @TableField("PRICING_METHOD")
    private String pricingMethod;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("TRANSACTION_DATE")
    private LocalDate transactionDate;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

