package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_INDUSTRY_LAYOUT")
public class GzctIndustryLayout {
    @TableId(value = "LAYOUT_ID", type = IdType.ASSIGN_UUID) private String layoutId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("INDUSTRY_NAME") private String industryName;
    @TableField("INDUSTRY_CODE") private String industryCode;
    @TableField("REVENUE") private BigDecimal revenue;
    @TableField("ASSET_AMOUNT") private BigDecimal assetAmount;
    @TableField("EMPLOYEE_COUNT") private Integer employeeCount;
    @TableField("REVENUE_RATIO") private BigDecimal revenueRatio;
    @TableField("IS_MAIN_INDUSTRY") private String isMainIndustry;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("REPORT_YEAR") private String reportYear;
    @TableField("SUB_INDUSTRY") private String subIndustry;
    @TableField("NET_MARGIN") private BigDecimal netMargin;
    @TableField("COMPETITIVENESS") private String competitiveness;
    @TableField("IS_KEY_MONITOR") private String isKeyMonitor;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}
