package com.financial.sharing.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 总账实体类
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class GeneralLedgerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总账ID
     */
    private Long ledgerId;

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 凭证明细ID
     */
    private Long detailId;

    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountingDate;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 科目编码
     */
    private String accountCode;

    /**
     * 科目名称
     */
    private String accountName;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}