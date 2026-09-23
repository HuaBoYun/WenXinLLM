package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 融资记录实体 - 金融风险穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCING_RECORD")
public class TblFinancingRecord {

    @TableId(value = "FINANCING_ID", type = IdType.ASSIGN_UUID)
    private String financingId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("FINANCING_TYPE")
    private String financingType;

    @TableField("FINANCING_AMOUNT")
    private BigDecimal financingAmount;

    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    @TableField("RATE_TYPE")
    private String rateType;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("MATURITY_DATE")
    private LocalDate maturityDate;

    @TableField("GUARANTEE_METHOD")
    private String guaranteeMethod;

    @TableField("LENDER")
    private String lender;

    @TableField("BORROWER_ID")
    private String borrowerId;

    @TableField("BORROWER_NAME")
    private String borrowerName;

    @TableField("FINANCING_STATUS")
    private String financingStatus;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("OUTSTANDING_AMOUNT")
    private BigDecimal outstandingAmount;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

