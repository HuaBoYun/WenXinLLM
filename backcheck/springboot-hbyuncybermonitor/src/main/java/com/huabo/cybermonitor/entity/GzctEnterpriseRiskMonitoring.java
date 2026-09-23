package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_RISK_MONITORING")
public class GzctEnterpriseRiskMonitoring extends Model<GzctEnterpriseRiskMonitoring> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("MONITOR_NAME")
    private String monitorName;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("INDICATOR_NAME")
    private String indicatorName;

    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue;

    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @TableField("IS_TRIGGERED")
    private String isTriggered;

    @TableField("TRIGGER_TIME")
    private LocalDateTime triggerTime;

    @TableField("MONITOR_FREQUENCY")
    private String monitorFrequency;

    @TableField("STATUS")
    private String status;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
