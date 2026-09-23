package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_RISK_INCIDENT")
public class GzctEnterpriseRiskIncident extends Model<GzctEnterpriseRiskIncident> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("INCIDENT_NAME")
    private String incidentName;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("SEVERITY")
    private String severity;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("IMPACT_DESC")
    private String impactDesc;

    @TableField("LOSS_AMOUNT")
    private BigDecimal lossAmount;

    @TableField("OCCUR_TIME")
    private LocalDateTime occurTime;

    @TableField("DISCOVER_TIME")
    private LocalDateTime discoverTime;

    @TableField("HANDLER")
    private String handler;

    @TableField("HANDLE_STATUS")
    private String handleStatus;

    @TableField("HANDLE_RESULT")
    private String handleResult;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
