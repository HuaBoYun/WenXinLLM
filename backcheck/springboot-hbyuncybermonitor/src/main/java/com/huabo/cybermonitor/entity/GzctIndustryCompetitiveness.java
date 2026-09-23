package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_INDUSTRY_COMPETITIVENESS")
public class GzctIndustryCompetitiveness extends Model<GzctIndustryCompetitiveness> {
    @TableId(value = "COMPET_ID", type = IdType.ASSIGN_UUID)
    private String competId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("INDUSTRY_NAME")
    private String industryName;
    @TableField("MARKET_SHARE")
    private BigDecimal marketShare;
    @TableField("ROE")
    private BigDecimal roe;
    @TableField("R_D_RATIO")
    private BigDecimal rDRatio;
    @TableField("COMPET_SCORE")
    private BigDecimal competScore;
    @TableField("RANK_INDUSTRY")
    private Integer rankIndustry;
    @TableField("REPORT_YEAR")
    private String reportYear;
    @TableField("TOTAL_SCORE")
    private BigDecimal totalScore;
    @TableField("RD_SCORE")
    private BigDecimal rdScore;
    @TableField("MARKET_SCORE")
    private BigDecimal marketScore;
    @TableField("PROFIT_SCORE")
    private BigDecimal profitScore;
    @TableField("BRAND_SCORE")
    private BigDecimal brandScore;
    @TableField("INNOV_SCORE")
    private BigDecimal innovScore;
    @TableField("TREND")
    private String trend;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
