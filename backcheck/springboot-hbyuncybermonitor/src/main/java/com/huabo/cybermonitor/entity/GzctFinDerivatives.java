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
@TableName("GZCT_FIN_DERIVATIVES")
public class GzctFinDerivatives extends Model<GzctFinDerivatives> {
    @TableId(value = "DERIVATIVE_ID", type = IdType.ASSIGN_UUID)
    private String derivativeId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("PRODUCT_TYPE") private String productType;
    @TableField("UNDERLYING_ASSET") private String underlyingAsset;
    @TableField("NOTIONAL_AMOUNT") private BigDecimal notionalAmount;
    @TableField("FAIR_VALUE") private BigDecimal fairValue;
    @TableField("UNREALIZED_PNL") private BigDecimal unrealizedPnl;
    @TableField("IS_HEDGING") private String isHedging;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("PRODUCT_NAME") private String productName;
    @TableField("MARKET_VALUE") private BigDecimal marketValue;
    @TableField("PROFIT_LOSS") private BigDecimal profitLoss;
    @TableField("MATURITY_DATE") private LocalDate maturityDate;
    @TableField("COUNTERPARTY") private String counterparty;
    @TableField("START_DATE") private LocalDate startDate;
    @TableField("BUSINESS_PURPOSE") private String businessPurpose;
    @TableField("STATUS") private String status;
    @TableField("REMARK") private String remark;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
