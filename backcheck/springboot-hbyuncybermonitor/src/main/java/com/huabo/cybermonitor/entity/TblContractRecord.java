package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同记录台账实体 - 合同穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_RECORD")
public class TblContractRecord {

    @TableId(value = "CONTRACT_ID", type = IdType.ASSIGN_UUID)
    private String contractId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("CONTRACT_CODE")
    private String contractCode;

    @TableField("CONTRACT_NAME")
    private String contractName;

    @TableField("CONTRACT_TYPE")
    private String contractType;

    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @TableField("COUNTERPARTY_NAME")
    private String counterpartyName;

    @TableField("COUNTERPARTY_CREDIT")
    private String counterpartyCredit;

    @TableField("SIGN_DATE")
    private LocalDate signDate;

    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @TableField("EXPIRY_DATE")
    private LocalDate expiryDate;

    @TableField("HAS_LEGAL_REVIEW")
    private String hasLegalReview;

    @TableField("HAS_FINANCE_REVIEW")
    private String hasFinanceReview;

    @TableField("APPROVAL_LEVEL")
    private String approvalLevel;

    @TableField("PAID_AMOUNT")
    private BigDecimal paidAmount;

    @TableField("RECEIVED_AMOUNT")
    private BigDecimal receivedAmount;

    @TableField("PLAN_PROGRESS")
    private BigDecimal planProgress;

    @TableField("ACTUAL_PROGRESS")
    private BigDecimal actualProgress;

    @TableField("CHANGE_COUNT")
    private Integer changeCount;

    @TableField("IS_MAJOR")
    private String isMajor;

    @TableField("CONTRACT_STATUS")
    private String contractStatus;

    @TableField("DATA_SOURCE")
    private String dataSource;

    @TableField("EXTERNAL_SOURCE_CODE")
    private String externalSourceCode;

    @TableField("EXTERNAL_CONTRACT_ID")
    private String externalContractId;

    @TableField("SYNC_TIME")
    private LocalDateTime syncTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

