package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务凭证明细实体
 * 用于财务穿透下钻第3层：科目明细凭证
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_VOUCHER")
public class GzctFinVoucher {

    @TableId(value = "VOUCHER_ID", type = IdType.ASSIGN_UUID)
    private String voucherId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("VOUCHER_NO")
    private String voucherNo;

    @TableField("BIZ_DATE")
    private String bizDate;

    @TableField("SUMMARY")
    private String summary;

    @TableField("SUBJECT_NAME")
    private String subjectName;

    @TableField("COUNTER_SUBJECT")
    private String counterSubject;

    @TableField("DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @TableField("CREDIT_AMOUNT")
    private BigDecimal creditAmount;

    @TableField("PERIOD")
    private String period;

    @TableField("DIMENSION")
    private String dimension;

    @TableField("ANOMALY")
    private String anomaly;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
