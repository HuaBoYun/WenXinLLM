package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_PURCHASE_RECORD")
public class GzctPurchaseRecord {

    @TableId
    private String id;

    private String purchaseNo;

    private String companyName;

    private String supplierName;

    private String purchaseType;

    private String biddingMethod;

    private BigDecimal contractAmount;

    private LocalDate purchaseDate;

    private String isRelated;

    private String complianceStatus;

    private String riskLevel;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;
}
