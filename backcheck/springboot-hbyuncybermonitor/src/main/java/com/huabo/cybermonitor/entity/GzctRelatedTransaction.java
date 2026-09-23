package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_RELATED_TRANSACTION")
public class GzctRelatedTransaction {
    @TableId
    private String id;
    private String transNo;
    private String companyName;
    private String supplierName;
    private String relationshipType;
    private BigDecimal transAmount;
    private BigDecimal companyPurchase;
    private BigDecimal relatedRatio;
    private BigDecimal limitRatio;
    private String isOverLimit;
    private String isDisclosed;
    private String approvalStatus;
    private String riskLevel;
    private LocalDate transDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
