package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_SUPPLY_CHAIN_RISK")
public class GzctSupplyChainRisk {
    @TableId
    private String id;
    private String supplierName;
    private BigDecimal purchaseRatio;
    private String riskLevel;
    private String hasAlternative;
    private String disruptionRisk;
    private String companyName;
    private String alternativeSupplier;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
