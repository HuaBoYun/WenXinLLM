package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MARKET_ANALYSIS")
public class GzctMarketAnalysis extends Model<GzctMarketAnalysis> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ANALYSIS_NAME")
    private String analysisName;

    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    @TableField("MARKET_SIZE")
    private BigDecimal marketSize;

    @TableField("MARKET_SHARE")
    private BigDecimal marketShare;

    @TableField("GROWTH_RATE")
    private BigDecimal growthRate;

    @TableField("COMPETITOR_COUNT")
    private Integer competitorCount;

    @TableField("ANALYSIS_DATE")
    private String analysisDate;

    @TableField("ANALYST")
    private String analyst;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
