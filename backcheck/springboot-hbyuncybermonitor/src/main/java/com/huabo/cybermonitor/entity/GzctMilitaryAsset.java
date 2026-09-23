package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_ASSET")
public class GzctMilitaryAsset extends Model<GzctMilitaryAsset> {

    @TableId(value = "ASSET_ID", type = IdType.ASSIGN_UUID)
    private String assetId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("ASSET_NAME")
    private String assetName;

    @TableField("ASSET_TYPE")
    private String assetType;

    @TableField("ASSET_CODE")
    private String assetCode;

    @TableField("ORIGINAL_VALUE")
    private BigDecimal originalValue;

    @TableField("NET_VALUE")
    private BigDecimal netValue;

    @TableField("DEPRECIATION_RATE")
    private BigDecimal depreciationRate;

    @TableField("PURCHASE_DATE")
    private LocalDate purchaseDate;

    @TableField("ASSET_STATUS")
    private String assetStatus;

    @TableField("LOCATION")
    private String location;

    @TableField("SECRET_LEVEL")
    private String secretLevel;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
