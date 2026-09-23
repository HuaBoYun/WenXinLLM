package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产映射实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ASSET_MAPPING")
public class TblAssetMapping {

    @TableId(value = "MAPPING_ID", type = IdType.ASSIGN_UUID)
    private String mappingId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("ASSET_CATEGORY")
    private String assetCategory;

    @TableField("ASSET_NAME")
    private String assetName;

    @TableField("ASSET_VALUE")
    private BigDecimal assetValue;

    @TableField("BOOK_VALUE")
    private BigDecimal bookValue;

    @TableField("FAIR_VALUE")
    private BigDecimal fairValue;

    @TableField("LOCATION")
    private String location;

    @TableField("CUSTODIAN")
    private String custodian;

    @TableField("MAPPING_STATUS")
    private String mappingStatus;

    @TableField("LAST_CHECK_DATE")
    private LocalDate lastCheckDate;

    @TableField("DISCREPANCY_TYPE")
    private String discrepancyType;

    @TableField("DISCREPANCY_AMOUNT")
    private BigDecimal discrepancyAmount;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
