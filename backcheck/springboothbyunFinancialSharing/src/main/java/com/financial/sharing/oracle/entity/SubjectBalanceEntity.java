package com.financial.sharing.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目余额实体类
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
public class SubjectBalanceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 余额ID
     */
    private Long balanceId;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 期初余额
     */
    private BigDecimal beginningBalance;

    /**
     * 本期借方发生额
     */
    private BigDecimal periodDebitAmount;

    /**
     * 本期贷方发生额
     */
    private BigDecimal periodCreditAmount;

    /**
     * 期末余额
     */
    private BigDecimal endingBalance;

    /**
     * 余额方向（借方/贷方）
     */
    private String balanceDirection;

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

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}