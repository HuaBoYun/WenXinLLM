package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_SUPPLY")
public class GzctEnterpriseSupply extends Model<GzctEnterpriseSupply> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("SUPPLIER_TYPE")
    private String supplierType;

    // 前端表格展示字段
    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    @TableField("CONTACT_PERSON")
    private String contactPerson;

    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @TableField("ADDRESS")
    private String address;

    @TableField("RATING")
    private String rating;

    @TableField("COOPERATION_YEARS")
    private Integer cooperationYears;

    // totalAmount 对应 SUPPLY_AMOUNT
    @TableField("SUPPLY_AMOUNT")
    private java.math.BigDecimal supplyAmount;

    // 前端展示用别名字段
    @TableField(exist = false)
    private java.math.BigDecimal totalAmount;

    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @TableField("DELIVERY_SCORE")
    private BigDecimal deliveryScore;

    @TableField("PRICE_SCORE")
    private BigDecimal priceScore;

    @TableField("OVERALL_SCORE")
    private BigDecimal overallScore;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
