package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ACCOUNTING_VOUCHER")
public class GzctAccountingVoucher {

    @TableId(value = "VOUCHER_ID", type = IdType.ASSIGN_UUID)
    private String voucherId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("VOUCHER_DATE")
    private LocalDate voucherDate;

    @TableField("VOUCHER_NO")
    private String voucherNo;

    @TableField("SUMMARY")
    private String summary;

    @TableField("DEBIT_SUBJECT")
    private String debitSubject;

    @TableField("CREDIT_SUBJECT")
    private String creditSubject;

    @TableField("DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @TableField("CREDIT_AMOUNT")
    private BigDecimal creditAmount;

    @TableField("INPUT_USER")
    private String inputUser;

    @TableField("ANOMALY_FLAG")
    private String anomalyFlag;

    @TableField("ANOMALY_TYPE")
    private String anomalyType;

    @TableField("PERIOD")
    private String period;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
