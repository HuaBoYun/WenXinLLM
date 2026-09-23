package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 控制链分析实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTROL_CHAIN_ANALYSIS")
public class TblControlChainAnalysis {

    @TableId(value = "ANALYSIS_ID", type = IdType.ASSIGN_UUID)
    private String analysisId;

    @TableField("CHAIN_ID")
    private String chainId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ANALYSIS_TYPE")
    private String analysisType;

    @TableField("PENETRATION_DEPTH")
    private Integer penetrationDepth;

    @TableField("TOTAL_CONTROL_RATIO")
    private BigDecimal totalControlRatio;

    @TableField("DIRECT_CONTROL_RATIO")
    private BigDecimal directControlRatio;

    @TableField("INDIRECT_CONTROL_RATIO")
    private BigDecimal indirectControlRatio;

    @TableField("KEY_NODE_COUNT")
    private Integer keyNodeCount;

    @TableField("WEAK_LINK_COUNT")
    private Integer weakLinkCount;

    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    @TableField("RISK_DESCRIPTION")
    private String riskDescription;

    @TableField("OPTIMIZATION_SUGGESTION")
    private String optimizationSuggestion;

    @TableField("ANALYSIS_DATE")
    private LocalDate analysisDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
