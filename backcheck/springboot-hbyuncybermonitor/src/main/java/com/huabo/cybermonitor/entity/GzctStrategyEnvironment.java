package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 环境分析实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_STRATEGY_ENVIRONMENT")
public class GzctStrategyEnvironment extends Model<GzctStrategyEnvironment> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ANALYSIS_NO")
    private String analysisNo;

    @TableField("ANALYSIS_THEME")
    private String analysisTheme;

    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    @TableField("ANALYST")
    private String analyst;

    @TableField("DEPARTMENT")
    private String department;

    @TableField("ANALYSIS_DATE")
    private String analysisDate;

    @TableField("POLITICAL_FACTOR")
    private Integer politicalFactor;

    @TableField("ECONOMIC_FACTOR")
    private Integer economicFactor;

    @TableField("SOCIAL_FACTOR")
    private Integer socialFactor;

    @TableField("TECHNICAL_FACTOR")
    private Integer technicalFactor;

    @TableField("OVERALL_RISK")
    private String overallRisk;

    @TableField("OPPORTUNITY_INDEX")
    private BigDecimal opportunityIndex;

    @TableField("THREAT_INDEX")
    private BigDecimal threatIndex;

    @TableField("STRATEGIC_RECOMMENDATION")
    private String strategicRecommendation;

    @TableField("VALIDITY_PERIOD")
    private Integer validityPeriod;

    @TableField("STATUS")
    private String status;

    @TableField("NEXT_UPDATE_DATE")
    private String nextUpdateDate;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
