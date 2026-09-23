package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_FINANCIAL")
public class GzctEnterpriseFinancial extends Model<GzctEnterpriseFinancial> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("REPORT_TYPE")
    private String reportType;

    @TableField("REPORT_PERIOD")
    private String reportPeriod;

    @TableField("REPORT_YEAR")
    private String reportYear;

    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    @TableField("TOTAL_LIABILITIES")
    private BigDecimal totalLiabilities;

    @TableField("NET_ASSETS")
    private BigDecimal netAssets;

    @TableField("OPERATING_REVENUE")
    private BigDecimal operatingRevenue;

    @TableField("NET_PROFIT")
    private BigDecimal netProfit;

    @TableField("CASH_FLOW")
    private BigDecimal cashFlow;

    @TableField("STATUS")
    private String status;

    @TableField("SUBMIT_TIME")
    private LocalDateTime submitTime;

    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
