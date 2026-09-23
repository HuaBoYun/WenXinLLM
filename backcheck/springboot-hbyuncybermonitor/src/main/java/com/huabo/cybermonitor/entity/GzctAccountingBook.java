package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_BOOK")
public class GzctAccountingBook {

    @TableId(value = "BOOK_ID", type = IdType.ASSIGN_UUID)
    private String bookId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("BOOK_TYPE")
    private String bookType;

    @TableField("SUBJECT_CODE")
    private String subjectCode;

    @TableField("SUBJECT_NAME")
    private String subjectName;

    @TableField("DIRECTION")
    private String direction;

    @TableField("OPENING_BALANCE")
    private BigDecimal openingBalance;

    @TableField("DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @TableField("CREDIT_AMOUNT")
    private BigDecimal creditAmount;

    @TableField("CLOSING_BALANCE")
    private BigDecimal closingBalance;

    @TableField("CHANGE_RATE")
    private BigDecimal changeRate;

    @TableField("VOUCHER_NO")
    private String voucherNo;

    @TableField("VOUCHER_DATE")
    private LocalDate voucherDate;

    @TableField("SUMMARY")
    private String summary;

    @TableField("AUX_NAME")
    private String auxName;

    @TableField("AUX_TYPE")
    private String auxType;

    @TableField("BALANCE")
    private BigDecimal balance;

    @TableField("WITHIN_1Y")
    private BigDecimal within1y;

    @TableField("Y1TO2")
    private BigDecimal y1to2;

    @TableField("Y2TO3")
    private BigDecimal y2to3;

    @TableField("OVER_3Y")
    private BigDecimal over3y;

    @TableField("OVER_3Y_RATE")
    private BigDecimal over3yRate;

    @TableField("RISK_FLAG")
    private String riskFlag;

    @TableField("PARENT_ID")
    private String parentId;

    @TableField("PERIOD")
    private String period;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
