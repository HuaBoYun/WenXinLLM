package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_SUPPLIER")
public class GzctProcSupplier extends Model<GzctProcSupplier> {
    @TableId(value = "SUPPLIER_ID", type = IdType.ASSIGN_UUID)
    private String supplierId;
    @TableField("SUPPLIER_CODE")
    private String supplierCode;
    @TableField("SUPPLIER_NAME")
    private String supplierName;
    @TableField("SUPPLIER_TYPE")
    private String supplierType;
    @TableField("QUALIFICATION_LEVEL")
    private String qualificationLevel;
    @TableField("REGION")
    private String region;
    @TableField("TOTAL_PURCHASE")
    private BigDecimal totalPurchase;
    @TableField("PURCHASE_RATIO")
    private BigDecimal purchaseRatio;
    @TableField("IS_RELATED")
    private String isRelated;
    @TableField("RELATED_COMPANY")
    private String relatedCompany;
    @TableField("BLACKLIST_STATUS")
    private String blacklistStatus;
    @TableField("CREDIT_RATING")
    private String creditRating;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("LAST_AUDIT_DATE")
    private LocalDate lastAuditDate;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
