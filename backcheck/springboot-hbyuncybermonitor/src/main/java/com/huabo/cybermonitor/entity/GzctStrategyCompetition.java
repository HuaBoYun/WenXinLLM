package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 竞争分析实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_COMPETITION")
public class GzctStrategyCompetition extends Model<GzctStrategyCompetition> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ANALYSIS_NO")
    private String analysisNo;

    @TableField("COMPETITOR_NAME")
    private String competitorName;

    @TableField("ANALYSIS_DIMENSION")
    private String analysisDimension;

    @TableField("ANALYST")
    private String analyst;

    @TableField("ANALYSIS_DATE")
    private String analysisDate;

    @TableField("MARKET_SHARE")
    private BigDecimal marketShare;

    @TableField("PRODUCT_STRENGTH")
    private Integer productStrength;

    @TableField("TECHNICAL_CAPABILITY")
    private Integer technicalCapability;

    @TableField("BRAND_INFLUENCE")
    private Integer brandInfluence;

    @TableField("FINANCIAL_STRENGTH")
    private Integer financialStrength;

    @TableField("COMPETITIVE_ADVANTAGE")
    private String competitiveAdvantage;

    @TableField("COMPETITIVE_WEAKNESS")
    private String competitiveWeakness;

    @TableField("THREAT_LEVEL")
    private String threatLevel;

    @TableField("COMPETITIVE_STRATEGY")
    private String competitiveStrategy;

    @TableField("OVERALL_SCORE")
    private BigDecimal overallScore;

    @TableField("STATUS")
    private String status;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
