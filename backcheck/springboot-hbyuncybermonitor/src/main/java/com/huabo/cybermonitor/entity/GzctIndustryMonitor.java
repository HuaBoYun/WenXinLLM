package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_INDUSTRY_MONITOR")
public class GzctIndustryMonitor {
    @TableId(value = "MONITOR_ID", type = IdType.ASSIGN_UUID) private String monitorId;
    @TableField("INDUSTRY_NAME") private String industryName;
    @TableField("INDUSTRY_CODE") private String industryCode;
    @TableField("COMPANY_COUNT") private Integer companyCount;
    @TableField("TOTAL_REVENUE") private BigDecimal totalRevenue;
    @TableField("TOTAL_PROFIT") private BigDecimal totalProfit;
    @TableField("AVG_RETURN_RATE") private BigDecimal avgReturnRate;
    @TableField("POLICY_COMPLIANCE_RATE") private BigDecimal policyComplianceRate;
    @TableField("KEY_RISK") private String keyRisk;
    @TableField("MONITOR_STATUS") private String monitorStatus;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("SUB_TYPE") private String subType;
    @TableField("NET_MARGIN") private BigDecimal netMargin;
    @TableField("RENEWABLE_RATIO") private BigDecimal renewableRatio;
    @TableField("CARBON_INTENSITY") private BigDecimal carbonIntensity;
    @TableField("BAD_DEBT_RATIO") private BigDecimal badDebtRatio;
    @TableField("CAPITAL_RATIO") private BigDecimal capitalRatio;
    @TableField("COMPLIANCE_STATUS") private String complianceStatus;
    @TableField("RD_INTENSITY") private BigDecimal rdIntensity;
    @TableField("SMART_LEVEL") private String smartLevel;
    @TableField("TECH_SELF_RATE") private BigDecimal techSelfRate;
    @TableField("ASSET_RETURN") private BigDecimal assetReturn;
    @TableField("DEBT_RATIO") private BigDecimal debtRatio;
    @TableField("SAFETY_RATING") private String safetyRating;
    @TableField("MAJOR_RISKS") private Integer majorRisks;
    @TableField("SUBSIDY_RATIO") private BigDecimal subsidyRatio;
    @TableField("LOSS_YEARS") private Integer lossYears;
    @TableField("SOCIAL_SCORE") private BigDecimal socialScore;
    @TableField("PROFIT_STATUS") private String profitStatus;
    @TableField("COMPETITIVENESS") private String competitiveness;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("REVENUE") private BigDecimal revenue;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
