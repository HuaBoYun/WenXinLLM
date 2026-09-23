package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产权交易合规审查实体
 * 包含交易信息和审查信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRADE_REVIEW")
public class TblTradeReview {

    @TableId(value = "REVIEW_ID", type = IdType.INPUT)
    private String reviewId;

    @TableField("TRANSACTION_ID")
    private String transactionId;

    @TableField("COMPANY_NAME")
    private String companyName;

    // ========== 交易相关字段 ==========

    /** 交易编号 */
    @TableField("TRANS_NO")
    private String transNo;

    /** 交易方式: EXCHANGE(进场交易), AGREEMENT(协议转让), AUCTION(拍卖), FREE_TRANSFER(无偿划转) */
    @TableField("TRANS_METHOD")
    private String transMethod;

    /** 交易金额(万元) */
    @TableField("TRANS_AMOUNT")
    private BigDecimal transAmount;

    /** 评估价值(万元) */
    @TableField("APPRAISAL_VALUE")
    private BigDecimal appraisalValue;

    /** 价格比率(百分比) */
    @TableField("PRICE_RATIO")
    private BigDecimal priceRatio;

    /** 合规状态: COMPLIANT(合规), ISSUE(问题), VIOLATION(违规) */
    @TableField("COMPLIANCE_STATUS")
    private String complianceStatus;

    /** 交易日期 */
    @TableField("TRANS_DATE")
    private LocalDate transDate;

    /** 是否进场交易: 1=是, 0=否 */
    @TableField("IS_EXCHANGE_TRADED")
    private String isExchangeTraded;

    /** 是否已评估: Y=已评估, N=未评估 */
    @TableField("HAS_APPRAISAL")
    private String hasAppraisal;

    /** 交易对手 */
    @TableField("COUNTERPARTY")
    private String counterparty;

    /** 合规问题描述 */
    @TableField("COMPLIANCE_ISSUES")
    private String complianceIssues;

    // ========== 审查相关字段 ==========

    @TableField("REVIEW_TYPE")
    private String reviewType;

    @TableField("REVIEW_ITEM")
    private String reviewItem;

    @TableField("REVIEW_RESULT")
    private String reviewResult;

    @TableField("REVIEW_OPINION")
    private String reviewOpinion;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("ISSUE_DESC")
    private String issueDesc;

    @TableField("RECTIFICATION_REQUIREMENT")
    private String rectificationRequirement;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("RECTIFICATION_DEADLINE")
    private LocalDate rectificationDeadline;

    @TableField("REVIEWER")
    private String reviewer;

    @TableField("REVIEW_DATE")
    private LocalDate reviewDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
