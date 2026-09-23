package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_OPERATION")
public class GzctEnterpriseOperation extends Model<GzctEnterpriseOperation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("PRODUCTION_VALUE")
    private BigDecimal productionValue;

    @TableField("SALES_REVENUE")
    private BigDecimal salesRevenue;

    @TableField("ORDER_AMOUNT")
    private BigDecimal orderAmount;

    @TableField("CAPACITY_UTILIZATION")
    private BigDecimal capacityUtilization;

    @TableField("PRODUCT_QUALITY_RATE")
    private BigDecimal productQualityRate;

    @TableField("DELIVERY_RATE")
    private BigDecimal deliveryRate;

    @TableField("INVENTORY_TURNOVER")
    private BigDecimal inventoryTurnover;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
