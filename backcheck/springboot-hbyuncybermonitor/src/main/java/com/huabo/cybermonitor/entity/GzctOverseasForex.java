package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_FOREX")
public class GzctOverseasForex extends Model<GzctOverseasForex> {
    @TableId(value = "FOREX_ID", type = IdType.ASSIGN_UUID) private String forexId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("CURRENCY") private String currency;
    @TableField("EXPOSURE_AMOUNT") private BigDecimal exposureAmount;
    @TableField("HEDGE_AMOUNT") private BigDecimal hedgeAmount;
    @TableField("HEDGE_RATIO") private BigDecimal hedgeRatio;
    @TableField("EXCHANGE_RATE") private BigDecimal exchangeRate;
    @TableField("RATE_CHANGE") private BigDecimal rateChange;
    @TableField("IMPACT_AMOUNT") private BigDecimal impactAmount;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
