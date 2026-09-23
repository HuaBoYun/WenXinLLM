package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产配置统计实体类
 * 对应表: TBL_ASSET_ALLOCATION
 */
@Data
@TableName("TBL_ASSET_ALLOCATION")
public class TblAssetAllocation {

    @TableId(value = "ALLOCATION_ID", type = IdType.ASSIGN_UUID)
    private String allocationId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("ASSET_TYPE")
    private String assetType;

    @TableField("ASSET_AMOUNT")
    private BigDecimal assetAmount;

    @TableField("ALLOCATION_RATIO")
    private BigDecimal allocationRatio;

    @TableField("TARGET_RATIO")
    private BigDecimal targetRatio;

    @TableField("DEVIATION")
    private BigDecimal deviation;

    @TableField("YIELD_RATE")
    private BigDecimal yieldRate;

    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    @TableField("ALLOCATION_STATUS")
    private String allocationStatus;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("REGION")
    private String region;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("ADJUST_SUGGESTION")
    private String adjustSuggestion;

    @TableField("REPORT_DATE")
    private LocalDate reportDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
