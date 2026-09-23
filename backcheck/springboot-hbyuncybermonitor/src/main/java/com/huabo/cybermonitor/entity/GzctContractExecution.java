package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_CONTRACT_EXECUTION")
public class GzctContractExecution {
    @TableId
    private String id;
    private String contractNo;
    private String contractName;
    private String companyName;
    private String supplierName;
    private BigDecimal contractAmount;
    private BigDecimal paidAmount;
    private Integer paymentRatio;
    private LocalDate startDate;
    private LocalDate endDate;
    private String acceptanceStatus;
    private String contractStatus;
    private String riskLevel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
