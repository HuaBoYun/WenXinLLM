package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_LIQUIDITY")
public class GzctFinLiquidity extends Model<GzctFinLiquidity> {
    @TableId(value = "LIQUIDITY_ID", type = IdType.ASSIGN_UUID)
    private String liquidityId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("REPORT_DATE")
    private LocalDateTime reportDate;
    @TableField("CURRENT_RATIO")
    private BigDecimal currentRatio;
    @TableField("QUICK_RATIO")
    private BigDecimal quickRatio;
    @TableField("CASH_RATIO")
    private BigDecimal cashRatio;
    @TableField("LIQUIDITY_SCORE")
    private BigDecimal liquidityScore;
    @TableField("SHORT_TERM_DEBT")
    private BigDecimal shortTermDebt;
    @TableField("CASH_EQUIVALENT")
    private BigDecimal cashEquivalent;
    @TableField("MATURITY_0_1Y")
    private BigDecimal maturity01y;
    @TableField("MATURITY_1_3Y")
    private BigDecimal maturity13y;
    @TableField("MATURITY_3_5Y")
    private BigDecimal maturity35y;
    @TableField("MATURITY_ABOVE_5Y")
    private BigDecimal maturityAbove5y;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
