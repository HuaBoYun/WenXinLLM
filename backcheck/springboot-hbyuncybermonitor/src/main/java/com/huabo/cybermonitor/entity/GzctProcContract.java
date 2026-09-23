package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_CONTRACT")
public class GzctProcContract extends Model<GzctProcContract> {
    @TableId(value = "CONTRACT_ID", type = IdType.ASSIGN_UUID)
    private String contractId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("CONTRACT_NO")
    private String contractNo;
    @TableField("CONTRACT_NAME")
    private String contractName;
    @TableField("SUPPLIER_NAME")
    private String supplierName;
    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;
    @TableField("PAID_AMOUNT")
    private BigDecimal paidAmount;
    @TableField("PAYMENT_RATE")
    private BigDecimal paymentRate;
    @TableField("ACCEPTANCE_STATUS")
    private String acceptanceStatus;
    @TableField("IS_OVERDUE")
    private String isOverdue;
    @TableField("OVERDUE_DAYS")
    private Integer overdueDays;
    @TableField("START_DATE")
    private LocalDate startDate;
    @TableField("END_DATE")
    private LocalDate endDate;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
