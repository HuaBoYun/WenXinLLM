package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应链风险管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SUPPLY_RISK")
public class GzctSupplyRisk extends Model<GzctSupplyRisk> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("RISK_NO")
    private String riskNo;

    @TableField("RISK_NAME")
    private String riskName;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("RISK_TYPE")
    private String riskType;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("RISK_SCORE")
    private Integer riskScore;

    @TableField("RISK_DESCRIPTION")
    private String riskDescription;

    @TableField("CONTROL_MEASURE")
    private String controlMeasure;

    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @TableField("IDENTIFY_DATE")
    private LocalDate identifyDate;

    @TableField("STATUS")
    private String status;

    @TableField("HANDLE_RESULT")
    private String handleResult;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
