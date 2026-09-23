package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产质量实体类
 * @author system
 * @date 2026-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ASSET_QUALITY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TblAssetQuality {

    @TableId("ASSET_QUALITY_ID")
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
    private Date assessmentDate;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_USER")
    private String createUser;
}
