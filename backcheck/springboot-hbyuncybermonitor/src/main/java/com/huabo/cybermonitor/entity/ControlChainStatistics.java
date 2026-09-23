package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 控制链分析统计实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_CONTROL_CHAIN_STATISTICS")
public class ControlChainStatistics extends Model<ControlChainStatistics> {

    @TableId(value = "STAT_ID", type = IdType.ASSIGN_UUID)
    private String statId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("TOTAL_CHAINS")
    private Integer totalChains;

    @TableField("DIRECT_CHAINS")
    private Integer directChains;

    @TableField("INDIRECT_CHAINS")
    private Integer indirectChains;

    @TableField("AVG_CHAIN_LENGTH")
    private BigDecimal avgChainLength;

    @TableField("MAX_CHAIN_LENGTH")
    private Integer maxChainLength;

    @TableField("AVG_CONTROL_STRENGTH")
    private BigDecimal avgControlStrength;

    @TableField("LOOP_COUNT")
    private Integer loopCount;

    @TableField("HIGH_RISK_COUNT")
    private Integer highRiskCount;

    @TableField("MEDIUM_RISK_COUNT")
    private Integer mediumRiskCount;

    @TableField("LOW_RISK_COUNT")
    private Integer lowRiskCount;

    @TableField("STAT_PERIOD")
    private String statPeriod;

    @TableField("STAT_TIME")
    private LocalDateTime statTime;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
