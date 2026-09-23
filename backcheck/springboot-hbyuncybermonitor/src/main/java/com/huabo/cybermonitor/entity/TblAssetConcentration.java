package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产集中度实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ASSET_CONCENTRATION")
public class TblAssetConcentration {

    @TableId(value = "CONCENTRATION_ID", type = IdType.ASSIGN_UUID)
    private String concentrationId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("REGION")
    private String region;

    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    @TableField("ASSET_RATIO")
    private BigDecimal assetRatio;

    @TableField("HHI_INDEX")
    private BigDecimal hhiIndex;

    @TableField("CONCENTRATION_LEVEL")
    private String concentrationLevel;

    @TableField("TOP_ASSET_TYPE")
    private String topAssetType;

    @TableField("TOP_ASSET_RATIO")
    private BigDecimal topAssetRatio;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("ANALYSIS_DATE")
    private LocalDate analysisDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
