package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 三表比对实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_THREE_TABLE_COMPARE")
public class TblThreeTableCompare {

    @TableId(value = "COMPARE_ID", type = IdType.ASSIGN_UUID)
    private String compareId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("PROPERTY_REG_STATUS")
    private String propertyRegStatus;

    @TableField("BUSINESS_REG_STATUS")
    private String businessRegStatus;

    @TableField("FINANCIAL_CONSOLIDATION")
    private String financialConsolidation;

    @TableField("PROPERTY_RATIO")
    private BigDecimal propertyRatio;

    @TableField("BUSINESS_RATIO")
    private BigDecimal businessRatio;

    @TableField("FINANCIAL_RATIO")
    private BigDecimal financialRatio;

    @TableField("IS_CONSISTENT")
    private String isConsistent;

    @TableField("INCONSISTENCY_TYPE")
    private String inconsistencyType;

    @TableField("INCONSISTENCY_DESC")
    private String inconsistencyDesc;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("COMPARE_DATE")
    private LocalDate compareDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
