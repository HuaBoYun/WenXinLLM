package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产质量评估实体
 * 对应表: TBL_ASSET_QUALITY
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ASSET_QUALITY")
public class TblAssetQuality {

    @TableId(value = "ASSET_QUALITY_ID", type = IdType.ASSIGN_UUID)
    private String assetQualityId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ASSET_NAME")
    private String assetName;

    @TableField("ASSET_CATEGORY")
    private String assetCategory;

    @TableField("ASSET_VALUE")
    private BigDecimal assetValue;

    @TableField("QUALITY_LEVEL")
    private String qualityLevel;

    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("QUALITY_STATUS")
    private String qualityStatus;

    @TableField("ASSESSMENT_STATUS")
    private String assessmentStatus;

    @TableField("IMPAIRMENT_AMOUNT")
    private BigDecimal impairmentAmount;

    @TableField("RETURN_RATE")
    private BigDecimal returnRate;

    @TableField("QUALITY_DESCRIPTION")
    private String qualityDescription;

    @TableField("ASSESSMENT_DATE")
    private LocalDateTime assessmentDate;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("CREATE_USER")
    private String createUser;
}
