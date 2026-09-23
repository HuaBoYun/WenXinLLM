package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产权交易记录实体 - 产权穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PROPERTY_TRANSACTION")
public class TblPropertyTransaction {

    @TableId(value = "TRANSACTION_ID", type = IdType.ASSIGN_UUID)
    private String transactionId;

    @TableField("PROPERTY_ID")
    private String propertyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("TRANSACTION_TYPE")
    private String transactionType;

    @TableField("TRANSACTION_METHOD")
    private String transactionMethod;

    @TableField("TRANSACTION_AMOUNT")
    private BigDecimal transactionAmount;

    @TableField("APPRAISAL_VALUE")
    private BigDecimal appraisalValue;

    @TableField("COUNTERPARTY")
    private String counterparty;

    @TableField("IS_EXCHANGE_TRADED")
    private String isExchangeTraded;

    @TableField("HAS_APPRAISAL")
    private String hasAppraisal;

    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @TableField("COMPLIANCE_STATUS")
    private String complianceStatus;

    @TableField("TRANSACTION_DATE")
    private LocalDate transactionDate;

    @TableField("COMPLETION_DATE")
    private LocalDate completionDate;

    @TableField("EXCHANGE_NAME")
    private String exchangeName;

    @TableField("LISTING_PRICE")
    private BigDecimal listingPrice;

    @TableField("DEAL_PRICE")
    private BigDecimal dealPrice;

    @TableField("PREMIUM_RATE")
    private BigDecimal premiumRate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}

