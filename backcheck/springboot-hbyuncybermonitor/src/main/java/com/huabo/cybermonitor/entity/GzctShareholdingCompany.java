package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SHAREHOLDING_COMPANY")
public class GzctShareholdingCompany {
    @TableId(value = "COMPANY_ID", type = IdType.ASSIGN_UUID) private String companyId;
    @TableField("PROPERTY_ID") private String propertyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("EQUITY_RATIO") private BigDecimal equityRatio;
    @TableField("INVEST_AMOUNT") private BigDecimal investAmount;
    @TableField("REVENUE") private BigDecimal revenue;
    @TableField("NET_PROFIT") private BigDecimal netProfit;
    @TableField("TOTAL_ASSETS") private BigDecimal totalAssets;
    @TableField("NET_ASSETS") private BigDecimal netAssets;
    @TableField("ROE") private BigDecimal roe;
    @TableField("ASSET_LIABILITY_RATIO") private BigDecimal assetLiabilityRatio;
    @TableField("IS_LOSS") private String isLoss;
    @TableField("LOSS_YEARS") private Integer lossYears;
    /** 连续亏损年数（用于前端展示） */
    @TableField("CONSECUTIVE_LOSS_YEARS") private Integer consecutiveLossYears;
    /** 未分红年数 */
    @TableField("NO_DIVIDEND_YEARS") private Integer noDividendYears;
    /** 综合评级 A/B/C/D */
    @TableField("RATING") private String rating;
    /** 最近分红年份 */
    @TableField("LAST_DIVIDEND_YEAR") private String lastDividendYear;
    @TableField("BUSINESS_STATUS") private String businessStatus;
    @TableField("INDUSTRY") private String industry;
    @TableField("REPORT_YEAR") private String reportYear;
    @TableField("DIVIDEND_AMOUNT") private BigDecimal dividendAmount;
    @TableField("REMARK") private String remark;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
